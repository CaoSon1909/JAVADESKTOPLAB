/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sonpc.daos.TblItemDAO;
import sonpc.daos.TblSupDAO;
import sonpc.daos.TblUsersDAO;
import sonpc.dtos.TblItemDTO;
import sonpc.dtos.TblSupDTO;
import sonpc.dtos.TblUsersDTO;
import sonpc.models.ItemTableModel;
import sonpc.models.SupTableModel;
import sonpc.validators.ItemValidator;
import sonpc.validators.SupValidator;

/**
 *
 * @author ACER
 */
public class MainForm extends javax.swing.JFrame {

    private String[] HEADERS_SUP = {"Code", "Name", "Address"};
    private String[] HEADERS_ITEM = {"Code", "Name", "Supplier", "Unit", "Price", "Supplying"};
    private int[] INDEXES_SUP = {1, 2, 3};
    private int[] INDEXES_ITEM = {1, 2, 3, 4, 5, 6};

    private SupTableModel supModel = new SupTableModel(HEADERS_SUP, INDEXES_SUP);
    private ItemTableModel itemModel = new ItemTableModel(HEADERS_ITEM, INDEXES_ITEM);

    //Logger
    private final sonpc.errors.Logger writeLog = new sonpc.errors.Logger();
    private List<String> exceptionList = new ArrayList<>();

    /**
     * Creates new form viewClient
     */
    public MainForm() {
        initComponents();
        loadComboBox();
        updateUIItem();
        updateUISupplier();
        setUnableItem();
        setUnableSupplier();
    }

    private void setUnableSupplier() {
        txtSupCode.setEnabled(false);
        txtSupName.setEnabled(false);
        txtSupAddr.setEnabled(false);
    }

    private void setUnableItem() {
        txtItemCode.setEnabled(false);
        txtItemName.setEnabled(false);
        txtUnit.setEnabled(false);
        txtPrice.setEnabled(false);
    }

    private void setEnableSupplier() {
        txtSupCode.setEnabled(true);
        txtSupName.setEnabled(true);
        txtSupAddr.setEnabled(true);
    }

    private void setEnableItem() {
        txtItemCode.setEnabled(true);
        txtItemName.setEnabled(true);
        txtUnit.setEnabled(true);
        txtPrice.setEnabled(true);
    }

    private void setTextSupplier(TblSupDTO dto) {
        if (dto != null) {
            // get fields
            String supCode = dto.getSupCode();
            String supName = dto.getSupName();
            String address = dto.getSupAddr();
            boolean collaborating = dto.isIsColla();
            // set text
            txtSupCode.setText(supCode);
            txtSupName.setText(supName);
            txtSupAddr.setText(address);
            chkColla.setSelected(collaborating);
        }
    }

    private void setTextItem(TblItemDTO dto) {
        if (dto != null) {
            try {
                //get fields
                String itemCode = dto.getItemCode();
                String itemName = dto.getItemName();
                String supCode = dto.getSupCode();
                String unit = dto.getUnit();
                float price = dto.getPrice();
                boolean isSupplying = dto.isIsSupplying();
                //set text
                txtItemCode.setText(itemCode);
                txtItemName.setText(itemName);

                //ý tưởng : combobox sẽ set những items bên trong dựa theo index của từng items
                //=> gọi dao để lấy ra supDTO dựa vào supCode. Từ supDTO đó mới lấy ra index của nó trong toàn bộ list supDTO
                TblSupDAO supDAO = new TblSupDAO();
                //getSupDTOBySupCode
                TblSupDTO supDTO = supDAO.getSupDTOBySupCode(supCode);
                //getIndexSupDTO
                int index = supDAO.getIndexSupDTO(supDTO);
                //set selected index
                cbSup.setSelectedIndex(index);

                txtUnit.setText(unit);
                txtPrice.setText(price + "");

                chkSup.setSelected(isSupplying);

            } catch (ClassNotFoundException | SQLException ex) {
                exceptionList.add("Main Form - setTextItem() : " + ex);
                writeLog.writeFile(exceptionList);
            }

        }
    }

    private void loadComboBox() {
        try {
            //ý tưởng : getAllSups lên rồi addItem vào combobox
            //lưu ý: trước khi addItem phải removeAllItems cũ ra khỏi combobox
            cbSup.removeAllItems();
            TblSupDAO supDAO = new TblSupDAO();
            List<TblSupDTO> list = supDAO.getAllSups();
            if (list != null) {
                for (TblSupDTO dto : list) {
                    cbSup.addItem(dto.getSupCode() + "-" + dto.getSupName());
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - loadComboBox() :" + ex);
            writeLog.writeFile(exceptionList);
        }
    }

    private TblSupDTO getTextSupDTO() {
        String supCode = txtSupCode.getText().trim();
        String supName = txtSupName.getText().trim();
        String address = txtSupAddr.getText().trim();
        boolean collaborating = chkColla.isSelected();

        TblSupDTO dto = new TblSupDTO(supCode, supName, address, collaborating);
        return dto;
    }

    private TblItemDTO getTextItemDTO() {
        String itemCode = txtItemCode.getText().trim();
        String itemName = txtItemName.getText().trim();
        TblItemDAO itemDAO = new TblItemDAO();
        String supplier = (String) cbSup.getSelectedItem();
        String tmp[] = supplier.split("-");
        String supCode = tmp[0];
        String unit = txtUnit.getText().trim();
        String priceString = txtPrice.getText().trim();
        float price = Float.parseFloat(priceString); // nhớ bắt lỗi numberformat exception
        boolean isSupplying = chkSup.isSelected();
        TblItemDTO itemDTO = new TblItemDTO(itemCode, itemName, supCode, unit, price, isSupplying);
        return itemDTO;
    }

    private void resetFieldsSupplier() {
        txtSupCode.setText("");
        txtSupName.setText("");
        txtSupAddr.setText("");
        chkColla.setSelected(false);
    }

    private void resetFieldsItem() {
        txtItemCode.setText("");
        txtItemName.setText("");
        txtUnit.setText("");
        txtPrice.setText("");

    }

    private void updateUISupplier() {
        try {
            supModel = new SupTableModel(HEADERS_SUP, INDEXES_SUP);
            TblSupDAO supDAO = new TblSupDAO();
            List<TblSupDTO> list = supDAO.getAllSups();
            supModel.loadData(list);
            tblSup.setModel(supModel);
            tblSup.updateUI();
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - updateUISupplier() : " + ex);
            writeLog.writeFile(exceptionList);
        }
    }

    private void updateUIItem() {
        try {
            itemModel = new ItemTableModel(HEADERS_ITEM, INDEXES_ITEM);
            TblItemDAO itemDAO = new TblItemDAO();
            List<TblItemDTO> list = itemDAO.getAllItems();
            itemModel.loadData(list);
            tblItem.setModel(itemModel);
            tblItem.updateUI();
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - updateUIItem():" + ex);
            writeLog.writeFile(exceptionList);
        }
    }

    private boolean updateSupplier() {
        try {
            TblSupDTO supDTO = getTextSupDTO();
            //validate
            SupValidator validator = new SupValidator();
            boolean checkValidSupName = validator.checkRegexSupName(supDTO.getSupName());
            boolean checkValidSupAddr = validator.checkRegexSupAddr(supDTO.getSupAddr());
            //
            //if
            if (!checkValidSupName){
                JOptionPane.showMessageDialog(this, "Sup Name have length [3-15] characters");
                return false;
            }
            if (!checkValidSupAddr){
                JOptionPane.showMessageDialog(this, "Sup Address have length [3-30] characters");
                return false;
            }
            //
            //update
            if (checkValidSupName && checkValidSupAddr) {
                TblSupDAO supDAO = new TblSupDAO();
                boolean result = supDAO.updateSupplier(supDTO);
                if (result) {
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - updateSupplier() :" + ex);
            writeLog.writeFile(exceptionList);
        }
        return false;
    }

    private boolean saveSupplier() {
        try {
            TblSupDTO supDTO = getTextSupDTO();
            //validate
            SupValidator validator = new SupValidator();
            boolean checkDupplicateSupCode = validator.checkDupplicateSupCode(supDTO.getSupCode());
            boolean checkValidSupCode = validator.checkRegexSupCode(supDTO.getSupCode());
            boolean checkValidSupName = validator.checkRegexSupName(supDTO.getSupName());
            boolean checkValidSupAddr = validator.checkRegexSupAddr(supDTO.getSupAddr());
            //
            //if...
            if (checkDupplicateSupCode){
                JOptionPane.showMessageDialog(this, "Duplicated SupCode");
                return false;
            }
            if (!checkValidSupCode){
                JOptionPane.showMessageDialog(this, "SupCode must not contain special characters and have length [3-15] characters");
                return false;
            }
            if (!checkValidSupName){
                JOptionPane.showMessageDialog(this, "SupName have length [3-15] characters");
                return false;
            }
            if (!checkValidSupAddr){
                JOptionPane.showMessageDialog(this, "Sup Address have length [3-30] characters");
                return false;
            }
            //
            //save
            if (checkDupplicateSupCode == false && checkValidSupCode && checkValidSupName && checkValidSupAddr) {
                TblSupDAO supDAO = new TblSupDAO();
                boolean result = supDAO.addSupplier(supDTO);
                if (result) {
                    return true;
                }
            }
        } catch (ClassNotFoundException ex) {
            exceptionList.add("Main Form - saveSupplier(): " + ex);
            writeLog.writeFile(exceptionList);
        } catch (SQLException ex) {
            exceptionList.add("Main Form - saveSupplier(): " + ex);
            writeLog.writeFile(exceptionList);
            JOptionPane.showMessageDialog(this, "Duplicate Supplier Code");
            return false;
        }
        return false;
    }

    private boolean deleteSupplier() {
        try {
            TblSupDTO supDTO = getTextSupDTO();
            boolean checkAvailable = checkSupplierStillAvailable(supDTO.getSupCode());
            if (!checkAvailable) {
                TblSupDAO supDAO = new TblSupDAO();
                boolean result = supDAO.deleteSupplier(supDTO);
                if (result) {
                    return true;
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - deleteSupplier(): " + ex);
            writeLog.writeFile(exceptionList);
        }
        return false;
    }

    private boolean checkSupplierStillAvailable(String supplierCode) {
        try {
            TblItemDAO itemDAO = new TblItemDAO();
            List<TblItemDTO> itemList = itemDAO.getAllItems();
            for (TblItemDTO dto : itemList) {
                String supCode = itemDAO.getSupCodeByItemCode(dto.getItemCode());
                if (supplierCode.equals(supCode)) {
                    return true;
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - checkSupplierStillAvailable(): " + ex);
            writeLog.writeFile(exceptionList);
        }
        return false;
    }

    private boolean isSelectingRowSupplier() {
        int rowSelect = tblSup.getSelectedRow();
        if (rowSelect >= 0) {
            return true;
        }
        return false;
    }

    private boolean isSelectingRowItem() {
        int rowSelect = tblItem.getSelectedRow();
        if (rowSelect >= 0) {
            return true;
        }
        return false;
    }

    private boolean updateItem() {
        try {
            TblItemDTO itemDTO = getTextItemDTO();
            //validate
            ItemValidator validator = new ItemValidator();
            boolean checkRegexItemName = validator.checkRegexItemName(itemDTO.getItemName());
            boolean checkRegexUnit = validator.checkRegexUnit(itemDTO.getUnit());
            boolean checkRegexPrice = validator.checkRegexPrice(itemDTO.getPrice() + "");
            //
            //if
            if (!checkRegexItemName){
                JOptionPane.showMessageDialog(this, "Item Name have length [3-15] characters");
                return false;
            }
            if (!checkRegexUnit){
                JOptionPane.showMessageDialog(this, "Item Unit have length [1-10] characters");
                return false;
            }
            if (!checkRegexPrice){
                JOptionPane.showMessageDialog(this, "Price must be a float number and > 0");
                return false;
            }
            //
            //update
            if (checkRegexItemName && checkRegexUnit && checkRegexPrice) {
                TblItemDAO itemDAO = new TblItemDAO();
                boolean result = itemDAO.updateItem(itemDTO);
                if (result) {
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - updateItem(): " + ex);
            writeLog.writeFile(exceptionList);
        } catch (NumberFormatException ex) {
            exceptionList.add("Main Form - updateItem(): " + ex);
            writeLog.writeFile(exceptionList);
            JOptionPane.showMessageDialog(this, "Wrong Number Format");
            return false;
        }
        return false;
    }

    private boolean saveItem() {
        try {
            TblItemDTO itemDTO = getTextItemDTO();
            //validate
            ItemValidator validator = new ItemValidator();
            boolean checkDupplicateItemCode = validator.checkDupplicateItemCode(itemDTO.getItemCode());
            boolean checkRegexItemCode = validator.checkRegexItemCode(itemDTO.getItemCode());
            boolean checkRegexItemName = validator.checkRegexItemName(itemDTO.getItemName());
            boolean checkRegexUnit = validator.checkRegexUnit(itemDTO.getUnit());
            boolean checkRegexPrice = validator.checkRegexPrice(itemDTO.getPrice() + "");
            //
            //if..
            if (checkDupplicateItemCode){
                JOptionPane.showMessageDialog(this, "Duplicate Item Code");
                return false;
            }
            if (!checkRegexItemCode){
                JOptionPane.showMessageDialog(this, "Item Code not contains special characters and Have Length [3-15] characters ");
                return false;
            }
            if (!checkRegexItemName){
                JOptionPane.showMessageDialog(this, "Item Name Have Length [3-15] characters ");
                return false;
            }
            if (!checkRegexUnit){
                JOptionPane.showMessageDialog(this, "Unit Have Length [1-10] characters");
                return false;
            }
            if (!checkRegexPrice){
                JOptionPane.showMessageDialog(this, "Price is in a float number and > 0");
                return false;
            }
            //
            //save
            if (checkDupplicateItemCode == false && checkRegexItemCode && checkRegexItemName && checkRegexUnit && checkRegexPrice) {
                TblItemDAO itemDAO = new TblItemDAO();
                boolean result = itemDAO.addItem(itemDTO);
                if (result) {
                    return true;
                }
            }
        } catch (ClassNotFoundException ex) {
            exceptionList.add("Main Form - saveItem(): " + ex);
            writeLog.writeFile(exceptionList);
        } catch (SQLException ex) {
            exceptionList.add("Main Form - saveItem(): " + ex);
            writeLog.writeFile(exceptionList);
            JOptionPane.showMessageDialog(this, "Duplicate Item Code");
            return false;
        } catch (NumberFormatException ex) {
            exceptionList.add("Main Form - saveItem(): " + ex);
            writeLog.writeFile(exceptionList);
            JOptionPane.showMessageDialog(this, "Wrong Number Format");
            return false;
        }
        return false;
    }

    private boolean deleteItem() {
        try {
            TblItemDTO itemDTO = getTextItemDTO();
            TblItemDAO itemDAO = new TblItemDAO();
            boolean deleteResult = itemDAO.deleteItem(itemDTO);
            if (deleteResult) {
                return true;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form -  deleteItem(): " + ex);
            writeLog.writeFile(exceptionList);
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        txtUnit = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        chkSup = new javax.swing.JCheckBox();
        cbSup = new javax.swing.JComboBox<>();
        btnItemAdd = new javax.swing.JButton();
        btnItemSave = new javax.swing.JButton();
        btnItemDel = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItem = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSup = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSupCode = new javax.swing.JTextField();
        txtSupName = new javax.swing.JTextField();
        txtSupAddr = new javax.swing.JTextField();
        chkColla = new javax.swing.JCheckBox();
        btnSupAdd = new javax.swing.JButton();
        btnSupSave = new javax.swing.JButton();
        btnSupDel = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtWelcome = new javax.swing.JTextField();

        jRadioButton1.setText("jRadioButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel6.setText("Item Code:");

        jLabel7.setText("Item Name:");

        jLabel8.setText("Supplier:");

        jLabel9.setText("Unit:");

        jLabel10.setText("Price:");

        jLabel11.setText("Supplying:");

        cbSup.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnItemAdd.setText("Add New");
        btnItemAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemAddActionPerformed(evt);
            }
        });

        btnItemSave.setText("Save");
        btnItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemSaveActionPerformed(evt);
            }
        });

        btnItemDel.setText("Delete");
        btnItemDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemDelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(chkSup))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtItemName)
                                        .addComponent(cbSup, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtUnit)))))
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnItemAdd)
                        .addGap(26, 26, 26)
                        .addComponent(btnItemSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnItemDel)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(chkSup))
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnItemAdd)
                    .addComponent(btnItemSave)
                    .addComponent(btnItemDel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblItem);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Item", jPanel2);

        tblSup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSup);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
        );

        jLabel1.setText("Code:");

        jLabel2.setText("Name:");

        jLabel3.setText("Address:");

        jLabel4.setText("Collaborating:");

        btnSupAdd.setText("Add New");
        btnSupAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupAddActionPerformed(evt);
            }
        });

        btnSupSave.setText("Save");
        btnSupSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupSaveActionPerformed(evt);
            }
        });

        btnSupDel.setText("Delete");
        btnSupDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupDelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(chkColla)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSupCode)
                            .addComponent(txtSupName)
                            .addComponent(txtSupAddr))))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btnSupAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSupSave)
                .addGap(18, 18, 18)
                .addComponent(btnSupDel)
                .addGap(0, 36, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtSupCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addComponent(jLabel2))
                    .addComponent(txtSupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtSupAddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(chkColla))
                .addGap(48, 48, 48)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSupAdd)
                    .addComponent(btnSupSave)
                    .addComponent(btnSupDel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Supplier", jPanel1);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 51, 51));
        jLabel5.setText("Item Management");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(txtWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(130, 130, 130))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel5))
                    .addComponent(txtWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemMouseClicked
        setEnableItem();
        txtItemCode.setEditable(false);
        int rowSelect = tblItem.getSelectedRow();
        if (rowSelect < 0) {
            return;
        } else {
            TblItemDTO itemDTO = itemModel.getList().get(rowSelect);
            setTextItem(itemDTO);
        }

    }//GEN-LAST:event_tblItemMouseClicked

    private void tblSupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupMouseClicked
        setEnableSupplier();
        txtSupCode.setEditable(false);
        int rowSelect = tblSup.getSelectedRow();
        if (rowSelect < 0) {
            return;
        } else {
            TblSupDTO supDTO = supModel.getList().get(rowSelect);
            setTextSupplier(supDTO);
        }
    }//GEN-LAST:event_tblSupMouseClicked

    private void btnSupAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupAddActionPerformed
        setEnableSupplier();
        resetFieldsSupplier();
        txtSupCode.setEditable(true);
        updateUISupplier();
    }//GEN-LAST:event_btnSupAddActionPerformed

    private void btnSupSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupSaveActionPerformed
        boolean result = isSelectingRowSupplier();
        if (result) {
            //update
            boolean updateResult = updateSupplier();
            if (updateResult) {
                JOptionPane.showMessageDialog(this, "Update Success");
                updateUISupplier();
                loadComboBox();
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed");
                return;
            }
        } else {
            //save
            boolean saveResult = saveSupplier();
            if (saveResult) {
                JOptionPane.showMessageDialog(this, "Save Successful");
                updateUISupplier();
                loadComboBox();
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Save Failed");
                return;
            }
        }
    }//GEN-LAST:event_btnSupSaveActionPerformed

    private void btnItemAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemAddActionPerformed
        setEnableItem();
        resetFieldsItem();
        txtItemCode.setEditable(true);
        updateUIItem();

    }//GEN-LAST:event_btnItemAddActionPerformed

    private void btnSupDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupDelActionPerformed
        boolean result = isSelectingRowSupplier();
        if (result) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to delete this Supplier?");
            if (choice == 0) {

                boolean deleteResult = deleteSupplier();
                if (deleteResult) {
                    JOptionPane.showMessageDialog(this, "Delete Successful");
                    updateUISupplier();
                    loadComboBox();
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "Delete Failed. You need to delete all items that belong to this supplier first");
                    return;
                }
            }
        } else {
            return;
        }
    }//GEN-LAST:event_btnSupDelActionPerformed

    private void btnItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemSaveActionPerformed
        boolean result = isSelectingRowItem();
        if (result) {
            //update
            boolean updateResult = updateItem();
            if (updateResult) {
                JOptionPane.showMessageDialog(this, "Update Successful");
                updateUIItem();
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed");
                return;
            }
        } else {
            //save
            boolean saveResult = saveItem();
            if (saveResult) {
                JOptionPane.showMessageDialog(this, "Save Successful");
                updateUIItem();
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Save Failed");
                return;
            }
        }
    }//GEN-LAST:event_btnItemSaveActionPerformed

    private void btnItemDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemDelActionPerformed
        boolean result = isSelectingRowItem();
        if (result) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to delete this Supplier?");
            if (choice == 0) {
                boolean deleteResult = deleteItem();
                if (deleteResult) {
                    JOptionPane.showMessageDialog(this, "Delete Successful");
                    updateUIItem();
                    btnSupAddActionPerformed(evt);
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "Delete Failed");
                    return;
                }
            }
        } else {
            return;
        }
    }//GEN-LAST:event_btnItemDelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnItemAdd;
    private javax.swing.JButton btnItemDel;
    private javax.swing.JButton btnItemSave;
    private javax.swing.JButton btnSupAdd;
    private javax.swing.JButton btnSupDel;
    private javax.swing.JButton btnSupSave;
    private javax.swing.JComboBox<String> cbSup;
    private javax.swing.JCheckBox chkColla;
    private javax.swing.JCheckBox chkSup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblItem;
    private javax.swing.JTable tblSup;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtSupAddr;
    private javax.swing.JTextField txtSupCode;
    private javax.swing.JTextField txtSupName;
    private javax.swing.JTextField txtUnit;
    private javax.swing.JTextField txtWelcome;
    // End of variables declaration//GEN-END:variables
}

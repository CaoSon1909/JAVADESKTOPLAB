/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.tablemodel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import sonpc.dtos.ArmorDTO;

/**
 *
 * @author ACER
 */
public class ArmorTableModel extends AbstractTableModel{

    String[] headers = null;
    int[] indexes = null;
    List<ArmorDTO> list = new Vector<>();

    
    public ArmorTableModel(String[] headers, int[] indexes){
        this.headers=headers;
        this.indexes=indexes;
    }
    
     public void loadData(List<ArmorDTO> list){
        if (list != null){
            for (ArmorDTO dto : list){
                this.list.add(dto);
            }
        }
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return indexes.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArmorDTO dto = list.get(rowIndex);
        switch(indexes[columnIndex]){
            case 1:
              return  dto.getArmorID();
            case 2:
               return dto.getClassification();
            case 3:
                return dto.getDescription();
            case 4:
               return dto.getStatus();
            case 5:
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String date = format.format(dto.getTimeOfCreate());
                return date;
            case 6:
                return dto.getDefense();
        }
        return null;
    }
    
    public List<ArmorDTO> getData(){
        return list;
    }

    @Override
    public String getColumnName(int column) {
        String columnName = headers[column];
//        switch(column){
//            case 0:
//                columnName = "3";
//                break;
//            case 1:
//                columnName = "Classification";
//                break;
//            case 2:
//                columnName = "TimeOfCreate";
//                break;
//            case 3:
//                columnName = "Defense";
//                break;
//        }
        return columnName;
    }


    
    
    

   
    
}

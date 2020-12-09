/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.models;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import sonpc.dtos.TblItemDTO;

/**
 *
 * @author ACER
 */
public class ItemTableModel extends AbstractTableModel{

    String[] headers;
    int[] indexes;
    List<TblItemDTO> list = new Vector<>();

    public List<TblItemDTO> getList() {
        return list;
    }

    
    public ItemTableModel(String[] headers, int[] indexes) {
        this.headers = headers;
        this.indexes = indexes;
    }
    
    public void loadData(List<TblItemDTO> list){
        if (list != null){
            for(TblItemDTO dto : list){
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
        TblItemDTO itemDTO = this.list.get(rowIndex);
        switch(indexes[columnIndex]){
            case 1: return itemDTO.getItemCode();
            case 2: return itemDTO.getItemName();
            case 3: return itemDTO.getSupCode();
            case 4: return itemDTO.getUnit();
            case 5: return itemDTO.getPrice();
            case 6: return itemDTO.isIsSupplying();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        String columnName = headers[column];
        return columnName;
    }
    
    
}

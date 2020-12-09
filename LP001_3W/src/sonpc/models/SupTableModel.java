/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.models;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import sonpc.dtos.TblSupDTO;

/**
 *
 * @author ACER
 */
public class SupTableModel extends AbstractTableModel{
    
    String headers[];
    int indexes[];
    List<TblSupDTO> list = new Vector<>();

    public List<TblSupDTO> getList() {
        return list;
    }

    
    
    public SupTableModel() {
    }

    public SupTableModel(String[] headers, int[] indexes) {
        this.headers = headers;
        this.indexes = indexes;
    }
    
    public void loadData(List<TblSupDTO> list){
        if (list != null){
            for (TblSupDTO dto : list) {
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
        TblSupDTO dto = this.list.get(rowIndex);
        switch(indexes[columnIndex]){
            case 1: return dto.getSupCode();
            case 2: return dto.getSupName();
            case 3: return dto.getSupAddr();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        String columnName = headers[column];
        return columnName;
    }
    
    
    
}

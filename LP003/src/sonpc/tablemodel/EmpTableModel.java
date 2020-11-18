/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.tablemodel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import sonpc.dtos.EmpDTO;

/**
 *
 * @author ACER
 */
public class EmpTableModel extends AbstractTableModel{

    private String[] headers;
    private int[] indexes;
    List<EmpDTO> list = new ArrayList<>();

    public EmpTableModel(String[] headers, int[] indexes) {
        this.headers = headers;
        this.indexes = indexes;
    }
    
    
    
    public void loadData(List<EmpDTO> list){
        for(EmpDTO dto : list){
            if (list != null){
                this.list.add(dto);
            }
        }
    }
    
    public List<EmpDTO> getData(){
        return this.list;
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
        EmpDTO dto = this.list.get(rowIndex);
        switch(indexes[columnIndex]){
            case 1: return dto.getEmpID();
            case 2: return dto.getFullName();
            case 3: return dto.getPhone();
            case 4: return dto.getEmail();
            case 5: return dto.getAddress();
            case 6: return dto.getDateOfBirth();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }
    
    
}

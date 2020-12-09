/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.validators;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import sonpc.daos.TblSupDAO;
import sonpc.dtos.TblSupDTO;

/**
 *
 * @author ACER
 */
public class SupValidator implements Serializable{
    
    public boolean checkDupplicateSupCode(String supCode) throws ClassNotFoundException, SQLException{
        TblSupDAO supDAO = new TblSupDAO();
        List<TblSupDTO> supList = supDAO.getAllSups();
        for (TblSupDTO dto : supList){
            if (dto.getSupCode().equals(supCode)){
                return true;
            }
        }
        return false;
    }
    
    public boolean checkRegexSupCode(String supCode){
        String pattern = "[a-zA-Z0-9]+{3,15}";
        boolean result = supCode.matches(pattern);
        if (result){
            return true;
        }
        return false;
    }
    
    public boolean checkRegexSupName(String supName){
        if (supName.isEmpty() || supName.length() < 3 || supName.length() > 15){
            return false;
        }
        return true;
    }
    
    public boolean checkRegexSupAddr(String supAddr){
        if (supAddr.isEmpty() || supAddr.length() < 3 || supAddr.length() > 30){
            return false;
        }
        return true;
    }
    
    
}

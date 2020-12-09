/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.validators;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import sonpc.daos.TblItemDAO;
import sonpc.dtos.TblItemDTO;

/**
 *
 * @author ACER
 */
public class ItemValidator implements Serializable{
    
    public boolean checkDupplicateItemCode(String itemCode) throws ClassNotFoundException, SQLException{
        TblItemDAO itemDAO = new TblItemDAO();
        List<TblItemDTO> listItem = itemDAO.getAllItems();
        for (TblItemDTO dto : listItem){
            if (dto.getItemCode().equals(itemCode)){
                return true;
            }
        }
        return false;
    }
    
    public boolean checkRegexItemCode(String itemCode){
        String pattern = "[a-zA-Z0-9]+{3,15}";
        if (!itemCode.matches(pattern)){
            return false;
        }
        return true;
    }
    
    public boolean checkRegexItemName(String itemName){
        if (itemName.isEmpty() || itemName.length() < 3 || itemName.length() > 15){
            return false;
        }
        return true;
    }
    
    public boolean checkRegexUnit(String unit){
        if (unit.isEmpty() || unit.length() > 10){
            return false;
        }
        return true;
    }
    
    public boolean checkRegexPrice(String priceString){
        String priceStringModified = priceString.trim();
        if (priceStringModified.isEmpty() || priceStringModified.length() > 5){
            return false;
        }
//        for (int i = 0 ; i < priceStringModified.length() ; i++){
//            char c = priceStringModified.charAt(i);
//            if (c < '0' || c > '9'){
//                return false;
//            }
//        }
        float price = Float.parseFloat(priceStringModified);
        if (price <= 0){
            return false;
        }
        return true;
    }
    
}

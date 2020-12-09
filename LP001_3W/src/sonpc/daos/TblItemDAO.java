/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.daos;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import sonpc.dtos.TblItemDTO;
import sonpc.utils.DBHelpers;

/**
 *
 * @author ACER
 */
public class TblItemDAO implements Serializable{
    
    public List<TblItemDTO> getAllItems() throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TblItemDTO> list = null;
        try{
            con = DBHelpers.makeConnection();
            if (con != null){
                String sql = "Select itemCode, itemName, unit, price, supplying, supCode From tblItems";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()){
                    String itemCode = rs.getString("itemCode");
                    String itemName = rs.getString("itemName");
                    String unit = rs.getString("unit");
                    float price = rs.getFloat("price");
                    boolean isSuppyling = rs.getBoolean("supplying");
                    String supCode = rs.getString("supCode");
                    TblItemDTO dto  = new TblItemDTO(itemCode, itemName, supCode, unit, price, isSuppyling);
                    if (list == null){
                        list =  new Vector<>();
                    }
                    list.add(dto);
                }
                return list;
            }
        }
        finally{
            if (rs != null){
                rs.close();
            }
            if (ps != null){
                ps.close();
            }
            if (con != null){
                con.close();
            }
        }
        return null;
    }
    
    public String getSupCodeByItemCode(String itemCode) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = DBHelpers.makeConnection();
            if (con != null){
                String sql = "Select supCode From tblItems Where itemCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, itemCode);
                rs = ps.executeQuery();
                if (rs.next()){
                    return rs.getString("supCode");
                }
            }
        }
        finally{
            if (rs != null){
                rs.close();
            }
            if (ps != null){
                ps.close();
            }
            if (con != null){
                con.close();
            }
        }
        return null;
    }
    
    public boolean updateItem(TblItemDTO dto) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = DBHelpers.makeConnection();
            if (con != null){
                String sql = "Update tblItems Set itemName = ?, unit = ?, price = ?, supplying = ? , supCode = ? Where itemCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getItemName());
                ps.setString(2, dto.getUnit());
                ps.setFloat(3, dto.getPrice());
                ps.setBoolean(4, dto.isIsSupplying());
                ps.setString(5, dto.getSupCode());
                ps.setString(6, dto.getItemCode());
                int row = ps.executeUpdate();
                if (row > 0){
                    return true;
                }
            }
        }
        finally{
            if (ps != null){
                ps.close();
            }
            if (con != null){
                con.close();
            }
        }
        return false;
    }
    
    public boolean addItem(TblItemDTO dto) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = DBHelpers.makeConnection();
            if (con != null){
                String sql = "Insert Into tblItems (itemCode, itemName, unit, price, supplying, supCode) Values (?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getItemCode());
                ps.setString(2, dto.getItemName());
                ps.setString(3, dto.getUnit());
                ps.setFloat(4, dto.getPrice());
                ps.setBoolean(5, dto.isIsSupplying());
                ps.setString(6, dto.getSupCode());
                int row = ps.executeUpdate();
                if (row > 0){
                    return true;
                }
            }
        }
        finally{
            if (ps != null){
                ps.close();
            }
            if (con != null){
                con.close();
            }
        }
        return false;
    }
    
    public boolean deleteItem(TblItemDTO dto) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = DBHelpers.makeConnection();
            if (con != null){
                String sql = "Delete From tblItems Where itemCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getItemCode());
                int row = ps.executeUpdate();
                if (row > 0){
                    return true;
                }
            }
        }
        finally{
            if (ps != null){
                ps.close();
            }
            if (con != null){
                con.close();
            }
        }
        return false;
    }
}

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
import sonpc.dtos.TblSupDTO;
import sonpc.utils.DBHelpers;

/**
 *
 * @author ACER
 */
public class TblSupDAO implements Serializable {

    public List<TblSupDTO> getAllSups() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TblSupDTO> list = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select supCode, supName, address, collaborating From tblSuppliers";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String supCode = rs.getString("supCode");
                    String supName = rs.getString("supName");
                    String supAddr = rs.getString("address");
                    boolean collaborating = rs.getBoolean("collaborating");
                    TblSupDTO dto = new TblSupDTO(supCode, supName, supAddr, collaborating);
                    if (list == null) {
                        list = new Vector<>();
                    }
                    list.add(dto);
                }
                return list;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public TblSupDTO getSupDTOBySupCode(String supCode) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select supName, address, collaborating From tblSuppliers Where supCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, supCode);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String supName = rs.getString("supName");
                    String address = rs.getString("address");
                    boolean collaborating = rs.getBoolean("collaborating");
                    TblSupDTO dto = new TblSupDTO(supCode, supName, supCode, collaborating);
                    return dto;
                }

            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public int getIndexSupDTO(TblSupDTO dto) throws ClassNotFoundException, SQLException {
        List<TblSupDTO> list = getAllSups();
        if (list != null){
            for (TblSupDTO dtoSup : list){
                if (dto.getSupCode().equals(dtoSup.getSupCode())){
                    return list.indexOf(dtoSup);
            }
        }
    }
        return -1;
}
    
    public boolean addSupplier(TblSupDTO dto) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Insert Into tblSuppliers (supCode, supName, address, collaborating) Values (?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getSupCode());
                ps.setString(2, dto.getSupName());
                ps.setString(3, dto.getSupAddr());
                ps.setBoolean(4, dto.isIsColla());
                int row = ps.executeUpdate();
                if (row > 0){
                    return true;
                }
            }
        } finally {

            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }
    
    public boolean updateSupplier(TblSupDTO dto) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = DBHelpers.makeConnection();
            if (con != null){
                String sql = "Update tblSuppliers Set supName = ?, address =?, collaborating =? Where supCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getSupName());
                ps.setString(2, dto.getSupAddr());
                ps.setBoolean(3, dto.isIsColla());
                ps.setString(4, dto.getSupCode());
                int row = ps.executeUpdate();
                if (row > 0){
                    return true;
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
        return false;
    }
    
    public boolean deleteSupplier(TblSupDTO dto) throws ClassNotFoundException, SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = DBHelpers.makeConnection();
            if (con != null){
                String sql = "Delete From tblSuppliers Where supCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getSupCode());
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

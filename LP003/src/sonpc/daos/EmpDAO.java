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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.sql.Date;
import java.util.List;
import sonpc.dtos.EmpDTO;
import sonpc.utils.DBHelpers;

/**
 *
 * @author ACER
 */
public class EmpDAO implements Serializable {

    private final String DATE_FORMAT = "MM/dd/yyyy";

    public boolean createEmp(EmpDTO dto) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                //mặc định khi add mới user thì isDelete là false, tức là chưa delete
                String sql = "Insert Into Employee (empID, fullName, phone, email, address, dateOfBirth, isDelete) Values (?,?,?,?,?,?,0)";
                ps = con.prepareStatement(sql);
                //get all fields dto
                String empID = dto.getEmpID();
                String fullName = dto.getFullName();
                String phone = dto.getPhone();
                String email = dto.getEmail();
                String address = dto.getAddress();
                //parsing from java.util.Date to java.sql.Date
                java.util.Date utilDate = dto.getDateOfBirth();
                long sqlLongDate = utilDate.getTime();
                //setString
                ps.setString(1, empID);
                ps.setString(2, fullName);
                ps.setString(3, phone);
                ps.setString(4, email);
                ps.setString(5, address);
                ps.setLong(6, sqlLongDate);

                //executeUpdate
                int row = ps.executeUpdate();

                if (row > 0) {
                    return true;
                }//end if row >0
            }// end if con existed

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

    public List<EmpDTO> getAllEmp() throws ClassNotFoundException, SQLException, ParseException {
        List<EmpDTO> list = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            if (con != null) {

                //load những record chưa đc delete
                String sql = "Select empID, fullName, phone, email, address, dateOfBirth From Employee Where isDelete = 0";
                ps = con.prepareStatement(sql);

                rs = ps.executeQuery();
                while (rs.next()) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }//end if list is existed
                    //getString
                    String empID = rs.getString("empID");
                    String fullName = rs.getString("fullName");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    long sqlDate = rs.getLong("dateOfBirth");
                    //parsing date
                    java.util.Date utilDate = new java.util.Date(sqlDate);
                    //new dto
                    EmpDTO dto = new EmpDTO(empID, fullName, phone, email, address, utilDate); //do chỉ load những dto có isDelete là true nên để là true luôn
                    //add to list
                    list.add(dto);
                }//end while
                return list;
            } //end if con existed
            return null;

        } finally {
            if (ps != null) {
                ps.close();
            }//end if ps existed
            if (con != null) {
                con.close();
            }//end if con existed
        }//end finally

    }

    public EmpDTO getEmpByID(String id) throws ClassNotFoundException, SQLException, ParseException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                //Lấy những employee mà thỏa mãn id và isDelete = 0
                String sql = "Select fullName, phone, email, address, dateOfBirth From Employee Where empID = ? and isDelete = 0";
                ps = con.prepareStatement(sql);
                ps.setString(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("fullName");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
//                    java.sql.Date sqlDate = rs.getDate("dateOfBirth");
//                    //parsing
//                    java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
                    long sqlDate = rs.getLong("dateOfBirth");
                    java.util.Date utilDate = new java.util.Date(sqlDate);
                    //new dto
                    EmpDTO dto = new EmpDTO(id, fullName, phone, email, address, utilDate);

                    return dto;
                }//end if rs
            }//end if con existed
            return null;

        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean updateEmp(EmpDTO dto) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {

                String sql = "Update Employee Set FullName = ?, phone = ?, email = ?, address = ?, dateOfBirth = ? Where empID = ? and isDelete = 0 ";
                ps = con.prepareStatement(sql);
                //get fields dto
                String empID = dto.getEmpID();
                String fullName = dto.getFullName();
                String phone = dto.getPhone();
                String email = dto.getEmail();
                String address = dto.getAddress();
                java.util.Date utilDate = dto.getDateOfBirth();
                //parsing
                long sqlDate = utilDate.getTime();
                //setString
                ps.setString(1, fullName);
                ps.setString(2, phone);
                ps.setString(3, email);
                ps.setString(4, address);
                ps.setLong(5, sqlDate);
                ps.setString(6, empID);
                //executeUpdate
                int row = ps.executeUpdate();
                if (row > 0) {
                    return true;
                }//end if row > 0
            } //end if con existed
            return false;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean removeEmp(EmpDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {

                //ko đc xóa dto bên trong DB mà sửa isDelete của nó từ true => false (1=>0)
                String sql = "Update Employee Set isDelete = 1 Where  empID = ?";
                ps = con.prepareStatement(sql);
                //setString
                ps.setString(1, dto.getEmpID());
                //executeUpdate
                int row = ps.executeUpdate();
                if (row > 0) {
                    return true;
                }//end if row > 0
            }//end if con existed
            return false;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean recoveryEmp(EmpDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Update Employee Set isDelete = 0 Where empID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getEmpID());

                int row = ps.executeUpdate();

                if (row > 0) {
                    return true;
                }//end if row >0
            }//end if con existed
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

    public boolean checkDuplicateEmpID(String id) throws SQLException, ClassNotFoundException, ParseException {
        List<EmpDTO> list = getAllEmp();
        for (EmpDTO dto : list){
            if (dto.getEmpID().equals(id)){
                return true;
            }
        }
        return false;
    }

    public boolean empIDRegex(String empID) {
        String regex = "[a-zA-Z0-9]{1,10}";
        boolean isOK = empID.matches(regex);
        return isOK;
    }

    public boolean fullNameRegex(String fullName) {
        if (fullName.isEmpty() || fullName.length() > 30) {
            return false;
        }
        return true;
    }

    public boolean phoneRegex(String phone) {
        String regex = "[0-9]{1,15}";
        boolean isOK = phone.matches(regex);
        return isOK;
    }

    public boolean emailRegex(String email) {
        String regex = "[a-zA-Z0-9]+@[a-zA-Z0-9]+[.][a-zA-Z0-9]+";
        boolean isOK = email.matches(regex);
        return isOK;
    }

    public boolean addressRegex(String address) {
        if (address.isEmpty() || address.length() > 300) {
            return false;
        }
        return true;
    }
    
    public boolean FebLeapYear(String dateString){
        String regex = "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$";
        if (dateString.matches(regex)){
            return true;
        }
        return false;
    }
    
    public boolean FebNormalYear(String dateString){
        String regex = "^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$";
        if (dateString.matches(regex)){
            return true;
        }
        return false;
    }

    public boolean dateRegex(String dateString) throws ParseException {
        String pattern = "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$";
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        if (dateString.length()==0){
            return false;
        }

            java.util.Date date = format.parse(dateString);
            java.util.Date now = new java.util.Date();

            if (date.getTime() > now.getTime()) {
                return false;
            }

        return true;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.dtos;

import java.io.Serializable;

/**
 *
 * @author ACER
 */
public class TblSupDTO implements Serializable {

    private String supCode;
    private String supName;
    private String supAddr;
    private boolean isColla;

    public TblSupDTO() {
    }

    public TblSupDTO(String supCode, String supName, String supAddr, boolean isColla) {
        this.supCode = supCode;
        this.supName = supName;
        this.supAddr = supAddr;
        this.isColla = isColla;
    }

    public String getSupCode() {
        return supCode;
    }

    public void setSupCode(String supCode) {
        this.supCode = supCode;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getSupAddr() {
        return supAddr;
    }

    public void setSupAddr(String supAddr) {
        this.supAddr = supAddr;
    }

    public boolean isIsColla() {
        return isColla;
    }

    public void setIsColla(boolean isColla) {
        this.isColla = isColla;
    }

    @Override
    public String toString() {
        return supCode +"-"+ supName;
    }

}

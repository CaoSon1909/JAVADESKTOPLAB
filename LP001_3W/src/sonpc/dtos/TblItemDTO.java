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
public class TblItemDTO implements Serializable{
    private String itemCode;
    private String itemName;
    private String supCode;
    private String unit;
    private float price;
    private boolean isSupplying;

    public TblItemDTO() {
    }

    public TblItemDTO(String itemCode, String itemName, String supCode, String unit, float price, boolean isSupplying) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.supCode = supCode;
        this.unit = unit;
        this.price = price;
        this.isSupplying = isSupplying;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSupCode() {
        return supCode;
    }

    public void setSupCode(String supCode) {
        this.supCode = supCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isIsSupplying() {
        return isSupplying;
    }

    public void setIsSupplying(boolean isSupplying) {
        this.isSupplying = isSupplying;
    }

    @Override
    public String toString() {
        return  itemCode + itemName;
    }
    
    
    
    
}

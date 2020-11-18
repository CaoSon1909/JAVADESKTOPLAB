package sonpc.dtos;




import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ACER
 */
public class ArmorDTO implements Serializable{
    private String armorID;
    private String classification;
    private String description;
    private String status;
    private Date timeOfCreate;
    private int defense;    

    public ArmorDTO() {
    }

    public ArmorDTO(String armorID, String classification, String description, String status, Date timeOfCreate, int defense) {
        this.armorID = armorID;
        this.classification = classification;
        this.description = description;
        this.status = status;
        this.timeOfCreate = timeOfCreate;
        this.defense = defense;
    }
    

    
    
    /**
     * @return the armorID
     */
    public String getArmorID() {
        return armorID;
    }

    /**
     * @param armorID the armorID to set
     */
    public void setArmorID(String armorID) {
        this.armorID = armorID;
    }

    /**
     * @return the classification
     */
    public String getClassification() {
        return classification;
    }

    /**
     * @param classification the classification to set
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the timeOfCreate
     */
    public Date getTimeOfCreate() {
        return timeOfCreate;
    }

    /**
     * @param timeOfCreate the timeOfCreate to set
     */
    public void setTimeOfCreate(Date timeOfCreate) {
        this.timeOfCreate = timeOfCreate;
    }

    /**
     * @return the defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * @param defense the defense to set
     */
    public void setDefense(int defense) {
        this.defense = defense;
    }

    @Override
    public String toString() {
        return armorID + "@-@" + classification + "@-@" + description + "@-@" + status + "@-@" + timeOfCreate + "@-@" + defense;
    }

  
    
}

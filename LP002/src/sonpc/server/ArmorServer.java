/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.server;

import sonpc.dtos.ArmorDTO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ACER
 */
public class ArmorServer extends UnicastRemoteObject implements ArmorInterface {

    private static final String FILE_NAME = "Armor.txt";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DELIMETER = "-;-";

    public ArmorServer() throws RemoteException {
        super();
        loadFile();
    }

    private String dateToStringFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String dateString = format.format(date);
        return dateString;
    }

    private Date stringToDateFormat(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date date = format.parse(dateString);
        return date;
    }

    private String parsingDTOtoString(ArmorDTO dto) {
        //ArmorDTO fields
        String armorID = dto.getArmorID();
        String classification = dto.getClassification();
        String description = dto.getDescription();
        String status = dto.getStatus();
        Date timeOfCreate = dto.getTimeOfCreate();
        int defense = dto.getDefense();

        //parsing date to String
        String dateString = dateToStringFormat(timeOfCreate);

        //Intitialize a StringBuilder to append those fields
        StringBuilder sb = new StringBuilder();
        sb.append(armorID);
        sb.append(DELIMETER);
        sb.append(classification);
        sb.append(DELIMETER);
        sb.append(description);
        sb.append(DELIMETER);
        sb.append(status);
        sb.append(DELIMETER);
        sb.append(dateString);
        sb.append(DELIMETER);
        sb.append(defense);
        return sb.toString();
    }

    private ArmorDTO parsingStringtoDTO(String line) throws ParseException {
        String tmp[] = line.split(DELIMETER);
        String armorID = tmp[0];
        String classification = tmp[1];
        String description = tmp[2];
        String status = tmp[3];
        String dateString = tmp[4];
        String defenseString = tmp[5];

        //parsing dateString to date
        Date timeOfCreate = stringToDateFormat(dateString);
        //parsing defenseString to int
        int defense = Integer.parseInt(defenseString);

        ArmorDTO dto = new ArmorDTO(armorID, classification, description, status, timeOfCreate, defense);

        return dto;
    }

    synchronized public List<ArmorDTO> loadFile() {
        //getter
        List<ArmorDTO> list = null;
        File file = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            file = new File(FILE_NAME);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(DELIMETER);
                String armorID = tmp[0];
                String classification = tmp[1];
                String description = tmp[2];
                String status = tmp[3];
                String dateString = tmp[4];
                String defenseString = tmp[5];
                //parsing
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                Date timeOfCreate = format.parse(dateString);
                int defense = Integer.parseInt(defenseString);
                ArmorDTO dto = new ArmorDTO(armorID, classification, description, status, timeOfCreate, defense);
                if (list == null) {
                    list = new Vector<>();
                }
                list.add(dto);
            }//end while
            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ArmorServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ArmorServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    synchronized public void saveFile(List<ArmorDTO> list) { //setter
        File file = null;
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            file = new File(FILE_NAME);
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);

            if (list == null) {
                list = new Vector<>();
            }
            for (ArmorDTO dto : list) {
                String armorID = dto.getArmorID();
                String classification = dto.getClassification();
                String description = dto.getDescription();
                String status = dto.getStatus();
                Date date = dto.getTimeOfCreate();
                int defense = dto.getDefense();

                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                String dateString = format.format(date);

                bw.write(armorID + DELIMETER + classification + DELIMETER + description + DELIMETER + status + DELIMETER
                        + dateString + DELIMETER + defense);

                bw.write("\n");
            }
            bw.close();
            fw.close();

        } catch (IOException ex) {
            Logger.getLogger(ArmorServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public boolean createArmor(ArmorDTO dto) {
        List<ArmorDTO> list = loadFile();
        if (list != null) {
            list.add(dto);
            saveFile(list);
            return true;
        }
        return false;
    }

    @Override
    public ArmorDTO findByArmorID(String id) {
        List<ArmorDTO> list = loadFile();
        for (ArmorDTO dto : list) {
            if (dto.getArmorID().equals(id)) {
                return dto;
            }
        }
        return null;
    }

    @Override
    public List<ArmorDTO> findAllArmor() {
        List<ArmorDTO> list = loadFile();
        if (list != null) {
            return list;
        }
        return null;
    }

    @Override
    public boolean removeArmor(String id) {
        List<ArmorDTO> list = loadFile();
        if (list != null) {
            for (ArmorDTO dto : list) {
                if (id.equals(dto.getArmorID())) {
                    list.remove(dto);
                    saveFile(list);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateArmor(ArmorDTO dto) {
        List<ArmorDTO> list = loadFile();
        ArmorDTO result = findByArmorID(dto.getArmorID());
        if ((list != null) && (result != null)) {
            result.setClassification(dto.getClassification());
            result.setDescription(dto.getDescription());
            result.setStatus(dto.getStatus());
            result.setTimeOfCreate(dto.getTimeOfCreate());
            result.setDefense(dto.getDefense());
            removeArmor(dto.getArmorID());
            createArmor(result);
//            saveFile(list); ko can ham save file nua
            return true;
        }//end if
        return false;
    }

}

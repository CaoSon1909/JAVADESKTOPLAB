/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.daos;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import sonpc.dtos.ArmorDTO;
import sonpc.server.ArmorInterface;

/**
 *
 * @author ACER
 */
public class ArmorDAO implements Serializable {

    ArmorInterface armorServer;

    public ArmorDAO() throws NotBoundException, MalformedURLException, RemoteException {
        //makeConnection() trong java web
        armorServer = (ArmorInterface) Naming.lookup("rmi://localhost:1234/armor");
    }

    public boolean addNewArmor(ArmorDTO dto) throws RemoteException {
        if (dto != null) {
            armorServer.createArmor(dto);
            return true;
        }
        return false;
    }

    public ArmorDTO findArmorByID(String id) throws RemoteException {
        ArmorDTO result = armorServer.findByArmorID(id);
        return result;
    }

    public List<ArmorDTO> findAllArmor() throws RemoteException {
        List<ArmorDTO> result = armorServer.findAllArmor();
        return result;
    }

    public boolean removeArmor(String id) throws RemoteException {
        boolean result = armorServer.removeArmor(id);
        return result;
    }

    public boolean updateArmor(ArmorDTO dto) throws RemoteException {
        boolean result = armorServer.updateArmor(dto);
        return result;
    }

}

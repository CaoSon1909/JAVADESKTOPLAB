/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonpc.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author ACER
 */
public class Server {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1234);
            ArmorInterface armor = new ArmorServer();
            String url = "rmi://localhost:1234/armor";
            Naming.rebind(url, armor);
            System.out.println("Server is running at:"+url);
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
    }
}

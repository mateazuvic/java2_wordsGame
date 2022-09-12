/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.contract.service.MessengerService;
import hr.algebra.contract.serviceImpl.MessengerServiceImpl;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Teodor
 */
public class NetworkServer {

    
    public static void main(String[] args) {
       new ServerThread().start();
       createMessengerService();
        
    }

    private static void createMessengerService() {
        MessengerService server = new MessengerServiceImpl();
        try {
            MessengerService stub = (MessengerService) UnicastRemoteObject.exportObject((MessengerService) server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MessengerService",  stub);
        } catch (RemoteException ex) {
            Logger.getLogger(NetworkServer.class.getName()).log(Level.SEVERE, null, ex);
        }

     
           

        
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.contract.service;

import hr.algebra.contract.model.ChatMessage;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Teodor
 */
public interface MessengerService extends Remote {
    
    void sendMessage(ChatMessage chatMessage) throws RemoteException;
    
    List<ChatMessage> getAllMessages() throws RemoteException;
    
    ChatMessage getLastMessage() throws RemoteException;
    
    void clearMessages() throws RemoteException;
    
}

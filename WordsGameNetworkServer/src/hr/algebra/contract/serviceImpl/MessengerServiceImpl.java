/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.contract.serviceImpl;

import hr.algebra.contract.model.ChatMessage;
import hr.algebra.contract.service.MessengerService;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Teodor
 */
public class MessengerServiceImpl implements MessengerService{
    
    List<ChatMessage> messagesList = new ArrayList<>();

    @Override
    public void sendMessage(ChatMessage chatMessage) throws RemoteException {
        messagesList.add(chatMessage);
    }

    @Override
    public List<ChatMessage> getAllMessages() throws RemoteException {
        return messagesList;
    }

    @Override
    public ChatMessage getLastMessage() throws RemoteException {
        if (!messagesList.isEmpty()) {
            return messagesList.get(messagesList.size() - 1);
        }
        return null;
    }

    @Override
    public void clearMessages() throws RemoteException {
        messagesList.clear();
    }
    
}

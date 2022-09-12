/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.network;

import hr.algebra.controller.ResultsController;
import hr.algebra.model.Word;
import hr.algebra.utils.ByteUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Teodor
 */
public class ClientThread extends Thread {

    private Word wordToSend;
    private final ResultsController controller;

    public ClientThread(String name, ResultsController controller) {
        super(name);
        this.controller = controller;
    }

    public static final String PROPS_FILE = "socket.properties";
    public static final String CLIENT_PORT = "CLIENT_PORT";
    public static final String GROUP = "GROUP";

    public static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(new FileInputStream(PROPS_FILE));
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void trigger(Word word) {
        wordToSend = word;
    }
    
    
    @Override
    public void run() {
        try (MulticastSocket clientSocket = new MulticastSocket(Integer.valueOf(PROPERTIES.getProperty(CLIENT_PORT)))) {

            InetAddress groupAddress = InetAddress.getByName(PROPERTIES.getProperty(GROUP));
            System.err.println(getName() + " joining group");
            clientSocket.joinGroup(groupAddress);
            
            Thread.sleep(1000);
            //slanje rijeci koju je slozio klijent
            sendToServer(wordToSend, clientSocket, groupAddress);


            //primanje rijeci koju je slozio klijent
            byte[] numberOfBytes = new byte[64];
            DatagramPacket packetReceived = new DatagramPacket(numberOfBytes, numberOfBytes.length);
            clientSocket.receive(packetReceived);
            int length = ByteUtils.byteArrayToInt(numberOfBytes);

            byte[] receivedBytes = new byte[length];
            packetReceived = new DatagramPacket(receivedBytes, receivedBytes.length);
            clientSocket.receive(packetReceived);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(receivedBytes);
                    ObjectInputStream ois = new ObjectInputStream(bais)) {
                Word wordReceived = (Word) ois.readObject();

                Platform.runLater(() -> {
                    controller.showWord(wordReceived);
                });

                //primanje rijeci koju je slozio drugi klijent
                byte[] numberOfOpponentBytes = new byte[64];
                DatagramPacket packetReceivedOpponent = new DatagramPacket(numberOfOpponentBytes, numberOfOpponentBytes.length);
                clientSocket.receive(packetReceivedOpponent);
                int lengthOpponent = ByteUtils.byteArrayToInt(numberOfOpponentBytes);

                byte[] receivedOpponentBytes = new byte[lengthOpponent];
                packetReceivedOpponent = new DatagramPacket(receivedOpponentBytes, receivedOpponentBytes.length);
                clientSocket.receive(packetReceivedOpponent);
                try (ByteArrayInputStream baisOpponent = new ByteArrayInputStream(receivedOpponentBytes);
                        ObjectInputStream oisOpponent = new ObjectInputStream(baisOpponent)) {
                    Word wordReceivedOpponent = (Word) oisOpponent.readObject();
        
                    Platform.runLater(() -> {
                        controller.showWordOpponent(wordReceivedOpponent);
                    });
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Thread.sleep(3000);
                //slanje rijeci
                sendToServer(wordToSend, clientSocket, groupAddress);

                // System.err.println(getName() + " leaving group");
                //clientSocket.leaveGroup(groupAddress);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);

        } catch (InterruptedException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendToServer(Word wordToSend, MulticastSocket clientSocket, InetAddress groupAddress) {
        byte[] wordBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(wordToSend);
            wordBytes = baos.toByteArray();

            byte[] numberOfWordBytes = ByteUtils.intToByteArray(wordBytes.length);
            DatagramPacket packet = new DatagramPacket(
                    numberOfWordBytes,
                    numberOfWordBytes.length,
                    groupAddress, Integer.valueOf(PROPERTIES.getProperty(CLIENT_PORT))
            );
            clientSocket.send(packet);

            packet = new DatagramPacket(
                    wordBytes,
                    wordBytes.length,
                    groupAddress, Integer.valueOf(PROPERTIES.getProperty(CLIENT_PORT)));
            clientSocket.send(packet);
            System.out.println(getName() + " sent word!");
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

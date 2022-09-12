/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.model.Word;
import hr.algebra.utilities.ByteUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 *
 * @author Teodor
 */
public class ServerThread extends Thread {

   
    private static final String CLIENT_PORT = "CLIENT_PORT";
    private static final String GROUP = "GROUP";
    private static final String FILE_LOCATION = ".\\";
        
    private static String group;
    public static Integer clientPort;

    static {
        
        //JNDI
        Hashtable conf = new Hashtable();

        conf.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.fscontext.RefFSContextFactory");

        conf.put(Context.PROVIDER_URL, "file:" + FILE_LOCATION);
        
        try {
            Context context = new InitialContext(conf);

            NamingEnumeration enumeration = context.listBindings("");

            List<String> fileNames = new ArrayList<>();

            while (enumeration.hasMore()) {
                Binding fileItem = (Binding) enumeration.next();
                fileNames.add(fileItem.getName());
            }

            String configurationFileName = fileNames
                    .stream()
                    .filter(fileName
                            -> new File(FILE_LOCATION + fileName)
                                    .isDirectory() == false)
                    .filter(fileName -> fileName.endsWith(".properties"))
                    .findFirst()
                    .get();

            System.out.println("Configuration file name found: "
                    + configurationFileName);

            Properties appProps = new Properties();
            appProps.load(new FileInputStream(FILE_LOCATION
                    + configurationFileName));
            
            String portString = appProps.getProperty(CLIENT_PORT);
            String groupString = appProps.getProperty(GROUP);
          
            
            System.out.println("Client port: " + portString);
            System.out.println("Server group: " + groupString);
            
            
            group = groupString;
            clientPort = Integer.parseInt(portString);
            

        } catch (NamingException | IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    @Override
    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (true) {

                receivePacket(serverSocket);
                receivePacket(serverSocket);
                receivePacket(serverSocket);

            }

        } catch (SocketException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void receivePacket(final DatagramSocket serverSocket) throws IOException {
        //primanje rijeci od klijenta
        byte[] numberOfBytes = new byte[64];
        DatagramPacket packetReceived = new DatagramPacket(numberOfBytes, numberOfBytes.length);
        serverSocket.receive(packetReceived);
        int length = ByteUtils.byteArrayToInt(numberOfBytes);
        
        byte[] receivedBytes = new byte[length];
        packetReceived = new DatagramPacket(receivedBytes, receivedBytes.length);
        serverSocket.receive(packetReceived);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(receivedBytes);
                ObjectInputStream ois = new ObjectInputStream(bais)) {
            Word wordReceived = (Word) ois.readObject();
           
            Platform.runLater(() -> {
                sendToClients(wordReceived, serverSocket); //salje svim klijentima
            });
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendToClients(Word word, DatagramSocket serverSocket)  {
       byte[] wordBytes;
      
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(word);
                wordBytes = baos.toByteArray();

                 InetAddress groupAddress = InetAddress.getByName(group);
                
                byte[] numberOfWordBytes = ByteUtils.intToByteArray(wordBytes.length);
                DatagramPacket packet = new DatagramPacket(
                        numberOfWordBytes,
                        numberOfWordBytes.length,
                        groupAddress, Integer.valueOf(clientPort)
                );
                serverSocket.send(packet);

                packet = new DatagramPacket(
                        wordBytes,
                        wordBytes.length,
                        groupAddress, Integer.valueOf(clientPort));
                serverSocket.send(packet);
                System.out.println(getName() + " sent word" + word);

    }   catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.contract.service.MessengerService;
import hr.algebra.enums.State;
import hr.algebra.holders.PlayerHolder;
import hr.algebra.holders.WordHolder;
import hr.algebra.model.Player;
import hr.algebra.model.Word;
import hr.algebra.network.ClientThread;
import hr.algebra.utils.MessageUtils;
import hr.algebra.utils.GameUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Teodor
 */
public class ResultsController implements Initializable {

    @FXML
    private TextField tfFirstWord;
    @FXML
    private Label lbFirstPoints;
    @FXML
    private TextField tfSecondWord;
    @FXML
    private Label lbSecondPoints;
     @FXML
    private TextArea taChat;
    @FXML
    private TextField tfMessage;
    @FXML
    private Button btnGoToMenu;
    
    private Word myWord;
    private Player player;
    
   private ClientThread ct;
   private MessengerService messangerService;
   
    public static final String PROPERTIES_FILE = "socket.properties";
    public static final String USERNAME = "USERNAME";
    public static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException ex) {
            Logger.getLogger(ResultsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WordHolder holder = WordHolder.getInstance();
        Optional<Word> word = holder.getWord();
        if (word.isPresent()) {
            myWord = word.get();
            lbFirstPoints.setText("+ " + word.get().getLength()); 
        }
        else {
            myWord = new Word("", 0);
            lbFirstPoints.setText("+ 0");
        }
        PlayerHolder ph = PlayerHolder.getInstance();
        player = ph.getPlayer();
        initClientThread();
        ct.trigger(myWord);
        initChat();

    }


     private void initClientThread() {
        ct = new ClientThread("Klijent", this);
        ct.setDaemon(true);
        ct.start();
    }

    public void showWord(Word wordReceived) {
        tfFirstWord.setText(wordReceived.getName());
        System.out.println("Dobio: " + wordReceived.getName());
    }

    public void showWordOpponent(Word wordReceived) {
        tfSecondWord.setText(wordReceived.getName());
        System.out.println("Dobio: " + wordReceived.getName());
        lbSecondPoints.setText("+ " + wordReceived.getLength());
    }

    public void showWordOpponentIfNotShown(Word wordReceived) {
        if (tfSecondWord.getText().isEmpty()) {
            tfSecondWord.setText(wordReceived.getName());
            System.out.println("Izvanredno dobio:" + wordReceived.getName());
        }
    }

    private void initChat() {
        messangerService = MessageUtils.getMessengerService();
        updateChat();
    }

    @FXML
    private void sendMessage(ActionEvent event) {
        String message = tfMessage.getText();
         if (message.length() != 0) {
             
            MessageUtils.sendMessage(player.getNickname(), tfMessage.getText().trim());          
                     
        }
         
          tfMessage.setText("");

    }

    
    private int nbrOfMessages = 0;
     public void updateChat() {
        try {
            messangerService.clearMessages();
            writeAllMessages();
            nbrOfMessages = messangerService.getAllMessages().size();
            new Timer().scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    try {
                        if (messangerService.getAllMessages().size() != nbrOfMessages) {
                            nbrOfMessages = messangerService.getAllMessages().size();
                            writeAllMessages();
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(ResultsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }, 0, 500);

        } catch (RemoteException ex) {
            Logger.getLogger(ResultsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeAllMessages() throws RemoteException {
       
        
        StringBuilder sb = new StringBuilder();
        messangerService.getAllMessages().forEach(m -> {

            m.setPlayer(m.getPlayer());
            
            sb.append(m).append("\n");
            taChat.setText(sb.toString());
                       
        });       
    }

   

    @FXML
    private void replay(ActionEvent event) {       
        try {
            GameUtils.startStage(getClass().getResource("/hr/algebra/view/Game.fxml"), "Slagalica", State.replay, player);
        } catch (IOException ex) {
            Logger.getLogger(ResultsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void goToMenu(ActionEvent event) {
         try {
            GameUtils.startStage(getClass().getResource("/hr/algebra/view/Menu.fxml"), "Slagalica", State.newGame, player);
        } catch (IOException ex) {
            Logger.getLogger(ResultsController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         ((Stage) btnGoToMenu.getScene().getWindow()).close();
    }
  
   
}

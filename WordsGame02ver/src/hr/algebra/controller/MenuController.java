/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.enums.State;
import hr.algebra.model.Player;
import hr.algebra.utils.GameUtils;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Teodor
 */
public class MenuController implements Initializable {

    @FXML
    private Button btnStart;
    @FXML
    private TextField tfNickname;

    private State previousState;
    
    private Player player;
    
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        previousState = State.newGame;
        player = new Player();
    }

    @FXML
    private void startGame(ActionEvent event) {
        try {            
            player.setNickname(tfNickname.getText().trim()); 
            showMessage();
            GameUtils.startStage(getClass().getResource("/hr/algebra/view/Game.fxml"), "Slagalica", previousState, player);
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }

        ((Stage) btnStart.getScene().getWindow()).close();
    }

    private void showMessage() {
        
       
        //za nastaviti prošlu igru

        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to continue your previous game?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Question");
        alert.setTitle("Dialog");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.YES) {
            previousState = State.oldGame;
        }

    }


    @FXML
    private void handleStartBtn() {
        btnStart.setDisable(tfNickname.getText().trim().isEmpty());
    }

}

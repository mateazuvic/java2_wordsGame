/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import hr.algebra.enums.State;
import hr.algebra.holders.PlayerHolder;
import hr.algebra.holders.StateHolder;
import hr.algebra.holders.WordHolder;
import hr.algebra.model.Player;
import hr.algebra.model.Word;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Teodor
 */
public class GameUtils {


    private GameUtils() {
    }

    public static void startStage(URL path, String title, State previousState, Player player, Optional<Word>... params) throws IOException {
        if (params.length > 0) {
            WordHolder holder = WordHolder.getInstance();
            
            for (Optional<Word> param : params) {
                holder.setWord(param);
            }
        }
        
        StateHolder sh = StateHolder.getInstance();
        sh.setState(previousState);
        
        PlayerHolder ph = PlayerHolder.getInstance();
        ph.setPlayer(player);
            
        Parent root = FXMLLoader.load(path);

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

}

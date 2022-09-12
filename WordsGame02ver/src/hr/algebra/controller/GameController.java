/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.enums.State;
import hr.algebra.model.Game;
import hr.algebra.model.Letter;
import hr.algebra.model.Player;
import hr.algebra.model.Reader;
import hr.algebra.model.Step;
import hr.algebra.model.Word;
import hr.algebra.network.ClientThread;
import hr.algebra.utils.DOMUtils;
import hr.algebra.utils.SerializationUtils;
import hr.algebra.utils.GameUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Teodor
 */
public class GameController implements Initializable {


    private static final String LETTERS_FILE = "/res/letters.txt";
    private static final String WORDS_FILE = "/res/words.txt";

    //FXML fields
    @FXML
    private Label lbWord;
    @FXML
    private Label lbError;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnL1;
    @FXML
    private Button btnL2;
    @FXML
    private Button btnL3;
    @FXML
    private Button btnL4;
    @FXML
    private Button btnL5;
    @FXML
    private Button btnL6;
    @FXML
    private Button btnL7;
    @FXML
    private Button btnL8;
    @FXML
    private Button btnL9;
    @FXML
    private Button btnL10;
    @FXML
    private Button btnL11;
    @FXML
    private Button btnL12;
    @FXML
    private Label lbCheck;
    @FXML
    private Label lbTimer;
     @FXML
    private Button btnPlay;

    //NON FXML fields
    private Button[] letterButtons;

    private String[] letterSigns;

    private ObservableList<String> clickedLetters;

    private Reader reader;

    private Game game;

    private List<Step> steps;

    private Optional<Word> foundWord = Optional.empty();

    private State previousState;
    private Player player;

    private File file = new File(SerializationUtils.SER_FILE_NAME);
    private File fileSteps = new File(SerializationUtils.SER_FILE_NAME_STEPS);
    List<Step> desSteps;
    List<Step> domSteps;

    //serijalizacija
    private Word finalWord;
    private String randomCombinationOfLetters;
    
    private ObservableList<Player> PLAYERS = FXCollections.observableArrayList();

    private Word writtenWord;

    //timer fields
    private long min, sec, hr, totalSec = 0; 
    private Timer timer;
    
    private static String nick;
   
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        letterButtons = new Button[]{btnL1, btnL2, btnL3, btnL4, btnL5, btnL6, btnL7, btnL8, btnL9, btnL10, btnL11, btnL12};
        letterSigns = new String[]{"a", "b", "c", "ć", "č", "d", "đ", "dž", "e", "f", "g", "h", "i", "j", "k", "l", "lj", "m", "n", "nj", "o", "p", "r", "s", "š", "t", "u", "v", "z", "ž"};
        reader = new Reader();
        clickedLetters = FXCollections.observableArrayList();
        game = new Game(PLAYERS, finalWord, randomCombinationOfLetters, totalSec);
        steps = new ArrayList<>();
        previousState = reader.readState();
        player = reader.readPlayer();
        nick = player.getNickname();
        prepareLetters(reader);

    }

    private void prepareLetters(Reader reader) {
        if (previousState.equals(State.newGame)) {
            reader.readFile(LETTERS_FILE);
            randomCombinationOfLetters = reader.getRandomCombinationOfLetters();
            List<Letter> letters = reader.parseDataToLetters(randomCombinationOfLetters);
            
            setLettersOnButtons(letters, State.newGame);

        } else if (previousState.equals(State.oldGame)) {

            Game desData = deserialize();
            Word w = (Word) desData.getFirstWord();
            lbWord.setText(w.getName());

            randomCombinationOfLetters = desData.getCombinationOfLetters();
            List<Letter> letters = reader.parseDataToLetters(randomCombinationOfLetters);
            setLettersOnButtons(letters, State.oldGame);
            clickedLetters.addAll(w.getName());
            disableClickedButtons(w.getName());

            String[] arrayLetters = w.getName().split("(?!^)");

            for (int i = 0; i < arrayLetters.length; i++) {
                clickedLetters.add(arrayLetters[i]);
            }

            totalSec = desData.getTimer();
            convertTime();

           
        } else if (previousState.equals(State.replay)) {
            loadDOM();
            randomCombinationOfLetters = domSteps.get(0).getCombination();
            List<Letter> letters = reader.parseDataToLetters(randomCombinationOfLetters);
            setLettersOnButtons(letters, State.replay);
            
            

        }
    }
     

    private void setLettersOnButtons(List<Letter> lettersInGame, State state) {

        for (int i = 0; i < letterButtons.length; i++) {
            for (int j = 0; j < lettersInGame.size(); j++) {
                letterButtons[i].setText(lettersInGame.get(j).toString());
                i++;
            }
        }

        if (state != State.replay) {
            setTimer();
            btnPlay.setDisable(true);
        }
        
    }

    private String format(long value) {
        if (value < 10) {
            return 0 + "" + value;
        }

        return value + "";
    }

    private void convertTime() {

        min = TimeUnit.SECONDS.toMinutes(totalSec);
        sec = totalSec - (min * 60);
        hr = TimeUnit.MINUTES.toHours(min);
        min = min - (hr * 60);
        lbTimer.setText(format(hr) + ":" + format(min) + ":" + format(sec));

        totalSec--;
    }

    private void setTimer() {
        totalSec = 60;
        this.timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(totalSec);
                        convertTime();
                        if (totalSec < 0) {
                            saveDOM();
                            timer.cancel();
                            lbTimer.setText("00:00:00");
                            
                            if (foundWord.isPresent()) {
                                try {
                                    GameUtils.startStage(getClass().getResource("/hr/algebra/view/Results.fxml"), "Slagalica", State.newGame, player, foundWord);
                                    ((Stage) btnDelete.getScene().getWindow()).close();
                                } catch (IOException ex) {
                                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                try {
                                    GameUtils.startStage(getClass().getResource("/hr/algebra/view/Results.fxml"), "Slagalica", State.newGame, player, Optional.empty());
                                    ((Stage) btnDelete.getScene().getWindow()).close();
                                } catch (IOException ex) {
                                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        }
                    }

                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
    
    @FXML
    private void onBtnLetterClick(ActionEvent event) {
        lbError.setVisible(false);
        lbCheck.setVisible(false);
        Button clickedBtn = (Button) event.getSource();                                                                                                                           
        lbWord.setText(lbWord.getText() + clickedBtn.getText()); 
        clickedLetters.add(clickedBtn.getText());
        clickedBtn.setDisable(true); 
        writtenWord = new Word(lbWord.getText(), lbWord.getText().length());

        steps.add(new Step(writtenWord, randomCombinationOfLetters)); 

    }
    
    
     @FXML
    private void onBtnDeleteClick(ActionEvent event) {
        
         if (lbWord.getText().length() > 0) {
             lbError.setVisible(false);
             lbCheck.setVisible(false);
             String lastLetter = lbWord.getText().substring(lbWord.getText().length() - 1, lbWord.getText().length());

             for (int i = clickedLetters.size() - 1; i >= 0; i--) {
                 if (!lastLetter.equals(clickedLetters.get(i))) {
                     lastLetter = doDoubleLetterCheck(lbWord);
                     lbWord.setText(lbWord.getText().substring(0, lbWord.getText().length() - 2));
                     clickedLetters.remove(i);                                                                                           
                     break;
                 } else {
                     lbWord.setText(lbWord.getText().substring(0, lbWord.getText().length() - 1));
                     clickedLetters.remove(i);                                                                                                     
                     break;
                 }

             }

             for (Button letterButton : letterButtons) {
                 if (letterButton.isDisabled() && letterButton.getText().equals(lastLetter)) { 
                     letterButton.setDisable(false);
                 }
             }
             writtenWord = new Word(lbWord.getText(), lbWord.getText().length());
             steps.add(new Step(writtenWord, randomCombinationOfLetters));

        }
    }
    
    private String doDoubleLetterCheck(Label lbWord) { 
        String lastLetter = lbWord.getText().substring(lbWord.getText().length() - 2, lbWord.getText().length());
        for (String letterSign : letterSigns) {
            if (lastLetter.equals(letterSign)) {
                return lastLetter;
            }
        }
        return null;
    }
    
     @FXML
    private void onBtnSaveWordClick(ActionEvent event) {
         finalWord = new Word(lbWord.getText().trim(), lbWord.getText().trim().length());
         reader.readFile(WORDS_FILE);
         List<Word> words = reader.getAllData();
         foundWord = words.stream()
                                        .filter(w -> w.getName().trim().equals(finalWord.getName().trim()))
                                        .findAny();

         if (!foundWord.isPresent()) {
             lbError.setVisible(true);
         } else {
             lbCheck.setVisible(true);

         }

         serialize();
        
    }

   

    
    private void disableClickedButtons(String word) {
        String[] usedLetters = word.split("(?!^)");
        List<Button> buttonsToDisable = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) { 
            for (Button letterButton : letterButtons) { 
                
                if (letterButton.getText().trim().equals(usedLetters[i])) { 
                    buttonsToDisable.add(letterButton);
                    letterButton.setDisable(true);
                    break;
                }
             }
        }
        

    }
    
     

    private void serialize() {

        try {
            if (file != null) {
                game.setFirstWord(finalWord);
                game.setCombinationOfLetters(randomCombinationOfLetters);
                game.setTimer(totalSec);
                SerializationUtils.write(game, file.getAbsolutePath());
            }
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Game deserialize() {
        try {
            if (file != null) {
                game = (Game) SerializationUtils.read(SerializationUtils.SER_FILE_NAME);
                return game;
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    

     int i = 0;
     
    @FXML
    private void play(ActionEvent event) {

        if (i < domSteps.size()) {

            Word word = domSteps.get(i).getWrittenWord(); 

            disableClickedButtons(word.getName());
            if (!lbWord.getText().equals(word.getName().trim())) { 
                if (lbWord.getText().length() > word.getName().trim().length()) {
                    String last = lbWord.getText().substring(lbWord.getText().length() - 1, lbWord.getText().length());
                    for (Button letterButton : letterButtons) {
                        if (letterButton.getText().trim().equals(last)) {
                            letterButton.setDisable(false);
                        }

                    }
                }
            }
            lbWord.setText(word.getName());

            i++;
        } 

    }


    private void saveDOM() {
        DOMUtils.saveSteps(steps);
    }
    
    
    public static String getFileNameDOM() {
        return "steps" + nick + ".xml";
    }

    private void loadDOM() {
        domSteps = DOMUtils.loadSteps();       
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Teodor
 */
public class Game implements Serializable {
    
    private static final long serialVersionUID = 4L;
    
    private ListProperty<Player> players;
    private ObjectProperty<Word> firstWord;
    //private ObjectProperty<Word> secondWord;
    private StringProperty combinationOfLetters;
    private LongProperty timer;

    public Game() {
    }
    

    public Game(ObservableList<Player> players, Word firstWord, String combinationOfLetters, long timer) {
        this.players = new SimpleListProperty<>(players);
        this.firstWord = new SimpleObjectProperty<>(firstWord);
        //this.secondWord = new SimpleObjectProperty<>(secondWord);
        this.combinationOfLetters = new SimpleStringProperty(combinationOfLetters);
        this.timer = new SimpleLongProperty(timer);
    }

    public ObservableList<Player> getPlayers() {
        return players.get();
    }

    public void setPlayers(ObservableList<Player> players) {
        this.players.set(players);
    }

    public Word getFirstWord() {
        return firstWord.get();
    }

    public void setFirstWord(Word firstWord) {
        this.firstWord.set(firstWord);
    }

//    public ObjectProperty<Word> getSecondWord() {
//        return secondWord;
//    }
//
//    public void setSecondWord(ObjectProperty<Word> secondWord) {
//        this.secondWord = secondWord;
//    }

    public String getCombinationOfLetters() {
        return combinationOfLetters.get();
    }

    public void setCombinationOfLetters(String combinationOfLetters) {
        this.combinationOfLetters.set(combinationOfLetters);
    }

    public long getTimer() {
        return timer.get();
    }

    public void setTimer(long timer) {
        this.timer.set(timer);
    }
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(new ArrayList<>(players.get()));
        oos.writeObject(firstWord.get());
        oos.writeObject(combinationOfLetters.get());
        oos.writeObject(timer.get());
        
    }

    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
       List<Player> serializedPlayers = (List<Player>) ois.readObject();
       players = new SimpleListProperty<>(FXCollections.observableArrayList(serializedPlayers));
        
        firstWord = new SimpleObjectProperty<>((Word) ois.readObject());
        
        combinationOfLetters = new SimpleStringProperty((String)ois.readObject());
        
        timer = new SimpleLongProperty((Long)ois.readObject());
    }
    
    
    
}

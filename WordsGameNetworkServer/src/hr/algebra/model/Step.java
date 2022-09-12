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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Teodor
 */
public class Step implements Serializable {

    // k i š a n b l k
    private ObjectProperty<Word> writtenWord; //k        //i         //š
    private StringProperty combinationOfLetters; // k i š a n b l k
    private ListProperty<String> leftLetters; // i š a n b l k         //š a n b l k             //a n b l k

    public Step(Word writtenWord, String combinationOfLetters, ObservableList<String> leftLetters) {
        this.writtenWord = new SimpleObjectProperty<>(writtenWord);
        this.combinationOfLetters = new SimpleStringProperty(combinationOfLetters);
        this.leftLetters = new SimpleListProperty<>(leftLetters);
    }


    public Word getWrittenWord() {
        return writtenWord.get();
    }

    public void setWrittenWord(Word writtenWord) {
        this.writtenWord =new SimpleObjectProperty<>(writtenWord);
    }

    public String getCombinationOfLetters() {
        return combinationOfLetters.get();
    }

    public void setCombinationOfLetters(String combinationOfLetters) {
        this.combinationOfLetters =new SimpleStringProperty(combinationOfLetters);
    }

    public ObservableList<String> getLeftLetters() {
        return leftLetters.get();
    }

    public void setLeftLetters(ObservableList<String> leftLetters) {
        this.leftLetters = new SimpleListProperty<>(leftLetters);
    }
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject((writtenWord.get()));
        oos.writeObject(combinationOfLetters.get());
        oos.writeObject(new ArrayList<>(leftLetters.get()));

    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        writtenWord = new SimpleObjectProperty<>((Word) ois.readObject());

        combinationOfLetters = new SimpleStringProperty((String) ois.readObject());

        List<String> serializedLetters = (List<String>) ois.readObject();
        leftLetters = new SimpleListProperty<>(FXCollections.observableArrayList(serializedLetters));
    }

}

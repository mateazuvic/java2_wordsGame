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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Teodor
 */
public class Step implements Serializable {
    
    private static final long serialVersionUID = 5L;

    // k i š a n b l k
    private ObjectProperty<Word> writtenWord; 
    private StringProperty combination; 
   
    
    public Step(Word writtenWord, String combination) {
        this.writtenWord = new SimpleObjectProperty<>(writtenWord);
        this.combination = new SimpleStringProperty(combination);
        //this.clickedButtons = new SimpleListProperty<>(clicked);
    }


    public Word getWrittenWord() {
        return writtenWord.get();
    }

    public void setWrittenWord(Word writtenWord) {
        this.writtenWord =new SimpleObjectProperty<>(writtenWord);
    }

//    public ObservableList<Button> getClickedButtons() {
//        return clickedButtons.get();
//    }
//
//    public void setClickedButtons(ObservableList<Button> clickedButtons) {
//        this.clickedButtons = new SimpleListProperty<>(clickedButtons);
//    }
//    

    public String getCombination() {
        return combination.get();
    }

    public void setCombination(String combination) {
        this.combination = new SimpleStringProperty(combination);
    }
    
    private void writeObject(ObjectOutputStream oos) throws IOException, Exception {
        
        try{
            oos.writeObject((writtenWord.get()));
            oos.writeUTF(combination.get());
        
        } catch(Exception ex) {
            throw new Exception(ex.getMessage());
        }
        
        

    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        writtenWord = new SimpleObjectProperty<>((Word) ois.readObject());
        combination = new SimpleStringProperty(ois.readUTF());
        
//        List<Button> serializedButtons = (List<Button>)ois.readObject();
//        clickedButtons = new SimpleListProperty<>(FXCollections.observableArrayList(serializedButtons));
    }

    @Override
    public String toString() {
        return "written word: " + writtenWord.get().getName() + "\n"
                + "combination: " + combination;
    }
    
    

}

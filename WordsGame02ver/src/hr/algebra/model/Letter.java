/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.enums.LetterSign;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Teodor
 */
public class Letter implements Serializable{
    
    private static final long serialVersionUID = 2L;
    
    private ObjectProperty<LetterSign> sign;

    public Letter(LetterSign sign) {
        this.sign = new SimpleObjectProperty<>(sign);
    }

    public LetterSign getSign() {
        return sign.get();
    }

    public void setSign(LetterSign sign) {
        this.sign = new SimpleObjectProperty<>(sign);
    }

    @Override
    public String toString() {
        return sign.get().name();
    }
    
     private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(sign.get());     
    }

    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        sign = new SimpleObjectProperty(ois.readObject());
       
    }
    
    
}

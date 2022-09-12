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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Teodor
 */
public class Word implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private StringProperty name;
    private IntegerProperty length;

    public Word() {
    }
    

    public Word(String name, int length) {
        this.name = new SimpleStringProperty(name);;
        this.length = new SimpleIntegerProperty(length);
    }

    public String getName() {
        return name.get();
    }

    public int getLength() {
        return length.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setLength(int length) {
        this.length.set(length);
    }

    @Override
    public String toString() {
        return name.get();
    }
   
     private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(name.get());
        oos.writeInt(length.get());
    }

    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        name = new SimpleStringProperty(ois.readUTF());
        length = new SimpleIntegerProperty(ois.readInt());      
        
    }
    
}

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

/**
 *
 * @author Teodor
 */
public class Player implements Serializable{
    
    private static final long serialVersionUID = 3L;
    
    private String nickname;
    private int points;

    public Player() {
    }

    public Player(String nickname, int points) {
        this.nickname = nickname;
        this.points = points;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return  nickname;
    }
    
     private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(nickname);
        oos.writeInt(points);
    }

    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        String serializedNIckname = ois.readUTF();
        int serializedPoints = ois.readInt();      
        setNickname(serializedNIckname);
        setPoints(serializedPoints);
    }
    
    
    
    
}

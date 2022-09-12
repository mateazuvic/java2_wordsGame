/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.enums.LetterSign;
import hr.algebra.enums.State;
import hr.algebra.holders.PlayerHolder;
import hr.algebra.holders.StateHolder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Teodor
 */
public class Reader {

    private static final String DEL = " ";

    private List<String> dataFromFile = new ArrayList<String>();

    public Reader() {
    }

    public void readFile(String fileName) {
        try (InputStream in = getClass().getResourceAsStream(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String line = "";
            while ((line = br.readLine()) != null) {
                dataFromFile.add(line); 
            }

        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Word> getAllData() {
        if (!dataFromFile.isEmpty()) {
            return mapFromStringToWord(dataFromFile);
        }
        return null;
    }

    public String getRandomCombinationOfLetters() {
        if (!dataFromFile.isEmpty()) {
            return dataFromFile.get(ThreadLocalRandom.current().nextInt(0, dataFromFile.size()));
        }
        return "Nema podataka";
    }

    public List<Letter> parseDataToLetters(String line) {
        List<Letter> letters = new ArrayList<>();

        String[] details = line.split(DEL);
        for (String detail : details) {
            letters.add(new Letter(LetterSign.valueOf(detail)));
        }
        dataFromFile.clear();
        return letters;
    }

    private List<Word> mapFromStringToWord(List<String> data) {
        List<Word> words = new ArrayList<Word>();
        for (String word : data) {
            words.add(new Word(word.toLowerCase(), word.length()));
        }

        return words;
    }

    public State readState() {
        StateHolder holder = StateHolder.getInstance();
        return holder.getState();
    }

    public Player readPlayer() {
        PlayerHolder ph = PlayerHolder.getInstance();
        return ph.getPlayer();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.holders;

import hr.algebra.model.Word;
import java.util.Optional;

/**
 *
 * @author Teodor
 */
public final class WordHolder { //singleton

    private Optional<Word> word;
    private final static WordHolder INSTANCE = new WordHolder();

    private WordHolder() {
    }

    public static WordHolder getInstance() {
        return INSTANCE;
    }

    public Optional<Word> getWord() {
        return word;
    }

    public void setWord(Optional<Word> word) {
        this.word = word;
    }

    

}

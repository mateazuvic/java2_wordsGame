/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.holders;

import hr.algebra.enums.State;

/**
 *
 * @author Teodor
 */
public final class StateHolder { //singleton

    private State state;
    private final static StateHolder INSTANCE = new StateHolder();

    private StateHolder() {
    }

    public static StateHolder getInstance() {
        return INSTANCE;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    

}

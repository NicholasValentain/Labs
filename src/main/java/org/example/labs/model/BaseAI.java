package org.example.labs.model;

import java.util.List;
import java.util.Vector;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public abstract class BaseAI extends Thread{
    public boolean isActive = false;
    protected double speed;
    public String monitor;

    public BaseAI(String name) {
        super(name);
    }
}

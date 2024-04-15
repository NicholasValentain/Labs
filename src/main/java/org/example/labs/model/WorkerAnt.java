package org.example.labs.model;

import java.util.Random;

class WorkerAnt extends Ant {
    private static final double MOVEMENT_SPEED = 5.0;
    private Random random;

    public WorkerAnt(long birthTime, long  lifeTime) {
        super("/org/example/labs/AntGIF/ant1.gif", birthTime, lifeTime);
        this.random = new Random();
    }
}

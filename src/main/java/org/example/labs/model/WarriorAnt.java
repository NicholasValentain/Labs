package org.example.labs.model;

import java.util.Random;

class WarriorAnt extends Ant {
    private static final double MOVEMENT_SPEED = 7.0;
    private Random random;

    public WarriorAnt(long birthTime, long  lifeTime) {
        super("/org/example/labs/AntGIF/table.png", birthTime, lifeTime);
        this.random = new Random();
    }

}

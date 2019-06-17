package com.bombgame.src;

import com.bombgame.src.enums.Direction;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class mainBot extends Person {

    private static Random randomGenerator = new Random();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    private Direction direction;

    protected mainBot(int x, int y, int speed) {
        super(x, y, speed);
        direction = getRandomDirection();
    }


    private Direction getRandomDirection() {
        return Direction.values()[randomGenerator.nextInt(4)];
    }
}

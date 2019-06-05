package com.bombgame.src;

import com.bombgame.src.enums.Direction;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot extends Person {
    private static Random randomGenerator = new Random();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    private Direction direction;

    public Bot(int x, int y) {
        super(x, y, 1);
        direction = getRandomDirection();
    }

    public boolean willCollide(Direction direction, Interactable anotherObject) {
        return (getExpectedCollider(direction).intersects(anotherObject.getCollider()));
    }

    public boolean willCollideMap(Direction direction, Map map) {
        return map.collideMap(getExpectedCollider(direction));
    }

    public void update(Map map) {

        if (!willCollideMap(direction, map)) {
            move(direction);
        }
        if (willCollideMap(direction, map)) {
            direction = getRandomDirection();
            if (!willCollideMap(direction, map)) {
                move(direction);
            }
            map.placeBomb(getX(), getY());
            Runnable explodeBomb = () -> {
                direction = getRandomDirection();

            };
            executor.schedule(explodeBomb, 1000, TimeUnit.MILLISECONDS);

        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(getImage(), getX(), getY(), null);
        g2d.setColor(Color.BLACK);
        g2d.draw(getCollider());
    }

    private Direction getRandomDirection() {
        return Direction.values()[randomGenerator.nextInt(4)];
    }
}




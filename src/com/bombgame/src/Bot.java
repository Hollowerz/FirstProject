package com.bombgame.src;

import java.awt.*;
import java.util.Random;

public class Bot extends Person {
    private static Random randomGenerator = new Random();

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




package com.bombgame.src;

import com.bombgame.src.enums.Direction;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Person implements Interactable, Movable {
    protected static final int WIDTH = 30;
    protected static final int HEIGHT = 30;
    private static Random randomGenerator = new Random();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    private Direction direction;

    private Position position;
    private int speed;
    private boolean isAlive = true;
    private String name;

    protected Person(int x, int y, int speed) {
        position = new Position(x, y);
        this.speed = speed;
        direction = getRandomDirection();
    }

    private Direction getRandomDirection() {
        return Direction.values()[randomGenerator.nextInt(4)];
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void setX(int x) {
        this.position.setX(x);
    }

    public void setY(int y) {
        this.position.setY(y);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Rectangle getCollider() {
        return new Rectangle(getX(), getY(), WIDTH, HEIGHT);
    }

    protected Rectangle getExpectedCollider(Direction direction) {
        // copy real position to restore after calculations
        Position realPosition = new Position(position);
        // move object with temporary position using provided direction
        move(direction);
        // retrieve expected collider
        Rectangle expectedCollider = getCollider();
        // restore old position(object mustn't be moved, this is just a calculations)
        position = realPosition;

        return expectedCollider;
    }

    /**
     * fffqwfqwf fsdfsdfffsdfsdggsdff
     *
     * @param map
     */
    public void update(Map map) {

        if (!willCollideMap(direction, map)) {
            move(direction);
        }
        if (willCollideMap(direction, map)) {
            direction = getRandomDirection();
            if (!willCollideMap(direction, map)) {
                move(direction);
            }

            Runnable explodeBomb = () -> {
                if (isAlive) map.placeBomb(getX(), getY());
                direction = getRandomDirection();


            };
            executor.schedule(explodeBomb, 2000, TimeUnit.MILLISECONDS);

        }
    }

    public boolean willCollide(Direction direction, Interactable anotherObject) {
        return (getExpectedCollider(direction).intersects(anotherObject.getCollider()));
    }

    public boolean willCollideMap(Direction direction, Map map) {
        return map.collideMap(getExpectedCollider(direction));
    }


    public void move(Direction direction) {
        if (direction == null) return;
        if (direction == Direction.UP) {
            setY(getY() - speed);
        }
        if (direction == Direction.DOWN) {
            setY(getY() + speed);
        }
        if (direction == Direction.LEFT) {
            setX(getX() - speed);
        }
        if (direction == Direction.RIGHT) {
            setX(getX() + speed);
        }
    }
}

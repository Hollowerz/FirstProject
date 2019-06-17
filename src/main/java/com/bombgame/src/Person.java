package com.bombgame.src;

import com.bombgame.src.enums.Direction;
import com.bombgame.src.util.ImageHolder;

import java.awt.*;

public class Person implements Interactable, Movable {
    protected static final int WIDTH = 30;
    protected static final int HEIGHT = 30;

    private Position position;
    private int speed;
    private final Image image = ImageHolder.getPlayerImage();
    private boolean isAlive = true;
    private String name;

    protected Person(int x, int y, int speed) {
        position = new Position(x, y);
        this.speed = speed;
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

    public Image getImage() {
        return image;
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

package com.bombgame.src;

import javax.swing.*;
import java.awt.*;

public class Player implements Interactable {

    private static final String PLAYER_IMAGE_PATH = "/images/player.jpg";
    private static final int PLAYER_WIDTH = 30;
    private static final int PLAYER_HEIGHT = 30;
    private Position position;
    private int speed = 2;

    public Player(int x, int y) {
        position = new Position(x, y);
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(getPlayerImage(), position.getX(), position.getY(), null);
        g2d.setColor(Color.BLACK);
        g2d.draw(getCollider());
    }

    public void move(Direction direction) {
        if (direction == null) return;
        switch (direction) {
            case UP:
                setY(getY() - speed);
                break;
            case DOWN:
                setY(getY() + speed);
                break;
            case LEFT:
                setX(getX() - speed);
                break;
            case RIGHT:
                setX(getX() + speed);
                break;


        }
    }



    public boolean willCollide(Direction direction, Interactable anotherObject) {
        return (getExpectedCollider(direction).intersects(anotherObject.getCollider()));
    }

    public boolean willCollideMap(Direction direction, Map map) {
        return map.collideMap(getExpectedCollider(direction));
    }


    public Rectangle getCollider() {
        return new Rectangle(position.getX(), position.getY(), PLAYER_WIDTH, PLAYER_HEIGHT);
    }


    private Rectangle getExpectedCollider(Direction direction) {
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

    private Image getPlayerImage() {
        ImageIcon i = new ImageIcon(getClass().getResource(PLAYER_IMAGE_PATH));
        return i.getImage();
    }
}
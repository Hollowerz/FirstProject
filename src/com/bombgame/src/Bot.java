package com.bombgame.src;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class Bot implements Interactable {

    private static final int BOT_SPEED = 1;
    private int dir;
    public Random randomGenerator = new Random();
    private Position botPosition;
    private static final int BOT_WIDTH = 30;
    private static final int BOT_HEIGHT = 30;
    private Direction botDirection;
    private static final String PLAYER_IMAGE_PATH = "/images/player.jpg";


    public Bot(int x, int y) {
        botPosition = new Position(x, y);
        botDirection = randomiseDirection();

    }

    public Direction randomiseDirection() {
        dir = randomGenerator.nextInt(4);
        if (dir == 0) {
            return Direction.UP;
        }
        if (dir == 1) {
            return Direction.DOWN;
        }
        if (dir == 2) {
            return Direction.RIGHT;
        }
        if (dir == 3) {
            return Direction.LEFT;
        }
        return null;
    }


    public boolean willCollide(Direction direction, Interactable anotherObject) {
        return (getExpectedCollider(direction).intersects(anotherObject.getCollider()));
    }

    public boolean willCollideMap(Direction direction, Map map) {
        return map.collideMap(getExpectedCollider(direction));
    }


    public Rectangle getCollider() {
        return new Rectangle(botPosition.getX(), botPosition.getY(), BOT_WIDTH, BOT_HEIGHT);
    }


    private Rectangle getExpectedCollider(Direction direction) {
        // copy real position to restore after calculations
        Position realPosition = new Position(botPosition);
        // move object with temporary position using provided direction
        moveBot(direction);
        //updateBot(direction);
        // retrieve expected collider
        Rectangle expectedCollider = getCollider();
        // restore old position(object mustn't be moved, this is just a calculations)
        botPosition = realPosition;
        return expectedCollider;
    }


    public void updateBot(Map map) {

        if (!willCollideMap(botDirection, map)) {
            moveBot(botDirection);

        }
        if (willCollideMap(botDirection, map)) {
            botDirection = randomiseDirection();
            if (!willCollideMap(botDirection, map)) {
                moveBot(botDirection);
            }
            map.placeBomb(getX(), getY());
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(getPlayerImage(), botPosition.getX(), botPosition.getY(), null);
        g2d.setColor(Color.BLACK);
        g2d.draw(getCollider());
    }
    public void drawBotDeadMessage(Graphics2D g2d, String botName, int x, int y) {
        g2d.setColor(Color.RED);
        g2d.drawString(botName + " IS DEAD", x,y);
    }

    private Image getPlayerImage() {
        ImageIcon i = new ImageIcon(getClass().getResource(PLAYER_IMAGE_PATH));
        return i.getImage();
    }

    public void moveBot(Direction direction) {
        if (direction == null) return;
        if (direction == Direction.UP) {
            setY(getY() - BOT_SPEED);
        }
        if (direction == Direction.DOWN) {
            setY(getY() + BOT_SPEED);
        }
        if (direction == Direction.LEFT) {
            setX(getX() - BOT_SPEED);
        }
        if (direction == Direction.RIGHT) {
            setX(getX() + BOT_SPEED);
        }

    }


    public int getX() {
        return botPosition.getX();
    }

    public int getY() {
        return botPosition.getY();
    }

    public void setX(int x) {
        this.botPosition.setX(x);
    }

    public void setY(int y) {
        this.botPosition.setY(y);
    }

}




/*package com.bombgame.src;

import java.awt.*;

public class Bomb implements Interactable {
    private Position position;
    private boolean isPlaced;
    private static final int BOMB_SIZE = 35;

    public Bomb(int x, int y) {
        position = new Position(x, y);
        isPlaced = false;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.drawRect(getX(), getY(), BOMB_SIZE, BOMB_SIZE);
        g2d.fillRect(getX(), getY(), BOMB_SIZE, BOMB_SIZE);
    }
    public boolean getStatus() {
        return isPlaced;
    }

    public void setStatus(boolean status) {
        isPlaced = status;
    }
    public void logicBomb() {

    }


    @Override
    public Rectangle getCollider() {
        return new Rectangle(getX(), getY(), BOMB_SIZE, BOMB_SIZE);
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


} */

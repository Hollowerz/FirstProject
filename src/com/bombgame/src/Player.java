package com.bombgame.src;

import java.awt.*;

public class Player extends Person {

    public Player(int x, int y) {
        super(x, y, 2);
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(getImage(), getX(), getY(), null);
        g2d.setColor(Color.BLACK);
        g2d.draw(getCollider());
    }

    public boolean willCollide(Direction direction, Interactable anotherObject) {
        return (getExpectedCollider(direction).intersects(anotherObject.getCollider()));
    }

    public boolean willCollideMap(Direction direction, Map map) {
        return map.collideMap(getExpectedCollider(direction));
    }
}
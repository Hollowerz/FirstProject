package com.bombgame.src;

import com.bombgame.src.util.ImageHolder;

import java.awt.*;


public class MainBot extends Person {
    private final Image image = ImageHolder.getMainBotImage();


    public MainBot(int x, int y) {
        super(x, y, 1);

    }

    public Image getImage() {
        return image;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(getImage(), getX(), getY(), null);

    }


}
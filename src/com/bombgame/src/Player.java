package com.bombgame.src;


import javax.swing.*;
import java.awt.*;


public class Player extends Position {



    public Player(int x, int y) {
        super(x, y);

    }

    public void update(int nx,int ny) {


        x = nx;
        y = ny;

    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(getPlayerImage(), x, y, null);
        g2d.draw(fields());
    }

    public Image getPlayerImage() {
        String mainplayerimage = "/images/player.jpg";
        ImageIcon i = new ImageIcon(getClass().getResource(mainplayerimage));
        return i.getImage();
    }

    public Rectangle fields() {
        return new Rectangle(x, y, 40, 40);
    }


}
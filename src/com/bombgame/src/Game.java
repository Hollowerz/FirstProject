package com.bombgame.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Game extends JPanel implements ActionListener, KeyListener {


    private Timer gameLoopTimer = new Timer(10, this);
    private Player p;
    private Player p2;
    private boolean right, left, up, down;
    private Map map;


    public Game() {
        setFocusable(true);
        gameLoopTimer.start();
        p = new Player(100, 100);
        p2 = new Player(160, 160);
        map = new Map();
        addKeyListener(this);


    }

    /*
    public Image getBackgroudImage() {

        ImageIcon i = new ImageIcon(getClass().getResource("/images/player.jpg"));
        return i.getImage();
    } */


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        setBackground(Color.DARK_GRAY);
        p2.draw(g2d);
        p.draw(g2d);
        map.draw(g2d);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();
        moveLogic();
        p.update();
    }

    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            right = true;
        } else if (key == KeyEvent.VK_DOWN) {
            down = true;
        } else if (key == KeyEvent.VK_UP) {
            up = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            right = false;
        } else if (key == KeyEvent.VK_DOWN) {
            down = false;
        } else if (key == KeyEvent.VK_UP) {
            up = false;
        }
    }

    public void moveLogic() {
        int tempX = p.x;
        int tempY = p.y;
        int pSpeed = 2;
        int ny = 0;
        int nx = 0;

        if (right) nx += pSpeed;
        if (left) nx -= pSpeed;
        if (up) ny -= pSpeed;
        if (down) ny += pSpeed;
        if (!collision(nx, ny)) {
            p.update(nx, ny);
        }
        if (collision(nx, ny)) {
            p.update(tempX, tempY);
        }

    }


    public boolean collision(int nx, int ny) {
        Rectangle rect = new Rectangle(nx, ny, 40, 40);
        return (rect.intersects(p2.fields()));
    }



}

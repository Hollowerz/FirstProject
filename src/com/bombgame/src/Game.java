package com.bombgame.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



public class Game extends JPanel implements ActionListener, KeyListener {

    private Timer gameLoopTimer = new Timer(10, this);
    private Player player;
    private Player bot;
    private Direction direction;
    private boolean gameRunning = true;


    private Map map;

    public Game() {
        setFocusable(true);
        gameLoopTimer.start();
        player = new Player(40, 40);
        bot = new Player(362, 362);
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
        bot.draw(g2d);
        map.draw(g2d);
        player.draw(g2d);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            repaint();
            if ((!player.willCollide(direction, bot)) && (!player.willCollideMap(direction, map))) {
                player.move(direction);
            }
            if ((player.willCollide(direction, bot))) {
                gameRunning = false;
                JOptionPane.showMessageDialog(null, "Вы сделали кусь, спасибо!");

            }
        }


    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                direction = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                direction = Direction.RIGHT;
                break;
            case KeyEvent.VK_DOWN:
                direction = Direction.DOWN;
                break;
            case KeyEvent.VK_UP:
                direction = Direction.UP;
                break;
            case KeyEvent.VK_SPACE:
                map.placeBomb(player.getX(), player.getY());


            default:
                direction = null;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        direction = null;
    }
}

package com.bombgame.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Game extends JPanel implements ActionListener, KeyListener {

    private Timer gameLoopTimer;
    private Player player;
    private Bot bot;
    private Bot bot2;
    private Direction direction;

    private boolean gameRunning = true;
    private boolean bot1Alive = true;
    private boolean bot2Alive = true;


    private Map map;

    public Game() {
        setFocusable(true);
        player = new Player(45, 45);
        bot = new Bot(410, 410);
        bot2 = new Bot(46, 410);
        map = new Map();
        addKeyListener(this);
        gameLoopTimer = new Timer(10, this);
        gameLoopTimer.start();


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
        map.drawMap(g2d);

        setBackground(Color.DARK_GRAY);

        map.draw(g2d);
        if (bot1Alive) {
            bot.draw(g2d);
        } else bot.drawBotDeadMessage(g2d, "BOT 1",8,12);
        if (bot2Alive) {
            bot2.draw(g2d);
        } else bot.drawBotDeadMessage(g2d, "BOT 2",8,27);
        player.draw(g2d);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {

            repaint();

            //updating bots if they are alive
            if (bot2Alive) bot2.updateBot(map);
            if (bot1Alive) bot.updateBot(map);
            //player movement checking for collision
            if ((!player.willCollide(direction, bot)) && (!player.willCollideMap(direction, map))) {
                player.move(direction);
            }

            //checking collision for bots with player
            if ((player.willCollide(direction, bot)) || (bot.willCollide(direction, player))) {
                gameRunning = false;
                JOptionPane.showMessageDialog(null, "YOU DEAD");

            }
            if (map.collideExplosion(player.getCollider())) {
                JOptionPane.showMessageDialog(null, "SUICIDE, GOOD JOB");
                gameRunning = false;
            }
            if (map.collideExplosion(bot2.getCollider())) {
                //gameRunning = false;
                bot2Alive = false;


                //JOptionPane.showMessageDialog(null, "PLAYER 1 WINS");
            }
            if (map.collideExplosion(bot.getCollider())) {
                bot1Alive = false;

            }
            if (!bot1Alive && !bot2Alive) {
                gameRunning = false;
                JOptionPane.showMessageDialog(null, "YOU WIN");
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

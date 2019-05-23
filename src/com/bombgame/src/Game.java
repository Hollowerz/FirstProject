package com.bombgame.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;


public class Game extends JPanel implements ActionListener, KeyListener {

    private Timer gameLoopTimer = new Timer(10, this);
    private Player player = new Player(45, 45);
    private List<Bot> botList;
    private Direction direction;

    private boolean gameRunning = true;
    private Map map = new Map();
    private byte deadBots = 0;

    public Game() {
        setFocusable(true);
        createBots();
        addKeyListener(this);
        gameLoopTimer.start();
    }

    private void createBots() {
        botList = Arrays.asList(
                new Bot(410, 410),
                new Bot(46, 410)
        );

        for (int i = 0; i < botList.size(); i++) {
            botList.get(i).setName("Bot #" + (i + 1));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        map.drawMap(g2d);

        setBackground(Color.DARK_GRAY);

        map.draw(g2d);
        player.draw(g2d);

        botList.forEach(bot -> {
            if (bot.isAlive()) {
                bot.draw(g2d);
            } else {
                drawBotDeadMessage(g2d, bot.getName(), 3, (deadBots + 1) * 13);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            repaint();
            //updating bots if they are alive
            botList.stream().filter(Person::isAlive).forEach(bot -> bot.update(map));

            //player movement checking for collision
            if (botList.stream().filter(Person::isAlive).noneMatch(bot -> player.willCollide(direction, bot))
                    && !player.willCollideMap(direction, map)) {
                player.move(direction);
            }

            //checking collision for bots with player & explosion
            botList.stream().filter(Person::isAlive).forEach(bot -> {
                if (player.willCollide(direction, bot) || bot.willCollide(direction, player)) {
                    gameRunning = false;
                    JOptionPane.showMessageDialog(null, "YOU DEAD");
                }
                if (map.collideExplosion(bot.getCollider())) {
                    bot.setAlive(false);
                    deadBots++;
                }
            });

            if (map.collideExplosion(player.getCollider())) {
                JOptionPane.showMessageDialog(null, "SUICIDE, GOOD JOB");
                gameRunning = false;
            }

            if (botList.stream().noneMatch(Person::isAlive)) {
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

    private void drawBotDeadMessage(Graphics2D g2d, String botName, int x, int y) {
        g2d.setColor(Color.RED);
        g2d.drawString(botName + " IS DEAD", x, y);
    }
}

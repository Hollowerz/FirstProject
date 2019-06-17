package com.bombgame.src;

import com.bombgame.src.enums.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;


public class Game extends JPanel implements ActionListener, KeyListener {

    private Timer gameLoopTimer = new Timer(10, this);
    private Player player = new Player(45, 45);
    private List<Bot> botList;
    private Direction direction;

    private boolean gameRunning;
    private Map map = new Map();
    private byte deadBots = 0;
    private int botCount;

    public Game(String botCountString) {
        botCount = Integer.parseInt(botCountString);
        initGame();
    }

    private void initGame() {
        setFocusable(true);
        createBots();
        addKeyListener(this);
        gameLoopTimer.start();
        gameRunning = true;
    }

    private void createBots() {
        botList = new ArrayList();
        for (int x = 0; x < botCount; x++) {
            botList.add(new Bot(x * 90 + 55, 200));

        }
        for (int i = 0; i < botList.size(); i++) {
            botList.get(i).setName("Bot " + (i + 1));
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
                drawBotDeadMessage(g2d, 5, 20);

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
                //JOptionPane.showMessageDialog(null, "SUICIDE, GOOD JOB");
                gameRunning = false;

            }

            if (botList.stream().noneMatch(Person::isAlive)) {
                gameRunning = false;
                JOptionPane.showMessageDialog(null, "YOU WIN");

            }
        } else {
            int input = JOptionPane.showConfirmDialog(null, "Restart simmulation", "End of simmulation", 2);
            if (input == 0) {
                initGame();
            } else System.exit(0);
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

    private void drawBotDeadMessage(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.WHITE);
        g2d.drawString(deadBots + " BOTS IS DEAD", x, y);
    }
}

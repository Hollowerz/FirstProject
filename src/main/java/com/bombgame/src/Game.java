package com.bombgame.src;

import com.bombgame.src.enums.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class Game extends JPanel implements ActionListener {

    private Timer gameLoopTimer = new Timer(10, this);
    private MainBot mainBot = new MainBot(45, 45);
    private List<Bot> botList = new ArrayList<>();
    private Direction direction;
    private boolean gameRunning;
    private Map map;
    private byte deadBots;
    private int botCount;

    public Game() {
        initGame();
    }

    private void initGame() {
        map = new Map();
        setBotCount();
        setFocusable(true);
        createBots();
        //addKeyListener(this);  this code is for adding controls for player
        gameLoopTimer.start();
        gameRunning = true;
        deadBots = 0;
    }

    private void setBotCount() {
        final String[] bots = {"1", "2", "3", "4", "5"};
        String botCountString = (String) JOptionPane.showInputDialog(null,
                "How many little bots you want?",
                "Bot count",
                JOptionPane.QUESTION_MESSAGE,
                null,
                bots,
                bots[0]);
        botCount = Integer.parseInt(botCountString);
    }

    private void createBots() {
        //botList = new ArrayList();
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
        mainBot.draw(g2d);

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
            botList.stream().filter(Bot::isAlive).forEach(bot -> bot.update(map));
            mainBot.update(map);

            //player movement checking for collision
            if (botList.stream().filter(Person::isAlive).noneMatch(bot -> mainBot.willCollide(direction, bot))
                    && !mainBot.willCollideMap(direction, map)) {
                mainBot.move(direction);
            }

            //checking collision for bots with player & explosion
            botList.stream().filter(Person::isAlive).forEach(bot -> {
                if (mainBot.willCollide(direction, bot) || bot.willCollide(direction, mainBot)) {
                    bot.setAlive(false);
                    deadBots++;
                }
                if (map.collideExplosion(bot.getCollider())) {
                    bot.setAlive(false);
                    deadBots++;
                }
            });

            if (botList.stream().noneMatch(Person::isAlive)) {
                gameRunning = false;
                JOptionPane.showMessageDialog(null, "EVERY BLACK BOTS IS DEAD");

            }
        } else {
            int input = JOptionPane.showConfirmDialog(null, "Restart simmulation", "End of simmulation", 2);
            if (input == 0) {
                botList.clear();
                initGame();
            } else System.exit(0);
        }
    }

    private void drawBotDeadMessage(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.WHITE);
        g2d.drawString(deadBots + " BOTS IS DEAD", x, y);
    }

}

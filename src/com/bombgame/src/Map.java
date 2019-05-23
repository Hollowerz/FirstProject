package com.bombgame.src;


import com.bombgame.src.util.ImageHolder;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Map {

    private static final int MAP_SIZE = 11;
    private static final int TILE_SIZE = 45;
    private int[][] map = new int[MAP_SIZE][MAP_SIZE];
    private static final int BLOCKED = 1;
    private static final int BOMB = 2;
    private static final int CLEAR = 3;
    private static final int EXPLODE_BLOCK_WAVE = 4;
    private List<Rectangle> platforms = new CopyOnWriteArrayList<>();
    private List<Rectangle> bombs = new CopyOnWriteArrayList<>();
    private List<Rectangle> explosion = new CopyOnWriteArrayList<>();
    private boolean nowExploding = false;
    private static final int BOMB_EXPLODE_RADIUS = 3;
    private static final int BOMB_COUNT = 3;
    private final Image boomImage = ImageHolder.getBoomImage();
    private final Image blockImage = ImageHolder.getBlockImage();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);


    public Map() {
        for (int kolumna = 0; kolumna < MAP_SIZE; kolumna++) {
            for (int wiersz = 0; wiersz < MAP_SIZE; wiersz++) {
                map[kolumna][wiersz] = CLEAR;
            }
            initPlatforms();
            createObjectList();
        }
    }

    public void createObjectList() {
        for (int kolumna = 0; kolumna < MAP_SIZE; kolumna++) {
            for (int wiersz = 0; wiersz < MAP_SIZE; wiersz++) {
                if (map[kolumna][wiersz] == BLOCKED) {
                    Rectangle platform = new Rectangle(kolumna * TILE_SIZE, wiersz * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    platforms.add(platform);
                }
                if (map[kolumna][wiersz] == BOMB) {
                    Rectangle bomb = new Rectangle(kolumna * TILE_SIZE, wiersz * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    bombs.add(bomb);
                }
                if (map[kolumna][wiersz] == EXPLODE_BLOCK_WAVE) {
                    Rectangle explodeWave = new Rectangle(kolumna * TILE_SIZE, wiersz * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    explosion.add(explodeWave);
                }
            }
        }
    }

    public void draw(Graphics2D g2d) {
        /*for (Rectangle platform : platforms) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(platform.x, platform.y, TILE_SIZE, TILE_SIZE);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(platform.x, platform.y, TILE_SIZE, TILE_SIZE);

            //g2d.drawImage(getBlockImage(), platform.x, platform.y, null);
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, 100, 30);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(0,0,100,30);
        } */
        for (Rectangle bomb : bombs) {
            g2d.setColor(Color.RED);
            g2d.fillRect(bomb.x, bomb.y, TILE_SIZE, TILE_SIZE);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(bomb.x, bomb.y, TILE_SIZE, TILE_SIZE);
        }
        if (nowExploding) {
            for (Rectangle explosion : explosion) {
                //g2d.setColor(Color.ORANGE);
                //g2d.fillRect(explosion.x, explosion.y, TILE_SIZE, TILE_SIZE);
                g2d.drawImage(boomImage, explosion.x, explosion.y, null);


            }

        }
    }

    public void drawMap(Graphics2D g2d) {
        for (Rectangle platform : platforms) {
            //g2d.setColor(Color.LIGHT_GRAY);
            //g2d.fillRect(platform.x, platform.y, TILE_SIZE, TILE_SIZE);
            //g2d.setColor(Color.BLACK);
            //g2d.drawRect(platform.x, platform.y, TILE_SIZE, TILE_SIZE);

            g2d.drawImage(blockImage, platform.x, platform.y, null);
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, 100, 30);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(0, 0, 100, 30);
        }
    }

    public boolean collideMap(Rectangle player) {
        boolean collide = false;

        for (Rectangle platform : platforms) {
            if (platform.intersects(player)) collide = true;
        }
        return collide;
    }

    public void initPlatforms() {
        for (int kolumna = 0; kolumna < MAP_SIZE; kolumna++) {
            for (int wiersz = 0; wiersz < MAP_SIZE; wiersz++) {
                map[0][wiersz] = BLOCKED;
                map[kolumna][0] = BLOCKED;
                map[MAP_SIZE - 1][wiersz] = BLOCKED;
                map[kolumna][MAP_SIZE - 1] = BLOCKED;
                if (kolumna < MAP_SIZE / 2) {
                    map[kolumna * 2][2] = BLOCKED;
                    map[kolumna * 2][4] = BLOCKED;
                    map[kolumna * 2][6] = BLOCKED;
                    map[kolumna * 2][8] = BLOCKED;
                }

            }
        }
    }

    public void placeBomb(int x, int y) {

        int bX = x / TILE_SIZE;
        int bY = y / TILE_SIZE;
        int bombPlacedCount = 0;
        for (int kolumna = 0; kolumna < MAP_SIZE; kolumna++) {
            for (int wiersz = 0; wiersz < MAP_SIZE; wiersz++) {
                if (map[kolumna][wiersz] == BOMB) bombPlacedCount++;
            }
        }
        if (bombPlacedCount < BOMB_COUNT) {
            map[bX][bY] = BOMB;
            createObjectList();
        }


        Runnable explodeBomb = () -> {
            explode(bX, bY);
            bombs.clear();
            createObjectList();

        };
        executor.schedule(explodeBomb, 2000, TimeUnit.MILLISECONDS);

    }

    public void explode(int x, int y) {
        nowExploding = true;
        map[x][y] = EXPLODE_BLOCK_WAVE;
        for (int j = 0; j < BOMB_EXPLODE_RADIUS; j++) {
            if (map[x - j][y] != BLOCKED) {
                map[x - j][y] = EXPLODE_BLOCK_WAVE;
            } else break;
        }
        for (int j = 0; j < BOMB_EXPLODE_RADIUS; j++) {
            if (map[x + j][y] != BLOCKED) {
                map[x + j][y] = EXPLODE_BLOCK_WAVE;
            } else break;
        }

        for (int k = 0; k < BOMB_EXPLODE_RADIUS; k++) {
            if (map[x][y - k] != BLOCKED) {
                map[x][y - k] = EXPLODE_BLOCK_WAVE;
            } else break;
        }
        for (int k = 0; k < BOMB_EXPLODE_RADIUS; k++) {
            if (map[x][y + k] != BLOCKED) {
                map[x][y + k] = EXPLODE_BLOCK_WAVE;
            } else break;
        }
        Runnable explodeBomb = () -> {

            for (int kolumna = 0; kolumna < MAP_SIZE; kolumna++) {
                for (int wiersz = 0; wiersz < MAP_SIZE; wiersz++) {
                    if (map[kolumna][wiersz] == EXPLODE_BLOCK_WAVE) map[kolumna][wiersz] = CLEAR;
                }
            }
            explosion.clear();
            createObjectList();

        };
        executor.schedule(explodeBomb, 400, TimeUnit.MILLISECONDS);
    }

    public boolean collideExplosion(Rectangle player) {
        boolean collide = false;

        for (Rectangle explosion : explosion) {
            if (explosion.intersects(player)) collide = true;
        }
        return collide;
    }


}
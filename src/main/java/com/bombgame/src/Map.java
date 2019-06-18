package com.bombgame.src;


import com.bombgame.src.enums.MapTile;
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
    private MapTile[][] map = new MapTile[MAP_SIZE][MAP_SIZE];
    private List<Rectangle> platforms = new CopyOnWriteArrayList<>();
    private List<Rectangle> bombs = new CopyOnWriteArrayList<>();
    private List<Rectangle> explosion = new CopyOnWriteArrayList<>();
    private boolean nowExploding = false;
    private boolean bombPlaced;
    private static final int BOMB_EXPLODE_RADIUS = 3;
    private static final int BOMB_COUNT = 3;
    private final Image boomImage = ImageHolder.getBoomImage();
    private final Image blockImage = ImageHolder.getBlockImage();
    private final Image bombImage = ImageHolder.getBombImage();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    private int bombPlacedCount = 0;


    public Map() {
        for (int row = 0; row < MAP_SIZE; row++) {
            for (int column = 0; column < MAP_SIZE; column++) {
                map[row][column] = MapTile.CLEAR;
            }
            initPlatforms();
            createObjectList();
        }
    }

    public void createObjectList() {
        platforms.clear();
        bombs.clear();
        explosion.clear();
        for (int row = 0; row < MAP_SIZE; row++) {
            for (int column = 0; column < MAP_SIZE; column++) {
                if (map[row][column] == MapTile.BLOCKED) {
                    Rectangle platform = new Rectangle(row * TILE_SIZE, column * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    platforms.add(platform);
                }
                if (map[row][column] == MapTile.BOMB) {
                    Rectangle bomb = new Rectangle(row * TILE_SIZE, column * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    bombs.add(bomb);
                }
                if (map[row][column] == MapTile.EXPOSION_WAVE) {
                    Rectangle explodeWave = new Rectangle(row * TILE_SIZE, column * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    explosion.add(explodeWave);
                }
            }
        }
    }


    public void draw(Graphics2D g2d) {
        for (Rectangle bomb : bombs) {
            g2d.drawImage(bombImage, bomb.x, bomb.y, null);
        }
        if (nowExploding) {
            for (Rectangle explosion : explosion) {
                g2d.drawImage(boomImage, explosion.x, explosion.y, null);
            }
        }

    }

    public void drawMap(Graphics2D g2d) {
        for (Rectangle platform : platforms) {
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
                map[0][wiersz] = MapTile.BLOCKED;
                map[kolumna][0] = MapTile.BLOCKED;
                map[MAP_SIZE - 1][wiersz] = MapTile.BLOCKED;
                map[kolumna][MAP_SIZE - 1] = MapTile.BLOCKED;
                if (kolumna < MAP_SIZE / 2) {
                    map[kolumna * 2][2] = MapTile.BLOCKED;
                    map[kolumna * 2][4] = MapTile.BLOCKED;
                    map[kolumna * 2][6] = MapTile.BLOCKED;
                    map[kolumna * 2][8] = MapTile.BLOCKED;
                }

            }
        }
    }

    public void placeBomb(int x, int y) {
        int bX = x / TILE_SIZE;
        int bY = y / TILE_SIZE;
        for (int kolumna = 0; kolumna < MAP_SIZE; kolumna++) {
            for (int wiersz = 0; wiersz < MAP_SIZE; wiersz++) {
                if (map[kolumna][wiersz] == MapTile.BOMB) bombPlacedCount++;
            }
        }
        if (bombPlacedCount < BOMB_COUNT) {
            map[bX][bY] = MapTile.BOMB;
            createObjectList();
            bombPlaced = true;
        }


        Runnable explodeBomb = () -> {
            if (map[bX][bY] == MapTile.BOMB) explode(bX, bY);
            bombs.clear();
            createObjectList();
            bombPlacedCount--;
        };
        executor.schedule(explodeBomb, 2000, TimeUnit.MILLISECONDS);


    }

    public void explode(int x, int y) {

        nowExploding = true;
        map[x][y] = MapTile.EXPOSION_WAVE;
        for (int j = 0; j < BOMB_EXPLODE_RADIUS; j++) {
            if (map[x - j][y] != MapTile.BLOCKED) {
                map[x - j][y] = MapTile.EXPOSION_WAVE;
            } else break;
        }
        for (int j = 0; j < BOMB_EXPLODE_RADIUS; j++) {
            if (map[x + j][y] != MapTile.BLOCKED) {
                map[x + j][y] = MapTile.EXPOSION_WAVE;
            } else break;
        }

        for (int k = 0; k < BOMB_EXPLODE_RADIUS; k++) {
            if (map[x][y - k] != MapTile.BLOCKED) {
                map[x][y - k] = MapTile.EXPOSION_WAVE;
            } else break;
        }
        for (int k = 0; k < BOMB_EXPLODE_RADIUS; k++) {
            if (map[x][y + k] != MapTile.BLOCKED) {
                map[x][y + k] = MapTile.EXPOSION_WAVE;
            } else break;
        }
        Runnable explodeBomb = () -> {

            for (int kolumna = 0; kolumna < MAP_SIZE; kolumna++) {
                for (int wiersz = 0; wiersz < MAP_SIZE; wiersz++) {
                    if (map[kolumna][wiersz] == MapTile.EXPOSION_WAVE) map[kolumna][wiersz] = MapTile.CLEAR;
                }
            }
            explosion.clear();
            createObjectList();
            nowExploding = false;
        };
        executor.schedule(explodeBomb, 500, TimeUnit.MILLISECONDS);

    }

    public boolean collideExplosion(Rectangle player) {
        boolean collide = false;

        for (Rectangle explosion : explosion) {
            if (explosion.intersects(player)) collide = true;
        }
        return collide;
    }


}
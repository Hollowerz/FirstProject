package com.bombgame.src;


import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Map {

    private static final int MAP_SIZE = 11;
    private static final int TILE_SIZE = 40;
    private int[][] map = new int[MAP_SIZE][MAP_SIZE];
    private static final int BLOCKED = 1;
    private static final int BOMB = 2;
    private static final int CLEAR = 3;
    private ArrayList<Rectangle> platforms = new ArrayList<>();
    private ArrayList<Rectangle> bombs = new ArrayList<>();
    public boolean nowExploding = false;
    private static final int BOMB_EXPLODE_RADIUS = 3;


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
                } if (map[kolumna][wiersz] == BOMB) {
                    Rectangle bomb = new Rectangle (kolumna * TILE_SIZE, wiersz * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    bombs.add(bomb);
                }
            }
        }
    }

    public void draw(Graphics2D g2d) {
        for (Rectangle platform : platforms) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(platform.x, platform.y, TILE_SIZE, TILE_SIZE);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(platform.x, platform.y, TILE_SIZE, TILE_SIZE);
        } for (Rectangle bomb: bombs){
            g2d.setColor(Color.RED);
            g2d.fillRect(bomb.x, bomb.y, TILE_SIZE, TILE_SIZE);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(bomb.x, bomb.y, TILE_SIZE, TILE_SIZE);
        }
        if (nowExploding) {
            //animation expl
        }

    }

    public boolean collideMap(Rectangle player) {
        boolean collide = false;

        for (Rectangle platform : platforms) {
            if (platform.intersects(player)) collide = true;
        }
        return collide;
    }
    public void initPlatforms (){
        for (int kolumna = 0; kolumna < MAP_SIZE; kolumna++) {
            for (int wiersz = 0; wiersz < MAP_SIZE; wiersz++) {
                //map[kolumna][wiersz] = CLEAR;
                map[0][wiersz] = BLOCKED;
                map[kolumna][0] = BLOCKED;
                map[MAP_SIZE-1][wiersz] = BLOCKED;
                map[kolumna][MAP_SIZE-1] = BLOCKED;
                if ( kolumna < MAP_SIZE/2) {
                    map[kolumna*2][2] = BLOCKED;
                    map[kolumna*2][4] = BLOCKED;
                    map[kolumna*2][6] = BLOCKED;
                    map[kolumna*2][8] = BLOCKED;
                }

            }
        }
    }

    public void placeBomb ( int x, int y){

        int bX = x/TILE_SIZE;
        int bY = y/TILE_SIZE;
        map[bX][bY] = BOMB;
        createObjectList();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable explodeBomb = () -> {
            explode();
            map[bX][bY] = CLEAR;
            bombs.clear();
            createObjectList();
        };
        executor.schedule(explodeBomb, 2, TimeUnit.SECONDS);

    }

    public void explode (){
        nowExploding = true;
        // expolde bomb, animation, bomb explode radius;
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable explodeBomb = () -> {
            //task
        };
        executor.schedule(explodeBomb, 2, TimeUnit.SECONDS);
    }




}
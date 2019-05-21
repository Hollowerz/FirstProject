package com.bombgame.src;


import java.awt.*;
import java.util.ArrayList;

public class Map {

    private static final int MAP_SIZE = 10;
    private static final int TILE_SIZE = 40;
    private int[][] map = new int[MAP_SIZE][MAP_SIZE];
    private static final int BLOCKED = 1;
    private ArrayList<Rectangle> platforms = new ArrayList<>();


    public Map() {
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                map[0][y] = BLOCKED;
                map[y][0] = BLOCKED;
            }
        }
        createPlatformList();
    }

    public void createPlatformList() {
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                if (map[x][y] == BLOCKED) {
                    Rectangle platform = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    platforms.add(platform);
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
        }
    }

    public boolean collideMap(Rectangle player) {
        boolean collide = false;

        for (Rectangle platform : platforms) {
            if (platform.intersects(player)) collide = true;
        }
        return collide;
    }


}
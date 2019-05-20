package com.bombgame.src;

import java.awt.*;

public class Map  {

    private static final int sizeOfMap = 10;
    private static final int sizeOfTile = 40;
    private int[][] map = new int[sizeOfMap][sizeOfMap];
    private static final int BLOCKED = 1;

    public Map() {

        for (int x = 0; x < sizeOfMap; x++) {
            for (int y = 0; y < sizeOfMap; y++) {
                map[0][y] = BLOCKED;
            }
        }
    }


    public void draw(Graphics2D g2d) {


        for (int x = 0; x < sizeOfMap; x++) {
            for (int y = 0; y < sizeOfMap; y++) {
                if (map[x][y] == BLOCKED) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(x * sizeOfTile, y * sizeOfTile, sizeOfTile, sizeOfTile);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x * sizeOfTile, y * sizeOfTile, sizeOfTile, sizeOfTile);
                }


            }
        }

    }



}
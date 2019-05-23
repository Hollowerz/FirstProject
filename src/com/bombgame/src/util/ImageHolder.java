package com.bombgame.src.util;

import javax.swing.*;
import java.awt.*;

public class ImageHolder {
    private static final String PLAYER_IMAGE_PATH = "/images/player.jpg";
    private static final String BOOM_IMAGE_PATH = "/images/boom.jpg";
    private static final String BLOCK_IMAGE_PATH = "/images/block.jpg";

    private static class PlayerImage {
        private static final Image image = new ImageIcon(ImageHolder.class.getResource(PLAYER_IMAGE_PATH)).getImage();
    }

    private static class BoomImage {
        private static final Image image = new ImageIcon(ImageHolder.class.getResource(BOOM_IMAGE_PATH)).getImage();
    }

    private static class BlockImage {
        private static final Image image = new ImageIcon(ImageHolder.class.getResource(BLOCK_IMAGE_PATH)).getImage();
    }

    public static Image getPlayerImage() {
        return PlayerImage.image;
    }

    public static Image getBoomImage() {
        return BoomImage.image;
    }

    public static Image getBlockImage() {
        return BlockImage.image;
    }
}

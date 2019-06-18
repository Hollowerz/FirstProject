package com.bombgame.src;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Bomberman clone");
        frame.pack();
        frame.setSize(500, 520);

        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  //position of window
        frame.add(new Game());
        frame.setVisible(true);
    }
}

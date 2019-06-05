package com.bombgame.src;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bomberman clone");
        frame.pack();
        frame.setSize(520, 540);

        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setBounds(100,100,500,500);  // position
        frame.setLocationRelativeTo(null);  //position of window
        frame.add(new Game());
        frame.setVisible(true);
    }
}

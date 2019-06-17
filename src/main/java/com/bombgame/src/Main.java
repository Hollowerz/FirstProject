package com.bombgame.src;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Bomberman clone");
        frame.pack();
        frame.setSize(500, 600);

        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  //position of window
        final String[] bots = { "1", "2", "3", "4", "5" };
        String botCount = (String) JOptionPane.showInputDialog(frame,
                "Ile chcesz botów?",
                "Ustaw ilość botów",
                JOptionPane.QUESTION_MESSAGE,
                null,
                bots,
                bots[0]);
        frame.add(new Game(botCount));
        frame.setVisible(true);
    }
}

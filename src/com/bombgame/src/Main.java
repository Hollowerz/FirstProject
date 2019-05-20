package com.bombgame.src;



import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame( "My first game");
        frame.pack();
        frame.setSize(500, 500);

        frame.setResizable( false );

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setBounds(100,100,500,500);  // position
        frame.setLocationRelativeTo(null);  //position of window
        frame.add( new Game());
        frame.setVisible( true );





    }
}

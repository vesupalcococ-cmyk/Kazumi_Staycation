/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.main;

import javax.swing.SwingUtilities;

/**
 *
 * @author Ysha
 */
public class Main {

    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> {
            Home_Page home = new Home_Page();
            home.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
            home.setLocationRelativeTo(null);
            home.setVisible(true);
        });
    }
}

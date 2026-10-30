package com.mycompany.main;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Ysha
 */
public class UserSession {
    private static int userId;
    private static String username;

    public static void setUser(int id, String name) {
        userId = id;
        username = name;
    }

    public static int getUserId() { return userId; }
    public static String getUsername() { return username; }

    public static void clear() {
        userId = 0;
        username = null;
    }
}

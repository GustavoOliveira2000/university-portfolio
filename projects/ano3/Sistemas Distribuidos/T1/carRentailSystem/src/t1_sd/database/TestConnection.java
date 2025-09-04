/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package t1_sd.database;

/**
 *
 * @author gustavooliveira
 * 
 *  */

public class TestConnection {
    public static void main(String[] args) {
        PostgresConnect pgConnect = new PostgresConnect("/Users/gustavooliveira/Desktop/WORKSPACE/SD/T1-46395-SD/carRentailSystem/src/resorces/configs.properties");
        pgConnect.connect();
        pgConnect.disconnect();
    }
}
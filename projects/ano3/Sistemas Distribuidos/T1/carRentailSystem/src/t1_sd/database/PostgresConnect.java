/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package t1_sd.database;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author gustavooliveira
 */

public class PostgresConnect 
{
    private Connection connection;
    private String url;
    private String user;
    private String password;

    public PostgresConnect(String configFile) 
    {
        try (FileInputStream input = new FileInputStream(configFile)) 
        {
            Properties prop = new Properties();
            prop.load(input);

            String host = prop.getProperty("host");
            String port = prop.getProperty("port");
            String db = prop.getProperty("db");
            this.user = prop.getProperty("user");
            this.password = prop.getProperty("password");

            this.url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o arquivo de configuração.");
        }
    }

    public void connect() 
    {
        try 
        {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
        
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Falha ao conectar ao banco de dados.");
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados encerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

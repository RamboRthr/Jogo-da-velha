package com.teste.teste;

import java.sql.*;

public class DataAcces {

    private Connection con;

    public  DataAcces(){

    }
    public Connection init(){
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres", "postgres", "postgres");
            System.out.println("Java JDBC PostgreSQL Example");
            Class.forName("org.postgresql.Driver");
            System.out.println("Connected to PostgreSQL database!");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.con = connection;
        return connection;
    }
    public Connection getCon() {
        return con;
    }
    public ResultSet selectZuege() throws SQLException{
        Statement statement = getCon().createStatement();
        return statement.executeQuery("SELECT * FROM public.logs");
    }
    public void insertZug(Play jogada) throws SQLException {
        //prepared statement
        var preparedStatement = getCon().prepareStatement("insert into zuege (id,spieler,position) values (?,?,?)");
        // preenche os valores
        preparedStatement.setInt(1, jogada.getId());
        preparedStatement.setString(2, jogada.getJogador());
        preparedStatement.setInt(3, jogada.getPosicao());
        // executa
        preparedStatement.execute();
        preparedStatement.close();
    }
    public void insertResultat(Result result) throws SQLException {
        var preparedStatement = getCon().prepareStatement("insert into resultate (gewinner, zuege_anzahl,unentschieden) values (?,?,?)");
        // preenche os valores
        preparedStatement.setString(1, result.getWinner());
        preparedStatement.setInt(2, result.getNbr_plays());
        preparedStatement.setBoolean(3, result.isDraw());
        // executa
        preparedStatement.execute();
        preparedStatement.close();
    }
    public void deleteZuege() throws SQLException {
        var preparedStatement = getCon().prepareStatement("delete from zuege");
        preparedStatement.execute();
        preparedStatement.close();
    }
}

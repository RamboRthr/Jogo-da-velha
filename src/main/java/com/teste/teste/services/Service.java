package com.teste.teste.services;
import com.teste.teste.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.sql.SQLException;

@RestController
@RequestMapping("/service")
public class Service {

    Board board;
    Rules rules;
    Result result;

    public static DataAcces dataAcces = new DataAcces();

    @GetMapping
    public ResponseEntity<String> iniciar(String msg){
        String res = "Iniciando Jogo da velha\n" +
                "Vez de X\nAltere para POST e escolha uma posição:";

        board = new Board();
        rules = new Rules();
        dataAcces.init();

        return new ResponseEntity(res, HttpStatus.OK); //Startet den Spiel
    }

    @PostMapping
    public ResponseEntity<String> post(@RequestBody Play obj){

        var play = new Play(rules.jogada, rules.marca, obj.getPosicao());


        int linha = setPos(obj.getPosicao())[0];
        int coluna = setPos(obj.getPosicao())[1];
        if (!validPosition(board, linha, coluna)){ //Verifiziert ob die gegebene Position nicht außerhalb des Bereichs oder nicht belegt ist.
            return new ResponseEntity<>("Posição inválida, tente novamente.", HttpStatus.OK);
        }
        else {

            board.board[linha][coluna] = rules.marca; //Füllt den Board in der gegebenen Position
            try {
                dataAcces.insertZug(new Play(rules.jogada, rules.marca, obj.getPosicao()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            rules.jogada++;

            if (rules.jogada == 9){//Verifiziert ob es kein Unentschieden gibt

                try {
                    dataAcces.insertResultat(new Result(null, rules.jogada, true));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return new ResponseEntity("Empate! Fim de jogo.", HttpStatus.OK);

            } else if (verifyIfIsOver(board.board)) {
                try {
                    dataAcces.deleteZuege();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                rules.vencedor = rules.marca;
                String str = rules.vencedor + " venceu!\nPara jogar novamente, altere para GET.";
                if (rules.marca.equalsIgnoreCase("X")){
                    rules.vencedor = "X";
                    try {
                        dataAcces.insertResultat(new Result(rules.vencedor, rules.jogada, false));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return new ResponseEntity(str, HttpStatus.OK);
                }
                else {
                    rules.vencedor = "O";
                    try {
                        dataAcces.insertResultat(new Result(rules.vencedor, rules.jogada, false));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return new ResponseEntity(str, HttpStatus.OK);
                }
            }
            else {

                if (rules.marca.equalsIgnoreCase("X")) {
                    rules.marca = "O";
                } else {
                    rules.marca = "X";
                }

                return new ResponseEntity(boardToString() + "\n" + rules.marca + " Escolheu a posição " + obj.getPosicao()
                        + "\nVez de " + rules.marca, HttpStatus.OK);
            }
        }
    }
    @RequestMapping("/overview")
    @GetMapping
    public ResponseEntity<String> boardOverview(String msg){
        return new ResponseEntity(boardToString(), HttpStatus.OK);
    }
    private static int[] setPos (int pos){ //bekommt die Nummer der Position und gibt ihre Zeile und Spalte zurück.
        int linha;
        int coluna;
        switch (pos){
            case 1:
                linha = 0;
                coluna = 0;
                break;

            case 2:
                linha = 0;
                coluna = 1;
                break;

            case 3:
                linha = 0;
                coluna = 2;
                break;

            case 4:
                linha = 1;
                coluna = 0;
                break;

            case 5:
                linha = 1;
                coluna = 1;
                break;

            case 6:
                linha = 1;
                coluna = 2;
                break;

            case 7:
                linha = 2;
                coluna = 0;
                break;

            case 8:
                linha = 2;
                coluna = 1;
                break;

            case 9:
                linha = 2;
                coluna = 2;
                break;

            default:
                linha = -1;
                coluna = -1;
        }
        return new int[]{linha, coluna};
    }
    private String boardToString(){//liest den Board durch und gibt ihr String zurück
        String boardToString = "";
        for (int i = 0; i < board.board.length; i++) {
            for (int j = 0; j < board.board[i].length; j++) {
                boardToString += board.board[i][j] + " |";
            }
            boardToString += "\n";
        }
        return boardToString;
    }
    private static boolean verifyIfIsOver(String[][] board){ //Verifiziert ob jemand gewonnen hat

        if(board[0][0] == board[0][1] && board[0][1] == board[0][2] && board[0][0] != " "){
            return true;
        } else if (board[1][0] == board[1][1] && board[1][1] == board[1][2] && board[1][0] != " ") {
            return true;
        } else if (board[2][0] == board[2][1] && board[1][1] == board[2][2] && board[2][0] != " ") {
            return true;
        } else if (board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[0][0] != " ") {
            return true;
        } else if (board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[0][1] != " ") {
            return true;
        } else if (board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[0][2] != " ") {
            return true;
        } else if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != " ") {
            return true;
        } else if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != " ") {
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean validPosition(Board board, int linha, int coluna){
        if(linha == -1 || coluna == -1 || board.board[linha][coluna] != " "){
            return false;
        }
        else{
            return true;
        }
    }
}

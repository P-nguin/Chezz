package com.zhao.daniel.chezz;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import chariot.Client;
import chariot.util.Board;
import chariot.util.Board.Move;

@Service
public class LichessService {

    private Client lichessClient;
    private Board board;

    public LichessService(@Value("${lichess.token}") String token) {
        this.lichessClient = Client.auth(token);
        this.board = Board.fromStandardPosition();
    }

    public Set<String> allValidMoves() {
        Set<String> movesList = new HashSet<>();
        
        for(Move move : board.validMoves()) {
            movesList.add(move.toString());
        }
        
        return movesList;
    }

    public List<String> drawBoard() {
        List<String> b = board.toString(c -> c.letter())
            .chars()
            .mapToObj(c -> String.valueOf((char) c))
            .collect(Collectors.toList());
        
        int cnt = 1;
        for(int i = 0; i < b.size(); i++) {
            if(cnt % 2 == 0) {
                b.remove(i);
                i--;
            }
            cnt++;
        }
        
        return b;
    }

    public boolean movePiece(int fromIndex, int toIndex) {
        String sanMove = convertToSan(fromIndex, toIndex);

        if (allValidMoves().contains(sanMove)) {
            board = board.play(sanMove);
            return true;
        }
        return false;
    }
    
    private String convertToSan(int fromIndex, int toIndex) {
        char fromFile = (char) ('a' + (fromIndex % 8));
        char fromRank = (char) ('1' + (7 - (fromIndex / 8)));
        
        char toFile = (char) ('a' + (toIndex % 8));
        char toRank = (char) ('1' + (7 - (toIndex / 8)));
    
        String fromSquare = "" + fromFile + fromRank;
        String toSquare = "" + toFile + toRank;
        
        return fromSquare + toSquare;
    }

    public String currentTurn() {
        return board.whiteToMove()? "White" : "Black";
    }
    

}

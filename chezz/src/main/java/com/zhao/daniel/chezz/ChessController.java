package com.zhao.daniel.chezz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessController {

    private final LichessService lichessService;

    @Autowired
    public ChessController(LichessService lichessService) {
        this.lichessService = lichessService;
    }

    @GetMapping("/board")
    public String getBoard(Model model) {
        List<String> board = lichessService.drawBoard();
        model.addAttribute("board", board);
        return "board";
    }
    

    @GetMapping("/allMoves")
    @ResponseBody
    public String allValidMoves() {
        return lichessService.allValidMoves().toString();
    }

    @GetMapping("/move")
    @ResponseBody
    public ResponseEntity<?> movePiece(@RequestParam String from, @RequestParam String to) {
        int fromIndex = Integer.parseInt(from);
        int toIndex = Integer.parseInt(to);

        boolean success = lichessService.movePiece(fromIndex, toIndex);
        
        if(success) {
            List<String> updatedBoard = lichessService.drawBoard();
            return ResponseEntity.ok(Map.of("success", true, "board", updatedBoard));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid move"));
        }
    }

    @GetMapping("/currentTurn")
    @ResponseBody
    public String getCurrentTurn() {
        return lichessService.currentTurn();
    }
}
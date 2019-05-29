package ru.hsespb.ticktacktoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class Tester {
    @Test
    public void testWinRow() {
        var testBoard = new CheckeredButtonBoard(3, 3);
        testBoard.setCellType(0, 0, CellType.CROSS);
        testBoard.setCellType(0, 1, CellType.CROSS);
        testBoard.setCellType(0, 2, CellType.CROSS);
        assertTrue(UserInputProcessor.checkIfWinner(testBoard, CellType.CROSS));
    }

    @Test
    public void testWinColumns() {
        var testBoard = new CheckeredButtonBoard(3, 3);
        testBoard.setCellType(0, 0, CellType.CROSS);
        testBoard.setCellType(1, 0, CellType.CROSS);
        testBoard.setCellType(2, 0, CellType.CROSS);
        assertTrue(UserInputProcessor.checkIfWinner(testBoard, CellType.CROSS));
    }

    @Test
    public void testWinDiagonals() {
        var testBoard = new CheckeredButtonBoard(3, 3);
        testBoard.setCellType(0, 0, CellType.CROSS);
        testBoard.setCellType(1, 1, CellType.CROSS);
        testBoard.setCellType(2, 2, CellType.CROSS);
        assertTrue(UserInputProcessor.checkIfWinner(testBoard, CellType.CROSS));
    }
}

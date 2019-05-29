package ru.hsespb.ticktacktoe;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Tester {
    @Test
    public void testBoardGeneration() {
        var board0 = new CheckeredButtonBoard(2, 2);
        var board1 = new CheckeredButtonBoard(4, 4);
        var board2 = new CheckeredButtonBoard(6, 6);
        var board3 = new CheckeredButtonBoard(8, 8);

        assertTrue(testGeneration(board0, 2));
        assertTrue(testGeneration(board1, 4));
        assertTrue(testGeneration(board2, 6));
        assertTrue(testGeneration(board3, 8));
    }

    private boolean testGeneration(@NotNull CheckeredButtonBoard board, int size) {
        var list = new ArrayList<Integer>(size * size / 2);

        for (int i = 0; i < size * size / 2; i++) {
            list.add(0);
        }

        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                var cell = board.getCell(i, j);
                list.set(cell.getNumber(), list.get(cell.getNumber()) + 1);
            }
        }

        for (int i = 0; i < size * size / 2; i++) {
            if (list.get(i) != 2) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testGetCellType() {
        var board = new CheckeredButtonBoard(4, 4);

        assertEquals(CellType.CLOSED, board.getCellType(0, 0));
        assertEquals(CellType.CLOSED, board.getCellType(3, 1));

        board.openCell(2, 2);
        board.openCell(0, 3);

        assertEquals(CellType.OPENED, board.getCellType(2, 2));
        assertEquals(CellType.OPENED, board.getCellType(0, 3));

        board.closeCell(2, 2);
        board.openCell(0, 3);

        assertEquals(CellType.CLOSED, board.getCellType(2, 2));
        assertEquals(CellType.OPENED, board.getCellType(0, 3));
    }

    @Test
    public void testAllOpened() {
        var board = new CheckeredButtonBoard(2, 2);

        assertFalse(board.isAllOpened());

        board.openCell(0, 0);
        board.openCell(1, 0);
        board.openCell(1, 1);
        board.openCell(0, 0);

        assertFalse(board.isAllOpened());

        board.closeCell(0, 0);
        board.openCell(0, 1);

        assertFalse(board.isAllOpened());

        board.openCell(0, 0);

        assertTrue(board.isAllOpened());
    }

    @Test
    public void testUserInputProcessor() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            var gameState = new PlayGameState();
            var board = new CheckeredButtonBoard(2, 2);
            var userProcessor = new UserInputProcessor(gameState, board);

            userProcessor.processClick(0, 0);
            userProcessor.processClick(1, 0);
            userProcessor.processClick(1,1);

            if (board.getCell(0, 0).hasEqualNumber(board.getCell(1, 0))) {
                assertEquals(CellType.OPENED, board.getCellType(0, 0));
                assertEquals(CellType.OPENED, board.getCellType(1, 0));
            } else {
                assertEquals(CellType.CLOSED, board.getCellType(0, 0));
                assertEquals(CellType.CLOSED, board.getCellType(1, 0));
            }
        }
    }
}

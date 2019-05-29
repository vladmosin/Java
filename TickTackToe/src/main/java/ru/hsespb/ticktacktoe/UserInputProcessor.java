package ru.hsespb.ticktacktoe;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/** Processes all user movements */
public class UserInputProcessor {
    private CheckeredButtonBoard board;
    private PlayGameState playGameState;
    private Point lastOpenedCellPosition;

    /** After user does not guess pair two buttons marks as should be closed and closes in next click */
    @NotNull private ArrayList<Point> shouldBeClosed = new ArrayList<>();

    public UserInputProcessor(@NotNull PlayGameState playGameState, @NotNull CheckeredButtonBoard board) {
        this.board = board;
        this.playGameState = playGameState;
    }

    /** Closes al cells? which are stores in shouldBeClosed*/
    private void closeCells() {
        if (shouldBeClosed.size() > 0) {
            for (var point : shouldBeClosed) {
                board.closeCell(point.getX(), point.getY());
            }
        }
        shouldBeClosed.clear();
    }

    /** Processes click on board */
    public void processClick(int x, int y) throws InterruptedException {
        closeCells();
        if (board.getCellType(x, y) != CellType.CLOSED) {
            return;
        }

        var cell = board.getCell(x, y);

        board.openCell(x, y);
        if (lastOpenedCellPosition == null) {
            lastOpenedCellPosition = new Point(x, y);
        } else if (board.getCell(lastOpenedCellPosition.getX(), lastOpenedCellPosition.getY()).hasEqualNumber(cell)) {
            lastOpenedCellPosition = null;
            if (checkIfWinner(board)) {
                playGameState.finishGame();
            }
        } else {
            shouldBeClosed.add(lastOpenedCellPosition);
            shouldBeClosed.add(new Point(x,y));
            lastOpenedCellPosition = null;
        }
    }

    /** Checks that game ands */
    public static boolean checkIfWinner(CheckeredButtonBoard board) {
        return board.isAllOpened();
    }
}

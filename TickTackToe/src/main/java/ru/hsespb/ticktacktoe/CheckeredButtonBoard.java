package ru.hsespb.ticktacktoe;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;


/** Stores board for game */
@SuppressWarnings("WeakerAccess")
public class CheckeredButtonBoard {
    /** Viewer */
    private PlayView playView;

    private int height;
    private int width;

    /** Stores number of opened cells */
    private int openedCells;

    @NotNull private Cell[][] cells;

    public CheckeredButtonBoard(int height, int width) {
        this.height = height;
        this.width = width;

        var permutation = generatePermutation();
        cells = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                cells[row][column] = new Cell(CellType.CLOSED, permutation.get(row * height + column));
            }
        }
    }

    /** Generates game (numbers in cells)*/
    @NotNull private ArrayList<Integer> generatePermutation() {
        var permutation = new ArrayList<Integer>();

        for (int i = 0; i < width * height / 2; i++) {
            permutation.add(i);
            permutation.add(i);
        }

        Collections.shuffle(permutation);
        return permutation;
    }

    @NotNull public CellType getCellType(int x, int y) {
        return cells[x][y].getCellType();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    /** Opens cell */
    public void openCell(int x, int y) {
        Cell cell = cells[x][y];
        if (cell.isOpen()) {
            return;
        }

        cell.open();
        openedCells++;
        if (playView != null) {
            playView.setButtonText(x, y, Integer.toString(cell.getNumber()));
        }
    }

    /** Closes cell */
    public void closeCell(int x, int y) {
        Cell cell = cells[x][y];
        if (!cell.isOpen()) {
            return;
        }

        cell.close();
        openedCells--;
        if (playView != null) {
            playView.setButtonText(x, y, "");
        }
    }

    /** Checks that all cells are opened */
    public boolean isAllOpened() {
        return openedCells == height * width;
    }

    public void setPlayView(@NotNull PlayView playView) {
        this.playView = playView;
    }
}

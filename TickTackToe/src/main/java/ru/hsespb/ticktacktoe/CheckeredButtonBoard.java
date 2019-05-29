package ru.hsespb.ticktacktoe;

import java.util.ArrayList;
import java.util.Collections;

public class CheckeredButtonBoard {
    private PlayView playView;

    private int height;
    private int width;
    private int openedCells;

    private Cell[][] cells;

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

    private ArrayList<Integer> generatePermutation() {
        var permutation = new ArrayList<Integer>();

        for (int i = 0; i < width * height / 2; i++) {
            permutation.add(i);
            permutation.add(i);
        }

        Collections.shuffle(permutation);
        return permutation;
    }

    public CellType getCellType(int x, int y) {
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

    public boolean isAllOpened() {
        return openedCells == height * width;
    }

    public void setPlayView(PlayView playView) {
        this.playView = playView;
    }
}

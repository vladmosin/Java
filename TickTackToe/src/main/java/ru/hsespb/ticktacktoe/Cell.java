package ru.hsespb.ticktacktoe;

import org.jetbrains.annotations.NotNull;

public class Cell {
    @NotNull private CellType cellType;
    private int number;

    public Cell(@NotNull CellType cellType, int number) {
        this.cellType = cellType;
        this.number = number;
    }

    @NotNull public CellType getCellType() {
        return cellType;
    }

    public int getNumber() {
        return number;
    }

    public void open() {
        cellType = CellType.OPENED;
    }

    public boolean isOpen() {
        return cellType == CellType.OPENED;
    }

    public void close() {
        cellType = CellType.CLOSED;
    }

    public boolean hasEqualNumber(@NotNull Cell cell) {
        return number == cell.number;
    }
}

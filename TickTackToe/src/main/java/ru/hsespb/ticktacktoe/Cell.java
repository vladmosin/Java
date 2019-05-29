package ru.hsespb.ticktacktoe;

import org.jetbrains.annotations.NotNull;

/** Stores information about one board cell */
@SuppressWarnings("WeakerAccess")
public class Cell {
    /** Closed or open */
    @NotNull private CellType cellType;

    /** Value in cell */
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

    /** Opens cell */
    public void open() {
        cellType = CellType.OPENED;
    }

    public boolean isOpen() {
        return cellType == CellType.OPENED;
    }

    /** Closes cell */
    public void close() {
        cellType = CellType.CLOSED;
    }

    /** Check that cells stores equal numbers */
    public boolean hasEqualNumber(@NotNull Cell cell) {
        return number == cell.number;
    }
}

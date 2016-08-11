/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.util.stream.Stream;
import utils.Tuple;

/**
 *
 * @author zugbug
 */
final class Grid {

    @Override
    public String toString() {
        return Stream.of(cells).map(s -> Stream.of(s))
                .map(s -> s.map(Cell::isAlive).map(w -> w ? "x" : " "))
                .map(s -> s.reduce((a, b) -> a + b).get())
                .reduce((a, b) -> a + "\n" + b).get();
    }

    private void reset() {
        for (Cell[] cell : cells) {
            for (int j = 0; j < (cell).length; j++) {
                cell[j] = new Cell();
            }
        }
    }

    class Cell {

        @Override
        public String toString() {
            return alive ? "x" : " ";
        }

        boolean alive;

        public Cell(boolean alive) {
            this.alive = alive;
        }

        public Cell() {
            this(false);
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }

        public boolean isAlive() {
            return alive;
        }

        private void process(int alives) {
            if (this.alive) {
                if (alives == 2 || alives == 3) {
                    setAlive(true);
                } else {
                    setAlive(false);
                }
            } else if (alives == 3) {
                setAlive(true);
            }
        }

    }

    Cell[][] cells;
    final int maxX, maxY;

    public Grid(int x, int y) {
        maxX = x;
        maxY = y;
        cells = new Cell[y][x];
        reset();
    }

    enum Offset {
        l(-1), e(0), m(1);
        private final int off;

        private Offset(int off) {
            this.off = off;
        }

    }

    Grid advance() {
        Grid ret = new Grid(this);
        for (int i = 0; i < ret.cells.length; i++) {
            for (int j = 0; j < ret.cells[i].length; j++) {
                Cell origin = ret.cells[i][j];
                Tuple<Integer, Integer> cellPos = Tuple.of(i, j);
                Stream<Tuple<Integer, Integer>> sot = Stream.of(Offset.values()).map(s -> s.off).flatMap(s -> Stream.of(Offset.values()).map(v -> v.off).map(v -> Tuple.of(v, s))).filter(s -> !(s.left() == 0 && s.right() == 0));
                int alives = (int) sot.map(s -> s.mapLeft((Integer l) -> l + cellPos.left()).mapRight((Integer r) -> r + cellPos.right()))
                        .map(s -> this.at(s.right(), s.left())).filter(s -> s.alive).count();
                origin.process(alives);
            }
        }
        return ret;
    }

    public Grid(Grid old) {
        this(old.toString(), old.maxX, old.maxY);

    }

    public Grid(String state, int x, int y) {
        this(x, y);
        setState(state);
    }

    Cell at(int right, int down) {
        if (right >= maxX) {
            right %= maxX;
        } else if (right < 0) {
            right = maxX + (right % maxX);
        }
        if (down >= maxY) {
            down %= maxY;
        } else if (down < 0) {
            down = maxY + (down % maxY);
        }
        return cells[down][right];
    }

    void setState(String state) {
        int x, y = 0;
        reset();
        for (String line : state.split("\n|;")) {
            for (x = 0; x < line.length(); x++) {
                char cell = line.charAt(x);
                at(x, y).setAlive(cell == 'x');
            }
            y++;
        }

    }

}

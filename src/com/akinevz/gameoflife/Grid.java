/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akinevz.gameoflife;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author zugbug
 */
final class Grid {

    final int maxX, maxY;
    Cell[][] cells;

    public Grid(int x, int y) {
        maxX = x;
        maxY = y;
        cells = new Cell[y][x];
        reset();
    }

    public Grid(Grid old) {
        this(old.toString(), old.maxX, old.maxY);

    }
    public Grid(String state, int x, int y) {
        this(x, y);
        setState(state);
    }

    @Override
    public String toString() {
        return Stream.of(cells).map(Stream::of)
                .map(s -> s.map(Cell::isAlive).map(w -> w ? "x" : " "))
                .flatMap(s -> s.reduce((a, b) -> a + b).stream())
                .reduce((a, b) -> a + "\n" + b).orElse("");
    }

    private void reset() {
        for (Cell[] cell : cells) {
            for (int j = 0; j < (cell).length; j++) {
                cell[j] = new Cell();
            }
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
        for (String line : state.split("[\n;]")) {
            for (x = 0; x < line.length(); x++) {
                char cell = line.charAt(x);
                at(x, y).setAlive(cell == 'x');
            }
            y++;
        }

    }

    enum Offset {
        l(-1), e(0), m(1);
        private final int off;

        private Offset(int off) {
            this.off = off;
        }

    }

    static class Tuple<L, R> {
        private L ldata;
        private R rdata;

        public Tuple(L ldata, R rdata) {
            this.ldata = ldata;
            this.rdata = rdata;
        }

        static <L, R> Tuple<L, R> of(L left, R right) {
            return new Tuple<>(left, right);
        }

        public L left() {
            return ldata;
        }

        public R right() {
            return rdata;
        }

        public <O> Tuple<O, R> mapLeft(Function<? super L, O> mapper) {
            return new Tuple<>(mapper.apply(ldata), rdata);
        }

        public <O> Tuple<L, O> mapRight(Function<? super R, O> mapper) {
            return new Tuple<>(ldata, mapper.apply(rdata));
        }
    }

    class Cell {

        boolean alive;

        public Cell(boolean alive) {
            this.alive = alive;
        }

        public Cell() {
            this(false);
        }

        @Override
        public String toString() {
            return alive ? "x" : " ";
        }

        public boolean isAlive() {
            return alive;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
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

}

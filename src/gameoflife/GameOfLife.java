/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.util.stream.Stream;

/**
 *
 * @author zugbug
 */
public final class GameOfLife {

    static GameOfLife instance;

    public static String[] getSeedNames() {
        return Stream.of(new String[]{"Reset"}, Seed.values()).flatMap(Stream::of)
                .map((Object s) -> s.toString())
                .toArray(String[]::new);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        instance = new GameOfLife(101, 101, "xxx x;x;   xx; xx x;x x x");
        instance.run();
    }
    private final String seed;

    
    Grid grid;

    GUI ui;
    public GameOfLife(int x, int y, String seed) {
        ui = new GUI(this);
        grid = new Grid(x, y);
        this.seed = seed;
        reset();
    }
    void run() {
        ui.setVisible(true);
    }

    void reset() {
        grid.setState(seed);
    }

    void advance() {
        grid = grid.advance();
    }

    void setSeed(String string) {
        String s;
        try {
            s = Seed.valueOf(string).seed;

        } catch (Exception e) {
            s = this.seed;
        }
        grid.setState(s);
    }
    enum Seed {
        Exploader("x;xx;x x"),
        New("xxxxxxxxxxxxxxx;x;x;x;x;x;x;x;x;;x;xx;x x"),
        Glider("  x;x x; xx");
        private final String seed;
        
        private Seed(String seed) {
            this.seed = seed;
        }
        
    }
}

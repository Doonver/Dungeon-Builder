package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    public static final int SIZE = 6;

    public static final int WIDTH = 3 * (SIZE + 2 * (SIZE - 1)) + 2 * SIZE + 2;
    public static final int HEIGHT = SIZE * 10;



    private static final long SEED = 0;
    private static final Random RANDOM = new Random(SEED);

    private static void addHexagon(int x, int y, TETile[][] world, TETile tile) {
        //deals with rows of the hexagon
        int currLengthOfRow = SIZE;
        int step = 0;
        //bottom half of hexagon
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < currLengthOfRow; j++) {
                world[x - step + j][y + i] = tile;
            }
            if (currLengthOfRow < SIZE + (SIZE - 1) * 2) {
                currLengthOfRow += 2;
                step++;
            }
        }
        //top half of hexagon
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < currLengthOfRow; j++) {
                world[x - step + j][y + i + SIZE] = tile;
            }
            currLengthOfRow -= 2;
            step--;
        }
    }

    public static void initializeWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.WATER;
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.TREE;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.SAND;
            case 4: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }

    public static void tessellate(TETile[][] world) {
        int backStep = 2 * SIZE - 1;
        int startY = 0;
        int keep = 0;
        for (int startX = WIDTH / 2 - SIZE / 2; startX < WIDTH - SIZE; startX += backStep) {
            int v = startY;
            for (int b = startX; b > 0; b -= backStep) {
                addHexagon(b, v, world, randomTile());
                v += SIZE;
            }
            startY += SIZE;
            keep = startX;
        }
        startY += SIZE;
        int counter = 0;
        while (counter < 2) {
            int v = startY;
            for (int b = keep; b > 0; b -= backStep) {
                if (v + 2 * SIZE < HEIGHT) {
                    addHexagon(b, v, world, randomTile());
                }
                v += SIZE;
            }
            startY += 2 * SIZE;
            counter++;
        }
        addHexagon(WIDTH / 2 - SIZE / 2, startY, world, randomTile());
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        initializeWorld(world);
        tessellate(world);
        ter.renderFrame(world);
    }
}

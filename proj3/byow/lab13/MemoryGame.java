package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.drawFrame("hi");
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        String randStr = "";
        for (int i = 0; i < n; i++) {
            int x = rand.nextInt(26);
            randStr += Character.toString(CHARACTERS[x]);
        }

        return randStr;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        //middle string
        StdDraw.text((float) width / 2, (float) height / 2, s);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.textLeft(width * 0.02, height * 0.95, "Round: " + round);
        int x = rand.nextInt(7);
        StdDraw.textRight(width * 0.98, height * 0.95, ENCOURAGEMENT[x]);
        if (playerTurn) {
            StdDraw.text((float) width / 2, height * 0.95, "Type!");
        } else {
            StdDraw.text((float) width / 2, height * 0.95, "Watch!");
        }
        StdDraw.line(0, 0.9 * height, width, 0.9 * height);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            Character c = letters.charAt(i);
            drawFrame(c.toString());
            StdDraw.pause(1000);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String s = "";
        int numCharsInputted = 0;
        while (numCharsInputted < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                s += c;
                drawFrame(s);
                numCharsInputted++;
            }
        }
        return s;
    }

    public void startGame() {
        gameOver = false;
        round = 1;
        playerTurn = false;
        while (!gameOver) {
            String s = generateRandomString(round);
            flashSequence(s);
            playerTurn = true;
            drawFrame("");
            String input = solicitNCharsInput(round);
            if (input.equals(s)) {
                round++;
            } else {
                gameOver = true;
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.text(width / 2, height / 2, "Game over! You made it to round: " + round);
                StdDraw.show();
            }
            playerTurn = false;
        }
    }

}

package game;

import java.util.LinkedList;
import java.util.Random;

public class Game {

    int size;
    int bombCount;
    int flagCount;
    int flaggedBombCount;

    int[][] checked;
    int[][] bombs;
    int[][] adjacentBombs;
    int[][] flags;

    boolean gameOver = false;
    boolean gameWon = false;

    public Game(Difficulty difficulty){
        switch (difficulty){
            case EASY:
                size = 8;
                bombCount = 10;
                break;
            case MEDIUM:
                size = 16;
                bombCount = 40;
                break;
            case HARD:
                size = 32;
                bombCount = 150;
                break;
        }

        flagCount = bombCount;
        flaggedBombCount = 0;
        checked = new int[size][size];
        bombs = new int[size][size];
        adjacentBombs = new int[size][size];
        flags = new int[size][size];
        initBoard();
    }

    void initBoard(){

        //place bombs
        for (int i = 0; i < bombCount; i++) {
            int x;
            int y;
            do{
                Random rnd = new Random();
                x = rnd.nextInt(size);
                y = rnd.nextInt(size);
            }
            while(bombs[x][y] == 1);

            bombs[x][y] = 1;
        }

        //set adjenctBombs table
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        if(i + k < size && i + k >=0 && j + l < size && j + l >=0){
                            adjacentBombs[i][j] += bombs[i+k][j+l];
                        }
                    }
                }
                adjacentBombs[i][j] -= bombs[i][j];
            }
        }
    }

    void check(int i, int j){
        if(!gameOver && checked[i][j] == 0) {
            checked[i][j] = 1;

            if (bombs[i][j] == 1) {
                onGameOver();
            } else if (adjacentBombs[i][j] == 0) {
                BFS(i, j);
            }
        }
    }

    void flag(int i, int j){
        if(flags[i][j] == 0) { //add flag
            if (checked[i][j] == 0 && flagCount > 0) {
                if (bombs[i][j] == 1) {
                    flaggedBombCount++;
                }
                flags[i][j] = 1;
                flagCount--;
            }
        }else { //remove flag
            if (bombs[i][j] == 1) {
                flaggedBombCount--;
            }
            flags[i][j] = 0;
            flagCount++;
            if (flaggedBombCount == bombCount) {
                onGameWin();
            }
        }
    }

    void BFS(int i, int j){
        LinkedList<Integer> xHeap = new LinkedList<>();
        LinkedList<Integer> yHeap = new LinkedList<>();
        xHeap.add(i);
        yHeap.add(j);
        while (!xHeap.isEmpty()) {
            int x = xHeap.pollFirst();
            int y = yHeap.pollFirst();
            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if (x + k < size && x + k >= 0 && y + l < size && y + l >= 0) {
                        if (!(k == 0 && l == 0)) {
                            if (adjacentBombs[x + k][y + l] == 0 && checked[x + k][y + l] == 0) {
                                xHeap.addFirst(x + k);
                                yHeap.addFirst(y + l);
                                checked[x + k][y + l] = 1;
                            } else {
                                checked[x + k][y + l] = 1;
                            }
                        }
                    }
                }
            }
        }
    }

    private void onGameOver(){
        gameOver = true;

        //show all bombs
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(bombs[i][j] == 1){
                    checked[i][j] = 1;
                }
            }
        }
    }

    void onGameWin(){
        gameWon = true;
    }
}

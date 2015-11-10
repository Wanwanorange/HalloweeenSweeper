package hu.ait.android.minesweeper.Model;

import java.util.Random;

/**
 * Created by Wanchen on 10/1/2015.
 */
public class MinesweeperModel {

    private static MinesweeperModel instance = null;
    public static final short EMPTY = 0;
    public static final short BOMB = 10;

    public MinesweeperModel() {
    }

    public static MinesweeperModel getInstance() {

        if (instance == null) {
            instance = new MinesweeperModel();
        }
        return instance;
    }

    private MineField[][] model = {
            {new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY)},
            {new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY)},
            {new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY)},
            {new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY)},
            {new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY), new MineField(EMPTY)}
    };

    private boolean[][] flagField = {
            {false, false,false,false,false,},
            {false, false,false,false,false,},
            {false, false,false,false,false,},
            {false, false,false,false,false,},
            {false, false,false,false,false,}
    };

    public void reset(){
        for (int i=0; i<5; i++){
            for (int j=0; j<5;j++){
                model[i][j].setVal(EMPTY);
                model[i][j].setReveal(false);
                flagField[i][j] = false;
            }
        }
        placeBombs();
    }

    public int getVal(int x, int y){
        return model[x][y].getVal();
    }

    public boolean isTouched(int x, int y){
        return model[x][y].getReveal();
    }

    public void showField(int x, int y){
        model[x][y].setReveal(true);
    }

    public boolean hasFlag(int x, int y){
        return flagField[x][y];
    }

    public void placeFlag(int x, int y){
        flagField[x][y] = true;
    }

    public void removeFlag(int x, int y){
        flagField[x][y] = false;
    }

    public void placeBombs() {
        int bombs = 0;
        while (bombs < 3) {
            Random rand = new Random();
            int numX = rand.nextInt(5);
            int numY = rand.nextInt(5);
            if (model[numX][numY].getVal() != BOMB) {

                model[numX][numY].setVal(BOMB);
                bombs++;

                if (numX > 0) {
                    int leftN = model[numX - 1][numY].getVal();
                    model[numX - 1][numY].setVal(leftN + 1);  //ADD 1 TO LEFT NEIGHBOUR
                    if (numY > 0) {
                        int leftUD = model[numX - 1][numY - 1].getVal();
                        model[numX - 1][numY - 1].setVal(leftUD + 1); //ADD 1 TO UP LEFT
                    }
                }

                if (numY > 0){
                    int upN = model[numX][numY - 1].getVal();
                    model[numX][numY - 1].setVal(upN + 1);     //ADD 1 TO UP NEIGHBOUR

                    if (numX < 4) {
                        int rightUD = model[numX + 1][numY - 1].getVal();
                        model[numX + 1][numY - 1].setVal(rightUD + 1);     //ADD 1 TO UP RIGHT

                    }
                }

                if (numX < 4){
                    int rightN = model[numX + 1][numY].getVal();
                    model[numX + 1][numY].setVal(rightN + 1);     //ADD 1 TO RIGHT

                    if (numY < 4) {
                        int rightDD = model[numX + 1][numY + 1].getVal();
                        model[numX + 1][numY + 1].setVal(rightDD + 1);     //ADD 1 TO DOWN RIGHT
                    }
                }

                if (numY <4){
                    int down = model[numX][numY + 1].getVal();
                    model[numX][numY + 1].setVal(down + 1);     //ADD 1 TO DOWN

                    if (numX > 0) {
                        int leftDD = model[numX - 1][numY + 1].getVal();
                        model[numX - 1][numY + 1].setVal(leftDD + 1);     //ADD 1 TO DOWN
                    }
                }
            }
        }
    }

    public boolean checkWin(){
        int correctFlags = 0;

        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                if (model[i][j].getVal()>= BOMB && hasFlag(i,j)){
                    correctFlags+=1;
                }
            }
        }
        if (correctFlags == 3){
            return true;
        }
        return false;
    }
}

class MineField {

    int value;
    boolean reveal = false;


    public MineField(int val) {
        value = val;
    }

    int getVal() {
        return value;
    }

    void setVal(int val) {
        value = val;
    }

    boolean getReveal(){
        return reveal;
    }

    void setReveal(boolean touched){
        reveal = touched;
    }

}

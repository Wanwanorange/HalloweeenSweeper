package hu.ait.android.minesweeper.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import hu.ait.android.minesweeper.R;
import hu.ait.android.minesweeper.MainActivity;
import hu.ait.android.minesweeper.Model.MinesweeperModel;


/**
 * Created by Wanchen on 9/30/2015.
 */
public class MinesweeperView extends View{

    private Paint paintBg;
    private Paint paintLine;
    private Bitmap background;
    private Bitmap flagBitmap;
    private Bitmap bombBitmap;
    public boolean flagMode = false;

    public MinesweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setColor(Color.BLACK);
        paintBg.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        flagBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
        bombBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);

        MinesweeperModel.getInstance().placeBombs();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        flagBitmap = Bitmap.createScaledBitmap(flagBitmap, getWidth() / 5, getHeight() / 5, true);
        bombBitmap = Bitmap.createScaledBitmap(bombBitmap, getWidth()/5, getHeight()/5, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);


        drawGameArea(canvas);

        drawSquares(canvas);

        invalidate();

    }

    private void checkFloodFill(){
        for (int x=0; x<5;x++){
            for (int y=0;y<5;y++){
                if (MinesweeperModel.getInstance().isTouched(x,y)){
                    if (!MinesweeperModel.getInstance().hasFlag(x,y)) {
                        int fieldVal = MinesweeperModel.getInstance().getVal(x,y);
                        if (fieldVal == 0){
                            floodFill(x,y);
                        }
                    }
                }
            }
        }
    }

    private void floodFill(int x, int y){
        int fieldVal = MinesweeperModel.getInstance().getVal(x,y);

        if (x < 0 || y < 0 || x > 4 || y > 4){return;}

        if (fieldVal == 0 && !MinesweeperModel.getInstance().isTouched(x,y)){
            MinesweeperModel.getInstance().showField(x, y);
            floodFill(x - 1, y);
            floodFill(x-1, y-1);
            floodFill(x, y-1);
            floodFill(x+1, y-1);
            floodFill(x+1, y);
            floodFill(x+1, y+1);
            floodFill(x, y + 1);
            floodFill(x - 1, y + 1);
        }else {
            return;}
    }

    private void drawFlag(int x, int y, Canvas canvas){
        if (MinesweeperModel.getInstance().hasFlag(x,y)){
            canvas.drawBitmap(flagBitmap,x*getWidth()/5,y*getHeight()/5,null);
            if (MinesweeperModel.getInstance().checkWin()){
                ((MainActivity) getContext()).showSnackBarMessageWithRestart(getContext().getString(R.string.txt_win));
            }
        }
        else{
            Paint cover = new Paint();
            cover.setColor(Color.BLACK);
            canvas.drawRect(x * getWidth() / 5, y * getHeight() / 5, getWidth() / 5, getHeight() / 5, cover);
        }
    }

    private void drawSquares(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);

        checkFloodFill();

        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){

                if (MinesweeperModel.getInstance().isTouched(i,j)){

                    drawFlag(i, j, canvas);

                    if (!MinesweeperModel.getInstance().hasFlag(i, j)) {
                        float centerX = i * getWidth() / 5 + getWidth() / 6;
                        float centerY = j * getHeight() / 5 + getHeight() / 6;
                        int fieldVal = MinesweeperModel.getInstance().getVal(i, j);

                        if (fieldVal >= 4) {
                            canvas.drawBitmap(bombBitmap, i * getWidth() / 5, j * getHeight() / 5, null);
                            ((MainActivity) getContext()).showSnackBarMessageWithRestart(getContext().getString(R.string.txt_lose));
                        }
                        else{
                            canvas.drawText(Integer.toString(fieldVal), centerX, centerY, paint);
                        }
                    }

                }

            }
        }
    }

    private void drawGameArea(Canvas canvas) {

        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // four horizontal lines
        canvas.drawLine(0, getHeight() / 5, getWidth(), getHeight() / 5,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 5, getWidth(),
                2 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 3 * getHeight() / 5, getWidth(),
                3 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 4 * getHeight() / 5, getWidth(),
                4 * getHeight() / 5, paintLine);
        // four vertical lines
        canvas.drawLine(getWidth() / 5, 0, getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 5, 0, 2 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(3 * getWidth() / 5, 0, 3 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(4 * getWidth() / 5, 0, 4 * getWidth() / 5, getHeight(),
                paintLine);

    }

    public void setFlagMode(boolean val){
        flagMode = val;
    }

    public boolean getFlagMode(){
        return flagMode;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int tX = ((int) event.getX()) / (getWidth() / 5);
            int tY = ((int) event.getY()) / (getHeight() / 5);

            ((MainActivity) getContext()).showSnackBarMessage(
                    getResources().getString(R.string.txt_touched, ((MinesweeperModel.getInstance().getVal(tX, tY)))));


            MinesweeperModel.getInstance().showField(tX,tY);

            if (flagMode){
                if (!MinesweeperModel.getInstance().hasFlag(tX,tY)){
                    MinesweeperModel.getInstance().placeFlag(tX,tY);}
                else {
                    MinesweeperModel.getInstance().removeFlag(tX, tY);
                }
            }

        }

        return super.onTouchEvent(event);
    }

    public void clearGameArea() {
        MinesweeperModel.getInstance().reset();
        setFlagMode(false);
        invalidate();
    }
}



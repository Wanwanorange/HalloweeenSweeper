package hu.ait.android.minesweeper;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import hu.ait.android.minesweeper.View.MinesweeperView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutContent;
    private MinesweeperView minesweeperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent = (LinearLayout) findViewById(R.id.layoutContent);

        minesweeperView =
                (MinesweeperView) findViewById(R.id.gameView);


        Button btnRestart =
                (Button) findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minesweeperView.clearGameArea();
                showSnackBarMessageWithRestart(getString(R.string.txt_game_restart_message));
                showSnackBarMessage(
                        getString(R.string.txt_game_restart_message));
            }
        });

        Button btnFlag =
                (Button) findViewById(R.id.btnFlag);
        btnFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!minesweeperView.getFlagMode()) {
                    showSnackBarMessage(getString(R.string.txt_flag, "ON"));
                    minesweeperView.setFlagMode(true);
                    Log.d("MY_TAG", "onClick ");}
                else {
                    showSnackBarMessage(getString(R.string.txt_flag, "OFF"));
                    minesweeperView.setFlagMode(false);
                    Log.d("THAT_TAG", "onClick ");}


            }
        });
    }

    public void showSnackBarMessage(String msg) {
        Snackbar.make(layoutContent,
                msg,
                Snackbar.LENGTH_LONG).show();
    }
    public void showSnackBarMessageWithRestart(String msg) {
        Snackbar.make(layoutContent,
                msg,
                Snackbar.LENGTH_LONG).setAction(R.string.txt_restart, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                minesweeperView.clearGameArea();

            }}).show();
    }
}

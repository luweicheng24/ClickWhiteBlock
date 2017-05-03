package com.gsww.www.clickwhiteblock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements StiackBlockView.OnBalckCheckListener {

    private TextView scoreText;
    private StiackBlockView stiackBlockView;
    private int rank;//等级

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        rank = b.getInt("rank");
        stiackBlockView = (StiackBlockView) findViewById(R.id.write_pieces);
        scoreText = (TextView) findViewById(R.id.score);
        switch (rank) {
            case 0:
                stiackBlockView.setSpeed(StiackBlockView.SPEED_PRIMARY);
                break;
            case 1:
                stiackBlockView.setSpeed(StiackBlockView.SPEED_MIDDLE);

                break;
            case 2:
                stiackBlockView.setSpeed(StiackBlockView.SPEED_HIGH);

                break;
            case 3:
                stiackBlockView.setSpeed(StiackBlockView.SPEED_MORE_HIGH);
                break;
        }
        stiackBlockView.setOnBalckCheckListener(this);
    }


    @Override
    public void score(int score) {
        scoreText.setText(score + "");
    }


    @Override
    public void gameOver() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("得分");

        dialog.setMessage("当前得分：" + scoreText.getText().toString());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                stiackBlockView.restart();
            }
        });
        dialog.show();
    }


}

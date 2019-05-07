package jkerver.example.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    int tries;
    TextView winText;
    int flipCount = 0;
    int[] selected = new int[2];
    ArrayList<ImageView> fronts;
    ImageView cardback;
    GridLayout gridLayout;
    Bitmap back;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        winText = findViewById(R.id.txtWin);
        gridLayout = findViewById(R.id.grdBoard);
        gridLayout.setUseDefaultMargins(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int width = extras.getInt("width");
        int height = extras.getInt("height");

        gridLayout.setRowCount(height);
        gridLayout.setColumnCount(width);

        back = BitmapFactory.decodeResource(getResources(), R.drawable.cardback);
        for (int i = 0; i < height * width; i++)
        {
            ImageView view = new ImageView(this);
            view.setImageBitmap(back);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = gridLayout.indexOfChild(v);
                    if (flipCount == 0)
                    {
                        gridLayout.addView(fronts.get(i), i);
                        selected[0] = i;
                        gridLayout.removeView(v);
                        flipCount = 1;
                    }
                    else if (flipCount == 1)
                    {
                        gridLayout.addView(fronts.get(i), i);
                        selected[1] = i;
                        gridLayout.removeView(v);
                        if (checkMatch()) {
                            //disable matched pair
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    gridLayout.getChildAt(selected[0]).setVisibility(View.INVISIBLE);
                                    gridLayout.getChildAt(selected[1]).setVisibility(View.INVISIBLE);
                                    gridLayout.getChildAt(selected[0]).setOnClickListener(null);
                                    gridLayout.getChildAt(selected[1]).setOnClickListener(null);
                                    checkComplete();
                                }
                            }, 1000);

                        } else {
                            //wait then flip
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    gridLayout.addView(getBack(), selected[0]);
                                    gridLayout.removeView(fronts.get(selected[0]));
                                    gridLayout.addView(getBack(), selected[1]);
                                    gridLayout.removeView(fronts.get(selected[1]));
                                }
                            }, 1000);

                        }

                        tries++;
                        flipCount = 0;
                    }
                }
            });

            gridLayout.addView(view);

        }

        fronts = prepareFronts(width, height);
    }

    private boolean checkMatch()
    {
        Bitmap bm1 = ((BitmapDrawable)fronts.get(selected[0]).getDrawable()).getBitmap();
        Bitmap bm2 = ((BitmapDrawable)fronts.get(selected[1]).getDrawable()).getBitmap();
        if (bm1.sameAs(bm2))
        {
            return true;
        }
        else
            return false;
    }

    private ArrayList<ImageView> prepareFronts(int w, int h)
    {
        ArrayList<Bitmap> bm = new ArrayList<>();
        bm.add(BitmapFactory.decodeResource(getResources(), R.drawable.blance));
        bm.add(BitmapFactory.decodeResource(getResources(), R.drawable.btome));
        bm.add(BitmapFactory.decodeResource(getResources(), R.drawable.gaxe));
        bm.add(BitmapFactory.decodeResource(getResources(), R.drawable.gtome));
        bm.add(BitmapFactory.decodeResource(getResources(), R.drawable.rsword));
        bm.add(BitmapFactory.decodeResource(getResources(), R.drawable.rtome));

        ArrayList<ImageView> iv = new ArrayList<>();

        int cards = w * h;
        if (cards == 12)
        {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < cards / 2; i++) {
                    ImageView view = new ImageView(this);
                    view.setImageBitmap(bm.get(i));
                    iv.add(view);
                }
            }
        }

        //shuffle
        Random rng = new Random();
        for(int i = 0; i < 10; i++)
        {
            int index1 = rng.nextInt(cards);
            int index2 = rng.nextInt(cards - 1);
            ImageView v = iv.get(index1);
            iv.remove(v);
            iv.add(index2, v);
        }
        return iv;
    }

    private void checkComplete()
    {
        boolean win = true;
        for (int i = 0; i < gridLayout.getChildCount(); i++)
        {
            if (gridLayout.getChildAt(i).getVisibility() == View.VISIBLE)
                win = false;
        }

        if (win)
        {
            winText.setText("Good job! \n Cleared in " + tries + " tries.");
        }
    }

    private ImageView getBack()
    {
        ImageView view = new ImageView(this);
        view.setImageBitmap(back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = gridLayout.indexOfChild(v);
                if (flipCount == 0)
                {
                    gridLayout.addView(fronts.get(i), i);
                    selected[0] = i;
                    gridLayout.removeView(v);
                    flipCount = 1;
                }
                else if (flipCount == 1)
                {
                    gridLayout.addView(fronts.get(i), i);
                    selected[1] = i;
                    gridLayout.removeView(v);
                    if (checkMatch()) {
                        //disable matched pair
                        gridLayout.getChildAt(selected[0]).setVisibility(View.INVISIBLE);
                        gridLayout.getChildAt(selected[1]).setVisibility(View.INVISIBLE);
                        gridLayout.getChildAt(selected[0]).setOnClickListener(null);
                        gridLayout.getChildAt(selected[1]).setOnClickListener(null);
                    } else {
                        //wait then flip
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                gridLayout.addView(getBack(), selected[0]);
                                gridLayout.removeView(fronts.get(selected[0]));
                                gridLayout.addView(getBack(), selected[1]);
                                gridLayout.removeView(fronts.get(selected[1]));
                                checkComplete();
                            }
                        }, 1000);

                    }


                    tries++;
                    flipCount = 0;
                }
            }
        });
        return view;
    }
}

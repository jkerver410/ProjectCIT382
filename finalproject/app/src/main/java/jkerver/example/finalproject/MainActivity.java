package jkerver.example.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    int width = 3;
    int height = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View v)
    {

        Intent intent = new Intent(getApplicationContext(), GameActivity.class);

        Bundle extras = new Bundle();
        extras.putInt("width", width);
        extras.putInt("height", height);
        intent.putExtras(extras);

        this.startActivity(intent);
    }

    public void setSize(View v)
    {

    }
}

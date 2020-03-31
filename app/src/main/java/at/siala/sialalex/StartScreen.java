package at.siala.sialalex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartScreen extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    public void startRefuelScreen(View view)
    {
        Intent refuelScreenIntent = new Intent(this, ExpenseScreen.class);
        this.startActivity(refuelScreenIntent);
    }
}

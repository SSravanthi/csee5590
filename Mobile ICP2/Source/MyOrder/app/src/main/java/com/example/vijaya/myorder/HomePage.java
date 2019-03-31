package com.example.vijaya.myorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    //Initiate the variables to the layouts
    private Button Continue;
    private TextView PizzaDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Assign Id's to the variables

        Continue = (Button)findViewById(R.id.btnContinue);
        PizzaDelivery = (TextView)findViewById(R.id.txtPizza);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent direct = new Intent(HomePage.this, MainActivity.class);
                startActivity(direct);

            }
        });
    }
}

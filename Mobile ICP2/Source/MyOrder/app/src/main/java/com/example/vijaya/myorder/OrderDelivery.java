package com.example.vijaya.myorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class OrderDelivery extends AppCompatActivity {

    TextView summaryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivery);

        summaryText = findViewById(R.id.summaryText);
        summaryText.setText(Html.fromHtml("<u>Your Order Summary</u><br/><br/>"));
        if(getIntent() != null){
            summaryText.append(getIntent().getStringExtra("orderSummary"));
        }else{
            summaryText.setText("You have no orders !!");
        }
        summaryText.append(Html.fromHtml("<br/>"));
        summaryText.setVisibility(View.VISIBLE);



    }
}

package com.example.billdivider2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class EqualBreakDown extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_break_down);

        //get amount input
        //TextView amountInputEqual=findViewById(R.id.amount_input);
        //String totalAmountEqual=amountInputEqual.getText().toString();
        //amountPaste.setText(totalAmountEqual);


        TextView totalPriceEqual=findViewById(R.id.tv_total_price_equal_num);
        TextView totalHeadCountEqual=findViewById(R.id.tv_head_count_equal_num);
        TextView totalAmountPerPeople=findViewById(R.id.tv_price_per_head_num);

        //retrieve intentExtra
        String inputDataPrice=getIntent().getStringExtra("inputTotalPrice");
        String inputDataHeadCount=getIntent().getStringExtra("inputHeadCount");

        //set total price and head count in this page
        totalPriceEqual.setText(inputDataPrice);
        totalHeadCountEqual.setText(inputDataHeadCount);

        //calculation per head price
        Float totalAmountCalc=Float.parseFloat(totalPriceEqual.getText().toString());
        Float totalHeadCountCalc=Float.parseFloat(totalHeadCountEqual.getText().toString());

        //set Head price
        Float averageAmount=totalAmountCalc/totalHeadCountCalc;
        totalAmountPerPeople.setText(String.format(Locale.getDefault(), "RM%.2f", averageAmount));







    }
}
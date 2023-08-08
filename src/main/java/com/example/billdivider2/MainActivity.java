package com.example.billdivider2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText amountInput = findViewById(R.id.amount_input);
        EditText amountPeopleInput = findViewById(R.id.people_amount_input);

        //Intent to navigate to the equalBreakDown
        Intent intentToEqual= new Intent(MainActivity.this,EqualBreakDown.class);
        Intent intentToCustom=new Intent(MainActivity.this,CustomBreakDown.class);
        //bridge between xml and program
        Button rbClear=findViewById(R.id.bt_clear);
        Button rbCalculate=findViewById(R.id.bt_calculate);

        //RadioGroup radioGroupBillMethod=findViewById(R.id.radioGroupMethod);
        RadioButton rbEqual=findViewById(R.id.rb_Equal);
        RadioButton rbCustom=findViewById(R.id.rb_Custom);


        //clear input
        rbClear.setOnClickListener(view->{
            amountInput.getText().clear();
            amountPeopleInput.getText().clear();
        });

        //to calculate
        rbCalculate.setOnClickListener(view->{
            String inputTotalPriceData = amountInput.getText().toString();
            String inputHeadCountData=amountPeopleInput.getText().toString();

            //decide intent to what page
            if(rbCustom.isChecked()){
                intentToCustom.putExtra("inputTotalPrice",inputTotalPriceData);
                intentToCustom.putExtra("inputHeadCount",inputHeadCountData);
                startActivity(intentToCustom);
            }
            else{
                intentToEqual.putExtra("inputTotalPrice",inputTotalPriceData);
                intentToEqual.putExtra("inputHeadCount",inputHeadCountData);
                startActivity(intentToEqual);
            }

        });

    }
}
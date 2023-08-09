package com.example.billdivider2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import java.util.Locale;

public class EqualBreakDown extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal_break_down);


        TextView totalPriceEqual=findViewById(R.id.tv_total_price_equal_num);
        TextView totalHeadCountEqual=findViewById(R.id.tv_head_count_equal_num);
        TextView totalAmountPerPeople=findViewById(R.id.tv_price_per_head_num);

        //retrieve intentExtra
        String inputDataPrice=getIntent().getStringExtra("inputTotalPrice");
        String inputDataHeadCount=getIntent().getStringExtra("inputHeadCount");
        int amount=Integer.parseInt(inputDataHeadCount);

        //set total price and head count in this page
        totalPriceEqual.setText(inputDataPrice);
        totalHeadCountEqual.setText(inputDataHeadCount);

        //calculation per head price
        Float totalAmountCalc=Float.parseFloat(totalPriceEqual.getText().toString());
        Float totalHeadCountCalc=Float.parseFloat(totalHeadCountEqual.getText().toString());

        //set Head price
        Float averageAmount=totalAmountCalc/totalHeadCountCalc;
        totalAmountPerPeople.setText(String.format(Locale.getDefault(), "RM%.2f", averageAmount));

        Button btShare=findViewById(R.id.bt_Share);

        //share
        btShare.setOnClickListener(view -> {
            //create message
            String message = "This is the Bill details" + "\n";
            for (int messageLoop = 0; messageLoop < amount; messageLoop++) {
                message = message + "People " + (messageLoop + 1) + ": RM" + String.format(Locale.getDefault(), "%.2f", averageAmount) + "\n";
            }
            String finalMessage = message;
            shareResult(finalMessage);
        });

    }
    public void shareResult(String message) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "WhatsApp not installed on your device", Toast.LENGTH_SHORT).show();
        }
    }
}
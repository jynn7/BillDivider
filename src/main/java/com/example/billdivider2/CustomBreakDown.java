package com.example.billdivider2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.telephony.PhoneNumberUtils;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class CustomBreakDown extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_break_down);

        TextView totalPriceCustom=findViewById(R.id.tv_total_price_equal_num);
        TextView totalHeadCountCustom=findViewById(R.id.tv_head_count_equal_num);

        Button btConfirm=findViewById(R.id.bt_custom_method);
        Button btCalculate=findViewById(R.id.bt_calculate_custom);
        Button btShare=findViewById(R.id.share_Button);

        RadioButton rbPercentage=findViewById(R.id.rb_percentage_word);
        RadioButton rbRatio=findViewById(R.id.rb_ratio_word);
        RadioButton rbAmount=findViewById(R.id.rb_amount_word);

        TableLayout tableLayoutInputBox=findViewById(R.id.table_layout_input);
        LinearLayout linearLayoutPriceShow = findViewById(R.id.price_per_head_show);

        //retrieve intentExtra
        String inputDataPrice=getIntent().getStringExtra("inputTotalPrice");
        String inputDataHeadCount=getIntent().getStringExtra("inputHeadCount");

        //set total price and head count in this page
        totalPriceCustom.setText(inputDataPrice);
        totalHeadCountCustom.setText(inputDataHeadCount);

        //head count amount to local
        int amount=Integer.parseInt(inputDataHeadCount);

        //array list
        //use by percentage,ratio,amount
        ArrayList<Float> allList=new ArrayList<>();
        ArrayList <Float> pricePerHeadList=new ArrayList<>();

        //share
        String phoneNumber="+60175862218";
        btShare.setOnClickListener(view -> {
            //create message
            String message = "This is the Bill details" + "\n";
            for (int messageLoop = 0; messageLoop < amount; messageLoop++) {
                message = message + "People " + (messageLoop + 1) + ": RM" + String.format(Locale.getDefault(), "%.2f",pricePerHeadList.get(messageLoop)).toString() + "\n";
            }
            String finalMessage = message;
            sendToWhatsApp(phoneNumber, finalMessage);
        });

        //confirm and generate input for the percentage
        btConfirm.setOnClickListener(view->{
            // Clear the content of the allList ArrayList
            allList.clear();

            // Clear the content of the pricePerHeadList ArrayList
            pricePerHeadList.clear();

            tableLayoutInputBox.removeAllViews();
            //extend height of table
            if(amount>5){
                int extendSize = (amount/ 5) * 1200;

                TableLayout.LayoutParams layoutParams=new TableLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        extendSize

                );
                tableLayoutInputBox.setLayoutParams(layoutParams);
            }

            for(int i=0;i<amount;i++){
                TextView percentageInputDesc=new TextView(this);
                EditText editText=new EditText(this);
                TableRow addRow=new TableRow(this);

                addRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));
                percentageInputDesc.setLayoutParams(new TableRow.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                percentageInputDesc.setText("People "+(i+1)+": ");

                editText.setLayoutParams(new TableRow.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                addRow.addView(percentageInputDesc);
                addRow.addView(editText);
                tableLayoutInputBox.addView(addRow);


            }
        });


        btCalculate.setOnClickListener(view->{

            Float totalAmountCalc=Float.parseFloat(totalPriceCustom.getText().toString());
            int rowCount=amount;
            int runStatus=1;
            if(rbPercentage.isChecked()){
                //get row count
                //int rowCount=tablelayoutInputBox.getChildCount();
                //loop the table layout and get input value at column 2 of every row

                for (int j = 0; j < rowCount; j++) {
                    EditText inputPercentageBox = (EditText) ((TableRow) tableLayoutInputBox.getChildAt(j)).getChildAt(1);
                    String inputPercentage = inputPercentageBox.getText().toString();

                    // Add the input value to the ArrayList
                    try {
                        // Parse the inputPercentage string to a float and add it to the allList
                        float percentageValue = Float.parseFloat(inputPercentage);
                        allList.add(percentageValue);
                    } catch (NumberFormatException e){

                    }
                }

                //calculate the amount and input into array list that store price to paid by each people
                for(int calcPrice=0;calcPrice<amount;calcPrice++){
                    float totalAmountCalcResult=totalAmountCalc*(allList.get(calcPrice)/100);
                    pricePerHeadList.add(totalAmountCalcResult);
                }

            }
            else if(rbRatio.isChecked()){
                for (int j = 0; j < rowCount; j++) {
                    EditText inputRatioBox = (EditText) ((TableRow) tableLayoutInputBox.getChildAt(j)).getChildAt(1);
                    String inputRatio = inputRatioBox.getText().toString();

                    // Add the input value to the ArrayList
                    try {
                        // Parse the inputPercentage string to a float and add it to the allList
                        float ratioValue = Float.parseFloat(inputRatio);
                        allList.add(ratioValue);
                    } catch (NumberFormatException e){

                    }
                }

                //calculate the amount and input into array list that store price to paid by each people
                float ratioTotal=0;
                int floatTotalCalc=1;
                for(int calcPrice=0;calcPrice<amount;calcPrice++){
                    if(floatTotalCalc==1){
                        for(int a=0;a<amount;a++){
                            ratioTotal=ratioTotal+allList.get(a);
                        }
                        floatTotalCalc=0;
                    }
                    float totalAmountCalcResult=totalAmountCalc*(allList.get(calcPrice)/ratioTotal);
                    pricePerHeadList.add(totalAmountCalcResult);
                }


            }
            else if(rbAmount.isChecked()){
                for (int j = 0; j < rowCount; j++) {
                    EditText inputAmountBox = (EditText) ((TableRow) tableLayoutInputBox.getChildAt(j)).getChildAt(1);
                    String inputAmount = inputAmountBox.getText().toString();

                    // Add the input value to the ArrayList
                    try {
                        // Parse the inputPercentage string to a float and add it to the allList
                        float amountValue = Float.parseFloat(inputAmount);
                        allList.add(amountValue);
                    } catch (NumberFormatException e){

                    }
                }

                //calculate the amount and input into array list that store price to paid by each people
                // Calculate the total amount
                float totalAmount = 0;
                for (int i = 0; i < allList.size(); i++) {
                    totalAmount += allList.get(i);
                }

                // Check if the calculated total amount matches the provided totalAmountCalc
                if (Math.abs(totalAmount - totalAmountCalc) > 0.001) { // Using a small epsilon for float comparison
                    Toast.makeText(this, "Error: Total amount does not match.", Toast.LENGTH_SHORT).show();
                    runStatus=0;

                }
                else {
                    // Calculate the individual price to be paid by each person
                    for (int calcPrice = 0; calcPrice < amount; calcPrice++) {
                        float totalAmountCalcResult = totalAmountCalc * (allList.get(calcPrice) / totalAmount);
                        pricePerHeadList.add(totalAmountCalcResult);
                    }
                }
            }
            else{
                Toast.makeText(this, "No Selection Done", Toast.LENGTH_SHORT).show();
            }

            if (runStatus == 1) {
                linearLayoutPriceShow.removeAllViews();
                //extend height of table
                if(amount>15){
                    int extendSize = amount * 100;

                    TableLayout.LayoutParams layoutParams=new TableLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            extendSize

                    );
                    linearLayoutPriceShow.setLayoutParams(layoutParams);
                }

                for(int k=0;k<amount;k++) {
                    //create text view
                    TextView pricePerHeadWord = new TextView(this);
                    TextView pricePerHeadNum = new TextView(this);

                    //create a linear layout(horizontal)
                    LinearLayout rowToKeepTV=new LinearLayout(this);
                    rowToKeepTV.setOrientation(LinearLayout.HORIZONTAL);
                    rowToKeepTV.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    //THIS IS price description layout
                    pricePerHeadWord.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    pricePerHeadWord.setText("Price for Person " + (k + 1) + ": ");

                    //create text view for show amount
                    pricePerHeadNum.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    pricePerHeadNum.setText("RM"+String.format(Locale.getDefault(), "%.2f",pricePerHeadList.get(k)).toString());

                    //add view into linearLayout
                    rowToKeepTV.addView(pricePerHeadWord);
                    rowToKeepTV.addView(pricePerHeadNum);
                    linearLayoutPriceShow.addView(rowToKeepTV);
                }
            }

        });

    }


    public void sendToWhatsApp(String phoneNumber, String message) {
        // Use the WhatsApp package name
        String whatsAppPackageName = "com.whatsapp";

        // Check if WhatsApp is installed on the device
        //PackageManager packageManager = getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(phoneNumber) + "@s.whatsapp.net");

        try {
            // launch WhatsApp
            //packageManager.getPackageInfo(whatsAppPackageName, PackageManager.GET_META_DATA);
            //sendIntent.setPackage(whatsAppPackageName);
            startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            // WhatsApp is not installed
            Toast.makeText(this, "WhatsApp not installed on your device", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.shadow.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final double EURO = 0.93; // Alt + 0128
    private final double POUND = 0.8; // Alt + 156

    public void convertMoney(View view)
    {
        EditText usdValue = (EditText)findViewById(R.id.usdValue);
        if(!usdValue.getText().toString().isEmpty())
        {
            double money = Double.parseDouble(usdValue.getText().toString()) * POUND;
            Toast.makeText(this, money > 0? "£" + String.format("%.2f", money) : "Error! Please Enter a positive value", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

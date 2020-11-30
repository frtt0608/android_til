package com.example.basicsum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText input1, input2;
    TextView result;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input1 = (EditText) findViewById(R.id.input1);
        input2 = (EditText) findViewById(R.id.input2);
        result = (TextView) findViewById(R.id.result);
        b1 = (Button) findViewById(R.id.b1);

        b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String inputStr1 = input1.getText().toString();
               String inputStr2 = input2.getText().toString();
               int val1 = Integer.parseInt(inputStr1);
               int val2 = Integer.parseInt(inputStr2);

               int sum = val1 + val2;

               result.setText(Integer.toString(sum));
           }
        });
    }
}
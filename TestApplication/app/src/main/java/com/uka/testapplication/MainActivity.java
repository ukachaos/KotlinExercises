package com.uka.testapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    MyExample mMyExample;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication)getApplication()).getComponent().inject(this);


        tv = findViewById(R.id.textView);

        tv.setText((new Date(mMyExample.getDate())).toString());
    }
}

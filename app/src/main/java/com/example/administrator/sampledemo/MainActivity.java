package com.example.administrator.sampledemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.activity_main_btn_line_view:
                intent = new Intent(this, LineViewActivity.class);
                break;
            case R.id.activity_main_btn_rect_password:
                intent = new Intent(this, RectPasswordActivity.class);
                break;
        }
        startActivity(intent);

    }
}

package com.gsww.www.arcmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ArcMenu.OnMenuItemClickListener {

    private ArcMenu arcMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         arcMenu = (ArcMenu) findViewById(R.id.arcMenu);
         arcMenu.setOnMenuItemClickListener(this);
    }

    @Override
    public void itemClick(int index) {
        Toast.makeText(this, "点击了"+index, Toast.LENGTH_SHORT).show();
    }
}

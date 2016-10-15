package com.autonavi.automenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import common.amapauto_relative.AMMapFunctionManager;
import common.system_tools.AMSystemManager;
import module_menu.AMMainMenuFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AMSystemManager.sharedInstance().setFullScreen(this);
        setContentView(R.layout.activity_main);
        AMSystemManager.sharedInstance().setFullScreenAfterAddContent(this);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_container, new AMMainMenuFragment()).commit();
    }

    @Override
    protected void onResume() {
        AMSystemManager.sharedInstance().setLandscape(this);
        super.onResume();
        AMMapFunctionManager.sharedInstance().registerAmapAutoReciver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AMMapFunctionManager.sharedInstance().unregisterAmapAutoReciver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

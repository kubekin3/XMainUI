package com.kubekin.xmainui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SDMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getBaseContext();
        Intent intent = new Intent();
        intent.setClassName("com.ts.MainUI", "com.ts.main.Media.SDMainActivity");
        startActivity(intent);
        finish();
    }
}

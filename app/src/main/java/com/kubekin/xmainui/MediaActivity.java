package com.kubekin.xmainui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MediaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String mediaApp = Settings.System.getString(this.getApplicationContext().getContentResolver(), "media_path_pname");

        Intent it = getPackageManager().getLaunchIntentForPackage(mediaApp);
        if (it != null) {
            startActivity(it);
        } else {
            ComponentName componentName = new ComponentName("com.ts.MainUI", "com.ts.set.SettingGpsPathActivity");
            Intent intent = new Intent();
            intent.setComponent(componentName);
            intent.addFlags((int) 337641472);
            XMain.setupMediaPath = true;
            this.getApplicationContext().startActivity(intent);
        }
        finish();

    }
}

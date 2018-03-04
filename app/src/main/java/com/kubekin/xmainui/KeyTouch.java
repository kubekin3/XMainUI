package com.kubekin.xmainui;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by Yuriy on 25.02.2018.
 */

public class KeyTouch {
    public static Class clazz = XposedHelpers.findClass("com.ts.main.common.MainUI.KeyTouch", null);

    public static KeyTouch GetInstance() {
        return (KeyTouch)XposedHelpers.callStaticMethod(KeyTouch.clazz, "GetInstance");
    }

}

package com.kubekin.xmainui;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by Yuriy on 25.02.2018.
 */

public class Evc {
    public static Class clazz = XposedHelpers.findClass("com.ts.MainUI.Evc", null);

    public static int mSystemState = XposedHelpers.getIntField(clazz, "mSystemState");

}

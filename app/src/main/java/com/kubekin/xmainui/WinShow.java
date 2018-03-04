package com.kubekin.xmainui;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by Yuriy on 25.02.2018.
 */

public class WinShow {
    public static Class clazz = XposedHelpers.findClass("com.ts.main.common.WinShow", null);

    public static void GotoWin(int nWin, int nParat1){
        XposedHelpers.callStaticMethod(WinShow.clazz, "GotoWin", nWin, nParat1);
    }
}

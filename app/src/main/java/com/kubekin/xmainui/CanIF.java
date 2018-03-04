package com.kubekin.xmainui;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by Yuriy on 25.02.2018.
 */

public class CanIF {
    public static Class clazz = XposedHelpers.findClass("com.ts.can.CanIF", null);

    public static void DealCam360Key(int nKeyCode) {
        XposedHelpers.callStaticMethod(CanIF.clazz, "DealCam360Key", nKeyCode);
    }

    public static boolean Deal360CameraKey() {
        return (boolean) XposedHelpers.callStaticMethod(CanIF.clazz, "Deal360CameraKey");
    }
}

package com.kubekin.xmainui;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by Yuriy on 24.02.2018.
 */

public class Mcu {
    public static Class clazz = XposedHelpers.findClass("com.yyw.ts90xhw.Mcu", null);

    public static int GetPkey(){
        return (int)XposedHelpers.callStaticMethod(Mcu.clazz, "GetPkey");
    }

    public static int GetEkey(){
        return (int)XposedHelpers.callStaticMethod(Mcu.clazz, "GetEkey");
    }

    public static int GetRkey(){
        return (int)XposedHelpers.callStaticMethod(Mcu.clazz, "GetRkey");
    }

    public static int GetSkey(){
        return (int)XposedHelpers.callStaticMethod(Mcu.clazz, "GetSkey");
    }
}

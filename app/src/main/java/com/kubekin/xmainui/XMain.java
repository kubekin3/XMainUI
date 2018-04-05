package com.kubekin.xmainui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.XModuleResources;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static android.content.ContentValues.TAG;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;


/**
 * Created by Yuriy on 17.02.2018.
 */
public class XMain implements IXposedHookLoadPackage, IXposedHookInitPackageResources {
    private Object showBackSW;
    private Object showHomeSW;
    private Object showExtPanelSW;
    private Object mediaPathSet;

    static boolean setupMediaPath = false;
    static int pKey;
    static int mEvc_Evol_workmode;
    static String lastAddrBluetooth;

    static boolean micGain = false;

    class KeyDef {
        public static final int SKEY_SEEKUP_2 = 785;
        public static final int SKEY_CHUP_1 = 794;
        public static final int PKEY_RIGHT = 69;
        public static final int RKEY_RIGHT = 287;
        public static final int PKEY_NEXT = 44;
        public static final int RKEY_NEXT = 291;

        public static final int PKEY_UP = 66;

        public static final int SKEY_SEEKDN_2 = 790;
        public static final int SKEY_CHDN_1 = 799;
        public static final int PKEY_LEFT = 68;
        public static final int RKEY_LEFT = 288;
        public static final int PKEY_PRE = 45;
        public static final int RKEY_PRE = 292;

        public static final int PKEY_DN = 67;
    }

    private void setViewPos(View view, int x, int y, int w, int h) {
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(w, h);
        rl.leftMargin = x;
        rl.topMargin = y - 60;
        view.setLayoutParams(rl);
    }

    public void handleLoadPackage(final LoadPackageParam paramLoadPackageParam) throws Throwable {

        if (paramLoadPackageParam.packageName.equals("com.ts.MainUI")) {
            Class CustomImgViewClass = XposedHelpers.findClass("com.ts.customctrl.CustomImgView", paramLoadPackageParam.classLoader);
            Class CanVwCarInfoActivityClass = XposedHelpers.findClass("com.ts.can.CanVwCarInfoActivity", paramLoadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(CanVwCarInfoActivityClass, "InitUI", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    RelativeLayout mLayout = (RelativeLayout) XposedHelpers.getObjectField(param.thisObject, "mLayout");
                    View mWarnBelt = (View) XposedHelpers.getObjectField(param.thisObject, "mWarnBelt");
                    View mWarnBrake = (View) XposedHelpers.getObjectField(param.thisObject, "mWarnBrake");
                    View mWarnTrunk = (View) XposedHelpers.getObjectField(param.thisObject, "mWarnTrunk");
                    View mWarnWash = (View) XposedHelpers.getObjectField(param.thisObject, "mWarnWash");
                    View mWarnBattery = (View) XposedHelpers.getObjectField(param.thisObject, "mWarnBattery");
                    View mWarnOil = (View) XposedHelpers.getObjectField(param.thisObject, "mWarnOil");

                    setViewPos(mWarnOil, 57, 93, 92, 93);
                    setViewPos(mWarnBattery, 57, 464, 92, 93);

                    setViewPos(mWarnBelt, 306, 93, 92, 93);
                    setViewPos(mWarnTrunk, 306, 217, 92, 93);
                    setViewPos(mWarnBrake, 306, 340, 92, 93);
                    setViewPos(mWarnWash, 306, 464, 92, 93);

                }
            });

            XposedHelpers.findAndHookMethod(CanVwCarInfoActivityClass, "ShowWarn", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.setBooleanField(param.thisObject, "mfgWarn", false);
                    XposedHelpers.setBooleanField(param.thisObject, "mfgDoorOpen", false);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object mInfo2 = XposedHelpers.getObjectField(param.thisObject, "mInfo2");

                    String mStrOil = String.format("%d л", XposedHelpers.getIntField(mInfo2, "RemainOil"));
                    String mStrOutTemp = String.format("%.1f °c", XposedHelpers.getIntField(mInfo2, "OutTemp") * 0.1);
                    String mStrBattery = String.format("%.1f в", XposedHelpers.getIntField(mInfo2, "Voltage") * 0.01);
                    String mStrRpm = String.format("%d об", XposedHelpers.getIntField(mInfo2, "Rpm"));
                    String mStrSpeed = String.format("%d км/ч", XposedHelpers.getIntField(mInfo2, "CurSpeed") * 0.01);
                    String mStrRange = String.format("%d км", XposedHelpers.getIntField(mInfo2, "Range"));

                    Object mIcoOil = XposedHelpers.getObjectField(param.thisObject, "mIcoOil");
                    Object mIcoOutTemp = XposedHelpers.getObjectField(param.thisObject, "mIcoOil");
                    Object mIcoBattery = XposedHelpers.getObjectField(param.thisObject, "mIcoOil");
                    Object mRpm = XposedHelpers.getObjectField(param.thisObject, "mIcoOil");
                    Object mSpeed = XposedHelpers.getObjectField(param.thisObject, "mIcoOil");
                    Object mRange = XposedHelpers.getObjectField(param.thisObject, "mIcoOil");

                    XposedHelpers.callMethod(mIcoOil, "SetValue", mStrOil);
                    XposedHelpers.callMethod(mIcoOutTemp, "SetValue", mStrOutTemp);
                    XposedHelpers.callMethod(mIcoBattery, "SetValue", mStrBattery);
                    XposedHelpers.callMethod(mRpm, "SetValue", mStrRpm);
                    XposedHelpers.callMethod(mSpeed, "SetValue", mStrSpeed);
                    XposedHelpers.callMethod(mRange, "SetValue", mStrRange);
                }
            });

            XposedHelpers.findAndHookMethod(CanVwCarInfoActivityClass, "SetWarnIco", CustomImgViewClass, boolean.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.callMethod(param.args[0], "Show", param.args[1]);
                    if ((Boolean) param.args[1] == true) {
                        XposedHelpers.callMethod(param.args[0], "setSelected", param.args[1]);
                    }

                    param.setResult(0);

                }
            });

            // непоказвать окон по кану
            Class CanFuncClass = XposedHelpers.findClass("com.ts.can.CanFunc", paramLoadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(CanFuncClass, "showCanActivity", Class.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(0);
                }
            });
        }

        if (paramLoadPackageParam.packageName.equals("com.ts.MainUI")) {

            // в боксе при разговоре кнопка сворачивает бокс
            Class BtCallMsgboxClass = XposedHelpers.findClass("com.ts.bt.BtCallMsgbox", paramLoadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(BtCallMsgboxClass, "onClick", View.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    View v = (View) param.args[0];

                    if (((Integer) v.getTag()).intValue() == 17) {
                        XposedHelpers.callMethod(param.thisObject, "Hide");
                        Log.d("BtCallMsgbox", "BTN_KB");
                        param.setResult(0);
                    }
                }
            });

            Class BtExeClass = XposedHelpers.findClass("com.ts.bt.BtExe", paramLoadPackageParam.classLoader);

            // отключаем установку флага, что бы поиск телефона был и при ативном окне
            XposedHelpers.findAndHookMethod(BtExeClass, "setUI", boolean.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(0);
                }
            });

            // переопределяем метод автопоиска для уменьшения времени поиска, а так же для подмены адреса для коннекта
            XposedHelpers.findAndHookMethod(BtExeClass, "checkAutoConnect", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Object _this = param.thisObject;
                    Class _thisClass = _this.getClass();

                    boolean mIsAutoConnect = XposedHelpers.getBooleanField(_this, "mIsAutoConnect");
                    boolean mbNeedAutoConnect = XposedHelpers.getStaticBooleanField(_thisClass, "mbNeedAutoConnect");
                    boolean mbConnectUI = XposedHelpers.getStaticBooleanField(_thisClass, "mbConnectUI");

                    if (mIsAutoConnect && mbNeedAutoConnect) {
                        long tick = (long) XposedHelpers.callStaticMethod(_thisClass, "getTickCount");
                        if (mbConnectUI || (boolean) XposedHelpers.callMethod(_this, "isConnected")) {
                            XposedHelpers.setStaticLongField(_thisClass, "mLastConnectTick", tick);
                        } else if (tick > XposedHelpers.getStaticLongField(_thisClass, "mLastConnectTick") + 20000) {
                            Log.d("BtExe", "checkAutoConnect connect");

                            if (lastAddrBluetooth == null || lastAddrBluetooth.isEmpty()) {
                                XposedHelpers.callMethod(_this, "getLastAddr");
                                lastAddrBluetooth = (String) XposedHelpers.getStaticObjectField(_thisClass, "mLastDeviceAddr");
                            } else {
                                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                                for (BluetoothDevice bt : pairedDevices) {
                                    int deviceClass = bt.getBluetoothClass().getDeviceClass();
                                    if (deviceClass == BluetoothClass.Device.PHONE_CELLULAR ||
                                            deviceClass == BluetoothClass.Device.PHONE_SMART ||
                                            deviceClass == BluetoothClass.Device.PHONE_UNCATEGORIZED ||
                                            deviceClass == BluetoothClass.Device.PHONE_ISDN) {
                                        String newAddr = bt.getAddress();

                                        String mLastDeviceAddr = (String) XposedHelpers.getStaticObjectField(_thisClass, "mLastDeviceAddr");
                                        if (!mLastDeviceAddr.equalsIgnoreCase(newAddr)) {
                                            XposedHelpers.setStaticObjectField(_thisClass, "mLastDeviceAddr", newAddr);
                                            break;
                                        }
                                    }
                                }

                            }

                            XposedHelpers.callMethod(_this, "connect");
                            XposedHelpers.setStaticLongField(_thisClass, "mLastConnectTick", tick);
                        }
                    }

                    param.setResult(0);
                }
            });

            // если коннет удался возвращаем последний адрес, что бы произошел сброс старого адреса и записался новый если надо
            XposedHelpers.findAndHookMethod(BtExeClass, "handleProfileStateUpdate", Context.class, Intent.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Intent intent = (Intent) param.args[1];

                    int profilestate = intent.getIntExtra("android.bluetooth.profilemanager.extra.EXTRA_NEW_STATE", 2);
                    if (profilestate == 1) {
                        XposedHelpers.setStaticObjectField(param.thisObject.getClass(), "mbFirstConnect", true);
                        XposedHelpers.setStaticObjectField(param.thisObject.getClass(), "mLastDeviceAddr", lastAddrBluetooth);
                        //XposedHelpers.callMethod(param.thisObject, "pbSync", false);
                    }
                }
            });
        }

        if (paramLoadPackageParam.packageName.equals("com.ts.MainUI")) {

            // смотрим когда загрузились библиотеки и после этого хакаем методы чтения нажатий на кнопки
            XposedHelpers.findAndHookMethod(Runtime.class, "loadLibrary", String.class, ClassLoader.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    if (param.args[0] == "ts90xhw") {

                        XposedHelpers.findAndHookMethod("com.yyw.ts90xhw.Mcu", paramLoadPackageParam.classLoader, "GetPkey", new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                int result = (int) param.getResult();
                                pKey = result;
                                if (result != 0)
                                    XposedBridge.log("ts90xhw GetPkey() " + result);
                                if (result == 13) {
                                    Object activityThread = XposedHelpers.callStaticMethod(
                                            XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
                                    Context context = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");

                                    ComponentName componentName = new ComponentName("com.kubekin.xmainui", "com.kubekin.xmainui.MediaActivity");
                                    Intent intent = new Intent();
                                    intent.setComponent(componentName);
                                    intent.addFlags((int) 337641472);
                                    XMain.setupMediaPath = true;
                                    param.setResult(0);
                                    context.startActivity(intent);
                                }
                            }
                        });

                        XposedHelpers.findAndHookMethod("com.yyw.ts90xhw.Mcu", paramLoadPackageParam.classLoader, "GetEkey", new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                int result = (int) param.getResult();
                                if (result != 0)
                                    XposedBridge.log("ts90xhw GetEkey() " + result);
                                //if (result == 514) param.setResult(0);
                            }
                        });

                        XposedHelpers.findAndHookMethod("com.yyw.ts90xhw.Mcu", paramLoadPackageParam.classLoader, "GetRkey", new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                int result = (int) param.getResult();
                                if (result != 0)
                                    XposedBridge.log("ts90xhw GetRkey() " + result);
                            }
                        });

                        XposedHelpers.findAndHookMethod("com.yyw.ts90xhw.Mcu", paramLoadPackageParam.classLoader, "GetSkey", new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                int result = (int) param.getResult();
                                if (result != 0)
                                    XposedBridge.log("ts90xhw GetSkey() " + result);
                            }
                        });
                    }
                }
            });
        }

        if (paramLoadPackageParam.packageName.equals("com.ts.MainUI")) {
            // отключаем проверку на аудиовывод для показа радио всегда
            Class MainUIClass = XposedHelpers.findClass("com.ts.main.common.MainUI", paramLoadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(MainUIClass, "DealKey", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (pKey == 43) {
                        Class WinShowClass = XposedHelpers.findClass("com.ts.main.common.WinShow", paramLoadPackageParam.classLoader);

                        XposedHelpers.callStaticMethod(WinShowClass, "GotoWin", 2, 0);
                    }
                }
            });

            // заменяем действие кнопок переключится на станцию к поиску станции в радио
            Class RadioFuncClass = XposedHelpers.findClass("com.ts.main.radio.RadioFunc", paramLoadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(RadioFuncClass, "DealKey", int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    int nkey = (int) param.args[0];

                    XposedBridge.log(String.valueOf(nkey));
                    switch (nkey) {
                        case KeyDef.SKEY_SEEKUP_2 /*785*/:
                        case KeyDef.SKEY_CHUP_1 /*794*/:
                        case KeyDef.PKEY_RIGHT /*69*/:
                        case KeyDef.RKEY_RIGHT /*287*/:
                        case KeyDef.PKEY_NEXT /*44*/:
                        case KeyDef.RKEY_NEXT /*291*/:
                            nkey = KeyDef.PKEY_UP /*66*/;
                            break;

                        case KeyDef.SKEY_SEEKDN_2 /*790*/:
                        case KeyDef.SKEY_CHDN_1 /*799*/:
                        case KeyDef.PKEY_LEFT /*68*/:
                        case KeyDef.RKEY_LEFT /*288*/:
                        case KeyDef.PKEY_PRE /*45*/:
                        case KeyDef.RKEY_PRE /*292*/:
                            nkey = KeyDef.PKEY_DN /*67*/;
                            break;
                    }
                    XposedBridge.log(String.valueOf(nkey));
                    param.args[0] = nkey;
                }
            });
        }

        if (paramLoadPackageParam.packageName.equals("com.ts.MainUI")) {
            Class SettingGerenalActivityClass = XposedHelpers.findClass("com.ts.set.SettingGerenalActivity", paramLoadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(SettingGerenalActivityClass, "initSetGeneraOptions", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    final Context context = (Context) param.thisObject;

                    Class SetMainItemSwClass = XposedHelpers.findClass("com.ts.set.setview.SetMainItemSw", paramLoadPackageParam.classLoader);
                    Class SetMainItemNoIconClass = XposedHelpers.findClass("com.ts.set.setview.SetMainItemNoIcon", paramLoadPackageParam.classLoader);

                    Constructor SetMainItemSwConstructor = XposedHelpers.findConstructorExact(SetMainItemSwClass, Context.class, String.class);
                    Constructor SetMainItemNoIconConstructor = XposedHelpers.findConstructorExact(SetMainItemNoIconClass, Context.class, String.class);

                    showBackSW = SetMainItemSwConstructor.newInstance(param.thisObject, "Показывать кнопку Back");
                    showHomeSW = SetMainItemSwConstructor.newInstance(param.thisObject, "Показывать кнопку Home и Recent");
                    showExtPanelSW = SetMainItemSwConstructor.newInstance(param.thisObject, "Показывать дополнительные плитки");
                    mediaPathSet = SetMainItemNoIconConstructor.newInstance(param.thisObject, "Приложение Музыка");


                    Class SetSroViewClass = XposedHelpers.findClass("com.ts.set.setview.SetSroView", paramLoadPackageParam.classLoader);
                    Object showBackSWView = XposedHelpers.callMethod(showBackSW, "GetView");
                    Object showHomeSWView = XposedHelpers.callMethod(showHomeSW, "GetView");
                    Object showExtPanelSWView = XposedHelpers.callMethod(showExtPanelSW, "GetView");
                    Object mediaPathSetView = XposedHelpers.callMethod(mediaPathSet, "GetView");


                    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            int checked = isChecked ? 1 : 0;
                            ContentResolver contentResolver = context.getContentResolver();

                            switch (((Integer) buttonView.getTag()).intValue()) {
                                case 0:
                                    XposedHelpers.callMethod(showBackSW, "SetSwitch", checked);
                                    Settings.System.putInt(contentResolver, "show_back_button", checked);
                                    break;
                                case 1:
                                    XposedHelpers.callMethod(showHomeSW, "SetSwitch", checked);
                                    Settings.System.putInt(contentResolver, "show_home_button", checked);
                                    break;
                                case 2:
                                    XposedHelpers.callMethod(showExtPanelSW, "SetSwitch", checked);
                                    Settings.System.putInt(contentResolver, "show_ext_panel", checked);
                                    break;
                            }
                        }
                    };

                    View.OnClickListener onClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (((Integer) view.getTag()).intValue()) {
                                case 3:
                                    ComponentName componentName = new ComponentName("com.ts.MainUI", "com.ts.set.SettingGpsPathActivity");
                                    Intent intent = new Intent();
                                    intent.setComponent(componentName);
                                    intent.addFlags((int) 337641472);
                                    XMain.setupMediaPath = true;
                                    view.getContext().startActivity(intent);
                                    break;
                            }

                        }
                    };


                    XposedHelpers.callMethod(showBackSW, "SetUserCallback", 0, onCheckedChangeListener);
                    XposedHelpers.callMethod(showHomeSW, "SetUserCallback", 1, onCheckedChangeListener);
                    XposedHelpers.callMethod(showExtPanelSW, "SetUserCallback", 2, onCheckedChangeListener);

                    XposedHelpers.callMethod(mediaPathSet, "SetUserCallback", 3, onClickListener);

                    callStaticMethod(SetSroViewClass, "AddView", showBackSWView);
                    callStaticMethod(SetSroViewClass, "AddView", showHomeSWView);
                    callStaticMethod(SetSroViewClass, "AddView", showExtPanelSWView);
                    callStaticMethod(SetSroViewClass, "AddView", mediaPathSetView);
                }
            });

            XposedHelpers.findAndHookMethod("com.ts.set.SettingGerenalActivity", paramLoadPackageParam.classLoader, "onResume", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Context context = (Context) param.thisObject;
                    int showBackSWValue = Settings.System.getInt(context.getContentResolver(), "show_back_button", 1);
                    int showHomeSWValue = Settings.System.getInt(context.getContentResolver(), "show_home_button", 0);
                    int showExtPanelSWValue = Settings.System.getInt(context.getContentResolver(), "show_ext_panel", 0);
                    String mediaAppName = Settings.System.getString(context.getContentResolver(), "media_path_appname");

                    XposedHelpers.callMethod(showBackSW, "SetSwitch", showBackSWValue);
                    XposedHelpers.callMethod(showHomeSW, "SetSwitch", showHomeSWValue);
                    XposedHelpers.callMethod(showExtPanelSW, "SetSwitch", showExtPanelSWValue);
                    XposedHelpers.callMethod(mediaPathSet, "ShowEndInfo", mediaAppName);
                }
            });
        }

        if (paramLoadPackageParam.packageName.equals("com.ts.MainUI")) {
            XposedHelpers.findAndHookMethod("com.ts.main.common.MainSet", paramLoadPackageParam.classLoader, "IsAvalidPackName", String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String name = (String) param.args[0];
                    if (name.contains("com.ts")) param.setResult(true);
                }
            });

            XposedHelpers.findAndHookMethod("com.ts.main.common.WinShow", paramLoadPackageParam.classLoader, "show", String.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String sPackage = (String) param.args[0];
                    String sActivity = (String) param.args[1];

                    if (sPackage == "com.ts.MainUI" && sActivity == "com.ts.set.SettingGpsPathActivity")
                        XMain.setupMediaPath = false;
                }
            });

            XposedHelpers.findAndHookMethod("com.ts.set.SettingGpsPathActivity", paramLoadPackageParam.classLoader, "onClick", View.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (XMain.setupMediaPath) {
                        View view = (View) param.args[0];
                        ArrayList<Object> apps = (ArrayList<Object>) XposedHelpers.getObjectField(param.thisObject, "apps");
                        int n = ((Integer) view.getTag()).intValue();
                        String name = (String) XposedHelpers.getObjectField(apps.get(n), "appname");
                        String pname = (String) XposedHelpers.getObjectField(apps.get(n), "pname");

                        ContentResolver contentResolver = view.getContext().getContentResolver();
                        Settings.System.putString(contentResolver, "media_path_appname", name);
                        Settings.System.putString(contentResolver, "media_path_pname", pname);

                        XposedHelpers.callMethod(param.thisObject, "finish");
                        param.setResult(null);
                    }
                }
            });
        }
    }

    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if (resparam.packageName.equals("com.ts.MainUI")) {
            resparam.res.setReplacement("com.ts.MainUI", "string", "title_activity_sdmain", "Музыка");
            resparam.res.setReplacement("com.ts.MainUI", "string", "title_activity_dvd_main", "Диск");
            resparam.res.setReplacement("com.ts.MainUI", "string", "title_activity_usbmain", "Видео");

            resparam.res.setReplacement("com.ts.MainUI", "string", "vol_common_mainvol", "Громкость");
            resparam.res.setReplacement("com.ts.MainUI", "string", "vol_common_btvol", "Громкость");
            resparam.res.setReplacement("com.ts.MainUI", "string", "vol_common_navivol", "Громкость");
            resparam.res.setReplacement("com.ts.MainUI", "string", "vol_common_mut", "Тихо");

            resparam.res.setReplacement("com.ts.MainUI", "string", "can_rest_oil", "Топливо");
            resparam.res.setReplacement("com.ts.MainUI", "string", "can_out_temp", "Температура");
            resparam.res.setReplacement("com.ts.MainUI", "string", "can_battery", "Аккумулятор");
            resparam.res.setReplacement("com.ts.MainUI", "string", "can_belt", "Ремень");
            resparam.res.setReplacement("com.ts.MainUI", "string", "can_trunk", "Багажник");
            resparam.res.setReplacement("com.ts.MainUI", "string", "can_brake", "Ручник");
            resparam.res.setReplacement("com.ts.MainUI", "string", "can_wash", "Омыватель");
            resparam.res.setReplacement("com.ts.MainUI", "string", "can_rpm", "Обороты");
            resparam.res.setReplacement("com.ts.MainUI", "string", "can_curspeed", "Скорость");
            resparam.res.setReplacement("com.ts.MainUI", "string", "can_range", "Пробег");

            int canvw_car3_up = resparam.res.getIdentifier("canvw_car3_up", "drawable", "com.ts.MainUI");
            resparam.res.setReplacement("com.ts.MainUI", "drawable", "canvw_car2_up", resparam.res.getDrawable(canvw_car3_up));

            int canvw_car3trunk_dn = resparam.res.getIdentifier("canvw_car3trunk_dn", "drawable", "com.ts.MainUI");
            resparam.res.setReplacement("com.ts.MainUI", "drawable", "canvw_car2trunk_dn", resparam.res.getDrawable(canvw_car3trunk_dn));
        }

        if (resparam.packageName.equals("com.ts.MainUI")) {
            resparam.res.hookLayout("com.ts.MainUI", "layout", "common_volume", new XC_LayoutInflated() {
                @Override
                public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                    SeekBar seekBar1 = (SeekBar) liparam.view.findViewById(
                            liparam.res.getIdentifier("seekBar1", "id", "com.ts.MainUI"));

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) seekBar1.getLayoutParams();
                    layoutParams.width = 740 - 40;
                    layoutParams.leftMargin = 173 + 40;

                    seekBar1.setLayoutParams(layoutParams);

                    Button volmodename = liparam.view.findViewById(
                            liparam.res.getIdentifier("volmodename", "id", "com.ts.MainUI"));

                    volmodename.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                }
            });
        }

        if (resparam.packageName.equals("com.android.systemui")) {
            resparam.res.hookLayout("com.android.systemui", "layout", "status_bar", new XC_LayoutInflated() {
                @Override
                public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                    Object activityThread = XposedHelpers.callStaticMethod(
                            XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
                    Context context = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");

                    int showBackSWValue = Settings.System.getInt(context.getContentResolver(), "show_back_button", 1);
                    int showHomeSWValue = Settings.System.getInt(context.getContentResolver(), "show_home_button", 0);

                    if (showBackSWValue == 0) {
                        Button my_back_button = (Button) liparam.view.findViewById(
                                liparam.res.getIdentifier("my_back_button", "id", "com.android.systemui"));

                        my_back_button.setVisibility(View.GONE);
                    }
                    if (showHomeSWValue == 0) {
                        Button my_home_button = (Button) liparam.view.findViewById(
                                liparam.res.getIdentifier("my_home_button", "id", "com.android.systemui"));
                        Button my_recent_button = (Button) liparam.view.findViewById(
                                liparam.res.getIdentifier("my_recent_button", "id", "com.android.systemui"));

                        my_home_button.setVisibility(View.GONE);
                        my_recent_button.setVisibility(View.GONE);
                    }
                }
            });

            resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_expanded", new XC_LayoutInflated() {
                @Override
                public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                    Object activityThread = XposedHelpers.callStaticMethod(
                            XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
                    Context context = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");

                    int showExtPanelSWValue = Settings.System.getInt(context.getContentResolver(), "show_ext_panel", 1);

                    if (showExtPanelSWValue == 0) {
                        Button forfan_button_acceleration = (Button) liparam.view.findViewById(
                                liparam.res.getIdentifier("forfan_button_acceleration", "id", "com.android.systemui"));

                        LinearLayout forfan_layout = (LinearLayout) forfan_button_acceleration.getParent();
                        forfan_layout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }


}
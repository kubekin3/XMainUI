package com.kubekin.xmainui;

import android.os.RemoteException;
import android.util.Log;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by Yuriy on 25.02.2018.
 */

public class MainUI {
    public static Class clazz = XposedHelpers.findClass("com.ts.main.common.MainUI", null);
    public static Object _this;

    public int nNawKey = XposedHelpers.getIntField(_this, "nNawKey");

    private int GetMcuState() {
        return (int)XposedHelpers.callMethod(_this, "GetMcuState");
    }

    public static int IsCameraMode() {
        return (int)XposedHelpers.callMethod(MainUI.clazz, "IsCameraMode");
    }

    public void DealKey(){
        /*final int n = 4;
        final int n2 = 2;
        final int n3 = 3;
        final int n4 = 1;

        int nNawKey = Mcu.GetPkey();
        if (nNawKey == 0) {
            nNawKey = Mcu.GetEkey();
            if (nNawKey == 0) {
                nNawKey = Mcu.GetRkey();
                if (nNawKey == 0) {
                    nNawKey = Mcu.GetSkey();
                }
            }
        }

        if (this.GetMcuState() == n3 && nNawKey != 81 && nNawKey != 332 && nNawKey != 769 && nNawKey != 70 && nNawKey != 314 && nNawKey != 0) {
            Log.d("MainUI", "Now is Power off so don't deal the key  = " + nNawKey);
        }
        else {
            if (nNawKey == 92 || nNawKey == 97) {
                CanIF.DealCam360Key(nNawKey);
                nNawKey = 0;
            }
            if ((nNawKey == 6 || nNawKey == 24) && CanIF.Deal360CameraKey()) {
                nNawKey = 0;
            }
            if (IsCameraMode() != n4 && Evc.mSystemState >= 10 && Evc.mSystemState < 20) {
                Label_1000: {
                    if (nNawKey > 0) {
                        Log.i("MainUI", "nDealPkey = = " + nNawKey);
                        switch (nNawKey) {
                            case 100: {
                                if (this.nNawKey > 0) {
                                    Log.i("MainUI", "Last key is not deal = = " + this.nNawKey);
                                }
                                this.nNawKey = nNawKey;
                                nNawKey = 0;
                                break;
                            }
                            case 516: {
                                if (MainSet.GetInstance().IsBMWVer() || MainSet.IsBmwX1() || MainSet.GetInstance().IsMINI()) {
                                    this.mKeyTouch.sendKeyClick(20);
                                    nNawKey = 0;
                                    break;
                                }
                                break;
                            }
                            case 515: {
                                if (MainSet.GetInstance().IsBMWVer() || MainSet.IsBmwX1() || MainSet.GetInstance().IsMINI()) {
                                    this.mKeyTouch.sendKeyClick(19);
                                    nNawKey = 0;
                                    break;
                                }
                                break;
                            }
                            case 21: {
                                if (MainSet.GetInstance().IsBMWVer() || MainSet.IsBmwX1() || MainSet.GetInstance().IsMINI()) {
                                    this.mKeyTouch.sendKeyClick(23);
                                    nNawKey = 0;
                                    break;
                                }
                                break;
                            }
                            case 2: {
                                WinShow.GotoWin(10, 0);
                                this.KeyBeep();
                                nNawKey = 0;
                                break;
                            }
                            case 3: {
                                WinShow.GotoWin(10, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 4: {
                                WinShow.GotoWin(23, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 1: {
                                WinShow.GotoWin(8, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 5: {
                                final Intent intent = new Intent("android.settings.DATE_SETTINGS");
                                intent.addFlags(337641472);
                                this.startActivity(intent);
                                nNawKey = 0;
                                break;
                            }
                            case 6:
                            case 24:
                            case 258: {
                                if (CanIF.Deal360CameraKey()) {
                                    nNawKey = 0;
                                    break;
                                }
                                Mcu.BklTurn();
                                nNawKey = 0;
                                break;
                            }
                            case 7:
                            case 257: {
                                WinShow.GotoWin(11, 98);
                                nNawKey = 0;
                                break;
                            }
                            case 9: {
                                this.SetVoiceState(false);
                                break;
                            }
                            case 8:
                            case 259: {
                                if (MainUI.bIsScreenMode) {
                                    KeyTouch.GetInstance().takeScreenShot("");
                                }
                                else {
                                    if (Mcu.BklisOn() == 0) {
                                        Mcu.BklTurn();
                                    }
                                    if (this.nHomeKey_enable == n4) {
                                        this.mKeyTouch.sendKeyClick(n3);
                                    }
                                }
                                nNawKey = 0;
                                break;
                            }
                            case 99: {
                                this.mKeyTouch.sendKeyClick(82);
                                nNawKey = 0;
                                break;
                            }
                            case 10:
                            case 261:
                            case 810: {
                                WinShow.DealModeKey();
                                nNawKey = 0;
                                break;
                            }
                            case 799: {
                                BtExe.getBtInstance();
                                if (BtExe.mCallSta == n3) {
                                    BtExe.getBtInstance().answer();
                                    nNawKey = 0;
                                    break;
                                }
                                break;
                            }
                            case 29:
                            case 273:
                            case 814: {
                                BtExe.getBtInstance();
                                if (BtExe.mCallSta == n3) {
                                    BtExe.getBtInstance().answer();
                                    break;
                                }
                                if (!BtExe.getBtInstance().isConnected()) {
                                    WinShow.GotoWin(7, 0);
                                }
                                else if (!BtExe.getBtInstance().isHaveCall()) {
                                    WinShow.GotoWin(7, n3);
                                }
                                else if (BtExe.getBtInstance().isHaveCall()) {
                                    BtExe.getBtInstance().hangup();
                                }
                                nNawKey = 0;
                                break;
                            }
                            case 30:
                            case 274:
                            case 794:
                            case 819: {
                                if (BtExe.getBtInstance().isHaveCall()) {
                                    BtExe.getBtInstance().hangup();
                                    nNawKey = 0;
                                    break;
                                }
                                break;
                            }
                            case 95:
                            case 96: {
                                if (MainSet.isZh() && FtSet.GetSpeechMode() != n3) {
                                    TxzReg.GetInstance().TxzStartMic(null);
                                }
                                else {
                                    this.mEvc.evol_mut_set(n2);
                                }
                                nNawKey = 0;
                                break;
                            }
                            case 829: {
                                if (CanJni.GetCanFsTp() == n3 && CanJni.GetSubType() == 0) {
                                    this.mKeyTouch.sendKeyClick(n);
                                    nNawKey = 0;
                                    break;
                                }
                            }
                            case 11:
                            case 262: {
                                if (MainUI.bIsScreenMode) {
                                    KeyTouch.GetInstance().takeScreenShot("");
                                }
                                else {
                                    final byte[] array = new byte[128];
                                    StSet.GetNaviPack(array);
                                    if (this.getTopPackageName().equals(CanIF.byte2String(array))) {
                                        if (this.mEvc.Evol.workmode == 0) {
                                            KeyTouch.GetInstance().sendKeyClick(n3);
                                        }
                                        else {
                                            WinShow.TsEnterMode(this.mEvc.Evol.workmode);
                                        }
                                    }
                                    else {
                                        WinShow.GotoWin(n4, 0);
                                    }
                                }
                                nNawKey = 0;
                                break;
                            }
                            case 12: {
                                WinShow.GotoWin(n2, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 13:
                            case 264: {
                                WinShow.GotoWin(n3, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 14:
                            case 265: {
                                WinShow.GotoWin(n, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 15:
                            case 266: {
                                WinShow.GotoWin(6, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 28: {
                                switch (this.mEvc.Evol.workmode) {
                                    default: {
                                        if (FtSet.IsIconExist(n) == n4) {
                                            WinShow.GotoWin(6, 0);
                                            break Label_1000;
                                        }
                                        if (FtSet.IsIconExist(n3) == n4) {
                                            WinShow.GotoWin(n, 0);
                                            break Label_1000;
                                        }
                                        if (FtSet.IsIconExist(n2) == n4) {
                                            WinShow.GotoWin(n3, 0);
                                            break Label_1000;
                                        }
                                        break Label_1000;
                                    }
                                    case 4: {
                                        if (FtSet.IsIconExist(n3) == n4) {
                                            WinShow.GotoWin(n, 0);
                                            break Label_1000;
                                        }
                                        if (FtSet.IsIconExist(n2) == n4) {
                                            WinShow.GotoWin(n3, 0);
                                            break Label_1000;
                                        }
                                        break Label_1000;
                                    }
                                    case 3: {
                                        if (FtSet.IsIconExist(n2) == n4) {
                                            WinShow.GotoWin(n3, 0);
                                            break Label_1000;
                                        }
                                        if (FtSet.IsIconExist(n) == n4) {
                                            WinShow.GotoWin(6, 0);
                                            break Label_1000;
                                        }
                                        break Label_1000;
                                    }
                                    case 2: {
                                        if (FtSet.IsIconExist(n) == n4) {
                                            WinShow.GotoWin(6, 0);
                                            break Label_1000;
                                        }
                                        if (FtSet.IsIconExist(n3) == n4) {
                                            WinShow.GotoWin(n, 0);
                                            break Label_1000;
                                        }
                                        break Label_1000;
                                    }
                                }
                                break;
                            }
                            case 16:
                            case 267:
                            case 805:
                            case 811: {
                                this.mEvc.evol_mut_set(n2);
                                nNawKey = 0;
                                break;
                            }
                            case 806: {
                                this.StartMic();
                                break;
                            }
                            case 17:
                            case 268: {
                                Log.i("MainUI", "PKEY_EQ = " + WinShow.getTopActivityName());
                                if (!WinShow.getTopActivityName().equals("com.ts.set.SettingSoundActivity")) {
                                    WinShow.TurnToEq();
                                }
                                else {
                                    this.nMode = this.mEvc.Evol.eqm;
                                    ++this.nMode;
                                    if (this.nMode > 5) {
                                        this.nMode = 0;
                                    }
                                    this.mEvc.evol_eqm_set(this.nMode);
                                }
                                nNawKey = 0;
                                break;
                            }
                            case 18: {
                                switch (this.mEvc.Evol.lud) {
                                    default: {
                                        this.mEvc.evol_lud_set(12);
                                        break;
                                    }
                                    case 12: {
                                        this.mEvc.evol_lud_set(6);
                                        break;
                                    }
                                    case 6: {
                                        this.mEvc.evol_lud_set(0);
                                        break;
                                    }
                                }
                                this.mEvc.evol_mut_set(0);
                                break;
                            }
                            case 43:
                            case 51:
                            case 52:
                            case 53:
                            case 263: {
                                if (this.mEvc.Evol.workmode != n4 && !WinShow.IsRadioActivity()) {
                                    WinShow.GotoWin(n2, 0);
                                    nNawKey = 0;
                                    break;
                                }
                                break;
                            }
                            case 19:
                            case 270:
                            case 514:
                            case 774:
                            case 776:
                            case 777: {
                                if (Mcu.BklisOn() == 0) {
                                    Mcu.BklTurn();
                                }
                                if (MainSet.GetInstance().IsMathToMcu()) {
                                    this.mMainVolume.VolWinShow();
                                    this.mEvc.Evol_vol_tune(n4);
                                    nNawKey = 0;
                                    break;
                                }
                                break;
                            }
                            case 20:
                            case 271:
                            case 513:
                            case 779:
                            case 781:
                            case 782: {
                                if (Mcu.BklisOn() == 0) {
                                    Mcu.BklTurn();
                                }
                                if (MainSet.GetInstance().IsMathToMcu()) {
                                    this.mMainVolume.VolWinShow();
                                    this.mEvc.Evol_vol_tune(0);
                                    nNawKey = 0;
                                    break;
                                }
                                break;
                            }
                            case 22:
                            case 272: {
                                this.mKeyTouch.sendKeyClick(n);
                                nNawKey = 0;
                                break;
                            }
                            case 23: {
                                WinShow.GotoWin(7, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 25: {
                                WinShow.GotoWin(13, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 26: {
                                WinShow.GotoWin(14, 0);
                                nNawKey = 0;
                                break;
                            }
                            case 82:
                            case 333: {
                                Mcu.SendXKey(16);
                                nNawKey = 0;
                                break;
                            }
                            case 81:
                            case 332: {
                                Mcu.SendXKey(17);
                                nNawKey = 0;
                                break;
                            }
                            case 70:
                            case 314:
                            case 769: {
                                if (this.GetMcuState() == n3) {
                                    Mcu.SendXKey(17);
                                    nNawKey = 0;
                                    break;
                                }
                                Mcu.SendXKey(16);
                                nNawKey = 0;
                                break;
                            }
                            case 71:
                            case 315: {
                                Label_3101: {
                                    if (this.mEvc.Evol.workmode != n2) {
                                        break Label_3101;
                                    }
                                    this.nDvdDelay = 100;
                                    Label_3095_Outer:
                                    while (true) {
                                        while (true) {
                                            if (this.mEvc.Evol.workmode != n2 || this.mTsPlayerService == null) {
                                                break Label_3095;
                                            }
                                            try {
                                                this.mTsPlayerService.enterMedia(0);
                                                nNawKey = 0;
                                                break;
                                                this.nDvdDelay = 10;
                                                continue Label_3095_Outer;
                                            }
                                            catch (RemoteException ex) {
                                                ex.printStackTrace();
                                                continue;
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                }
                            }
                            case 83:
                            case 334: {
                                Mcu.SendXKey(18);
                                nNawKey = 0;
                                break;
                            }
                        }
                    }
                }
                if (nNawKey > 0) {
                    Log.i("MainUI", "mEvc.Evol.workmode = " + this.mEvc.Evol.workmode);
                    Label_1152: {
                        switch (this.mEvc.Evol.workmode) {
                            case 1: {
                                if (RadioFunc.DealKey(nNawKey) == n4) {
                                    this.KeyBeep();
                                    break;
                                }
                                break;
                            }
                            case 5: {
                                if (BtFunc.DealKey(nNawKey) == n4) {
                                    this.KeyBeep();
                                    break;
                                }
                                break;
                            }
                            case 0: {
                                switch (nNawKey) {
                                    default: {
                                        break Label_1152;
                                    }
                                    case 44:
                                    case 291:
                                    case 789:
                                    case 799: {
                                        this.mKeyTouch.sendKeyClick(87);
                                        break Label_1152;
                                    }
                                    case 91: {
                                        this.mKeyTouch.sendKeyClick(127);
                                        break Label_1152;
                                    }
                                    case 90: {
                                        this.mKeyTouch.sendKeyClick(126);
                                        break Label_1152;
                                    }
                                    case 60:
                                    case 299:
                                    case 824: {
                                        this.mKeyTouch.sendKeyClick(85);
                                        break Label_1152;
                                    }
                                    case 45:
                                    case 292:
                                    case 784:
                                    case 794: {
                                        this.mKeyTouch.sendKeyClick(88);
                                        break Label_1152;
                                    }
                                }
                                break;
                            }
                            case 2:
                            case 3:
                            case 4:
                            case 15: {
                                try {
                                    if (this.mTsPlayerService != null) {
                                        this.mTsPlayerService.nDealMediaKey(nNawKey);
                                    }
                                }
                                catch (RemoteException ex2) {
                                    ex2.printStackTrace();
                                }
                                break;
                            }
                            case 6: {
                                if (Cmmb.GetInstance().DealCmmbKey(nNawKey) == n4) {
                                    this.KeyBeep();
                                    break;
                                }
                                break;
                            }
                            case 12: {
                                if (CanIF.DealExdKey(nNawKey) == n4) {
                                    this.KeyBeep();
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }*/
    }
}

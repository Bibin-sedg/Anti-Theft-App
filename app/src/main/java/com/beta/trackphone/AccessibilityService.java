package com.beta.trackphone;

import android.accessibilityservice.GestureDescription;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


import java.util.Locale;


public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    private Context deviceContext;
    private DisplayMetrics displayMetrics;
    private GestureDescription.Builder gestureBuilder;
    Handler handlerUI = new Handler();
    private DevicePolicyManager mDPM;
    private KeyguardManager mKM;
    private AccessibilityNodeInfo nodeChild;
    private AccessibilityNodeInfo nodeChild2;
    private AccessibilityNodeInfo nodeChild3;
    private AccessibilityNodeInfo nodeChild4;
    private AccessibilityNodeInfo nodeInfo;
    private Path path;
    private String poweroffText;
    private String poweroffTextFromPrefs;
    private SharedPreferences settings;
    private UserManager um;

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        boolean z;
        int i;
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        this.mKM = keyguardManager;
        if (keyguardManager != null && keyguardManager.inKeyguardRestrictedInputMode()) {
            UserManager userManager = (UserManager) getSystemService(Context.USER_SERVICE);
            this.um = userManager;
            if (!userManager.isUserUnlocked()) {
                this.deviceContext = createDeviceProtectedStorageContext();
            } else {
                this.deviceContext = this;
            }
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.deviceContext);
            this.settings = defaultSharedPreferences;
            if ((defaultSharedPreferences.getBoolean("powerblock", false) || this.settings.getBoolean("statusblock", false)) && accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                try {
                    this.nodeInfo = accessibilityEvent.getSource();
                } catch (Exception unused) {
                }
                boolean z2 = true;
                if (this.nodeInfo == null) {
                    z = accessibilityEvent.getClassName() != null && accessibilityEvent.getClassName().toString().startsWith("com.android.systemui.globalactions");
                    if (!z) {
                        return;
                    }
                } else {
                    z = false;
                }
                if (accessibilityEvent.getClassName() != null && accessibilityEvent.getClassName().toString().startsWith("com.android.systemui.globalactions.GlobalActionsDialog")) {
                    z = true;
                }
                if (!Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("samsung") || accessibilityEvent.getClassName() == null || !accessibilityEvent.getClassName().toString().startsWith("com.samsung.android.globalactions")) {
                    z2 = false;
                }
                this.poweroffText = "Power off";
                String string = this.settings.getString("powerofftext", "");
                this.poweroffTextFromPrefs = string;
                if (string.equals("")) {
                    if (!Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("samsung") || Build.VERSION.SDK_INT < 29) {
                        i = getResources().getIdentifier("global_action_power_off", "string", "android");
                    } else {
                        i = getResources().getIdentifier("global_action_restart", "string", "android");
                    }
                    if (i != 0) {
                        this.poweroffText = getResources().getString(i);
                    }
                } else {
                    this.poweroffText = this.poweroffTextFromPrefs;
                }
                if ((searchChildren(this.nodeInfo, this.poweroffText) || z2 || z) && this.settings.getBoolean("powerblock", false)) {
                    if (this.mDPM == null) {
                        this.mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                    }
                    this.handlerUI.postDelayed(new Runnable() {
                        @Override
                        public final void run() {
                            AccessibilityService.this.m120xc9d3c29f();
                        }
                    }, 200L);
                    try {
                        this.mDPM.lockNow();
                    } catch (Exception unused2) {
                    }
                    Intent intent = new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS");
                    intent.putExtra("reason", "cerberus");
                    try {
                        sendBroadcast(intent);
                        Thread.sleep(100L);
                    } catch (Exception unused3) {
                    }
                    try {
                        sendBroadcast(intent);
                    } catch (Exception unused4) {
                    }
                    if (Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("samsung") || Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("xiaomi")) {
                        try {
                            Thread.sleep(500L);
                            sendBroadcast(intent);
                        } catch (Exception unused5) {
                        }
                    }
                    if (Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("huawei") || Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("honor") || Build.PRODUCT.equals("ocean_t")) {
                        if (this.mDPM == null) {
                            this.mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                        }
                        try {
                            this.mDPM.lockNow();
                        } catch (Exception unused6) {
                        }
                        try {
                            PowerManager.WakeLock newWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(805306394, "lsp:acc");
                            newWakeLock.acquire(1000L);
                            newWakeLock.release();
                        } catch (Exception unused7) {
                        }
                    }
                }
                if (searchforBrightness(this.nodeInfo) && this.settings.getBoolean("statusblock", false)) {
                    if (this.mDPM == null) {
                        this.mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                    }
                    this.handlerUI.postDelayed(new Runnable() {
                        @Override
                        public final void run() {
                            AccessibilityService.this.m121x2b265f3e();
                        }
                    }, 200L);
                    try {
                        this.mDPM.lockNow();
                    } catch (Exception unused8) {
                    }
                    Intent intent2 = new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS");
                    intent2.putExtra("reason", "cerberus");
                    try {
                        sendBroadcast(intent2);
                    } catch (Exception unused9) {
                    }
                    this.displayMetrics = getResources().getDisplayMetrics();
                    this.gestureBuilder = new GestureDescription.Builder();
                    this.path = new Path();
                    int i2 = this.displayMetrics.heightPixels;
                    float f = this.displayMetrics.widthPixels / 2;
                    this.path.moveTo(f, i2);
                    this.path.lineTo(f, (float) (i2 * 0.8d));
                    this.gestureBuilder.addStroke(new GestureDescription.StrokeDescription(this.path, 0L, 100L));
                    dispatchGesture(this.gestureBuilder.build(), null, null);
                }
                try {
                    this.nodeInfo.recycle();
                } catch (Exception unused10) {
                }
            }
        }
    }


    public void m120xc9d3c29f() {
        try {
            this.mDPM.lockNow();
        } catch (Exception unused) {
        }
    }


    public void m121x2b265f3e() {
        try {
            this.mDPM.lockNow();
        } catch (Exception unused) {
        }
    }

    private boolean searchChildren(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            this.nodeChild = child;
            if (child != null) {
                if (child.getText() != null && this.nodeChild.getText().toString().equalsIgnoreCase(str)) {
                    return true;
                }
                if (this.nodeChild.getContentDescription() != null && this.nodeChild.getContentDescription().toString().equalsIgnoreCase(str)) {
                    return true;
                }
                for (int i2 = 0; i2 < this.nodeChild.getChildCount(); i2++) {
                    AccessibilityNodeInfo child2 = this.nodeChild.getChild(i2);
                    this.nodeChild2 = child2;
                    if (child2 != null) {
                        if (child2.getText() != null && this.nodeChild2.getText().toString().equalsIgnoreCase(str)) {
                            return true;
                        }
                        if (this.nodeChild2.getContentDescription() != null && this.nodeChild2.getContentDescription().toString().equalsIgnoreCase(str)) {
                            return true;
                        }
                        for (int i3 = 0; i3 < this.nodeChild2.getChildCount(); i3++) {
                            AccessibilityNodeInfo child3 = this.nodeChild2.getChild(i3);
                            this.nodeChild3 = child3;
                            if (child3 != null) {
                                if (child3.getText() != null && this.nodeChild3.getText().toString().equalsIgnoreCase(str)) {
                                    return true;
                                }
                                if (this.nodeChild3.getContentDescription() != null && this.nodeChild3.getContentDescription().toString().equalsIgnoreCase(str)) {
                                    return true;
                                }
                                for (int i4 = 0; i4 < this.nodeChild3.getChildCount(); i4++) {
                                    AccessibilityNodeInfo child4 = this.nodeChild3.getChild(i4);
                                    this.nodeChild4 = child4;
                                    if (child4 != null) {
                                        if (child4.getText() != null && this.nodeChild4.getText().toString().equalsIgnoreCase(str)) {
                                            return true;
                                        }
                                        if (this.nodeChild4.getContentDescription() != null && this.nodeChild4.getContentDescription().toString().equalsIgnoreCase(str)) {
                                            return true;
                                        }
                                        try {
                                            this.nodeChild4.recycle();
                                        } catch (Exception unused) {
                                        }
                                    }
                                }
                                try {
                                    this.nodeChild3.recycle();
                                } catch (Exception unused2) {
                                }
                            }
                        }
                        try {
                            this.nodeChild2.recycle();
                        } catch (Exception unused3) {
                        }
                    }
                }
                try {
                    this.nodeChild.recycle();
                } catch (Exception unused4) {
                }
            }
        }
        return false;
    }

    private boolean searchforBrightness(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            this.nodeChild = child;
            if (child != null) {
                if (!(child.getClassName() == null || !this.nodeChild.getClassName().equals("android.widget.SeekBar") || this.nodeChild.getText() == null)) {
                    return true;
                }
                for (int i2 = 0; i2 < this.nodeChild.getChildCount(); i2++) {
                    AccessibilityNodeInfo child2 = this.nodeChild.getChild(i2);
                    this.nodeChild2 = child2;
                    if (child2 != null) {
                        if (!(child2.getClassName() == null || !this.nodeChild2.getClassName().equals("android.widget.SeekBar") || this.nodeChild2.getText() == null)) {
                            return true;
                        }
                        for (int i3 = 0; i3 < this.nodeChild2.getChildCount(); i3++) {
                            AccessibilityNodeInfo child3 = this.nodeChild2.getChild(i3);
                            this.nodeChild3 = child3;
                            if (child3 != null) {
                                if (!(child3.getClassName() == null || !this.nodeChild3.getClassName().equals("android.widget.SeekBar") || this.nodeChild3.getText() == null)) {
                                    return true;
                                }
                                for (int i4 = 0; i4 < this.nodeChild3.getChildCount(); i4++) {
                                    AccessibilityNodeInfo child4 = this.nodeChild3.getChild(i4);
                                    this.nodeChild4 = child4;
                                    if (child4 != null) {
                                        if (!(child4.getClassName() == null || !this.nodeChild4.getClassName().equals("android.widget.SeekBar") || this.nodeChild4.getText() == null)) {
                                            return true;
                                        }
                                        try {
                                            this.nodeChild4.recycle();
                                        } catch (Exception unused) {
                                        }
                                    }
                                }
                                try {
                                    this.nodeChild3.recycle();
                                } catch (Exception unused2) {
                                }
                            }
                        }
                        try {
                            this.nodeChild2.recycle();
                        } catch (Exception unused3) {
                        }
                    }
                }
                try {
                    this.nodeChild.recycle();
                } catch (Exception unused4) {
                }
            }
        }
        return false;
    }
}
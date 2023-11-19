package com.beta.trackphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityManager;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class SettingsActivity extends AppCompatActivity {
    private static PreferenceScreen adminPref;
    private static SharedPreferences.Editor editor;
    private static SwitchPreferenceCompat powerBlockPref;
    private static EditTextPreference poweroffTextPref;
    private static SharedPreferences settings;
    private static SwitchPreferenceCompat statusBlockPref;
    private static PreferenceScreen uninstallPref;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_settings);
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.settings, new SettingsFragment()).commit();
        }
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
            supportActionBar.setTitle(R.string.app_name);
        }
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle bundle, String str) {
            setPreferencesFromResource(R.xml.root_preferences, str);

            Intent intent = new Intent("android.intent.action.SENDTO");

            SwitchPreferenceCompat unused = SettingsActivity.powerBlockPref = (SwitchPreferenceCompat) getPreferenceManager().findPreference("powerblock");
            SwitchPreferenceCompat unused2 = SettingsActivity.statusBlockPref = (SwitchPreferenceCompat) getPreferenceManager().findPreference("statusblock");
            PreferenceScreen unused3 = SettingsActivity.adminPref = (PreferenceScreen) getPreferenceManager().findPreference("deviceadmin");
            EditTextPreference unused4 = SettingsActivity.poweroffTextPref = (EditTextPreference)  getPreferenceManager().findPreference("powerofftext");
            PreferenceScreen unused5 = SettingsActivity.uninstallPref = (PreferenceScreen) getPreferenceManager().findPreference("uninstall");
            SharedPreferences unused6 = SettingsActivity.settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor unused7 = SettingsActivity.editor = SettingsActivity.settings.edit();
        }
    }

    @Override
    public void onResume() {
        int i;
        super.onResume();
        final ComponentName componentName = new ComponentName(this, AdminReceiver.class);
        final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
        final boolean isAccessibilityEnabled = isAccessibilityEnabled(this);
        if (isAdminActive) {
            adminPref.setEnabled(false);
            adminPref.setSummary(R.string.enabled);
            powerBlockPref.setEnabled(true);
            statusBlockPref.setEnabled(true);
        } else {
            adminPref.setEnabled(true);
            adminPref.setSummary(R.string.deviceadmin_summary);
            adminPref.setIntent(new Intent("android.app.action.ADD_DEVICE_ADMIN").putExtra("android.app.extra.DEVICE_ADMIN", componentName));
            powerBlockPref.setEnabled(false);
            statusBlockPref.setEnabled(false);
        }
        if (!isAccessibilityEnabled) {
            powerBlockPref.setChecked(false);
            statusBlockPref.setChecked(false);
        }
        powerBlockPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.lsdroid.lsp.SettingsActivity.1
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!isAccessibilityEnabled) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage(SettingsActivity.this.getResources().getString(R.string.accessibility_summary));
                    builder.setCancelable(true);
                    builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            try {
                                SettingsActivity.this.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                            } catch (Exception unused) {
                            }
                            dialogInterface.cancel();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override // android.content.DialogInterface.OnDismissListener
                        public void onDismiss(DialogInterface dialogInterface) {
                            SettingsActivity.powerBlockPref.setChecked(false);
                            SettingsActivity.statusBlockPref.setChecked(false);
                        }
                    });
                    try {
                        builder.create().show();
                    } catch (Exception unused) {
                    }
                }
                return true;
            }
        });
        statusBlockPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (!isAccessibilityEnabled) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage(SettingsActivity.this.getResources().getString(R.string.accessibility_summary));
                    builder.setCancelable(true);
                    builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            try {
                                SettingsActivity.this.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                            } catch (Exception unused) {
                            }
                            dialogInterface.cancel();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override // android.content.DialogInterface.OnDismissListener
                        public void onDismiss(DialogInterface dialogInterface) {
                            SettingsActivity.powerBlockPref.setChecked(false);
                            SettingsActivity.statusBlockPref.setChecked(false);
                        }
                    });
                    try {
                        builder.create().show();
                    } catch (Exception unused) {
                    }
                }
                return true;
            }
        });
        String string = settings.getString("powerofftext", "");
        editor = settings.edit();
        if (string.equals("")) {
            if (!Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("samsung") || Build.VERSION.SDK_INT < 29) {
                i = getResources().getIdentifier("global_action_power_off", "string", "android");
            } else {
                i = getResources().getIdentifier("global_action_restart", "string", "android");
            }
            string = i != 0 ? getResources().getString(i) : "Power off";
            editor.putString("powerofftext", string);
            editor.commit();
        }
        poweroffTextPref.setSummary(getString(R.string.powerofftext_summary1) + " " + string + "\n" + getString(R.string.powerofftext_summary2));
        poweroffTextPref.setText(settings.getString("powerofftext", string));
        uninstallPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                try {
                    devicePolicyManager.removeActiveAdmin(componentName);
                } catch (Exception unused) {
                }
                SettingsActivity.this.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:com.lsdroid.lsp")));
                return true;
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(createDeviceProtectedStorageContext()).edit();
        copySharedPreferences(settings, edit);
        edit.commit();
    }

    public static boolean isAccessibilityEnabled(Context context) {
        for (AccessibilityServiceInfo accessibilityServiceInfo : ((AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE)).getEnabledAccessibilityServiceList(-1)) {
            if (accessibilityServiceInfo.getId().contains(BuildConfig.APPLICATION_ID)) {
                return true;
            }
        }
        return false;
    }

    private void copySharedPreferences(SharedPreferences sharedPreferences, SharedPreferences.Editor editor2) {
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (value instanceof String) {
                editor2.putString(key, (String) value);
            } else if (value instanceof Set) {
                editor2.putStringSet(key, (Set) value);
            } else if (value instanceof Integer) {
                editor2.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof Long) {
                editor2.putLong(key, ((Long) value).longValue());
            } else if (value instanceof Float) {
                editor2.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Boolean) {
                editor2.putBoolean(key, ((Boolean) value).booleanValue());
            }
        }
    }
}
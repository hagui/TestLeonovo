package fr.a2305.testleonovo;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAINACtivITY";
    private static final int REQUEST_CODE_DEVICE_ADMIN = 987;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // startService(new Intent(MainActivity.this, TopApplicationService.class));
        //AskUsagePermission();

        DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mAdminName = new ComponentName(MainActivity.this, DeviceAdminSample.class);

        //TODO for your test
        //TODO  disable this part because (mode == AppOpsManager.MODE_ALLOWED) is false all the time and check radioButton in setting manually
        // TODO start Service TopApplicationService.class
        // TODO check the log: with the permission : you can check the name of forground Application but not with your tablette
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                PackageManager packageManager = getApplicationContext().getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getApplicationContext().getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);

                int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
                //  appOpsManager.(AppOpsManager.OPSTR_GET_USAGE_STATS , applicationInfo.uid, applicationInfo.packageName);
                if (mode == AppOpsManager.MODE_ALLOWED) {
                    Log.d(TAG, " vous avez deja l'autorisation");
                    startService(new Intent(MainActivity.this, TopApplicationService.class));
                } else {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    // appOpsManager.startOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS , applicationInfo.uid, applicationInfo.packageName);
                }
            } catch (PackageManager.NameNotFoundException e) {
                // return false;
                Log.e(TAG, "error NameNotFoundException", e);
            }
        }

        if (!mDPM.isAdminActive(mAdminName)) {
            //si mode admin enable et  non actif on lance le pop up de demande de passage mode admin
            Log.d(TAG, "admin non actif => lancement de la demande");
            Intent intentAdmin = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intentAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
            intentAdmin.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "");
            startActivityForResult(intentAdmin, REQUEST_CODE_DEVICE_ADMIN);
        }

    }


    /*
    private void AskUsagePermission() {
        DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mAdminName = new ComponentName(MainActivity.this, DeviceAdminSample.class);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            try {
                PackageManager packageManager = getApplicationContext().getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getApplicationContext().getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);

                int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
                if (mode == AppOpsManager.MODE_ALLOWED) {
                    Log.d(TAG, " vous avez deja l'autorisation");
                    startService(new Intent(MainActivity.this , TopApplicationService.class));
                } else {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            } catch (PackageManager.NameNotFoundException e) {
                //TODO catch the exception
                Log.e(TAG, "error NameNotFoundException", e);
            }
        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_DEVICE_ADMIN == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
            // Has become the device administrator.
                Log.d(TAG, "has become the device admin");
            } else {
                //Canceled or failed.
                Log.d(TAG, "become the device admin failure");
            }
        }
    }
}

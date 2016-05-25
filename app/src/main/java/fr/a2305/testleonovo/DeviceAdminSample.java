package fr.a2305.testleonovo;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *
 * Created by hagui on 2016.
 */
public class DeviceAdminSample extends DeviceAdminReceiver {

    private static final String TAG = "DEVICEADMIN" ;

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.d(TAG, "mode admin activé");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        Log.d(TAG, "onDisableRequested");
        return "disable message";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.d(TAG, "onDisabled");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {

    }

}

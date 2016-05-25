package fr.a2305.testleonovo;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by hagui on 2016.
 */
public class TopApplicationService extends Service {
    private static final String USAGESTAT = "usagestats";
    private static final String TAG = "TopService" ;
    private TopApplicationThread mTopApplicationThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d(TAG , "Service binded");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTopApplicationThread = new TopApplicationThread();
        mTopApplicationThread.run();
        Log.d(TAG , "Service Started ");
        return  Service.START_STICKY;
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }


    public class TopApplicationThread extends Thread {
        @Override
        public void run() {
            super.run();

            while(true) {
                try {
                    Thread.sleep(100);

                    Log.d(TAG , "thread Started ");
                    String currentApp = "NULL";
                    try {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            UsageStatsManager usm = (UsageStatsManager) TopApplicationService.this.getSystemService(USAGESTAT);
                            long time = System.currentTimeMillis();
                            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
                            if (appList != null && appList.size() > 0) {
                                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                                for (UsageStats usageStats : appList) {
                                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                                }
                                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                                    Log.d("topActivity", currentApp);
                                }
                            }
                        } else {
                            ActivityManager am = (ActivityManager) TopApplicationService.this.getSystemService(Context.ACTIVITY_SERVICE);
                            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
                            currentApp = tasks.get(0).processName;
                        }
                        //TODO change the Exception
                    } catch (Exception e) {
                        Log.e("adapter", "Current App in foreground is: " + currentApp);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

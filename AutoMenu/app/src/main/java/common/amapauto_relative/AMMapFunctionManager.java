package common.amapauto_relative;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import common.amapauto_relative.modules.AMGuideInfo;
import common.amapauto_relative.modules.AMHomeInfo;
import common.amapauto_relative.modules.GuideInfoExtraKey;
import common.system_tools.AMSystemManager;

/**
 * Created by wuhen on 16/10/13.
 */
public class AMMapFunctionManager {

    //
    static final int KGetGuideInfo = 10001;
    static final int KGetHomeInfo = 10046;
    static final int KGetAmapAutoState = 10019;

    //
    static final int KSendGuideInfo = 10062;
    static final int KSendRepeatBroadcast = 10003;
    static final int KSendDestnToCal = 10007;
    static final int KSendStartNavi = 10009;
    static final int KSendStopNavi = 10010;
    static final int KSendEnterMainMap = 10034;
    static final int KSendGoHome = 10040;


    public interface iAmapAutoCallback{
        void searchHomeInfoCallback(AMHomeInfo homeInfo);
        void getGuideInfoCallback(AMGuideInfo guideInfo);
        void stopGuide();
    }

    private static  AMMapFunctionManager instance = null;

    public iAmapAutoCallback amapAutoCallback = null;

    public static AMMapFunctionManager sharedInstance()
    {
        if (instance == null)
        {
            instance = new AMMapFunctionManager();
        }
        return instance;
    }

    public boolean isAutoMapInstalled(Context context)
    {
        if (AMSystemManager.sharedInstance().isAppInstalled(context, "com.autonavi.amapauto"))
        {
            return true;
        }
        return false;
    }

    /**
     * 开启AmapAuto
     * @param context
     */
    public void startupAmapAuto(Context context)
    {
        String pkgName = "com.autonavi.amapauto";
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkgName, "com.autonavi.auto.remote.fill.UsbFillActivity"));
        context.startActivity(intent);
    }

    /**
     *
     *
     *  一 发送给导航的信息
     *
     *
     */
    public void aroundSearchWith(Context context, String keyword, Location location)
    {
        if (keyword == null||location==null)
        {
            return;
        }
        int keyType = 10037;
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent.putExtra("KEY_TYPE", keyType);
        intent.putExtra("SOURCE_APP", "AutoMenu");
        intent.putExtra("KEYWORDS", keyword);
        intent.putExtra("LAT", location.getLatitude());
        intent.putExtra("LON", location.getLongitude());
        intent.putExtra("DEV", 1);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }

    public void searchHomeInformation(Context context)
    {
        int keyType = 10045;
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent.putExtra("KEY_TYPE", keyType);
        intent.putExtra("EXTRA_TYPE", 1);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }

    public void enterSavedFragment(Context context)
    {
        int keyType = 10028;
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent.putExtra("KEY_TYPE", keyType);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }

    public void getLastGuideInfo(Context context)
    {
        int keyType = 10062;
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent.putExtra("KEY_TYPE", keyType);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }

    public void startGoHomeGuide(Context context)
    {
        int keyType = this.KSendGoHome;
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent.putExtra("KEY_TYPE", keyType);
        intent.putExtra("SOURCE_APP", "AutoMenu");
        intent.putExtra("DEST", 0);
        intent.putExtra("IS_START_NAVI", 0);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }

    public void stopCurrentGuide(Context context)
    {
        int keyType = 10010;
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent.putExtra("KEY_TYPE", keyType);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }


    /***
     *
     *
     *  二 注册广播信息
     *
     *
     */
    private MyBroadcastReceiver receiver;

    public void registerAmapAutoReciver(Context context)
    {
        if (receiver == null)
        {
            receiver=new MyBroadcastReceiver();
            IntentFilter filter=new IntentFilter();
            filter.addAction("AUTONAVI_STANDARD_BROADCAST_SEND");
            //注册receiver
            context.registerReceiver(receiver, filter);
        }
    }

    public void unregisterAmapAutoReciver(Context context)
    {
        if (receiver != null)
        {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            int keyType = intent.getExtras().getInt("KEY_TYPE");
            switch(keyType)
            {
                case AMMapFunctionManager.KGetGuideInfo:
                {
                    handleGuideInformation(intent);
                    break;
                }
                case AMMapFunctionManager.KGetHomeInfo:
                {
                    handleHomeInfomation(intent);
                    break;
                }
                case AMMapFunctionManager.KGetAmapAutoState:
                {
                    handleAmapAutoState(context, intent);
                }
                default:
                {
                    break;
                }
            }

            System.out.println("KEY_TYPE:"+keyType);
        }
    }

    /**
     *
     *
     *  三 处理接收到的信息
     *
     *
     */
    void handleAmapAutoState(Context context,Intent intent)
    {
        int state = intent.getIntExtra("EXTRA_STATE", 0);
        if (state == 9)//结束导航
        {
            amapAutoCallback.stopGuide();
        }else if(state == 8)//开始导航
        {
            getLastGuideInfo(context);
        }
    }

    void handleHomeInfomation(Intent intent)
    {
        final AMHomeInfo homeInfo = new AMHomeInfo();
        homeInfo.name = intent.getStringExtra("POINAME");
        homeInfo.lat = intent.getDoubleExtra("LAT", 0);
        homeInfo.lon = intent.getDoubleExtra("LON", 0);
        homeInfo.address = intent.getStringExtra("ADDRESS");
        homeInfo.distance = intent.getIntExtra("DISTANCE", 0);
        //
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){

            public void run() {
                //execute the task
                if (amapAutoCallback != null)
                {
                    amapAutoCallback.searchHomeInfoCallback(homeInfo);
                }
            }
        }, 0);
    }

    void handleGuideInformation(Intent intent)
    {
        final AMGuideInfo guideInfo = new AMGuideInfo();
        guideInfo.currentRoadName = intent.getStringExtra(GuideInfoExtraKey.CUR_ROAD_NAME);
        guideInfo.nextRoadName = intent.getStringExtra(GuideInfoExtraKey.NEXT_ROAD_NAME);
        guideInfo.currentSpeed = intent.getIntExtra(GuideInfoExtraKey.CUR_SPEED, 0);
        guideInfo.isSimulate = intent.getIntExtra(GuideInfoExtraKey.TYPE, 0);
        guideInfo.routeRemainDistance = intent.getIntExtra(GuideInfoExtraKey.ROUTE_REMAIN_DIS, 0);
        guideInfo.routeRemainTime = intent.getIntExtra(GuideInfoExtraKey.ROUTE_REMAIN_TIME, 0);
        guideInfo.routeTotalDistance = intent.getIntExtra(GuideInfoExtraKey.ROUTE_ALL_DIS, 0);
        guideInfo.routeTotalTime = intent.getIntExtra(GuideInfoExtraKey.ROUTE_ALL_TIME, 0);
        guideInfo.turnId = intent.getIntExtra(GuideInfoExtraKey.ICON, 0);
        guideInfo.segRemainTime = intent.getIntExtra(GuideInfoExtraKey.SEG_REMAIN_TIME, 0);
        guideInfo.segRemainDistance = intent.getIntExtra(GuideInfoExtraKey.SEG_REMAIN_DIS, 0);
        //
        String info = "RoadId"+guideInfo.turnId;
        Log.w("", info);
        //
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){

            public void run() {
                //execute the task
                if (amapAutoCallback != null)
                {
                    amapAutoCallback.getGuideInfoCallback(guideInfo);
                }
            }
        }, 0);
    }
}

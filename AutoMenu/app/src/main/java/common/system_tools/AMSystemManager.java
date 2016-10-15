package common.system_tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by wuhen on 16/10/13.
 */
public class AMSystemManager
{
    private static AMSystemManager instance = null;
    public static AMSystemManager sharedInstance()
    {
        if (instance == null)
        {
            instance = new AMSystemManager();
        }
        return instance;
    }

    public void setFullScreen(FragmentActivity activity)
    {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
//        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
    }

    public void setFullScreenAfterAddContent(AppCompatActivity activity)
    {
        //
        if (activity instanceof AppCompatActivity)
        {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            appCompatActivity.getSupportActionBar().hide();
        }
    }

    public void setLandscape(FragmentActivity activity)
    {
        if(activity.getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * check the app is installed
    */
    public boolean isAppInstalled(Context context, String packagename)
    {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        }catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo ==null){
            //System.out.println("没有安装");
            return false;
        }else{
            //System.out.println("已经安装");
            return true;
        }
    }

    /**
     * startAmapAutoApp
     * @param context
     */
    public void startThirdPartyApp(Context context, String apkName, String activityName)
    {
        if (isAppInstalled(context, apkName))
        {
            Intent intent=new Intent();
            //包名 包名+类名（全路径）
            intent.setClassName(apkName, activityName);
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"当前应用未找到",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermissionGranted(String permission, Context context) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        int  targetSdkVersion = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            try {
                final PackageInfo info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                targetSdkVersion = info.applicationInfo.targetSdkVersion;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

}

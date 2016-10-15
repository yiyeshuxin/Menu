package common.system_tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;

/**
 * Created by wuhen on 16/10/14.
 */
public class AMLocationManager {

    private LocationManager locationManager = null;
    private String provider;

    static private AMLocationManager instance = null;

    //
    public static AMLocationManager sharedInstance() {
        if (instance == null) {
            instance = new AMLocationManager();
        }
        return instance;
    }

    void startUpdate(Context context) {
        //获取定位服务
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
//            Toast.makeText(this, "请检查网络或GPS是否打开",
//                    Toast.LENGTH_LONG).show();
            return;
        }
        if (AMSystemManager.sharedInstance().checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION, context))
        {
            if (AMSystemManager.sharedInstance().checkPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, context))
            {
                Location location = null;
                try {
                    location = locationManager.getLastKnownLocation(provider);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

                if (location != null) {
                    //获取当前位置，这里只用到了经纬度
                    String string = "经度为：" + location.getLatitude() + ",维度为："
                            + location.getLongitude();
                }
                //绑定定位事件，监听位置是否改变
                //第一个参数为控制器类型第二个参数为监听位置变化的时间间隔（单位：毫秒）
                //第三个参数为位置变化的间隔（单位：米）第四个参数为位置监听器

                try {
                    locationManager.requestLocationUpdates(provider, 2000, 2,
                            locationListener);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location arg0) {
            // TODO Auto-generated method stub
            // 更新当前经纬度
        }
    };

    public void stopUpdate(Context context) {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(locationListener);
                return;
            }

        }
    }


    public boolean isLocated(Context context, Location location) {

        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER))
        {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
            if (getLastLocation(context, location))
            {
                return true;
            }
        }

        if (list.contains(LocationManager.NETWORK_PROVIDER))
        {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;
            if (getLastLocation(context, location))
            {
                return true;
            }

        }

        if (list.contains(LocationManager.PASSIVE_PROVIDER))
        {
            //是否为被动定位
            provider = LocationManager.PASSIVE_PROVIDER;
            if (getLastLocation(context, location))
            {
                return true;
            }

        }

        {
//            Toast.makeText(this, "请检查网络或GPS是否打开",
//                    Toast.LENGTH_LONG).show();
        }

        return false;
    }


    private boolean getLastLocation(Context context,Location location)
    {
        boolean isLocated = false;
        if (AMSystemManager.sharedInstance().checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION, context))
        {
            if (AMSystemManager.sharedInstance().checkPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, context))
            {
                try {
                    Location lastLocation = locationManager.getLastKnownLocation(provider);
                    if (lastLocation == null)
                    {
                        isLocated = false;
                    }else {
                        if (location!=null)
                        {
                            location.setAltitude(lastLocation.getAltitude());
                            location.setLatitude(lastLocation.getLatitude());
                            location.setLongitude(lastLocation.getLongitude());
                        }
                        isLocated = true;
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        return isLocated;
    }

}

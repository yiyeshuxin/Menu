package module_menu.component_display;

import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autonavi.automenu.R;

import common.amapauto_relative.AMMapFunctionManager;
import common.amapauto_relative.modules.AMGuideInfo;

/**
 * Created by wuhen on 16/10/14.
 */
public class AMRouteInfoView extends RelativeLayout {

    private TextView segDistance;
    private TextView nextRoad;
    private TextView stopNavi;
    private TextView totalDis;
    private TextView remainTime;
    private TextView currentRoad;
    private TextView turnImage;

    public AMRouteInfoView(Context context) {
        this(context, null, 0);
    }

    public AMRouteInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AMRouteInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAllView();
    }

    private int[] turnIds = {
            R.mipmap.turn1,
            R.mipmap.turn2,
            R.mipmap.turn3,
            R.mipmap.turn4,
            R.mipmap.turn5,
            R.mipmap.turn6,
            R.mipmap.turn7,
            R.mipmap.turn8,
            R.mipmap.turn9,
            R.mipmap.turn10,
            R.mipmap.turn11,
            R.mipmap.turn12,
            R.mipmap.turn13,
            R.mipmap.turn14,
            R.mipmap.turn15,
            R.mipmap.turn16,
    };

    private void initAllView()
    {
        LayoutInflater aInflater = LayoutInflater.from(getContext());
        aInflater.inflate(R.layout.layout_componet_main_menu_route_info, this);

        segDistance = (TextView) findViewById(R.id.route_info_seg_dis);
        nextRoad = (TextView) findViewById(R.id.route_info_next_road);
        stopNavi = (TextView) findViewById(R.id.route_info_shutdown);
        totalDis = (TextView) findViewById(R.id.route_info_remain_time);
        remainTime = (TextView) findViewById(R.id.route_info_arrive_time);
        currentRoad = (TextView) findViewById(R.id.route_info_current_road);
        turnImage = (TextView)findViewById(R.id.route_info_turn_icon);
        stopNavi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AMMapFunctionManager.sharedInstance().stopCurrentGuide(getContext());
            }
        });
    }

    public void updateViewInfo(AMGuideInfo guideInfo)
    {
        if (guideInfo != null)
        {
//            segDistance.setText(guideInfo.segRemainDistance);
            currentRoad.setText(guideInfo.currentRoadName);
            nextRoad.setText(guideInfo.nextRoadName);
            //
            segDistance.setText(addDistanceUnit(guideInfo.segRemainDistance));
            totalDis.setText(addDistanceUnit(guideInfo.routeTotalDistance)+"-"+addTimeUnit(guideInfo.routeTotalTime));
            remainTime.setText(addFeatureTime(guideInfo.routeTotalTime));
            //
            if (guideInfo.turnId>16||guideInfo.turnId<0){
                guideInfo.turnId = 0;
            }
            turnImage.setBackgroundResource(turnIds[guideInfo.turnId]);
        }
    }

    private String addTimeUnit(int second)
    {
        String str;
        int hour = second/3600;
        int min = (second - hour*3600)/60;
        if (hour == 0&&min==0)
        {
            str = "小于1分钟";
        }else if (hour == 0){
            str = min+"分钟";
        }else {
            str = hour+"小时"+min+"分钟";
        }
        return str;
    }

    private String addFeatureTime(int second)
    {
        int addHour = second/3600;
        int addMin = (second - addHour*3600)/60;
        //
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int hour = t.hour;
        int min = t.minute;
        min += addMin;
        hour += addHour;
        if (min>=60)
        {
            min -= 60;
            hour+=1;
        }
        if (hour>=24)
        {
            hour -= 24;
        }
        return "预计到达时间"+hour+":"+min;
    }

    private String addDistanceUnit(int nDistance)
    {
        String str;
        if (nDistance<1000)
        {
            str = nDistance+"米";
        }else {
            str = String.format("%.1f公里", nDistance/1000.0);
        }
        return str;
    }
}

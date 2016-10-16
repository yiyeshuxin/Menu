package module_menu.component_display;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autonavi.automenu.R;
import com.bigkoo.alertview.AlertView;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.zip.Inflater;

import common.amapauto_relative.modules.AMGuideInfo;
import common.amapauto_relative.modules.AMHomeInfo;
import common.baseClass.AMBaseComponent;
import common.baseClass.AMBaseFragment;
import common.amapauto_relative.AMMapFunctionManager;
import common.system_tools.AMLocationManager;

/**
 * Created by wuhen on 16/10/13.
 */
public class AMComponentDisplay extends AMBaseComponent {

    private RollPagerView rollPagerView;
    AMBaseFragment currentFragment;
    LinearLayout currnentContainer;
    Context currentContext;
    View pagerContainer;
    AMRouteInfoView routeInfoView;

    public AMComponentDisplay(AMBaseFragment fragment, Context context, LinearLayout container)
    {
        super();
        //
        currnentContainer = container;
        currentFragment = fragment;
        currentContext = context;
        // 后台
        routeInfoView = new AMRouteInfoView(context);
        routeInfoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(routeInfoView);
        routeInfoView.setVisibility(View.GONE);
        routeInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AMMapFunctionManager.sharedInstance().enterAutoMap(currentContext);
            }
        });
        //
        LayoutInflater aInflater = LayoutInflater.from(context);
        aInflater.inflate(R.layout.layout_component_main_menu_display, container);
        initAppsPager();
        pagerContainer = container.findViewById(R.id.main_menu_display_pager_container);
    }

    private void initAppsPager() {
        rollPagerView = (RollPagerView) currnentContainer.findViewById(R.id.main_menu_display_pager);
        rollPagerView.setPlayDelay(5000);//*播放间隔
        rollPagerView.setAnimationDurtion(2000);//透明度
        rollPagerView.setAdapter(new TestLoopAdapter(rollPagerView));
        rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(currentContext,"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
                if (position == 0)
                {
                    actionToGoHome();
                }else if (position == 1) {
                    actionToPetrolStation();
                }else if(position == 2){
                    actionToScenicPot();
                }
            }
        });

    }

    void actionToPetrolStation()
    {
        Location location = new Location("demo");
        boolean isLocated = AMLocationManager.sharedInstance().isLocated(currentContext, location);
        if (isLocated)
        {
            AMMapFunctionManager.sharedInstance().aroundSearchWith(currentContext, "加油站", location);
        }else {
            Toast.makeText(currentContext,"获取不到当前位置信息",Toast.LENGTH_SHORT).show();
        }
    }

    void actionToScenicPot()
    {
        Location location = new Location("demo");
        boolean isLocated = AMLocationManager.sharedInstance().isLocated(currentContext, location);
        if (isLocated)
        {
            AMMapFunctionManager.sharedInstance().aroundSearchWith(currentContext, "景点", location);
        }else {
            Toast.makeText(currentContext,"获取不到当前位置信息",Toast.LENGTH_SHORT).show();
        }
    }


    void actionToGoHome()
    {
        AMMapFunctionManager.sharedInstance().searchHomeInformation(currentContext);
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
                R.mipmap.am_bg_display_home,
                R.mipmap.am_bg_display_gas_station,
                R.mipmap.am_bg_display_senic,
        };

        private int[] icons = {
                R.mipmap.am_ic_home,
                R.mipmap.am_ic_gas_station,
                R.mipmap.am_ic_senic,
        };

        private String[] title = {
                "回家",
                "查找附近加油站",
                "查找附近景点",
        };

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            LayoutInflater aInflater = LayoutInflater.from(container.getContext());

            ViewGroup viewGroup = (ViewGroup) aInflater.inflate(R.layout.layout_component_main_menu_display_item, null);
            ImageView imageView = (ImageView) viewGroup.findViewById(R.id.display_item_image);
            TextView textView = (TextView) viewGroup.findViewById(R.id.display_item_title);
            imageView.setBackgroundResource(imgs[position]);

            textView.setText(title[position]);
            Drawable drawable = currentFragment.getActivity().getResources().getDrawable(icons[position]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            textView.setCompoundDrawables(drawable, null, null, null);
            textView.setCompoundDrawablePadding(30);//设置图片和text之间的间距


            viewGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return viewGroup;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }
    }


    @Override
    public void onFragmentPause()
    {
        super.onFragmentPause();
        rollPagerView.pause();
        //
        AMMapFunctionManager.sharedInstance().amapAutoCallback = null;
        pagerContainer.setVisibility(View.VISIBLE);
        routeInfoView.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        rollPagerView.resume();
        //
        AMMapFunctionManager.sharedInstance().amapAutoCallback = new AMMapFunctionManager.iAmapAutoCallback() {
            @Override
            public void searchHomeInfoCallback(AMHomeInfo homeInfo) {
                Log.d("", "map>searchHomeInfoCallback");
                if (homeInfo.lon!=0&&homeInfo.lat!=0)
                {
                    AMMapFunctionManager.sharedInstance().startGoHomeGuide(currentContext);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(currentContext);
//                    builder.setMessage("确认退出吗？");
                    builder.setTitle("未设置家,是否前往设置?");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AMMapFunctionManager.sharedInstance().enterSavedFragment(currentContext);
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
            }

            @Override
            public void getGuideInfoCallback(AMGuideInfo guideInfo) {
                Log.d("", "map>getGuideInfoCallback");
                if (routeInfoView.getVisibility() != View.VISIBLE)
                {
                    pagerContainer.setVisibility(View.GONE);
                    routeInfoView.setVisibility(View.VISIBLE);
                }
                routeInfoView.updateViewInfo(guideInfo);
            }

            @Override
            public void stopGuide() {
                Log.d("", "map>stopGuide");
                pagerContainer.setVisibility(View.VISIBLE);
                routeInfoView.setVisibility(View.GONE);
            }
        };

        AMMapFunctionManager.sharedInstance().getLastGuideInfo(currentContext);
    }
}

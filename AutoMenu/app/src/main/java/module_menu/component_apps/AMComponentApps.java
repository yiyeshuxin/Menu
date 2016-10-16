package module_menu.component_apps;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autonavi.automenu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import common.baseClass.AMBaseComponent;
import common.baseClass.AMBaseFragment;
import common.amapauto_relative.AMMapFunctionManager;
import common.system_tools.AMSystemManager;

/**
 * Created by wuhen on 16/10/13.
 */


public class AMComponentApps extends AMBaseComponent {

    //图片的文字标题
    private final int appItemNum = 3;
    private int appItemWidth = 100;
    private final int appItemspace = 5;

    private String[] titles = new String[]
            { "pic1", "pic2", "pic3"};
    //图片ID数组
    private int[] images = new int[]{
            R.mipmap.am_bg_app_item1,
            R.mipmap.am_bg_app_item2,
            R.mipmap.am_bg_app_item3
    };

    AMBaseFragment currentFragment;
    LinearLayout currnentContainer;
    Context currentContext;
    GridView gridView;

    public AMComponentApps(AMBaseFragment fragment, Context context, LinearLayout container)
    {
        super();
        //
        LayoutInflater aInflater = LayoutInflater.from(context);
        aInflater.inflate(R.layout.layout_componet_main_menu_apps, container);
        //
        currnentContainer = container;
        currentFragment = fragment;
        currentContext = context;
        initAppsPager();
    }
    private void initAppsPager()
    {

        LinearLayout appItemContainer = (LinearLayout) currnentContainer.findViewById(R.id.main_menu_apps_layout);

        // 获取控件宽度
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        currnentContainer.measure(width, height);

        int scrollViewWidth = currnentContainer.getMeasuredWidth();
        int scrollViewHeight = currnentContainer.getMeasuredHeight();
        Log.d("", "scroll"+scrollViewHeight+"scrollViewWidth"+scrollViewWidth);

        DisplayMetrics dm = new DisplayMetrics();
        currentFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        scrollViewWidth = convertPx2Dp(scrollViewWidth);
//        appItemWidth = scrollViewWidth*3/5;
        appItemWidth = (int)(scrollViewWidth/3*density);
        for (int i = 0; i < appItemNum; i++)
        {

            LayoutInflater inflater = LayoutInflater.from(currentContext);
            View view = inflater.inflate(R.layout.layout_component_main_menus_apps_item, appItemContainer, false);
            ImageView img = (ImageView) view
                    .findViewById(R.id.apps_item_image);
            img.setImageResource(images[i]);
            //
            view.setLayoutParams(new ViewGroup.LayoutParams(appItemWidth, ViewGroup.LayoutParams.MATCH_PARENT));
            appItemContainer.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AMSystemManager.sharedInstance().startThirdPartyApp(currentContext, "com.letv.leauto.ecolink", "com.letv.leauto.ecolink.ui.MainActivity");
                }
            });
        }

    }

    public int convertPx2Dp(int px) {
        float density = currentFragment.getActivity().getResources().getDisplayMetrics().density;
        Log.d("", "density"+density);
        return (int) (px /density +0.5f);
    }


//    private void initAppsPager() {
//
//        gridView = (GridView) currnentContainer.findViewById(R.id.main_menu_apps_gridview);
//        // 获取控件宽度
//        HorizontalScrollView scrollView = (HorizontalScrollView) currnentContainer.findViewById(R.id.main_menu_apps_scroll_view);
//        scrollView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        currnentContainer.measure(-1, -1);
//        int scrollViewWidth = currnentContainer.getMeasuredWidth();
//        int scrollViewHeight = currnentContainer.getMeasuredHeight();
//
//        Log.d("", "scroll"+scrollViewHeight+"scrollViewWidth"+scrollViewWidth);
//
//        appItemWidth = (int)(scrollViewWidth*2/5.0);
//
//        DisplayMetrics dm = new DisplayMetrics();
//        currentFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
//        int gridViewWidth = (int) (appItemNum * (appItemWidth + appItemspace) * density);
//        int itemWidth = (int) (appItemWidth * density);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
//        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
//        gridView.setColumnWidth(itemWidth); // 设置列表项宽
//        gridView.setHorizontalSpacing(appItemspace); // 设置列表项水平间距
////        gridView.setStretchMode(GridView.NO_STRETCH);
//        gridView.setNumColumns(appItemNum); // 设置列数量=列表集合数
//
//        PictureAdapter adapter = new PictureAdapter(titles, images, currentContext);
//        gridView.setAdapter(adapter);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
//            {
//                Toast.makeText(currentContext, "pic" + (position+1), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//
//    //自定义适配器
//    class PictureAdapter extends BaseAdapter {
//        private LayoutInflater inflater;
//        private List<Picture> pictures;
//
//        public PictureAdapter(String[] titles, int[] images, Context context)
//        {
//            super();
//            pictures = new ArrayList<>();
//            inflater = LayoutInflater.from(context);
//            for (int i = 0; i < images.length; i++)
//            {
//                Picture picture = new Picture(titles[i], images[i]);
//                pictures.add(picture);
//            }
//        }
//
//        @Override
//        public int getCount()
//        {
//            if (null != pictures)
//            {
//                return pictures.size();
//            } else
//            {
//                return 0;
//            }
//        }
//
//        @Override
//        public Object getItem(int position)
//        {
//            return pictures.get(position);
//        }
//
//        @Override
//        public long getItemId(int position)
//        {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent)
//        {
//            ViewHolder viewHolder;
//            if (convertView == null)
//            {
//                convertView = inflater.inflate(R.layout.layout_component_main_menus_apps_item, null);
//                viewHolder = new ViewHolder();
//                viewHolder.title = (TextView) convertView.findViewById(R.id.apps_item_title);
//                viewHolder.image = (ImageView) convertView.findViewById(R.id.apps_item_image);
//                convertView.setTag(viewHolder);
//            } else
//            {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//            viewHolder.title.setText(pictures.get(position).title);
//            viewHolder.image.setBackgroundResource(pictures.get(position).imageId);
//            return convertView;
//        }
//
//    }
//
//    class ViewHolder
//    {
//        public TextView title;
//        public ImageView image;
//    }
//
//    class Picture
//    {
//        public String title;
//        public int imageId;
//
//        public Picture(String title, int imageId)
//        {
//            super();
//            this.title = title;
//            this.imageId = imageId;
//        }
//
//    }
}

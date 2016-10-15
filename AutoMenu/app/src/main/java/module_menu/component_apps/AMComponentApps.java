package module_menu.component_apps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autonavi.automenu.R;

import java.util.ArrayList;
import java.util.List;

import common.baseClass.AMBaseComponent;
import common.baseClass.AMBaseFragment;
import common.amapauto_relative.AMMapFunctionManager;

/**
 * Created by wuhen on 16/10/13.
 */


public class AMComponentApps extends AMBaseComponent {

    //图片的文字标题
    private String[] titles = new String[]
            { "pic1", "pic2", "pic3", "pic4", "pic5", "pic6", "pic7", "pic8"};
    //图片ID数组
    private int[] images = new int[]{
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher
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

    private void initAppsPager() {
        gridView = (GridView) currnentContainer.findViewById(R.id.main_menu_apps_gridview);
        PictureAdapter adapter = new PictureAdapter(titles, images, currentContext);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Toast.makeText(currentContext, "pic" + (position+1), Toast.LENGTH_SHORT).show();
                if (position == 1)
                {
                    if (AMMapFunctionManager.sharedInstance().isAutoMapInstalled(currentContext))
                    {
                        AMMapFunctionManager.sharedInstance().startupAmapAuto(currentContext);
                    }
                }
            }
        });
    }


    //自定义适配器
    class PictureAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Picture> pictures;

        public PictureAdapter(String[] titles, int[] images, Context context)
        {
            super();
            pictures = new ArrayList<>();
            inflater = LayoutInflater.from(context);
            for (int i = 0; i < images.length; i++)
            {
                Picture picture = new Picture(titles[i], images[i]);
                pictures.add(picture);
            }
        }

        @Override
        public int getCount()
        {
            if (null != pictures)
            {
                return pictures.size();
            } else
            {
                return 0;
            }
        }

        @Override
        public Object getItem(int position)
        {
            return pictures.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder;
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.layout_component_main_menus_apps_item, null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.apps_item_title);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.apps_item_image);
                convertView.setTag(viewHolder);
            } else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(pictures.get(position).title);
            viewHolder.image.setImageResource(pictures.get(position).imageId);
            return convertView;
        }

    }

    class ViewHolder
    {
        public TextView title;
        public ImageView image;
    }

    class Picture
    {
        public String title;
        public int imageId;

        public Picture(String title, int imageId)
        {
            super();
            this.title = title;
            this.imageId = imageId;
        }

    }
}

package module_menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.autonavi.automenu.R;

import common.amapauto_relative.AMMapFunctionManager;
import common.baseClass.AMBaseFragment;
import common.system_tools.AMSystemManager;
import module_menu.component_apps.AMComponentApps;
import module_menu.component_display.AMComponentDisplay;

/**
 * Created by wuhen on 16/10/13.
 */
public class AMMainMenuFragment extends AMBaseFragment {

    //
    AMComponentDisplay componentDisplay;
    AMComponentApps componentApps;

    @Override
    public void onPause() {
        super.onPause();
        componentDisplay.onFragmentPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        componentDisplay.onFragmentResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_fragment_main_menu, container, false);

//        componentApps = rootView.findViewById(R.id.main_menu_apps);
//        componentApps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AMSystemManager.sharedInstance().startThirdPartyApp(getContext(), "com.letv.leauto.ecolink", "com.letv.leauto.ecolink.ui.MainActivity");
//            }
//        });

        componentDisplay = new AMComponentDisplay(this, this.getActivity(), (LinearLayout)rootView.findViewById(R.id.main_menu_display_component));
        componentApps = new AMComponentApps(this, this.getActivity(), (LinearLayout)rootView.findViewById(R.id.main_menu_apps));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

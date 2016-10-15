package common.baseClass;

public interface IAMBaseComponent {

    void onAcivityResume();

    void onAcivityPause();

    void onFragmentResume();

    void onFragmentPause();

    void onFragmentDestroy();

    void destroy();
}

package jokes.gigglebyte.destino.ush.gigglebyte.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;

public class Fragment_Posts extends Fragment implements FragmentLifecycle {
  private FragmentTabHost tabHost;

  public Fragment_Posts() {
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.tab_layout, container, false);

    tabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
    tabHost.setup(getActivity(), getChildFragmentManager(), R.id.content);

    tabHost.addTab(tabHost.newTabSpec("hot").setIndicator(getActivity().getResources().getString(R.string.tab_hot)), Fragment_Hot.class, null);
    tabHost.addTab(tabHost.newTabSpec("new").setIndicator(getActivity().getResources().getString(R.string.tab_new)), Fragment_New.class, null);
    tabHost.addTab(tabHost.newTabSpec("favourite").setIndicator(getActivity().getResources().getString(R.string.tab_favourite)), Fragment_Favorite.class, null);
    tabHost.getTabWidget().getChildAt(0).setBackgroundColor(getActivity().getResources().getColor(R.color.tab_strip));
    tabHost.getTabWidget().getChildAt(1).setBackgroundColor(getActivity().getResources().getColor(R.color.tab_strip));
    tabHost.getTabWidget().getChildAt(2).setBackgroundColor(getActivity().getResources().getColor(R.color.tab_strip));
    TextView selectedTabText = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
    selectedTabText.setTextColor(getActivity().getResources().getColor(R.color.white));
    TextView unSelectedTabText1 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
    unSelectedTabText1.setTextColor(getActivity().getResources().getColor(R.color.gray));
    TextView unSelectedTabText2 = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
    unSelectedTabText2.setTextColor(getActivity().getResources().getColor(R.color.gray));
    tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
      @Override
      public void onTabChanged(String tabId) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
          TextView tabText = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
          tabText.setTextColor(getActivity().getResources().getColor(R.color.gray));
        }
        TextView tabText = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tabText.setTextColor(getActivity().getResources().getColor(R.color.white));
      }
    });

    return rootView;
  }

  @Override
  public void onPauseFragment() {

  }

  @Override
  public void onResumeFragment() {

  }
}

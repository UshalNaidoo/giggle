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

public class Tabs_Search extends Fragment implements FragmentLifecycle {
  private static FragmentTabHost tabHost;

  public Tabs_Search() {
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.tab_layout, container, false);

    tabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
    tabHost.setup(getActivity(), getChildFragmentManager(), R.id.content);

    tabHost.addTab(tabHost.newTabSpec("users").setIndicator(getActivity().getResources().getString(R.string.tab_users), getActivity().getResources().getDrawable(R.drawable.profile_tab)),Fragment_Search_User.class, null);
    tabHost.addTab(tabHost.newTabSpec("tags").setIndicator(getActivity().getResources().getString(R.string.tab_tags), getActivity().getResources().getDrawable(R.drawable.tag_tab)),Fragment_Search_Tag.class, null);
    tabHost.getTabWidget().getChildAt(0).setBackgroundColor(getActivity().getResources().getColor(R.color.tab_strip_selected));
    tabHost.getTabWidget().getChildAt(1).setBackgroundColor(getActivity().getResources().getColor(R.color.tab_strip));
    TextView selectedTabText = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
    selectedTabText.setTextColor(getActivity().getResources().getColor(R.color.text_tab_unselected));
    TextView unSelectedTabText = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
    unSelectedTabText.setTextColor(getActivity().getResources().getColor(R.color.text_tab_unselected));
    tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
      @Override
      public void onTabChanged(String tabId) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
          tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getActivity().getResources().getColor(R.color.tab_strip));
        }
        tabHost.getCurrentTabView().setBackgroundColor(getActivity().getResources().getColor(R.color.tab_strip_selected));
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

  public static void switchTab(int tab){
    tabHost.setCurrentTab(tab);
  }

}

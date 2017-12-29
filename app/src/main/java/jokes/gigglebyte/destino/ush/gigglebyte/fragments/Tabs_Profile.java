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

public class Tabs_Profile extends Fragment implements FragmentLifecycle {
  private static FragmentTabHost tabHost;

  public Tabs_Profile() {
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.tab_layout, container, false);

    tabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
    tabHost.setup(getActivity(), getChildFragmentManager(), R.id.content);

    tabHost.addTab(tabHost.newTabSpec("profile").setIndicator(getActivity().getResources().getString(R.string.tab_profile)),Fragment_Profile.class, null);
    tabHost.addTab(tabHost.newTabSpec("notifications").setIndicator(getActivity().getResources().getString(R.string.tab_notifications)),Fragment_Notifications.class, null);
    tabHost.getTabWidget().getChildAt(0).setBackgroundColor(getActivity().getResources().getColor(R.color.main_background_colour));
    tabHost.getTabWidget().getChildAt(1).setBackgroundColor(getActivity().getResources().getColor(R.color.tab_strip));
    TextView selectedTabText = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
    selectedTabText.setTextColor(getActivity().getResources().getColor(R.color.tab_text_selected));
    TextView unSelectedTabText = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
    unSelectedTabText.setTextColor(getActivity().getResources().getColor(R.color.tab_text_unselected));
    tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
      @Override
      public void onTabChanged(String tabId) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
          TextView tabText = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
          tabText.setTextColor(getActivity().getResources().getColor(R.color.tab_text_unselected));
          tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getActivity().getResources().getColor(R.color.tab_strip));
        }
        TextView tabText = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tabText.setTextColor(getActivity().getResources().getColor(R.color.tab_text_selected));
        tabHost.getCurrentTabView().setBackgroundColor(getActivity().getResources().getColor(R.color.main_background_colour));
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

package jokes.gigglebyte.destino.ush.gigglebyte.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;

public class Fragment_Search extends Fragment  implements FragmentLifecycle {
  private FragmentTabHost mTabHost;

  //Mandatory Constructor
  public Fragment_Search() {
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.tab_search,container, false);


    mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
    mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

    mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("Fragment B"),
                    Fragment_Search_User.class, null);
    mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("Fragment C"),
                    Fragment_Search_Tag.class, null);


    return rootView;
  }

  @Override
  public void onPauseFragment() {

  }

  @Override
  public void onResumeFragment() {

  }
}

package jokes.gigglebyte.destino.ush.gigglebyte.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.PostListAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;

public class Fragment_Favorite extends Fragment implements FragmentLifecycle {

  private static PostListAdapter adapter;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    Activity activity = this.getActivity();
    View rootView = inflater.inflate(R.layout.fragment_favorite_screen, container, false);
    ListView listView = (ListView) rootView.findViewById(R.id.listView);
    adapter = new PostListAdapter(activity, PostHelper.getFavoritePosts());
    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 0) {
          adapter.notifyDataSetChanged();
        }
      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                           int totalItemCount) {
      }
    });
    listView.setAdapter(adapter);
    return rootView;
  }

  public static void refreshList() {
    if (adapter != null) {
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onPauseFragment() {
  }

  @Override
  public void onResumeFragment() {
    refreshList();
  }
}
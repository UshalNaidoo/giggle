package jokes.gigglebyte.destino.ush.gigglebyte.fragments;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.PostListAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class Fragment_Feed extends Fragment implements FragmentLifecycle {

  private static PostListAdapter adapter;

  public static void refreshList() {
    if (adapter != null) {
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    final Activity activity = this.getActivity();
    View rootView = inflater.inflate(R.layout.fragment_post_list, container, false);
    final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
    swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        swipeView.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
          @Override
          public void run() {
            swipeView.setRefreshing(false);
            Thread thread = new Thread() {
              @Override
              public void run() {
                PostHelper.initialiseFeedPosts(activity, ConnectToServer.getFeed(UserHelper.getUsersId(activity)));
              }
            };
            thread.start();
          }
        }, 200);
        refreshList();
      }
    });

    final ListView listView = (ListView) rootView.findViewById(R.id.listView);
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
        int topRowVerticalPosition =
            (listView == null || listView.getChildCount() == 0) ?
            0 : listView.getChildAt(0).getTop();
        swipeView.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
      }
    });

    List<Post> infoPost = new ArrayList<>();
    infoPost.add(new Post(activity.getResources()
                              .getString(R.string.info_follow), BitmapFactory.decodeResource(activity
                                                                                                 .getResources(), R.drawable.follow), PostType.INFO_POST));
    infoPost.add(new Post(activity.getResources()
                              .getString(R.string.info_like), BitmapFactory.decodeResource(activity.getResources(), R.drawable.up_arrow), PostType.INFO_POST));

    adapter = new PostListAdapter(activity, PostHelper.getFeedPosts().size() > 0
                                            ? PostHelper.getFeedPosts()
                                            : infoPost, FromScreen.FEED);
    listView.setAdapter(adapter);
    return rootView;
  }

  @Override
  public void onPauseFragment() {
  }

  @Override
  public void onResumeFragment() {
  }
}

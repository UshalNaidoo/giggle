package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.CommentActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.OptionsPostDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostTextViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.UserViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class PosterProfileListAdapter extends BaseAdapter {

  private boolean isDoubleClick = false;
  private static List<Post> posts;
  private static User poster;
  private LayoutInflater mInflater;
  private Activity activity;
  private ToastWithImage toastWithImage;

  public PosterProfileListAdapter(Activity activity, List<Post> results, User user) {
    posts = results;
    poster = user;
    mInflater = LayoutInflater.from(activity);
    this.activity = activity;
    toastWithImage = new ToastWithImage(activity);
  }

  @Override
  public int getCount() {
    return posts.size() + 1;
  }

  @Override
  public Object getItem(int arg0) {
    if (arg0 == 0) {
      return poster;
    }
    return posts.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    if (position == 0) {
      UserViewHolder holder;
      convertView = mInflater.inflate(R.layout.poster_header, parent, false);
      holder = new UserViewHolder();
      holder.description = (TextView) convertView.findViewById(R.id.description);
      holder.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
      convertView.setTag(holder);
      holder.setUserData(activity, poster);
    } else {
      final int pos = position - 1;
      final Post post = posts.get(pos);
      if (post.getType() == PostType.TEXT_POST) {
        final PostTextViewHolder holder;
        convertView = mInflater.inflate(R.layout.profile_text_item, parent, false);
        holder = new PostTextViewHolder();
        holder.timeSince = (TextView) convertView.findViewById(R.id.timeSince);
        holder.postText = (TextView) convertView.findViewById(R.id.postText);
        holder.likes = (TextView) convertView.findViewById(R.id.likes);
        holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
        holder.comments = (TextView) convertView.findViewById(R.id.comments);
        holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
        holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
        holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);

        convertView.setTag(holder);

        holder.timeSince.setText(post.getTimeSincePost());
        holder.comments.setText(String.valueOf(post.getCommentCount()));
        holder.postText.setText(post.getPostText());
        holder.likes.setText(String.valueOf(post.getLikes()));
        final View finalConvertView = convertView;
        holder.shareImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            PostHelper.shareBitmap(activity, finalConvertView.findViewById(R.id.layoutView));
          }
        });

        holder.postText.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent myIntent = new Intent(activity, CommentActivity.class);
            myIntent.putExtra("postId", post.getPostId());
            myIntent.putExtra("posterId", post.getUser().getId());
            myIntent.putExtra("position", pos);
            activity.startActivity(myIntent);
          }
        });

        if (post.isUserLike()) {
          holder.likeImage.setImageResource(R.drawable.star_like);
        } else {
          holder.likeImage.setImageResource(R.drawable.star_unlike);
        }

        if (post.isUserFavorite()) {
          holder.favoriteImage.setImageResource(R.drawable.heart_like);
        } else {
          holder.favoriteImage.setImageResource(R.drawable.heart_unlike);
        }

        holder.layout.setOnClickListener(doubleClickListener(pos, holder));

        holder.layout.setOnLongClickListener(longClickListener(pos, holder.likeImage, holder.likes, poster
            .getId()));

        holder.postText.setOnClickListener(doubleClickListener(pos, holder));

        holder.postText.setOnLongClickListener(longClickListener(pos, holder.likeImage, holder.likes, poster
            .getId()));

        holder.likeImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            likePost(pos, holder);
          }
        });

        holder.likes.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            likePost(pos, holder);
          }
        });

        holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            favoritePost(pos, holder);
          }
        });
      } else if (post.getType() == PostType.IMAGE_POST) {
        final PostImageViewHolder holder;
        convertView = mInflater.inflate(R.layout.profile_image_item, parent, false);
        holder = new PostImageViewHolder();
        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.timeSince = (TextView) convertView.findViewById(R.id.timeSince);
        holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
        holder.likes = (TextView) convertView.findViewById(R.id.likes);
        holder.comments = (TextView) convertView.findViewById(R.id.comments);
        holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
        holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
        holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
        holder.imageProgressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
        convertView.setTag(holder);

        holder.title.setText(post.getPostTitle());

        holder.comments.setText(String.valueOf(post.getCommentCount()));
        holder.timeSince.setText(post.getTimeSincePost());

        //get image from url like profile
        if (post.getPostPicture() != null) {
          holder.imageProgressBar.setVisibility(View.GONE);
          holder.postImage.setImageBitmap(post.getPostPicture());
        } else {
          post.loadImagePost(activity, post.getUser().getId(), post.getImageId(), this, holder.imageProgressBar, holder.postImage);
        }

        holder.likes.setText(String.valueOf(post.getLikes()));

        final int userId = post.getUser().getId();

        final View finalConvertView1 = convertView;
        holder.shareImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            PostHelper.shareBitmap(activity, finalConvertView1.findViewById(R.id.layoutView));
          }
        });

        if (post.isUserLike()) {
          holder.likeImage.setImageResource(R.drawable.star_like);
        } else {
          holder.likeImage.setImageResource(R.drawable.star_unlike);
        }

        if (post.isUserFavorite()) {
          holder.favoriteImage.setImageResource(R.drawable.heart_like);
        } else {
          holder.favoriteImage.setImageResource(R.drawable.heart_unlike);
        }

        holder.layout.setOnClickListener(doubleClickListener(pos, holder));

        holder.layout.setOnLongClickListener(longClickListener(pos, holder.likeImage, holder.likes, userId));

        holder.postImage.setOnClickListener(doubleClickListener(pos, holder));

        holder.postImage.setOnLongClickListener(longClickListener(pos, holder.likeImage, holder.likes, userId));

        holder.likeImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            likePost(pos, holder);
          }
        });

        holder.likes.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            likePost(pos, holder);
          }
        });

        holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            favoritePost(pos, holder);
          }
        });
      }
    }
    return convertView;
  }

  private View.OnLongClickListener longClickListener(final int position, final ImageView likeImage, final TextView likes, final int userId) {
    return new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (userId != UserHelper.getUserDetails(activity).getId()) {
          postOptions(position, likeImage, likes);
        }
        return true;
      }
    };
  }

  private void postOptions(int position, ImageView likeImage, TextView likes) {
    OptionsPostDialog optionsPostDialog = new OptionsPostDialog();
    optionsPostDialog.setPost(posts.get(position));
    optionsPostDialog.setFromAdapter("poster");
    optionsPostDialog.setPosition(position);
    optionsPostDialog.setLikeImage(likeImage);
    optionsPostDialog.setLikes(likes);
    optionsPostDialog.show(activity.getFragmentManager(), "");
  }

  private View.OnClickListener doubleClickListener(final int pos, final PostViewHolder holder) {
    return new View.OnClickListener() {
      int i = 0;

      @Override
      public void onClick(View v) {
        i++;
        Handler handler = new Handler();
        Runnable singleClick = new Runnable() {
          @Override
          public void run() {
            if (!isDoubleClick) {
              Intent myIntent = new Intent(activity, CommentActivity.class);
              MainActivity.selectedPost = posts.get(pos);

              myIntent.putExtra("postId", posts.get(pos).getPostId());
              myIntent.putExtra("posterId", posts.get(pos).getUser().getId());
              myIntent.putExtra("position", pos);
              activity.startActivity(myIntent);
            }
            isDoubleClick = false;
            i = 0;
          }
        };

        if (i == 1) {
          handler.postDelayed(singleClick, 200);
        } else if (i == 2) {
          i = 0;
          likePost(pos, holder);
          isDoubleClick = true;
        }
      }
    };
  }

  private void likePost(int position, PostViewHolder holder) {
    Post post = posts.get(position);
    if (post.isUserLike()) {
      int likes = post.getLikes() - 1;
      holder.likes.setText(String.valueOf(likes));
      PostHelper.adjustPost(activity, holder.likeImage, PostHelper.PostAction.UNLIKE_POST, likes, post);
    } else {
      toastWithImage.show(activity.getResources().getString(R.string.upvoted), R.drawable.star_like);
      int likes = post.getLikes() + 1;
      holder.likes.setText(String.valueOf(likes));
      PostHelper.adjustPost(activity, holder.likeImage, PostHelper.PostAction.LIKE_POST, likes, post);
    }
  }

  private void favoritePost(int position, PostViewHolder holder) {
    Post post = posts.get(position);
    if (post.isUserFavorite()) {
      PostHelper.adjustPost(activity, holder.favoriteImage, PostHelper.PostAction.UNFAVORITE_POST, 0, post);
    } else {
      toastWithImage.show(activity.getResources().getString(R.string.favourites), R.drawable.heart_like);
      PostHelper.adjustPost(activity, holder.favoriteImage, PostHelper.PostAction.FAVORITE_POST, 0, post);
    }
  }

}
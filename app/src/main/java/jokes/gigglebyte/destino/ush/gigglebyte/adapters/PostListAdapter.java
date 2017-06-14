package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.CommentActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.TagActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.OptionsPostDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostTextViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class PostListAdapter extends BaseAdapter {

  private boolean isDoubleClick = false;
  private List<Post> posts;
  private LayoutInflater mInflater;
  private Activity activity;
  private ToastWithImage toastWithImage;

  public PostListAdapter(Activity activity, List<Post> results) {
    this.activity = activity;
    posts = results;
    mInflater = LayoutInflater.from(activity);
    toastWithImage = new ToastWithImage(activity);
  }

  @Override
  public int getCount() {
    return posts.size();
  }

  @Override
  public Object getItem(int arg0) {
    return posts.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    if (posts.get(position).getType() == PostType.TEXT_POST) {
      final PostTextViewHolder holder;
      convertView = mInflater.inflate(R.layout.post_text_item, parent, false);
      holder = new PostTextViewHolder();
      holder.userName = (TextView) convertView.findViewById(R.id.userName);
      holder.timeSince = (TextView) convertView.findViewById(R.id.timeSince);
      holder.tags =  (TextView) convertView.findViewById(R.id.tags);
      holder.postText = (TextView) convertView.findViewById(R.id.postText);
      holder.likes = (TextView) convertView.findViewById(R.id.likes);
      holder.comments = (TextView) convertView.findViewById(R.id.comments);
      holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
      holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
      holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
      holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
      holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
      holder.menuImage = (ImageView) convertView.findViewById(R.id.menuImage);
      convertView.setTag(holder);

      Post post = posts.get(position);

      holder.userName.setText(post.getUserName() == null || post.getUserName().isEmpty()
                              ? "Unknown"
                              : post.getUserName());
      holder.timeSince.setText(post.getTimeSincePost());

      if (post.getTags() != null) {
        String definition = "";
        for (String tag : post.getTags()) {
          definition += " #" + tag;
        }

        holder.tags.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tags.setText(definition, TextView.BufferType.SPANNABLE);
        holder.tags.setTextColor(activity.getResources().getColor(R.color.app_primary_dark));

        Spannable spans = (Spannable) holder.tags.getText();
        Integer[] indices = getIndices(holder.tags.getText().toString(), ' ');
        int start = 0;
        int end;

        for (int i = 0; i <= indices.length; i++) {
          ClickableSpan clickSpan = getClickableSpan();
          end = (i < indices.length ? indices[i] : spans.length());
          spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
          start = end + 1;
        }
      } else {
        holder.tags.setVisibility(View.GONE);
      }

      holder.postText.setText(post.getPostText());
      holder.likes.setText(String.valueOf(post.getLikes()));
      holder.progressBar.setVisibility(View.INVISIBLE);
      holder.comments.setText(String.valueOf(post.getCommentCount()));

      final int userId = post.getUserId();

      if (post.getUserPicture() != null) {
        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.profileImage.setImageBitmap(post.getUserPicture());
      } else {
        post.loadProfileImage(activity, post.getUserId(), this, holder.progressBar, holder.profileImage);
      }

      final View finalConvertView1 = convertView;
      holder.shareImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          PostHelper.shareBitmap(activity, finalConvertView1.findViewById(R.id.layoutView));
        }
      });

      holder.menuImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          postOptions(position, holder.likeImage, holder.favoriteImage, holder.likes);
        }
      });

      holder.profileImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent myIntent = new Intent(activity, PosterProfileActivity.class);
          myIntent.putExtra("userId", userId);
          activity.startActivity(myIntent);
        }
      });

      holder.userName.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent myIntent = new Intent(activity, PosterProfileActivity.class);
          myIntent.putExtra("userId", userId);
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

      holder.layout.setOnClickListener(doubleClickListener(position, holder, PostType.TEXT_POST));

      holder.layout.setOnLongClickListener(longClickListener(position, holder.likeImage, holder.favoriteImage, holder.likes, userId));

      holder.postText.setOnClickListener(doubleClickListener(position, holder, PostType.TEXT_POST));

      holder.postText.setOnLongClickListener(longClickListener(position, holder.likeImage, holder.favoriteImage, holder.likes, userId));

      holder.likes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          likePost(position, holder);
        }
      });

      holder.likeImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          likePost(position, holder);
        }
      });

      holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          favoritePost(position, holder);
        }
      });
    } else if (posts.get(position).getType() == PostType.IMAGE_POST) {
      final PostImageViewHolder holder;
      convertView = mInflater.inflate(R.layout.post_image_item, parent, false);
      holder = new PostImageViewHolder();
      holder.title = (TextView) convertView.findViewById(R.id.title);
      holder.userName = (TextView) convertView.findViewById(R.id.userName);
      holder.timeSince = (TextView) convertView.findViewById(R.id.timeSince);
      holder.tags =  (TextView) convertView.findViewById(R.id.tags);
      holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
      holder.likes = (TextView) convertView.findViewById(R.id.likes);
      holder.comments = (TextView) convertView.findViewById(R.id.comments);
      holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
      holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
      holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
      holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
      holder.imageProgressBar = (ProgressBar) convertView.findViewById(R.id.imageProgressBar);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
      holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
      holder.menuImage = (ImageView) convertView.findViewById(R.id.menuImage);
      convertView.setTag(holder);

      Post post = posts.get(position);
      holder.title.setText(post.getPostTitle() == null ? "" : post.getPostTitle());

      holder.userName.setText(post.getUserName() == null || post.getUserName().isEmpty()
                              ? "Unknown"
                              : post.getUserName());
      holder.comments.setText(String.valueOf(post.getCommentCount()));
      holder.title.setText(post.getPostTitle() == null ? "" : post.getPostTitle());
      holder.timeSince.setText(post.getTimeSincePost());


      if (post.getTags() != null) {
        String definition = "";
        for (String tag : post.getTags()) {
          definition += " #" + tag;
        }

        holder.tags.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tags.setText(definition, TextView.BufferType.SPANNABLE);
        holder.tags.setTextColor(activity.getResources().getColor(R.color.app_primary_dark));

        Spannable spans = (Spannable) holder.tags.getText();
        Integer[] indices = getIndices(holder.tags.getText().toString(), ' ');
        int start = 0;
        int end;

        for (int i = 0; i <= indices.length; i++) {
          ClickableSpan clickSpan = getClickableSpan();
          end = (i < indices.length ? indices[i] : spans.length());
          spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
          start = end + 1;
        }
      } else {
        holder.tags.setVisibility(View.GONE);
      }

      //get image from url like profile
      if (post.getPostPicture() != null) {
        holder.imageProgressBar.setVisibility(View.GONE);
        holder.postImage.setImageBitmap(post.getPostPicture());
      } else {
        post.loadImagePost(activity, post.getUserId(), post.getImageId(), this, holder.imageProgressBar, holder.postImage);
      }

      holder.likes.setText(String.valueOf(post.getLikes()));
      holder.progressBar.setVisibility(View.INVISIBLE);

      final int userId = post.getUserId();
      if (post.getUserPicture() != null) {
        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.profileImage.setImageBitmap(post.getUserPicture());
      } else {
        post.loadProfileImage(activity, post.getUserId(), this, holder.progressBar, holder.profileImage);
      }

      final View finalConvertView = convertView;
      holder.shareImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          PostHelper.shareBitmap(activity, finalConvertView.findViewById(R.id.layoutView));
        }
      });

      holder.profileImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent myIntent = new Intent(activity, PosterProfileActivity.class);
          myIntent.putExtra("userId", userId);
          activity.startActivity(myIntent);
        }
      });

      holder.userName.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent myIntent = new Intent(activity, PosterProfileActivity.class);
          myIntent.putExtra("userId", userId);
          activity.startActivity(myIntent);
        }
      });

      holder.menuImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          postOptions(position, holder.likeImage, holder.favoriteImage, holder.likes);
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

      holder.layout.setOnClickListener(doubleClickListener(position, holder, PostType.IMAGE_POST));

      holder.layout.setOnLongClickListener(longClickListener(position, holder.likeImage, holder.favoriteImage, holder.likes, userId));

      holder.postImage.setOnClickListener(doubleClickListener(position, holder, PostType.IMAGE_POST));

      holder.postImage.setOnLongClickListener(longClickListener(position, holder.likeImage, holder.favoriteImage, holder.likes, userId));

      holder.likeImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          likePost(position, holder);
        }
      });

      holder.likes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          likePost(position, holder);
        }
      });

      holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          favoritePost(position, holder);
        }
      });
    }
    return convertView;
  }

  private View.OnLongClickListener longClickListener(final int position, final ImageView likeImage,
                                                     final ImageView favoriteImage,
                                                     final TextView likes, final int userId) {
    return new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (userId != UserHelper.getUserDetails(activity).getId()) {
          postOptions(position, likeImage, favoriteImage, likes);
        }
        return true;
      }
    };
  }

  public void postOptions(int position, ImageView likeImage, ImageView favoriteImage,
                          TextView likes) {
    OptionsPostDialog optionsPostDialog = new OptionsPostDialog();
    optionsPostDialog.setPost(posts.get(position));
    optionsPostDialog.setFromAdapter("favorite");
    optionsPostDialog.setPosition(position);
    optionsPostDialog.setLikeImage(likeImage);
    optionsPostDialog.setFavoriteImage(favoriteImage);
    optionsPostDialog.setLikes(likes);
    optionsPostDialog.show(activity.getFragmentManager(), "");
  }


  private View.OnClickListener doubleClickListener(final int position, final PostViewHolder holder,
                                                   final PostType type) {
    return new View.OnClickListener() {
      int i = 0;

      @Override
      public void onClick(View v) {
        i++;
        final Handler handler = new Handler();
        final Runnable singleClick = new Runnable() {
          @Override
          public void run() {
            if (!isDoubleClick) {
              if (!posts.get(position).isFinishLoadingProfilePicture()
                  || type == PostType.IMAGE_POST && posts.get(position).getPostPicture() == null) {
                toastWithImage.show("Please wait for the post to load", R.drawable.clock);
                return;
              }
              Intent myIntent = new Intent(activity, CommentActivity.class);
              MainActivity.selectedPost = posts.get(position);
              myIntent.putExtra("postId", posts.get(position).getPostId());
              myIntent.putExtra("posterId", posts.get(position).getUserId());
              myIntent.putExtra("position", position);
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
          likePost(position, holder);
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
      toastWithImage.show("Post upvoted", R.drawable.star_like);
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
      toastWithImage.show("Added to favorites", R.drawable.heart_like);
      PostHelper.adjustPost(activity, holder.favoriteImage, PostHelper.PostAction.FAVORITE_POST, 0, post);
    }
  }

  private ClickableSpan getClickableSpan() {
    return new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        TextView textView = (TextView) widget;
        if (!textView.getText().toString().isEmpty()) {
          String s = textView.getText().subSequence(textView.getSelectionStart(),
                                                    textView.getSelectionEnd()).toString();
          Intent intent = new Intent(activity, TagActivity.class);
          intent.putExtra("tag", s);
          activity.startActivity(intent);
        }
      }

      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
      }
    };
  }

  public static Integer[] getIndices(String s, char c) {
    int pos = s.indexOf(c, 0);
    List<Integer> indices = new ArrayList<Integer>();
    while (pos != -1) {
      indices.add(pos);
      pos = s.indexOf(c, pos + 1);
    }
    return indices.toArray(new Integer[indices.size()]);
  }
}
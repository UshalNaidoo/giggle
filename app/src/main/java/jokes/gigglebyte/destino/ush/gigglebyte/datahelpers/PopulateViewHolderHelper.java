package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.CommentViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.MentionViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationFollowViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationImagePostViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationMentionImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationMentionTextViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationTextPostViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostInfoViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostTextViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.TagViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.UserGridViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.UserListViewHolder;

public class PopulateViewHolderHelper {

  //TODO refactor
  public static void populatePostTextViewHolder(View row, PostTextViewHolder holder) {
    populatePostViewHolder(row, holder, false);
    holder.postText = (TextView) row.findViewById(R.id.postText);
  }

  public static void populatePostImageViewHolder(View row, PostImageViewHolder holder) {
    populatePostViewHolder(row, holder, false);
    holder.menuImage = (ImageView) row.findViewById(R.id.menuImage);
    holder.title = (TextView) row.findViewById(R.id.title);
    holder.postImage = (ImageView) row.findViewById(R.id.postImage);
    holder.imageProgressBar = (ProgressBar) row.findViewById(R.id.imageProgressBar);
  }

  public static void populatePostTextViewHolder(View row, PostTextViewHolder holder,
                                                boolean isMentionContent) {
    populatePostViewHolder(row, holder, isMentionContent);
    holder.postText = (TextView) row.findViewById(R.id.postText);
  }

  public static void populatePostImageViewHolder(View row, PostImageViewHolder holder,
                                                 boolean isMentionContent) {
    populatePostViewHolder(row, holder, isMentionContent);
    holder.menuImage = (ImageView) row.findViewById(R.id.menuImage);
    holder.title = (TextView) row.findViewById(R.id.title);
    holder.postImage = (ImageView) row.findViewById(R.id.postImage);
    holder.imageProgressBar = (ProgressBar) row.findViewById(R.id.imageProgressBar);
  }

  public static void populatePostInfoViewHolder(View convertView, PostInfoViewHolder holder) {
    holder.title = (TextView) convertView.findViewById(R.id.title);
    holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
  }

  private static void populatePostViewHolder(View row, PostViewHolder holder,
                                             boolean isMentionContent) {
    if (isMentionContent) {
      holder.userName = (TextView) row.findViewById(R.id.content_userName);
      holder.profileImage = (ImageView) row.findViewById(R.id.content_pic);
      holder.progressBar = (ProgressBar) row.findViewById(R.id.content_progressBar);
      holder.followButton = (Button) row.findViewById(R.id.content_followButton);
    } else {
      holder.userName = (TextView) row.findViewById(R.id.userName);
      holder.profileImage = (ImageView) row.findViewById(R.id.pic);
      holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
      holder.followButton = (Button) row.findViewById(R.id.followButton);
    }
    holder.tags = (TextView) row.findViewById(R.id.tags);
    holder.postNumberOfLikes = (TextView) row.findViewById(R.id.postNumberOfLikes);
    holder.postNumberOfComments = (TextView) row.findViewById(R.id.postNumberOfComments);
    holder.layout = (LinearLayout) row.findViewById(R.id.layout);
    holder.likeImage = (ImageView) row.findViewById(R.id.likeImage);
    holder.shareImage = (ImageView) row.findViewById(R.id.shareImage);
    holder.menuImage = (ImageView) row.findViewById(R.id.menuImage);
  }

  public static void populateMentionHolder(View row, MentionViewHolder holder) {
    holder.layout = (LinearLayout) row.findViewById(R.id.layout);
    holder.userName = (TextView) row.findViewById(R.id.userName);
    holder.profileImage = (ImageView) row.findViewById(R.id.profileImage);
    holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
  }

  public static void populateCommentViewHolder(View row, CommentViewHolder holder) {
    holder.layout = (LinearLayout) row.findViewById(R.id.layout);
    holder.commentText = (TextView) row.findViewById(R.id.commentText);
    holder.userName = (TextView) row.findViewById(R.id.userName);
    holder.likes = (TextView) row.findViewById(R.id.likes);
    holder.menuImage = (ImageView) row.findViewById(R.id.menuImage);
    holder.likeImage = (ImageView) row.findViewById(R.id.likeImage);
    holder.profileImage = (ImageView) row.findViewById(R.id.profileImage);
    holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
  }

  public static void populateUserListViewHolder(View convertView, UserListViewHolder holder) {
    holder.description = (TextView) convertView.findViewById(R.id.description);
    holder.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    holder.viewFollowers = (Button) convertView.findViewById(R.id.button_followers);
    holder.viewFollowing = (Button) convertView.findViewById(R.id.button_following);
  }

  public static void populateUserGridViewHolder(View convertView, UserGridViewHolder holder) {
    holder.userName = (TextView) convertView.findViewById(R.id.userName);
    holder.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    holder.followButton = (TextView) convertView.findViewById(R.id.followButton);
  }

  public static void populateTagViewHolder(View row, TagViewHolder holder) {
    holder.tagName = (TextView) row.findViewById(R.id.tag);
    holder.numberOfPosts = (TextView) row.findViewById(R.id.numberOfPosts);
  }

  public static void populateNotificationFollowViewHolder(View convertView,
                                                          NotificationFollowViewHolder holder) {
    holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
    holder.userName = (TextView) convertView.findViewById(R.id.userName);
    holder.followButton = (Button) convertView.findViewById(R.id.followButton);
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    holder.informationTextView = (TextView) convertView.findViewById(R.id.userName);
    holder.followeesName = (TextView) convertView.findViewById(R.id.followeesName);
  }

  public static void populateNotificationTextPostViewHolder(View convertView,
                                                            NotificationTextPostViewHolder holder) {
    holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
    holder.userName = (TextView) convertView.findViewById(R.id.userName);
    holder.followButton = (Button) convertView.findViewById(R.id.followButton);
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    holder.informationTextView = (TextView) convertView.findViewById(R.id.userName);
  }

  public static void populateNotificationImagePostViewHolder(View convertView,
                                                             NotificationImagePostViewHolder holder) {
    holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
    holder.userName = (TextView) convertView.findViewById(R.id.userName);
    holder.followButton = (Button) convertView.findViewById(R.id.followButton);
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    holder.informationTextView = (TextView) convertView.findViewById(R.id.userName);
  }

  public static void populateNotificationMentionTextViewHolder(View convertView,
                                                               NotificationMentionTextViewHolder holder) {
    holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
    holder.userName = (TextView) convertView.findViewById(R.id.userName);
    holder.followButton = (Button) convertView.findViewById(R.id.followButton);
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    holder.informationTextView = (TextView) convertView.findViewById(R.id.userName);
  }

  public static void populateNotificationMentionImageViewHolder(View convertView,
                                                                NotificationMentionImageViewHolder holder) {
    holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
    holder.userName = (TextView) convertView.findViewById(R.id.userName);
    holder.followButton = (Button) convertView.findViewById(R.id.followButton);
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    holder.informationTextView = (TextView) convertView.findViewById(R.id.userName);
  }

}

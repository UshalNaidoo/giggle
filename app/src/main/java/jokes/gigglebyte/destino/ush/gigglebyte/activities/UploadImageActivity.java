package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class UploadImageActivity extends FragmentActivity {

  private Activity activity;
  private Bitmap bitmap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    setContentView(R.layout.activity_uploadimage);
    UIHelper.setActionBar(this, "Add Image", true);

    final ImageView image = (ImageView) findViewById(R.id.image);
    final EditText title = (EditText) findViewById(R.id.title);
    final EditText tagText = (EditText) findViewById(R.id.tagText);
    tagText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    try {
      bitmap = createBitmap(MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri
          .parse(getIntent().getStringExtra("uri"))));
      if (getIntent().getBooleanExtra("camera_request", false)) {
        bitmap = rotateImage(bitmap, 90);
      }
      image.setImageBitmap(bitmap);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Button sendButton = (Button) findViewById(R.id.button1);
    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Post post = new Post();
        Thread t = new Thread(new Runnable() {

          @Override
          public void run() {
            try {
              User user = UserHelper.getUserDetails(activity);
              int userId = user.getId();
              String userName = user.getName();

              post.setLikes(0);
              post.setUserLike(false);
              post.setPostText("Testing Image");
              post.setType(PostType.IMAGE_POST);
              post.setUserPicture(ImageHelper.getProfilePicture(userId));
              post.setUserId(userId);
              post.setUserName(userName);
              //TODO get id from server
              post.setImageId(1);
              post.setTimeSincePost("Just Now");
              post.setPostTitle(title.getText().toString());
              post.setPostPicture(bitmap);

              ByteArrayOutputStream stream = new ByteArrayOutputStream();
              bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
              byte[] byte_arr = stream.toByteArray();
              String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);


              final Set<String> tags = new HashSet<String>();
              if (!tagText.getText().toString().isEmpty()) {
                for (String s : tagText.getText().toString().split(" ")) {
                  tags.add(s.substring(1));
                }
              }

              ConnectToServer.postImagePost(userId, image_str, tags, title.getText().toString());
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
        t.start();
        PostHelper helper = new PostHelper();
        helper.setOnSubmitListener(post);
        finish();
      }
    });


    final TextWatcher watcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() > 0 && s.toString().charAt(s.length() - 1) == ' ') {
          StringBuilder sb = new StringBuilder();
          tagText.removeTextChangedListener(this);
          String[] tags = s.toString().trim().split(" ");
          for (String tag : tags) {
            if (!(tag.charAt(0) == '#')) {
              sb.append("#" + tag + " ");
            } else {
              sb.append(tag + " ");
            }
          }
          tagText.setText(sb);
          tagText.setSelection(tagText.getText().length());
          tagText.addTextChangedListener(this);
        }
      }
    };

    tagText.addTextChangedListener(watcher);
  }

  private static Bitmap rotateImage(Bitmap source, int angle) {
    Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
  }

  private static Bitmap createBitmap(Bitmap bitmap) {
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
  }
}

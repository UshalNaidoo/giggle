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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class UploadImageActivity extends FragmentActivity {

  private Activity activity;
  private Bitmap bitmap;

  private static Bitmap rotateImage(Bitmap source, int angle) {
    Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
  }

  private static Bitmap createBitmap(Bitmap bitmap) {
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    setContentView(R.layout.activity_uploadimage);

    final ImageView image = (ImageView) findViewById(R.id.image);
    final EditText title = (EditText) findViewById(R.id.title);
    final MultiAutoCompleteTextView tagText = (MultiAutoCompleteTextView) findViewById(R.id.tagText);

    tagText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    tagText.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
      public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;
        while (i > 0 && text.charAt(i - 1) != ' ') {
          i--;
        }
        while (i < cursor && text.charAt(i) == ' ' || i > 0 && text.charAt(i - 1) == '\n') {
          i++;
        }
        return i;
      }

      public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();

        while (i < len) {
          if (text.charAt(i) == ' ' || text.charAt(i - 1) == '\n') {
            return i;
          } else {
            i++;
          }
        }

        return len;
      }

      public CharSequence terminateToken(CharSequence text) {
        int i = text.length();
        while (i > 0 && text.charAt(i - 1) == ' ' || text.charAt(i - 1) == '\n') {
          i--;
        }

        if (i > 0 && text.charAt(i - 1) == ' ' || text.charAt(i - 1) == '\n') {
          return text;
        } else {
          if (text instanceof Spanned) {
            SpannableString sp = new SpannableString(text + " ");
            TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
            return sp;
          } else {
            return text + " ";
          }
        }
      }
    });

    ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, MainActivity.allTags);
    tagText.setThreshold(1);
    tagText.setAdapter(adp);

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
              post.setLikes(0);
              post.setUserLike(false);
              post.setType(PostType.IMAGE_POST);
              post.setUser(user);
              //TODO get id from server
              post.setImageId(1);
              post.setTimeSincePost("Just Now");
              post.setPostTitle(title.getText().toString());
              post.setPostPicture(bitmap);

              ByteArrayOutputStream stream = new ByteArrayOutputStream();
              bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
              byte[] byte_arr = stream.toByteArray();
              String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

              final Set<String> tags = new HashSet<>();
              if (!tagText.getText().toString().isEmpty()) {
                for (String s : tagText.getText().toString().split(" ")) {
                  if (!(s.charAt(0) == '#')) {
                    tags.add(s);
                  } else {
                    tags.add(s.substring(1));
                  }
                }
              }

              post.setTags((List<String>) tags);
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
              sb.append("#").append(tag).append(" ");
            } else {
              sb.append(tag).append(" ");
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
}

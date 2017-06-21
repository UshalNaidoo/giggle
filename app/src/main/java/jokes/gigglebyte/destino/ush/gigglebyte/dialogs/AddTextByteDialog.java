package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class AddTextByteDialog extends DialogFragment {

  private int userId;
  private String userName;

  private onSubmitListener listener;
  private EditText byteText;
  private EditText tagText;
  private TextView countTextView;

  String regexPattern = "(#\\w+)";

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Dialog dialog = new Dialog(getActivity());
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_add_textbyte);
    dialog.show();
    Button sendButton = (Button) dialog.findViewById(R.id.button1);
    byteText = (EditText) dialog.findViewById(R.id.postText);
    tagText = (EditText) dialog.findViewById(R.id.tagText);
    tagText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    if (getArguments() != null && getArguments().getString("text") != null) {
      byteText.setText(
          getArguments().getString("text") == null ? "" : getArguments().getString("text"));
    }

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
    countTextView = (TextView) dialog.findViewById(R.id.countTextView);

    sendButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (!byteText.getText().toString().trim().isEmpty()) {
          final Post post = new Post();
          Thread thread = new Thread() {
            @Override
            public void run() {
              post.setLikes(0);
              post.setUserLike(false);
              post.setPostText(byteText.getText().toString().trim());
              post.setType(PostType.TEXT_POST);
              User user = new User(userId, userName, null, ImageHelper.getProfilePicture(userId));
              post.setUser(user);
              post.setTimeSincePost("Just Now");
              post.setPostTitle("");

              final Set<String> tags = new HashSet<>();
              if (!tagText.getText().toString().isEmpty()) {
                for (String s : tagText.getText().toString().split(" ")) {
                  tags.add(s.substring(1));
                }
              }
              post.setPostId(ConnectToServer.postTextPost(userId, byteText.getText()
                  .toString()
                  .trim(), tags));

              getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  getListener().setOnSubmitListener(post);
                }
              });
              dismiss();
            }
          };
          thread.start();
        }
      }
    });

    byteText.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        int remaining = 300 - s.toString().length();
        String text = remaining + "/300";
        countTextView.setText(text);
        //TODO check for hashtags in byte
      }
    });

    tagText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && tagText.getText().toString().equals("")) {
          Pattern p = Pattern.compile(regexPattern);
          Matcher m = p.matcher(byteText.getText().toString());
          while (m.find()) {
            String hashtag = m.group(1);
            tagText.setText(tagText.getText() + " " + hashtag);
          }
          //TODO check for # and space
          //TODO check for duplicate bytes
        }
      }
    });

    return dialog;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public onSubmitListener getListener() {
    return listener;
  }

  public void setListener(onSubmitListener listener) {
    this.listener = listener;
  }
}

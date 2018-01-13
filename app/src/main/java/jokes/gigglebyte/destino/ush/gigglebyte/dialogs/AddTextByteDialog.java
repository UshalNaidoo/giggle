package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class AddTextByteDialog extends DialogFragment {

  String regexPattern = "(#\\w+)";
  private int userId;
  private String userName;
  private onSubmitListener listener;
  private EditText byteText;
  private TextView countTextView;
  private List<String> tagsAssigned;
  private MultiAutoCompleteTextView tagText;

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
    tagText = (MultiAutoCompleteTextView) dialog.findViewById(R.id.tagText);
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

    ArrayAdapter<String> adp = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, MainActivity.allTags);
    tagText.setThreshold(1);
    tagText.setAdapter(adp);

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
          tagsAssigned = new ArrayList<>();
          for (String tag : tags) {
            if (!(tag.charAt(0) == '#')) {
              tagsAssigned.add(tag);
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
              post.setTags(tagsAssigned);
              User user = new User(userId, userName, null, ImageHelper.getProfilePicture(userId));
              post.setUser(user);
              post.setTimeSincePost("Just Now");
              post.setPostTitle("");

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

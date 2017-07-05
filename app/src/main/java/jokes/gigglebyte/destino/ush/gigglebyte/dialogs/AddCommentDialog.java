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

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class AddCommentDialog extends DialogFragment {

  private int userId;
  private String userName;
  private int postId;
  private int posterId;
  private onSubmitListener listener;
  private MultiAutoCompleteTextView commentText;
  private TextView countTextView;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Dialog dialog = new Dialog(getActivity());
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_add_comment);
    dialog.show();
    Button sendButton = (Button) dialog.findViewById(R.id.button1);
    commentText = (MultiAutoCompleteTextView) dialog.findViewById(R.id.editText1);
    countTextView = (TextView) dialog.findViewById(R.id.countTextView);
    commentText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    commentText.setTokenizer(new MultiAutoCompleteTextView.Tokenizer(){
      public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;
        if (i > 0 && text.charAt(i - 1) == '@') return i;
        while (i > 0 && text.charAt(i - 1) != ' ') {
          i--;
        }
        while (i < cursor && text.charAt(i) == ' ' || i >0 && text.charAt(i - 1) == '\n') {
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
          }
          else {
            return text + " ";
          }
        }
      }
    });

    ArrayAdapter<String> adp= new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, FollowHelper.getFollowerNames());
    commentText.setThreshold(0);
    commentText.setAdapter(adp);

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
          commentText.removeTextChangedListener(this);
          commentText.setSelection(commentText.getText().length());
          commentText.addTextChangedListener(this);
        }
      }
    };

    commentText.addTextChangedListener(watcher);

    sendButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (!commentText.getText().toString().trim().isEmpty()) {
          Random r = new Random();
          int Low = 900;
          int High = 999;
          int CommentId = r.nextInt(High - Low) + Low;

          Comment comment = new Comment();
          comment.setCommentId(CommentId);
          comment.setCommentText(commentText.getText().toString().trim());
          comment.setLikes(0);
          comment.setUserLike(false);
          User user = new User(getUserId(), getUserName(), null, null);
          comment.setUser(user);
          getListener().setOnSubmitListener(comment);

          Thread thread = new Thread() {
            @Override
            public void run() {
              ConnectToServer.postComment(userId, getPostId(), commentText.getText()
                  .toString()
                  .trim(), posterId);
            }
          };
          thread.start();

          dismiss();
        }
      }
    });

    commentText.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        int remaining = 160 - s.toString().length();
        String text = remaining + "/160";
        countTextView.setText(text);
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

  public int getPostId() {
    return postId;
  }

  public void setPosterId(int posterId) {
    this.posterId = posterId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public onSubmitListener getListener() {
    return listener;
  }

  public void setListener(onSubmitListener listener) {
    this.listener = listener;
  }

}

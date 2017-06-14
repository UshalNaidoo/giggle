package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class EditUserNameDialog extends DialogFragment {

  private Activity activity;
  private User user;
  private onSubmitListener listener;
  private EditText userText;
  private TextView countTextView;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    activity = getActivity();
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_edit_username);
    dialog.show();
    Button sendButton = (Button) dialog.findViewById(R.id.button1);
    userText = (EditText) dialog.findViewById(R.id.editText1);
    countTextView = (TextView) dialog.findViewById(R.id.countTextView);

    ImageButton closeButton = (ImageButton) dialog.findViewById(R.id.buttonClose);
    closeButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });

    sendButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (!userText.getText().toString().trim().isEmpty()) {
          user.setName(userText.getText().toString());

          Thread thread = new Thread() {
            @Override
            public void run() {
              ConnectToServer.changeUserName(user.getId(), user.getName());
            }
          };
          thread.start();

          getListener().setOnSubmitListener(activity, user);
          dismiss();
        }
      }
    });

    userText.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        countTextView.setText(20 - s.toString().length() + "/20");
      }
    });
    return dialog;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public onSubmitListener getListener() {
    return listener;
  }

  public void setListener(onSubmitListener listener) {
    this.listener = listener;
  }

}

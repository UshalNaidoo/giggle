package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class OptionsConfirmUnfollow extends DialogFragment {

  private User user;
  private Menu menu;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Activity activity = getActivity();
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_option_delete);
    dialog.show();

    Button buttonYes = (Button) dialog.findViewById(R.id.buttonYes);
    Button buttonNo = (Button) dialog.findViewById(R.id.buttonNo);
    TextView messageTextView = (TextView) dialog.findViewById(R.id.messageTextView);
    messageTextView.setText(activity.getResources().getString(R.string.confirm_unfollow));

    buttonNo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });

    buttonYes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Thread thread = new Thread(new Runnable() {
          @Override
          public void run() {
            try  {
              FollowHelper.unfollowUser(activity, user);
              if (menu != null) {
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
              }
              new ToastWithImage(activity).show(activity.getResources().getString(R.string.unfollowing) + " " + user.getName(), R.drawable.follow);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

        thread.start();
        dismiss();
      }
    });

    return dialog;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMenu(Menu menu) {
    this.menu = menu;
  }
}

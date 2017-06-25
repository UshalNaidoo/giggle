package jokes.gigglebyte.destino.ush.gigglebyte.widgets;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;

public class ToastWithImage {

  private Activity activity;
  private View layout;

  public ToastWithImage(Activity activity) {
    this.activity = activity;
    layout = LayoutInflater.from(this.activity).inflate(R.layout.toast_with_image, null);
  }

  public void show(String textToDisplay, Integer imageToDisplay) {
    ImageView image = (ImageView) layout.findViewById(R.id.image);
    if (imageToDisplay != null) {
      image.setImageResource(imageToDisplay);
    } else {
      image.setVisibility(View.INVISIBLE);
    }
    TextView text = (TextView) layout.findViewById(R.id.text);
    text.setText(textToDisplay);

    Toast toast = new Toast(this.activity);
    toast.setGravity(Gravity.CENTER, 0, 40);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(layout);
    toast.show();
    UIHelper.imageViewClickAnimation(image);
  }
}

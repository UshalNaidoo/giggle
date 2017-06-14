package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Favorite;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Hot;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_New;

public class UIHelper {

  public static void imageViewClickAnimation(ImageView image) {
    final float growTo = 1.2f;
    final long duration = 300;
    ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo,
                                             Animation.RELATIVE_TO_SELF, 0.5f,
                                             Animation.RELATIVE_TO_SELF, 0.5f);
    grow.setDuration(duration / 2);
    ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1,
                                               Animation.RELATIVE_TO_SELF, 0.5f,
                                               Animation.RELATIVE_TO_SELF, 0.5f);
    shrink.setDuration(duration / 2);
    shrink.setStartOffset(duration / 2);
    AnimationSet growAndShrink = new AnimationSet(true);
    growAndShrink.setInterpolator(new LinearInterpolator());
    growAndShrink.addAnimation(grow);
    growAndShrink.addAnimation(shrink);
    image.startAnimation(growAndShrink);
  }

  public static ActionBar setActionBar(Activity activity) {
    final ActionBar actionBar = activity.getActionBar();
    ColorDrawable colorDrawable = new ColorDrawable(activity.getResources()
                                                        .getColor(R.color.main_black));
    actionBar.setBackgroundDrawable(colorDrawable);
    actionBar.setCustomView(R.layout.actionbar_custom_view_home);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setDisplayUseLogoEnabled(false);
    actionBar.setDisplayShowHomeEnabled(false);
    return actionBar;
  }

  public static ActionBar setActionBar(Activity activity, String userName, boolean showBackButton) {
    final ActionBar actionBar = setActionBar(activity);
    if (!userName.isEmpty()) {
      LayoutInflater inflater = LayoutInflater.from(activity);
      View view = inflater.inflate(R.layout.actionbar_custom_view, null);
      ((TextView) view.findViewById(R.id.title)).setText(userName);
      actionBar.setCustomView(view);
    }

    if (showBackButton) {
      final Drawable upArrow = activity.getResources()
          .getDrawable(R.drawable.ic_keyboard_arrow_left);
      upArrow.setColorFilter(activity.getResources()
                                 .getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
      actionBar.setHomeAsUpIndicator(upArrow);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
    return actionBar;
  }

  public static void updateScreen() {
    Fragment_New.refreshList();
    Fragment_Hot.refreshList();
    Fragment_Favorite.refreshList();
  }
}

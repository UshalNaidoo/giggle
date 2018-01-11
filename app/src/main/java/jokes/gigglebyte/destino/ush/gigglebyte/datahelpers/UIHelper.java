package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Favorite;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Feed;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Hot;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_New;

public class UIHelper {

  public static void imageViewClickAnimation(ImageView image) {
    final float growTo = 1.1f;
    final long duration = 400;
    ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo , 1, 0.5f, 1, 0.5f);
    grow.setDuration(duration / 2);
    ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1, 2, 0.6f,2, 0.6f);
    shrink.setDuration(duration / 2);
    shrink.setStartOffset(duration / 2);
    AnimationSet growAndShrink = new AnimationSet(true);
    growAndShrink.setInterpolator(new LinearInterpolator());
    growAndShrink.addAnimation(grow);
    growAndShrink.addAnimation(shrink);
    image.startAnimation(growAndShrink);
  }

  public static void updateScreen() {
    Fragment_Feed.refreshList();
    Fragment_New.refreshList();
    Fragment_Hot.refreshList();
    Fragment_Favorite.refreshList();
  }
}

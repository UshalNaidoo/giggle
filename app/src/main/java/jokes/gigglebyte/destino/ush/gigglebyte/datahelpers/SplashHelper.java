package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import jokes.gigglebyte.destino.ush.gigglebyte.R;

public class SplashHelper {

  public static String[] getLoadingMessages(Activity activity) {
    Map<Integer, String> messages = new HashMap<>();
    messages.put(0, activity.getResources().getString(R.string.loadMessage0));
    messages.put(1, activity.getResources().getString(R.string.loadMessage1));
    messages.put(2, activity.getResources().getString(R.string.loadMessage2));
    messages.put(3, activity.getResources().getString(R.string.loadMessage3));
    messages.put(4, activity.getResources().getString(R.string.loadMessage4));
    messages.put(5, activity.getResources().getString(R.string.loadMessage5));
    messages.put(6, activity.getResources().getString(R.string.loadMessage6));
    messages.put(7, activity.getResources().getString(R.string.loadMessage7));
    messages.put(8, activity.getResources().getString(R.string.loadMessage8));
    messages.put(9, activity.getResources().getString(R.string.loadMessage9));
    messages.put(10, activity.getResources().getString(R.string.loadMessage10));
    messages.put(11, activity.getResources().getString(R.string.loadMessage11));
    messages.put(12, activity.getResources().getString(R.string.loadMessage12));
    messages.put(13, activity.getResources().getString(R.string.loadMessage13));
    messages.put(14, activity.getResources().getString(R.string.loadMessage14));
    messages.put(15, activity.getResources().getString(R.string.loadMessage15));
    messages.put(16, activity.getResources().getString(R.string.loadMessage16));
    messages.put(17, activity.getResources().getString(R.string.loadMessage17));
    messages.put(18, activity.getResources().getString(R.string.loadMessage18));
    messages.put(19, activity.getResources().getString(R.string.loadMessage19));
    messages.put(20, activity.getResources().getString(R.string.loadMessage20));
    messages.put(21, activity.getResources().getString(R.string.loadMessage21));
    messages.put(22, activity.getResources().getString(R.string.loadMessage22));
    messages.put(23, activity.getResources().getString(R.string.loadMessage23));
    messages.put(24, activity.getResources().getString(R.string.loadMessage24));
    messages.put(25, activity.getResources().getString(R.string.loadMessage25));
    messages.put(26, activity.getResources().getString(R.string.loadMessage26));
    messages.put(27, activity.getResources().getString(R.string.loadMessage27));
    messages.put(28, activity.getResources().getString(R.string.loadMessage28));
    messages.put(29, activity.getResources().getString(R.string.loadMessage29));
    messages.put(30, activity.getResources().getString(R.string.loadMessage30));
    messages.put(31, activity.getResources().getString(R.string.loadMessage31));
    messages.put(32, activity.getResources().getString(R.string.loadMessage32));
    messages.put(33, activity.getResources().getString(R.string.loadMessage33));
    messages.put(34, activity.getResources().getString(R.string.loadMessage34));
    messages.put(35, activity.getResources().getString(R.string.loadMessage35));
    messages.put(36, activity.getResources().getString(R.string.loadMessage36));
    messages.put(37, activity.getResources().getString(R.string.loadMessage37));
    messages.put(38, activity.getResources().getString(R.string.loadMessage38));
    messages.put(39, activity.getResources().getString(R.string.loadMessage39));
    messages.put(40, activity.getResources().getString(R.string.loadMessage40));
    messages.put(41, activity.getResources().getString(R.string.loadMessage41));

    String[] strings = new String[9];
    for (int i = 0; i < 9; i ++) {
      int j = (int) Math.floor(Math.random() * messages.size() + 1);
      strings[i] = messages.get(j);
    }
    return strings;
  }
}

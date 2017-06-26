package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import java.util.HashMap;
import java.util.Map;

public class SplashHelper {

  public static String[] getLoadingMessages() {
    Map<Integer, String> messages = new HashMap<>();
    messages.put(0, "Collecting funny jokes");
    messages.put(1, "Lifting weights");
    messages.put(2, "Looking for my keys");
    messages.put(3, "Misspelling random words in your posts");
    messages.put(4, "Making you wait");
    messages.put(5, "Don\'t Panic");
    messages.put(6, "Removing zeros, keeping ones");
    messages.put(7, "Talking smack");
    messages.put(8, "Stay a while and listen");
    messages.put(9, "Lok Tar");
    messages.put(10, "Eating ice cream");
    messages.put(11, "Your momma...is nice");
    messages.put(12, "Need a hug?");
    messages.put(13, "Have you lost weight?");
    messages.put(14, "Flipping tables");
    messages.put(15, "Twiddling thumbs");
    messages.put(16, "Browsing the web");
    messages.put(17, "Look, I am your father");
    messages.put(18, "Spinning spinners");
    messages.put(19, "Deleting the internet");
    messages.put(20, "Ctrl Alt Del! Ctrl Alt Delete!");
    messages.put(21, "Switching off and on again");
    messages.put(22, "Delete * from Google");
    messages.put(23, "Generic loading message");
    messages.put(24, "Let the dogs out");
    messages.put(25, "Unfollowing friends");
    messages.put(26, "Stretching");
    messages.put(27, "Brushing teeth");
    messages.put(28, "Does Anyone Actually Read This?");
    messages.put(29, "You\'re looking great today");
    messages.put(30, "Have you lost weight?");
    messages.put(31, "Generating typos");
    messages.put(32, "Banana for scale");
    messages.put(33, "The cake is lying");
    messages.put(34, "Fly you fools");
    messages.put(35, "Barrens chat: LFG WC");
    messages.put(36, "Tap two lands");
    messages.put(37, "Quick Scope");
    messages.put(38, "I can\' tell you the rules of Fight Club");
    messages.put(39, "Reposting generic post");
    messages.put(40, "GG ez");

    String[] strings = new String[9];
    for (int i = 0; i < 9; i ++) {
      int j = (int) Math.floor(Math.random() * messages.size() + 1);
      strings[i] = messages.get(j);
    }
    return strings;
  }
}

package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;

public class LargeProfileImageActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_large_image);

    ImageView imageView = (ImageView) findViewById(R.id.profileImage);
    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

    if (getIntent().getIntExtra("userId", 0) == UserHelper.getUserDetails(this).getId()) {
      progressBar.setVisibility(View.VISIBLE);
      imageView.setImageBitmap(ImageHelper.getProfilePicture(getIntent().getIntExtra("userId", 0)));
      progressBar.setVisibility(View.INVISIBLE);
    } else {
//      if (getIntent().getIntExtra("userId", 0) == -1) {
        //TODO THINK ABOUT HOW TO DO LARGE IMAGES NOW
//        if (ImagePostActivity.postBitmap != null) {
//          progressBar.setVisibility(View.VISIBLE);
//          imageView.setImageBitmap(ImagePostActivity.postBitmap);
//          progressBar.setVisibility(View.INVISIBLE);
//        }
//      } else {
        if (PosterProfileActivity.poster != null) {
          progressBar.setVisibility(View.VISIBLE);
          imageView.setImageBitmap(PosterProfileActivity.poster.getProfile_pic());
          progressBar.setVisibility(View.INVISIBLE);
        }
//      }
    }

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }
}

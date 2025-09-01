package app.ncore.piction;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;


import androidx.appcompat.app.AlertDialog;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import app.ncore.piction.R;


public class AppRater {

    private final static int DAYS_UNTIL_PROMPT = 2;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 2;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.apply();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {

        AlertDialog box = new AlertDialog.Builder(mContext)
                .setTitle(R.string.leave_a_review)
                .setMessage(R.string.review_request)
                .setIcon(R.drawable.ic_star_rate)
                .setPositiveButton(R.string.rate_piction, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ReviewManager manager = ReviewManagerFactory.create(mContext);
                        Task<ReviewInfo> request = manager.requestReviewFlow();
                        request.addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // We can get the ReviewInfo object
                                ReviewInfo reviewInfo = task.getResult();
                                Task<Void> flow = manager.launchReviewFlow((Activity)mContext, reviewInfo);
                                flow.addOnCompleteListener(task1 -> {});
                            }else{
                                if(task.getException()!=null){
                                    Log.d("AppRater", task.getException().toString());
                                }
                                Log.d("AppRater","Exited");
                            }

                        });


                    }})

                .setNeutralButton(R.string.remind_me_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.not_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editor != null) {
                            editor.putBoolean("dontshowagain", true);
                            editor.commit();
                        }
                        dialogInterface.dismiss();
                    }
                })
                .create();
        box.show();

   }


}

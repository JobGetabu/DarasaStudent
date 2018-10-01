package com.job.darasastudent.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringRes;

import com.job.darasastudent.R;

/**
 * Created by Job on Friday : 8/3/2018.
 */
public class Constants {
    public static final String LECUSERCOL = "LecUser";
    public static final String LECTEACHCOL = "LecTeach";
    public static final String LECTEACHTIMECOL = "LecTeachTime";
    public static final String LECTEACHCOURSESUBCOL = "Courses";
    public static final String LECAUTHCOL = "LecAuth";
    public static final String DONECLASSES = "DoneClasses";
    public static final String STUDENTDETAILSCOL = "StudentDetails";
    public static final String DKUTCOURSES = "DkutCourses";


    //prefs
    public static final String COMPLETED_GIF_PREF_NAME = "COMPLETED_GIF_PREF_NAME";



    public static void createEmailIntent(Context ctx,
                                         final @StringRes int int_email,
                                         final @StringRes int int_subject,
                                         final String message) {

        final String toEmail = ctx.getString(R.string.dev_email);
        final String subject = ctx.getString(R.string.dev_subject);

        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(toEmail) +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(message);
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        ctx.startActivity(Intent.createChooser(send, "Send Email to Darasa"));
    }

    public static String getDay(int day) {
        switch (day) {
            case 7:
                return "Saturday";

            case 6:
                return "Friday";

            case 5:
                return "Thursday";

            case 4:
                return "Wednesday";

            case 3:
                return "Tuesday";

            case 2:
                return "Monday";

            case 1:
                return "Sunday";

            default:
                return "";
        }
    }


    //for future  when we will access even finer location details
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.job.hacelaapp.util";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    public static final int LOCATION_INTERVAL = 10000;
    public static final int FASTEST_LOCATION_INTERVAL = 5000;
}

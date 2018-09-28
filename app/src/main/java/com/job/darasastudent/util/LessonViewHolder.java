package com.job.darasastudent.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.GThumb;
import com.job.darasastudent.R;
import com.job.darasastudent.appexecutor.DefaultExecutorSupplier;
import com.job.darasastudent.model.LecTeachTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Job on Monday : 8/6/2018.
 */
public class LessonViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.ls_unit_cd)
    TextView lsUnitcode;
    @BindView(R.id.ls_unit_nm)
    TextView lsUnitname;
    @BindView(R.id.ls_time)
    TextView lsTime;
    @BindView(R.id.ls_card)
    ConstraintLayout lsCard;
    @BindView(R.id.ls_venue)
    TextView lsVenue;
    @BindView(R.id.attn_gthumb)
    GThumb gThumb;


    private Context mContext;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private LecTeachTime lecTeachTime;

    private static final String TAG = "LessonVH";

    public LessonViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void init(Context mContext, FirebaseFirestore mFirestore, FirebaseAuth mAuth, LecTeachTime lecTeachTime) {
        this.mContext = mContext;
        this.mFirestore = mFirestore;
        this.mAuth = mAuth;
        this.lecTeachTime = lecTeachTime;
    }

    /*
    @OnClick(R.id.ls_btn_status)
    public void onLsBtnClicked() {

        QRParser qrParser = new QRParser();
        qrParser.setTime(Calendar.getInstance().getTime());
        qrParser.setLecteachtimeid(lecTeachTime.getLecteachtimeid());
        qrParser.setUnitcode(lecTeachTime.getUnitcode());
        qrParser.setUnitname(lecTeachTime.getUnitname());

        //change status when class is recorded
    }
    */



    @OnClick(R.id.ls_card)
    public void onLsCardClicked() {
    }

    public void setUpUi(final LecTeachTime lecTeachTime) {

        //smoother experience...
        DefaultExecutorSupplier.getInstance().forMainThreadTasks()
                .execute(new Runnable() {
                    @Override
                    public void run() {

                        lsUnitcode.setText("Unit code: "+lecTeachTime.getUnitcode());
                        lsUnitname.setText("Unit :"+lecTeachTime.getUnitname());
                        lsVenue.setText("Venue : "+lecTeachTime.getVenue());
                        lessonTime(lecTeachTime.getTime());
                        gThumb.applyMultiColor();
                        gThumb.setBackgroundShape(GThumb.BACKGROUND_SHAPE.ROUND);
                        gThumb.loadThumbForName("", lecTeachTime.getUnitname());
                        //locationViewer(lecTeachTime);

                    }
                });
    }

   /* private void locationViewer(LecTeachTime lecTeachTime) {
        if (lecTeachTime.getVenue().isEmpty()) {

            DrawableHelper
                    .withContext(mContext)
                    .withColor(R.color.greyish)
                    .withDrawable(R.drawable.ic_location_on)
                    .tint()
                    .applyTo(lsLocImg);
        } else {
            DrawableHelper
                    .withContext(mContext)
                    .withColor(R.color.darkbluish)
                    .withDrawable(R.drawable.ic_location_on)
                    .tint()
                    .applyTo(lsLocImg);
        }
    }*/


    private void lessonTime(Date timestamp) {
        //Timestamp timestamp = model.getTimestamp();
        if (timestamp != null) {

            Date date = timestamp;
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            DateFormat dateFormat2 = new SimpleDateFormat("hh.mm aa");
            lsTime.setText(dateFormat2.format(date));

        }
    }

}

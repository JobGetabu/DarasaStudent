package com.job.darasastudent.util;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.choota.dev.ctimeago.TimeAgo;
import com.hbb20.GThumb;
import com.job.darasastudent.R;
import com.job.darasastudent.model.Notif;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Job on Wednesday : 12/19/2018.
 */
public class NotifViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.ce_gthumb)
    GThumb ceGthumb;
    @BindView(R.id.ce_lecname)
    TextView ceLecname;
    @BindView(R.id.ce_message)
    TextView ceMessage;
    @BindView(R.id.ce_time)
    TextView ceTime;
    @BindView(R.id.ls_card)
    ConstraintLayout lsCard;

    public NotifViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setUpUI(Notif notif){

        ceGthumb.setMonoColor(R.color.white,R.color.white);
        ceGthumb.loadThumbForName("",notif.getTitle());
        ceLecname.setText(notif.getTitle());
        ceMessage.setText(notif.getMessage());

        TimeAgo timeAgo = new TimeAgo();
        String result = timeAgo.getTimeAgo(notif.getTime().toDate());

        ceTime.setText(result);
    }
}

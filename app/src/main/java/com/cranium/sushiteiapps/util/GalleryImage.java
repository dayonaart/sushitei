package com.cranium.sushiteiapps.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.MyCardActivity;
import com.cranium.sushiteiapps.activity.PointHistoryActivity;
import com.vlk.multimager.activities.GalleryActivity;

/**
 * Created by cranium on 3/22/2018.
 */

public class GalleryImage extends GalleryActivity {


    @Override
    public void showLimitAlert(String message) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.remindertoreset_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv = (TextView) dialog.findViewById(R.id.description);
        tv.setText(message);
        ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
        close.setVisibility(View.GONE);
        AppCompatButton rese = (AppCompatButton) dialog.findViewById(R.id.register_button);
        rese.setText("Ok");
        rese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                dialog.cancel();
            }
        });

        dialog.show();
    }
}

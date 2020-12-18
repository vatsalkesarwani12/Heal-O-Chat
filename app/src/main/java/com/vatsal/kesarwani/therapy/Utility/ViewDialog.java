package com.vatsal.kesarwani.therapy.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vatsal.kesarwani.therapy.R;

public class ViewDialog {
    Activity activity;
    Dialog dialog;

    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {
        dialog  = new Dialog(activity);
        dialog.setContentView(R.layout.loading_dialog);

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        Glide.with(activity)
                .asGif()
                .load(R.drawable.loading)
                .into(gifImageView);

        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
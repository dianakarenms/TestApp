package com.itexico.karen.testapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.itexico.karen.testapp.R;

public class Repository {
    //region Progress View
    private static Dialog dialogTransparent;

    public static void showProgressDialog(Context cxt) {
        hideProgressDialog();

        dialogTransparent = new Dialog(cxt, R.style.AppTheme_NoActionBar);
        dialogTransparent.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(cxt).inflate(
                R.layout.item_progress_view, null);
        dialogTransparent.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
        dialogTransparent.setContentView(view);
        dialogTransparent.setCancelable(true);
        dialogTransparent.show();
    }

    public static void hideProgressDialog() {
        if (dialogTransparent != null) {
            if (dialogTransparent.isShowing())
                dialogTransparent.dismiss();
        }
    }
    //endregion
}

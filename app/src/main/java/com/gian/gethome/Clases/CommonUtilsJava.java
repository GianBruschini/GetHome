package com.gian.gethome.Clases;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.gian.gethome.R;

public class CommonUtilsJava {

    public CommonUtilsJava() {
    }

    public Dialog showLoadingDialog(Context context) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog_animated);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

}

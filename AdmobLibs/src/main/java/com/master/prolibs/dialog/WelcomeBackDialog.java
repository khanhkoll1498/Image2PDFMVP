package com.master.prolibs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.master.prolibs.R;

public class WelcomeBackDialog extends Dialog {

    public WelcomeBackDialog(Context context) {
        super(context, R.style.AppTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_welcome_back);
    }
}

package com.master.prolibs.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.master.prolibs.R;


public class PrepareLoadingAdsDialog extends AppCompatActivity {

    public static final String ACTION_DISMISS_DIALOG = "action_dismiss_dialog";
    public static final String ACTION_CLEAR_TEXT_AD = "action_clear_text_ad";
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_DISMISS_DIALOG)) {
                finish();
                return;
            }
            if (intent.getAction().equals(ACTION_CLEAR_TEXT_AD)) {
                clearTextAd();
            }
        }
    };

    @Override
    public void onBackPressed() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PrepareLoadingAdsDialog.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prepair_loading_ads);
        registerReceiver(receiver, new IntentFilter(ACTION_DISMISS_DIALOG));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void clearTextAd() {
        TextView tvLoading = findViewById(R.id.loading_dialog_tv);
        tvLoading.setText("Loading...");
    }
}

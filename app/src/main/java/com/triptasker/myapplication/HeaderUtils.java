package com.triptasker.myapplication;

import android.app.Activity;
import android.widget.ImageView;

public class HeaderUtils {
    public static void setupBackButton(Activity activity) {
        ImageView navigationIcon = activity.findViewById(R.id.icon_navigation);
        navigationIcon.setOnClickListener(v -> {
            activity.finish();
        });
    }
}

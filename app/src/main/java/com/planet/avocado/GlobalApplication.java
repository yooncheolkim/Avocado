package com.planet.avocado;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tsengvn.typekit.Typekit;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumSquareRoundOTFR.otf"))
                .addBold(Typekit.createFromAsset(this, "NanumSquareRoundOTFEB.otf"))
                .addItalic(Typekit.createFromAsset(this, "NanumSquareRoundOTFL.otf"))
                .addBoldItalic(Typekit.createFromAsset(this, "NanumSquareRoundOTFB.otf"));
        Fresco.initialize(this);
    }
}

package com.dhiway.app.seqr;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class MLKitModule  extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;
    MLKitModule(ReactApplicationContext context){
        super(context);
        reactContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return "MLKit";
    }
}
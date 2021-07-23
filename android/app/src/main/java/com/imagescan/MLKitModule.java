package com.imagescan;

import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;


import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.IOException;
import java.util.List;


class BarcodeScanningActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Task<List<Barcode>> getScanningTask(InputImage img){
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        Task<List<Barcode>> result_task = scanner.process(img);

        return result_task;
    }


}

class BarcodeDataProcessor{
    String Taskid;
    public BarcodeDataProcessor(String taskid){
        Taskid = taskid;
    }

    public WritableMap createSuccessResponse(List<Barcode> barcodes){
        WritableMap SuccessResponse = new WritableNativeMap();
        SuccessResponse.putBoolean("Success",true);
        WritableArray response = new WritableNativeArray();

        for(Barcode barcode:barcodes) {

            WritableMap map = new WritableNativeMap();

            //write success


            //write bounding box
            WritableArray boundingPoints = new WritableNativeArray();
            Rect box = barcode.getBoundingBox();
            boundingPoints.pushInt(box.left);
            boundingPoints.pushInt(box.right);
            boundingPoints.pushInt(box.top);
            boundingPoints.pushInt(box.bottom);
            map.putArray("BoundingPoints", boundingPoints);

            //write corner points

            WritableArray cornerpoints = new WritableNativeArray();
            Point[] corners = barcode.getCornerPoints();
            for(Point p : corners) {
                WritableMap point = new WritableNativeMap();
                point.putInt("x", p.x);
                point.putInt("y", p.y);
                cornerpoints.pushMap(point);
            }
            map.putArray("CornerPoints",cornerpoints);

            //Raw Value
            map.putString("RawValue", barcode.getRawValue());


            //value type
            int valueType = barcode.getValueType();

            switch (valueType) {
                case Barcode.TYPE_WIFI:
                    map.putString("value type", "wifi");
                    WritableMap wifimap = new WritableNativeMap();
                    String ssid = barcode.getWifi().getSsid();
                    String password = barcode.getWifi().getPassword();
                    int type = barcode.getWifi().getEncryptionType();

                    wifimap.putString("ssid", ssid);
                    wifimap.putString("password", password);
                    wifimap.putInt("encryption type", type);

                    map.putMap("wifi data", wifimap);
                    break;
                case Barcode.TYPE_URL:
                    map.putString("value type", "url");
                    WritableMap urlmap = new WritableNativeMap();
                    String title = barcode.getUrl().getTitle();
                    String url = barcode.getUrl().getUrl();

                    urlmap.putString("title", title);
                    urlmap.putString("url", url);

                    map.putMap("url data", urlmap);
                    break;
            }
            response.pushMap(map);
        }
        SuccessResponse.putArray("data",response);

        SuccessResponse.putString("taskid",Taskid);



        return SuccessResponse;
    }

    public WritableMap createFailureResponse(Exception e){
        WritableMap map = new WritableNativeMap();
        map.putBoolean("Success",false);
        map.putString("error",e.getMessage());

        map.putString("taskid",Taskid);

        return map;
    }
}


public class MLKitModule  extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;
    MLKitModule(ReactApplicationContext context){
        super(context);
        reactContext = context;
    }

    @ReactMethod
    public void QueueScanTask(String FileName,String taskid, Callback FailureCallback, Callback SuccessCallback){

        InputImage img;

        try{
            Uri imguri = Uri.fromFile(new File(FileName));

            img = InputImage.fromFilePath(reactContext,imguri);

            BarcodeScanningActivity bc = new BarcodeScanningActivity();

            Task<List<Barcode>> scanTask = bc.getScanningTask(img);

            BarcodeDataProcessor bdp = new BarcodeDataProcessor(taskid);

            scanTask.addOnSuccessListener(barcodes -> {
                WritableMap SuccessResponse = bdp.createSuccessResponse(barcodes);

                sendEvent(reactContext,"ScanResponse",SuccessResponse);

            });



            scanTask.addOnFailureListener(error ->{
                WritableMap FailureResponse = bdp.createFailureResponse(error);

                sendEvent(reactContext,"ScanResponse",FailureResponse);
            });


            SuccessCallback.invoke("task "+taskid+" queued");

        }
        catch (Exception e){
            FailureCallback.invoke(e.getMessage());
        }

    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @NonNull
    @Override
    public String getName() {
        return "MLKit";
    }
}
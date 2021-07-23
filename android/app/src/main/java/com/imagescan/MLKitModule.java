package com.imagescan;

import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;


import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
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

    public WritableMap Response = new WritableNativeMap();
    public boolean isSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createSuccessResponse(List<Barcode> barcodes){
        WritableMap SuccessResponse = new WritableNativeMap();
        SuccessResponse.putBoolean("success",true);
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
            map.putArray("corner points",cornerpoints);

            //Raw Value
            map.putString("value", barcode.getRawValue());

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



        Response = SuccessResponse;
    }

    private void createFailureResponse(Exception e){
        WritableMap map = new WritableNativeMap();
        map.putBoolean("Success",false);
        map.putString("error",e.getMessage());

        Response = map;
    }


    private void scanBarcodes(InputImage image) {
        // [START set_detector_options]
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .build();
        // [END set_detector_options]

        // [START get_detector]
        BarcodeScanner scanner = BarcodeScanning.getClient(options);


        // Or, to specify the formats to recognize:
        // BarcodeScanner scanner = BarcodeScanning.getClient(options);
        // [END get_detector]

        // [START run_detector]
        Response.putString("before processing","hi2");
        /*Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    // Task completed successfully
                    // [START_EXCLUDE]
                    // [START get_barcodes]
                    createSuccessResponse(barcodes);

                    Response.putString("inside success function","hi3");
                    // [END get_barcodes]
                    // [END_EXCLUDE]
                })
                .addOnFailureListener(e -> {
                    // Task failed with an exception
                    // ...

                    createFailureResponse(e);
                    Response.putString("inside failure function","hi4");
                })
                .addOnCompleteListener(bar -> {
                    Response.putString("inside on complete listener", "hello5");

                });*/

        Task<List<Barcode>> result = scanner.process(image);

        result.addOnSuccessListener(barcodes -> {
            // Task completed successfully
            // [START_EXCLUDE]
            // [START get_barcodes]
            createSuccessResponse(barcodes);

            Response.putString("inside success function","hi3");
            // [END get_barcodes]
            // [END_EXCLUDE]
        });

        result.addOnFailureListener(e -> {
            // Task failed with an exception
            // ...

            createFailureResponse(e);
            Response.putString("inside failure function","hi4");
        });

        result.addOnCompleteListener(bar -> {
            Response.putString("inside on complete listener", "hello5");

        });

        Response.putBoolean("cancelled",result.isCanceled());
        Response.putBoolean("complete",result.isComplete());
        Response.putBoolean("isSuccessful",result.isSuccessful());


    }

    public WritableMap processImage(InputImage img){

        scanBarcodes(img);

        return Response;

    }

}


public class MLKitModule  extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;
    MLKitModule(ReactApplicationContext context){
        super(context);
        reactContext = context;
    }

    @ReactMethod
    public void MyFunction(String FileName, Callback FailureCallback, Callback SuccessCallback){

        InputImage img;
        WritableMap response;

        try{
            Uri imguri = Uri.fromFile(new File(FileName));



            img = InputImage.fromFilePath(reactContext,imguri);



            BarcodeScanningActivity bc = new BarcodeScanningActivity();
            response = bc.processImage(img);

            response.putInt("Height",img.getHeight());

            response.putString("inside function","hi1");

            SuccessCallback.invoke(response);

        }
        catch (Exception e){
            e.printStackTrace();
            FailureCallback.invoke(e.getMessage());
        }





    }

    @NonNull
    @Override
    public String getName() {
        return "MLKit";
    }
}
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
import com.facebook.react.bridge.Promise;
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
            map.putArray("boundingPoints", boundingPoints);

            //write corner points

            WritableArray cornerpoints = new WritableNativeArray();
            Point[] corners = barcode.getCornerPoints();
            for(Point p : corners) {
                WritableMap point = new WritableNativeMap();
                point.putInt("x", p.x);
                point.putInt("y", p.y);
                cornerpoints.pushMap(point);
            }
            map.putArray("cornerPoints",cornerpoints);

            //Raw Value
            map.putString("rawValue", barcode.getRawValue());

            map.putInt("valueTypeCode",barcode.getValueType());


            //value type
            int valueType = barcode.getValueType();

            switch (valueType) {
                case Barcode.TYPE_WIFI:
                    map.putString("valueType", "wifi");
                    WritableMap wifimap = new WritableNativeMap();
                    String ssid = barcode.getWifi().getSsid();

                    String password = barcode.getWifi().getPassword();
                    int type = barcode.getWifi().getEncryptionType();

                    wifimap.putString("ssid", ssid);
                    wifimap.putString("password", password);
                    wifimap.putInt("encryptionType", type);

                    map.putMap("wifiData", wifimap);
                    break;
                case Barcode.TYPE_URL:
                    map.putString("valueType", "url");
                    WritableMap urlmap = new WritableNativeMap();
                    String title = barcode.getUrl().getTitle();
                    String url = barcode.getUrl().getUrl();

                    urlmap.putString("title", title);
                    urlmap.putString("url", url);

                    map.putMap("urlData", urlmap);
                    break;

                case Barcode.TYPE_TEXT:
                    map.putString("valueType","text");
                    break;

                case Barcode.TYPE_CALENDAR_EVENT:
                    map.putString("valueType","calendarEvent");
                    Barcode.CalendarEvent calendarEvent = barcode.getCalendarEvent();
                    WritableMap cemap = new WritableNativeMap();
                    String description = calendarEvent.getDescription();
                    cemap.putString("description",description);
                    String startTime = calendarEvent.getStart().getRawValue();
                    cemap.putString("startTime",startTime);
                    String endTime = calendarEvent.getEnd().getRawValue();
                    cemap.putString("endTime",endTime);
                    cemap.putString("location",calendarEvent.getLocation());
                    cemap.putString("status",calendarEvent.getStatus());
                    cemap.putString("organizer",calendarEvent.getOrganizer());
                    cemap.putString("Summary",calendarEvent.getSummary());

                    map.putMap("calendarEventData",cemap);
                    break;

                case Barcode.TYPE_CONTACT_INFO:
                    map.putString("valueType","conatactInfo");
                    WritableMap cimap = new WritableNativeMap();
                    Barcode.ContactInfo contactInfo = barcode.getContactInfo();

                    cimap.putString("name",contactInfo.getName().getFormattedName());
                    cimap.putString("title",contactInfo.getTitle());
                    cimap.putString("organization",contactInfo.getOrganization());

                    WritableArray addresses = new WritableNativeArray();
                    for(Barcode.Address address : contactInfo.getAddresses()) {
                        WritableMap addressData = new WritableNativeMap();
                        addressData.putString("address", address.getAddressLines().toString());
                        addressData.putInt("type", address.getType());
                        addresses.pushMap(addressData);
                    }
                    cimap.putArray("addresses",addresses);

                    WritableArray emails = new WritableNativeArray();
                    for(Barcode.Email email : contactInfo.getEmails()) {
                        WritableMap emailData = new WritableNativeMap();
                        emailData.putString("email", email.getAddress());
                        emailData.putInt("type", email.getType());

                        emails.pushMap(emailData);
                    }
                    cimap.putArray("emails",emails);

                    WritableArray phones = new WritableNativeArray();
                    for(Barcode.Phone phone : contactInfo.getPhones()) {
                        WritableMap phoneData = new WritableNativeMap();
                        phoneData.putString("number", phone.getNumber());
                        phoneData.putInt("type", phone.getType());

                        phones.pushMap(phoneData);
                    }
                    cimap.putArray("phones",phones);

                    WritableArray urls = new WritableNativeArray();
                    for(String contacturl : contactInfo.getUrls()) {
                        urls.pushString(contacturl);
                    }
                    cimap.putArray("urls",urls);

                    map.putMap("contactInfoData",cimap);
                    break;

                case Barcode.TYPE_GEO:
                    map.putString("valueType","geo");
                    WritableMap geoData = new WritableNativeMap();
                    geoData.putString("lat", Double.toString(barcode.getGeoPoint().getLat()));
                    geoData.putString("lng", Double.toString(barcode.getGeoPoint().getLng()));
                    map.putMap("geoData",geoData);
                    break;

                case Barcode.TYPE_EMAIL:
                    map.putString("valueType","email");
                    WritableMap emailData = new WritableNativeMap();
                    emailData.putString("email", barcode.getEmail().getAddress());
                    emailData.putInt("type", barcode.getEmail().getType());

                    map.putMap("emailData",emailData);
                    break;

                case Barcode.TYPE_PHONE:
                    map.putString("valueType","phone");
                    WritableMap phoneData = new WritableNativeMap();
                    phoneData.putString("number", barcode.getPhone().getNumber());
                    phoneData.putInt("type", barcode.getPhone().getType());

                    map.putMap("phoneData",phoneData);
                    break;

                case Barcode.TYPE_SMS:
                    map.putString("valueType","sms");
                    WritableMap smsData = new WritableNativeMap();
                    smsData.putString("message",barcode.getSms().getMessage());
                    smsData.putString("number",barcode.getSms().getPhoneNumber());

                    map.putMap("smsData",smsData);
                    break;

                case Barcode.TYPE_DRIVER_LICENSE:
                    map.putString("valueType","driverLicense");
                    WritableMap dlData = new WritableNativeMap();
                    dlData.putString("licenseNumber",barcode.getDriverLicense().getLicenseNumber());
                    dlData.putString("firstName",barcode.getDriverLicense().getFirstName());
                    dlData.putString("middleName",barcode.getDriverLicense().getMiddleName());
                    dlData.putString("lastName",barcode.getDriverLicense().getLastName());
                    dlData.putString("gender",barcode.getDriverLicense().getGender());
                    dlData.putString("birthDate",barcode.getDriverLicense().getBirthDate());
                    dlData.putString("issueDate",barcode.getDriverLicense().getIssueDate());
                    dlData.putString("issuingCountry",barcode.getDriverLicense().getIssuingCountry());
                    dlData.putString("expiryDate",barcode.getDriverLicense().getExpiryDate());
                    dlData.putString("addressCity",barcode.getDriverLicense().getAddressCity());
                    dlData.putString("addressState",barcode.getDriverLicense().getAddressState());
                    dlData.putString("addressStreet",barcode.getDriverLicense().getAddressStreet());
                    dlData.putString("addressZip",barcode.getDriverLicense().getAddressZip());
                    dlData.putString("documentType",barcode.getDriverLicense().getDocumentType());

                    map.putMap("driverLicenseData",dlData);
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
    public void ScanImage(String FileName, Promise promise){
        InputImage img;

        try{
            Uri imguri = Uri.fromFile(new File(FileName));

            img = InputImage.fromFilePath(reactContext,imguri);

            BarcodeScanningActivity bc = new BarcodeScanningActivity();

            Task<List<Barcode>> scanTask = bc.getScanningTask(img);

            BarcodeDataProcessor bdp = new BarcodeDataProcessor("image task");

            scanTask.addOnSuccessListener(barcodes -> {
                WritableMap SuccessResponse = bdp.createSuccessResponse(barcodes);
                promise.resolve(SuccessResponse);

            });

            scanTask.addOnFailureListener(error ->{
                WritableMap FailureResponse = bdp.createFailureResponse(error);

                promise.reject(error);
            });

        }
        catch (Exception e){
            promise.reject(e);
        }
    }


    @NonNull
    @Override
    public String getName() {
        return "MLKit";
    }
}
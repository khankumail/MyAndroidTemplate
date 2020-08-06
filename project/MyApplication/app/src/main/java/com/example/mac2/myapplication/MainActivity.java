package com.example.mac2.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    boolean isKitKat = false;
    String realPath_1;
    Context context;
     private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 940 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        Toast.makeText(getApplicationContext(),"ShouldShowRequest",Toast.LENGTH_LONG).show();
                    } else {
                        // No explanation needed; request the permission
                         ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        Toast.makeText(getApplicationContext(),"ShouldnotShowRequest",Toast.LENGTH_LONG).show();

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    //showFileChooser();
                    selectImages();
                }
                //showFileChooser();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // showFileChooser();
                    selectImages();
                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)){

                    }else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);



                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showFileChooser() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            isKitKat = true;
            startActivityForResult(Intent.createChooser(intent, "Select file"), 1);
        } else {
            isKitKat = false;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select file"), 1);
        }
    }
    public void selectImages() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    int counter = data.getClipData().getItemCount();
                    String [] pathArray = new String[counter];
                    for(int j = 0; j < data.getClipData().getItemCount(); j++)
                    {
                        Uri uri = data.getClipData().getItemAt(j).getUri();
                        String realpath= FilePath.getPath(getApplicationContext(),uri);
                        pathArray[j]=realpath;

                        System.out.println("image" + j + "=" + pathArray[j]);

                    }
                    GetFilePath(pathArray);
                }
            }
        }
        }
        public String[] GetFilePath( String[] path)
        {
            SendingFileToServer(path);
            return path;
        }
        public void SendingFileToServer(String[] filepath)
        {
            //Calling Asynctask function for sending data to server
            String url = "http://yourserver";
            int count = filepath.length;
            File []file;
            file = new File[count];
            File file_explorer;
            for(int i=0;i<count;i++)
            {
                file[i] = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                        filepath[i]);
            }

            /*
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(url);

                InputStreamEntity reqEntity = new InputStreamEntity(
                        new FileInputStream(file), -1);
                reqEntity.setContentType("binary/octet-stream");
                reqEntity.setChunked(true); // Send in multiple parts if needed
                httppost.setEntity(reqEntity);
                HttpResponse response = httpclient.execute(httppost);
                //Do something with response...

            } catch (Exception e) {
                // show error

            }*/


        }

}


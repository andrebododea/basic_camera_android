package com.example.s1350924.basic_camera_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


/* NOTE TO USER: GIVE THE APP PERMISSION FOR
    - Camera
    - Storage

    Do this by going Settings > Apps > Basic_camera_app > Permissions
 */
public class MainActivity extends AppCompatActivity {

    static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private Uri fileUri;

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        final File mediaStorageDir;

        // If external media storage (SD Card) is mounted
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"myWeeCameraApp");
        }else{
            mediaStorageDir = new File("/storage/sdcard0/myWeeCameraApp/");
        }

        // If the storage director doesn't exits
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("myWeeCameraApp", "failed to create directory"); // Sends a DEBUG log message
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile = null;
        if(type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+timeStamp+".jpg");
        }else{
            return null;
        }
        return mediaFile;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image successfully saved", Toast.LENGTH_SHORT).show();
            } else if(resultCode == RESULT_CANCELED){
                // User canceled the image capture
                Toast.makeText(this, "Image capture canceled", Toast.LENGTH_SHORT).show();
            } else{
                // Image capture failed, advise user
                Toast.makeText(this, "Image capture failed, try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action in here

                // Have the camera application capture an image and return it
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Start the camera intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                // Create the Uri of a file to save the image
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                // Specify the path and file name of the received image
                intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.imagenotravez;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imageview;
    Button mButton;
    String mAbsolutePath="";
    final int PHOTO_CONST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageview = (ImageView) findViewById(R.id.imageViewPhoto);
        mButton = (Button)findViewById(R.id.buttonTakePhoto);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    private void takePhoto(){

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePhotoIntent.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try{
                photoFile = createPhotoFile();

            }catch (Exception e){
                e.printStackTrace();
            }

            if(photoFile!=null){
                Uri photoUri = FileProvider.getUriForFile(MainActivity.this,"com.example.imagenotravez",photoFile);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePhotoIntent,PHOTO_CONST);
            }
        }

    }

    private File createPhotoFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        String imageFileName = "imagen "+ timestamp;

        File storageFile=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(imageFileName,".jpg",storageFile);
        mAbsolutePath = photoFile.getAbsolutePath();
        return photoFile;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PHOTO_CONST && resultCode == RESULT_OK){
            Uri uri = Uri.parse(mAbsolutePath);
            imageview.setImageURI(uri);
            //imageview.setImageURI(data.getData());
        }
    }
}
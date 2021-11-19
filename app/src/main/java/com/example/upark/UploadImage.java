package com.example.upark;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UploadImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        ImageView imageView = findViewById(R.id.image_preview);
        imageView.setImageDrawable(null);
    }

    public void upload(View view) {
        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference uploadImg = storageRef.child("images/upload.jpg");

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.upload_img);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] uploadByteStream = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = uploadImg.putBytes(uploadByteStream);
            uploadTask.addOnFailureListener((e) -> {
                Log.i("UPark", "Error uploading Image");
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("UPark", "Successfully uploaded to Firebase.");
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
            Log.i("UPark", "Unknown Error Uploading Image");
        }
    }
}
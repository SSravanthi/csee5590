package com.example.lab4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "LogActivity";
    private final String API_KEY = "IdIwD2cdaC4qmx9beDda4eVnfoH2rh7pM4TFvJRA9Yuu";
    public static final int CAMERA_REQ = 01;
    Single<ClassifiedImages> observable;
    private String foodurl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        observable = Single.create((SingleOnSubscribe<ClassifiedImages>) emitter -> {
            InputStream imageStream = null;
            try {
                imageStream = new FileInputStream(foodurl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream finalImageStream = imageStream;
            IamOptions options = new IamOptions.Builder()
                    .apiKey(API_KEY)
                    .build();

            VisualRecognition visualRecognition = new VisualRecognition("2018-03-19", options);
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .imagesFile(finalImageStream)
                    .imagesFilename("fruitbowl.jpg")
                    .classifierIds(Collections.singletonList("default"))
                    .threshold((float) 0.6)
                    .owners(Collections.singletonList("me"))
                    .build();
            ClassifiedImages classifiedImages = visualRecognition.classify(classifyOptions).execute();
            emitter.onSuccess(classifiedImages);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void getResult() {
        observable.subscribe(new SingleObserver<ClassifiedImages>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ClassifiedImages classifiedImages) {
                System.out.println(classifiedImages.toString());
                TextView result = findViewById(R.id.lblResult);
                List<ClassResult> resultList = classifiedImages.getImages().get(0).getClassifiers().get(0).getClasses();
                String displayText = "";
                for (ClassResult resultL : resultList) {
                    displayText = displayText + " Class : " + resultL.getClassName() + " Score : " + resultL.getScore()+"\n";
                }

                result.setText(displayText);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQ) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageView imgV = findViewById(R.id.imgView);
            imgV.setImageBitmap(bitmap);
            String path = saveImage(bitmap);
            System.out.print(path);
            foodurl = path;
            getResult();
        }
    }

    private String saveImage(Bitmap finalBitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "sample" + timeStamp + ".jpg";

        File file = new File(getFilesDir(), filename);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public void goTohome(View view) {
        Intent redirect = new Intent(CameraActivity.this,HomeActivity.class);
        startActivity(redirect);
    }
}
package cn.nano.camerademo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.camera1_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraPermission()) {
                    Toast.makeText(MainActivity.this, "必须给予camera权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                CameraActivity.forward(MainActivity.this, 1);
            }
        });

        findViewById(R.id.camera2_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraPermission()) {
                    Toast.makeText(MainActivity.this, "必须给予camera权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                CameraActivity.forward(MainActivity.this, 2);
            }
        });


        findViewById(R.id.camerax_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraPermission()) {
                    Toast.makeText(MainActivity.this, "必须给予camera权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                CameraActivity.forward(MainActivity.this, 3);
            }
        });

        //申请权限
        if (!checkCameraPermission()) {
            requestCameraPermission();
        }
    }


    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }


    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "必须给予camera权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

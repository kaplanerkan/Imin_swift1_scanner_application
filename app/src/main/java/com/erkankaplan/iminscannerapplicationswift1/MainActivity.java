package com.erkankaplan.iminscannerapplicationswift1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.erkankaplan.iminscannerapplicationswift1.databinding.ActivityHomeBinding;
import com.erkankaplan.iminscannerapplicationswift1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkPermissions();


    }


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            if (Build.VERSION.SDK_INT >= 30) {
                startActivity(new Intent(this, CustomCaptureActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity2.class));
            }
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("MainActivity", "lsy====onRequestPermissionsResult=");
       /* MyPrintService myPrintService = new MyPrintService();
        myPrintService.callPermissionCall();*/
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//			if (Build.VERSION.SDK_INT >= 30) {
////				startActivity(new Intent(this,MainActivity3.class));
//				startActivity(new Intent(this,CustomCaptureActivity.class));
//			} else {
//				startActivity(new Intent(this,MainActivity2.class));
//			}
        }
        this.finish();

    }


}
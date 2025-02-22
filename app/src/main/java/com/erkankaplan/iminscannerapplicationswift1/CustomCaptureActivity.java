package com.erkankaplan.iminscannerapplicationswift1;


import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imin.scan.CameraScan;
import com.imin.scan.CaptureActivity;
import com.imin.scan.DecodeConfig;
import com.imin.scan.DecodeFormatManager;
import com.imin.scan.Result;
import com.imin.scan.analyze.MultiFormatAnalyzer;
import com.imin.scan.config.ResolutionCameraConfig;

public class CustomCaptureActivity extends CaptureActivity {
    public int decode_count = 0;
    StringBuilder sb = new StringBuilder();
    private Toast toast;
    private TextView textresult, tvScanResult;
    private Button btnStartScan, btnStopScan, btnYazdir;
    private DecodeConfig decodeConfig;

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_capture;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initUI() {
        super.initUI();
        textresult = findViewById(R.id.textresult);
        btnStartScan = findViewById(R.id.btnStartScan);
        btnStopScan = findViewById(R.id.btnStopScan);
        tvScanResult = findViewById(R.id.tvScanResult);
        btnYazdir = findViewById(R.id.btnYazdir);

        btnStartScan.setVisibility(TextView.INVISIBLE);
        btnYazdir.setVisibility(TextView.INVISIBLE);
        initViews();
    }

    @Override
    public void initCameraScan() {
        super.initCameraScan();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getNewDecodeConfig();

        getCameraScan().setPlayBeep(true)//设置是否播放音效，默认为false
                .setPlayRaw(R.raw.beep).setVibrate(true)//设置是否震动，默认为false
//                    .setCameraConfig(new CameraConfig())//设置相机配置信息，CameraConfig可覆写options方法自定义配置
                .setCameraConfig(new ResolutionCameraConfig(CustomCaptureActivity.this))//设置CameraConfig，可以根据自己的需求去自定义配置
                .setNeedAutoZoom(true)//二维码太小时可自动缩放，默认为false
                .setNeedTouchZoom(true)//Unterstützt Multi-Finger-Touch-Pinch-Zoom, Standard ist „true“
                .setDarkLightLux(45f)//设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                .setBrightLightLux(100f)//设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                .bindFlashlightView(null)//绑定手电筒，绑定后可根据光线传感器，动态显示或隐藏手电筒按钮
                .setOnScanResultCallback(this)//设置扫码结果回调，需要自己处理或者需要连扫时，可设置回调，自己去处理相关逻辑
                .setAnalyzer(new MultiFormatAnalyzer(decodeConfig))//设置分析器,DecodeConfig可以配置一些解码时的配置信息，如果内置的不满足您的需求，你也可以自定义实现，
                .setAnalyzeImage(true);//设置是否分析图片，默认为true。如果设置为false，相当于关闭了扫码识别功能

    }


    private void getNewDecodeConfig() {
        decodeConfig = new DecodeConfig();
        decodeConfig.setHints(DecodeFormatManager.ALL_HINTS)////设置解码
                .setSupportVerticalCode(true)//设置是否支持扫垂直的条码 （增强识别率，相应的也会增加性能消耗）
                .setSupportLuminanceInvert(true)//设置是否支持识别反色码，黑白颜色反转（增强识别率，相应的也会增加性能消耗）
                .setAreaRectRatio(0.8f)//设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
//                .setAreaRectVerticalOffset(0)//设置识别区域垂直方向偏移量，默认为0，为0表示居中，可以为负数
//                .setAreaRectHorizontalOffset(0)//设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数
                .setFullAreaScan(false);//设置是否全区域识别，默认false

    }

    private void initViews() {
        btnStartScan.setOnClickListener(v -> {
            tvScanResult.setText("");
            btnStopScan.setVisibility(TextView.VISIBLE);
            btnStartScan.setVisibility(TextView.INVISIBLE);
            btnYazdir.setVisibility(TextView.INVISIBLE);
            getCameraScan().startCamera();
        });

        btnStopScan.setOnClickListener(v -> {
            btnStopScan.setVisibility(TextView.INVISIBLE);
            btnStartScan.setVisibility(TextView.VISIBLE);
            getCameraScan().stopCamera();
        });
    }


    /**
     * 扫码结果回调
     *
     * @param result
     * @return 返回false表示不拦截，将关闭扫码界面并将结果返回给调用界面；
     * 返回true表示拦截，需自己处理逻辑。当isAnalyze为true时，默认会继续分析图像（也就是连扫）。
     * 如果只是想拦截扫码结果回调，并不想继续分析图像（不想连扫），请在拦截扫码逻辑处通过调
     * 用{@link CameraScan#setAnalyzeImage(boolean)}，
     * 因为{@link CameraScan#setAnalyzeImage(boolean)}方法能动态控制是否继续分析图像。
     */
    @Override
    public boolean onScanResultCallback(Result result) {

        if (result == null) {
            return false;
        }


        Resources resources = getResources();
        sb.append(resources.getString(R.string.count)).append(decode_count++);
        sb.append("\n").append(resources.getString(R.string.time_consuming)).append(System.currentTimeMillis() - result.getTimestamp()).append(" ms\n");
        sb.append(resources.getString(R.string.symbology)).append(result.getBarcodeFormat().name()).append("\n");
        sb.append(resources.getString(R.string.capacity)).append(result.getNumBits()).append("\n");
        sb.append(resources.getString(R.string.content)).append(result);
        textresult.setText(sb.toString());
        sb.delete(0, sb.length());
        /*
         * 因为setAnalyzeImage方法能动态控制是否继续分析图像。
         *
         * 1. 因为分析图像默认为true，如果想支持连扫，返回true即可。
         * 当连扫的处理逻辑比较复杂时，请在处理逻辑前调用getCameraScan().setAnalyzeImage(false)，
         * 来停止分析图像，等逻辑处理完后再调用getCameraScan().setAnalyzeImage(true)来继续分析图像。
         *
         * 2. 如果只是想拦截扫码结果回调自己处理逻辑，但并不想继续分析图像（即不想连扫），可通过
         * 调用getCameraScan().setAnalyzeImage(false)来停止分析图像。
         */
        Log.e("XGH", "ScanResult :: " + result);
        tvScanResult.setText(result.toString());

        if (result.toString().length() >= 8) {
            getCameraScan().stopCamera();
            btnStopScan.setVisibility(TextView.INVISIBLE);
            btnStartScan.setVisibility(TextView.VISIBLE);
            btnYazdir.setVisibility(TextView.VISIBLE);
        }
        return true;
    }


    @Override
    public void onScanResultFailure() {
        textresult.setText(sb.toString());
        sb.delete(0, sb.length());
    }

    private void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(text);
        }
        toast.show();
    }
}
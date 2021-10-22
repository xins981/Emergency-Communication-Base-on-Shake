package com.example.demo_service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    private SensorManager _sensorManager = null;
    private Sensor _sensor = null;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = null;
    private boolean _up = false;
    private boolean _down = false;
    private int _count = 0;
    public final static int REQUEST_READ_PHONE_STATE = 1;
    public double latitude = 0;
    public double longitude = 0;
    public String Add = "";
    private String tex = "";
    EditText editText = null;
    SmsManager sms = null;
    LocationClientOption option = null;

    public boolean setLocationInfo(double lati, double longi, String add)
    {
        latitude = lati;
        longitude = longi;
        Add = add;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= (EditText)findViewById(R.id.editTextPersonName);
        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // 创建 SensorManager 类的实例
        _sensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // 创建加速度计的实例
        _sensorManager.registerListener(this, _sensor, SensorManager.SENSOR_DELAY_NORMAL); // 监听传感器
        sms = SmsManager.getDefault();
        mLocationClient = new LocationClient(getApplicationContext());
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(200);
        option.setOpenGps(true);
        option.setIsNeedAltitude(true);
        option.setLocationNotify(true);
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);
        option.setNeedNewVersionRgc(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    // 22222

    /*class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(@NonNull Location location)
        {
            latitude = location.getLatitude(); // 纬度
            longitude = location.getLongitude(); // 经度
            Add = latitude + "," + longitude;
        }
    }*/
    // lallallal woxiugaile sdds
    //dsaffddddddddd

    // master test
    // hot-fix test   

    public class MyLocationListener extends BDAbstractLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //MainActivity.this.setLocationInfo(location.getLatitude(), location.getLongitude(), String.valueOf(location.getAddrStr()));
            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
            Add = String.valueOf(location.getAddrStr());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    //TODO
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                _down = true;
                _up = false;
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                _down = false;
                _up = true;
                return true;
        }
        return super.onKeyDown (keyCode, event);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float[] values = event.values; // 获取三个方向的加速度值
            if(Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math.abs(values[2]) > 14) // 如果三个分量的绝对值超过阈值15，则判断进行了一次摇一摇操作
            {
                tex = editText.getText().toString();
                if (--_count <= 0 && _up && !_down)
                {
                    _count = 5;
                    callPhone(tex);
                }
                else if(--_count <= 0 && _down && !_up)
                {
                    _count = 5;
                    sms.sendTextMessage(tex, null, "我在" + Add + ", 请求 help！",null,null);
                }
            }
        }
    }

    private void callPhone(String phoneNumber)
    {
        //CALL直接拨号，DIAL手动拨号
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}


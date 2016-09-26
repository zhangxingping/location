package com.tjstudy.getlocationdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 定位到当前城市的一个demo
 * 思考：如何实现这样的功能？
 * 网络和GPS 定位  集成第三方sdk
 *
 * 最后还是决定使用第三方的定位功能，使用手机ip定位，获取ip的位置接口混乱（要是网站失效了怎么办？） 还是跟着大公司走好了
 * 经过查看，
 * 百度Android定位SDK自v7.0版本起，按照附加功能不同，向开发者提供了四种不同类型的定位开发包，可根据不同需求下载不同的开发包
 * ----这里使用了其中的基准定位  开发包也才几百k 没有担心apk 突然增大很多的顾虑
 *
 * 百度基准定位，提供几种方式进行定位，wifi 、网络、以及GPS 只要任意一种符合要求就能够进行定位了。
 *
 * 准备设置的demo 场景：
 *   直接使用网络定位  不用考虑GPS的 情况
 *   网络没有开启的情况下  提示打开网络 点击城市 重新加载城市
 *
 */
public class MainActivity extends AppCompatActivity {

    private LocationClient mLocationClient;//定位的核心类
    private TextView tvCity;
    private Button startBtn,stopBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setBaiduLBS();
    }

    private void initView() {
        tvCity = (TextView) findViewById(R.id.tv_city);
        startBtn = (Button) findViewById(R.id.startBtn);
        stopBtn = (Button) findViewById(R.id.stopBtn);
    }

    /**
     * 设置百度基站
     */
    private void setBaiduLBS() {
        mLocationClient = new LocationClient(getApplicationContext());
        //shezhi定位参数
        setLocationOption();
        //注册监听事件
        mLocationClient.registerLocationListener(new MyCityLocationListener());
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient.start();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient.stop();
                tvCity.setText("当前所在地点：null");
            }
        });
    }

    /**
     * 设置定位的参数  模式 经纬度精度等
     */
       private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(false);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * BDLocationListener 监听事件，获取结果信息，成功或失败 以及产生的数据
     */
    class MyCityLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {
            String city = location.getAddrStr();
            if(null==city){
                city="null";
            }
            tvCity.setText("当前所在地点："+city);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}

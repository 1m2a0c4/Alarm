package com.example.alarm

import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView as TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //通信
        //wifidirectの有効無効
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

                // UI update to indicate wifi p2p status.
                String state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    // Wifi Direct mode is enabled
                    activity.setIsWifiP2pEnabled(true);
                } else {
                    activity.setIsWifiP2pEnabled(false);
                    activity.resetData();

                }
                Log.d(WiFiDirectActivity.TAG, "P2P state changed - " + state);
            }
        //デバイス一覧を取得

            // コネクション情報

            //デバイス状態


        //ボタンを定義
        val buttonR = findViewById<Button>(R.id.R) as Button
        val buttonL = findViewById<Button>(R.id.L) as Button
        val buttonU = findViewById<Button>(R.id.U) as Button
        val buttonD = findViewById<Button>(R.id.D) as Button
        val buttonE = findViewById<Button>(R.id.E) as Button

        var TextView = findViewById<TextView>(R.id.TV) as TextView

        //必要な変数を定義
        var flag = "null"

        var ctl = "null"

        /*ボタンが押された時の処理
            --ここから--*/

        //右が押されたら
        buttonR.setOnClickListener(){
            flag = "right"
        }

        //左が押されたら
        buttonL.setOnClickListener(){
            flag = "left"
        }

        buttonD.setOnClickListener() {
            flag = "down"
        }

        buttonU.setOnClickListener(){
            flag = "up"
        }

        buttonE.setOnClickListener(){
            flag = "end"
        }

        //押された結果により処理分岐
       when(flag){
           "right" ->{
                TV.text = "→"
           }
           "left" ->{
               TV.text = "←"
           }
           "up" ->{
               TV.text = "↑"
           }
           "down" ->{
               TV.text = "↓"
           }
           "end" ->{
               TV.text = "おしまい"
           }
           "else" ->{

           }
       }
    }


}

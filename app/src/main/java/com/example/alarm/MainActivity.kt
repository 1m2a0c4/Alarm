package com.example.alarm

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView as TextView

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //通信
        //wifidirectの有効無効
       /*javaでのwifi direct
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
            */
/*
        <uses- Manifest.permission
        android:required="true"
        android:name="android.permission.ACCESS_FINE_LOCATION"/>
        <uses- Manifest.permission
        android:required="true"
        android:name="android.permission.ACCESS_WIFI_STATE"/>
        <uses- Manifest.permission
        android:required="true"
        android:name="android.permission.CHANGE_WIFI_STATE"/>
        <uses- Manifest.permission
        android:required="true"
        android:name="android.permission.INTERNET"/>


 */


        //kotlinでのwi-fi direct

        val intentFilter = IntentFilter()

        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)


        lateinit var channel: WifiP2pManager.Channel
        lateinit var manager: WifiP2pManager

        override fun onCreate(savedInstanceState: Bundle?) {
            manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
            channel = manager.initialize(this, mainLooper, null)
        }


        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                    // Determine if Wifi P2P mode is enabled or not, alert
                    // the Activity.
                    val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                    activity.isWifiP2pEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
                }
                WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {

                    // The peer list has changed! We should probably do something about
                    // that.

                }
                WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

                    // Connection state changed! We should probably do something about
                    // that.

                }
                WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                    (activity.supportFragmentManager.findFragmentById(R.id.frag_list) as DeviceListFragment)
                        .apply {
                            updateThisDevice(
                                intent.getParcelableExtra(
                                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice
                            )
                        }
                }
            }
        }


        /** register the BroadcastReceiver with the intent values to be matched  */
        public override fun onResume() {
            super.onResume()
            receiver = WiFiDirectBroadcastReceiver(manager, channel, this)
            registerReceiver(receiver, intentFilter)
        }

        public override fun onPause() {
            super.onPause()
            unregisterReceiver(receiver)
        }


        override fun onSuccess() {
            // Code for when the discovery initiation is successful goes here.
            // No services have actually been discovered yet, so this method
            // can often be left blank. Code for peer discovery goes in the
            // onReceive method, detailed below.
        }

        override fun onFailure(reasonCode: Int) {
            // Code for when the discovery initiation fails goes here.
            // Alert the user that something went wrong.
        }
    })

    private val peers = mutableListOf<WifiP2pDevice>()

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val refreshedPeers = peerList.deviceList
        if (refreshedPeers != peers) {
            peers.clear()
            peers.addAll(refreshedPeers)

            // If an AdapterView is backed by this data, notify it
            // of the change. For instance, if you have a ListView of
            // available peers, trigger an update.
            (listAdapter as WiFiPeerListAdapter).notifyDataSetChanged()

            // Perform any other updates needed based on the new list of
            // peers connected to the Wi-Fi P2P network.
        }

        if (peers.isEmpty()) {
            Log.d(TAG, "No devices found")
            return@PeerListListener
        }
    }


    fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
                WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            mManager?.requestPeers(channel, peerListListener)
            Log.d(TAG, "P2P peers changed")

        }
        }
    }

    override fun connect() {
        // Picking the first device found on the network.
        val device = peers[0]

        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
            wps.setup = WpsInfo.PBC
        }

        manager.connect(channel, config, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {
                // WiFiDirectBroadcastReceiver notifies us. Ignore for now.
            }

            override fun onFailure(reason: Int) {
                Toast.makeText(
                    this@WiFiDirectActivity,
                    "Connect failed. Retry.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    manager.requestGroupInfo(channel) { group ->
        val groupPassword = group.passphrase
    }


    private val connectionListener = WifiP2pManager.ConnectionInfoListener { info ->

        // InetAddress from WifiP2pInfo struct.
        val groupOwnerAddress: String = info.groupOwnerAddress.hostAddress

        // After the group negotiation, we can determine the group owner
        // (server).
        if (info.groupFormed && info.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a group owner thread and accepting
            // incoming connections.
        } else if (info.groupFormed) {
            // The other device acts as the peer (client). In this case,
            // you'll want to create a peer thread that connects
            // to the group owner.
        }
    }


    when (intent.action) {
        WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

            // Connection state changed! We should probably do something about
            // that.

            mManager?.let { manager ->

                val networkInfo: NetworkInfo? = intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO) as NetworkInfo

                if (networkInfo?.isConnected == true) {

                    // We are connected with the other device, request connection
                    // info to find group owner IP

                    manager.requestConnectionInfo(channel, connectionListener)
                }
            }
        }
    }

    manager.createGroup(channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            // Device is ready to accept incoming connections from peers.
        }

        override fun onFailure(reason: Int) {
            Toast.makeText(
                this@WiFiDirectActivity,
                "P2P group creation failed. Retry.",
                Toast.LENGTH_SHORT
            ).show()
        }
    })






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

package com.riocallos.githubusers.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkReceiver(private val onNetworkChanged: OnNetworkChanged) : BroadcastReceiver() {

    interface OnNetworkChanged {
        fun connected()
        fun disconnected()
    }

    override fun onReceive(context: Context, intent: Intent) {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isWifiConn: Boolean = false
        var isMobileConn: Boolean = false
        connMgr.allNetworks.forEach { network ->
            connMgr.getNetworkInfo(network).apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
                if(isWifiConn || isMobileConn) {
                    onNetworkChanged.connected()
                } else {
                    onNetworkChanged.disconnected()
                }
            }
        }


    }
}
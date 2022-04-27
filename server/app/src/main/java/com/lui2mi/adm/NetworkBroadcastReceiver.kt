package com.lui2mi.adm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.lui2mi.adm.utils.Wifi
import io.reactivex.rxjava3.core.Observable


class NetworkBroadcastReceiver(): BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return
        }
        val ip = Wifi.getIPAddress(true)
        if(MainService.isNetworkObserver()){
            Observable.just(ip.isNotEmpty()).subscribe(MainService.networkObserver)
        }

    }

}
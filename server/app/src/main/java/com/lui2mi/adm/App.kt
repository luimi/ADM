package com.lui2mi.adm

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager


class App: Application() {
    init {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        //registerReceiver(NetworkBroadcastReceiver(), intentFilter)

    }
}
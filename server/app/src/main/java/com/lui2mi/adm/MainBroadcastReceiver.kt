package com.lui2mi.adm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MainBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.e("lui2mi","MainBroadcastReceiver.onReceive")
        if (Intent.ACTION_BOOT_COMPLETED == p1?.getAction()) {
            p0?.startService(Intent(p0,MainService::class.java))
        }
    }
}
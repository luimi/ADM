package com.lui2mi.adm

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lui2mi.adm.adapters.DataListAdapter
import com.lui2mi.adm.models.Device
import com.lui2mi.adm.utils.Terminal
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class MainActivity : AppCompatActivity() {

    lateinit var dataList: RecyclerView
    lateinit var ip: TextView
    lateinit var status: TextView

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            bind = service as MainService.ConnectionBinder
            fillData()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataList = findViewById(R.id.rv_list)
        ip = findViewById(R.id.tv_ip)
        status = findViewById(R.id.tv_status)
        if(!isMainServiceRunning()){
            startService(Intent(this,MainService::class.java))
        }
        val device = Device()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        if(device.isRooted){
            Terminal.simple("su")
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MainService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
        eventObserver = object: Observer<Int> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Int) {
                Log.e("lui2mi","MainActivity.onNext "+t)
                runOnUiThread {
                    fillConnectionStatus()
                }

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        eventObserver = null
    }

    fun fillData(){
        val device = Device().setExtraData(this)
        val data: MutableMap<String, String> = mutableMapOf(
            "IsRoot" to device.isRooted.toString(),
            "Brand" to device.manufacter,
            "Model" to device.model,
            "Processor" to device.processor,
            "Android" to device.version,
            "SDK" to device.versionSDK.toString(),
            "DiskSpace" to device.diskSpace,
            "DiskUsage" to device.diskUsage,
            "DiskFree" to device.diskFree
        )
        dataList.apply {
            adapter = DataListAdapter(this@MainActivity,data)
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
        fillConnectionStatus()
    }
    fun fillConnectionStatus(){
        val device = Device().setExtraData(this)
        ip.setText(if(device.ip.isNotEmpty())"${device.ip}:${device.port}" else "Not Connected")
        val isRunning = bind.getIsServerRunning()
        status.setText(if(isRunning)"Running" else "Waiting")
    }
    private fun isMainServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ((MainService::class.java).name == service.service.className) {
                return true
            }
        }
        return false
    }
    companion object {
        var eventObserver: Observer<Int>? = null
        lateinit var bind: MainService.ConnectionBinder
        val EVENT_SERVER = 1
        val EVENT_DEVICE = 2
        fun isEventObserver(): Boolean{
            return eventObserver!=null
        }
        fun isBind(): Boolean {
            return this::bind.isInitialized
        }
    }
}
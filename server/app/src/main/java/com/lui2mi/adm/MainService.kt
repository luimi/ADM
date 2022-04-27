package com.lui2mi.adm

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.lui2mi.adm.models.Device
import com.lui2mi.adm.utils.Server
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.net.InetSocketAddress


class MainService : Service() {

    override fun onCreate() {
        super.onCreate()
        MainService.context = this
    }

    override fun onBind(intent: Intent): IBinder {
        return ConnectionBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        networkObserver = object : Observer<Boolean> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Boolean) {
                Log.e("lui2mi","networkObserver.onNext")
                if(t) startServer() else stopServer()
            }

            override fun onError(e: Throwable) {
                Log.e("lui2mi","networkObserver.onError")
            }

            override fun onComplete() {
            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isConnectivityManager()) {
            connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    Log.e("lui2mi","NetworkCallback.onAvailable")
                    startServer()
                }

                override fun onLost(network: Network) {
                    Log.e("lui2mi","NetworkCallback.onLost")
                    stopServer()
                    super.onLost(network)
                }
            })
        } else {
            startServer()
        }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.e("lui2mi","MainService.onTaskRemoved")
        stopServer()
        super.onTaskRemoved(rootIntent)
    }

    companion object {
        lateinit var device: Device
        lateinit var context: Context
        lateinit var server: Server
        lateinit var networkObserver: Observer<Boolean>
        lateinit var connectivityManager: ConnectivityManager
        fun startServer(){
            device = Device().setExtraData(context)
            stopServer()
            if(device.ip.isEmpty()){
                Log.e("lui2mi","MainService.startServer.isEmpty")
                return
            }
            Log.e("lui2mi","MainService.startServer "+ device.ip)
            val inetSockAddress  = InetSocketAddress(device.ip, device.port)
            server  = Server(inetSockAddress, context)

            server.start()
            updateView(MainActivity.EVENT_SERVER)
        }
        fun stopServer(){
            if(isServerInitialized() && server.isRunning){
                Log.e("lui2mi","MainService.stopServer")
                server.stop()
                server.isRunning = false
                device = Device()
                updateView(MainActivity.EVENT_SERVER)
            }
        }
        fun isServerInitialized():Boolean {
            return this::server.isInitialized
        }
        fun isConnectivityManager(): Boolean {
            return this::connectivityManager.isInitialized
        }
        fun isNetworkObserver(): Boolean {
            return this::networkObserver.isInitialized
        }
        fun updateView(code: Int) {
            if(MainActivity.isEventObserver() && MainActivity.isBind()){
                Observable.just(code).subscribe(MainActivity.eventObserver)
            }
        }
    }
    inner class ConnectionBinder: Binder(){
        fun getIsServerRunning():Boolean {
            return if(isServerInitialized()) server.isRunning else false
        }
    }


}
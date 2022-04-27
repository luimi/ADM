package com.lui2mi.adm.utils

import android.Manifest
import android.R.attr
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Context.WIFI_SERVICE
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.lui2mi.adm.MainActivity
import com.lui2mi.adm.models.*
import io.reactivex.rxjava3.core.Observable
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress


class Server(address: InetSocketAddress,val context: Context): WebSocketServer(address) {

    var isRunning = false
    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        //Toast.makeText(context, "Dispositivo conectado",Toast.LENGTH_LONG).show()
        Log.e("lui2mi","Server.onOpen")
        val device: Device = Device().setExtraData(context)
        conn?.send(Gson().toJson(device))
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        Log.e("lui2mi","Server.onClose")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        Log.e("lui2mi","Server.onMessage")
        var isReturnDevice = true
        if(message == null || message.isEmpty()){
            return
        }
        Log.e("lui2mi","Server.onMessage.continue "+message)
        val action = Gson().fromJson(message,Action::class.java)
        when (action.type) {
            "shutdown" -> {
                Terminal.sudo("am start -a android.intent.action.ACTION_REQUEST_SHUTDOWN --ez KEY_CONFIRM true --activity-clear-task")
                isReturnDevice = false
            }
            "reboot" -> {
                Terminal.sudo("am start -a android.intent.action.REBOOT")
                isReturnDevice = false
            }
            "setdpi" -> {
                val dpi = Gson().fromJson(message,Action1Value::class.java)
                Terminal.sudo("wm density ${dpi.value1}")
            }
            "resetdpi" -> {
                Terminal.sudo("wm density reset")
            }
            "startadbwifi" -> {
                Terminal.sudo("setprop service.adb.tcp.port 5555 && stop adbd && start adbd")
            }
            "stopadbwifi" -> {
                Terminal.sudo("setprop service.adb.tcp.port -1 && stop adbd && start adbd")
            }
            "startapp" -> {
                val app = Gson().fromJson(message,Action1Value::class.java)
                val intent = context.packageManager.getLaunchIntentForPackage(app.value1)
                context.startActivity(intent)
                isReturnDevice = false
            }
            "removeapp" -> {
                val app = Gson().fromJson(message,Action1Value::class.java)
                Terminal.sudo("pm uninstall ${app.value1}")
            }
            "clearapp" -> {
                val app = Gson().fromJson(message,Action1Value::class.java)
                Terminal.sudo("pm clear ${app.value1}")
                isReturnDevice = false
            }
            "setscreentimeout" -> {
                val timeout = Gson().fromJson(message,Action1Value::class.java)
                Terminal.sudo("settings put system screen_off_timeout ${timeout.value1}")
            }
            "setwifi" -> {
                val wifi = Gson().fromJson(message,Action2Value::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val config = WifiNetworkSpecifier.Builder()
                        .setSsid("\"${wifi.value1}\"")
                        .setWpa2Passphrase("\"${wifi.value2}\"")
                        .build()
                    val network = NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .setNetworkSpecifier(config)
                        .build()
                    val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                    cm.requestNetwork(network, ConnectivityManager.NetworkCallback())
                } else {
                    val config = WifiConfiguration()
                    config.SSID = "\"${wifi.value1}\""
                    config.preSharedKey = "\"${wifi.value2}\""
                    val wm =  context.getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager
                    wm.addNetwork(config)
                }
            }
            "removewifi" -> {
                val wifi = Gson().fromJson(message,Action1Value::class.java)
                val wm =  context.getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager
                wm.removeNetwork(wifi.value1.toInt())
            }
            "setadbkey" -> {
                val key = Gson().fromJson(message,Action1Value::class.java)
                Terminal.sudo("echo \"${key.value1}\" >> /data/misc/adb/adb_keys")
                isReturnDevice = false
            }
            "startbluetooth" -> {
                val ba = BluetoothAdapter.getDefaultAdapter()
                if(checkBluetoothPermission()) ba.enable()
            }
            "stopbluetooth" -> {
                val ba = BluetoothAdapter.getDefaultAdapter()
                if(checkBluetoothPermission()) ba.disable()
            }
            "stopwebsocket" -> {
                stop()
                isReturnDevice = false
            }
            "terminal" -> {
                try {
                    val generic = Gson().fromJson(message,Action1Value::class.java)
                    val response = Terminal.sudo("${generic.value1}")
                    val message = JSONObject()
                    message.put("type","log")
                    message.put("message",response)
                    conn?.send(message.toString())
                }catch (e:Exception) {
                    Log.e("lui2mi","server.generic.exception ${e.message}")
                }
                isReturnDevice = false

            }
        }
        if(isReturnDevice){
            val device = Device().setExtraData(context)
            conn?.send(Gson().toJson(device))
        }
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        //Toast.makeText(context, "Ocurrio un error...",Toast.LENGTH_LONG).show()
        Log.e("lui2mi","Server.onError ${ex?.message} isRunning ${isRunning}" )
    }

    override fun onStart() {
        //Toast.makeText(context, "Servidor iniciado",Toast.LENGTH_LONG).show()
        Log.e("lui2mi","Server.onStart")
        isRunning = true
        if(MainActivity.isEventObserver()){
            Observable.just(MainActivity.EVENT_SERVER).subscribe(MainActivity.eventObserver)
        }
    }
    fun checkBluetoothPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (context as Activity).requestPermissions( arrayOf(Manifest.permission.BLUETOOTH_CONNECT),1)
            }
        }
        return false
    }

}
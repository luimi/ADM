package com.lui2mi.adm.models

import android.Manifest
import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings.Secure
import androidx.core.app.ActivityCompat
import com.lui2mi.adm.utils.Root
import com.lui2mi.adm.utils.Terminal
import com.lui2mi.adm.utils.Wifi
import java.io.BufferedReader
import java.io.FileReader
import java.text.DecimalFormat


class Device() {
    var type: String = "device"
    var id: String = ""
    var ip: String = Wifi.getIPAddress(true)
    val port: Int = 8887
    var manufacter: String = Build.MANUFACTURER
    var model: String = Build.MODEL
    var version: String = Build.VERSION.RELEASE
    var versionSDK: Int = Build.VERSION.SDK_INT
    var diskSpace: String = bytesToHuman(totalMemory())
    var diskUsage: String = bytesToHuman(busyMemory())
    var diskFree: String = bytesToHuman(freeMemory())
    //var ram: String = bytesToHuman(getRamSize())
    var processor: String = getProcessorName()
    var displayDPI: Int = 0
    var isRooted: Boolean = Root().isDeviceRooted()
    var isADBWifi: Boolean = false
    var installedApps: MutableList<App> = mutableListOf()
    var storedWifis: MutableList<com.lui2mi.adm.models.Wifi> = mutableListOf()
    var isBluetooth: Boolean = false
    var screenTimeOut: Long = 0
    init {
        val ba = BluetoothAdapter.getDefaultAdapter()
        isADBWifi = checkADBWifi()
        isBluetooth = ba.isEnabled()
        screenTimeOut = Terminal.sudo("settings get system screen_off_timeout").toLong()
    }
    fun setExtraData(context: Context): Device{
        id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID)
        displayDPI = getDPI(context)
        installedApps = appList(context)
        storedWifis = wifilist(context)
        return this
    }
    fun totalMemory(): Long {
        val statFs = StatFs(Environment.getRootDirectory().absolutePath)
        return (statFs.blockCount * statFs.blockSize).toLong()
    }

    fun freeMemory(): Long {
        val statFs = StatFs(Environment.getRootDirectory().absolutePath)
        return (statFs.availableBlocks * statFs.blockSize).toLong()
    }

    fun busyMemory(): Long {
        val statFs = StatFs(Environment.getRootDirectory().absolutePath)
        val total = (statFs.blockCount * statFs.blockSize).toLong()
        val free = (statFs.availableBlocks * statFs.blockSize).toLong()
        return total - free
    }
    fun floatForm(d: Double): String {
        return DecimalFormat("#.##").format(d)
    }


    fun bytesToHuman(size: Long): String {
        val Kb = (1 * 1024).toLong()
        val Mb = Kb * 1024
        val Gb = Mb * 1024
        val Tb = Gb * 1024
        val Pb = Tb * 1024
        val Eb = Pb * 1024
        if (size < Kb) return floatForm(size.toDouble()) + " byte"
        if (size >= Kb && size < Mb) return floatForm(size.toDouble() / Kb) + " Kb"
        if (size >= Mb && size < Gb) return floatForm(size.toDouble() / Mb) + " Mb"
        if (size >= Gb && size < Tb) return floatForm(size.toDouble() / Gb) + " Gb"
        if (size >= Tb && size < Pb) return floatForm(size.toDouble() / Tb) + " Tb"
        if (size >= Pb && size < Eb) return floatForm(size.toDouble() / Pb) + " Pb"
        return if (size >= Eb) floatForm(size.toDouble() / Eb) + " Eb" else "???"
    }

    /*fun getRamSize(): Long{
        var result = 0L
        try{
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()

            if(actManager != null){
                actManager!!.getMemoryInfo(memInfo)
                result = memInfo.totalMem
                //val availMemory = memInfo.availMem
                //val usedMemory = totalMemory - availMemory
                //val precentlong = (availMemory / totalMemory).toFloat() * 100
            }
        }catch (e: Exception){}

        return result
    }*/

    fun getProcessorName(): String {
        var result = ""
        val br = BufferedReader(FileReader("/proc/cpuinfo"))
        var str: String
        val output: MutableMap<String, String> = HashMap()
        try{
            while (br.readLine().also { str = it } != null) {
                val data = str.split(":").toTypedArray()
                if (data.size > 1) {
                    var key = data[0].trim { it <= ' ' }.replace(" ", "_")
                    if (key == "model_name") key = "cpu_model"
                    output[key] = data[1].trim { it <= ' ' }
                }
            }
        }catch (e: Exception){}
        br.close()
        result = output.getValue("Hardware")
        /*
            "CPU_revision" -> "1"
            "cpu_model" -> "ARMv7 Processor rev 1 (v7l)"
            "processor" -> "3"
            "BogoMIPS" -> "48.00"
            "Revision" -> "0000"
            "CPU_variant" -> "0x0"
            "Serial" -> "e3f5bd623c144bca"
            "CPU_architecture" -> "7"
            "Hardware" -> "Rockchip RK3288 (Flattened Device Tree)"
            "CPU_part" -> "0xc0d"
            "Processor" -> "ARMv7 Processor rev 1 (v7l)"
            "CPU_implementer" -> "0x41"
         */
        return result
    }

    fun getDPI(context: Context): Int {
        try {
            return context.getResources().getDisplayMetrics().densityDpi
        } catch (e: java.lang.Exception){}
        return 0
    }
    fun checkADBWifi(): Boolean{
        val result = Terminal.sudo("getprop service.adb.tcp.port")
        return result == "5555"
    }
    fun appList(context: Context) : MutableList<App>{
        val apps = mutableListOf<App>()
        val packList = context.packageManager.getInstalledPackages(0)
        for (i in packList.indices) {
            val packInfo = packList[i]
            if (packInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                val name = packInfo.applicationInfo.loadLabel(context.packageManager).toString()
                apps.add(App(name, packInfo.packageName, packInfo.versionName))
            }
        }
        return apps
    }
    fun wifilist(context: Context): MutableList<com.lui2mi.adm.models.Wifi> {
        val wifis = mutableListOf<com.lui2mi.adm.models.Wifi>()
        val wm =  context.getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            wm.configuredNetworks.forEach {
                wifis.add(Wifi(it.SSID, it.networkId.toString()))
            }
        }
        return wifis
    }
}
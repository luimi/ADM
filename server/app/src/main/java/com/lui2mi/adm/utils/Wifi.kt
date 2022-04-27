package com.lui2mi.adm.utils

import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

class Wifi {
    companion object {
         /**
         * Returns MAC address of the given interface name.
         * @param interfaceName eth0, wlan0 or NULL=use first interface
         * @return  mac address or empty string
         */
        fun getMACAddress(interfaceName: String?): String? {
            try {
                val interfaces: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    if (interfaceName != null) {
                        if (intf.getName() != interfaceName) continue
                    }
                    val mac: ByteArray = intf.getHardwareAddress() ?: return ""
                    val buf = StringBuilder()
                    for (aMac in mac) buf.append(String.format("%02X:", aMac))
                    if (buf.length > 0) buf.deleteCharAt(buf.length - 1)
                    return buf.toString()
                }
            } catch (ignored: Exception) {
            } // for now eat exceptions
            return ""
            /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
        }

        /**
         * Get IP address from first non-localhost interface
         * @param useIPv4   true=return ipv4, false=return ipv6
         * @return  address or empty string
         */
        fun getIPAddress(useIPv4: Boolean): String {
            try {
                val interfaces: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
                    for (addr in addrs) {
                        if (!addr.isLoopbackAddress()) {
                            val sAddr: String = addr.getHostAddress()
                            val isIPv4 = sAddr.indexOf(':') < 0
                            if (useIPv4) {
                                if (isIPv4) return sAddr
                            } else {
                                if (!isIPv4) {
                                    val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                    return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                        0,
                                        delim
                                    ).toUpperCase()
                                }
                            }
                        }
                    }
                }
            } catch (ignored: Exception) {
            }
            return ""
        }
    }
}
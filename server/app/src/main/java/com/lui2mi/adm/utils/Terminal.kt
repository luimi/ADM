package com.lui2mi.adm.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.jaredrummler.ktsh.Shell
import com.lui2mi.adm.MainService

class Terminal {
    companion object {
        fun simple(command: String): String {
            val shell = Shell("sh")
            val result = shell.run(command)
            return result.stdout()
        }
        fun sudo(command: String): String{
            val shell = Shell("su")
            val result = shell.run(command)
            return result.output()
        }
    }
}
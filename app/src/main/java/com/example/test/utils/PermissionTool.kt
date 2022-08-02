package com.example.test.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionTool {

    fun isPermissionsSetGranted(context: Context, list: List<String>): Boolean {
        list.forEach { item ->
            if (ContextCompat.checkSelfPermission(context,
                    item) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    fun isPermissionGranted(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun getNotGranted(context: Context, list: List<String>): List<String> =
        mutableListOf<String>().apply {
            list.forEach { item ->
                if (ContextCompat.checkSelfPermission(context,
                        item) != PackageManager.PERMISSION_GRANTED
                ) this.add(item)
            }
        }
}
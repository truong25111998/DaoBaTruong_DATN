package com.t3h.doantotngiep2021.camera

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    @JvmStatic
    fun checkPermission(context: Context, vararg pers: String): Boolean {
        for (per in pers) {
            val isPass =
                ContextCompat.checkSelfPermission(context, per)
            if (isPass != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun requestPermission(
        activity: Activity,
        requestCode: Int,
        vararg pers: String
    ): Boolean {
        val perNeeds = mutableListOf<String>()
        for (per in pers) {
            if (checkPermission(activity, per)) {
                continue
            }

            val isPas = ActivityCompat
                .shouldShowRequestPermissionRationale(activity, per)
            if (isPas) {
                return false
            }
            perNeeds.add(per)
        }
        if (perNeeds.size > 0) {
            ActivityCompat.requestPermissions(
                activity,
                perNeeds.toTypedArray(),
                requestCode
            )
        }
        return true

    }
}
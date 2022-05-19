package com.example.android.politicalpreparedness.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class LocationPermissionsUtil {

    companion object {
        const val PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

        fun isPermissionGranted(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}
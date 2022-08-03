package com.example.test.presentation

import android.Manifest
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import com.example.test.utils.PermissionTool

private const val PERMISSION_REQUEST_CODE: Int = 10_000

abstract class BaseActivity: AppCompatActivity() {
    private val permissions: List<String> = arrayListOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val resourdisplayMetrics: DisplayMetrics by lazy { resources.displayMetrics }

    fun edgeToEdge(){
        setWindowTransparency { _, navigationBarSize ->
        }
    }

    fun removeSystemInsets(view: View, listener: OnSystemInsetsChangedListener) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val desiredBottomInset = calculateDesiredBottomInset(
                view,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetBottom,
                listener
            )
            ViewCompat.onApplyWindowInsets(
                view,
                insets.replaceSystemWindowInsets(0, 0, 0, desiredBottomInset)
            )
        }
    }

    private fun isKeyboardAppeared(bottomInset: Int) =
        bottomInset / resourdisplayMetrics.heightPixels.toDouble() > .25

    fun calculateDesiredBottomInset(
        view: View,
        topInset: Int,
        bottomInset: Int,
        listener: OnSystemInsetsChangedListener,
    ): Int {
        val hasKeyboard = isKeyboardAppeared(bottomInset)
        val desiredBottomInset = bottomInset
        listener(topInset, if (hasKeyboard) 0 else bottomInset)
        return desiredBottomInset
    }


    private fun setWindowTransparency(
        listener: OnSystemInsetsChangedListener = { _, _ -> },
    ) {
        removeSystemInsets(window.decorView, listener)
        //window.navigationBarColor = Color.TRANSPARENT
        //window.statusBarColor = Color.TRANSPARENT
    }



    override fun onStart() {
        super.onStart()
        if (!PermissionTool.isPermissionsSetGranted(this, permissions)
        ) {
            ActivityCompat.requestPermissions(this,
                PermissionTool.getNotGranted(this, permissions).toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    fun allPermissionGrandted():Boolean = PermissionTool.isPermissionsSetGranted(this, permissions)

        abstract fun onAfterRequestPermission()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }else onAfterRequestPermission()
    }



}
typealias OnSystemInsetsChangedListener =
            (statusBarSize: Int, navigationBarSize: Int) -> Unit
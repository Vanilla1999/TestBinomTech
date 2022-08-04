package com.example.test.utils

import android.content.res.Resources
import android.graphics.*
import com.example.test.R

fun getCroppedBitmap(bitmap: Bitmap, radius: Int,resources: Resources): Bitmap? {
   val diam = radius shl 1
   val targetBitmap = Bitmap.createBitmap(diam, diam, Bitmap.Config.ARGB_8888)
   val canvas = Canvas(targetBitmap)
   val paint = Paint(Paint.ANTI_ALIAS_FLAG)
   canvas.drawARGB(0, 0, 0, 0)
   paint.color = -0xbdbdbe
   canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint)
   paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
   canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, radius, radius, false), ( -radius/6).toFloat(), ( 0).toFloat(), paint)
     var paintCircle: Paint = Paint().apply {
       color =resources.getColor(R.color.blue, null)
       strokeWidth = 10f
       style = Paint.Style.STROKE
       flags = Paint.ANTI_ALIAS_FLAG
    }
    canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat()-5f, paintCircle)
   return targetBitmap
}



//paint.color = color
//paint.style = Paint.Style.STROKE
//paint.strokeWidth = 10f
//canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat()-5f, paint)
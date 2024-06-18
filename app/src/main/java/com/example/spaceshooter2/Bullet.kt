package com.example.spaceshooter2

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect


class Bullet internal constructor(res: Resources?) {
    @JvmField
    var x = 0
    @JvmField
    var y = 0
    var width: Int
    var height: Int
    @JvmField
    var bullet: Bitmap

    init {
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet)
        width = bullet.getWidth()
        height = bullet.getHeight()
        width /= 4
        height /= 4
        width = (width * GameView.screenRatioX).toInt()
        height = (height * GameView.screenRatioY).toInt()
        bullet = Bitmap.createScaledBitmap(bullet, width, height, false)
    }

    val collisionShape: Rect
        get() = Rect(x, y, x + width, y + height)
}

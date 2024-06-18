package com.example.spaceshooter2

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Background internal constructor(screenX: Int, screenY: Int, res: Resources?) {
    @JvmField
    var x = 0
    @JvmField
    var y = 0
    @JvmField
    var background: Bitmap

    init {
        background = BitmapFactory.decodeResource(res, R.drawable.background)
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false)
    }
}

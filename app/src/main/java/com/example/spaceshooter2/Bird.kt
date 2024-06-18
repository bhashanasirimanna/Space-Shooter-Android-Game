package com.example.spaceshooter2

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect


class Bird internal constructor(res: Resources?) {
    @JvmField
    var speed = 20
    @JvmField
    var wasShot = true
    @JvmField
    var x = 0
    @JvmField
    var y: Int
    @JvmField
    var width: Int
    @JvmField
    var height: Int
    var birdCounter = 1
    var bird1: Bitmap
    var bird2: Bitmap
    var bird3: Bitmap
    var bird4: Bitmap

    init {
        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1)
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2)
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3)
        bird4 = BitmapFactory.decodeResource(res, R.drawable.bird4)
        width = bird1.getWidth()
        height = bird1.getHeight()
        width /= 11
        height /= 11
        width = (width * GameView.screenRatioX).toInt()
        height = (height * GameView.screenRatioY).toInt()
        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false)
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false)
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false)
        bird4 = Bitmap.createScaledBitmap(bird4, width, height, false)
        y = -height
    }

    val bird: Bitmap
        get() {
            if (birdCounter == 1) {
                birdCounter++
                return bird1
            }
            if (birdCounter == 2) {
                birdCounter++
                return bird2
            }
            if (birdCounter == 3) {
                birdCounter++
                return bird3
            }
            birdCounter = 1
            return bird4
        }
    val collisionShape: Rect
        get() = Rect(x, y, x + width, y + height)
}

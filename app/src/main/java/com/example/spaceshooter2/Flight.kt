package com.example.spaceshooter2

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect


class Flight internal constructor(private val gameView: GameView, screenY: Int, res: Resources?) {
    @JvmField
    var toShoot = 0
    @JvmField
    var isgoingUp = false
    @JvmField
    var x: Int
    @JvmField
    var y: Int
    @JvmField
    var width: Int
    @JvmField
    var height: Int
    var wingCounter = 0
    var shootCounter = 1
    var flight1: Bitmap
    var flight2: Bitmap
    var shoot1: Bitmap
    var shoot2: Bitmap
    var shoot3: Bitmap
    var shoot4: Bitmap
    var shoot5: Bitmap
    @JvmField
    var dead: Bitmap

    init {
        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1)
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly2)
        width = flight1.getWidth()
        height = flight1.getHeight()
        width /= 4
        height /= 4
        width = (width * GameView.screenRatioX).toInt()
        height = (height * GameView.screenRatioY).toInt()
        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false)
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false)
        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1)
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2)
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3)
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4)
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5)
        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false)
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false)
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false)
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false)
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false)
        dead = BitmapFactory.decodeResource(res, R.drawable.dead)
        dead = Bitmap.createScaledBitmap(dead, width, height, false)
        y = screenY / 2
        x = (64 * GameView.screenRatioX).toInt()
    }

    val flight: Bitmap
        get() {
            if (toShoot != 0) {
                if (shootCounter == 1) {
                    shootCounter++
                    return shoot1
                }
                if (shootCounter == 2) {
                    shootCounter++
                    return shoot2
                }
                if (shootCounter == 3) {
                    shootCounter++
                    return shoot3
                }
                if (shootCounter == 4) {
                    shootCounter++
                    return shoot4
                }
                shootCounter = 1
                toShoot--
                gameView.newBullet()
                return shoot5
            }
            if (wingCounter == 0) {
                wingCounter++
                return flight1
            }
            wingCounter--
            return flight2
        }
    val collisionShape: Rect
        get() = Rect(x, y, x + width, y + height)
}

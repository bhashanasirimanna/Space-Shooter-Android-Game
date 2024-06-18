package com.example.spaceshooter2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceView
import java.util.Random

class GameView(private val activity: GameActivity, screenX: Int, screenY: Int) : SurfaceView(
    activity
), Runnable {
    private var thread: Thread? = null
    private var isPlaying = false
    private var isGameOver = false
    private val screenX: Int
    private val screenY: Int
    private var score = 0
    private val paint: Paint
    private val birds: Array<Bird?>
    private val prefs: SharedPreferences
    private val random: Random
    private var soundPool: SoundPool? = null
    private val bullets: MutableList<Bullet>
    private val sound: Int
    private val flight: Flight
    private val background1: Background
    private val background2: Background

    init {
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE)
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        } else SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        sound = soundPool!!.load(activity, R.raw.shoot, 1)
        this.screenX = screenX
        this.screenY = screenY
        screenRatioX = 1920f / screenX
        screenRatioY = 1080f / screenY
        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)
        flight = Flight(this, screenY, resources)
        bullets = ArrayList()
        background2.x = screenX
        paint = Paint()
        paint.textSize = 120f
        paint.setColor(Color.WHITE)
        birds = arrayOfNulls(4)
        for (i in 0..3) {
            val bird = Bird(resources)
            birds[i] = bird
        }
        random = Random()
    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    private fun update() {
        background1.x = (background1.x - 10 * screenRatioX).toInt()
        background2.x = (background2.x - 10 * screenRatioX).toInt()
        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX
        }
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX
        }
        if (flight.isgoingUp) flight.y = (flight.y - 30 * screenRatioY).toInt() else flight.y =
            (flight.y + 30 * screenRatioY).toInt()
        if (flight.y < 0) flight.y = 0
        if (flight.y > screenY - flight.height) flight.y = screenY - flight.height
        val trash: MutableList<Bullet> = ArrayList()
        for (bullet in bullets) {
            if (bullet.x > screenX) trash.add(bullet)
            bullet.x = (bullet.x + 50 * screenRatioX).toInt()
            for (bird in birds) {
                if (Rect.intersects(bird!!.collisionShape, bullet.collisionShape)) {
                    score++
                    bird.x = -500
                    bullet.x = screenX + 500
                    bird.wasShot = true
                }
            }
        }
        for (bullet in trash) bullets.remove(bullet)
        for (bird in birds) {
            bird!!.x -= bird!!.speed
            if (bird.x + bird.width < 0) {
                if (!bird.wasShot) {
                    isGameOver = true
                    return
                }
                val bound = (8 * screenRatioX).toInt()
                bird.speed = random.nextInt(bound).97
                if (bird.speed < 2 * screenRatioX) bird.speed = (2 * screenRatioX).toInt()
                bird.x = screenX
                bird.y = random.nextInt(screenY - bird.height)
                bird.wasShot = false
            }
            if (Rect.intersects(bird.collisionShape, flight.collisionShape)) {
                isGameOver = true
                return
            }
        }
    }

    private fun draw() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawBitmap(
                background1.background,
                background1.x.toFloat(),
                background1.y.toFloat(),
                paint
            )
            canvas.drawBitmap(
                background2.background,
                background2.x.toFloat(),
                background2.y.toFloat(),
                paint
            )
            for (bird in birds) canvas.drawBitmap(
                bird!!.bird,
                bird.x.toFloat(),
                bird.y.toFloat(),
                paint
            )
            canvas.drawText(score.toString() + "", screenX / 2f, 200f, paint)
            if (isGameOver) {
                isPlaying = false
                canvas.drawBitmap(flight.dead, flight.x.toFloat(), flight.y.toFloat(), paint)
                holder.unlockCanvasAndPost(canvas)
                saveIfHighScore()
                waitBeforeExiting()
                return
            }
            canvas.drawBitmap(flight.flight, flight.x.toFloat(), flight.y.toFloat(), paint)
            for (bullet in bullets) canvas.drawBitmap(
                bullet.bullet,
                bullet.x.toFloat(),
                bullet.y.toFloat(),
                paint
            )
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun waitBeforeExiting() {
        try {
            Thread.sleep(3000)
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun saveIfHighScore() {
        if (prefs.getInt("highscore", 0) < score) {
            val editor = prefs.edit()
            editor.putInt("highscore", score)
            editor.apply()
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(17)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread!!.start()
    }

    fun puse() {
        try {
            isPlaying = false
            thread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (event.x < screenX / 2) {
                flight.isgoingUp = true
            }

            MotionEvent.ACTION_UP -> {
                flight.isgoingUp = false
                if (event.x > screenX / 2) flight.toShoot++
            }
        }
        return true
    }

    fun newBullet() {
        if (!prefs.getBoolean("isMute", false)) soundPool!!.play(sound, 1f, 1f, 0, 0, 1f)
        val bullet = Bullet(resources)
        bullet.x = flight.x + flight.width
        bullet.y = flight.y + flight.height / 2
        bullets.add(bullet)
    }

    companion object {
        var screenRatioX: Float = 0f
        var screenRatioY: Float = 0f
    }
}

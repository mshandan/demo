package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gifImageView: GifImageView
    private lateinit var gifDrawable: GifDrawable

    private val frameCount:Int = 58

    private var isShow = true
    private var cp = 0
    private var tp = 0
    private var delay = 300L
    private lateinit var mHandle: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    fun initViews(){
        initHandle()
        gifImageView = findViewById(R.id.gifImageView)
        gifImageView.setImageResource(R.mipmap.luori)
        gifDrawable = gifImageView.drawable as GifDrawable
        gifDrawable.stop()
        mHandle.sendEmptyMessageDelayed(0x101, delay)
        Log.e("gifDrawable  ",gifDrawable.numberOfFrames.toString())
    }


    private fun initHandle() {
        mHandle = Handler(object : Handler.Callback {
            override fun handleMessage(msg: Message): Boolean {
                msg?.apply {
                    when (what) {
                        0x100 -> {
                            when {
                                cp < tp -> {
                                    cp++
                                    gifDrawable.seekToFrame(cp)
                                    gifDrawable.stop()
                                    mHandle.sendEmptyMessageDelayed(0x100, delay)
                                }
                                cp == tp -> {

                                }
                                else -> {
                                    cp--
                                    gifDrawable.seekToFrame(cp)
                                    gifDrawable.stop()
                                    mHandle.sendEmptyMessageDelayed(0x100, delay)
                                }
                            }
                        }
                        0x101->{
                            seekGif(frameCount - Random.nextInt(10,40), isEnlarge = true)
                            mHandle.sendEmptyMessageDelayed(0x101, delay)

                        }

                    }
                }
                return false
            }
        })
    }

    //荷花入口
    private var mLastTp = -1
    fun seekGif(tagetFrameIndex: Int, delayTime: Long = 300, isEnlarge: Boolean = false) {
        if (isShow) {
            if (isEnlarge) {
                if (tagetFrameIndex == mLastTp) {//防止回溯到原来的帧数。相等说明已经在当前的帧数，荷花不用动.
                    return
                }
                val enlargeData =
                    if (mLastTp != -1) {
                        (tagetFrameIndex - mLastTp) * 2 + tagetFrameIndex
                    } else {
                        tagetFrameIndex
                    }
                tp = when {
                    enlargeData < 0 -> 0
                    enlargeData > frameCount -> frameCount
                    else -> enlargeData
                }
                mLastTp = tagetFrameIndex
            } else {
                tp = when {
                    tagetFrameIndex < 0 -> 0
                    tagetFrameIndex > frameCount -> frameCount
                    else -> tagetFrameIndex
                }
            }
            delay = delayTime
            mHandle.removeCallbacksAndMessages(null)
            mHandle.sendEmptyMessageDelayed(0x100, delay)
        }
    }
}


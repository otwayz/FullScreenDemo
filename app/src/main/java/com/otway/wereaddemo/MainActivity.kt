package com.otway.wereaddemo

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var fullScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        change_mode.setOnClickListener {
            if (fullScreen) {
                changeToNotFullScreen()
                fullScreen = false
            } else {
                changeToFullScreen()
                fullScreen = true
            }
        }


        window.decorView.setOnSystemUiVisibilityChangeListener {
            val visible = it and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0

            // you should over the anim height
            // add the navigator height and self height
            bottom_control.animate()
                    .alpha((if (visible) 1 else 0).toFloat())
                    .translationY((if (visible) 0 else
                        (bottom_control.height + getNavigationBarHeight(this))).toFloat())
        }
    }

    private fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    override fun onResume() {
        super.onResume()
        changeToFullScreen()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.immersive -> {
                startActivity(Intent(MainActivity@this,ImmersiveActivity::class.java))
                true
            }

            R.id.immersiveSticky -> {
                startActivity(Intent(MainActivity@this,ImmersiveSticky2Activity::class.java))
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun changeToFullScreen() {
        var uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        /**
         *  the WeRead has this , if you meet some problems with this block abandon , please release this block
         */

        /*if (window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION != View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
            changeToNotFullScreen()
        }*/

        // if your status bar is light mode ,you should execute otherwise remove the if block
        if (Build.VERSION.SDK_INT >= 23) {
            uiOptions = lightStatusbar(window, uiOptions, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        window.decorView.systemUiVisibility = uiOptions
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun changeToNotFullScreen() {
        var uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        // if your status bar is light mode ,you should execute otherwise remove the if block
        if (Build.VERSION.SDK_INT >= 23) {
            uiOptions = lightStatusbar(window, uiOptions, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        window.decorView.systemUiVisibility = uiOptions
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun lightStatusbar(paramWindow: Window, paramInt1: Int, paramInt2: Int): Int {
        var i = paramInt1
        if (paramWindow.decorView.systemUiVisibility and paramInt2 == paramInt2) {
            i = paramInt1 or paramInt2
        }
        return i
    }
}

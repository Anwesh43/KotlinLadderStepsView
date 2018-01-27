package ui.anwesome.com.kotlinladderstepsview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.ladderstepsview.LadderStepsView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LadderStepsView.create(this)
    }
}

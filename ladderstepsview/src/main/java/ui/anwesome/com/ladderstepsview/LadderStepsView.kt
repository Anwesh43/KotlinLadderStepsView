package ui.anwesome.com.ladderstepsview

/**
 * Created by anweshmishra on 27/01/18.
 */
import android.graphics.*
import android.content.*
import android.view.*
class LadderStepsView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}
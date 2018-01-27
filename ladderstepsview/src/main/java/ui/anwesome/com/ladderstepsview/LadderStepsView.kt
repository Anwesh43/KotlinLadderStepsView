package ui.anwesome.com.ladderstepsview

/**
 * Created by anweshmishra on 27/01/18.
 */
import android.graphics.*
import android.content.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

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
    data class Animator(var view:LadderStepsView,var animated:Boolean = false) {
        fun animate(updatecb:()->Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class LadderStep(var i:Int,var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.drawLine(x,y-size/2,x,y+size/2,paint)
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
    data class Ladder(var w:Float,var h:Float) {
        var steps:ConcurrentLinkedQueue<LadderStep> = ConcurrentLinkedQueue()
        fun draw(canvas:Canvas,paint:Paint) {
            steps.forEach {
                it.draw(canvas,paint)
            }
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
}
fun ConcurrentLinkedQueue<LadderStepsView.LadderStep>.at(i:Int):LadderStepsView.LadderStep? {
    var j = 0
    forEach {
        if(i == j) {
            return it
        }
        j++
    }
    return null
}
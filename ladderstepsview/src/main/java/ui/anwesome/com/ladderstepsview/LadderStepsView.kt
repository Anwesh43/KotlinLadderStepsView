package ui.anwesome.com.ladderstepsview

/**
 * Created by anweshmishra on 27/01/18.
 */
import android.app.Activity
import android.graphics.*
import android.content.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

class LadderStepsView(ctx:Context,var n:Int=10):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
        val state = LadderStepState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.drawLine(x-(size/2)*state.scale,y,x+(size/2)*state.scale,y,paint)
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class LadderStepState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1-2*scale
                startcb()
            }
        }
    }
    data class Ladder(var w:Float,var h:Float,var n:Int,var y_gap:Float = 0f) {
        var steps:ConcurrentLinkedQueue<LadderStep> = ConcurrentLinkedQueue()
        val state = LadderState(n)
        init {
            if(n > 0) {
                y_gap = (9 * h / 10) / (n+1)
                var y = 19*h/20 - y_gap
                for (i in 0..n - 1) {
                    steps.add(LadderStep(i,w/2,y,w/5))
                    y -= y_gap
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = Math.min(w,h)/45
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.parseColor("#1A237E")
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w/2-w/10+w/5*i,h/20)
                canvas.drawLine(0f,0f,0f,0.9f*h,paint)
                canvas.restore()
            }
            steps.forEach {
                it.draw(canvas,paint)
            }
            state.executeCB {
                val diff = (w / 2) / (n)
                val x_offset = diff * it
                val scale = steps.at(it)?.state?.scale ?: 0f
                for(j in 0..1) {
                    canvas.save()
                    canvas.translate(w / 2, h / 40+(19*h/20)*j)
                    canvas.drawLine(-x_offset - diff * scale, 0f, x_offset + diff * scale, 0f, paint)
                    canvas.restore()
                }
                paint.color = Color.parseColor("#EEEEEE")
                canvas.drawCircle(w/2,19*h/20-y_gap*it-y_gap*scale,y_gap/5,paint)
            }
        }
        fun update(stopcb:(Float)->Unit) {
            state.executeCB {
                steps.at(it)?.update{scale ->
                    state.incrementCounter()
                    stopcb(scale)
                }
            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.executeCB {
                steps.at(it)?.startUpdating(startcb)
            }
        }
    }
    data class LadderState(var n:Int,var j:Int = 0,var dir:Int = 1) {
        fun incrementCounter() {
            j+=dir
            if(j == n || j == -1) {
                dir*=-1
                j+=dir
            }
        }
        fun executeCB(cb:(Int)->Unit) {
            cb(j)
        }
    }
    data class Renderer(var view:LadderStepsView,var time:Int = 0) {
        val animator = Animator(view)
        var ladder:Ladder?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                ladder = Ladder(w,h,view.n)
            }
            ladder?.draw(canvas,paint)
            time++
            animator.animate {
                ladder?.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            ladder?.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity:Activity):LadderStepsView {
            val view = LadderStepsView(activity)
            activity.setContentView(view)
            return view
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
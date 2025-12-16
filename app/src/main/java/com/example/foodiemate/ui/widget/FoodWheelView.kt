package com.example.foodiemate.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class FoodWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }

    private val arcRect = RectF()

    private var items: List<String> = emptyList()

    private var rotationDeg: Float = 0f

    private val colors = intArrayOf(
        0xFFB3541E.toInt(),
        0xFF2A9D8F.toInt(),
        0xFFF4A261.toInt(),
        0xFFE76F51.toInt(),
        0xFF264653.toInt(),
        0xFF6D597A.toInt(),
        0xFF355070.toInt(),
        0xFF84A59D.toInt()
    )

    fun setItems(newItems: List<String>) {
        items = newItems
        invalidate()
    }

    fun spin(onResult: (String) -> Unit) {
        if (items.isEmpty()) return

        val start = rotationDeg
        val extra = (360f * 6f) + (0..359).random()
        val end = start + extra

        ValueAnimator.ofFloat(start, end).apply {
            duration = 2600L
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                rotationDeg = it.animatedValue as Float
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    val index = calculateSelectedIndex(rotationDeg)
                    onResult(items[index])
                }
            })
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val size = min(w, h)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (items.isEmpty()) return

        val size = min(width, height).toFloat()
        val padding = size * 0.06f
        val cx = size / 2f
        val cy = size / 2f
        val radius = (size / 2f) - padding

        arcRect.set(cx - radius, cy - radius, cx + radius, cy + radius)

        val sweep = 360f / items.size

        canvas.save()
        canvas.rotate(rotationDeg, cx, cy)

        for (i in items.indices) {
            val startAngle = i * sweep
            arcPaint.color = colors[i % colors.size]
            canvas.drawArc(arcRect, startAngle, sweep, true, arcPaint)

            drawLabel(canvas, cx, cy, radius, startAngle + sweep / 2f, items[i])
        }

        canvas.restore()

        val pointerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFFFFF.toInt()
            style = Paint.Style.FILL
        }
        val pW = size * 0.035f
        val pH = size * 0.07f
        val px = cx
        val py = padding * 0.6f
        canvas.drawRoundRect(
            px - pW,
            py,
            px + pW,
            py + pH,
            pW,
            pW,
            pointerPaint
        )
    }

    private fun drawLabel(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        angleDeg: Float,
        label: String
    ) {
        val angleRad = Math.toRadians(angleDeg.toDouble())
        val r = radius * 0.62f
        val x = (cx + (r * cos(angleRad))).toFloat()
        val y = (cy + (r * sin(angleRad))).toFloat()

        val text = if (label.length > 6) label.take(6) else label
        canvas.save()
        canvas.rotate(angleDeg, x, y)
        canvas.drawText(text, x, y + (textPaint.textSize / 3f), textPaint)
        canvas.restore()
    }

    private fun calculateSelectedIndex(currentRotation: Float): Int {
        val n = items.size
        val sweep = 360f / n

        val pointerAngle = 270f
        val normalizedRotation = ((currentRotation % 360f) + 360f) % 360f
        val relative = (pointerAngle - normalizedRotation + 360f) % 360f
        val index = (relative / sweep).toInt().coerceIn(0, n - 1)

        return index
    }
}

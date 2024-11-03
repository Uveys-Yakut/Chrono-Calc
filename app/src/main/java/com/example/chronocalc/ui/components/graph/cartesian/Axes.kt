package com.example.chronocalc.ui.components.graph.cartesian

import android.graphics.Paint
import android.text.TextPaint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chronocalc.ui.theme.Gray800
import com.example.chronocalc.ui.theme.White
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private enum class AxesNames { X, Y }
private enum class AxesDirection { POSITIVE, NEGATIVE }

private data class AxesItemsData(
    val centerPos: Float,
    val axesName: AxesNames,
    val fixedAxesPos: Float,
    val itemLimitWithSize: Float
)

fun DrawScope.cartesianAxes(intervalGrid: Float) {
    val width = size.width
    val height = size.height
    val arrowHeadLength = 20f

    val center = Offset(width / 2, height / 2)

    val axesOffsets = listOf(
        Pair(Offset(0f, center.y), Offset(width, center.y)), // X Axis
        Pair(Offset(center.x, height), Offset(center.x, 0f)) // Y Axis
    )

    for ((start, end) in axesOffsets) {
        axisLineCreator(start, end, arrowHeadLength, intervalGrid)
    }
}

private fun DrawScope.axisLineCreator(startPos: Offset, endPos: Offset, arrowHeadLength: Float, intervalGrid: Float) {
    drawLine(
        start = startPos,
        end = endPos,
        color = CartesianDefinitions.AXIS_LINE_COLOR,
        strokeWidth = CartesianDefinitions.AXIS_STROKE_WIDTH
    )
    angelArrowHead(
        startPos,
        endPos,
        CartesianDefinitions.AXIS_LINE_COLOR,
        CartesianDefinitions.AXIS_STROKE_WIDTH,
        arrowHeadLength
    )
    axesTickLabels(intervalGrid)
}
private fun DrawScope.angelArrowHead(
    start: Offset,
    end: Offset,
    color: Color,
    strokeWidth: Float,
    arrowAngelHeadLength: Float = 20f
) {
    val angleOffsets = listOf(-Math.PI / 4, Math.PI / 4)

    val angle = atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble()).toFloat()

    angleOffsets.forEach { offset ->
        val arrowPoint = calculateAngelArrowPoint(end, arrowAngelHeadLength, angle + offset)
        drawLine(color = color, start = end, end = arrowPoint, strokeWidth = strokeWidth)
    }
}
private fun calculateAngelArrowPoint(end: Offset, length: Float, angle: Double): Offset {
    return Offset(
        x = end.x - length * cos(angle).toFloat(),
        y = end.y - length * sin(angle).toFloat()
    )
}

private fun DrawScope.axesTickLabels(intervalGrid: Float) {
    val width = size.width
    val height = size.height
    val centerX = width/2
    val centerY = height/2
    val intervalTickLabel = intervalGrid * 5f
    val axesTickLabelData = listOf(
        AxesItemsData(centerX, AxesNames.X, centerY, width),
        AxesItemsData(centerY, AxesNames.Y, centerX, height)
    )
    for (itemAxesLabelData in axesTickLabelData) {
        for (direction in AxesDirection.entries) {
            var pos = itemAxesLabelData.centerPos
            var counter = 0f
            while ((direction == AxesDirection.POSITIVE && pos <= itemAxesLabelData.itemLimitWithSize) ||
                (direction == AxesDirection.NEGATIVE && pos >= 0f)) {
                if (counter != 0f) {
                    drawTickLabels(
                        text = if (direction == AxesDirection.POSITIVE) "${counter.toInt()}" else "-${counter.toInt()}",
                        pos = if (itemAxesLabelData.axesName == AxesNames.X) Offset(pos, itemAxesLabelData.fixedAxesPos) else Offset(itemAxesLabelData.fixedAxesPos, pos),
                        color = Gray800,
                        textSize = 12.dp,
                        strokeColor = White,
                        strokeWidth = 8f,
                        marginTop = if (itemAxesLabelData.axesName == AxesNames.X) 35f else 0f,
                        marginRight = if (itemAxesLabelData.axesName == AxesNames.Y) 35f else 0f,
                        marginBottom = 2f
                    )
                }
                pos += if (direction == AxesDirection.POSITIVE) intervalTickLabel else -intervalTickLabel
                counter++
            }
        }
    }
}

private fun DrawScope.drawTickLabels(
    text: String,
    pos: Offset,
    color: Color,
    textSize: Dp,
    strokeColor: Color = Color.Transparent,
    strokeWidth: Float = 0f,
    marginLeft: Float = 0f,
    marginRight: Float = 0f,
    marginTop: Float = 0f,
    marginBottom: Float = 0f
) {
    val textPaint = TextPaint().apply {
        this.color = color.toArgb()
        this.textSize = textSize.toPx()
        if (strokeWidth > 0) {
            this.style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
            this.strokeCap = Paint.Cap.ROUND
            this.strokeJoin = Paint.Join.ROUND
            this.color = strokeColor.toArgb()
        }
    }

    val fillPaint = TextPaint().apply {
        this.color = color.toArgb()
        this.textSize = textSize.toPx()
    }
    val textWidth = textPaint.measureText(text)
    val textBounds = android.graphics.Rect()
    textPaint.getTextBounds(text, 0, text.length, textBounds)
    val textHeight = textBounds.height()

    val adjustedX = pos.x - (textWidth / 2) + marginLeft - marginRight
    val adjustedY = pos.y + (textHeight / 2) + marginTop - marginBottom

    drawContext.canvas.nativeCanvas.drawText(
        text,
        adjustedX,
        adjustedY,
        textPaint
    )

    drawContext.canvas.nativeCanvas.drawText(
        text,
        adjustedX,
        adjustedY,
        fillPaint
    )
}
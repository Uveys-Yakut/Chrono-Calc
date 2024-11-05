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

private data class AxesLabelItemsData(
    val centerPos: Float,
    val axesName: AxesNames,
    val fixedAxesLabelPos: Float,
    val itemLimitWithSize: Float
)

fun DrawScope.cartesianAxes(intervalGrid: Float, dragOffset: Offset) {
    val width = size.width
    val height = size.height
    val arrowHeadLength = 20f
    val center = Offset(
        x = width / 2 + dragOffset.x,
        y= height / 2 + dragOffset.y
    )
    val axesOffsets = listOf(
        Pair(Offset(0f, center.y), Offset(width, center.y)), // X Axis
        Pair(Offset(center.x, height), Offset(center.x, 0f)) // Y Axis
    )

    for ((start, end) in axesOffsets) {
        axisLineCreator(start, end, arrowHeadLength, intervalGrid, dragOffset)
    }
}

private fun DrawScope.axisLineCreator(
    startPos: Offset,
    endPos: Offset,
    arrowHeadLength: Float,
    intervalGrid: Float,
    dragOffset: Offset
) {
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
    axesTickLabels(intervalGrid, dragOffset)
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

private fun DrawScope.axesTickLabels(intervalGrid: Float, dragOffset: Offset) {
    val width = size.width
    val height = size.height
    val centerX = width/2  + dragOffset.x
    val centerY = height/2 + dragOffset.y
    val intervalTickLabel = intervalGrid * 5f
    val axesTickLabelData = listOf(
        AxesLabelItemsData(centerX, AxesNames.X, centerY, width),
        AxesLabelItemsData(centerY, AxesNames.Y, centerX, height)
    )
    for (itemAxesLabelData in axesTickLabelData) {
        axesTickLabelsCreator(itemAxesLabelData, intervalTickLabel, intervalGrid)
    }
    drawZeroTickLabel(Offset(centerX, centerY))
}
private fun DrawScope.axesTickLabelsCreator(itemAxesLabelData: AxesLabelItemsData, intervalTickLabel: Float, intervalGrid: Float) {
    var breaker = 0f
    for (direction in AxesDirection.entries) {
        var pos = itemAxesLabelData.centerPos
        var counter = 0f

        val isXAxis = itemAxesLabelData.axesName == AxesNames.X

        val fixedAxesLabelPos = when {
            (isXAxis && itemAxesLabelData.fixedAxesLabelPos < 0f) || (!isXAxis && itemAxesLabelData.fixedAxesLabelPos < 35f) ->
                if (isXAxis) 0f else intervalGrid
            (isXAxis && itemAxesLabelData.fixedAxesLabelPos > size.height) || (!isXAxis && itemAxesLabelData.fixedAxesLabelPos > size.width) ->
                if (isXAxis) size.height - intervalGrid else size.width
            else -> itemAxesLabelData.fixedAxesLabelPos
        }

        // This logic addresses the coordinate system difference between a Cartesian plane and typical canvas systems.
        // In a Cartesian coordinate system, the Y-axis values increase upward. On canvas systems (e.g., Android or HTML Canvas),
        // however, the Y-axis increases downward, with the origin (0,0) at the top-left. To align the behavior of the Cartesian
        // system on a canvas, we adjust movement on the Y-axis by reversing addition and subtraction. Specifically, moving "up"
        // requires subtracting from the Y position, while moving "down" requires adding. This conditional setup dynamically
        // applies the correct direction adjustments for X and Y-axis values.
        val limitCheck: (Float) -> Boolean = {
            when {
                isXAxis && direction == AxesDirection.POSITIVE -> it <= itemAxesLabelData.itemLimitWithSize
                isXAxis && direction == AxesDirection.NEGATIVE -> it >= 0f
                !isXAxis && direction == AxesDirection.POSITIVE -> it >= 0f
                else -> it <= itemAxesLabelData.itemLimitWithSize
            }
        }

        val updatePos: (Float) -> Float = {
            when {
                isXAxis && direction == AxesDirection.POSITIVE -> it + intervalTickLabel
                isXAxis && direction == AxesDirection.NEGATIVE -> it - intervalTickLabel
                !isXAxis && direction == AxesDirection.POSITIVE -> it - intervalTickLabel
                else -> it + intervalTickLabel
            }
        }

        while (limitCheck(pos)) {
            if (counter != 0f) {
                val labelText = if (direction == AxesDirection.POSITIVE) "${counter.toInt()}" else "-${counter.toInt()}"
                val labelPos = if (isXAxis) Offset(pos, fixedAxesLabelPos) else Offset(fixedAxesLabelPos, pos)

                drawTickLabel(
                    text = labelText,
                    pos = labelPos,
                    color = Gray800,
                    textSize = 12.dp,
                    strokeColor = White,
                    strokeWidth = 8f,
                    marginTop = if (isXAxis) 35f else 0f,
                    marginRight = if (!isXAxis) 35f else 0f,
                    marginBottom = 2f
                )
            }
            pos = updatePos(pos)
            counter++
        }
    }
}
private fun DrawScope.drawZeroTickLabel(centerPos: Offset) {
    drawTickLabel(
        text = "0",
        pos = Offset(centerPos.x - 35f, centerPos.y + 35f),
        color = Gray800,
        textSize = 12.dp,
        strokeColor = White,
        strokeWidth = 8f,
        marginBottom = 2f
    )
}

private fun DrawScope.drawTickLabel(
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
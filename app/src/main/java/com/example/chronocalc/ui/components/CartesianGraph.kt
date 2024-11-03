package com.example.chronocalc.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.chronocalc.ui.theme.Gray300
import com.example.chronocalc.ui.theme.Gray400
import com.example.chronocalc.ui.theme.Gray900
import com.example.chronocalc.ui.theme.White

class CartesianGraph {
    companion object CartesianConstant {
        val AXIS_LINE_COLOR = Gray900
        val GRID_LINE_COLOR = Gray300
        val GRID_MAIN_LINE_COLOR = Gray400
        const val AXIS_STROKE_WIDTH = 2f
    }
    enum class GridLineType { VERTICAL, HORIZONTAL }
    enum class DirectionLine { POSITIVE, NEGATIVE }

    @Composable
    fun CartesianCanvas() {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(White)
        ) {
            cartesianGrid()
            axisLines()
        }
    }

    private fun DrawScope.axisLines() {
        val width = size.width
        val height = size.height
        // X line
        drawLine(
            start = Offset(0f, height/2),
            end = Offset(width, height/2),
            color = AXIS_LINE_COLOR,
            strokeWidth = AXIS_STROKE_WIDTH
        )
        // Y line
        drawLine(
            start = Offset(width/2, 0f),
            end = Offset(width/2, height),
            color = AXIS_LINE_COLOR,
            strokeWidth = AXIS_STROKE_WIDTH
        )
    }

    private fun DrawScope.cartesianGrid() {
        val width = size.width
        val height = size.height
        val intervalGrid = 50f

        for (type in GridLineType.entries) {
            gridLinesCreator(width, height, intervalGrid, type)
        }
    }
    private fun DrawScope.gridLinesCreator(
        width: Float,
        height: Float,
        intervalGrid: Float,
        type: GridLineType
    ) {
        val centerX = width/2
        val centerY = height/2
        val limit = if (type == GridLineType.VERTICAL) width else height
        val pos = if (type == GridLineType.VERTICAL) centerX else centerY

        for (direction in DirectionLine.entries) {
            drawGridLine(width, height, pos, limit, intervalGrid, type, direction)
        }
    }
    private fun DrawScope.drawGridLine(
        width: Float,
        height: Float,
        startPos: Float,
        limit: Float,
        intervalGrid: Float,
        type: GridLineType,
        directionLine: DirectionLine
    ) {
        var pos = startPos
        var counter = 0f

        while ((directionLine == DirectionLine.POSITIVE && pos <= limit) ||
            (directionLine == DirectionLine.NEGATIVE && pos >= 0)) {
            val lineStartOffset = if (type == GridLineType.VERTICAL) Offset(pos, 0f) else Offset(0f, pos)
            val lineEndOffset = if (type == GridLineType.VERTICAL) Offset(pos, height) else Offset(width, pos)
            val lineColor = if (counter % 5 == 0f) GRID_MAIN_LINE_COLOR else GRID_LINE_COLOR
            drawLine(
                start = lineStartOffset,
                end = lineEndOffset,
                color = lineColor,
                strokeWidth = AXIS_STROKE_WIDTH
            )
            pos += if (directionLine == DirectionLine.POSITIVE) intervalGrid else -intervalGrid
            counter++
        }
    }
}
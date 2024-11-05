package com.example.chronocalc.ui.components.graph.cartesian

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.cartesianGrid(intervalGrid: Float = 50f, dragOffset: Offset) {
    val width = size.width
    val height = size.height
    for (type in GridLineType.entries) {
        gridLinesCreator(width, height, intervalGrid, type, dragOffset)
    }
}
private fun DrawScope.gridLinesCreator(
    width: Float,
    height: Float,
    intervalGrid: Float,
    type: GridLineType,
    dragOffset: Offset
) {
    val centerX = width/2 + dragOffset.x
    val centerY = height/2 + dragOffset.y
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
        val lineColor = if (counter % 5 == 0f) CartesianDefinitions.GRID_MAIN_LINE_COLOR else CartesianDefinitions.GRID_LINE_COLOR
        drawLine(
            start = lineStartOffset,
            end = lineEndOffset,
            color = lineColor,
            strokeWidth = CartesianDefinitions.AXIS_STROKE_WIDTH
        )
        pos += if (directionLine == DirectionLine.POSITIVE) intervalGrid else -intervalGrid
        counter++
    }
}
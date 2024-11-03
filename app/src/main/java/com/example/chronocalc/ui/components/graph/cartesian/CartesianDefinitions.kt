package com.example.chronocalc.ui.components.graph.cartesian

import com.example.chronocalc.ui.theme.Gray300
import com.example.chronocalc.ui.theme.Gray400
import com.example.chronocalc.ui.theme.Gray900

object CartesianDefinitions {
    val AXIS_LINE_COLOR = Gray900
    val GRID_LINE_COLOR = Gray300
    val GRID_MAIN_LINE_COLOR = Gray400
    const val AXIS_STROKE_WIDTH = 2.2f
}
enum class GridLineType { VERTICAL, HORIZONTAL }
enum class DirectionLine { POSITIVE, NEGATIVE }
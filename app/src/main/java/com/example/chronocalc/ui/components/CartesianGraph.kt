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
    object CartesianConstant {
        val AXIS_LINE_COLOR = Gray900
        val GRID_LINE_COLOR = Gray300
        val GRID_MAIN_LINE_COLOR = Gray400
        const val AXIS_STROKE_WIDTH = 2f
    }

    @Composable
    fun CartesianCanvas() {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(White)
        ) {
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
            color = CartesianConstant.AXIS_LINE_COLOR,
            strokeWidth = CartesianConstant.AXIS_STROKE_WIDTH
        )
        // Y line
        drawLine(
            start = Offset(width/2, 0f),
            end = Offset(width/2, height),
            color = CartesianConstant.AXIS_LINE_COLOR,
            strokeWidth = CartesianConstant.AXIS_STROKE_WIDTH
        )
    }
}
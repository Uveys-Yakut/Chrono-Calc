package com.example.chronocalc.ui.components.graph.cartesian

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import com.example.chronocalc.ui.theme.White

@Composable
fun CartesianCanvas() {
    val intervalGrid = 50f
    var offset by remember { mutableStateOf(Offset.Zero) }
    Box(modifier = Modifier
        .fillMaxSize()
        .clip(RectangleShape)
    ){
        Canvas(modifier = Modifier
            .matchParentSize()
            .background(White)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset = Offset(
                            offset.x + dragAmount.x,
                            offset.y + dragAmount.y
                        )
                    }
                )
            }
        ) {
            dragCartesian(offset, intervalGrid)
        }
    }
}

private fun DrawScope.dragCartesian(dragOffset: Offset, intervalGrid: Float) {
    cartesianGrid(intervalGrid, dragOffset)
    cartesianAxes(intervalGrid, dragOffset)
}
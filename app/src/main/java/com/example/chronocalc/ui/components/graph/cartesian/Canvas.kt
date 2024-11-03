package com.example.chronocalc.ui.components.graph.cartesian

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chronocalc.ui.theme.White

@Composable
fun CartesianCanvas() {
    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(White)
    ) {
        val intervalGrid = 50f

        cartesianGrid(intervalGrid)
        cartesianAxes(intervalGrid)
    }
}
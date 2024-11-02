package com.example.chronocalc.ui.screens

import androidx.compose.runtime.Composable
import com.example.chronocalc.ui.components.CartesianGraph

@Composable
fun HomeScreen() {
    CartesianGraph().apply {
        CartesianCanvas()
    }
}
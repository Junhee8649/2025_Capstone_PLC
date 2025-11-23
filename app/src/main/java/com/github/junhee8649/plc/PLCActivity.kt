package com.github.junhee8649.plc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.junhee8649.plc.ui.theme.PLCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PLCActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            PLCTheme {
                PLCApp(windowSizeClass)
            }
        }
    }
}

@Composable
fun PLCApp(windowSizeClass: WindowSizeClass) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        PLCNavGraph(
            windowSizeClass = windowSizeClass,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
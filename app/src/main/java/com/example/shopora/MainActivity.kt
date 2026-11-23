package com.example.shopora

/**
 * @author Utkarsh Srivastava
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shopora.ui.ShoporaApp
import com.example.shopora.ui.theme.ShoporaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoporaTheme {
                ShoporaApp()
            }
        }
    }
}

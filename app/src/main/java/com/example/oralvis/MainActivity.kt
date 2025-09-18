package com.example.oralvis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.oralvis.ui.navigation.AppNavHost
import com.example.oralvis.ui.theme.OralvisTheme
import com.example.oralvis.viewmodel.SessionViewModel
import com.example.oralvis.viewmodel.SessionViewModelFactory

class MainActivity : ComponentActivity() {

    private val vm: SessionViewModel by viewModels {
        val repo = (application as OralVisApp).repository
        SessionViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OralvisTheme {
                AppNavHost(vm = vm)
            }
        }
    }
}

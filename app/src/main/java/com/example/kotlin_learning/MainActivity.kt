package com.example.kotlin_learning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlin_learning.viewModel.SetupNavgraph
import com.example.kotlin_learning.ui.theme.KotlinLearningTheme
import com.example.kotlin_learning.viewModel.MainViewModel

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
        }
        setContent {
            KotlinLearningTheme {
                navController = rememberNavController()
                SetupNavgraph(navController = navController)
            }
        }
    }
}

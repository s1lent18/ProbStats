package com.example.kotlin_learning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlin_learning.viewModel.SetupNavgraph
import com.example.kotlin_learning.ui.theme.KotlinLearningTheme
import com.example.kotlin_learning.viewModel.AuthViewModel

class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KotlinLearningTheme {
                navController = rememberNavController()
                SetupNavgraph(navController = navController)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        authViewModel.signout()
    }
}

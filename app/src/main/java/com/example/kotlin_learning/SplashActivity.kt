package com.example.kotlin_learning

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_learning.ui.theme.KotlinLearningTheme
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.viewModel.AuthViewModel
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinLearningTheme {
                SplashScreen {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish() // Make sure to finish the splash activity so it's removed from the back stack
                }
            }
        }
    }

    @Composable
    private fun SplashScreen(onSplashFinished: () -> Unit) {
        val alpha = remember {
            Animatable(0f)
        }
        val authViewModel: AuthViewModel = viewModel()
        val isLoggedIn = authViewModel.loggedin.observeAsState(initial = false).value

        LaunchedEffect(isLoggedIn) {
            if (isLoggedIn) {
                authViewModel.signout()
            }
        }

        LaunchedEffect(Unit) {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(1500)
            )
            delay(2000)
            onSplashFinished()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xFF121212)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.prob_stats),
                contentDescription = "Icon",
                tint = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}
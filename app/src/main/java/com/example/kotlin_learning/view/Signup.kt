package com.example.kotlin_learning.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.viewModel.rWindowInfo
import com.example.kotlin_learning.data.request.Users
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor


@Composable
fun SignupButton(
    iconId: Int,
    contentDescription: String,
    onClick: () -> Unit
) {

    Button(
        modifier = Modifier.fillMaxWidth(fraction = 0.8f),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = iconId),
                contentDescription = contentDescription,
                tint = if(isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = contentDescription,
                style = TextStyle(fontSize = 15.sp)
            )
        }
    }
}

@Composable
fun Signup(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    Surface {
        val windowInfo = rWindowInfo()
        val signedUp by authViewModel.loggedin.observeAsState(initial = false)
        val errorMessage by authViewModel.errorMessage.observeAsState()
        val showDialog = remember { mutableStateOf(false) }
        val showSuccessDialog = remember { mutableStateOf(false) }

        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                showDialog.value = true
            }
        }

        if (showDialog.value) {
            DialogBox(
                title = "Error",
                text = "Email Already In Use",
                setShowDialog = showDialog,
                onClick = { showDialog.value = false }
            )
            authViewModel.resetErrorMessage()
        }

        if (signedUp) {
            showSuccessDialog.value = true
        }

        if (showSuccessDialog.value) {
            DialogBox(
                title = "Successful",
                text = "Press Ok to Login",
                setShowDialog = showSuccessDialog,
                onClick = {
                    showSuccessDialog.value = false
                    navController.navigate(route = Screen.Login.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                }
            )
        }

        when (windowInfo.screenWidthInfo) {
            is WindowInfo.WindowType.Compact -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    val (email, setEmail) = remember { mutableStateOf("") }
                    val (password, setpassword) = remember { mutableStateOf("") }
                    val (username, setusername) = remember { mutableStateOf("") }
                    val image = if (isSystemInDarkTheme()) R.drawable.shape1 else R.drawable.shape

                    Box (
                        contentAlignment = Alignment.TopCenter
                    ){
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(fraction = 0.46f),
                            painter = painterResource(id = image),
                            contentDescription = "Facebook Logo",
                            contentScale = ContentScale.FillBounds
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 70.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(120.dp),
                                painter = painterResource(id = R.drawable.prob_stats),
                                contentDescription = stringResource(id = R.string.app_name),
                                tint = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                            )
                            Spacer(modifier = Modifier.height(150.dp))
                            Text(text = "Signup", style = TextStyle(color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground, fontSize = 30.sp, fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(30.dp))
                            LoginTextField(
                                label = "Email",
                                value = email,
                                onValueChange = setEmail,
                                trailing = "",
                                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            LoginTextField(
                                label = "Username",
                                value = username,
                                onValueChange = setusername,
                                trailing = "",
                                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            LoginTextField(
                                label = "Password",
                                value = password,
                                onValueChange = setpassword,
                                trailing = "",
                                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                onClick = {})
                            Spacer(modifier = Modifier.height(15.dp))
                            SignupButton(
                                iconId = R.drawable.unlock,
                                contentDescription = "Signup",
                                onClick = {
                                    if (email.isNotEmpty() && password.isNotEmpty()) {
                                        val user = Users(username)
                                        authViewModel.signup(email, password, user)
                                    }
                                })
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(text = "Or Continue with")
                            Spacer(modifier = Modifier.height(15.dp))
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth(fraction = 0.8f)
                            ) {
                                SocialIconButton(modifier = Modifier.weight(1f),iconId = R.drawable.facebook, contentDescription = "Facebook", onClick = {}, check = false)
                                Spacer(modifier = Modifier.width(8.dp))
                                SocialIconButton(modifier = Modifier.weight(1f),iconId = R.drawable.google, contentDescription = "Google", onClick = {}, check = false)
                            }
                        }
                    }
                }
            }
            is WindowInfo.WindowType.Medium -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        val (email, setEmail) = remember { mutableStateOf("") }
                        val (password, setpassword) = remember { mutableStateOf("") }
                        val (username, setusername) = remember { mutableStateOf("") }
                        val image = if (isSystemInDarkTheme()) R.drawable.shape1 else R.drawable.shape

                        Box (
                            contentAlignment = Alignment.TopCenter
                        ){
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(fraction = 0.46f),
                                painter = painterResource(id = image),
                                contentDescription = "Facebook Logo",
                                contentScale = ContentScale.FillBounds
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 70.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(145.dp),
                                    painter = painterResource(id = R.drawable.prob_stats),
                                    contentDescription = stringResource(id = R.string.app_name),
                                    tint = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                )
                                Spacer(modifier = Modifier.height(150.dp))
                                Text(text = "Signup", style = TextStyle(color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground, fontSize = 30.sp, fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.height(30.dp))
                                LoginTextField(
                                    label = "Email",
                                    value = email,
                                    onValueChange = setEmail,
                                    trailing = "",
                                    modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                    onClick = {}
                                )
                                Spacer(modifier = Modifier.height(15.dp))
                                LoginTextField(
                                    label = "Username",
                                    value = username,
                                    onValueChange = setusername,
                                    trailing = "",
                                    modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                    onClick = {}
                                )
                                Spacer(modifier = Modifier.height(15.dp))
                                LoginTextField(
                                    label = "Password",
                                    value = password,
                                    onValueChange = setpassword,
                                    trailing = "",
                                    modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                    onClick = {})
                                Spacer(modifier = Modifier.height(15.dp))
                                SignupButton(
                                    iconId = R.drawable.unlock,
                                    contentDescription = "Signup",
                                    onClick = {
                                        if (email.isNotEmpty() && password.isNotEmpty()) {
                                            val user = Users(username)
                                            authViewModel.signup(email, password, user)
                                        }
                                    })
                                Spacer(modifier = Modifier.height(15.dp))
                                Text(text = "Or Continue with")
                                Spacer(modifier = Modifier.height(15.dp))
                                Row (
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth(fraction = 0.8f)
                                ) {
                                    SocialIconButton(modifier = Modifier.weight(1f),iconId = R.drawable.facebook, contentDescription = "Facebook", onClick = {}, check = false)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    SocialIconButton(modifier = Modifier.weight(1f),iconId = R.drawable.google, contentDescription = "Google", onClick = {}, check = false)
                                }
                                spacer50()
                            }
                        }
                    }
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        val (email, setEmail) = remember { mutableStateOf("") }
                        val (password, setpassword) = remember { mutableStateOf("") }
                        val (username, setusername) = remember { mutableStateOf("") }
                        val image = if (isSystemInDarkTheme()) R.drawable.shape1 else R.drawable.shape

                        Box (
                            contentAlignment = Alignment.TopCenter
                        ){
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(fraction = 0.46f),
                                painter = painterResource(id = image),
                                contentDescription = "Facebook Logo",
                                contentScale = ContentScale.FillBounds
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 70.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(145.dp),
                                    painter = painterResource(id = R.drawable.prob_stats),
                                    contentDescription = stringResource(id = R.string.app_name),
                                    tint = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                )
                                Spacer(modifier = Modifier.height(150.dp))
                                Text(text = "Signup", style = TextStyle(color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground, fontSize = 30.sp, fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.height(30.dp))
                                LoginTextField(
                                    label = "Email",
                                    value = email,
                                    onValueChange = setEmail,
                                    trailing = "",
                                    modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                    onClick = {}
                                )
                                Spacer(modifier = Modifier.height(15.dp))
                                LoginTextField(
                                    label = "Username",
                                    value = username,
                                    onValueChange = setusername,
                                    trailing = "",
                                    modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                    onClick = {}
                                )
                                Spacer(modifier = Modifier.height(15.dp))
                                LoginTextField(
                                    label = "Password",
                                    value = password,
                                    onValueChange = setpassword,
                                    trailing = "",
                                    modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                    onClick = {})
                                Spacer(modifier = Modifier.height(15.dp))
                                SignupButton(
                                    iconId = R.drawable.unlock,
                                    contentDescription = "Signup",
                                    onClick = {
                                        if (email.isNotEmpty() && password.isNotEmpty()) {
                                            val user = Users(username)
                                            authViewModel.signup(email, password, user)
                                        }
                                    })
                                Spacer(modifier = Modifier.height(15.dp))
                                Text(text = "Or Continue with")
                                Spacer(modifier = Modifier.height(15.dp))
                                Row (
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth(fraction = 0.8f)
                                ) {
                                    SocialIconButton(modifier = Modifier.weight(1f),iconId = R.drawable.facebook, contentDescription = "Facebook", onClick = {}, check = false)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    SocialIconButton(modifier = Modifier.weight(1f),iconId = R.drawable.google, contentDescription = "Google", onClick = {}, check = false)
                                }
                                spacer50()
                            }
                        }
                    }
                }
            }
        }
    }
}


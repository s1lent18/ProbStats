package com.example.kotlin_learning.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.viewModel.rWindowInfo
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
        val signedUp by authViewModel.signedup.observeAsState(initial = false)
        val errorMessage1 by authViewModel.errorMessage.observeAsState()
        val showDialog = remember { mutableStateOf(false) }
        val showSuccessDialog = remember { mutableStateOf(false) }
        val (email, setEmail) = remember { mutableStateOf("") }
        val (password, setpassword) = remember { mutableStateOf("") }
        val (username, setusername) = remember { mutableStateOf("") }
        val image = if (isSystemInDarkTheme()) R.drawable.shape1 else R.drawable.shape
        var passwordvisibility by remember { mutableStateOf(false) }
        val gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF14213d), Color(0xFF219ebc))
        )
        val icon = if (passwordvisibility)
            painterResource(id = R.drawable.vision)
        else
            painterResource(id = R.drawable.eyelock)

        println(errorMessage1)
        LaunchedEffect(errorMessage1) {
            if (errorMessage1 != null) {
                showDialog.value = true
            }
        }

        if (showDialog.value) {
            errorMessage1?.let {
                DialogBox(
                    title = "Error",
                    text = it,
                    setShowDialog = showDialog,
                    onClick = {
                        showDialog.value = false
                        authViewModel.resetErrorMessage()
                    }
                )
            }
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
                    Box(
                        modifier = Modifier.fillMaxHeight(fraction = 0.40f).clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)).fillMaxWidth().background(gradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(145.dp),
                            painter = painterResource(id = R.drawable.prob_stats),
                            contentDescription = stringResource(id = R.string.app_name),
                            tint = lightmodefontcolor
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("     Let's Begin Your\nJourney With our App",
                            fontSize = 24.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = setEmail,
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = TextFieldDefaults.colors(
                                cursorColor = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        OutlinedTextField(
                            value = username,
                            onValueChange = setusername,
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = TextFieldDefaults.colors(
                                cursorColor = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = setpassword,
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                cursorColor = Color.Black
                            ),
                            trailingIcon = {
                                Box(
                                    modifier = Modifier.padding(end = 15.dp)
                                ) {
                                    Row {
                                        IconButton(
                                            onClick = {
                                                passwordvisibility = !passwordvisibility
                                            },
                                            modifier = Modifier.size(25.dp)
                                        ) {
                                            Icon(painter = icon,
                                                contentDescription = "Icon",
                                                tint = if (isSystemInDarkTheme()) lightmodefontcolor else darkmodefontcolor
                                            )
                                        }
                                    }
                                }
                            },
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = {
                                if (email.isNotEmpty() && password.isNotEmpty()) {
                                    authViewModel.signup(email, password, username = username)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF219ebc)
                            ),
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Signup", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
            is WindowInfo.WindowType.Medium -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box (
                        contentAlignment = Alignment.TopCenter
                    ){
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(fraction = 0.50f),
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
                            Spacer(modifier = Modifier.height(60.dp))
                            LoginTextField(
                                label = "Email",
                                value = email,
                                onValueChange = setEmail,
                                trailing = "",
                                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            LoginTextField(
                                label = "Username",
                                value = username,
                                onValueChange = setusername,
                                trailing = "",
                                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            LoginTextField(
                                label = "Password",
                                value = password,
                                onValueChange = setpassword,
                                trailing = "",
                                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                onClick = {})
                            Spacer(modifier = Modifier.height(30.dp))
                            SignupButton(
                                iconId = R.drawable.unlock,
                                contentDescription = "Signup",
                                onClick = {
                                    if (email.isNotEmpty() && password.isNotEmpty()) {
                                        authViewModel.signup(email, password, username = username)
                                    }
                                }
                            )
                        }
                    }
                }
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box (
                        contentAlignment = Alignment.TopCenter
                    ){
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(fraction = 0.50f),
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
                            Spacer(modifier = Modifier.height(60.dp))
                            LoginTextField(
                                label = "Email",
                                value = email,
                                onValueChange = setEmail,
                                trailing = "",
                                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            LoginTextField(
                                label = "Username",
                                value = username,
                                onValueChange = setusername,
                                trailing = "",
                                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            LoginTextField(
                                label = "Password",
                                value = password,
                                onValueChange = setpassword,
                                trailing = "",
                                modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                onClick = {})
                            Spacer(modifier = Modifier.height(30.dp))
                            SignupButton(
                                iconId = R.drawable.unlock,
                                contentDescription = "Signup",
                                onClick = {
                                    if (email.isNotEmpty() && password.isNotEmpty()) {
                                        authViewModel.signup(email, password, username = username)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


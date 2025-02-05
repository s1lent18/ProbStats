package com.example.kotlin_learning.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.example.kotlin_learning.viewModel.InternetViewModel

@Composable
fun Spacer50() {
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
fun DialogBox(title: String, text: String, setShowDialog: MutableState<Boolean>, onClick: () -> Unit) {

    AlertDialog(
        textContentColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
        titleContentColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
        onDismissRequest = { setShowDialog.value = false },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = text, fontSize = 15.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = onClick) {
                Text("OK", color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground)
            }
        }
    )
}

@Composable
fun Login(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    internetViewModel: InternetViewModel = viewModel()
) {
    Surface {
        val windowInfo = rWindowInfo()
        val loggedIn by authViewModel.loggedin.observeAsState(initial = false)
        val errorMessage by authViewModel.errorMessage.observeAsState()
        val showDialog = remember { mutableStateOf(false) }
        val showInternetDialog = remember { mutableStateOf(false) }
        val isInternetAvailable by internetViewModel.isInternetAvailable.observeAsState(initial = true)
        val activity = (LocalContext.current as? Activity)
        val resetpass by authViewModel.send.observeAsState()
        val showresetdialog = remember { mutableStateOf(false) }
        val emailenter = remember { mutableStateOf(false) }
        val errorcount = remember { mutableIntStateOf(0) }
        val errordialogbox = remember { mutableStateOf(false) }
        val (email, setEmail) = remember { mutableStateOf("") }
        val (password, setpassword) = remember { mutableStateOf("") }
        var passwordvisibility by remember { mutableStateOf(false) }
        val gradient = Brush.verticalGradient(colors = listOf(Color(0xFF14213d), Color(0xFF219ebc)))
        val icon = if (passwordvisibility) painterResource(id = R.drawable.vision) else painterResource(id = R.drawable.eyelock)

        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                showDialog.value = true
                errorcount.intValue++
            }
        }

        LaunchedEffect(resetpass) {
            if (resetpass != null) {
                showresetdialog.value = true
                errorcount.intValue = 0
            }
        }

        LaunchedEffect(Unit) {
            internetViewModel.checkInternetConnection()
        }

        if(errorcount.intValue > 2) {
            errordialogbox.value = true
        }

        if(errordialogbox.value) {
            AlertDialog(
                textContentColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                titleContentColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                onDismissRequest = {
                      errordialogbox.value = false
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "", style = MaterialTheme.typography.titleLarge)
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Forgot Password?", fontSize = 15.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (email != "") {
                            authViewModel.sendPasswordviaemail(email)
                        } else {
                            emailenter.value = true
                        }
                    }) {
                        Text("OK", color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            errordialogbox.value = false
                            errorcount.intValue = 0
                        }
                    ) {
                        Text("Dismiss", color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground)
                    }
                }
            )
        }

        if (emailenter.value) {
            DialogBox(
                title = "Error",
                text = "Please enter an email",
                setShowDialog = emailenter,
                onClick = { emailenter.value = false }
            )
        }

        if (showDialog.value) {
            DialogBox(
                title = "Error",
                text = "Wrong Email/Password",
                setShowDialog = showDialog,
                onClick = { showDialog.value = false }
            )
            authViewModel.resetErrorMessage()
        }

        if(showresetdialog.value) {
            DialogBox(
                title = "Success",
                text = resetpass.toString(),
                setShowDialog = showresetdialog,
                onClick = { showresetdialog.value = false }
            )
        }

        if (!isInternetAvailable) {
            DialogBox(
                title = "Error",
                text = "Please check your internet connection.",
                setShowDialog = showInternetDialog,
                onClick = { activity?.finish() }
            )
        }

        when (windowInfo.screenWidthInfo) {
            is WindowInfo.WindowType.Compact -> {
                if(loggedIn) {
                    LaunchedEffect(Unit) {
                        navController.navigate(route = Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                } else {
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
                        Row(
                            Modifier.padding(top = 25.dp, start = 20.dp)
                        ) {
                            Text("Welcome Back",
                                fontSize = 24.sp,
                                color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = setEmail,
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    cursorColor = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212)
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = setpassword,
                                label = { Text("Password") },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = if (passwordvisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                colors = TextFieldDefaults.colors(
                                    cursorColor = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212)
                                ),
                                trailingIcon = {
                                    Box(
                                        modifier = Modifier.padding(end = 20.dp)
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
                            Spacer(modifier = Modifier.height(5.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text("Forgot Password?",
                                    color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212),
                                    modifier = Modifier.clickable {
                                        if (email != "") {
                                            authViewModel.sendPasswordviaemail(email)
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Button(
                                onClick = {
                                    if (email != "" && password != "") {
                                            authViewModel.signin(email = email, password = password)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF219ebc)
                                ),
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("Login", color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212))
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            TextButton(
                                onClick = { navController.navigate(route = Screen.Signup.route) }
                            ) {
                                Text("You Don't Have Account? Sign Up", color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212))
                            }
                        }
                    }
                }
            }
            is WindowInfo.WindowType.Medium -> {
                if(loggedIn) {
                    LaunchedEffect(Unit) {
                        navController.navigate(route = Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                } else {
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
                        Row(
                            Modifier.padding(top = 25.dp, start = 20.dp)
                        ) {
                            Text("Welcome Back",
                                fontSize = 24.sp,
                                color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = setEmail,
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    cursorColor = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212)
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = setpassword,
                                label = { Text("Password") },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = if (passwordvisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                colors = TextFieldDefaults.colors(
                                    cursorColor = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212)
                                ),
                                trailingIcon = {
                                    Box(
                                        modifier = Modifier.padding(end = 20.dp)
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
                            Spacer(modifier = Modifier.height(5.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text("Forgot Password?",
                                    color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212),
                                    modifier = Modifier.clickable {
                                        if (email != "") {
                                            authViewModel.sendPasswordviaemail(email)
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Button(
                                onClick = {
                                    if (email != "" && password != "") {
                                        authViewModel.signin(email = email, password = password)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF219ebc)
                                ),
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("Login", color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212))
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            TextButton(
                                onClick = { navController.navigate(route = Screen.Signup.route) }
                            ) {
                                Text("You Don't Have Account? Sign Up", color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212))
                            }
                        }
                    }
                }
            }
            else -> {
                if(loggedIn) {
                    LaunchedEffect(Unit) {
                        navController.navigate(route = Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                } else {
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
                        Row(
                            Modifier.padding(top = 25.dp, start = 20.dp)
                        ) {
                            Text("Welcome Back",
                                fontSize = 24.sp,
                                color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = setEmail,
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    cursorColor = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212)
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = setpassword,
                                label = { Text("Password") },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = if (passwordvisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                colors = TextFieldDefaults.colors(
                                    cursorColor = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212)
                                ),
                                trailingIcon = {
                                    Box(
                                        modifier = Modifier.padding(end = 20.dp)
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
                            Spacer(modifier = Modifier.height(5.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text("Forgot Password?",
                                    color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212),
                                    modifier = Modifier.clickable {
                                        if (email != "") {
                                            authViewModel.sendPasswordviaemail(email)
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Button(
                                onClick = {
                                    if (email != "" && password != "") {
                                        authViewModel.signin(email = email, password = password)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF219ebc)
                                ),
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("Login", color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212))
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            TextButton(
                                onClick = { navController.navigate(route = Screen.Signup.route) }
                            ) {
                                Text("You Don't Have Account? Sign Up", color = if (isSystemInDarkTheme()) Color.White else Color(0xFF121212))
                            }
                        }
                    }
                }
            }
        }
    }
}
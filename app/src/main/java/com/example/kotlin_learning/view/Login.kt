package com.example.kotlin_learning.view

import android.app.Activity
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.InternetViewModel

@Composable
fun LoginTextField(
    modifier: Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailing: String,
    onClick: () -> Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            disabledTextColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            unfocusedTextColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            focusedTextColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            unfocusedContainerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            focusedContainerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
        ),
        trailingIcon = {
            TextButton(onClick = onClick) {
                Text(text = trailing,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            }
        }
    )
}

// Check Boolean Passed to differentiate between Google/FaceBook and drawable icons

@Composable
fun spacer50() {
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
fun SocialIconButton(
    modifier: Modifier,
    iconId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    check: Boolean
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
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
                tint = if (!check) Color.Unspecified else if (isSystemInDarkTheme()) darkmodefontcolor else if(!isSystemInDarkTheme()) lightmodefontcolor else Color.Unspecified
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

        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                showDialog.value = true
            }
        }

        LaunchedEffect(Unit) {
            internetViewModel.checkInternetConnection()
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
                        val (email, setEmail) = remember { mutableStateOf("") }
                        val (password, setpassword) = remember { mutableStateOf("") }
                        val image = if (isSystemInDarkTheme()) R.drawable.shape1 else R.drawable.shape

                        Box (
                            contentAlignment = Alignment.TopCenter
                        ){
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(fraction = 0.46f),
                                painter = painterResource(id = image),
                                contentDescription = "Cover Image",
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
                                Spacer(modifier = Modifier.height(120.dp))
                                Text(
                                    text = "Login",
                                    style = TextStyle(
                                        color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    ))
                                Spacer(modifier = Modifier.height(40.dp))
                                LoginTextField(
                                    label = "Email",
                                    value = email,
                                    onValueChange = setEmail,
                                    trailing = "Forgot?",
                                    modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                    onClick = {}
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                LoginTextField(
                                    label = "Password",
                                    value = password,
                                    onValueChange = setpassword,
                                    trailing = "Forgot?",
                                    modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                    onClick = {}
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Row (
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth(fraction = 0.8f)
                                ){
                                    SocialIconButton(
                                        modifier = Modifier.weight(1f),
                                        iconId = R.drawable.unlock,
                                        contentDescription = "Login",
                                        onClick = {
                                            if (email != "" && password != "") {
                                                authViewModel.signin(email, password)
                                            }

                                        },
                                        check = true)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    SocialIconButton(
                                        modifier = Modifier.weight(1f),
                                        iconId = R.drawable.add,
                                        contentDescription = "Add User",
                                        onClick = {
                                            navController.navigate(
                                                route = Screen.Signup.route
                                            )
                                        },
                                        check = true
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(text = "Or Continue with")
                                Spacer(modifier = Modifier.height(20.dp))
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
            }
            is WindowInfo.WindowType.Medium -> {
                if(loggedIn) {
                    LaunchedEffect(Unit) {
                        navController.navigate(route = Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            val (email, setEmail) = remember { mutableStateOf("") }
                            val (password, setpassword) = remember { mutableStateOf("") }
                            val image = if (isSystemInDarkTheme()) R.drawable.shape1 else R.drawable.shape

                            Box (
                                contentAlignment = Alignment.TopCenter
                            ){
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(fraction = 0.46f),
                                    painter = painterResource(id = image),
                                    contentDescription = "Cover Image",
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
                                    Spacer(modifier = Modifier.height(200.dp))
                                    Text(
                                        text = "Login",
                                        style = TextStyle(
                                            color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.Bold
                                        ))
                                    Spacer(modifier = Modifier.height(40.dp))
                                    LoginTextField(
                                        label = "Email",
                                        value = email,
                                        onValueChange = setEmail,
                                        trailing = "Forgot?",
                                        modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                        onClick = {}
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    LoginTextField(
                                        label = "Password",
                                        value = password,
                                        onValueChange = setpassword,
                                        trailing = "Forgot?",
                                        modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                        onClick = {}
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Row (
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth(fraction = 0.8f)
                                    ){
                                        SocialIconButton(
                                            modifier = Modifier.weight(1f),
                                            iconId = R.drawable.unlock,
                                            contentDescription = "Login",
                                            onClick = {
                                                if (email != "" && password != "") {
                                                    authViewModel.signin(email, password)
                                                }
                                            },
                                            check = true)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        SocialIconButton(
                                            modifier = Modifier.weight(1f),
                                            iconId = R.drawable.add,
                                            contentDescription = "Add User",
                                            onClick = {
                                                navController.navigate(
                                                    route = Screen.Signup.route
                                                )
                                            },
                                            check = true
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(text = "Or Continue with")
                                    Spacer(modifier = Modifier.height(20.dp))
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
            else -> {
                if(loggedIn) {
                    LaunchedEffect(Unit) {
                        navController.navigate(route = Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            val (email, setEmail) = remember { mutableStateOf("") }
                            val (password, setpassword) = remember { mutableStateOf("") }
                            val image = if (isSystemInDarkTheme()) R.drawable.shape1 else R.drawable.shape

                            Box (
                                contentAlignment = Alignment.TopCenter
                            ){
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(fraction = 0.46f),
                                    painter = painterResource(id = image),
                                    contentDescription = "Cover Image",
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
                                    Spacer(modifier = Modifier.height(120.dp))
                                    Text(
                                        text = "Login",
                                        style = TextStyle(
                                            color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.Bold
                                        ))
                                    Spacer(modifier = Modifier.height(40.dp))
                                    LoginTextField(
                                        label = "Email",
                                        value = email,
                                        onValueChange = setEmail,
                                        trailing = "Forgot?",
                                        modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                        onClick = {}
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    LoginTextField(
                                        label = "Password",
                                        value = password,
                                        onValueChange = setpassword,
                                        trailing = "Forgot?",
                                        modifier = Modifier.fillMaxWidth(fraction = 0.80f),
                                        onClick = {}
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Row (
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth(fraction = 0.8f)
                                    ){
                                        SocialIconButton(
                                            modifier = Modifier.weight(1f),
                                            iconId = R.drawable.unlock,
                                            contentDescription = "Login",
                                            onClick = {
                                                if (email != "" && password != "") {
                                                    authViewModel.signin(email, password)
                                                }

                                            },
                                            check = true)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        SocialIconButton(
                                            modifier = Modifier.weight(1f),
                                            iconId = R.drawable.add,
                                            contentDescription = "Add User",
                                            onClick = {
                                                navController.navigate(
                                                    route = Screen.Signup.route
                                                )
                                            },
                                            check = true
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(text = "Or Continue with")
                                    Spacer(modifier = Modifier.height(20.dp))
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
}
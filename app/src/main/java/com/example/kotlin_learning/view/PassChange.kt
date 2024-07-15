package com.example.kotlin_learning.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.ui.theme.Amatic
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.viewModel.rWindowInfo

@Composable
fun StringInput(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        label = { Text(label)},
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            disabledTextColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            unfocusedTextColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            focusedTextColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            unfocusedContainerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            focusedContainerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            unfocusedLabelColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            focusedLabelColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,

        )
    )
}

@Composable
fun PassChange(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val windowInfo = rWindowInfo()
    val ddone by authViewModel.done.observeAsState(initial = false)
    val email by authViewModel.email.collectAsState()
    val (oldpass, setoldpass) = remember {mutableStateOf("")}
    val (newpass, setnewpass) = remember {mutableStateOf("")}
    val (connewpass, setconnewpass) = remember {mutableStateOf("")}
    val showDialog = remember { mutableStateOf(false) }
    val showerrorDialog = remember { mutableStateOf(false) }
    val loggedIn by authViewModel.loggedin.observeAsState(initial = true)


    LaunchedEffect (Unit) {
        val userid = authViewModel.getuserid()
        if(userid != null) {
            authViewModel.getemail(userid)
        }
    }

    LaunchedEffect (ddone) {
        if(ddone) {
            showDialog.value = true
        }
    }

    if(showerrorDialog.value) {
        DialogBox(
            title = "Failed",
            text = "Fill all the fields",
            setShowDialog = showerrorDialog,
            onClick = { showerrorDialog.value = false }
        )
    }

    if(showDialog.value) {
        DialogBox(
            title = "Success",
            text = "Password Changed",
            setShowDialog = showDialog,
            onClick = { showDialog.value = false }
        )
    }

    when(windowInfo.screenWidthInfo) {
        WindowInfo.WindowType.Compact -> {
            if(loggedIn) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = if (isSystemInDarkTheme()) Color(0xFF121212) else Color.White
                        )
                ) {
                    Card (
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.9f)
                            .fillMaxHeight(fraction = 0.8f)
                            .align(Alignment.Center)
                        ,
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFF101720)
                        ),
                        elevation = CardDefaults.cardElevation(
                            10.dp
                        ),
                        border = BorderStroke(1.dp, color = Color.Transparent)
                    ) {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Change Password",
                                color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                fontSize = 50.sp,
                                fontFamily = Amatic
                            )
                            Spacer(modifier = Modifier.height(100.dp))
                            StringInput(label = "Old Password", value = oldpass, onValueChange = setoldpass)
                            Spacer50()
                            StringInput(label = "New Password", value = newpass, onValueChange = setnewpass)
                            Spacer50()
                            StringInput(label = "Confirm New Password", value = connewpass, onValueChange = setconnewpass)
                            Spacer(modifier = Modifier.height(70.dp))
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp),
                                onClick = {
                                    if(email != null && oldpass != "" && newpass != "" && connewpass != "" && newpass == connewpass) {
                                        authViewModel.reauthenticateAndChangePassword(email!!, oldpass, newpass)
                                    } else {
                                        showerrorDialog.value = true
                                    }
                                },
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    10.dp
                                ),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                )
                            ) {
                                Text(text = "Change Password", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
                            }
                        }
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(route = Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }
        WindowInfo.WindowType.Expanded -> {
            if(loggedIn) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = if (isSystemInDarkTheme()) Color(0xFF121212) else Color.White
                        )
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Change Password", color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground, fontSize = 30.sp)
                        Spacer50()
                        StringInput(label = "Old Password", value = oldpass, onValueChange = setoldpass)
                        Spacer50()
                        StringInput(label = "New Password", value = newpass, onValueChange = setnewpass)
                        Spacer50()
                        StringInput(label = "Confirm New Password", value = connewpass, onValueChange = setconnewpass)
                        Spacer50()
                        ElevatedButton(
                            modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                            onClick = {
                                if(email != null && oldpass != "" && newpass != "" && connewpass != "" && newpass == connewpass) {
                                    authViewModel.reauthenticateAndChangePassword(email!!, oldpass, newpass)
                                    //authViewModel.signout()
                                }
                            },
                            elevation = ButtonDefaults.elevatedButtonElevation(
                                10.dp
                            ),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                            )
                        ) {
                            Text(text = "Generate Answer", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
                        }
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(route = Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }
        WindowInfo.WindowType.Medium -> {
            if(loggedIn) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = if (isSystemInDarkTheme()) Color(0xFF121212) else Color.White
                        )
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Change Password", color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground, fontSize = 30.sp)
                        Spacer50()
                        StringInput(label = "Old Password", value = oldpass, onValueChange = setoldpass)
                        Spacer50()
                        StringInput(label = "New Password", value = newpass, onValueChange = setnewpass)
                        Spacer50()
                        StringInput(label = "Confirm New Password", value = connewpass, onValueChange = setconnewpass)
                        Spacer50()
                        ElevatedButton(
                            modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                            onClick = {
                                if(email != null && oldpass != "" && newpass != "" && connewpass != "" && newpass == connewpass) {
                                    authViewModel.reauthenticateAndChangePassword(email!!, oldpass, newpass)
                                }
                            },
                            elevation = ButtonDefaults.elevatedButtonElevation(
                                10.dp
                            ),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                            )
                        ) {
                            Text(text = "Generate Answer", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
                        }
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(route = Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }
    }
}
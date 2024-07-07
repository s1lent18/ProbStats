package com.example.kotlin_learning.view

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel

@Preview
@Composable
fun PassChange(
    authViewModel: AuthViewModel = viewModel(),
) {
    val ddone by authViewModel.done.observeAsState(initial = false)
    val email by authViewModel.email.collectAsState()
    val (oldpass, setoldpass) = remember {mutableStateOf("")}
    val (newpass, setnewpass) = remember {mutableStateOf("")}
    val (connewpass, setconnewpass) = remember {mutableStateOf("")}
    val showDialog = remember { mutableStateOf(false) }

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

    if(showDialog.value) {
        DialogBox(
            title = "Success",
            text = "Password Changed",
            setShowDialog = showDialog,
            onClick = { showDialog.value = false }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            color = if (isSystemInDarkTheme()) Color(0xFF121212) else Color.White
        )
    ) {
        Text("Change Password", color = Color.White)
        Column (
            modifier = Modifier.fillMaxSize().align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                label = { Text("Old Password")},
                value = oldpass,
                onValueChange = setoldpass,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(fraction =0.9f),
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
            OutlinedTextField(
                label = { Text("New Password")},
                value = newpass,
                onValueChange = setnewpass,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(fraction =0.9f),
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
            OutlinedTextField(
                label = { Text("Confirm New Password")},
                value = connewpass,
                onValueChange = setconnewpass,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(fraction =0.9f),
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
}
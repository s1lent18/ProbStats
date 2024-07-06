package com.example.kotlin_learning.view

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel

@Composable
fun DetailRow(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, detail: String) {
    Column(modifier = Modifier.padding(vertical = 15.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) lightmodefontcolor else darkmodefontcolor)
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = if (isSystemInDarkTheme()) lightmodefontcolor else darkmodefontcolor)
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = detail, color = if (isSystemInDarkTheme()) lightmodefontcolor else darkmodefontcolor)
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 15.dp),
            thickness = 1.dp,
            color = if (isSystemInDarkTheme()) lightmodefontcolor else darkmodefontcolor
        )
    }
}

@Composable
fun Account(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val username by authViewModel.username.collectAsState()
    val isLoading by authViewModel.loading.collectAsState()
    val email by authViewModel.email.collectAsState()
    val numberofsolutions by authViewModel.nos.collectAsState()

    LaunchedEffect(Unit) {
        val userid = authViewModel.getuserid()
        if (userid != null) {
            authViewModel.getusername(userid)
            authViewModel.getemail(userid)
            authViewModel.getnos(userid)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color(0xFF121212) else Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(fraction = 0.35f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.height(8.dp))
                username?.let {
                    Text(
                        text = "Hey $it!",
                        color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            email?.let { DetailRow("Email", Icons.Default.Email, it) }
            Spacer(modifier = Modifier.height(10.dp))
            DetailRow("Number Of Solutions", Icons.Default.AddCircle, numberofsolutions.toString())
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Change Password")
            }
        }
    }
}
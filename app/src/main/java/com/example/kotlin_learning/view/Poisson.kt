package com.example.kotlin_learning.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.PoissonViewModel
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.request.PoissonRequest
import com.example.kotlin_learning.viewModel.rWindowInfo
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Repository
import kotlinx.coroutines.launch

fun String.isNumericOrFloat(): Boolean {
    return this.toFloatOrNull() != null || this.isEmpty() || this == "."
}

@Composable
fun Floatinput(
    modifier: Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {

    TextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            if (it.isNumericOrFloat()) {
                onValueChange(it)
            }
        },
        label = {
            Text(
                text = label,
                color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            unfocusedContainerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            unfocusedTextColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            focusedTextColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            cursorColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
            focusedPlaceholderColor = Color.Transparent,
            unfocusedPlaceholderColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        visualTransformation = VisualTransformation.None,
    )
}

@Composable
fun FloatAnswer(
    value: Float,
    text: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            color = Color.Blue
        ),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
        ),
        modifier = Modifier
            .fillMaxWidth(fraction = 0.9f)
            .height(50.dp),
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "$text $value")
        }
    }
}

@Composable
fun StringAnswer(
    text: String,
    modifier: Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            color = Color.Blue
        ),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
        ),
        modifier = modifier,
    ) {
        Spacer(Modifier.height(12.dp))
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = text)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Poisson(
    navController: NavController,
    poissonViewModel: PoissonViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val repository = Repository()
    val userId = authViewModel.getuserid()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val (x, setx) = remember { mutableStateOf("") }
    val (lamda, setlamda) = remember { mutableStateOf("") }
    val probability = poissonViewModel.poissonresult.observeAsState()
    var isSubmitted by remember { mutableStateOf(false) }
    var isupdated by remember { mutableStateOf(false) }
    var display by remember { mutableStateOf(false) }

    LaunchedEffect(isupdated) {
        if (isupdated) {
            val poissonRequest = PoissonRequest(x.toFloat(), lamda.toFloat())
            poissonViewModel.getPoissonAnswer(poissonRequest)
            isupdated = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (modifier = Modifier
                .background(if (isSystemInDarkTheme()) Color(0xFF023047) else Color(0xFF0077B6))
                .fillMaxHeight()
                .fillMaxWidth(fraction = 0.8f)) {
                LazyColumn (
                    modifier = Modifier.padding(16.dp),
                ){
                    item {
                        Icon(
                            painter = painterResource(R.drawable.prob_stats),
                            contentDescription = "Icon",
                            modifier = Modifier.size(300.dp),
                            tint = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        SidebarItem(
                            icon = Icons.Default.AccountCircle,
                            text = "Account",
                            isSelected = false,
                            onClick = {
                                navController.navigate(route = Screen.Account.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        SidebarItem(
                            icon = Icons.Default.Home,
                            text = "Home",
                            isSelected = true,
                            onClick = {
                                navController.navigate(route = Screen.Home.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        SidebarItem(
                            icon = Icons.Default.Info,
                            text = "History",
                            isSelected = false,
                            onClick = {
                                navController.navigate(route = Screen.History.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        SidebarItem(
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            text = "Logout",
                            isSelected = false,
                            onClick = {
                                authViewModel.signout()
                                navController.navigate(route = Screen.Login.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }
    ) {
        val windowInfo = rWindowInfo()
        when (windowInfo.screenWidthInfo) {
            is WindowInfo.WindowType.Compact -> {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Poisson") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "X:",
                                value = x,
                                onValueChange = {
                                    setx(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Lambda(t):",
                                value = lamda,
                                onValueChange = {
                                    setlamda(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    if (x != "" && lamda != "") {
                                        isSubmitted = false
                                        isupdated = true
                                        display = true
                                        keyboardController?.hide()
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
                            if (x != "" && lamda != "" && !isupdated) {
                                when(val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f))
                                        Spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                        isSubmitted = false
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            Card(
                                                shape = RoundedCornerShape(20.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                                ),
                                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                                elevation = CardDefaults.cardElevation(10.dp),
                                                border = BorderStroke(
                                                    1.dp,
                                                    Color.Blue
                                                )
                                            ) {
                                                Column(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Spacer50()
                                                    Text("P(X=$x):    ${result.data.equal}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X<$x):    ${result.data.less}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X<=$x):  ${result.data.lessequal}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X>$x):    ${result.data.greater}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X>=$x):  ${result.data.greaterequal}", fontSize = 20.sp)
                                                    Spacer50()
                                                    if (!isSubmitted && userId != null) {
                                                        repository.sendpoisson(
                                                            userId = userId,
                                                            x = x.toFloat(),
                                                            lamda = lamda.toFloat(),
                                                            equal = result.data.equal,
                                                            greater = result.data.greater,
                                                            greaterequal = result.data.greaterequal,
                                                            less = result.data.less,
                                                            lessequal = result.data.lessequal
                                                        )
                                                        repository.incrementcount(userId)
                                                        isSubmitted = true
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    null -> {

                                    }
                                }
                            }
                        }
                    }
                }
            }

            is WindowInfo.WindowType.Medium -> {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Poisson") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "X:",
                                value = x,
                                onValueChange = {
                                    setx(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Lambda(t):",
                                value = lamda,
                                onValueChange = {
                                    setlamda(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    if (x != "" && lamda != "") {
                                        isSubmitted = false
                                        isupdated = true
                                        display = true
                                        keyboardController?.hide()
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
                            if (x != "" && lamda != "" && !isupdated) {
                                when(val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f))
                                        Spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                        isSubmitted = false
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            Card(
                                                shape = RoundedCornerShape(20.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                                ),
                                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                                elevation = CardDefaults.cardElevation(10.dp),
                                                border = BorderStroke(
                                                    1.dp,
                                                    Color.Blue
                                                )
                                            ) {
                                                Column(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Spacer50()
                                                    Text("P(X=$x):    ${result.data.equal}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X<$x):    ${result.data.less}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X<=$x):  ${result.data.lessequal}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X>$x):    ${result.data.greater}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X>=$x):  ${result.data.greaterequal}", fontSize = 20.sp)
                                                    Spacer50()
                                                    if (!isSubmitted && userId != null) {
                                                        repository.sendpoisson(
                                                            userId = userId,
                                                            x = x.toFloat(),
                                                            lamda = lamda.toFloat(),
                                                            equal = result.data.equal,
                                                            greater = result.data.greater,
                                                            greaterequal = result.data.greaterequal,
                                                            less = result.data.less,
                                                            lessequal = result.data.lessequal
                                                        )
                                                        repository.incrementcount(userId)
                                                        isSubmitted = true
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    null -> {

                                    }
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Poisson") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "X:",
                                value = x,
                                onValueChange = {
                                    setx(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Lambda(t):",
                                value = lamda,
                                onValueChange = {
                                    setlamda(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    if (x != "" && lamda != "") {
                                        isSubmitted = false
                                        isupdated = true
                                        display = true
                                        keyboardController?.hide()
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
                            if (x != "" && lamda != "" && !isupdated) {
                                when(val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f))
                                        Spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                        isSubmitted = false
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            Card(
                                                shape = RoundedCornerShape(20.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                                ),
                                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                                elevation = CardDefaults.cardElevation(10.dp),
                                                border = BorderStroke(
                                                    1.dp,
                                                    Color.Blue
                                                )
                                            ) {
                                                Column(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Spacer50()
                                                    Text("P(X=$x):    ${result.data.equal}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X<$x):    ${result.data.less}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X<=$x):  ${result.data.lessequal}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X>$x):    ${result.data.greater}", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(20.dp))
                                                    Text("P(X>=$x):  ${result.data.greaterequal}", fontSize = 20.sp)
                                                    Spacer50()
                                                    if (!isSubmitted && userId != null) {
                                                        repository.sendpoisson(
                                                            userId = userId,
                                                            x = x.toFloat(),
                                                            lamda = lamda.toFloat(),
                                                            equal = result.data.equal,
                                                            greater = result.data.greater,
                                                            greaterequal = result.data.greaterequal,
                                                            less = result.data.less,
                                                            lessequal = result.data.lessequal
                                                        )
                                                        repository.incrementcount(userId)
                                                        isSubmitted = true
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    null -> {

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
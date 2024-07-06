package com.example.kotlin_learning.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.SLRViewModel
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.request.SLRRequest
import com.example.kotlin_learning.viewModel.rWindowInfo
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun tailSwitch(onSwitchChange: (Boolean) -> Unit, first: String, second: String): Boolean {
    var check by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(
                checked = check,
                onCheckedChange = {
                    check = it
                    onSwitchChange(it)
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
                    checkedThumbColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    uncheckedThumbColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    uncheckedTrackColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
                    uncheckedBorderColor = if (isSystemInDarkTheme()) lightmodefontcolor else darkmodefontcolor,
                    checkedBorderColor = if (isSystemInDarkTheme()) lightmodefontcolor else darkmodefontcolor,
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = if (check) first else second,
                color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
            )
        }
    }
    return check
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SLR(
    navController: NavController,
    viewModel: SLRViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val drawerstate = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val (n, setn) = remember { mutableStateOf("") }
    val (alpha, setalpha) = remember { mutableStateOf("") }
    val (x, setx) = remember { mutableStateOf(arrayOf("")) }
    val (y, sety) = remember { mutableStateOf(arrayOf("")) }
    var tail = false
    val rresult = viewModel.SLRresult.observeAsState()
    val userId = authViewModel.getuserid()
    val isSubmitted = remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerstate,
        drawerContent = {
            ModalDrawerSheet {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    item {
                        Icon(
                            painter = painterResource(R.drawable.prob_stats),
                            contentDescription = "Icon",
                            modifier = Modifier.size(200.dp),
                            tint = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(text = "Account", color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground)
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(modifier = Modifier.clickable {
                            navController.navigate(route = Screen.Home.route)
                        }, text = "Calculators", color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground)
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(modifier = Modifier.clickable {
                            navController.navigate(route = Screen.History.route)
                        }, text = "History", color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground)
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            modifier = Modifier.clickable {
                                authViewModel.signout()
                                navController.navigate(route = Screen.Login.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            },
                            text = "Logout",
                            color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }
    ) {
        val windowInfo = rWindowInfo()
        when (windowInfo.screenWidthInfo) {
            WindowInfo.WindowType.Compact -> {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("SLR") {
                            scope.launch { drawerstate.open() }
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
                            Spacer(modifier = Modifier.height(50.dp))
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = { newValue ->
                                    setn(newValue)
                                    if (newValue.isNotEmpty()) {
                                        val newSize = newValue.toIntOrNull() ?: 0
                                        sety(Array(newSize) { if (it < y.size) y[it] else "" })
                                        setx(Array(newSize) { if (it < x.size) x[it] else "" })
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Significance Level:",
                                value = alpha,
                                onValueChange = { newValue ->
                                    setalpha(newValue)
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        item {
                            tail = tailSwitch (onSwitchChange = {newTail -> tail = newTail }, first = "One Tail", second = "Two Tails")
                        }
                        if (n.isNotEmpty() && alpha.isNotEmpty()) {
                            item{
                                StringAnswer(text = "X:", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "$it:",
                                    index = it,
                                    values = x,
                                    onValueChange = setx
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(50.dp))
                                StringAnswer(text = "Y:", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "$it:",
                                    index = it,
                                    values = y,
                                    onValueChange = sety
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    if(n != "" && alpha != "" && x.isNotEmpty() && y.isNotEmpty()) {
                                        val slrrequest = SLRRequest(
                                            n = n.toInt(),
                                            x = stof(x),
                                            y = stof(y),
                                            alpha = alpha.toFloat(),
                                            tail = tail
                                        )
                                        viewModel.getSLRAnswer(slrrequest)
                                    }
                                },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                ),
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    10.dp
                                )
                            ) {
                                Text(text = "Generate Answer", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
                            }
                            Spacer(modifier = Modifier.height(50.dp))
                            if (n.isNotEmpty() && alpha.isNotEmpty() && x.isNotEmpty() && y.isNotEmpty()) {
                                when (val result = rresult.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer(modifier = Modifier.height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        FloatAnswer(text = "r:", value = result.data.r)
                                        Spacer(modifier = Modifier.height(50.dp))
                                        FloatAnswer(text = "t:", value = result.data.t)
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer(text = "y': ${result.data.Y}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer(modifier = Modifier.height(50.dp))
                                        if (userId != null && !isSubmitted.value) {
                                            authViewModel.sendslr(
                                                userId = userId,
                                                n = n.toInt(),
                                                x = stoff(x),
                                                y = stoff(y),
                                                alpha = alpha.toFloat(),
                                                tail = tail,
                                                hypothesis = result.data.hypothesis,
                                                r = result.data.r,
                                                t = result.data.t,
                                                Y = result.data.Y
                                            )
                                            authViewModel.incrementcount(userId)
                                            isSubmitted.value = true
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
            WindowInfo.WindowType.Expanded -> {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("SLR") {
                            scope.launch { drawerstate.open() }
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
                            Spacer(modifier = Modifier.height(50.dp))
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = { newValue ->
                                    setn(newValue)
                                    if (newValue.isNotEmpty()) {
                                        val newSize = newValue.toIntOrNull() ?: 0
                                        sety(Array(newSize) { if (it < y.size) y[it] else "" })
                                        setx(Array(newSize) { if (it < x.size) x[it] else "" })
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Significance Level:",
                                value = alpha,
                                onValueChange = { newValue ->
                                    setalpha(newValue)
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        item {
                            tail = tailSwitch (onSwitchChange = {newTail -> tail = newTail }, first = "One Tail", second = "Two Tails")
                        }
                        if (n.isNotEmpty() && alpha.isNotEmpty()) {
                            item{
                                StringAnswer(text = "X:", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "$it:",
                                    index = it,
                                    values = x,
                                    onValueChange = setx
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(50.dp))
                                StringAnswer(text = "Y:", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "$it:",
                                    index = it,
                                    values = y,
                                    onValueChange = sety
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    if(n != "" && alpha != "" && x.isNotEmpty() && y.isNotEmpty()) {
                                        val slrrequest = SLRRequest(
                                            n = n.toInt(),
                                            x = stof(x),
                                            y = stof(y),
                                            alpha = alpha.toFloat(),
                                            tail = tail
                                        )
                                        viewModel.getSLRAnswer(slrrequest)
                                    }
                                },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                ),
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    10.dp
                                )
                            ) {
                                Text(text = "Generate Answer", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
                            }
                            Spacer(modifier = Modifier.height(50.dp))
                            if (n.isNotEmpty() && alpha.isNotEmpty() && x.isNotEmpty() && y.isNotEmpty()) {
                                when (val result = rresult.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer(modifier = Modifier.height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        FloatAnswer(text = "r:", value = result.data.r)
                                        Spacer(modifier = Modifier.height(50.dp))
                                        FloatAnswer(text = "t:", value = result.data.t)
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer(text = "y': ${result.data.Y}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer(modifier = Modifier.height(50.dp))
                                        if (userId != null && !isSubmitted.value) {
                                            authViewModel.sendslr(
                                                userId = userId,
                                                n = n.toInt(),
                                                x = stoff(x),
                                                y = stoff(y),
                                                alpha = alpha.toFloat(),
                                                tail = tail,
                                                hypothesis = result.data.hypothesis,
                                                r = result.data.r,
                                                t = result.data.t,
                                                Y = result.data.Y
                                            )
                                            authViewModel.incrementcount(userId)
                                            isSubmitted.value = true
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
            WindowInfo.WindowType.Medium -> {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("SLR") {
                            scope.launch { drawerstate.open() }
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
                            Spacer(modifier = Modifier.height(50.dp))
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = { newValue ->
                                    setn(newValue)
                                    if (newValue.isNotEmpty()) {
                                        val newSize = newValue.toIntOrNull() ?: 0
                                        sety(Array(newSize) { if (it < y.size) y[it] else "" })
                                        setx(Array(newSize) { if (it < x.size) x[it] else "" })
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Significance Level:",
                                value = alpha,
                                onValueChange = { newValue ->
                                    setalpha(newValue)
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        item {
                            tail = tailSwitch (onSwitchChange = {newTail -> tail = newTail }, first = "One Tail", second = "Two Tails")
                        }
                        if (n.isNotEmpty() && alpha.isNotEmpty()) {
                            item{
                                StringAnswer(text = "X:", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "$it:",
                                    index = it,
                                    values = x,
                                    onValueChange = setx
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(50.dp))
                                StringAnswer(text = "Y:", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "$it:",
                                    index = it,
                                    values = y,
                                    onValueChange = sety
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    if(n != "" && alpha != "" && x.isNotEmpty() && y.isNotEmpty()) {
                                        val slrrequest = SLRRequest(
                                            n = n.toInt(),
                                            x = stof(x),
                                            y = stof(y),
                                            alpha = alpha.toFloat(),
                                            tail = tail
                                        )
                                        viewModel.getSLRAnswer(slrrequest)
                                    }
                                },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                ),
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    10.dp
                                )
                            ) {
                                Text(text = "Generate Answer", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
                            }
                            Spacer(modifier = Modifier.height(50.dp))
                            if (n.isNotEmpty() && alpha.isNotEmpty() && x.isNotEmpty() && y.isNotEmpty()) {
                                when (val result = rresult.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer(modifier = Modifier.height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        FloatAnswer(text = "r:", value = result.data.r)
                                        Spacer(modifier = Modifier.height(50.dp))
                                        FloatAnswer(text = "t:", value = result.data.t)
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer(text = "y': ${result.data.Y}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer(modifier = Modifier.height(50.dp))
                                        if (userId != null && !isSubmitted.value) {
                                            authViewModel.sendslr(
                                                userId = userId,
                                                n = n.toInt(),
                                                x = stoff(x),
                                                y = stoff(y),
                                                alpha = alpha.toFloat(),
                                                tail = tail,
                                                hypothesis = result.data.hypothesis,
                                                r = result.data.r,
                                                t = result.data.t,
                                                Y = result.data.Y
                                            )
                                            authViewModel.incrementcount(userId)
                                            isSubmitted.value = true
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
package com.example.kotlin_learning.view

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.HypothesisViewModel
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.data.request.HypothesisRequest
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.viewModel.rWindowInfo
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Repository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Hypothesis(
    navController: NavController,
    viewModel: HypothesisViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val repository = Repository()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val (smean, setsmean) = remember { mutableStateOf("") }
    var tail = false
    var samplem = false
    val (sd, setsd) = remember { mutableStateOf("") }
    val (hmean, sethmean) = remember { mutableStateOf("") }
    val (n, setn) = remember { mutableStateOf("") }
    val (sl, setsl) = remember { mutableStateOf("") }
    val Result = viewModel.hypothesisresult.observeAsState()
    var isSubmitted by remember { mutableStateOf(false) }
    var isupdated by remember { mutableStateOf(false) }
    var display by remember { mutableStateOf(false) }
    val userId = authViewModel.getuserid()
    val windowInfo = rWindowInfo()

    LaunchedEffect (isupdated) {
        if(isupdated) {
            val hypothesisrequest = HypothesisRequest (
                smean = smean.toFloat(),
                sl = sl.toFloat(),
                sd = sd.toFloat(),
                hmean = hmean.toFloat(),
                tail = tail,
                samplem = samplem,
                n = n.toInt()
            )
            viewModel.getHypothesisAnswer(hypothesisrequest)
            isupdated = false
        }
    }

    val drawerWidth = if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
        Modifier.fillMaxWidth(fraction = 0.8f)
    } else {
        Modifier.width(300.dp)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (modifier = Modifier
                .background(if (isSystemInDarkTheme()) Color(0xFF023047) else Color(0xFF0077B6))
                .fillMaxHeight()
                .then(drawerWidth)) {
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

        when (windowInfo.screenWidthInfo) {
            WindowInfo.WindowType.Compact -> {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Hypothesis") {
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
                                label = "Sample Mean:",
                                value = smean,
                                onValueChange = {
                                    setsmean(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            tail = tailSwitch (onSwitchChange = {newTail -> tail = newTail }, first = "One Tail", second = "Two Tails")
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Standard Deviation:",
                                value = sd,
                                onValueChange = {
                                    setsd(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            samplem = tailSwitch ({ newsamplem -> samplem = newsamplem }, first = "Sample Standard Deviation", second = "Population Standard Deviation")
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Significance Level:",
                                value = sl,
                                onValueChange = {
                                    setsl(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = {
                                    setn(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Hypothesized Mean:",
                                value = hmean,
                                onValueChange = {
                                    sethmean(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    if (smean != "" && sd != "" && hmean != "" && n != "" && sl != "") {
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
                            Spacer50()
                            if (smean.isNotEmpty() && sd.isNotEmpty() && hmean.isNotEmpty() && n.isNotEmpty() && sl.isNotEmpty() && !isupdated) {
                                when(val result = Result.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f))
                                        Spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            if(userId != null && !isSubmitted) {
                                                repository.sendhypothesis(
                                                    userId = userId,
                                                    smean = smean.toFloat(),
                                                    sl = sl.toFloat(),
                                                    sd = sd.toFloat(),
                                                    hmean = hmean.toFloat(),
                                                    tail = tail,
                                                    samplem = samplem,
                                                    n = n.toInt(),
                                                    hypothesis = result.data.hypothesis
                                                )
                                                repository.incrementcount(userId)
                                                isSubmitted = true
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
            WindowInfo.WindowType.Expanded -> {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Hypothesis") {
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
                                label = "Sample Mean:",
                                value = smean,
                                onValueChange = {
                                    setsmean(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            tail = tailSwitch (onSwitchChange = {newTail -> tail = newTail }, first = "One Tail", second = "Two Tails")
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Standard Deviation:",
                                value = sd,
                                onValueChange = {
                                    setsd(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            samplem = tailSwitch ({ newsamplem -> samplem = newsamplem }, first = "Sample Standard Deviation", second = "Population Standard Deviation")
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Significance Level:",
                                value = sl,
                                onValueChange = {
                                    setsl(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = {
                                    setn(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Hypothesized Mean:",
                                value = hmean,
                                onValueChange = {
                                    sethmean(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    if (smean != "" && sd != "" && hmean != "" && n != "" && sl != "") {
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
                            Spacer50()
                            if (smean.isNotEmpty() && sd.isNotEmpty() && hmean.isNotEmpty() && n.isNotEmpty() && sl.isNotEmpty() && !isupdated) {
                                when(val result = Result.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f))
                                        Spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            if(userId != null && !isSubmitted) {
                                                repository.sendhypothesis(
                                                    userId = userId,
                                                    smean = smean.toFloat(),
                                                    sl = sl.toFloat(),
                                                    sd = sd.toFloat(),
                                                    hmean = hmean.toFloat(),
                                                    tail = tail,
                                                    samplem = samplem,
                                                    n = n.toInt(),
                                                    hypothesis = result.data.hypothesis
                                                )
                                                repository.incrementcount(userId)
                                                isSubmitted = true
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
            WindowInfo.WindowType.Medium -> {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Hypothesis") {
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
                                label = "Sample Mean:",
                                value = smean,
                                onValueChange = {
                                    setsmean(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            tail = tailSwitch (onSwitchChange = {newTail -> tail = newTail }, first = "One Tail", second = "Two Tails")
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Standard Deviation:",
                                value = sd,
                                onValueChange = {
                                    setsd(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            samplem = tailSwitch ({ newsamplem -> samplem = newsamplem }, first = "Sample Standard Deviation", second = "Population Standard Deviation")
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Significance Level:",
                                value = sl,
                                onValueChange = {
                                    setsl(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = {
                                    setn(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Hypothesized Mean:",
                                value = hmean,
                                onValueChange = {
                                    sethmean(it)
                                    display = false
                                }
                            )
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    if (smean != "" && sd != "" && hmean != "" && n != "" && sl != "") {
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
                            Spacer50()
                            if (smean.isNotEmpty() && sd.isNotEmpty() && hmean.isNotEmpty() && n.isNotEmpty() && sl.isNotEmpty() && !isupdated) {
                                when(val result = Result.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f))
                                        Spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            if(userId != null && !isSubmitted) {
                                                repository.sendhypothesis(
                                                    userId = userId,
                                                    smean = smean.toFloat(),
                                                    sl = sl.toFloat(),
                                                    sd = sd.toFloat(),
                                                    hmean = hmean.toFloat(),
                                                    tail = tail,
                                                    samplem = samplem,
                                                    n = n.toInt(),
                                                    hypothesis = result.data.hypothesis
                                                )
                                                repository.incrementcount(userId)
                                                isSubmitted = true
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
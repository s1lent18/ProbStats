package com.example.kotlin_learning.view

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import com.example.kotlin_learning.viewModel.GroupedViewModel
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.data.request.GroupedRequest
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
fun Grouped(
        navController: NavController,
        viewModel: GroupedViewModel,
        authViewModel: AuthViewModel = viewModel()
    ) {
    val repository = Repository()
    val drawerstate = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val (first, setfirst) = remember { mutableStateOf(arrayOf("")) }
    val (second, setsecond) = remember { mutableStateOf(arrayOf("")) }
    val (n, setn) = remember { mutableStateOf("") }
    val (freq, setfreq) = remember { mutableStateOf(arrayOf("")) }
    val basic = viewModel.groupedresult.observeAsState()
    var isSubmitted by remember { mutableStateOf(false) }
    var isupdated by remember { mutableStateOf(false) }
    var display by remember { mutableStateOf(false) }
    val userId = authViewModel.getuserid()

    LaunchedEffect(isupdated) {
        if(isupdated) {
            val groupedrequest = GroupedRequest(
                first = stof(first),
                second = stof(second),
                freq = stof(freq)
            )
            viewModel.getGroupedAnswer(groupedrequest)
            isupdated = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerstate,
        drawerContent = {
            ModalDrawerSheet (modifier = Modifier
                .background(if (isSystemInDarkTheme()) Color(0xFF023047) else Color(0xFF0077B6))
                .fillMaxHeight()
                .fillMaxWidth(fraction = 0.8f)) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
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
            WindowInfo.WindowType.Compact -> {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Grouped") {
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
                            Spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = { newValue ->
                                    setn(newValue)
                                    setfirst(Array(newValue.toIntOrNull() ?: 0) { if (it < first.size) first[it] else "" })
                                    setsecond(Array(newValue.toIntOrNull() ?: 0) { if (it < second.size) second[it] else "" })
                                    setfreq(Array(newValue.toIntOrNull() ?: 0) { if ( it < freq.size) freq[it] else ""})
                                    display = false
                                }
                            )
                            Spacer50()
                        }
                        if (n.isNotEmpty()) {
                            items(n.toInt()) {
                                Row (
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ArrayInput(
                                        modifier = Modifier.weight(1f),
                                        label = "Starting",
                                        values = first,
                                        index = it,
                                        onValueChange = {
                                            setfirst(it)
                                            display = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    ArrayInput(
                                        modifier = Modifier.weight(1f),
                                        label = "Ending",
                                        values = second,
                                        index = it,
                                        onValueChange = {
                                            setsecond(it)
                                            display = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    ArrayInput(
                                        modifier = Modifier.weight(1f),
                                        label = "Frequency",
                                        values = freq,
                                        index = it,
                                        onValueChange = {
                                            setfreq(it)
                                            display = false
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    isSubmitted = false
                                    isupdated = true
                                    display = true
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
                            Spacer50()
                            if (first.isNotEmpty() && second.isNotEmpty() && freq.isNotEmpty() && !isupdated) {
                                when (val result = basic.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer("Failed To Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                        Spacer50()
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            FloatAnswer(text = "Mean:", value = result.data.mean)
                                            Spacer50()
                                            StringAnswer(text = "Median: ${result.data.median}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            StringAnswer(text = "Mode: ${result.data.mode}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            FloatAnswer(text = "Standard Deviation:", value = result.data.sd)
                                            Spacer50()
                                            FloatAnswer(text = "Variance:", value = result.data.variance)
                                            Spacer50()
                                            if(!isSubmitted && userId != null) {
                                                repository.sendgrouped(
                                                    userId = userId,
                                                    n = stoff(first),
                                                    second = stoff(second),
                                                    freq = stoff(freq),
                                                    mean = result.data.mean,
                                                    median = result.data.median,
                                                    mode = result.data.mode,
                                                    sd = result.data.sd,
                                                    variance = result.data.variance
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
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Grouped") {
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
                            Spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = { newValue ->
                                    setn(newValue)
                                    setfirst(Array(newValue.toIntOrNull() ?: 0) { if (it < first.size) first[it] else "" })
                                    setsecond(Array(newValue.toIntOrNull() ?: 0) { if (it < second.size) second[it] else "" })
                                    setfreq(Array(newValue.toIntOrNull() ?: 0) { if ( it < freq.size) freq[it] else ""})
                                    display = false
                                }
                            )
                            Spacer50()
                        }
                        if (n.isNotEmpty()) {
                            items(n.toInt()) {
                                Row (
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ArrayInput(
                                        modifier = Modifier.weight(1f),
                                        label = "Starting",
                                        values = first,
                                        index = it,
                                        onValueChange = {
                                            setfirst(it)
                                            display = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    ArrayInput(
                                        modifier = Modifier.weight(1f),
                                        label = "Ending",
                                        values = second,
                                        index = it,
                                        onValueChange = {
                                            setsecond(it)
                                            display = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    ArrayInput(
                                        modifier = Modifier.weight(1f),
                                        label = "Frequency",
                                        values = freq,
                                        index = it,
                                        onValueChange = {
                                            setfreq(it)
                                            display = false
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    isSubmitted = false
                                    isupdated = true
                                    display = true
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
                            Spacer50()
                            if (first.isNotEmpty() && second.isNotEmpty() && freq.isNotEmpty() && !isupdated) {
                                when (val result = basic.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer("Failed To Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                        Spacer50()
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            FloatAnswer(text = "Mean:", value = result.data.mean)
                                            Spacer50()
                                            StringAnswer(text = "Median: ${result.data.median}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            StringAnswer(text = "Mode: ${result.data.mode}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            FloatAnswer(text = "Standard Deviation:", value = result.data.sd)
                                            Spacer50()
                                            FloatAnswer(text = "Variance:", value = result.data.variance)
                                            Spacer50()
                                            if(!isSubmitted && userId != null) {
                                                repository.sendgrouped(
                                                    userId = userId,
                                                    n = stoff(first),
                                                    second = stoff(second),
                                                    freq = stoff(freq),
                                                    mean = result.data.mean,
                                                    median = result.data.median,
                                                    mode = result.data.mode,
                                                    sd = result.data.sd,
                                                    variance = result.data.variance
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
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Grouped") {
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
                            Spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = { newValue ->
                                    setn(newValue)
                                    setfirst(Array(newValue.toIntOrNull() ?: 0) { if (it < first.size) first[it] else "" })
                                    setsecond(Array(newValue.toIntOrNull() ?: 0) { if (it < second.size) second[it] else "" })
                                    setfreq(Array(newValue.toIntOrNull() ?: 0) { if ( it < freq.size) freq[it] else ""})
                                    display = false
                                }
                            )
                            Spacer50()
                        }
                        if (n.isNotEmpty()) {
                            items(n.toInt()) {
                                Row (
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ArrayInput(
                                        modifier = Modifier.weight(1f),
                                        label = "Starting",
                                        values = first,
                                        index = it,
                                        onValueChange = {
                                            setfirst(it)
                                            display = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    ArrayInput(
                                        modifier = Modifier.weight(1f),
                                        label = "Ending",
                                        values = second,
                                        index = it,
                                        onValueChange = {
                                            setsecond(it)
                                            display = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    ArrayInput(
                                        modifier = Modifier.weight(1f),
                                        label = "Frequency",
                                        values = freq,
                                        index = it,
                                        onValueChange = {
                                            setfreq(it)
                                            display = false
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    isSubmitted = false
                                    isupdated = true
                                    display = true
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
                            Spacer50()
                            if (first.isNotEmpty() && second.isNotEmpty() && freq.isNotEmpty() && !isupdated) {
                                when (val result = basic.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer("Failed To Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        Spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                        Spacer50()
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            FloatAnswer(text = "Mean:", value = result.data.mean)
                                            Spacer50()
                                            StringAnswer(text = "Median: ${result.data.median}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            StringAnswer(text = "Mode: ${result.data.mode}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            FloatAnswer(text = "Standard Deviation:", value = result.data.sd)
                                            Spacer50()
                                            FloatAnswer(text = "Variance:", value = result.data.variance)
                                            Spacer50()
                                            if(!isSubmitted && userId != null) {
                                                repository.sendgrouped(
                                                    userId = userId,
                                                    n = stoff(first),
                                                    second = stoff(second),
                                                    freq = stoff(freq),
                                                    mean = result.data.mean,
                                                    median = result.data.median,
                                                    mode = result.data.mode,
                                                    sd = result.data.sd,
                                                    variance = result.data.variance
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
package com.example.kotlin_learning.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.HorizontalDivider
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
import com.example.kotlin_learning.viewModel.UnGroupedViewModel
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.request.UnGroupedRequest
import com.example.kotlin_learning.viewModel.rWindowInfo
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnGrouped(
    navController: NavController,
    viewModel: UnGroupedViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val drawerstate = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val (size, setsize) = remember { mutableStateOf("") }
    val (n, setn) = remember { mutableStateOf(arrayOf("")) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val basic = viewModel.ungroupedresult.observeAsState()
    val userId = authViewModel.getuserid()
    var isSubmitted by remember { mutableStateOf(false) }
    var isupdated by remember { mutableStateOf(false) }
    var display by remember { mutableStateOf(false) }

    LaunchedEffect(isupdated) {
        if(isupdated) {
            val ungroupedrequest = UnGroupedRequest(
                n = stof(n)
            )
            viewModel.getUnGroupedAnswer(ungroupedrequest)
            isupdated = false
        }
    }

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
                        Appbar("UnGrouped") {
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
                                value = size,
                                onValueChange = { newValue ->
                                    setsize(newValue)
                                    setn(Array(newValue.toIntOrNull() ?: 0) { if (it < n.size) n[it] else "" })
                                    display = false
                                }
                            )
                            Spacer50()
                        }
                        if (size.isNotEmpty()) {
                            items(size.toInt()) { p ->
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "$p:",
                                    index = p,
                                    values = n,
                                    onValueChange = {
                                        setn(it)
                                        display = false
                                    }
                                )
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
                            if (size.isNotEmpty() && n.isNotEmpty() && !isupdated) {
                                when (val result = basic.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
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
                                            FloatAnswer(text = "Median:", value = result.data.median)
                                            Spacer50()
                                            StringAnswer(text = "Mode: ${result.data.mode}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            FloatAnswer(text = "Standard Deviation:", value = result.data.sd)
                                            Spacer50()
                                            FloatAnswer(text = "Variance:", value = result.data.variance)
                                            Spacer50()
                                            FloatAnswer(text = "µ±1σ:", value = result.data.one)
                                            Spacer50()
                                            FloatAnswer(text = "µ±2σ:", value = result.data.two)
                                            Spacer50()
                                            FloatAnswer(text = "µ±3σ:", value = result.data.three)
                                            Spacer50()
                                            FloatAnswer(text = "First Quartile:", value = result.data.q1)
                                            Spacer50()
                                            FloatAnswer(text = "Third Quartile:", value = result.data.q3)
                                            Spacer50()
                                            StringAnswer(text = "Shape of The Distribution: ${result.data.Shape_of_the_Distribution}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            StringAnswer(text = "Stem Leaf", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Card (
                                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                                shape = RoundedCornerShape(20.dp),
                                                elevation = CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                                ),
                                                border = BorderStroke(1.dp, color = Color.Blue)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxHeight(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .weight(1f),
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        for (i in result.data.stemleaf) {
                                                            Spacer(modifier = Modifier.height(20.dp))
                                                            Text(text = i.key)
                                                        }
                                                        Spacer(modifier = Modifier.height(20.dp))
                                                    }
                                                    HorizontalDivider(
                                                        color = Color.Black,
                                                        modifier = Modifier
                                                            .fillMaxHeight(fraction = 0.8f)
                                                            .width(1.dp)
                                                    )
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .weight(1f),
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.Start
                                                    ) {
                                                        for (i in result.data.stemleaf) {
                                                            Spacer(modifier = Modifier.height(20.dp))
                                                            Row (
                                                                horizontalArrangement = Arrangement.SpaceAround
                                                            ){
                                                                for(j in i.value) {
                                                                    Text(text = "$j ")
                                                                }
                                                            }

                                                        }
                                                        Spacer(modifier = Modifier.height(20.dp))
                                                    }
                                                }
                                            }
                                            Spacer50()
                                            if(userId != null && !isSubmitted) {
                                                val convertedStemLeaf: Map<String, List<Float>> = result.data.stemleaf.mapValues { entry ->
                                                    entry.value.toList()
                                                }
                                                val listAny: List<Any> = convertedStemLeaf.flatMap { entry ->
                                                    listOf(entry.key) + entry.value
                                                }
                                                authViewModel.sendungrouped(
                                                    userId,
                                                    n = stoff(n),
                                                    shape = result.data.Shape_of_the_Distribution,
                                                    mean = result.data.mean,
                                                    median = result.data.median,
                                                    mode = result.data.mode,
                                                    one = result.data.one,
                                                    two = result.data.two,
                                                    three = result.data.three,
                                                    q1 = result.data.q1,
                                                    q3 = result.data.q3,
                                                    variance = result.data.variance,
                                                    stemleaf = listAny,
                                                    sd = result.data.sd
                                                )
                                                authViewModel.incrementcount(userId)
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
                        Appbar("UnGrouped") {
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
                                value = size,
                                onValueChange = { newValue ->
                                    setsize(newValue)
                                    setn(Array(newValue.toIntOrNull() ?: 0) { if (it < n.size) n[it] else "" })
                                    display = false
                                }
                            )
                            Spacer50()
                        }
                        if (size.isNotEmpty()) {
                            items(size.toInt()) { p ->
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "$p:",
                                    index = p,
                                    values = n,
                                    onValueChange = {
                                        setn(it)
                                        display = false
                                    }
                                )
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
                            if (size.isNotEmpty() && n.isNotEmpty() && !isupdated) {
                                when (val result = basic.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
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
                                            FloatAnswer(text = "Median:", value = result.data.median)
                                            Spacer50()
                                            StringAnswer(text = "Mode: ${result.data.mode}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            FloatAnswer(text = "Standard Deviation:", value = result.data.sd)
                                            Spacer50()
                                            FloatAnswer(text = "Variance:", value = result.data.variance)
                                            Spacer50()
                                            FloatAnswer(text = "µ±1σ:", value = result.data.one)
                                            Spacer50()
                                            FloatAnswer(text = "µ±2σ:", value = result.data.two)
                                            Spacer50()
                                            FloatAnswer(text = "µ±3σ:", value = result.data.three)
                                            Spacer50()
                                            FloatAnswer(text = "First Quartile:", value = result.data.q1)
                                            Spacer50()
                                            FloatAnswer(text = "Third Quartile:", value = result.data.q3)
                                            Spacer50()
                                            StringAnswer(text = "Shape of The Distribution: ${result.data.Shape_of_the_Distribution}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            StringAnswer(text = "Stem Leaf", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Card (
                                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                                shape = RoundedCornerShape(20.dp),
                                                elevation = CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                                ),
                                                border = BorderStroke(1.dp, color = Color.Blue)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxHeight(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .weight(1f),
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        for (i in result.data.stemleaf) {
                                                            Spacer(modifier = Modifier.height(20.dp))
                                                            Text(text = i.key)
                                                        }
                                                        Spacer(modifier = Modifier.height(20.dp))
                                                    }
                                                    HorizontalDivider(
                                                        color = Color.Black,
                                                        modifier = Modifier
                                                            .fillMaxHeight(fraction = 0.8f)
                                                            .width(1.dp)
                                                    )
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .weight(1f),
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.Start
                                                    ) {
                                                        for (i in result.data.stemleaf) {
                                                            Spacer(modifier = Modifier.height(20.dp))
                                                            Row (
                                                                horizontalArrangement = Arrangement.SpaceAround
                                                            ){
                                                                for(j in i.value) {
                                                                    Text(text = "$j ")
                                                                }
                                                            }

                                                        }
                                                        Spacer(modifier = Modifier.height(20.dp))
                                                    }
                                                }
                                            }
                                            Spacer50()
                                            if(userId != null && !isSubmitted) {
                                                val convertedStemLeaf: Map<String, List<Float>> = result.data.stemleaf.mapValues { entry ->
                                                    entry.value.toList()
                                                }
                                                val listAny: List<Any> = convertedStemLeaf.flatMap { entry ->
                                                    // Create a list containing the key and its associated list
                                                    listOf(entry.key) + entry.value
                                                }
                                                authViewModel.sendungrouped(
                                                    userId,
                                                    n = stoff(n),
                                                    shape = result.data.Shape_of_the_Distribution,
                                                    mean = result.data.mean,
                                                    median = result.data.median,
                                                    mode = result.data.mode,
                                                    one = result.data.one,
                                                    two = result.data.two,
                                                    three = result.data.three,
                                                    q1 = result.data.q1,
                                                    q3 = result.data.q3,
                                                    variance = result.data.variance,
                                                    stemleaf = listAny,
                                                    sd = result.data.sd
                                                )
                                                authViewModel.incrementcount(userId)
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
                        Appbar("UnGrouped") {
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
                                value = size,
                                onValueChange = { newValue ->
                                    setsize(newValue)
                                    setn(Array(newValue.toIntOrNull() ?: 0) { if (it < n.size) n[it] else "" })
                                    display = false
                                }
                            )
                            Spacer50()
                        }
                        if (size.isNotEmpty()) {
                            items(size.toInt()) { p ->
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "$p:",
                                    index = p,
                                    values = n,
                                    onValueChange = {
                                        setn(it)
                                        display = false
                                    }
                                )
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
                            if (size.isNotEmpty() && n.isNotEmpty() && !isupdated) {
                                when (val result = basic.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
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
                                            FloatAnswer(text = "Median:", value = result.data.median)
                                            Spacer50()
                                            StringAnswer(text = "Mode: ${result.data.mode}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            FloatAnswer(text = "Standard Deviation:", value = result.data.sd)
                                            Spacer50()
                                            FloatAnswer(text = "Variance:", value = result.data.variance)
                                            Spacer50()
                                            FloatAnswer(text = "µ±1σ:", value = result.data.one)
                                            Spacer50()
                                            FloatAnswer(text = "µ±2σ:", value = result.data.two)
                                            Spacer50()
                                            FloatAnswer(text = "µ±3σ:", value = result.data.three)
                                            Spacer50()
                                            FloatAnswer(text = "First Quartile:", value = result.data.q1)
                                            Spacer50()
                                            FloatAnswer(text = "Third Quartile:", value = result.data.q3)
                                            Spacer50()
                                            StringAnswer(text = "Shape of The Distribution: ${result.data.Shape_of_the_Distribution}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer50()
                                            StringAnswer(text = "Stem Leaf", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Card (
                                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                                shape = RoundedCornerShape(20.dp),
                                                elevation = CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                                                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                                                ),
                                                border = BorderStroke(1.dp, color = Color.Blue)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxHeight(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .weight(1f),
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        for (i in result.data.stemleaf) {
                                                            Spacer(modifier = Modifier.height(20.dp))
                                                            Text(text = i.key)
                                                        }
                                                        Spacer(modifier = Modifier.height(20.dp))
                                                    }
                                                    HorizontalDivider(
                                                        color = Color.Black,
                                                        modifier = Modifier
                                                            .fillMaxHeight(fraction = 0.8f)
                                                            .width(1.dp)
                                                    )
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .weight(1f),
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.Start
                                                    ) {
                                                        for (i in result.data.stemleaf) {
                                                            Spacer(modifier = Modifier.height(20.dp))
                                                            Row (
                                                                horizontalArrangement = Arrangement.SpaceAround
                                                            ){
                                                                for(j in i.value) {
                                                                    Text(text = "$j ")
                                                                }
                                                            }

                                                        }
                                                        Spacer(modifier = Modifier.height(20.dp))
                                                    }
                                                }
                                            }
                                            Spacer50()
                                            if(userId != null && !isSubmitted) {
                                                val convertedStemLeaf: Map<String, List<Float>> = result.data.stemleaf.mapValues { entry ->
                                                    entry.value.toList()
                                                }
                                                val listAny: List<Any> = convertedStemLeaf.flatMap { entry ->
                                                    listOf(entry.key) + entry.value
                                                }
                                                authViewModel.sendungrouped(
                                                    userId,
                                                    n = stoff(n),
                                                    shape = result.data.Shape_of_the_Distribution,
                                                    mean = result.data.mean,
                                                    median = result.data.median,
                                                    mode = result.data.mode,
                                                    one = result.data.one,
                                                    two = result.data.two,
                                                    three = result.data.three,
                                                    q1 = result.data.q1,
                                                    q3 = result.data.q3,
                                                    variance = result.data.variance,
                                                    stemleaf = listAny,
                                                    sd = result.data.sd
                                                )
                                                authViewModel.incrementcount(userId)
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
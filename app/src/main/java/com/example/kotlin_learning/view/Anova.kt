package com.example.kotlin_learning.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.AnovaViewModel
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.data.request.AnovaRequest
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.viewModel.rWindowInfo
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Repository
import kotlinx.coroutines.launch

@Composable
fun RowScope.TableCell(text: String, weight: Float, fontWeight: FontWeight? = null) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp),
        fontSize = 12.sp,
        fontWeight = fontWeight
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Anova(
    navController: NavController,
    viewModel: AnovaViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val repository = Repository()
    val drawerstate = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val (arrayCount, setarrayCount) = remember { mutableStateOf("") }
    val (arraySize, setarraySize) = remember { mutableStateOf("") }
    var (n, setn) = remember { mutableStateOf(arrayOf("")) }
    val (sl, setsl) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val probability = viewModel.anovaresult.observeAsState()
    var isSubmitted by remember { mutableStateOf(false) }
    var isupdated by remember { mutableStateOf(false) }
    var display by remember { mutableStateOf(false) }
    val userId = authViewModel.getuserid()

    LaunchedEffect (isupdated) {
        if (isupdated) {
            val anovaRequest = AnovaRequest(
                n = stof(n),
                size = arraySize.toIntOrNull() ?: 0,
                sl = sl.toFloat()
            )
            viewModel.getAnovaAnswer(anovaRequest)
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
                        Appbar("Anova") {
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
                                label = "Significance level:",
                                value = sl,
                                onValueChange = { newValue ->
                                    setsl(newValue)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Number of Arrays in 2D Array:",
                                value = arrayCount,
                                onValueChange = { newValue ->
                                    setarrayCount(newValue)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "The size of the arrays:",
                                value = arraySize,
                                onValueChange = { newValue ->
                                    setarraySize(newValue)
                                    display = false
                                }
                            )
                            Spacer50()
                        }
                        if (arrayCount.isNotEmpty() && arraySize.isNotEmpty()) {
                            val count = arrayCount.toIntOrNull() ?: 0
                            val size = arraySize.toIntOrNull() ?: 0
                            val newsize = count * size
                            if (n.size != newsize) {
                                n = Array(newsize) { "" }
                            }
                            var i = 0
                            var k = 0
                            items(newsize) { j ->
                                if( k == size) {
                                    k = 0
                                    i++
                                }
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "n[$i][$k]:",
                                    index = j,
                                    values = n,
                                    onValueChange = {
                                        setn(it)
                                        display = false
                                    }
                                )
                                k++
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    isSubmitted = false
                                    isupdated = true
                                    display = true
                                    keyboardController?.hide()
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
                            if (arrayCount.isNotEmpty() && arraySize.isNotEmpty() && n.isNotEmpty() && !isupdated) {
                                when (val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer("Failed To Load",
                                            Modifier
                                                .fillMaxWidth(fraction = 0.9f)
                                                .height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
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
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    // Table Header
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background( if (isSystemInDarkTheme()) Color(0xFFCCC5B9) else Color(0xFF124559), shape = RoundedCornerShape(4.dp))
                                                            .padding(8.dp)
                                                    ) {
                                                        TableCell(text = "S.V.", weight = 1f, fontWeight = FontWeight.Bold)
                                                        TableCell(text = "SS", weight = 1f, fontWeight = FontWeight.Bold)
                                                        TableCell(text = "d.f.", weight = 1f, fontWeight = FontWeight.Bold)
                                                        TableCell(text = "MS", weight = 1f, fontWeight = FontWeight.Bold)
                                                        TableCell(text = "F-ratio", weight = 1f, fontWeight = FontWeight.Bold)
                                                    }

                                                    // Table Rows
                                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                                        TableCell(text = "Between", weight = 1f)
                                                        TableCell(text = "${result.data.SSB}", weight = 1f)
                                                        TableCell(text = "${result.data.dfB}", weight = 1f)
                                                        TableCell(text = "${result.data.MSB}", weight = 1f)
                                                        TableCell(text = "${result.data.fratio}", weight = 1f)
                                                    }
                                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                                        TableCell(text = "Within (error)", weight = 1f)
                                                        TableCell(text = "${result.data.SSW}", weight = 1f)
                                                        TableCell(text = "${result.data.dfW}", weight = 1f)
                                                        TableCell(text = "${result.data.MSW}", weight = 1f)
                                                        TableCell(text = "", weight = 1f)
                                                    }
                                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                                        TableCell(text = "Total", weight = 1f)
                                                        TableCell(text = "${result.data.SSB + result.data.SSW}", weight = 1f)
                                                        TableCell(text = "${result.data.dfB + result.data.dfW}", weight = 1f)
                                                        TableCell(text = "", weight = 1f)
                                                        TableCell(text = "", weight = 1f)
                                                    }
                                                    if (!isSubmitted && userId != null) {
                                                        repository.sendanova(
                                                            userId = userId,
                                                            n = stoff(n),
                                                            sl = sl.toFloat(),
                                                            SSB = result.data.SSB,
                                                            SSW = result.data.SSW,
                                                            MSB = result.data.MSB,
                                                            MSW = result.data.MSW,
                                                            dfB = result.data.dfB,
                                                            dfW = result.data.dfW,
                                                            fratio = result.data.fratio,
                                                            hypothesis = result.data.hypothesis,
                                                            size = arraySize.toInt()
                                                        )
                                                        repository.incrementcount(userId)
                                                        isSubmitted = true
                                                    }
                                                }
                                            }
                                            Spacer50()
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
                        Appbar("Anova") {
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
                                label = "Significance level:",
                                value = sl,
                                onValueChange = { newValue ->
                                    setsl(newValue)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Number of Arrays in 2D Array:",
                                value = arrayCount,
                                onValueChange = { newValue ->
                                    setarrayCount(newValue)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "The size of the arrays:",
                                value = arraySize,
                                onValueChange = { newValue ->
                                    setarraySize(newValue)
                                    display = false
                                }
                            )
                            Spacer50()
                        }
                        if (arrayCount.isNotEmpty() && arraySize.isNotEmpty()) {
                            val count = arrayCount.toIntOrNull() ?: 0
                            val size = arraySize.toIntOrNull() ?: 0
                            val newsize = count * size
                            if (n.size != newsize) {
                                n = Array(newsize) { "" }
                            }
                            var i = 0
                            var k = 0
                            items(newsize) { j ->
                                if( k == size) {
                                    k = 0
                                    i++
                                }
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "n[$i][$k]:",
                                    index = j,
                                    values = n,
                                    onValueChange = {
                                        setn(it)
                                        display = false
                                    }
                                )
                                k++
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    isSubmitted = false
                                    isupdated = true
                                    display = true
                                    keyboardController?.hide()
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
                            if (arrayCount.isNotEmpty() && arraySize.isNotEmpty() && n.isNotEmpty() && !isupdated) {
                                when (val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer("Failed To Load",
                                            Modifier
                                                .fillMaxWidth(fraction = 0.9f)
                                                .height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            FloatAnswer(text = "SS [Between]:", value = result.data.SSB)
                                            Spacer50()
                                            FloatAnswer(text = "SS [Within]:", value = result.data.SSW)
                                            Spacer50()
                                            FloatAnswer(text = "d.f [Between]:", value = result.data.dfB.toFloat())
                                            Spacer50()
                                            FloatAnswer(text = "d.f [Within]:", value = result.data.dfW.toFloat())
                                            Spacer50()
                                            FloatAnswer(text = "MS [Between]:", value = result.data.MSB)
                                            Spacer50()
                                            FloatAnswer(text = "MS [Within]:", value = result.data.MSW)
                                            Spacer50()
                                            FloatAnswer(text = "F Ratio:", value = result.data.fratio)
                                            Spacer50()
                                            StringAnswer(text = "Hypothesis: ${result.data.hypothesis}",
                                                Modifier
                                                    .fillMaxWidth(fraction = 0.9f)
                                                    .height(50.dp))
                                            Spacer50()
                                            if (!isSubmitted && userId != null) {
                                                repository.sendanova(
                                                    userId = userId,
                                                    n = stoff(n),
                                                    sl = sl.toFloat(),
                                                    SSB = result.data.SSB,
                                                    SSW = result.data.SSW,
                                                    MSB = result.data.MSB,
                                                    MSW = result.data.MSW,
                                                    dfB = result.data.dfB,
                                                    dfW = result.data.dfW,
                                                    fratio = result.data.fratio,
                                                    hypothesis = result.data.hypothesis,
                                                    size = arraySize.toInt()
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
                        Appbar("Anova") {
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
                                label = "Significance level:",
                                value = sl,
                                onValueChange = { newValue ->
                                    setsl(newValue)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Number of Arrays in 2D Array:",
                                value = arrayCount,
                                onValueChange = { newValue ->
                                    setarrayCount(newValue)
                                    display = false
                                }
                            )
                            Spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "The size of the arrays:",
                                value = arraySize,
                                onValueChange = { newValue ->
                                    setarraySize(newValue)
                                    display = false
                                }
                            )
                            Spacer50()
                        }
                        if (arrayCount.isNotEmpty() && arraySize.isNotEmpty()) {
                            val count = arrayCount.toIntOrNull() ?: 0
                            val size = arraySize.toIntOrNull() ?: 0
                            val newsize = count * size
                            if (n.size != newsize) {
                                n = Array(newsize) { "" }
                            }
                            var i = 0
                            var k = 0
                            items(newsize) { j ->
                                if( k == size) {
                                    k = 0
                                    i++
                                }
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    label = "n[$i][$k]:",
                                    index = j,
                                    values = n,
                                    onValueChange = {
                                        setn(it)
                                        display = false
                                    }
                                )
                                k++
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            Spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    isSubmitted = false
                                    isupdated = true
                                    display = true
                                    keyboardController?.hide()
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
                            if (arrayCount.isNotEmpty() && arraySize.isNotEmpty() && n.isNotEmpty() && !isupdated) {
                                when (val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer50()
                                        StringAnswer("Failed To Load",
                                            Modifier
                                                .fillMaxWidth(fraction = 0.9f)
                                                .height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        if (display) {
                                            Spacer50()
                                            FloatAnswer(text = "SS [Between]:", value = result.data.SSB)
                                            Spacer50()
                                            FloatAnswer(text = "SS [Within]:", value = result.data.SSW)
                                            Spacer50()
                                            FloatAnswer(text = "d.f [Between]:", value = result.data.dfB.toFloat())
                                            Spacer50()
                                            FloatAnswer(text = "d.f [Within]:", value = result.data.dfW.toFloat())
                                            Spacer50()
                                            FloatAnswer(text = "MS [Between]:", value = result.data.MSB)
                                            Spacer50()
                                            FloatAnswer(text = "MS [Within]:", value = result.data.MSW)
                                            Spacer50()
                                            FloatAnswer(text = "F Ratio:", value = result.data.fratio)
                                            Spacer50()
                                            StringAnswer(text = "Hypothesis: ${result.data.hypothesis}",
                                                Modifier
                                                    .fillMaxWidth(fraction = 0.9f)
                                                    .height(50.dp))
                                            Spacer50()
                                            if (!isSubmitted && userId != null) {
                                                authViewModel.sendanova(
                                                    userId = userId,
                                                    n = stoff(n),
                                                    sl = sl.toFloat(),
                                                    SSB = result.data.SSB,
                                                    SSW = result.data.SSW,
                                                    MSB = result.data.MSB,
                                                    MSW = result.data.MSW,
                                                    dfB = result.data.dfB,
                                                    dfW = result.data.dfW,
                                                    fratio = result.data.fratio,
                                                    hypothesis = result.data.hypothesis,
                                                    size = arraySize.toInt()
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
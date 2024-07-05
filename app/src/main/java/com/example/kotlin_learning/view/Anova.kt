package com.example.kotlin_learning.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Anova(
    navController: NavController,
    viewModel: AnovaViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val drawerstate = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val (arrayCount, setarrayCount) = remember { mutableStateOf("") }
    val (arraySize, setarraySize) = remember { mutableStateOf("") }
    var (n, setn) = remember { mutableStateOf(arrayOf("")) }
    val (sl, setsl) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val probability = viewModel.anovaresult.observeAsState()
    val isSubmitted = remember { mutableStateOf(false) }
    val userId = authViewModel.getuserid()

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
                        Text(
                            text = "Account",
                            color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            modifier = Modifier.clickable {
                                navController.navigate(route = Screen.Home.route)
                            },
                            text = "Calculators",
                            color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "History",
                            color = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            modifier = Modifier.clickable {
                                navController.navigate(route = Screen.Login.route)
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
                            spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Significance level:",
                                value = sl,
                                onValueChange = { newValue ->
                                    setsl(newValue)
                                }
                            )
                            spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Number of Arrays in 2D Array:",
                                value = arrayCount,
                                onValueChange = { newValue ->
                                    setarrayCount(newValue)
                                }
                            )
                            spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "The size of the arrays:",
                                value = arraySize,
                                onValueChange = { newValue ->
                                    setarraySize(newValue)
                                }
                            )
                            spacer50()
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
                                    onValueChange = setn
                                )
                                k++
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                        val anovaRequest = AnovaRequest(
                                            n = stof(n),
                                            size = arraySize.toIntOrNull() ?: 0,
                                            sl = sl.toFloat()
                                        )
                                        viewModel.getAnovaAnswer(anovaRequest)
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
                            spacer50()
                            if (arrayCount.isNotEmpty() && arraySize.isNotEmpty() && n.isNotEmpty()) {
                                when (val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        spacer50()
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        spacer50()
                                        FloatAnswer(text = "SS [Between]:", value = result.data.SSB)
                                        spacer50()
                                        FloatAnswer(text = "SS [Within]:", value = result.data.SSW)
                                        spacer50()
                                        FloatAnswer(text = "d.f [Between]:", value = result.data.dfB.toFloat())
                                        spacer50()
                                        FloatAnswer(text = "d.f [Within]:", value = result.data.dfW.toFloat())
                                        spacer50()
                                        FloatAnswer(text = "MS [Between]:", value = result.data.MSB)
                                        spacer50()
                                        FloatAnswer(text = "MS [Within]:", value = result.data.MSW)
                                        spacer50()
                                        FloatAnswer(text = "F Ratio:", value = result.data.fratio)
                                        spacer50()
                                        StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        spacer50()
                                        if (!isSubmitted.value && userId != null) {
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
                            spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Significance level:",
                                value = sl,
                                onValueChange = { newValue ->
                                    setsl(newValue)
                                }
                            )
                            spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Number of Arrays in 2D Array:",
                                value = arrayCount,
                                onValueChange = { newValue ->
                                    setarrayCount(newValue)
                                }
                            )
                            spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "The size of the arrays:",
                                value = arraySize,
                                onValueChange = { newValue ->
                                    setarraySize(newValue)
                                }
                            )
                            spacer50()
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
                                    onValueChange = setn
                                )
                                k++
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    val anovaRequest = AnovaRequest(
                                        n = stof(n),
                                        size = arraySize.toIntOrNull() ?: 0,
                                        sl = sl.toFloat()
                                    )
                                    viewModel.getAnovaAnswer(anovaRequest)
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
                            spacer50()
                            if (arrayCount.isNotEmpty() && arraySize.isNotEmpty() && n.isNotEmpty()) {
                                when (val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        spacer50()
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        spacer50()
                                        FloatAnswer(text = "SS [Between]:", value = result.data.SSB)
                                        spacer50()
                                        FloatAnswer(text = "SS [Within]:", value = result.data.SSW)
                                        spacer50()
                                        FloatAnswer(text = "d.f [Between]:", value = result.data.dfB.toFloat())
                                        spacer50()
                                        FloatAnswer(text = "d.f [Within]:", value = result.data.dfW.toFloat())
                                        spacer50()
                                        FloatAnswer(text = "MS [Between]:", value = result.data.MSB)
                                        spacer50()
                                        FloatAnswer(text = "MS [Within]:", value = result.data.MSW)
                                        spacer50()
                                        FloatAnswer(text = "F Ratio:", value = result.data.fratio)
                                        spacer50()
                                        StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        spacer50()
                                        if (!isSubmitted.value && userId != null) {
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
                            spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Significance level:",
                                value = sl,
                                onValueChange = { newValue ->
                                    setsl(newValue)
                                }
                            )
                            spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "Number of Arrays in 2D Array:",
                                value = arrayCount,
                                onValueChange = { newValue ->
                                    setarrayCount(newValue)
                                }
                            )
                            spacer50()
                            Floatinput(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                label = "The size of the arrays:",
                                value = arraySize,
                                onValueChange = { newValue ->
                                    setarraySize(newValue)
                                }
                            )
                            spacer50()
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
                                    onValueChange = setn
                                )
                                k++
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    val anovaRequest = AnovaRequest(
                                        n = stof(n),
                                        size = arraySize.toIntOrNull() ?: 0,
                                        sl = sl.toFloat()
                                    )
                                    viewModel.getAnovaAnswer(anovaRequest)
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
                            spacer50()
                            if (arrayCount.isNotEmpty() && arraySize.isNotEmpty() && n.isNotEmpty()) {
                                when (val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        spacer50()
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        spacer50()
                                        FloatAnswer(text = "SS [Between]:", value = result.data.SSB)
                                        spacer50()
                                        FloatAnswer(text = "SS [Within]:", value = result.data.SSW)
                                        spacer50()
                                        FloatAnswer(text = "d.f [Between]:", value = result.data.dfB.toFloat())
                                        spacer50()
                                        FloatAnswer(text = "d.f [Within]:", value = result.data.dfW.toFloat())
                                        spacer50()
                                        FloatAnswer(text = "MS [Between]:", value = result.data.MSB)
                                        spacer50()
                                        FloatAnswer(text = "MS [Within]:", value = result.data.MSW)
                                        spacer50()
                                        FloatAnswer(text = "F Ratio:", value = result.data.fratio)
                                        spacer50()
                                        StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        spacer50()
                                        if (!isSubmitted.value && userId != null) {
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
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Hypothesis(
    navController: NavController,
    viewModel: HypothesisViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val (smean, setsmean) = remember { mutableStateOf("") }
    var tail: Boolean
    var samplem: Boolean
    val (sd, setsd) = remember { mutableStateOf("") }
    val (hmean, sethmean) = remember { mutableStateOf("") }
    val (n, setn) = remember { mutableStateOf("") }
    val (sl, setsl) = remember { mutableStateOf("") }
    val Result = viewModel.hypothesisresult.observeAsState()
    val isSubmitted = remember { mutableStateOf(false) }
    val userId = authViewModel.getuserid()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                LazyColumn (
                    modifier = Modifier.padding(16.dp),
                ){
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
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Sample Mean:",
                                value = smean,
                                onValueChange = setsmean
                            )
                            spacer50()
                            tail = tailSwitch (onSwitchChange = {newTail -> tail = newTail }, first = "One Tail", second = "Two Tails")
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Standard Deviation:",
                                value = sd,
                                onValueChange = setsd
                            )
                            spacer50()
                            samplem = tailSwitch ({ newsamplem -> samplem = newsamplem }, first = "Sample Standard Deviation", second = "Population Standard Deviation")
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Significance Level:",
                                value = sl,
                                onValueChange = setsl
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = setn
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Hypothesized Mean:",
                                value = hmean,
                                onValueChange = sethmean
                            )
                            spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    if (smean != "" && sd != "" && hmean != "" && n != "" && sl != "") {
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
                            spacer50()
                            if (smean.isNotEmpty() && sd.isNotEmpty() && hmean.isNotEmpty() && n.isNotEmpty() && sl.isNotEmpty()) {
                                when(val result = Result.value) {
                                    is NetworkResponse.Failure -> {
                                        spacer50()
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f))
                                        spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        spacer50()
                                        StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        spacer50()
                                        if(userId != null && !isSubmitted.value) {
                                            authViewModel.sendhypothesis(
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
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Sample Mean:",
                                value = smean,
                                onValueChange = setsmean
                            )
                            spacer50()
                            tail = tailSwitch (onSwitchChange = {newTail -> tail = newTail }, first = "One Tail", second = "Two Tails")
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Standard Deviation:",
                                value = sd,
                                onValueChange = setsd
                            )
                            spacer50()
                            samplem = tailSwitch ({ newsamplem -> samplem = newsamplem }, first = "Sample Standard Deviation", second = "Population Standard Deviation")
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Significance Level:",
                                value = sl,
                                onValueChange = setsl
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = setn
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Hypothesized Mean:",
                                value = hmean,
                                onValueChange = sethmean
                            )
                            spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    if (smean != "" && sd != "" && hmean != "" && n != "" && sl != "") {
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
                            if (smean.isNotEmpty() && sd.isNotEmpty() && hmean.isNotEmpty() && n.isNotEmpty() && sl.isNotEmpty()) {
                                when(val result = Result.value) {
                                    is NetworkResponse.Failure -> {
                                        spacer50()
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f))
                                        spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        spacer50()
                                        StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        spacer50()
                                        if(userId != null && !isSubmitted.value) {
                                            authViewModel.sendhypothesis(
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
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Sample Mean:",
                                value = smean,
                                onValueChange = setsmean
                            )
                            spacer50()
                            tail = tailSwitch (onSwitchChange = {newTail -> tail = newTail }, first = "One Tail", second = "Two Tails")
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Standard Deviation:",
                                value = sd,
                                onValueChange = setsd
                            )
                            spacer50()
                            samplem = tailSwitch ({ newsamplem -> samplem = newsamplem }, first = "Sample Standard Deviation", second = "Population Standard Deviation")
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Significance Level:",
                                value = sl,
                                onValueChange = setsl
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Size:",
                                value = n,
                                onValueChange = setn
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "Hypothesized Mean:",
                                value = hmean,
                                onValueChange = sethmean
                            )
                            spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    if (smean != "" && sd != "" && hmean != "" && n != "" && sl != "") {
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
                            spacer50()
                            if (smean.isNotEmpty() && sd.isNotEmpty() && hmean.isNotEmpty() && n.isNotEmpty() && sl.isNotEmpty()) {
                                when(val result = Result.value) {
                                    is NetworkResponse.Failure -> {
                                        spacer50()
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f))
                                        spacer50()
                                    }
                                    NetworkResponse.Loading -> {
                                        spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        spacer50()
                                        StringAnswer(text = "Hypothesis: ${result.data.hypothesis}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                        spacer50()
                                        if(userId != null && !isSubmitted.value) {
                                            authViewModel.sendhypothesis(
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
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
import com.example.kotlin_learning.viewModel.BinomialViewModel
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.data.request.BinomialRequest
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
fun Binomial(
    navController: NavController,
    viewModel: BinomialViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val (p, setp) = remember { mutableStateOf("") }
    val (x, setx) = remember { mutableStateOf("") }
    val (n, setn) = remember { mutableStateOf("") }
    val probability = viewModel.binomialresult.observeAsState()
    val userId = authViewModel.getuserid()
    val isSubmitted = remember { mutableStateOf(false) }

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
            is WindowInfo.WindowType.Compact -> {
                Scaffold (
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Binomial") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "n:",
                                value = n,
                                onValueChange = setn
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "x:",
                                value = x,
                                onValueChange = setx
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "p:",
                                value = p,
                                onValueChange = setp
                            )
                            spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    if (n != "" && p != "" && x != "") {
                                        val binomialrequest = BinomialRequest(
                                            n = n.toFloat(),
                                            x = x.toFloat(),
                                            p = p.toFloat()
                                        )
                                        viewModel.getBinomialAnswer(binomialrequest)
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
                            if (x != "" && n != "" && p != "") {
                                when(val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        spacer50()
                                        FloatAnswer(text = "P(X=$x):", value = result.data.equal)
                                        spacer50()
                                        FloatAnswer(text = "P(X<$x):", value = result.data.less)
                                        spacer50()
                                        FloatAnswer(text = "P(X<=$x):", value = result.data.lessequal)
                                        spacer50()
                                        FloatAnswer(text = "P(X>$x):", value = result.data.greater)
                                        spacer50()
                                        FloatAnswer(text = "P(X>=$x):", value = result.data.greaterequal)
                                        spacer50()
                                        if (!isSubmitted.value && userId != null) {
                                            authViewModel.sendbinomial(
                                                userId = userId,
                                                x = x.toFloat(),
                                                n = n.toFloat(),
                                                p = p.toFloat(),
                                                equal = result.data.equal,
                                                greater = result.data.greater,
                                                greaterequal = result.data.greaterequal,
                                                less = result.data.less,
                                                lessequal = result.data.lessequal
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

            is WindowInfo.WindowType.Medium -> {
                Scaffold (
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Binomial") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "n:",
                                value = n,
                                onValueChange = setn
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "x:",
                                value = x,
                                onValueChange = setx
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "p:",
                                value = p,
                                onValueChange = setp
                            )
                            spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    if (n != "" && p != "" && x != "") {
                                        val binomialrequest = BinomialRequest(
                                            n = n.toFloat(),
                                            x = x.toFloat(),
                                            p = p.toFloat()
                                        )
                                        viewModel.getBinomialAnswer(binomialrequest)
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
                            if (x != "" && n != "" && p != "") {
                                when(val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        spacer50()
                                        FloatAnswer(text = "P(X=$x):", value = result.data.equal)
                                        spacer50()
                                        FloatAnswer(text = "P(X<$x):", value = result.data.less)
                                        spacer50()
                                        FloatAnswer(text = "P(X<=$x):", value = result.data.lessequal)
                                        spacer50()
                                        FloatAnswer(text = "P(X>$x):", value = result.data.greater)
                                        spacer50()
                                        FloatAnswer(text = "P(X>=$x):", value = result.data.greaterequal)
                                        spacer50()
                                        if (!isSubmitted.value && userId != null) {
                                            authViewModel.sendbinomial(
                                                userId = userId,
                                                x = x.toFloat(),
                                                n = n.toFloat(),
                                                p = p.toFloat(),
                                                equal = result.data.equal,
                                                greater = result.data.greater,
                                                greaterequal = result.data.greaterequal,
                                                less = result.data.less,
                                                lessequal = result.data.lessequal
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

            else -> {
                Scaffold (
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Binomial") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "n:",
                                value = n,
                                onValueChange = setn
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "x:",
                                value = x,
                                onValueChange = setx
                            )
                            spacer50()
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "p:",
                                value = p,
                                onValueChange = setp
                            )
                            spacer50()
                            ElevatedButton(
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                onClick = {
                                    keyboardController?.hide()
                                    if (n != "" && p != "" && x != "") {
                                        val binomialrequest = BinomialRequest(
                                            n = n.toFloat(),
                                            x = x.toFloat(),
                                            p = p.toFloat()
                                        )
                                        viewModel.getBinomialAnswer(binomialrequest)
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
                            if (x != "" && n != "" && p != "") {
                                when(val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        StringAnswer(text = "Failed to Load", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        spacer50()
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        spacer50()
                                        FloatAnswer(text = "P(X=$x):", value = result.data.equal)
                                        spacer50()
                                        FloatAnswer(text = "P(X<$x):", value = result.data.less)
                                        spacer50()
                                        FloatAnswer(text = "P(X<=$x):", value = result.data.lessequal)
                                        spacer50()
                                        FloatAnswer(text = "P(X>$x):", value = result.data.greater)
                                        spacer50()
                                        FloatAnswer(text = "P(X>=$x):", value = result.data.greaterequal)
                                        spacer50()
                                        if (!isSubmitted.value && userId != null) {
                                            authViewModel.sendbinomial(
                                                userId = userId,
                                                x = x.toFloat(),
                                                n = n.toFloat(),
                                                p = p.toFloat(),
                                                equal = result.data.equal,
                                                greater = result.data.greater,
                                                greaterequal = result.data.greaterequal,
                                                less = result.data.less,
                                                lessequal = result.data.lessequal
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
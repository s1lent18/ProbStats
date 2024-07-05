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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.MultinomialViewModel
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.data.request.MultinomialRequest
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.viewModel.rWindowInfo
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel
import kotlinx.coroutines.launch

fun stof(stringArray: Array<String>): FloatArray {
    return stringArray.mapNotNull { it.toFloatOrNull() }.toFloatArray()
}

fun stoff(stringArray: Array<String>): List<Float> {
    return stringArray.mapNotNull { it.toFloatOrNull() }
}

@Composable
fun ArrayInput(
    modifier: Modifier = Modifier,
    label: String,
    values: Array<String>,
    index: Int,
    onValueChange: (Array<String>) -> Unit
) {
    TextField(
        modifier = modifier,
        value = values[index],
        onValueChange = { newValue ->
            if (newValue.isNumericOrFloat()) {
                val newValues = values.copyOf()
                newValues[index] = newValue
                onValueChange(newValues)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Multinomial(
    navController: NavController,
    viewModel: MultinomialViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val (p ,setp) = remember { mutableStateOf(arrayOf("")) }
    val (x, setx) = remember { mutableStateOf(arrayOf("")) }
    val (n, setn) = remember { mutableStateOf(("")) }
    val probability = viewModel.multinomialresult.observeAsState()
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
            is WindowInfo.WindowType.Compact -> {
                Scaffold (
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Multinomial") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "n:",
                                value = n,
                                onValueChange = { newN ->
                                    setn(newN)
                                    if (newN.isNotEmpty()) {
                                        val newSize = newN.toIntOrNull() ?: 0
                                        setp(Array(newSize) { if (it < p.size) p[it] else "" })
                                        setx(Array(newSize) { if (it < x.size) x[it] else "" })
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        if (n != "") {
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "X$it",
                                    values = x,
                                    index = it,
                                    onValueChange = setx
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "P$it",
                                    values = p,
                                    index = it,
                                    onValueChange = setp
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
                                    if (n != "" && p.isNotEmpty() && x.isNotEmpty()) {
                                        val multinomialrequest = MultinomialRequest(
                                            n = n.toFloat(),
                                            x = stof(x),
                                            p = stof(p)
                                        )
                                        viewModel.getMultinomialAnswer(multinomialrequest)
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
                            if (n.isNotEmpty() && p.isNotEmpty() && x.isNotEmpty()) {
                                when (val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        FloatAnswer(text = "Probability:", value = result.data.ans)
                                        Spacer(modifier = Modifier.height(50.dp))
                                        if (!isSubmitted.value && userId != null) {
                                            authViewModel.sendmultinomial(
                                                userId = userId,
                                                n = n.toFloat(),
                                                x = stoff(x),
                                                p = stoff(p),
                                                ans = result.data.ans
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
                        Appbar("Multinomial") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "n:",
                                value = n,
                                onValueChange = { newN ->
                                    setn(newN)
                                    if (newN.isNotEmpty()) {
                                        val newSize = newN.toIntOrNull() ?: 0
                                        setp(Array(newSize) { if (it < p.size) p[it] else "" })
                                        setx(Array(newSize) { if (it < x.size) x[it] else "" })
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        if (n != "") {
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "X$it",
                                    values = x,
                                    index = it,
                                    onValueChange = setx
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "P$it",
                                    values = p,
                                    index = it,
                                    onValueChange = setp
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
                                    if (n != "" && p.isNotEmpty() && x.isNotEmpty()) {
                                        val multinomialrequest = MultinomialRequest(
                                            n = n.toFloat(),
                                            x = stof(x),
                                            p = stof(p)
                                        )
                                        viewModel.getMultinomialAnswer(multinomialrequest)
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
                            if (n.isNotEmpty() && p.isNotEmpty() && x.isNotEmpty()) {
                                when (val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        FloatAnswer(text = "Probability:", value = result.data.ans)
                                        Spacer(modifier = Modifier.height(50.dp))
                                        if (!isSubmitted.value && userId != null) {
                                            authViewModel.sendmultinomial(
                                                userId = userId,
                                                n = n.toFloat(),
                                                x = stoff(x),
                                                p = stoff(p),
                                                ans = result.data.ans
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
                        Appbar("Multinomial") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                            Floatinput(
                                Modifier.fillMaxWidth(fraction = 0.9f),
                                label = "n:",
                                value = n,
                                onValueChange = { newN ->
                                    setn(newN)
                                    if (newN.isNotEmpty()) {
                                        val newSize = newN.toIntOrNull() ?: 0
                                        setp(Array(newSize) { if (it < p.size) p[it] else "" })
                                        setx(Array(newSize) { if (it < x.size) x[it] else "" })
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        if (n != "") {
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "X$it",
                                    values = x,
                                    index = it,
                                    onValueChange = setx
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) {
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "P$it",
                                    values = p,
                                    index = it,
                                    onValueChange = setp
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
                                    if (n != "" && p.isNotEmpty() && x.isNotEmpty()) {
                                        val multinomialrequest = MultinomialRequest(
                                            n = n.toFloat(),
                                            x = stof(x),
                                            p = stof(p)
                                        )
                                        viewModel.getMultinomialAnswer(multinomialrequest)
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
                            if (n.isNotEmpty() && p.isNotEmpty() && x.isNotEmpty()) {
                                when (val result = probability.value) {
                                    is NetworkResponse.Failure -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        StringAnswer("Failed To Load", Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp))
                                    }
                                    NetworkResponse.Loading -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        CircularProgressIndicator()
                                    }
                                    is NetworkResponse.Success -> {
                                        Spacer(modifier = Modifier.height(50.dp))
                                        FloatAnswer(text = "Probability:", value = result.data.ans)
                                        Spacer(modifier = Modifier.height(50.dp))
                                        if (!isSubmitted.value && userId != null) {
                                            authViewModel.sendmultinomial(
                                                userId = userId,
                                                n = n.toFloat(),
                                                x = stoff(x),
                                                p = stoff(p),
                                                ans = result.data.ans
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
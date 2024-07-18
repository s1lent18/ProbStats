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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.kotlin_learning.viewModel.Repository
import kotlinx.coroutines.launch

fun stof(stringArray: Array<String>): FloatArray {
    return stringArray.mapNotNull { it.toFloatOrNull() }.toFloatArray()
}

fun stoi(stringArray: Array<String>): IntArray {
    return stringArray.mapNotNull { it.toIntOrNull() }.toIntArray()
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
    val repository = Repository()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val (p ,setp) = remember { mutableStateOf(arrayOf("")) }
    val (x, setx) = remember { mutableStateOf(arrayOf("")) }
    val (n, setn) = remember { mutableStateOf(("")) }
    val probability = viewModel.multinomialresult.observeAsState()
    var isSubmitted by remember { mutableStateOf(false) }
    var isupdated by remember { mutableStateOf(false) }
    var display by remember { mutableStateOf(false) }
    val userId = authViewModel.getuserid()
    val showDialog = remember { mutableStateOf(false) }
    val windowInfo = rWindowInfo()

    if(showDialog.value) {
        DialogBox(
            title = "Error",
            text = "The Sum of all Probabilities should be 1 | The sum of all X should be n",
            setShowDialog = showDialog,
            onClick = { showDialog.value = false }
        )
    }

    LaunchedEffect(isupdated) {
        if(isupdated) {
            val multinomialrequest = MultinomialRequest(
                n = n.toFloat(),
                x = stof(x),
                p = stof(p)
            )
            viewModel.getMultinomialAnswer(multinomialrequest)
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
                                    display = false
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        if (n != "") {
                            items(n.toInt()) { a ->
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "X$a",
                                    values = x,
                                    index = a,
                                    onValueChange = {
                                        setx(it)
                                        display = false
                                    }
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) { b ->
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "P$b",
                                    values = p,
                                    index = b,
                                    onValueChange = {
                                        setp(it)
                                        display = false
                                    }
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
                                        if(stof(p).sum() != 1f || stoi(x).sum() != n.toInt()) {
                                            showDialog.value = true
                                        } else {
                                            isSubmitted = false
                                            isupdated = true
                                            display = true
                                            keyboardController?.hide()
                                            showDialog.value = false
                                        }
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
                            if (n.isNotEmpty() && p.isNotEmpty() && x.isNotEmpty() && !isupdated) {
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
                                        if (display) {
                                            Spacer(modifier = Modifier.height(50.dp))
                                            FloatAnswer(text = "Probability:", value = result.data.ans)
                                            Spacer(modifier = Modifier.height(50.dp))
                                            if (!isSubmitted && userId != null) {
                                                repository.sendmultinomial(
                                                    userId = userId,
                                                    n = n.toFloat(),
                                                    x = stoff(x),
                                                    p = stoff(p),
                                                    ans = result.data.ans
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
                                    display = false
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        if (n != "") {
                            items(n.toInt()) { a ->
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "X$a",
                                    values = x,
                                    index = a,
                                    onValueChange = {
                                        setx(it)
                                        display = false
                                    }
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) { b ->
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "P$b",
                                    values = p,
                                    index = b,
                                    onValueChange = {
                                        setp(it)
                                        display = false
                                    }
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
                                        if(stof(p).sum() != 1f || stoi(x).sum() != n.toInt()) {
                                            showDialog.value = true
                                        } else {
                                            isSubmitted = false
                                            isupdated = true
                                            display = true
                                            keyboardController?.hide()
                                            showDialog.value = false
                                        }
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
                            if (n.isNotEmpty() && p.isNotEmpty() && x.isNotEmpty() && !isupdated) {
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
                                        if (display) {
                                            Spacer(modifier = Modifier.height(50.dp))
                                            FloatAnswer(text = "Probability:", value = result.data.ans)
                                            Spacer(modifier = Modifier.height(50.dp))
                                            if (!isSubmitted && userId != null) {
                                                repository.sendmultinomial(
                                                    userId = userId,
                                                    n = n.toFloat(),
                                                    x = stoff(x),
                                                    p = stoff(p),
                                                    ans = result.data.ans
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
                                    display = false
                                }
                            )
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        if (n != "") {
                            items(n.toInt()) { a ->
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "X$a",
                                    values = x,
                                    index = a,
                                    onValueChange = {
                                        setx(it)
                                        display = false
                                    }
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                            items(n.toInt()) { b ->
                                ArrayInput(
                                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                    label = "P$b",
                                    values = p,
                                    index = b,
                                    onValueChange = {
                                        setp(it)
                                        display = false
                                    }
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
                                        if(stof(p).sum() != 1f || stoi(x).sum() != n.toInt()) {
                                            showDialog.value = true
                                        } else {
                                            isSubmitted = false
                                            isupdated = true
                                            display = true
                                            keyboardController?.hide()
                                            showDialog.value = false
                                        }
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
                            if (n.isNotEmpty() && p.isNotEmpty() && x.isNotEmpty() && !isupdated) {
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
                                        if (display) {
                                            Spacer(modifier = Modifier.height(50.dp))
                                            FloatAnswer(text = "Probability:", value = result.data.ans)
                                            Spacer(modifier = Modifier.height(50.dp))
                                            if (!isSubmitted && userId != null) {
                                                repository.sendmultinomial(
                                                    userId = userId,
                                                    n = n.toFloat(),
                                                    x = stoff(x),
                                                    p = stoff(p),
                                                    ans = result.data.ans
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
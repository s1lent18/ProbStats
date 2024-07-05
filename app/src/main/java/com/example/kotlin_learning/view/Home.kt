package com.example.kotlin_learning.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.viewModel.rWindowInfo
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import kotlinx.coroutines.launch

@Composable
fun ScrollableList(navController: NavController, items: List<Pair<String, String>>, title: String, screentype: String) {

    val width = 250.dp
    val height = 450.dp

    val titlesize = if(screentype == "Compact") 20.sp else if(screentype == "Medium") 30.sp else 20.sp
    val bodysize = if(screentype == "Compact") 15.sp else if(screentype == "Medium") 20.sp else 15.sp

    Card (
        modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
        ),
    ) {
        Spacer(Modifier.height(12.dp))
        Column (
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title)
        }
    }

    Spacer(modifier = Modifier.height(60.dp))

    LazyRow (
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier.width(width).height(height),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(text = item.first, fontSize = titlesize, modifier = Modifier.clickable {
                        when (item.first) {
                            "Poisson" -> {
                                navController.navigate(route = Screen.Poisson.route)
                            }
                            "Binomial" -> {
                                navController.navigate(route = Screen.Binomial.route)
                            }
                            "Multinomial" -> {
                                navController.navigate(route = Screen.Multinomial.route)
                            }
                            "Anova" -> {
                                navController.navigate(route = Screen.Anova.route)
                            }
                            "Bayes Rule" -> {
                                navController.navigate(route = Screen.BayesRule.route)
                            }
                            "SLR" -> {
                                navController.navigate(route = Screen.SLR.route)
                            }
                            "UnGrouped" -> {
                                navController.navigate(route = Screen.UnGrouped.route)
                            }
                            "Grouped" -> {
                                navController.navigate(route = Screen.Grouped.route)
                            }
                            "Hypothesis" -> {
                                navController.navigate(route = Screen.Hypothesis.route)
                            }
                        }
                    })
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = item.second, fontSize = bodysize)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appbar(title: String, openDrawer: () -> Unit) {
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()

    TopAppBar(
        title = {
            Text(text = title, style = TextStyle(color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor, fontSize = 20.sp))
        },
        navigationIcon = {
            IconButton(onClick = {
                openDrawer()
            }) {
                Icon(painter = painterResource(R.drawable.menu), contentDescription = "menu button", tint = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            scrolledContainerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
        ),
        scrollBehavior = scrollbehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val uicolor = if (isSystemInDarkTheme()) Color(0xFF023047) else Color(0xFF0077B6)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()

    val probs = listOf(
        "Poisson" to "Formula: (μ^x * e^-μ)/x! \n\nHint: If a mean or average probability of an event is given, then the Poisson Distribution is used.",
        "Binomial" to "Formula: nCx * p^x  * q^(n-x) \n\nHint: If, an exact probability of an event happening is given, then the Binomial Distribution must be used." ,
        "Multinomial" to "Formula:\n(n! / (x1! * x2! * xn!)) * p1^x1 * p2^x2 * pn^xn ",
        "Anova" to "It is a tool for analyzing how the mean value of a quantitative response variable is related to one or more categorical explanatory factors.",
        "Bayes Rule" to "Formula:\nP (A|B) = (P(B|A) * P(A)) / P(B)",
        "SLR" to "Update timestamp",
        "UnGrouped" to "Find:\n- Mean\n- Median\n- Mode\n- First Quartile\n- Third Quartile\n- Standard Deviation\n- Variance\n- Stemleaf\n- Shape Distribution\n- Empirical Rule",
        "Grouped" to "Update timestamp",
        "Hypothesis" to "Update timestamp"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (modifier = Modifier.background(uicolor)) {
                LazyColumn (
                    modifier = Modifier.padding(16.dp)
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
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Calculators") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(values)
                    ) {
                        item {
                            Column (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Spacer(modifier = Modifier.height(35.dp))
                                ScrollableList(navController, probs, "Probability and Statistics", "Compact")
                                Spacer(modifier = Modifier.height(35.dp))
                            }
                        }
                    }
                }
            }
            WindowInfo.WindowType.Expanded -> {
                Scaffold (
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Calculators") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(values)
                    ) {
                        item {
                            Column (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Spacer(modifier = Modifier.height(35.dp))
                                ScrollableList(navController, probs, "Probability and Statistics", "Expanded")
                                Spacer(modifier = Modifier.height(35.dp))
                            }
                        }
                    }
                }
            }
            WindowInfo.WindowType.Medium -> {
                Scaffold (
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Calculators") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(values)
                    ) {
                        item {
                            Column (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Spacer(modifier = Modifier.height(35.dp))
                                ScrollableList(navController, probs, "Probability and Statistics", "Medium")
                                Spacer(modifier = Modifier.height(35.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

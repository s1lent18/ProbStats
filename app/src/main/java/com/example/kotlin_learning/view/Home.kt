package com.example.kotlin_learning.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.data.model.Probability
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.viewModel.rWindowInfo
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun ScrollableList(navController: NavController, items: List<Probability>, title: String, screentype: String) {

    val width = 250.dp
    val height = 450.dp

    val titlesize = if(screentype == "Compact") 20.sp else if(screentype == "Medium") 30.sp else 20.sp
    val bodysize = if(screentype == "Compact") 15.sp else if(screentype == "Medium") 20.sp else 15.sp

    Card (
        modifier = Modifier
            .fillMaxWidth(fraction = 0.9f)
            .height(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
            contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
        ),
    ) {
        Spacer(Modifier.height(15.dp))
        Column (
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontWeight = FontWeight.Bold)
        }
    }

    Spacer(modifier = Modifier.height(80.dp))

    LazyRow (
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(items) { item ->
            FloatingActionButton(
                modifier = Modifier
                    .width(width)
                    .height(height),
                onClick = {
                    when (item.title) {
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
                },
                containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(text = item.title, fontSize = titlesize)
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = item.description, fontSize = bodysize)
                }
            }
        }
    }
}

@Composable
fun SidebarItem(
    icon: ImageVector,
    text: String,
    count: Int? = null,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground} else Color.Transparent
    val textColor = if (isSelected) {
        if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
    } else {
        if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(text = text, color = textColor)
        }
        count?.let {
            Text(text = it.toString(), color = textColor)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
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
    authViewModel: AuthViewModel = viewModel(),
    homeviewmodel: HomeViewModel = viewModel()
) {
    val uicolor = if (isSystemInDarkTheme()) Color(0xFF023047) else Color(0xFF0077B6)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val isLoading by homeviewmodel.isLoading
    val probs by homeviewmodel.probs

//    val probs = listOf(
//        "Poisson" to "the number of outcomes occurring during a given time interval or in a specified region, are called Poisson experiments.",
//        "Binomial" to "The number X of successes in n Bernoulli trials is called a binomial random variable. The probability distribution of this discrete random variable is called the binomial distribution" ,
//        "Multinomial" to "The binomial experiment becomes a multinomial experiment if we let each trial have more than two possible outcomes. The classification of a manufactured product as being light, heavy, or acceptable and the recording of accidents at a certain intersection according to the day of the week constitute multinomial experiments.",
//        "Anova" to "Often the problem of analyzing the quality of the estimated regression line is handled by an analysis-of-variance (ANOVA) approach: a procedure whereby the total variation in the dependent variable is subdivided into meaningful components that are then observed and treated in a systematic fashion.",
//        "Bayes Rule" to "Bayesian statistics is a collection of tools that is used in a special form of statistical inference which applies in the analysis of experimental data in many practical situations in science and engineering.",
//        "SLR" to "The relationship between two variables X (independent variable) and Y (dependent variable) is modeled by a linear equation. This is called simple linear regression.",
//        "UnGrouped" to "Find:\n- Mean\n- Median\n- Mode\n- First Quartile\n- Third Quartile\n- Standard Deviation\n- Variance\n- Stemleaf\n- Shape Distribution\n- Empirical Rule",
//        "Grouped" to "Find:\n- Mean\n- Median\n- Mode\n- Standard Deviation\n- variance",
//        "Hypothesis" to "The process of making decisions about population parameters based on sample data is called hypothesis testing. The probability distribution used to determine the likelihood of the sample data under the null hypothesis is called the sampling distribution."
//    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (modifier = Modifier
                .background(uicolor)
                .fillMaxHeight()
                .fillMaxWidth(fraction = 0.8f)) {
                LazyColumn (
                    modifier = Modifier.padding(16.dp)
                ){
                    item {
                        Icon(
                            painter = painterResource(R.drawable.prob_stats),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(fraction = 0.3f),
                            tint = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground
                        )
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
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Home") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(values),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(values)
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
            }
            WindowInfo.WindowType.Expanded -> {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Calculators") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(values)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("Calculators") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(values)
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

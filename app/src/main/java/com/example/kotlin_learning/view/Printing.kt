package com.example.kotlin_learning.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_learning.R
import com.example.kotlin_learning.data.forhistory.Anovaclass
import com.example.kotlin_learning.data.forhistory.BayesRuleclass
import com.example.kotlin_learning.data.forhistory.Binomialclass
import com.example.kotlin_learning.data.forhistory.Groupedclass
import com.example.kotlin_learning.data.forhistory.Hypothesisclass
import com.example.kotlin_learning.data.forhistory.Multinomialclass
import com.example.kotlin_learning.data.forhistory.Poissonclass
import com.example.kotlin_learning.data.forhistory.SLRclass
import com.example.kotlin_learning.data.forhistory.UnGroupedclass
import com.example.kotlin_learning.ui.theme.darkmodebackground
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Repository
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.viewModel.rWindowInfo
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Printing(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController,
    index: Int,
    tabindex: Int
) {
    val repository = Repository()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val userId = authViewModel.getuserid() ?: return
    val poissonState = remember { mutableStateOf<List<Poissonclass>>(emptyList()) }
    val binomialState = remember { mutableStateOf<List<Binomialclass>>(emptyList()) }
    val multinomialState = remember { mutableStateOf<List<Multinomialclass>>(emptyList()) }
    val bayesruleState = remember { mutableStateOf<List<BayesRuleclass>>(emptyList()) }
    val anovaState = remember { (mutableStateOf<List<Anovaclass>>(emptyList())) }
    val slrState = remember { mutableStateOf<List<SLRclass>>(emptyList()) }
    val ungroupedState = remember { mutableStateOf<List<UnGroupedclass>>(emptyList()) }
    val groupedState = remember { mutableStateOf<List<Groupedclass>>(emptyList()) }
    val hypothesisState = remember { mutableStateOf<List<Hypothesisclass>>(emptyList()) }
    val showsearch = remember { mutableStateOf(false) }
    val windowInfo = rWindowInfo()

    LaunchedEffect(userId) {
        repository.receiverpoisson(userId) { messages ->
            poissonState.value = messages
        }
    }

    LaunchedEffect(userId) {
        repository.receiverbinomial(userId) { bm ->
            binomialState.value = bm
        }
    }

    LaunchedEffect(userId) {
        repository.receivermultinomial(userId) { m ->
            multinomialState.value = m
        }
    }

    LaunchedEffect(userId) {
        repository.receiveranova(userId) {a ->
            anovaState.value = a
        }
    }

    LaunchedEffect(userId) {
        repository.receiverbayesrule(userId) {ba ->
            bayesruleState.value = ba
        }
    }

    LaunchedEffect(userId) {
        repository.receiverslr(userId) { s ->
            slrState.value = s
        }
    }

    LaunchedEffect(userId) {
        repository.receiverungrouped(userId) { ug ->
            ungroupedState.value = ug
        }
    }

    LaunchedEffect(userId) {
        repository.receivergrouped(userId) { g ->
            groupedState.value = g
        }
    }

    LaunchedEffect(userId) {
        repository.receiverhypothesis(userId) { h ->
            hypothesisState.value = h
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
                            isSelected = false,
                            onClick = {
                                navController.navigate(route = Screen.Home.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        SidebarItem(
                            icon = Icons.Default.Info,
                            text = "History",
                            isSelected = true,
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
            WindowInfo.WindowType.Compact -> {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar(title = "Printing") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(values),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            item {
                                Spacer50()
                                if (!showsearch.value) {
                                    when (tabindex) {
                                        0 -> if (poissonState.value.isNotEmpty() && index < poissonState.value.size) {
                                            PoissonItem(poissonState.value[index])
                                        } else {
                                            CircularProgressIndicator()
                                        }
                                        1 -> if (binomialState.value.isNotEmpty() && index < binomialState.value.size) {
                                            BinomialItem(binomialState.value[index])
                                        } else {
                                            CircularProgressIndicator()
                                        }
                                        2 -> if (multinomialState.value.isNotEmpty() && index < multinomialState.value.size) {
                                            MultinomialItem(multinomialState.value[index])
                                        } else {
                                            CircularProgressIndicator()
                                        }
                                        3 -> if (anovaState.value.isNotEmpty() && index < anovaState.value.size) {
                                            AnovaItem(anovaState.value[index])
                                        } else {
                                            CircularProgressIndicator()
                                        }
                                        4 -> if (bayesruleState.value.isNotEmpty() && index < bayesruleState.value.size) {
                                            BayesRuleItem(bayesruleState.value[index])
                                        } else {
                                            CircularProgressIndicator()
                                        }
                                        5 -> if (slrState.value.isNotEmpty() && index < slrState.value.size) {
                                            SLRItem(slrState.value[index])
                                        } else {
                                            CircularProgressIndicator()
                                        }
                                        6 -> if (ungroupedState.value.isNotEmpty() && index < ungroupedState.value.size) {
                                            UnGroupedItem(ungroupedState.value[index])
                                        } else {
                                            CircularProgressIndicator()
                                        }
                                        7 -> if (groupedState.value.isNotEmpty() && index < groupedState.value.size) {
                                            GroupedItem(groupedState.value[index])
                                        } else {
                                            CircularProgressIndicator()
                                        }
                                        8 -> if (hypothesisState.value.isNotEmpty() && index < hypothesisState.value.size) {
                                            HypothesisItem(hypothesisState.value[index])
                                        } else {
                                            CircularProgressIndicator()
                                        }
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
                        Appbar("Printing") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        item {
                            if(tabindex == 0) {
                                if (poissonState.value.isNotEmpty() && index < poissonState.value.size) {
                                    PoissonItem(poissonState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 1) {
                                if (binomialState.value.isNotEmpty() && index < binomialState.value.size) {
                                    BinomialItem(binomialState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 2) {
                                if (multinomialState.value.isNotEmpty() && index < multinomialState.value.size) {
                                    MultinomialItem(multinomialState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 3) {
                                if (anovaState.value.isNotEmpty() && index < anovaState.value.size) {
                                    AnovaItem(anovaState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 4) {
                                if(bayesruleState.value.isNotEmpty() && index < bayesruleState.value.size) {
                                    BayesRuleItem(bayesruleState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 5) {
                                if(slrState.value.isNotEmpty() && index < slrState.value.size) {
                                    SLRItem(slrState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 6) {
                                if(ungroupedState.value.isNotEmpty() && index < ungroupedState.value.size) {
                                    UnGroupedItem(ungroupedState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 7) {
                                if(groupedState.value.isNotEmpty() && index < groupedState.value.size) {
                                    GroupedItem(groupedState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 8) {
                                if(hypothesisState.value.isNotEmpty() && index < hypothesisState.value.size) {
                                    HypothesisItem(hypothesisState.value[index])
                                } else {
                                    CircularProgressIndicator()
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
                        Appbar("Printing") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        item {
                            if(tabindex == 0) {
                                if (poissonState.value.isNotEmpty() && index < poissonState.value.size) {
                                    PoissonItem(poissonState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 1) {
                                if (binomialState.value.isNotEmpty() && index < binomialState.value.size) {
                                    BinomialItem(binomialState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 2) {
                                if (multinomialState.value.isNotEmpty() && index < multinomialState.value.size) {
                                    MultinomialItem(multinomialState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 3) {
                                if (anovaState.value.isNotEmpty() && index < anovaState.value.size) {
                                    AnovaItem(anovaState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 4) {
                                if(bayesruleState.value.isNotEmpty() && index < bayesruleState.value.size) {
                                    BayesRuleItem(bayesruleState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 5) {
                                if(slrState.value.isNotEmpty() && index < slrState.value.size) {
                                    SLRItem(slrState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 6) {
                                if(ungroupedState.value.isNotEmpty() && index < ungroupedState.value.size) {
                                    UnGroupedItem(ungroupedState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 7) {
                                if(groupedState.value.isNotEmpty() && index < groupedState.value.size) {
                                    GroupedItem(groupedState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            } else if (tabindex == 8) {
                                if(hypothesisState.value.isNotEmpty() && index < hypothesisState.value.size) {
                                    HypothesisItem(hypothesisState.value[index])
                                } else {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
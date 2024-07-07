package com.example.kotlin_learning.viewModel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kotlin_learning.view.Account
import com.example.kotlin_learning.view.Anova
import com.example.kotlin_learning.view.BayesRule
import com.example.kotlin_learning.view.Binomial
import com.example.kotlin_learning.view.Grouped
import com.example.kotlin_learning.view.History
import com.example.kotlin_learning.view.Home
import com.example.kotlin_learning.view.Hypothesis
import com.example.kotlin_learning.view.Login
import com.example.kotlin_learning.view.Multinomial
import com.example.kotlin_learning.view.PassChange
import com.example.kotlin_learning.view.Poisson
import com.example.kotlin_learning.view.Printing
import com.example.kotlin_learning.view.SLR
import com.example.kotlin_learning.view.Signup
import com.example.kotlin_learning.view.UnGrouped

@Composable
fun SetupNavgraph (
    navController: NavHostController
) {
    val owner = LocalViewModelStoreOwner.current
    val slrviewmodel = ViewModelProvider(owner!!)[SLRViewModel::class.java]
    val anovaviewmodel = ViewModelProvider(owner!!)[AnovaViewModel::class.java]
    val groupedviewmodel = ViewModelProvider(owner!!)[GroupedViewModel::class.java]
    val poissonviewmodel = ViewModelProvider(owner!!)[PoissonViewModel::class.java]
    val binomialviewmodel = ViewModelProvider(owner!!)[BinomialViewModel::class.java]
    val bayesruleviewmodel = ViewModelProvider(owner!!)[BayesRuleViewModel::class.java]
    val ungroupedviewmodel = ViewModelProvider(owner!!)[UnGroupedViewModel::class.java]
    val hypothesisviewmodel = ViewModelProvider(owner!!)[HypothesisViewModel::class.java]
    val multinomialviewmodel = ViewModelProvider(owner!!)[MultinomialViewModel::class.java]

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        this.composable(
            route = Screen.Login.route
        ) { Login(navController = navController) }

        this.composable(
            route = Screen.Signup.route
        ) { Signup(navController = navController) }

        this.composable(
            route = Screen.Home.route
        ) { Home(navController = navController) }

        this.composable(
            route = Screen.Poisson.route
        ) { Poisson(navController = navController, poissonviewmodel) }

        this.composable(
            route = Screen.Binomial.route
        ) { Binomial(navController = navController, binomialviewmodel) }

        this.composable(
            route = Screen.BayesRule.route
        ) { BayesRule(navController = navController, bayesruleviewmodel) }

        this.composable(
            route = Screen.Multinomial.route
        ) { Multinomial(navController = navController, multinomialviewmodel) }

        this.composable(
            route = Screen.Anova.route
        ) { Anova(navController = navController, anovaviewmodel) }

        this.composable(
            route = Screen.SLR.route
        ) { SLR(navController = navController, slrviewmodel) }

        this.composable(
            route = Screen.UnGrouped.route
        ) { UnGrouped(navController = navController, ungroupedviewmodel) }

        this.composable(
            route = Screen.Grouped.route
        ) { Grouped(navController = navController, groupedviewmodel) }

        this.composable(
            route = Screen.Hypothesis.route
        ) { Hypothesis(navController = navController, hypothesisviewmodel) }

        this.composable(
            route = Screen.History.route
        ) { History(navController = navController) }

        this.composable(
            route = "Printing_screen/{index}/{tabindex}",
            arguments = listOf(
                navArgument("index") {
                    type = NavType.IntType
                    nullable = false
                },
                navArgument("tabindex") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { entry ->
            entry.arguments?.let { Printing(navController = navController, index = it.getInt("index"), tabindex = it.getInt("tabindex")) }
        }

        this.composable(
            route = Screen.Account.route
        ) { Account(navController = navController) }

        this.composable(
            route = Screen.PassChange.route
        ) { PassChange() }
    }
}
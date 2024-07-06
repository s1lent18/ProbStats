package com.example.kotlin_learning.viewModel

sealed class Screen(val route: String) {
    data object Login: Screen(route = "Login_screen")
    data object Signup: Screen(route = "Signup_screen")
    data object Home: Screen(route = "Home_screen")
    data object Poisson: Screen(route = "Poisson_screen")
    data object Binomial: Screen(route = "Binomial_screen")
    data object BayesRule: Screen(route = "BayesRule_screen")
    data object Multinomial: Screen(route = "Multinomial_screen")
    data object Anova: Screen(route = "Anova_screen")
    data object SLR: Screen(route = "SLR_screen")
    data object UnGrouped: Screen(route = "UnGrouped_screen")
    data object Grouped: Screen(route = "Grouped_screen")
    data object Hypothesis: Screen(route = "Hypothesis_screen")
    data object History: Screen(route = "History_screen")
    data object Printing: Screen(route = "Printing_screen/{index}/{tabindex}")
    data object Account: Screen(route = "Account_screen")
}
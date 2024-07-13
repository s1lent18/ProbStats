package com.example.kotlin_learning.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.model.Probability
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _probs = mutableStateOf<List<Probability>>(emptyList())
    val probs: State<List<Probability>> = _probs

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadProbs()
    }

    private fun loadProbs() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000)
            _probs.value = fetchProbabilities()
            _isLoading.value = false
        }
    }

    private suspend fun fetchProbabilities(): List<Probability> {
        // Replace with actual data fetching logic
        return listOf(
            Probability("Poisson", "the number of outcomes occurring during a given time interval or in a specified region, are called Poisson experiments."),
            Probability("Binomial", "The number X of successes in n Bernoulli trials is called a binomial random variable. The probability distribution of this discrete random variable is called the binomial distribution"),
            Probability("Multinomial", "The binomial experiment becomes a multinomial experiment if we let each trial have more than two possible outcomes. The classification of a manufactured product as being light, heavy, or acceptable and the recording of accidents at a certain intersection according to the day of the week constitute multinomial experiments."),
            Probability("Anova", "Often the problem of analyzing the quality of the estimated regression line is handled by an analysis-of-variance (ANOVA) approach: a procedure whereby the total variation in the dependent variable is subdivided into meaningful components that are then observed and treated in a systematic fashion."),
            Probability("Bayes Rule", "Bayesian statistics is a collection of tools that is used in a special form of statistical inference which applies in the analysis of experimental data in many practical situations in science and engineering."),
            Probability("SLR", "The relationship between two variables X (independent variable) and Y (dependent variable) is modeled by a linear equation. This is called simple linear regression."),
            Probability("UnGrouped", "Find:\n" +
                    "- Mean\n" +
                    "- Median\n" +
                    "- Mode\n" +
                    "- First Quartile\n" +
                    "- Third Quartile\n" +
                    "- Standard Deviation\n" +
                    "- Variance\n" +
                    "- Stemleaf\n" +
                    "- Shape Distribution\n" +
                    "- Empirical Rule"),
            Probability("Grouped", "Find:\n" +
                    "- Mean\n" +
                    "- Median\n" +
                    "- Mode\n" +
                    "- Standard Deviation\n" +
                    "- variance"),
            Probability("Hypothesis", "The process of making decisions about population parameters based on sample data is called hypothesis testing. The probability distribution used to determine the likelihood of the sample data under the null hypothesis is called the sampling distribution.")
        )
    }
}
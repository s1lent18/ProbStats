package com.example.kotlin_learning.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.kotlin_learning.ui.theme.darkmodefontcolor
import com.example.kotlin_learning.ui.theme.lightmodebackground
import com.example.kotlin_learning.ui.theme.lightmodefontcolor
import com.example.kotlin_learning.viewModel.AuthViewModel
import com.example.kotlin_learning.viewModel.Screen
import com.example.kotlin_learning.viewModel.WindowInfo
import com.example.kotlin_learning.viewModel.rWindowInfo
import kotlinx.coroutines.launch


@Composable
fun PoissonItem(poisson: Poissonclass) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StringAnswer("X: ${poisson.x}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("Lamda: ${poisson.lamda}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${poisson.x}) = ${poisson.equal}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${poisson.x}) = ${poisson.less}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${poisson.x}) = ${poisson.lessequal}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${poisson.x}) = ${poisson.greater}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${poisson.x}) = ${poisson.greaterequal}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
    }
}

@Composable
fun BinomialItem(binomial: Binomialclass) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer50()
        StringAnswer("X: ${binomial.x}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("n: ${binomial.n}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("p: ${binomial.p}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${binomial.x}) = ${binomial.equal}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${binomial.x}) = ${binomial.less}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${binomial.x}) = ${binomial.lessequal}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${binomial.x}) = ${binomial.greater}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("P(X=${binomial.x}) = ${binomial.greaterequal}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
    }
}

@Composable
fun MultinomialItem(multinomial: Multinomialclass) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer50()
        StringAnswer("n: ${multinomial.n}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("x", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        for (i in 0..<multinomial.x.size) {
            StringAnswer("x$i: ${multinomial.x[i]}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
            Spacer( modifier = Modifier.height(20.dp))
        }
        Spacer50()
        StringAnswer("p", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        for (i in 0..<multinomial.p.size) {
            StringAnswer("p$i: ${multinomial.p[i]}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
            Spacer( modifier = Modifier.height(20.dp))
        }
        Spacer50()
        StringAnswer("ans = ${multinomial.ans}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
    }
}

@Composable
fun AnovaItem(anova: Anovaclass) {
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer50()
        for(i in 0 ..<anova.n.size) {
            StringAnswer("n$i : ${anova.n[i]}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer50()
        StringAnswer("n: ${anova.size}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("sl: ${anova.sl}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
            ),
            modifier = Modifier.fillMaxWidth(fraction = 0.9f).align(Alignment.CenterHorizontally),
            elevation = CardDefaults.cardElevation(10.dp),
            border = BorderStroke(
                1.dp,
                Color.Blue
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background( if (isSystemInDarkTheme()) Color(0xFFEFF6E0) else Color(0xFF0d1b2a), shape = RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    TableCell(text = "S.V.", weight = 1f, fontWeight = FontWeight.Bold)
                    TableCell(text = "SS", weight = 1f, fontWeight = FontWeight.Bold)
                    TableCell(text = "d.f.", weight = 1f, fontWeight = FontWeight.Bold)
                    TableCell(text = "MS", weight = 1f, fontWeight = FontWeight.Bold)
                    TableCell(text = "F-ratio", weight = 1f, fontWeight = FontWeight.Bold)
                }

                // Table Rows
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    TableCell(text = "Between", weight = 1f)
                    TableCell(text = "${anova.ssb}", weight = 1f)
                    TableCell(text = "${anova.dfB}", weight = 1f)
                    TableCell(text = "${anova.msb}", weight = 1f)
                    TableCell(text = "${anova.fratio}", weight = 1f)
                }
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    TableCell(text = "Within (error)", weight = 1f)
                    TableCell(text = "${anova.ssw}", weight = 1f)
                    TableCell(text = "${anova.dfW}", weight = 1f)
                    TableCell(text = "${anova.msw}", weight = 1f)
                    TableCell(text = "", weight = 1f)
                }
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    TableCell(text = "Total", weight = 1f)
                    TableCell(text = "${anova.ssb + anova.ssw}", weight = 1f)
                    TableCell(text = "${anova.dfB + anova.dfW}", weight = 1f)
                    TableCell(text = "", weight = 1f)
                    TableCell(text = "", weight = 1f)
                }
            }
        }
    }
}

@Composable
fun BayesRuleItem(bayesrule: BayesRuleclass) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StringAnswer("pa: ${bayesrule.pa}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("pb: ${bayesrule.pb}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("pab: ${bayesrule.pab}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("Probability: ${bayesrule.ans}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
    }
}

@Composable
fun SLRItem(slr: SLRclass) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StringAnswer("size: ${slr.n}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        for(i in 0 ..<slr.n) {
            StringAnswer("$i : ${slr.x[i]}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer50()
        for(i in 0 ..<slr.n) {
            StringAnswer("$i : ${slr.y[i]}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer50()
        StringAnswer("alpha: ${slr.alpha}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        val temp = if (!slr.tail) "Two Tails" else "One Tail"
        StringAnswer("Tail: $temp", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("hypothesis: ${slr.hypothesis}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        FloatAnswer(value = slr.r, text = "r: ")
        Spacer50()
        FloatAnswer(value = slr.t, text = "t: ")
        Spacer50()
        StringAnswer("Y: ${slr.Y}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
    }
}

@Composable
fun UnGroupedItem(ungrouped: UnGroupedclass) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StringAnswer("size: ${ungrouped.n}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        for(i in 0 ..<ungrouped.n.size) {
            StringAnswer("$i : ${ungrouped.n[i]}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer50()
        StringAnswer("Shape of the Distribution: ${ungrouped.Shape_of_the_Distribution}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("mean: ${ungrouped.mean}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("median: ${ungrouped.median}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("mode: ${ungrouped.mode}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("one: ${ungrouped.one}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("two: ${ungrouped.two}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("three: ${ungrouped.three}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("q1: ${ungrouped.q1}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("q3: ${ungrouped.q3}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("sd: ${ungrouped.sd}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("Stem Leaf: ", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        Card (
            modifier = Modifier.fillMaxWidth(fraction = 0.9f),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
            ),
            border = BorderStroke(1.dp, color = Color.Blue)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (i in ungrouped.stemleaf) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = i.key)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
                HorizontalDivider(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxHeight(fraction = 0.8f)
                        .width(1.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    for (i in ungrouped.stemleaf) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row (
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            for(j in i.value) {
                                Text(text = "$j ")
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        Spacer50()
        StringAnswer("variance: ${ungrouped.variance}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
    }

}

@Composable
fun GroupedItem(grouped: Groupedclass) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer50()
        StringAnswer("size: ${grouped.first.size}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                for(i in 0 until grouped.first.size) {
                    Row (
                        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                        horizontalArrangement = Arrangement.Center, // Centering the content within the Row
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StringAnswer("$i : ${grouped.first[i]}", modifier = Modifier.weight(1f).height(50.dp))
                        Spacer(modifier = Modifier.width(20.dp))
                        StringAnswer("$i : ${grouped.second[i]}", modifier = Modifier.weight(1f).height(50.dp))
                        Spacer(modifier = Modifier.width(20.dp))
                        StringAnswer("$i : ${grouped.freq[i]}", modifier = Modifier.weight(1f).height(50.dp))
                    }
                    Spacer50()
                }
            }
        }
        StringAnswer("mean: ${grouped.mean}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("median: ${grouped.median}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("mode: ${grouped.mode}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("sd: ${grouped.sd}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("variance: ${grouped.variance}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
    }
}

@Composable
fun HypothesisItem(hypothesis: Hypothesisclass) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StringAnswer("Sample Mean: ${hypothesis.smean}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("Hypothesized Mean: ${hypothesis.hmean}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("Size of the Sample: ${hypothesis.n}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("Standard Deviation: ${hypothesis.sd}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("Significance Level: ${hypothesis.sl}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        StringAnswer("Hypothesis: ${hypothesis.hypothesis}", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        val temp1 = if (!hypothesis.tail) "Two Tails" else "One Tail"
        StringAnswer("Tail: $temp1", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
        val temp2 = if (!hypothesis.samplem) "Population Mean" else "Sample Mean"
        StringAnswer("Tail: $temp2", modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp).align(Alignment.CenterHorizontally))
        Spacer50()
    }
}

@Composable
fun PoissonList(poissons: List<Poissonclass>, navController: NavController) {
    LazyColumn {
        items (poissons.size) { poisson ->
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                onClick = {
                    navController.navigate(route = "Printing_screen/$poisson/${0}")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    10.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            ) {
                Text(text = "$poisson : X = ${poissons[poisson].x} | Lamda: ${poissons[poisson].lamda}", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        }
    }
}

@Composable
fun BinomialList(binomials: List<Binomialclass>, navController: NavController) {
    LazyColumn {
        items (binomials.size) { binomial ->
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                onClick = {
                    navController.navigate(route = "Printing_screen/$binomial/${1}")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    10.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            ) {
                Text(text = "$binomial : x = ${binomials[binomial].x} | n = ${binomials[binomial].n} | p = ${binomials[binomial].p}", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        }
    }
}

@Composable
fun MultinomialList(multinomials: List<Multinomialclass>, navController: NavController) {
    LazyColumn {
        items (multinomials.size) { multinomial ->
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                onClick = {
                    navController.navigate(route = "Printing_screen/$multinomial/${2}")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    10.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            ) {
                Text(text = "$multinomial : n = ${multinomials[multinomial].n}", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        }
    }
}

@Composable
fun AnovaList(anovas: List<Anovaclass>, navController: NavController) {
    LazyColumn {
        items(anovas.size) { anova ->
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                onClick = {
                    navController.navigate(route = "Printing_screen/$anova/${3}")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    10.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            ) {
                Text(text = "$anova : size = ${anovas[anova].size} | Significance Level: ${anovas[anova].sl}", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        }
    }
}

@Composable
fun BayesRuleList(bayesrules: List<BayesRuleclass>, navController: NavController) {
    LazyColumn {
        items (bayesrules.size) { bayesrule ->
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                onClick = {
                    navController.navigate(route = "Printing_screen/$bayesrule/${4}")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    10.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            ) {
                Text(text = "$bayesrule : pa: ${bayesrules[bayesrule].pa} | pb: ${bayesrules[bayesrule].pb} | pab: ${bayesrules[bayesrule].pab}", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        }
    }
}

@Composable
fun SLRList(slrs: List<SLRclass>, navController: NavController) {
    LazyColumn {
        items (slrs.size) { slr ->
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                onClick = {
                    navController.navigate(route = "Printing_screen/$slr/${5}")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    10.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            ) {
                Text(text = "$slr : n: ${slrs[slr].n} | alpha: ${slrs[slr].alpha} | Hypo: ${slrs[slr].hypothesis}", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        }
    }
}

@Composable
fun UnGroupedList(ungroupeds: List<UnGroupedclass>, navController: NavController) {
    LazyColumn {
        items (ungroupeds.size) { ungrouped ->
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                onClick = {
                    navController.navigate(route = "Printing_screen/$ungrouped/${6}")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    10.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            ) {
                Text(text = "$ungrouped : mean: ${ungroupeds[ungrouped].mean} | median: ${ungroupeds[ungrouped].median}", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        }
    }
}

@Composable
fun GroupedList(groupeds: List<Groupedclass>, navController: NavController) {
    LazyColumn {
        items (groupeds.size) { grouped ->
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                onClick = {
                    navController.navigate(route = "Printing_screen/$grouped/${7}")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    10.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            ) {
                Text(text = "$grouped : mean: ${groupeds[grouped].mean} | median: ${groupeds[grouped].median}", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        }
    }
}

@Composable
fun HypothesisList(hypothesiss: List<Hypothesisclass>, navController: NavController) {
    LazyColumn {
        items (hypothesiss.size) { hypothesis ->
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                onClick = {
                    navController.navigate(route = "Printing_screen/$hypothesis/${8}")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    10.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                    contentColor = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor
                )
            ) {
                Text(text = "$hypothesis : smean: ${hypothesiss[hypothesis].smean} | hmean: ${hypothesiss[hypothesis].hmean}", color = if (isSystemInDarkTheme()) darkmodefontcolor else lightmodefontcolor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollbehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val userId = authViewModel.getuserid() ?: return
    val poissonState = remember { mutableStateOf<List<Poissonclass>>(emptyList()) }
    val binomialState = remember { mutableStateOf<List<Binomialclass>>(emptyList()) }
    val multinomialState = remember { mutableStateOf<List<Multinomialclass>>(emptyList()) }
    val anovaState = remember { mutableStateOf<List<Anovaclass>>(emptyList()) }
    val bayesruleState = remember { mutableStateOf<List<BayesRuleclass>>(emptyList()) }
    val slrState = remember { mutableStateOf<List<SLRclass>>(emptyList()) }
    val ungroupedState = remember { mutableStateOf<List<UnGroupedclass>>(emptyList()) }
    val groupedState = remember { mutableStateOf<List<Groupedclass>>(emptyList()) }
    val hypothesisState = remember { mutableStateOf<List<Hypothesisclass>>(emptyList()) }
    val calculators = listOf("Poisson", "Binomial", "Multinomial", "Anova", "Bayes Rule", "SLR", "UnGrouped", "Grouped", "Hypothesis")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(userId) {
        authViewModel.receiverpoisson(userId) { messages ->
            poissonState.value = messages
        }
    }

    LaunchedEffect(userId) {
        authViewModel.receiverbinomial(userId) { bm ->
            binomialState.value = bm
        }
    }

    LaunchedEffect(userId) {
        authViewModel.receivermultinomial(userId) { m ->
            multinomialState.value = m
        }
    }

    LaunchedEffect(userId) {
        authViewModel.receiveranova(userId) { a ->
            anovaState.value = a
        }
    }

    LaunchedEffect(userId) {
        authViewModel.receiverbayesrule(userId) { ba ->
            bayesruleState.value = ba
        }
    }

    LaunchedEffect(userId) {
        authViewModel.receiverslr(userId) { s ->
            slrState.value = s
        }
    }

    LaunchedEffect(userId) {
        authViewModel.receiverungrouped(userId) { ug ->
            ungroupedState.value = ug
        }
    }

    LaunchedEffect(userId) {
        authViewModel.receivergrouped(userId) { g ->
            groupedState.value = g
        }
    }

    LaunchedEffect(userId) {
        authViewModel.receiverhypothesis(userId) { h ->
            hypothesisState.value = h
        }
    }

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
        val windowInfo = rWindowInfo()
        when (windowInfo.screenWidthInfo) {
            WindowInfo.WindowType.Compact -> {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollbehavior.nestedScrollConnection),
                    topBar = {
                        Appbar("History") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ScrollableTabRow(
                            contentColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                            selectedTabIndex = selectedTabIndex,
                            edgePadding = 16.dp
                        ) {
                            calculators.forEachIndexed {index, i ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = { Text(i) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        when (selectedTabIndex) {
                            0 -> {
                                PoissonList(poissons = poissonState.value, navController)
                            }
                            1 -> {
                                BinomialList(binomials = binomialState.value, navController)
                            }
                            2 -> {
                                MultinomialList(multinomials = multinomialState.value, navController)
                            }
                            3 -> {
                                AnovaList(anovas = anovaState.value, navController)
                            }
                            4 -> {
                                BayesRuleList(bayesrules = bayesruleState.value, navController)
                            }
                            5 -> {
                                SLRList(slrs = slrState.value, navController)
                            }
                            6 -> {
                                UnGroupedList(ungroupeds = ungroupedState.value, navController)
                            }
                            7 -> {
                                GroupedList(groupeds = groupedState.value, navController)
                            }
                            8 -> {
                                HypothesisList(hypothesiss = hypothesisState.value, navController)
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
                        Appbar("History") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ScrollableTabRow(
                            contentColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                            selectedTabIndex = selectedTabIndex,
                            edgePadding = 16.dp
                        ) {
                            calculators.forEachIndexed {index, i ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = { Text(i) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        when (selectedTabIndex) {
                            0 -> {
                                PoissonList(poissons = poissonState.value, navController)
                            }
                            1 -> {
                                BinomialList(binomials = binomialState.value, navController)
                            }
                            2 -> {
                                MultinomialList(multinomials = multinomialState.value, navController)
                            }
                            3 -> {
                                AnovaList(anovas = anovaState.value, navController)
                            }
                            4 -> {
                                BayesRuleList(bayesrules = bayesruleState.value, navController)
                            }
                            5 -> {
                                SLRList(slrs = slrState.value, navController)
                            }
                            6 -> {
                                UnGroupedList(ungroupeds = ungroupedState.value, navController)
                            }
                            7 -> {
                                GroupedList(groupeds = groupedState.value, navController)
                            }
                            8 -> {
                                HypothesisList(hypothesiss = hypothesisState.value, navController)
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
                        Appbar("History") {
                            scope.launch {drawerState.open()}
                        }
                    }
                ) { values ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize().padding(values),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ScrollableTabRow(
                            contentColor = if (isSystemInDarkTheme()) darkmodebackground else lightmodebackground,
                            selectedTabIndex = selectedTabIndex,
                            edgePadding = 16.dp
                        ) {
                            calculators.forEachIndexed {index, i ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = { Text(i) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        when (selectedTabIndex) {
                            0 -> {
                                PoissonList(poissons = poissonState.value, navController)
                            }
                            1 -> {
                                BinomialList(binomials = binomialState.value, navController)
                            }
                            2 -> {
                                MultinomialList(multinomials = multinomialState.value, navController)
                            }
                            3 -> {
                                AnovaList(anovas = anovaState.value, navController)
                            }
                            4 -> {
                                BayesRuleList(bayesrules = bayesruleState.value, navController)
                            }
                            5 -> {
                                SLRList(slrs = slrState.value, navController)
                            }
                            6 -> {
                                UnGroupedList(ungroupeds = ungroupedState.value, navController)
                            }
                            7 -> {
                                GroupedList(groupeds = groupedState.value, navController)
                            }
                            8 -> {
                                HypothesisList(hypothesiss = hypothesisState.value, navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
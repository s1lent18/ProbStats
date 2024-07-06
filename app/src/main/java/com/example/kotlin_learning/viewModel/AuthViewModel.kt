package com.example.kotlin_learning.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.forhistory.Anovaclass
import com.example.kotlin_learning.data.forhistory.BayesRuleclass
import com.example.kotlin_learning.data.forhistory.Binomialclass
import com.example.kotlin_learning.data.forhistory.Groupedclass
import com.example.kotlin_learning.data.forhistory.Hypothesisclass
import com.example.kotlin_learning.data.forhistory.Multinomialclass
import com.example.kotlin_learning.data.forhistory.Poissonclass
import com.example.kotlin_learning.data.forhistory.SLRclass
import com.example.kotlin_learning.data.forhistory.UnGroupedclass
import com.example.kotlin_learning.data.request.Users
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private fun adduser(user: Users) {
        val usersRef = database.child("users")
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            usersRef.child(userId).setValue(user)
                .addOnSuccessListener {
                    // User successfully added
                }
                .addOnFailureListener {
                    // Handle the error
                }
        }
    }

    private fun addpoisson(userId: String, poisson: Poissonclass) {
        val poissonId = database.child("poisson").push().key ?: return
        val poissonwithId = poisson.copy(poissonid = poissonId)

        database.child("users")
            .child(userId)
            .child("poisson")
            .child(poissonId)
            .setValue(poissonwithId)
    }

    private fun addbinomial(userId: String, binomial: Binomialclass) {
        val binomialId = database.child("binomial").push().key ?: return
        val binomialwithId = binomial.copy(binomialid = binomialId)

        database.child("users")
            .child(userId)
            .child("binomial")
            .child(binomialId)
            .setValue(binomialwithId)
    }

    private fun addmultinomial(userId: String, multinomial: Multinomialclass) {
        val multinomialId = database.child("multinomial").push().key ?: return
        val multinomialwithId = multinomial.copy(multinomialid = multinomialId)

        database.child("users")
            .child(userId)
            .child("multinomial")
            .child(multinomialId)
            .setValue(multinomialwithId)
    }

    private fun addanova(userId: String, anova: Anovaclass) {
        val anovaId = database.child("anova").push().key ?: return
        val anovawithId = anova.copy(anovaid = anovaId)

        database.child("users")
            .child(userId)
            .child("anova")
            .child(anovaId)
            .setValue(anovawithId)
    }

    private fun addbayesrule(userId: String, bayesrule: BayesRuleclass) {
        val bayesruleId = database.child("bayesrule").push().key ?: return
        val bayesrulewithId = bayesrule.copy(bayesruleId = bayesruleId)

        database.child("users")
            .child(userId)
            .child("bayesrule")
            .child(bayesruleId)
            .setValue(bayesrulewithId)
    }

    private fun addslr(userId: String, slr: SLRclass) {
        val slrId = database.child("slr").push().key ?: return
        val slrwithId = slr.copy(slrid = slrId)

        database.child("users")
            .child(userId)
            .child("slr")
            .child(slrId)
            .setValue(slrwithId)
    }

    private fun addungrouped(userId: String, ungrouped: UnGroupedclass) {
        val ungroupedId = database.child("ungrouped").push().key ?: return
        val ungroupedwithId = ungrouped.copy(ungroupedId = ungroupedId)

        database.child("users")
            .child(userId)
            .child("ungrouped")
            .child(ungroupedId)
            .setValue(ungroupedwithId)
    }

    private fun addgrouped(userId: String, grouped: Groupedclass) {
        val groupedId = database.child("grouped").push().key ?: return
        val groupedwithId = grouped.copy(groupedId = groupedId)

        database.child("users")
            .child(userId)
            .child("grouped")
            .child(groupedId)
            .setValue(groupedwithId)
    }

    private fun addhypothesis(userId: String, hypothesis: Hypothesisclass) {
        val hypothesisId = database.child("hypothesis").push().key ?: return
        val hypothesiswithId = hypothesis.copy(hypothesisId = hypothesisId)

        database.child("users")
            .child(userId)
            .child("hypothesis")
            .child(hypothesisId)
            .setValue(hypothesiswithId)
    }

    fun sendpoisson(userId: String, x: Float, lamda: Float, equal: Float, greater: Float, greaterequal: Float, less: Float, lessequal: Float) {
        val poisson = Poissonclass(
            userid = userId,
            x = x,
            lamda = lamda,
            equal = equal,
            greater = greater,
            greaterequal = greaterequal,
            less = less,
            lessequal = lessequal
        )
        addpoisson(userId, poisson)
    }

    fun sendbinomial(userId: String, x: Float, n: Float, p: Float, equal: Float, greater: Float, greaterequal: Float, less: Float, lessequal: Float) {
        val binomial = Binomialclass(
            userid = userId,
            x = x,
            n = n,
            p = p,
            equal = equal,
            greater = greater,
            greaterequal = greaterequal,
            less = less,
            lessequal = lessequal
        )
        addbinomial(userId, binomial)
    }

    fun sendmultinomial(userId: String, x: List<Float>, n: Float, p: List<Float>, ans: Float) {
        val multinomial = Multinomialclass(
            userid = userId,
            x = x,
            n = n,
            p = p,
            ans = ans
        )
        addmultinomial(userId, multinomial)
    }

    fun sendanova(userId: String, n: List<Float>, size: Int, sl: Float, MSB: Float, MSW: Float, SSB: Float, SSW: Float, dfB: Int, dfW: Int, fratio: Float, hypothesis: String) {
        val anova = Anovaclass (
            userid = userId,
            n = n,
            size = size,
            sl = sl,
            MSB = MSB,
            MSW = MSW,
            SSB = SSB,
            SSW = SSW,
            dfB = dfB,
            dfW = dfW,
            hypothesis = hypothesis,
            fratio = fratio
        )
        addanova(userId, anova)
    }

    fun sendbayesrule(userId: String, pa: Float, pb: Float, pab: Float, ans: Float) {
        val bayesrule = BayesRuleclass (
            userId = userId,
            pa = pa,
            pb = pb,
            pab = pab,
            ans = ans
        )
        addbayesrule(userId, bayesrule)
    }

    fun sendslr(userId: String, n: Int, x: List<Float>, y: List<Float>, alpha: Float, tail: Boolean, hypothesis: String, r: Float, t: Float, Y: String) {
        val slr = SLRclass(
            userId = userId,
            n = n,
            x = x,
            y = y,
            alpha = alpha,
            tail = tail,
            hypothesis = hypothesis,
            r = r,
            t = t,
            Y = Y
        )
        addslr(userId, slr)
    }

    fun sendungrouped(userId: String, n: List<Float>, shape: String, mean: Float, median: Float, mode: String, one: Float, q1: Float, q3: Float, sd: Float, stemleaf: Map<String, List<Float>>, three: Float, two: Float, variance: Float) {
        val ungrouped = UnGroupedclass (
            userId = userId,
            n = n,
            Shape_of_the_Distribution = shape,
            mean = mean,
            median = median,
            mode = mode,
            one = one,
            two = two,
            three = three,
            q1 = q1,
            q3 = q3,
            sd = sd,
            stemleaf = stemleaf,
            variance = variance
        )
        addungrouped(userId, ungrouped)
    }

    fun sendgrouped(userId: String, n: List<Float>, second: List<Float>, freq: List<Float>,mean: Float, median: String, mode: String, sd: Float, variance: Float) {
        val grouped = Groupedclass (
            userId = userId,
            first = n,
            second = second,
            freq = freq,
            mean = mean,
            median = median,
            mode = mode,
            sd = sd,
            variance = variance
        )
        addgrouped(userId, grouped)
    }

    fun sendhypothesis(userId: String, smean: Float, hmean: Float, n: Int, sd: Float, sl: Float, tail: Boolean, samplem: Boolean, hypothesis: String) {
        val Hypothesis = Hypothesisclass (
            userId = userId,
            smean = smean,
            hmean = hmean,
            n = n,
            sd = sd,
            sl = sl,
            tail = tail,
            samplem = samplem,
            hypothesis = hypothesis
        )
        addhypothesis(userId, Hypothesis)
    }

    fun receiverpoisson(userid: String, callback: (List<Poissonclass>) -> Unit) {
        database.child("users")
            .child(userid)
            .child("poisson")
            .get().addOnSuccessListener { snapshot ->
                val poissons = snapshot.children.mapNotNull { it.getValue(Poissonclass::class.java) }
                callback(poissons)
            }
    }

    fun receiverbinomial(userid: String, callback: (List<Binomialclass>) -> Unit) {
        database.child("users")
            .child(userid)
            .child("binomial")
            .get().addOnSuccessListener { snapshot ->
                val binomials = snapshot.children.mapNotNull { it.getValue(Binomialclass::class.java) }
                callback(binomials)
            }
    }

    fun receivermultinomial(userid: String, callback: (List<Multinomialclass>) -> Unit) {
        database.child("users")
            .child(userid)
            .child("multinomial")
            .get().addOnSuccessListener { snapshot ->
                val multinomials = snapshot.children.mapNotNull { it.getValue(Multinomialclass::class.java) }
                callback(multinomials)
            }
    }

    fun receiveranova(userid: String, callback: (List<Anovaclass>) -> Unit) {
        database.child("users")
            .child(userid)
            .child("anova")
            .get().addOnSuccessListener { snapshot ->
                val anovas = snapshot.children.mapNotNull { it.getValue(Anovaclass::class.java) }
                callback(anovas)
            }
    }

    fun receiverbayesrule(userid: String, callback: (List<BayesRuleclass>) -> Unit) {
        database.child("users")
            .child(userid)
            .child("bayesrule")
            .get().addOnSuccessListener { snapshot ->
                val bayesrules = snapshot.children.mapNotNull { it.getValue(BayesRuleclass::class.java) }
                callback(bayesrules)
            }
    }

    fun receiverslr(userid: String, callback: (List<SLRclass>) -> Unit) {
        database.child("users")
            .child(userid)
            .child("slr")
            .get().addOnSuccessListener { snapshot ->
                val slrs = snapshot.children.mapNotNull { it.getValue(SLRclass::class.java) }
                callback(slrs)
            }
    }

    fun receiverungrouped(userid: String, callback: (List<UnGroupedclass>) -> Unit) {
        database.child("users")
            .child(userid)
            .child("ungrouped")
            .get().addOnSuccessListener { snapshot ->
                val ungroupeds = snapshot.children.mapNotNull { it.getValue(UnGroupedclass::class.java) }
                callback(ungroupeds)
            }
    }

    fun receivergrouped(userid: String, callback: (List<Groupedclass>) -> Unit) {
        database.child("users")
            .child(userid)
            .child("grouped")
            .get().addOnSuccessListener { snapshot ->
                val groupeds = snapshot.children.mapNotNull { it.getValue(Groupedclass::class.java) }
                callback(groupeds)
            }
    }

    fun receiverhypothesis(userid: String, callback: (List<Hypothesisclass>) -> Unit) {
        database.child("users")
            .child(userid)
            .child("hypothesis")
            .get().addOnSuccessListener { snapshot ->
                val hypothesiss = snapshot.children.mapNotNull { it.getValue(Hypothesisclass::class.java) }
                callback(hypothesiss)
            }
    }

    private val firebaseauth = Firebase.auth
    private val _loggedin = MutableLiveData<Boolean>()
    val loggedin: LiveData<Boolean> = _loggedin
    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> get() = _errorMessage
    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        _loggedin.value = firebaseauth.currentUser != null
    }

    fun signin(email: String, password: String) {
        firebaseauth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loggedin.value = true
                } else {
                    _errorMessage.value = "Wrong Email/Password"
                }
            }
    }

    fun signup(email: String, password: String, username: Users) {
        firebaseauth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loggedin.value = true
                    adduser(username)
                } else {
                    _errorMessage.value = "SignUp Failed"
                }
            }
    }

    fun signout() {
        firebaseauth.signOut()
        _loggedin.value = false
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    fun getuserid() : String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun isUserSignedin() : Boolean {
        return firebaseauth.currentUser != null
    }

    fun getusername(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            database.child("users")
                .child(userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(Users::class.java)
                    _username.value = user?.username
                }
                .addOnFailureListener {
                    _username.value = null
                }
                .addOnCompleteListener {
                    _loading.value = false
                }
        }
    }
}
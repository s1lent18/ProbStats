package com.example.kotlin_learning.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.request.Users
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.EmailAuthProvider

class AuthViewModel : ViewModel() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val repository = Repository()
    private val firebaseauth = Firebase.auth
    private val _loggedin = MutableLiveData<Boolean>()
    val loggedin: LiveData<Boolean> = _loggedin
    private val _signedup = MutableLiveData<Boolean>()
    val signedup: LiveData<Boolean> = _signedup
    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> get() = _errorMessage
    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username
    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email
    private val _nos = MutableStateFlow(0)
    val nos: StateFlow<Int> = _nos
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _send = MutableLiveData<String?>(null)
    val send: LiveData<String?> get() = _send
    private val _done = MutableLiveData(false)
    val done: LiveData<Boolean> = _done

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

    fun signup(email: String, password: String, username: String) {
        repository.checkUsernameAvailability(username) { isAvailable ->
            if (isAvailable) {
                firebaseauth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            repository.adduser(email = email, username = username)
                            _signedup.value = true
                        } else {
                            _errorMessage.value = "Email Already in use"
                        }
                    }
            } else {
                _errorMessage.value = "Username Already in Use"
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

    fun getemail(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            database.child("users")
                .child(userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(Users::class.java)
                    _email.value = user?.email
                }
                .addOnFailureListener {
                    _email.value = null
                }
                .addOnCompleteListener {
                    _loading.value = false
                }
        }
    }

    fun getnos(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            database.child("users")
                .child(userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(Users::class.java)
                    _nos.value = user?.numberofSolutions!!
                }
                .addOnFailureListener {
                    _nos.value = 0
                }
                .addOnCompleteListener {
                    _loading.value = false
                }
        }
    }

    fun sendPasswordviaemail(email: String) {
        firebaseauth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _send.value = "Password reset email sent."
                } else {
                    _send.value = "Failed to send password reset email."
                }
            }
    }

    fun reauthenticateAndChangePassword(email: String, oldPassword: String, newPassword: String) {
        val user = firebaseauth.currentUser
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(email, oldPassword)
            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    _done.value = true
                                    signout()
                                } else {
                                    _errorMessage.value = "Failed to get"
                                }
                            }
                    } else {
                        _errorMessage.value = "Not Successful"
                    }
                }
        } else {
            _loggedin.value = false
        }
    }
}
package com.example.kotlin_learning.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.model.BayesRuleModel
import com.example.kotlin_learning.data.request.BayesRuleRequest
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.api.RetrofitInstance
import kotlinx.coroutines.launch

class BayesRuleViewModel : ViewModel() {

    private val bayesruleapi = RetrofitInstance.bayesruleapi
    private val _bayesruleresult = MutableLiveData<NetworkResponse<BayesRuleModel>>()
    val bayesruleresult : LiveData<NetworkResponse<BayesRuleModel>> = _bayesruleresult

    fun getBayesRuleAnswer(bayesrulerequest: BayesRuleRequest) {
        _bayesruleresult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = bayesruleapi.getBayesRulePara(bayesrulerequest.pa, bayesrulerequest.pb, bayesrulerequest.pab)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response:", response.body().toString())
                        _bayesruleresult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _bayesruleresult.value = NetworkResponse.Failure("Invalid Input")
                }
            }
            catch (e: Exception) {
                _bayesruleresult.value = NetworkResponse.Failure("Invalid Input")
            }
        }
    }
}
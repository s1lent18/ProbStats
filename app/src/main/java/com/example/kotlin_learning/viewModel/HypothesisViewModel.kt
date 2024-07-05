package com.example.kotlin_learning.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.model.HypothesisModel
import com.example.kotlin_learning.data.request.HypothesisRequest
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.api.RetrofitInstance
import kotlinx.coroutines.launch

class HypothesisViewModel : ViewModel() {

    private val hypothesisapi = RetrofitInstance.hypothesisapi
    private val _hypothesisresult = MutableLiveData<NetworkResponse<HypothesisModel>>()
    val hypothesisresult: LiveData<NetworkResponse<HypothesisModel>> = _hypothesisresult

    fun getHypothesisAnswer(hypothesisrequest: HypothesisRequest) {
        _hypothesisresult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = hypothesisapi.getHypothesisPara(hypothesisrequest.smean, hypothesisrequest.hmean, hypothesisrequest.n, hypothesisrequest.sd, hypothesisrequest.sl, hypothesisrequest.tail, hypothesisrequest.samplem)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response:", response.body().toString())
                        _hypothesisresult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _hypothesisresult.value = NetworkResponse.Failure("Invalid Input")
                }
            }
            catch (e: Exception) {
                _hypothesisresult.value = NetworkResponse.Failure("Invalid Input")
            }
        }
    }
}
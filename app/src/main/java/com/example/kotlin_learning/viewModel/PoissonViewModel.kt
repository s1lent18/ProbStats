package com.example.kotlin_learning.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.model.PoissonModel
import com.example.kotlin_learning.data.request.PoissonRequest
import com.example.kotlin_learning.data.api.RetrofitInstance
import kotlinx.coroutines.launch

class PoissonViewModel: ViewModel() {

    private val poissonapi = RetrofitInstance.poissonapi
    private val _poissonresult = MutableLiveData<NetworkResponse<PoissonModel>>()
    val poissonresult : LiveData<NetworkResponse<PoissonModel>> = _poissonresult

    fun getPoissonAnswer(poissonrequest: PoissonRequest) {
        _poissonresult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = poissonapi.getPoissonPara(poissonrequest.x, poissonrequest.lamda)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response:", response.body().toString())
                        _poissonresult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _poissonresult.value = NetworkResponse.Failure("Invalid Input")
                }
            }
            catch (e: Exception) {
                _poissonresult.value = NetworkResponse.Failure("Invalid Input")
            }
        }
    }
}

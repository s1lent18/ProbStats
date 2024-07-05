package com.example.kotlin_learning.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.api.RetrofitInstance
import com.example.kotlin_learning.data.model.UnGroupedModel
import com.example.kotlin_learning.data.request.UnGroupedRequest
import kotlinx.coroutines.launch

class UnGroupedViewModel : ViewModel() {

    private val ungroupedapi = RetrofitInstance.ungroupedapi
    private val _ungroupedresult = MutableLiveData<NetworkResponse<UnGroupedModel>>()
    val ungroupedresult : LiveData<NetworkResponse<UnGroupedModel>> = _ungroupedresult

    fun getUnGroupedAnswer(ungroupedrequest: UnGroupedRequest) {
        _ungroupedresult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = ungroupedapi.getUnGroupedPara(ungroupedrequest.n)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response:", response.body().toString())
                        _ungroupedresult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _ungroupedresult.value = NetworkResponse.Failure("Invalid Input")
                }
            }
            catch (e: Exception) {
                _ungroupedresult.value = NetworkResponse.Failure("Invalid Input")
            }
        }
    }
}
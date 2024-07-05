package com.example.kotlin_learning.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_learning.data.model.GroupedModel
import com.example.kotlin_learning.data.request.GroupedRequest
import com.example.kotlin_learning.data.response.NetworkResponse
import com.example.kotlin_learning.data.api.RetrofitInstance
import kotlinx.coroutines.launch

class GroupedViewModel : ViewModel() {

    private val groupedapi = RetrofitInstance.groupedapi
    private val _groupedresult = MutableLiveData<NetworkResponse<GroupedModel>>()
    val groupedresult : LiveData<NetworkResponse<GroupedModel>> = _groupedresult

    fun getGroupedAnswer(groupedrequest: GroupedRequest) {
        _groupedresult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = groupedapi.getGroupedPara(groupedrequest.first, groupedrequest.second, groupedrequest.freq)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response:", response.body().toString())
                        _groupedresult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _groupedresult.value = NetworkResponse.Failure("Invalid Input")
                }
            }
            catch (e: Exception) {
                _groupedresult.value = NetworkResponse.Failure("Invalid Input")
            }
        }
    }
}
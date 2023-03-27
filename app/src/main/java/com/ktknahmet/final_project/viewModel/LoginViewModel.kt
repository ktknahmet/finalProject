package com.ktknahmet.final_project.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ktknahmet.final_project.model.loginModel.LoginResponse
import com.ktknahmet.final_project.sevices.ApiService
import com.ktknahmet.final_project.sevices.NetworkResult
import com.ktknahmet.final_project.ui.base.BaseViewModel
import com.ktknahmet.final_project.utils.SingleLiveEvent
import com.ktknahmet.final_project.utils.getData
import dagger.hilt.android.lifecycle.HiltViewModel


import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val api: ApiService) : BaseViewModel() {

     private val dataLoading: MutableLiveData<Boolean> = MutableLiveData()
     val liveUserData :SingleLiveEvent<NetworkResult<List<LoginResponse>>> = SingleLiveEvent()


    fun getLoginCheckUser(): SingleLiveEvent<NetworkResult<List<LoginResponse>>> {
        dataLoading.value = true
        viewModelScope.launch {
            api.getUserData().getData(dataLoading) {
                liveUserData.value = it
            }
        }

        return liveUserData

    }

}






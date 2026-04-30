package com.aimcode.userdirectory.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimcode.userdirectory.core.data.repository.UserRepository
import com.aimcode.userdirectory.core.model.Resource
import com.aimcode.userdirectory.core.model.request.UserRequest
import com.aimcode.userdirectory.core.model.response.UserResponse
import com.aimcode.userdirectory.core.ui.UiLoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private var _addUserState by mutableStateOf<UiLoadState<UserResponse>>(UiLoadState.Idle())
    val addUserState get() = _addUserState

    var name by mutableStateOf("")
        private set

    var address by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    var city by mutableStateOf("")
        private set

    var gender by mutableIntStateOf(1)
        private set

    fun onNameChange(value: String) { name = value }
    fun onAddressChange(value: String) { address = value }
    fun onEmailChange(value: String) { email = value }
    fun onPhoneNumberChange(value: String) { phoneNumber = value }
    fun onCityChange(value: String) { city = value }
    fun onGenderChange(value: Int) { gender = value }

    fun resetState() {
        _addUserState = UiLoadState.Idle()
    }

    val isFormValid: Boolean
        get() = name.isNotBlank() &&
                address.isNotBlank() &&
                email.isNotBlank() &&
                phoneNumber.isNotBlank() &&
                city.isNotBlank()

    fun addUser() {
        viewModelScope.launch {
            _addUserState = UiLoadState.Loading()
            _addUserState = when (
                val result = userRepository.addUser(
                    UserRequest(
                        name = name,
                        address = address,
                        email = email,
                        phoneNumber = phoneNumber,
                        city = city,
                        gender = gender
                    )
                )
            ) {
                is Resource.Success -> UiLoadState.Success(result.data)
                is Resource.Failed -> UiLoadState.Failed(result.code)
            }
        }
    }
}
package practice.app.stateflowpractice

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)

    fun onLoginUiState(): StateFlow<LoginUiState> = _loginUiState

    fun login(username: String, password: String) {

        viewModelScope.launch {

            _loginUiState.value = LoginUiState.Loading

            delay(1000L)

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {

                _loginUiState.value = LoginUiState.InvalidUser("Username or Password should not be null")

            } else {

                if (username == "android" && password == "password") {

                    val user = User("1", username)
                    _loginUiState.value = LoginUiState.Success(user)

                } else {

                    val error = "Wrong credentials"
                    _loginUiState.value = LoginUiState.Error(Throwable(error))

                }

            }


        }

    }

}

sealed class LoginUiState {

    data class Success(val user: User) : LoginUiState()
    data class Error(val error: Throwable) : LoginUiState()
    object Loading : LoginUiState()
    object Empty : LoginUiState()
    data class InvalidUser(val message: String) : LoginUiState()

}

data class User(val id: String, val username: String)

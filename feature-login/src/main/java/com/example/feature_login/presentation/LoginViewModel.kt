package com.example.feature_login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_common.AppResult
import com.example.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> = _uiState

    private val _events =
        MutableSharedFlow<LoginUiEvent>()

    val events = _events.asSharedFlow()

    fun onAction(action: LoginUiAction) {

        when (action) {

            is LoginUiAction.UsernameChanged -> {

                _uiState.update {
                    it.copy(username = action.username)
                }
            }
            is LoginUiAction.PasswordChanged -> {

                _uiState.update {
                    it.copy(password = action.password)
                }

            }
            LoginUiAction.LoginClicked -> {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                login()
            }
        }
    }

   private fun login(){
           viewModelScope.launch {

               _uiState.update {
                   it.copy(isLoading = true)
               }

               when (
                   loginUseCase(
                       _uiState.value.username,
                       _uiState.value.password
                   )
               ) {

                   is AppResult.Success -> {

                       _uiState.update {
                           it.copy(
                               isLoading = false,
                               isLoggedIn = true
                           )
                       }

                       _events.emit(
                           LoginUiEvent.NavigateToHome
                       )

                   }

                   is AppResult.Error -> Unit
               }
           }
    }
}
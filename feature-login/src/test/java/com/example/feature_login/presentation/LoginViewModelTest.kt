package com.example.feature_login.presentation


import app.cash.turbine.test
import com.example.core_common.AppResult
import com.example.core_testing.fake.FakeUserRepository
import com.example.domain.error.LoginError
import com.example.domain.error.toMessage
import com.example.domain.model.User
import com.example.domain.usecase.LoginUseCase
import com.example.feature_login.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initial_state_is_correct() = runTest {

        // Arrange
        val repository = FakeUserRepository()
        val useCase = LoginUseCase(repository)

        val viewModel = LoginViewModel(useCase)

        // Assert
        viewModel.uiState.test {

            val state = awaitItem()

            assertThat(state.isLoading).isFalse()
            assertThat(state.isLoggedIn).isFalse()
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEmpty()
            assertThat(state.error).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun login_success_updates_logged_in_state() = runTest {

        val repository = FakeUserRepository()

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        viewModel.onAction(LoginUiAction.UsernameChanged("admin"))
        viewModel.onAction(LoginUiAction.PasswordChanged("1234"))

        viewModel.onAction(LoginUiAction.LoginClicked)

        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isLoggedIn)
            .isTrue()
    }

    @Test
    fun username_changed_updates_ui_state() = runTest {

        val viewModel = LoginViewModel(
            LoginUseCase(FakeUserRepository())
        )

        viewModel.onAction(
            LoginUiAction.UsernameChanged("admin")
        )

        assertThat(viewModel.uiState.value.username)
            .isEqualTo("admin")
    }

    @Test
    fun password_changed_updates_ui_state() = runTest {

        val viewModel = LoginViewModel(
            LoginUseCase(FakeUserRepository())
        )

        viewModel.onAction(
            LoginUiAction.PasswordChanged("1234")
        )

        assertThat(viewModel.uiState.value.password)
            .isEqualTo("1234")
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun login_clicked_calls_use_case() = runTest {

        val repository = FakeUserRepository()

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        viewModel.onAction(
            LoginUiAction.UsernameChanged("admin")
        )

        viewModel.onAction(
            LoginUiAction.PasswordChanged("1234")
        )

        viewModel.onAction(LoginUiAction.LoginClicked)
        advanceUntilIdle()
        assertThat(repository.loginCalled)
            .isTrue()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun successful_login_updates_logged_in_state() = runTest {

        val repository = FakeUserRepository()

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        viewModel.onAction(
            LoginUiAction.UsernameChanged("admin")
        )

        viewModel.onAction(
            LoginUiAction.PasswordChanged("1234")
        )

        viewModel.onAction(LoginUiAction.LoginClicked)

        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isLoggedIn)
            .isTrue()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun successful_login_emits_navigation_event() = runTest {

        val repository = FakeUserRepository()

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        viewModel.events.test {

            viewModel.onAction(
                LoginUiAction.UsernameChanged("admin")
            )

            viewModel.onAction(
                LoginUiAction.PasswordChanged("1234")
            )

            viewModel.onAction(LoginUiAction.LoginClicked)

            advanceUntilIdle()

            assertThat(awaitItem())
                .isEqualTo(LoginUiEvent.NavigateToHome)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun login_failure_updates_error() = runTest {

        // Arrange
        val repository = FakeUserRepository().apply {
            givenLoginSuccess()
        }

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        viewModel.onAction(
            LoginUiAction.UsernameChanged("admin")
        )

        viewModel.onAction(
            LoginUiAction.PasswordChanged("wrong")
        )

        // Act
        viewModel.onAction(LoginUiAction.LoginClicked)

        advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value.isLoggedIn)
            .isFalse()

        assertThat(viewModel.uiState.value.isLoading)
            .isFalse()

        assertThat(viewModel.uiState.value.error)
            .isEqualTo(LoginError.InvalidCredentials.toMessage())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun login_failure_emits_snackbar() = runTest {

        // Arrange
        val repository = FakeUserRepository().apply {
            givenLoginSuccess()
        }

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        viewModel.onAction(
            LoginUiAction.UsernameChanged("admin")
        )

        viewModel.onAction(
            LoginUiAction.PasswordChanged("wrong")
        )

        // Assert
        viewModel.events.test {

            // Act
            viewModel.onAction(LoginUiAction.LoginClicked)

            advanceUntilIdle()

            assertThat(awaitItem())
                .isEqualTo(
                    LoginUiEvent.ShowSnackbar(
                        LoginError.InvalidCredentials
                    )
                )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun successful_login_clears_previous_error() = runTest {

        // Arrange
        val repository = FakeUserRepository()

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        // First attempt fails
        repository.givenLoginFailure(LoginError.InvalidCredentials)

        viewModel.onAction(LoginUiAction.UsernameChanged("admin"))
        viewModel.onAction(LoginUiAction.PasswordChanged("wrong"))
        viewModel.onAction(LoginUiAction.LoginClicked)

        advanceUntilIdle()

        assertThat(viewModel.uiState.value.error)
            .isEqualTo(LoginError.InvalidCredentials.toMessage())

        // Second attempt succeeds
        repository.givenLoginSuccess()

        viewModel.onAction(LoginUiAction.PasswordChanged("1234"))
        viewModel.onAction(LoginUiAction.LoginClicked)

        advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value.error)
            .isNull()

        assertThat(viewModel.uiState.value.isLoggedIn)
            .isTrue()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loading_is_hidden_after_login_failure() = runTest {

        // Arrange
        val repository = FakeUserRepository().apply {
            givenLoginFailure(LoginError.InvalidCredentials)
        }

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        viewModel.onAction(LoginUiAction.UsernameChanged("admin"))
        viewModel.onAction(LoginUiAction.PasswordChanged("wrong"))

        // Act
        viewModel.onAction(LoginUiAction.LoginClicked)

        advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value.isLoading)
            .isFalse()

        assertThat(viewModel.uiState.value.isLoggedIn)
            .isFalse()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun latest_username_and_password_are_used_for_login() = runTest {

        // Arrange
        val repository = FakeUserRepository().apply {
            givenLoginSuccess()
        }

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        // User changes credentials multiple times
        viewModel.onAction(LoginUiAction.UsernameChanged("oldUser"))
        viewModel.onAction(LoginUiAction.PasswordChanged("oldPass"))

        viewModel.onAction(LoginUiAction.UsernameChanged("admin"))
        viewModel.onAction(LoginUiAction.PasswordChanged("1234"))

        // Act
        viewModel.onAction(LoginUiAction.LoginClicked)

        advanceUntilIdle()

        // Assert
        assertThat(repository.lastUsername)
            .isEqualTo("admin")

        assertThat(repository.lastPassword)
            .isEqualTo("1234")
    }
}
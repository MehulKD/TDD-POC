package com.example.feature_login.presentation


import app.cash.turbine.test
import com.example.core_testing.fake.FakeUserRepository
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
    fun login_clicked_shows_loading() = runTest {

        val repository = FakeUserRepository()

        val viewModel = LoginViewModel(
            LoginUseCase(repository)
        )

        viewModel.uiState.test {
            viewModel.onAction(LoginUiAction.UsernameChanged("admin"))
            viewModel.onAction(LoginUiAction.PasswordChanged("1234"))
            viewModel.onAction(LoginUiAction.LoginClicked)

            // Loading state
            assertThat(awaitItem().isLoading).isTrue()
        }
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
}
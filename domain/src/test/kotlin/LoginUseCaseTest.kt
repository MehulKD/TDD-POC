import com.example.core_common.AppResult
import com.example.core_testing.fake.FakeUserRepository
import com.example.domain.error.LoginError
import com.example.domain.usecase.LoginUseCase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.jvm.java

class LoginUseCaseTest {

    @Test
    fun `login with valid credentials should return success`() = runTest {

        val repository = FakeUserRepository()

        val useCase = LoginUseCase(repository)

        val result = useCase(
            username = "admin",
            password = "1234"
        )

        assertThat(result).isInstanceOf(AppResult.Success::class.java)
    }

    @Test
    fun login_with_invalid_password_returns_error() = runTest {
        val repository = FakeUserRepository()
        val useCase = LoginUseCase(repository)

        val result = useCase(
            username = "admin",
            password = "wrong"
        )

        assertThat(result)
            .isEqualTo(AppResult.Error(LoginError.InvalidCredentials))
    }

    @Test
    fun login_with_empty_username_returns_error() = runTest {

        val repository = FakeUserRepository()
        val useCase = LoginUseCase(repository)

        val result = useCase(
            username = "",
            password = "1234"
        )

        assertThat(result)
            .isEqualTo(AppResult.Error(LoginError.EmptyUsername))
    }

    @Test
    fun login_with_empty_password_returns_empty_password_error() = runTest {

        val repository = FakeUserRepository()
        val useCase = LoginUseCase(repository)

        val result = useCase(
            username = "admin",
            password = ""
        )

        assertThat(result)
            .isEqualTo(AppResult.Error(LoginError.EmptyPassword))
    }

    @Test
    fun login_trims_username_before_authentication() = runTest {

        val repository = FakeUserRepository()
        val useCase = LoginUseCase(repository)

        val result = useCase(
            username = "  admin  ",
            password = "1234"
        )

        assertThat(result)
            .isInstanceOf(AppResult.Success::class.java)
    }

    @Test
    fun login_password_is_case_sensitive() = runTest {

        val repository = FakeUserRepository()
        val useCase = LoginUseCase(repository)

        val result = useCase(
            username = "admin",
            password = "1234ABC"
        )

        assertThat(result)
            .isEqualTo(AppResult.Error(LoginError.InvalidCredentials))
    }

    @Test
    fun login_username_is_case_insensitive() = runTest {

        val repository = FakeUserRepository()
        val useCase = LoginUseCase(repository)

        val result = useCase(
            username = "ADMIN",
            password = "1234"
        )

        assertThat(result)
            .isInstanceOf(AppResult.Success::class.java)
    }

    @Test
    fun login_with_blank_spaces_only_username_returns_empty_username() = runTest {

        val repository = FakeUserRepository()
        val useCase = LoginUseCase(repository)

        val result = useCase(
            username = "     ",
            password = "1234"
        )

        assertThat(result)
            .isEqualTo(AppResult.Error(LoginError.EmptyUsername))
    }

    @Test
    fun login_with_blank_spaces_only_password_returns_empty_password() = runTest {

        val repository = FakeUserRepository()
        val useCase = LoginUseCase(repository)

        val result = useCase(
            username = "admin",
            password = "      "
        )

        assertThat(result)
            .isEqualTo(AppResult.Error(LoginError.EmptyPassword))
    }
}
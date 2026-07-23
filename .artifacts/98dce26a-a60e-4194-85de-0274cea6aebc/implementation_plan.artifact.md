# Fix failing LoginViewModel tests

The user is reporting that `login_clicked_shows_loading` is failing even though the code "looks correct". Investigation reveals that the test fails because it incorrectly asserts on the first item emitted by Turbine (the initial state) rather than the state after the login action is triggered. Additionally, other tests are failing because core logic in `LoginViewModel` is commented out.

## Proposed Changes

### [Component Name] Login Feature

#### [MODIFY] [LoginViewModel.kt](file:///C:/Users/mehulk/AndroidStudioProjects/TDDPOC/feature-login/src/main/java/com/example/feature_login/presentation/LoginViewModel.kt)
- Uncomment the logic in `login()` to update the UI state with the result and emit navigation events.
- Clean up redundant/commented code.

#### [MODIFY] [LoginViewModelTest.kt](file:///C:/Users/mehulk/AndroidStudioProjects/TDDPOC/feature-login/src/test/java/com/example/feature_login/presentation/LoginViewModelTest.kt)
- Fix `login_clicked_shows_loading` to correctly consume intermediate states or skip them when using Turbine.
- Ensure other tests correctly handle the asynchronous nature of the login process using `advanceUntilIdle()`.

## Verification Plan

### Automated Tests
- Run all tests in `LoginViewModelTest.kt` to ensure they pass.
- Command: `./gradlew :feature-login:testDebugUnitTest`

### Manual Verification
- None required as this is a unit testing fix.

package ru.fasdev.tfs.screen.fragment.anotherProfile.mvi

import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.mviCore.entity.state.UiState

data class AnotherProfileState(val user: User? = null, val isLoading: Boolean = false, val error: Throwable? = null) : UiState

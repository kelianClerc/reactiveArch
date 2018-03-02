package com.fabernovel.technologies.app.common

import com.fabernovel.technologies.core.RandomUserReactiveError

sealed class UiState {
    object Loading : UiState()
    data class Error(val message: RandomUserReactiveError) : UiState()
    object Data : UiState()
    object Empty : UiState()
}

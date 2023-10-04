package com.cristianomadeira.ulessontest.ui.state

sealed interface ScreenUiState<out T> {
    data object Loading : ScreenUiState<Nothing>
    data class Success <out T>(val data: T) : ScreenUiState<T>
}

inline fun <T> ScreenUiState<T>.onSuccess(block: ScreenUiState.Success<T>.() -> Unit) =
    if (this is ScreenUiState.Success) block() else Unit

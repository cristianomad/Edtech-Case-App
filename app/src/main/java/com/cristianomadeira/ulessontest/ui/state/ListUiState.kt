package com.cristianomadeira.ulessontest.ui.state

sealed interface ListUiState<out T: List<Any>> {
    data object Loading : ListUiState<Nothing>
    data object Empty : ListUiState<Nothing>
    data class Success <out T: List<Any>>(val data: T) : ListUiState<T>
}

inline fun <T: List<Any>> ListUiState<T>.onSuccess(block: ListUiState.Success<T>.() -> Unit) =
    if (this is ListUiState.Success) block() else Unit

package com.valmiraguiar.listo.feature.lists.presentation.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.listo.feature.common.extensions.onError
import com.valmiraguiar.listo.feature.common.extensions.onSuccess
import com.valmiraguiar.listo.feature.lists.domain.usecase.ObserveShoppingListsUseCase
import com.valmiraguiar.listo.feature.lists.presentation.list.state.ShoppingListUiResult
import com.valmiraguiar.listo.feature.lists.presentation.list.state.ShoppingListsUiAction
import com.valmiraguiar.listo.feature.lists.presentation.list.state.ShoppingListsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    private val observeShoppingListsUseCase: ObserveShoppingListsUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ShoppingListsUiState> by lazy {
        MutableStateFlow(
            ShoppingListsUiState()
        )
    }
    val uiState: StateFlow<ShoppingListsUiState> get() = _uiState

    private val _uiResult: MutableSharedFlow<ShoppingListUiResult> =
        MutableSharedFlow()
    val uiResult: SharedFlow<ShoppingListUiResult> get() = _uiResult

    private fun emitUiResult(uiResult: ShoppingListUiResult) = viewModelScope.launch {
        _uiResult.emit(uiResult)
    }

    fun dispatch(action: ShoppingListsUiAction) {
        when (action) {
            is ShoppingListsUiAction.FetchLists -> fetchShoppingLists()

            is ShoppingListsUiAction.BackClick -> emitUiResult(ShoppingListUiResult.OnNavigateBack)

            is ShoppingListsUiAction.ItemListClick -> emitUiResult(
                ShoppingListUiResult.OnDetailListNavigate(
                    action.cardId
                )
            )

            is ShoppingListsUiAction.NewListClick -> emitUiResult(ShoppingListUiResult.OnCreateListNavigate)
        }
    }

    private fun fetchShoppingLists() {
        observeShoppingListsUseCase().onStart {
            updateUiState { copy(isLoading = true) }
        }.onCompletion {
            updateUiState { copy(isLoading = false) }
        }.onSuccess { resume ->
            handleFetchShoppingListsSuccess(resume)
        }.onError { error ->
            handleError(error)
        }.launchIn(viewModelScope)
    }

    fun handleError(error: Throwable) {
        Log.e("app-error-log", error.toString()) // TODO - fix error log
    }

    private fun handleFetchShoppingListsSuccess(resume: List<ShoppingListSummary>) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    shoppingLists = resume
                )
            }
        }
    }

    private fun updateUiState(reduce: ShoppingListsUiState.() -> ShoppingListsUiState) {
        _uiState.value = uiState.value.reduce()
    }

    private companion object {
        val MOCK = listOf(
            ShoppingListSummary(
                id = 1L,
                title = "Teste1",
                createdAt = 1_784_324_400_000L
            ),
            ShoppingListSummary(
                id = 2L,
                title = "Teste2",
                createdAt = 1_784_324_400_000L
            ),
            ShoppingListSummary(
                id = 3L,
                title = "Teste3",
                createdAt = 1_784_324_400_000L
            ),
            ShoppingListSummary(
                id = 4L,
                title = "Teste4",
                createdAt = 1_784_324_400_000L
            ),
            ShoppingListSummary(
                id = 5L,
                title = "Teste5",
                createdAt = 1_784_324_400_000L
            )
        )
    }
}

package com.valmiraguiar.listo.feature.lists.domain.usecase

import com.valmiraguiar.listo.feature.common.flow.FlowResult
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveShoppingListsUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    operator fun invoke(): Flow<FlowResult<List<ShoppingList>, Throwable>> = flow {
        shoppingListRepository.observeShoppingLists().catch {
            emit(FlowResult.Error(it))
        }.collect {
            emit(FlowResult.Success(it))
        }
    }
}

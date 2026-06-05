package com.valmiraguiar.listo.feature.lists.domain.usecase

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListSummary
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveShoppingListsUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    operator fun invoke(): Flow<List<ShoppingListSummary>> {
        return shoppingListRepository.observeShoppingLists()
    }
}

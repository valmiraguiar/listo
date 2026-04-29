package com.valmiraguiar.listo.feature.lists.domain.usecase

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import javax.inject.Inject

class CreateShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(draft: ShoppingListDraft): Long {
        return shoppingListRepository.createShoppingList(draft)
    }
}

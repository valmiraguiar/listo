package com.valmiraguiar.listo.feature.lists.domain.usecase

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import javax.inject.Inject

class UpdateShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(shoppingList: ShoppingList) {
        shoppingListRepository.updateShoppingList(shoppingList)
    }
}

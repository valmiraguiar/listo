package com.valmiraguiar.listo.feature.lists.domain.usecase

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import javax.inject.Inject

class CreateShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(shoppingList: ShoppingList) {
        shoppingListRepository.createShoppingList(shoppingList)
    }
}

package com.valmiraguiar.listo.feature.lists.domain.usecase

import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import javax.inject.Inject

class UpdateProductsCheckedStateUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(shoppingListId: Long, checkedProductIds: Set<Long>) {
        shoppingListRepository.updateProductsCheckedState(
            shoppingListId = shoppingListId,
            checkedProductIds = checkedProductIds,
        )
    }
}

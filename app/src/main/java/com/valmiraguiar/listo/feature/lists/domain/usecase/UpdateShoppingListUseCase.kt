//package com.valmiraguiar.listo.feature.lists.domain.usecase
//
//import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
//import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
//import javax.inject.Inject
//
//class UpdateShoppingListUseCase @Inject constructor(
//    private val shoppingListRepository: ShoppingListRepository,
//) {
//    suspend operator fun invoke(listId: Long, draft: ShoppingListDraft): Long {
//        return shoppingListRepository.updateShoppingList(listId, draft)
//    }
//}

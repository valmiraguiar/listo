//package com.valmiraguiar.listo.feature.lists.domain.usecase
//
//import com.valmiraguiar.listo.feature.common.flow.FlowResult
//import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDetails
//import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.flow
//import javax.inject.Inject
//
//class ObserveShoppingListDetailsUseCase @Inject constructor(
//    private val shoppingListRepository: ShoppingListRepository,
//) {
//    operator fun invoke(
//        listId: Long,
//    ): Flow<FlowResult<ShoppingListDetails?, Throwable>> = flow {
//        shoppingListRepository.observeShoppingListDetails(listId).catch {
//            emit(FlowResult.Error(it))
//        }.collect {
//            emit(FlowResult.Success(it))
//        }
//    }
//}

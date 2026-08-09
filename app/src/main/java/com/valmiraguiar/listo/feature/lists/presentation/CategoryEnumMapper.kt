package com.valmiraguiar.listo.feature.lists.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.lists.domain.model.CategoryEnum

@Composable
fun CategoryEnum.label(): String {
    val stringId = when (this) {
        CategoryEnum.Beverages -> R.string.category_beverages
        CategoryEnum.Grocery -> R.string.category_grocery
        CategoryEnum.Dairy -> R.string.category_dairy
        CategoryEnum.Meat -> R.string.category_meat
    }
    return stringResource(id = stringId)
}

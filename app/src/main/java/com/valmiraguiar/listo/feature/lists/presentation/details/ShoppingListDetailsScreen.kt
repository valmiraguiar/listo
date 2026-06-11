package com.valmiraguiar.listo.feature.lists.presentation.details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.valmiraguiar.listo.ui.theme.ListoTheme

@Composable
fun ShoppingListDetailsRoute(
    onBack: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    ShoppingListDetailsScreen(
        onBack = onBack,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailsScreen(
    onBack: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    Text("Details route")
}

@Preview(showBackground = true)
@Composable
private fun ShoppingListDetailsScreenPreview() {
    ListoTheme {
        ShoppingListDetailsScreen(onBack = { true })
    }
}

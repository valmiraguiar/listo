package com.valmiraguiar.listo.feature.lists.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valmiraguiar.listo.ui.theme.ListoTheme

@Composable
fun ShoppingListDetailsRoute(
    onBack: () -> Unit,
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
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onBack
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back arrow",
                modifier = Modifier
                    .size(24.dp)
            )
        }


        LazyColumn(
            modifier = Modifier,
        ) {
            items(
                items = MOCK,
                key = { mockList -> mockList },
                contentType = { "shopping-list" }
            ) { item ->
                OutlinedCard() { }
                Text(text = item)
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Details route")

//            FloatingActionButton(
//                onClick = {},
//                containerColor = MaterialTheme.colorScheme.primary,
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(8.dp),
//            ) {
//                Row(
//                    modifier = Modifier.padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "Add item list",
//                        modifier = Modifier.size(16.dp),
//                    )
//
//                    Text(text = stringResource(R.string.detail_list_add_item))
//                }
//            }
        }
    }


}

private val MOCK = listOf("Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7")

@Preview(showBackground = true)
@Composable
private fun ShoppingListDetailsScreenPreview() {
    ListoTheme {
        ShoppingListDetailsScreen(onBack = { true })
    }
}

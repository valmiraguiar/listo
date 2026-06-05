package com.valmiraguiar.listo.feature.lists.presentation.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
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
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.shopping_list_details_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = dropUnlessResumed {
                            onBack()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.action_back),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        ShoppingListDetailsRoute(
            onBack = onBack,
            modifier = modifier.padding(contentPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShoppingListDetailsScreenPreview() {
    ListoTheme {
        ShoppingListDetailsScreen(onBack = { true })
    }
}

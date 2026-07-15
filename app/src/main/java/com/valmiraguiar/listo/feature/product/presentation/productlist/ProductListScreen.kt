package com.valmiraguiar.listo.feature.product.presentation.productlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.theme.ListoTheme

@Composable
fun ProductListRoute() {
    ProductListScreen()
}

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CreateListTopBar({
                println("TESTE")
                true
            })
        }
    }
}

@Composable
private fun CreateListTopBar(
    onBack: () -> Boolean,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = dropUnlessResumed {
                onBack()
            },
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.action_back),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            text = stringResource(id = R.string.create_list_topbar_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateProductScreenPreview() {
    ListoTheme {
        ProductListScreen()
    }
}

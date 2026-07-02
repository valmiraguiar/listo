package com.valmiraguiar.listo.feature.lists.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.components.UnderlinedTextField
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum
import com.valmiraguiar.listo.feature.product.presentation.createproduct.DraftListItemUiState
import com.valmiraguiar.listo.ui.theme.ListoTheme

@Composable
fun CreateListRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val successMessage = stringResource(id = R.string.create_list_saved_message)

    LaunchedEffect(uiState.saveConfirmationVisible) {
        if (uiState.saveConfirmationVisible) {
            snackbarHostState.showSnackbar(
                message = successMessage,
            )
            viewModel.dismissSaveConfirmation()
        }
    }

    CreateListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onListTitleChange = viewModel::updateListTitle,
        onCreateList = viewModel::createList,
        modifier = modifier,
    )
}

@Composable
fun CreateListScreen(
    uiState: CreateListUiState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onListTitleChange: (String) -> Unit,

    onCreateList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
            ) {
                CreateListTopBar(onBack = onBack)
                Spacer(modifier = Modifier.height(28.dp))
                UnderlinedTextField(
                    value = uiState.listTitle,
                    onValueChange = onListTitleChange,
                    label = stringResource(id = R.string.create_list_name_label),
                    placeholder = stringResource(id = R.string.create_list_name_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))
                LabeledSection(title = stringResource(id = R.string.create_list_items_label)) {
                }

                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = dropUnlessResumed(block = onCreateList),
                    enabled = uiState.canSubmit,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(999.dp),
                    contentPadding = PaddingValues(horizontal = 30.dp, vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.create_list_cta),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }

    /*
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Details route")

        FloatingActionButton(
            onClick = {},
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add item list",
                    modifier = Modifier.size(16.dp),
                )

                Text(text = stringResource(R.string.detail_list_add_item))
            }
        }
    }
     */
    // TODO - Fix this screen
}

@Composable
private fun CreateListTopBar(
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
//                IconButton(
//                    onClick = dropUnlessResumed {
//                        onBack()
//                    },
//                ) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
//                        contentDescription = stringResource(id = R.string.action_back),
//                    )
//                }
                IconButton(
                    onClick = dropUnlessResumed {
                        onBack()
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.action_back),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Text(
                    text = stringResource(id = R.string.create_list_topbar_title),
                    style = MaterialTheme.typography.headlineMedium
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = dropUnlessResumed {
                        onBack()
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.action_back),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Text(text = "Concluir")
            }

        }
    }
}

@Composable
private fun LabeledSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(10.dp))
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateListScreenPreview() {
    ListoTheme {
        CreateListScreen(
            uiState = CreateListUiState(
                listTitle = "Churrasco do fim de semana",
                items = listOf(
                    DraftListItemUiState(
                        id = 1L,
                        quantity = "2",
                        unit = UnitEnum.Kilogram,
                        description = "Picanha",
                        categoryEnum = CategoryEnum.Meat,
                    ),
                    DraftListItemUiState(
                        id = 2L,
                        quantity = "6",
                        unit = UnitEnum.Unit,
                        description = "Refrigerante",
                        categoryEnum = CategoryEnum.Beverages,
                    ),
                ),
            ),
            snackbarHostState = SnackbarHostState(),
            onBack = { true },
            onListTitleChange = {},
            onCreateList = {},
        )
    }
}

package com.valmiraguiar.listo.feature.lists.presentation.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.components.LaunchOnce
import com.valmiraguiar.listo.feature.common.theme.ListoTheme
import com.valmiraguiar.listo.feature.common.theme.Red
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.presentation.list.state.ShoppingListUiResult
import com.valmiraguiar.listo.feature.lists.presentation.list.state.ShoppingListsUiAction
import com.valmiraguiar.listo.feature.lists.presentation.list.state.ShoppingListsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date
import kotlin.math.roundToInt

private const val DIVIDER_ALPHA = 0.35f
private const val DELETE_REVEAL_ANIMATION_DURATION_MILLIS = 220
private const val DELETE_ITEM_ANIMATION_DURATION_MILLIS = 280
private const val DELETE_REVEAL_THRESHOLD = 0.45f
private const val DELETE_REVEAL_FLING_VELOCITY = 700f
private const val DELETE_ACTION_WIDTH_DP = 72
private val deleteActionWidth = DELETE_ACTION_WIDTH_DP.dp

@Composable
fun ShoppingListsRoute(
    onShoppingListClickNavigate: (Long) -> Unit,
    onCreateListClickNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShoppingListsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiResult) {
        viewModel.uiResult.collect { result ->
            when (result) {
                is ShoppingListUiResult.OnCreateListNavigate -> onCreateListClickNavigate()
                is ShoppingListUiResult.OnDetailListNavigate -> onShoppingListClickNavigate(result.listId)
                is ShoppingListUiResult.OnError -> Unit // TODO - Not implemented yet
                is ShoppingListUiResult.OnListDeleted -> Unit // TODO - Not implemented yet
                is ShoppingListUiResult.OnLoading -> Unit // TODO - Not implemented yet
                is ShoppingListUiResult.OnNavigateBack -> Unit // TODO - Not implemented yet
                is ShoppingListUiResult.OnShowEmptyLists -> Unit // TODO - Not implemented yet
                is ShoppingListUiResult.OnShowLists -> Unit // TODO - Not implemented yet
            }
        }
    }

    ShoppingListsScreen(
        uiState = uiState,
        onUiEvent = viewModel::dispatch,
        modifier = modifier,
    )
}

@Composable
fun ShoppingListsScreen(
    uiState: ShoppingListsUiState,
    onUiEvent: (ShoppingListsUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchOnce {
        onUiEvent(ShoppingListsUiAction.FetchLists)
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.shoppingLists.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.shopping_lists_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            else -> {
                ShoppingListsContent(
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        if (!uiState.isLoading) {
            ExtendedFloatingActionButton(
                onClick = dropUnlessResumed {
                    onUiEvent(ShoppingListsUiAction.NewListClick)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(text = stringResource(id = R.string.shopping_lists_create))
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(16.dp),
            )
        }
    }
}

@Composable
private fun ShoppingListsContent(
    uiState: ShoppingListsUiState,
    onUiEvent: (ShoppingListsUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 12.dp,
            end = 16.dp,
            bottom = 96.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = uiState.shoppingLists,
            key = { shoppingList -> shoppingList.id },
            contentType = { "shopping_list" },
        ) { shoppingList ->
            AnimatedShoppingListItem(
                shoppingList = shoppingList,
                showDivider = shoppingList != uiState.shoppingLists.last(),
                onClick = {
                    onUiEvent(
                        ShoppingListsUiAction.ItemListClick(shoppingList.id)
                    )
                },
                onDelete = {
                    onUiEvent(
                        ShoppingListsUiAction.DeleteListClick(shoppingList.id)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(
                        placementSpec = tween(
                            durationMillis = DELETE_ITEM_ANIMATION_DURATION_MILLIS,
                        )
                    ),
            )
        }
    }
}

@Composable
private fun AnimatedShoppingListItem(
    shoppingList: ShoppingList,
    showDivider: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isVisible by remember(shoppingList.id) { mutableStateOf(true) }
    val currentOnDelete by rememberUpdatedState(onDelete)

    LaunchedEffect(isVisible) {
        if (!isVisible) {
            delay(DELETE_ITEM_ANIMATION_DURATION_MILLIS.toLong())
            currentOnDelete()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = DELETE_ITEM_ANIMATION_DURATION_MILLIS),
        ) + fadeOut(
            animationSpec = tween(durationMillis = DELETE_ITEM_ANIMATION_DURATION_MILLIS),
        ),
        modifier = modifier,
    ) {
        Column {
            SwipeToDeleteShoppingListItem(
                shoppingList = shoppingList,
                onClick = onClick,
                onDelete = {
                    isVisible = false
                },
                modifier = Modifier.fillMaxWidth(),
            )

            if (showDivider) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA),
                )
            }
        }
    }
}

@Composable
private fun SwipeToDeleteShoppingListItem(
    shoppingList: ShoppingList,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val deleteActionWidthPx = with(density) { deleteActionWidth.toPx() }
    var offsetX by remember(shoppingList.id) { mutableFloatStateOf(0f) }

    fun animateOffsetTo(targetOffset: Float) {
        coroutineScope.launch {
            animate(
                initialValue = offsetX,
                targetValue = targetOffset,
                animationSpec = tween(durationMillis = DELETE_REVEAL_ANIMATION_DURATION_MILLIS),
            ) { value, _ ->
                offsetX = value
            }
        }
    }

    val draggableState = rememberDraggableState { delta ->
        offsetX = (offsetX + delta).coerceIn(
            minimumValue = 0f,
            maximumValue = deleteActionWidthPx,
        )
    }

    Box(
        modifier = modifier.clipToBounds(),
    ) {
        DeleteListAction(
            onClick = onDelete,
            modifier = Modifier.matchParentSize(),
        )
        ShoppingListCard(
            shoppingList = shoppingList,
            onClick = {
                if (offsetX > 0f) {
                    animateOffsetTo(0f)
                } else {
                    onClick()
                }
            },
            onLongClick = {
                animateOffsetTo(deleteActionWidthPx)
            },
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = offsetX.roundToInt(),
                        y = 0,
                    )
                }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = { velocity ->
                        val shouldReveal = velocity > DELETE_REVEAL_FLING_VELOCITY ||
                            (
                                velocity > -DELETE_REVEAL_FLING_VELOCITY &&
                                    offsetX > deleteActionWidthPx * DELETE_REVEAL_THRESHOLD
                                )
                        animateOffsetTo(
                            if (shouldReveal) {
                                deleteActionWidthPx
                            } else {
                                0f
                            }
                        )
                    },
                ),
        )
    }
}

@Composable
private fun DeleteListAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(Red),
    ) {
        Column(
            modifier = Modifier
                .background(Red)
                .fillMaxHeight()
                .width(deleteActionWidth)
                .clickable(onClick = onClick),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = stringResource(id = R.string.shopping_lists_delete),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShoppingListCard(
    shoppingList: ShoppingList,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = remember {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    }
    val createdAtText = remember(shoppingList.createdAt, dateFormatter) {
        dateFormatter.format(Date(shoppingList.createdAt))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(end = 8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = shoppingList.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 16.dp, end = 18.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = createdAtText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 6.dp, end = 18.dp, bottom = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "navigation item"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShoppingListsScreenPreview() {
    ListoTheme {
        ShoppingListsScreen(
            uiState = ShoppingListsUiState(
                isLoading = false,
                shoppingLists = listOf(
                    ShoppingList(
                        id = 1L,
                        title = "Compras da semana",
                        createdAt = 1_784_324_400_000L,
                        products = listOf(),
                    ),
                    ShoppingList(
                        id = 2L,
                        title = "Churrasco de domingo",
                        createdAt = 1_784_238_000_000L,
                        products = listOf(),
                    ),
                    ShoppingList(
                        id = 3L,
                        title = "Churrasco de domingo",
                        createdAt = 1_784_238_000_000L,
                        products = listOf(),
                    ),
                ),
            ),
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyShoppingListsScreenPreview() {
    ListoTheme {
        ShoppingListsScreen(
            uiState = ShoppingListsUiState(isLoading = false),
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteAction() {
    ListoTheme {
        DeleteListAction({}, modifier = Modifier.height(64.dp))
    }
}

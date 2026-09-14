package com.valmiraguiar.listo.feature.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Deselect
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.theme.ListoTheme

@Composable
fun ListoTopBar(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    showAccountButton: Boolean = false,
    onAccountClick: () -> Unit = {},
    showResetButton: Boolean = false,
    onResetClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ).height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = showBackButton,
                enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
                exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back arrow",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }

            Icon(
                painter = painterResource(R.drawable.listo_icon),
                contentDescription = "",
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(id = R.string.splash_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = showResetButton,
                enter = expandHorizontally(expandFrom = Alignment.End) + fadeIn(),
                exit = shrinkHorizontally(shrinkTowards = Alignment.End) + fadeOut(),
                modifier = Modifier.fillMaxHeight(),
            ) {
                IconButton(onClick = onResetClick) {
                    Icon(
                        imageVector = Icons.Outlined.Deselect,
                        contentDescription = stringResource(id = R.string.shopping_list_details_reset),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }

            AnimatedVisibility(
                visible = showAccountButton,
                enter = expandHorizontally(expandFrom = Alignment.End) + fadeIn(),
                exit = shrinkHorizontally(shrinkTowards = Alignment.End) + fadeOut(),
                modifier = Modifier.fillMaxHeight(),
            ) {
                IconButton(onClick = onAccountClick) {
                    Icon(
                        imageVector = Icons.Outlined.AccountBox,
                        contentDescription = stringResource(id = R.string.action_login),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Light Theme",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ListoTopBarPreview() {
    ListoTheme {
        ListoTopBar(
            showBackButton = true,
            onBackClick = {},
            showAccountButton = true,
            onAccountClick = {},
        )
    }
}

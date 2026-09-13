package com.song.demos.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.song.demos.presentation.R

@Composable
fun DiscardChangesDialog(
    onKeepEditing: () -> Unit,
    onDiscard: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onKeepEditing,
        icon = {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.WarningAmber,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.65f)
            )
        },
        title = {
            Text(text = stringResource(R.string.discard_changes_title))
        },
        text = {
            Text(text = stringResource(R.string.discard_changes_message))
        },
        confirmButton = {
            TextButton(onClick = onDiscard) {
                Text(
                    text = stringResource(R.string.discard),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onKeepEditing) {
                Text(
                    text = stringResource(R.string.keep_editing),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

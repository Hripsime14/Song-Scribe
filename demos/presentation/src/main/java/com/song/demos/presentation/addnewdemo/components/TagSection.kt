package com.song.demos.presentation.addnewdemo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribePositiveButton
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R
import com.song.demos.presentation.demos.mapper.toTagModels
import com.song.demos.presentation.demos.model.TagModel

@Composable
fun TagSection(
    modifier: Modifier = Modifier,
    tags: List<TagModel>,
    onTagClick: (TagModel) -> Unit = {},
    onAddCustomTag: () -> Unit = {},
    onCustomTagClick: () -> Unit = {},
    newTagState: TextFieldState,
    addNewTag: Boolean = false
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .addDefaultTopPadding()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = "Search",
                modifier = Modifier
                    .padding(start = 16.dp,top = 16.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp, end = 16.dp, top = 16.dp)
                    .weight(1f),
                text = stringResource(R.string.tags),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            SongScribePositiveButton(
                modifier = Modifier
                    .padding(end = 16.dp, top = 16.dp),
                text = stringResource(R.string.custom),
                onClick = onCustomTagClick,
                icon = Icons.Default.Add,
                hasBorder = true,
                textColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.Transparent,
            )
        }
        if (addNewTag) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth()
                        .weight(1f),
                    state = newTagState,
                    shape = RoundedCornerShape(8.dp),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.add_tag),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary
                    )
                )
                SongScribePositiveButton(
                    icon = Icons.Default.Add,
                    text = stringResource(R.string.add),
                    containerColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = onAddCustomTag,
                    cornerShape = 8.dp
                )
            }
        }
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tags.forEach { tag ->
                val isSelected = tag.isSelected
                AssistChip(
                    onClick = { onTagClick(tag) },
                    label = { Text(text = tag.name, style = MaterialTheme.typography.bodyMedium) },
                    shape = RoundedCornerShape(32.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Transparent
                        },
                        labelColor = if (isSelected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    ),
                    border = AssistChipDefaults.assistChipBorder(
                        enabled = true,
                        borderColor = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                        borderWidth = 0.5.dp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TagSectionPreview() {
    SongScribeTheme {
        var selectedTags by remember { mutableStateOf(setOf("piano", "lo-fi")) }
        TagSection(
            modifier = Modifier,
            tags = stringArrayResource(R.array.instruments).toList().toTagModels(),
            onTagClick = { tag ->
            },
            addNewTag = true,
            newTagState = rememberTextFieldState(),
        )
    }
}
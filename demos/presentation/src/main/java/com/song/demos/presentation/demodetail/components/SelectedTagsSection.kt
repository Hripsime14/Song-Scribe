package com.song.demos.presentation.demodetail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribeBeige
import com.song.core.presentation.designsystem.components.SongScribePositiveButton
import com.song.core.presentation.designsystem.extension.addDefaultStartPadding
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.components.TagSection
import com.song.demos.presentation.demos.model.TagModel

@Composable
fun SelectedTagsSection(
    modifier: Modifier = Modifier,
    selectedTags: List<TagModel> = emptyList(),
    tagOptions: List<TagModel> = emptyList(),
    showTagSection: Boolean = false,
    showAddTagSection: Boolean = false,
    newTagState: TextFieldState = TextFieldState(),
    onEditClick: () -> Unit = {},
    onTagClick: (TagModel) -> Unit = {},
    onCustomTagClick: () -> Unit = {},
    onAddCustomTag: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .addDefaultTopPadding()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                text = stringResource(R.string.tags),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 16.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                selectedTags.forEach {
                    SongScribeBeige(
                        text = it.name,
                        modifier = Modifier
                            .padding(top = 8.dp),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            AnimatedContent (
                targetState = showTagSection,
            ) {
                SongScribePositiveButton(
                    modifier = Modifier
                        .padding(16.dp),
                    icon = if (it) Icons.Default.Close else Icons.Default.Edit,
                    text = "",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.primary,
                    hasBorder = false,
                    cornerShape = 32.dp,
                    onClick = onEditClick
                )
            }
        }
        AnimatedVisibility(
            visible = showTagSection
        ) {
            Box(
                modifier = Modifier
                    .height(0.5.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
            TagSection(
                tags = tagOptions,
                onTagClick = onTagClick,
                onAddCustomTag = onAddCustomTag,
                onCustomTagClick = onCustomTagClick,
                newTagState = newTagState,
                addNewTag = showAddTagSection,
                showTags = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectedTagsSectionPreview() {
    SongScribeTheme {
        SelectedTagsSection(
            selectedTags = listOf(TagModel(name = "piano", isSelected = true)),
            tagOptions = listOf(TagModel(name = "piano", isSelected = true), TagModel(name = "guitar", isSelected = false)),
            showTagSection = true,
            newTagState = rememberTextFieldState()
        )
    }
}

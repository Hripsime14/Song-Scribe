package com.song.demos.presentation.addnewdemo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribeColorLabel
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.LabelLavender
import com.song.core.presentation.designsystem.theme.LabelLightGreen
import com.song.core.presentation.designsystem.theme.LabelPink
import com.song.core.presentation.designsystem.theme.LabelRose
import com.song.core.presentation.designsystem.theme.LabelSageGreen
import com.song.core.presentation.designsystem.theme.LabelSkyBlue
import com.song.core.presentation.designsystem.theme.LabelYellow
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R
import com.song.demos.presentation.demos.model.ColorModel

@Composable
fun ColorLabelSection(
    modifier: Modifier = Modifier,
    colors: List<ColorModel>,
    onColorSelect: (ColorModel) -> Unit = {}
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .addDefaultTopPadding()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = stringResource(R.string.color_label),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            colors.forEach { color ->
                key(color) {
                    SongScribeColorLabel(
                        modifier = Modifier.clickable { onColorSelect(color) },
                        color = color.color,
                        isSelected = color.isSelected,
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorLabelSectionPreview() {
    SongScribeTheme {
        ColorLabelSection(
            modifier = Modifier,
            colors = listOf(
                ColorModel(LabelLavender),
                ColorModel(LabelRose),
                ColorModel(LabelSageGreen),
                ColorModel(LabelYellow),
                ColorModel(LabelSkyBlue),
                ColorModel(LabelPink),
                ColorModel(LabelLightGreen)
            )
        )
    }
}
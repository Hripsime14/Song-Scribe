package com.song.demos.presentation.addnewdemo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.domain.validation.DemoValidationRules
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R

@Composable
fun DemoTitleSection(
    modifier: Modifier = Modifier,
    titleState: TextFieldState
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .addDefaultTopPadding()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = stringResource(R.string.demo_title),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            state = titleState,
            inputTransformation = InputTransformation.maxLength(DemoValidationRules.TITLE_MAX_LENGTH),
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            placeholder = {
                Text(
                    text = stringResource(R.string.give_your_demo_name),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            },
            supportingText = {
                val length = titleState.text.length
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                    ,
                    textAlign = TextAlign.End,
                    text = "$length/${DemoValidationRules.TITLE_MAX_LENGTH}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (length >= DemoValidationRules.TITLE_MAX_LENGTH) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
private fun DemoTitleSectionPreview() {
    SongScribeTheme {
        DemoTitleSection(
            modifier = Modifier,
            titleState = TextFieldState()
        )
    }
}

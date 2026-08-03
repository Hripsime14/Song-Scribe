package com.song.demos.presentation.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.song.demos.presentation.R
import com.song.demos.presentation.demos.components.DemoSearchBar

@Composable
fun DemosScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateToDetail: () -> Unit
) {
    DemosScreen()
}

@Composable
fun DemosScreen(
    modifier: Modifier = Modifier,
) {
    Box (
        modifier = modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.my_demos),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.my_demos)
                )
            }
            DemoSearchBar(
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.recordings, 2),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoScreenPreview() {
    DemosScreen(
        modifier = Modifier.fillMaxSize()
    )
}
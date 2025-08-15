package com.example.securenotes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.securenotes.ui.viewmodels.NoteDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import androidx.compose.foundation.layout.width


@Composable
fun NoteDetailScreen(
    noteId: Long?,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val vm: NoteDetailViewModel = viewModel(
        factory = NoteDetailViewModel.Factory(context, noteId)
    )
    val state by vm.state.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = state.title,
            onValueChange = vm::updateTitle,
            label = { Text("Title") },
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxSize(fraction = 0.0f)
        )
        OutlinedTextField(
            value = state.body,
            onValueChange = vm::updateBody,
            label = { Text("Body") },
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp)
        )
        Row {
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    vm.save()
                    onNavigateBack()
                }
            }) {
                Text("Save")
            }
            Spacer(Modifier.width(16.dp))
            TextButton(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    vm.delete()
                    onNavigateBack()
                }
            }) {
                Text("Delete")
            }
        }
    }
}

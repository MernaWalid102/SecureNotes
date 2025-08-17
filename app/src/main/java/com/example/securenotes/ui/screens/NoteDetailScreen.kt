package com.example.securenotes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.securenotes.ui.viewmodels.NoteDetailViewModel
import kotlinx.coroutines.launch

@Composable
fun NoteDetailScreen(
    noteId: Long?,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val vm: NoteDetailViewModel =
        viewModel(factory = NoteDetailViewModel.Factory(context, noteId))
    val state by vm.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    // ✅ Collect one-shot events and navigate/show messages here
    LaunchedEffect(Unit) {
        vm.events.collect { e ->
            when (e) {
                is NoteDetailViewModel.DetailEvent.Saved -> onNavigateBack()
                is NoteDetailViewModel.DetailEvent.Deleted -> onNavigateBack()
                is NoteDetailViewModel.DetailEvent.Error ->
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

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
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = state.body,
            onValueChange = vm::updateBody,
            label = { Text("Body") },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row {
            Button(onClick = { scope.launch { vm.save() } }) {
                Text("Save")
            }
            Spacer(Modifier.width(16.dp))
            TextButton(onClick = { scope.launch { vm.delete() } }) {
                Text("Delete")
            }
        }
    }
}

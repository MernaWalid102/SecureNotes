package com.example.securenotes.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.securenotes.data.NoteRepository
import com.example.securenotes.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteDetailState(
    val id: Long? = null,
    val title: String = "",
    val body: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isNew: Boolean = true
)

class NoteDetailViewModel(
    private val repository: NoteRepository,
    noteId: Long?
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailState(isNew = noteId == null))
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()

    init {
        if (noteId != null) {
            viewModelScope.launch {
                repository.observeNote(noteId).filterNotNull().collect { n ->
                    _state.value = NoteDetailState(
                        id = n.id,
                        title = n.title,
                        body = n.body,
                        timestamp = n.timestamp,
                        isNew = false
                    )
                }
            }
        }
    }

    fun updateTitle(v: String) = _state.update { it.copy(title = v) }
    fun updateBody(v: String) = _state.update { it.copy(body = v) }

    suspend fun save(): Long {
        val s = _state.value
        val note = Note(
            id = s.id ?: 0L,
            title = s.title,
            body = s.body,
            timestamp = System.currentTimeMillis()
        )
        val id = repository.upsert(note)
        _state.update { it.copy(id = id, isNew = false) }
        return id
    }

    suspend fun delete() {
        val id = _state.value.id ?: return
        repository.deleteById(id)
    }

    class Factory(private val context: Context, private val noteId: Long?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = NoteRepository(context.applicationContext)
            @Suppress("UNCHECKED_CAST")
            return NoteDetailViewModel(repo, noteId) as T
        }
    }
}

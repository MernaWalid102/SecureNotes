package com.example.securenotes.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.securenotes.data.NoteRepositoryImpl
import com.example.securenotes.domain.Note
import com.example.securenotes.domain.NoteRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class NoteDetailState(
    val id: Long? = null,
    val title: String = "",
    val body: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isPrivate: Boolean = false,
    val isNew: Boolean = true,
    val isDirty: Boolean = false,
    val isSaving: Boolean = false
)

class NoteDetailViewModel(
    private val repository: NoteRepository,
    noteId: Long?
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailState(isNew = noteId == null))
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()


    private val _events = MutableSharedFlow<DetailEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()


    private val saveMutex = Mutex()

    init {
        if (noteId != null) {
            viewModelScope.launch {
                repository.observeNote(noteId)
                    .filterNotNull()
                    .collectLatest { n ->
                        // Only replace fields that come from DB; keep isDirty false on load
                        _state.update {
                            it.copy(
                                id = n.id,
                                title = n.title,
                                body = n.body,
                                timestamp = n.timestamp,
                                isPrivate = n.isPrivate,
                                isNew = false,
                                isDirty = false
                            )
                        }
                    }
            }
        }
    }

    fun updateTitle(v: String) = _state.update { it.copy(title = v, isDirty = true) }
    fun updateBody(v: String) = _state.update { it.copy(body = v, isDirty = true) }
    fun updateIsPrivate(v: Boolean) = _state.update { it.copy(isPrivate = v, isDirty = true) }

    /** Save regardless of dirty state (used by explicit Save button). */
    suspend fun save(): Long = doSave(force = true)

    /** Save only if there are unsaved edits (useful for auto-save/back handler). */
    suspend fun saveIfDirty(): Long? {
        return if (_state.value.isDirty) doSave(force = false) else _state.value.id
    }

    private suspend fun doSave(force: Boolean): Long = saveMutex.withLock {
        val s = _state.value
        if (!force && !s.isDirty) return s.id ?: 0L

        try {
            _state.update { it.copy(isSaving = true) }
            val note = Note(
                id = s.id ?: 0L,
                title = s.title,
                body = s.body,
                timestamp = System.currentTimeMillis(),
                isPrivate = s.isPrivate
            )
            val id = repository.upsert(note)
            _state.update { it.copy(id = id, isNew = false, isDirty = false, isSaving = false) }
            _events.tryEmit(DetailEvent.Saved(id))
            return id
        } catch (t: Throwable) {
            _state.update { it.copy(isSaving = false) }
            _events.tryEmit(DetailEvent.Error(t.message ?: "Failed to save"))
            throw t
        }
    }

    suspend fun delete() {
        val id = _state.value.id ?: return
        try {
            repository.deleteById(id)
            _events.tryEmit(DetailEvent.Deleted)
        } catch (t: Throwable) {
            _events.tryEmit(DetailEvent.Error(t.message ?: "Failed to delete"))
            throw t
        }
    }

    sealed interface DetailEvent {
        data class Saved(val id: Long) : DetailEvent
        data object Deleted : DetailEvent
        data class Error(val message: String) : DetailEvent
    }

    class Factory(
        private val context: Context,
        private val noteId: Long?
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo: NoteRepository = NoteRepositoryImpl(context.applicationContext)
            @Suppress("UNCHECKED_CAST")
            return NoteDetailViewModel(repo, noteId) as T
        }
    }
}

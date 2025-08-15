package com.example.securenotes.data

import android.content.Context
import com.example.securenotes.data.db.AppDatabase
import com.example.securenotes.domain.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(context: Context) {

    private val dao = AppDatabase.getInstance(context).noteDao()

    fun getNotes(): Flow<List<Note>> = dao.getAll()

    fun observeNote(id: Long): Flow<Note?> = dao.observeById(id)

    suspend fun upsert(note: Note): Long {
        return if (note.id == 0L) dao.insert(note) else {
            dao.update(note)
            note.id
        }
    }

    suspend fun delete(note: Note) = dao.delete(note)

    suspend fun deleteById(id: Long) = dao.deleteById(id)
}

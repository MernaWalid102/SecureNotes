package com.example.securenotes.data

import android.content.Context
import com.example.securenotes.data.db.AppDatabase
import com.example.securenotes.domain.Note
import com.example.securenotes.domain.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(context: Context) : NoteRepository {

    private val dao = AppDatabase.getInstance(context).noteDao()

    override fun getNotes(): Flow<List<Note>> = dao.getAll()

    override fun observeNote(id: Long): Flow<Note?> = dao.observeById(id)

    override suspend fun upsert(note: Note): Long {
        return if (note.id == 0L) dao.insert(note) else {
            dao.update(note)
            note.id
        }
    }

    override suspend fun deleteById(id: Long) = dao.deleteById(id)
}

package com.example.securenotes.domain

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    fun observeNote(id: Long): Flow<Note?>
    suspend fun upsert(note: Note): Long
    suspend fun deleteById(id: Long)
}
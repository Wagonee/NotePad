package com.example.notepad.domain

import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun addNote(title: String, content: String, isPinned: Boolean, updatedAt: Long)
    suspend fun deleteNote(id: Int)
    suspend fun editNote(note: Note)
    fun getAllNotes(): Flow<List<Note>>
    fun searchNotes(query: String): Flow<List<Note>>
    suspend fun getNote(id: Int): Note
    suspend fun switchPinnedStatus(id: Int)
}
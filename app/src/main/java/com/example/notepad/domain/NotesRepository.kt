package com.example.notepad.domain

import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun addNote(note: Note)
    fun deleteNote(id: Int)
    fun editNote(note: Note)
    fun getAllNotes(): Flow<List<Note>>
    fun searchNotes(query: String): Flow<List<Note>>
    fun getNote(id: Int): Note
    fun switchPinnedStatus(id: Int)
}
package com.example.notepad.domain

import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(): Flow<List<Note>> {
       return repository.getAllNotes()
    }
}
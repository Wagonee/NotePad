package com.example.notepad.domain

class EditNoteUseCase(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.editNote(note.copy(updatedAt = System.currentTimeMillis()))
    }
}
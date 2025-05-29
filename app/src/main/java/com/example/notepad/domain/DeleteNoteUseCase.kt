package com.example.notepad.domain

class DeleteNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(id: Int) {
        repository.deleteNote(id)
    }
}
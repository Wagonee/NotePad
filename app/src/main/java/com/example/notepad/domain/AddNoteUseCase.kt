package com.example.notepad.domain

import androidx.compose.ui.tooling.animation.ToolingState

class AddNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(note: Note) {
        repository.addNote(note)
    }
}
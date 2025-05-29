package com.example.notepad.domain

import android.icu.text.CaseMap

class AddNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(title: String, content: String) {
        repository.addNote(title, content)
    }
}
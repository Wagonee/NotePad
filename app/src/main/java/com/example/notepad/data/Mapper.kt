package com.example.notepad.data

import com.example.notepad.domain.Note

fun Note.toDbModel() : NoteDbModel {
    return NoteDbModel(id, content, title, updatedAt = updatedAt, isPinned = isPinned)
}

fun NoteDbModel.toEntity() : Note {
    return Note(id, content, title, updatedAt = updatedAt, isPinned = isPinned)
}
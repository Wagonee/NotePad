package com.example.notepad.presentation.screens.notes

import androidx.lifecycle.ViewModel
import com.example.notepad.data.TestNotesRepositoryImpl
import com.example.notepad.domain.AddNoteUseCase
import com.example.notepad.domain.DeleteNoteUseCase
import com.example.notepad.domain.EditNoteUseCase
import com.example.notepad.domain.GetAllNotesUseCase
import com.example.notepad.domain.GetNoteUseCase
import com.example.notepad.domain.Note
import com.example.notepad.domain.SearchNotesUseCase
import com.example.notepad.domain.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel : ViewModel() {
    private val repository = TestNotesRepositoryImpl
    private val addNoteUseCase = AddNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val getAllNoteUseCase = GetAllNotesUseCase(repository)
    private val searchNotesUseCase = SearchNotesUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)


    private val query = MutableStateFlow("")

    private val _state = MutableStateFlow(NotesScreenState())

    val state = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        addSomeNotes()
        query
            .onEach { input ->
                _state.update { it.copy(query = input) }
            }
            .flatMapLatest { input ->
                if (input.isBlank()) {
                    getAllNoteUseCase()
                } else {
                    searchNotesUseCase(input)
                }
            }
            .onEach { notes ->
                val pinnedNotes = notes.filter { it.isPinned }
                val otherNotes = notes.filter { !it.isPinned }
                _state.update { it.copy(pinnedNotes = pinnedNotes, otherNotes = otherNotes) }
            }
            .launchIn(scope)
    }

    // TODO: test method.
    private fun addSomeNotes() {
        repeat(50000) {
            addNoteUseCase(title = "Title - $it", "Content - $it")
        }
    }



    fun processCommand(command: NotesCommand) {
        when (command) {
            is NotesCommand.DeleteNote -> {
                deleteNoteUseCase(command.id)
            }

            is NotesCommand.EditNote -> {

                val note = getNoteUseCase(id = command.note.id)
                val title = command.note.title
                editNoteUseCase(command.note.copy(title = title + "_edited"))

            }

            is NotesCommand.InputSearchQuery -> {
                query.update { command.query.trim() }
            }

            is NotesCommand.SwitchPinnedStatus -> {
                switchPinnedStatusUseCase(command.id)
            }
        }
    }

}

sealed interface NotesCommand {
    data class InputSearchQuery(val query: String) : NotesCommand
    data class SwitchPinnedStatus(val id: Int) : NotesCommand

    // Temp
    data class DeleteNote(val id: Int) : NotesCommand
    data class EditNote(val note: Note) : NotesCommand
}

data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf()
)
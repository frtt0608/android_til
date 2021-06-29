package com.heon9u.aacproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface NoteDao {

    @Insert
    Completable insert(Note note);

    @Update
    Completable update(Note note);

    @Delete
    Completable delete(Note note);

    @Query("DELETE FROM note_table")
    Completable deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();
}

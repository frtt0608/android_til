package com.heon9u.aacproject;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) { insertNoteRxJava(note); }

    public void update(Note note) {
        updateNoteRxJava(note);
    }

    public void delete(Note note) {
        deleteNoteRxJava(note);
    }

    public void deleteAllNotes() { deleteAllNotesRxJava(); }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    private void insertNoteRxJava(Note note) {
        noteDao.insert(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void updateNoteRxJava(Note note) {
        noteDao.update(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void deleteNoteRxJava(Note note) {
        noteDao.delete(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void deleteAllNotesRxJava() {
        noteDao.deleteAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}

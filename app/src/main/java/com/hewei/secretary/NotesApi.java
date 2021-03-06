package com.hewei.secretary;

import com.hewei.secretary.domain.NoteIdRequest;
import com.hewei.secretary.note.Note;
import com.hewei.secretary.note.NoteTemplate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by fengyinpeng on 2018/1/4.
 */

public interface NotesApi {
    @GET("notes/listNotes")
    Call<List<Note>> listNotes(@Query("key") String searchKey);

    @POST("notes/addNote")
    Call<NoteIdRequest> addNote(@Body Note note);

    @POST("notes/addData")
    Call<String> addData(@Body NoteTemplate template);

    @GET("notes/getNote")
    Call<Note> getNote(@Query("id") String id);
}

package com.example.afmobile.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class SerieId {

    @Exclude
    public String SerieId;

    public  <T extends  SerieId> T withId(@NonNull final String id){
        this.SerieId = id;
        return  (T) this;
    }
}

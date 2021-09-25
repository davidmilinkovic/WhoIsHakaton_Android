package com.fortablydumb.whoishakatonandroid;

import android.app.Application;

import androidx.room.Room;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Retrofit;


public class AppModule {

    private static AppModule am;
    private final Application application;

    private ExecutorService executor;
    private AppDatabase db;
    private DomenRepo domenRepo;

    public static AppModule getInstance(Application application) {
        if(am == null) {
            am = new AppModule(application);
        }
        return am;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public AppDatabase getDb() {
        return db;
    }

    public DomenRepo getDomenRepo() {
        return domenRepo;
    }

    public Application getApplication() {
        return application;
    }

    private AppModule(Application application) {
        this.application = application;
        db = Room.databaseBuilder(application,
                AppDatabase.class, "AppDatabase.db").allowMainThreadQueries()
                .build();
        executor = Executors.newSingleThreadExecutor();
        domenRepo = new DomenRepo(db.domenDao(), executor);
    }
}

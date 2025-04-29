package com.jabiseo.infra.fcm;

import com.google.firebase.FirebaseApp;
import com.google.firebase.ThreadManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class CustomThreadManger extends ThreadManager {

    private final ExecutorService executor = Executors.newFixedThreadPool(70);

    @Override
    protected ExecutorService getExecutor(FirebaseApp firebaseApp) {
        return executor;
    }

    @Override
    protected void releaseExecutor(FirebaseApp firebaseApp, ExecutorService executorService) {
        executor.shutdown(); // 종료 시 정리
    }

    @Override
    protected ThreadFactory getThreadFactory() {
        return Executors.defaultThreadFactory();
    }
}

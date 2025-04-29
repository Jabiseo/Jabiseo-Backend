package com.jabiseo.infra.utils;

import com.google.api.core.ApiFuture;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ApiFutureConverter {

    public static <T> CompletableFuture<T> toCompletableFuture(ApiFuture<T> apiFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        apiFuture.addListener(() -> {
            try {
                // ApiFuture의 결과를 get()으로 가져와 CompletableFuture에 전달
                T result = apiFuture.get();  // blocking call
                completableFuture.complete(result);
            } catch (InterruptedException | ExecutionException e) {
                // 예외 발생 시 CompletableFuture에 예외 전달
                completableFuture.completeExceptionally(e);
            }
        }, Runnable::run);  // Runnable::run을 사용해 현재 스레드에서 실행

        return completableFuture;
    }
}

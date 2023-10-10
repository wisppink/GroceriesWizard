package com.example.grocerieswizard.data.repo;

public interface RepositoryCallback<T> {
    void onSuccess(T data);

    void onError(Exception e);
}
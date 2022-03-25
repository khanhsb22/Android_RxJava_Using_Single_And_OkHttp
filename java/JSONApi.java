package com.example.demoapp;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JSONApi {
    @GET("posts")
    Single<List<Post>> getAllPosts();

    @PUT("posts/{id}")
    Single<Post> updatePost(@Path("id") int id, @Body Post post);

    @DELETE("posts/{id}")
    Single<Post> deletePost(@Path("id") int id);

    @POST("posts")
    Single<Post> addPost(@Body Post post);
}

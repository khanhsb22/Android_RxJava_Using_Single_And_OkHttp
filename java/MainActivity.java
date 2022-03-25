package com.example.demoapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MyCustomDialog.OnInputListener {
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable disposable;
    private MutableLiveData<List<Post>> postMulLiveData = new MutableLiveData<>();
    private MutableLiveData<Post> postMul = new MutableLiveData<>();
    private ArrayList<Post> list = new ArrayList<>();
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getAllPosts();
        getAllPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                if (!posts.isEmpty()) {
                    for (Post item : posts) {
                        // Chỉ lấy 25 item
                        if (item.getId() <= 25) {
                            list.add(new Post(item.getUserId(), item.getId(), item.getTitle(), item.getBody()));
                        }
                    }
                    buildRecyclerView(list);
                }
            }
        });

        binding.fabAddPost.setOnClickListener(view -> {
            DialogAddPost dialogAddPost = new DialogAddPost();
            dialogAddPost.showDialog(this);
            dialogAddPost.setDialogInterface(new DialogAddPost.DialogInterface() {
                @Override
                public void sendDataToAddPost(String title, String body, int userId) {
                    addPost(new Post(userId, title, body)).observe(MainActivity.this, new Observer<Post>() {
                        @Override
                        public void onChanged(Post post) {
                            if (post != null) {
                                Toast.makeText(MainActivity.this, "Add post success with id: " + post.getId(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onChanged: add post with info " + "{ id: " + post.getId() + ", title: "
                                        + post.getTitle() + ", body: " + post.getBody() + ", userId: " + post.getUserId() + " }");
                            }
                        }
                    });
                }
            });
        });

    }

    // Sử dụng MutableLiveData để lấy được list response ra ngoài hàm onSuccess()
    private MutableLiveData<List<Post>> getAllPosts() {
        JSONApi jsonApi = Builder.builder();
        jsonApi.getAllPosts().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Post>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<Post> posts) {
                        postMulLiveData.setValue(posts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
        return postMulLiveData;
    }

    private MutableLiveData<Post> updatePost(int id, String title, int userId, String body) {
        JSONApi jsonApi = Builder.builder();
        jsonApi.updatePost(id, new Post(userId, title, body)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Post post) {
                        postMul.setValue(post);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "updatePost: error " + e.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
        return postMul;
    }

    private MutableLiveData<Post> deletePost(int id) {
        JSONApi jsonApi = Builder.builder();
        jsonApi.deletePost(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Post post) {
                        postMul.setValue(post);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "updatePost: error " + e.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
        return postMul;
    }

    private MutableLiveData<Post> addPost(Post post) {
        JSONApi jsonApi = Builder.builder();
        jsonApi.addPost(post).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Post post) {
                        postMul.setValue(post);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "addPost: error " + e.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
        return postMul;
    }

    private void buildRecyclerView(ArrayList<Post> arrayList) {
        postAdapter = new PostAdapter(arrayList, MainActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();
        setOnClickAdapter();
    }

    private void setOnClickAdapter() {
        postAdapter.setOnItemClickListener(new PostAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Post item = list.get(position);
                MyCustomDialog dialog = new MyCustomDialog(item.getId(), item.getUserId(), item.getTitle(), item.getBody());
                dialog.show(getSupportFragmentManager(), "MyCustomDialog");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void sendInput(int id, String title, String body, int userId) {
        // Retrieve post when update success
        updatePost(id, title, userId, body).observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                if (post.getId() != -1) {
                    Toast.makeText(MainActivity.this, "Update post success with id: " + post.getId(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onChanged: post " + "{ id: " + post.getId() + ", title: "
                            + post.getTitle() + ", body: " + post.getBody() + ", userId: " + post.getUserId() + " }");
                }
            }
        });
    }

    @Override
    public void sendId(int id) {
        deletePost(id).observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                if (post.getId() != -1) {
                    Toast.makeText(MainActivity.this, "Deleted post success with id: " + post.getId(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onChanged: deleted post with info " + "{ id: " + post.getId() + ", title: "
                            + post.getTitle() + ", body: " + post.getBody() + ", userId: " + post.getUserId() + " }");
                }
            }
        });
    }
}
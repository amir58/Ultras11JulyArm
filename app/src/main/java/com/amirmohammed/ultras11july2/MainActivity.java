package com.amirmohammed.ultras11july2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.amirmohammed.ultras11july2.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// enable dataBinding in gradle > synNow
// convert layout to dataBindingLayout > alt + enter in the first line
// using binding in java code > LayoutNameBinding
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    //    RecyclerView recyclerView;
    List<Task> activeTasks = new ArrayList<>();
    List<Task> doneTasks = new ArrayList<>();
    List<Task> archiveTasks = new ArrayList<>();
    TasksAdapter tasksAdapter;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                if (item.getItemId() == R.id.item_tasks) {
                    getTasks("active", activeTasks);

                } else if (item.getItemId() == R.id.item_done) {
                    getTasks("done", doneTasks);

                } else if (item.getItemId() == R.id.item_archive) {
                    getTasks("archive", archiveTasks);
                }

                return false;
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Task task = tasksAdapter.taskArrayList.get(viewHolder.getAdapterPosition());
//                TasksDatabase.getInstance(MainActivity.this).tasksDao().deleteTask(task);
                tasksAdapter.taskArrayList.remove(viewHolder.getAdapterPosition());
                tasksAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                deleteTask(task);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvTasks);

        getTasks("active", activeTasks);
    }

    public void insertTask(View view) {
        new InsertTaskSheetFragment(iInsertTask).show(getSupportFragmentManager(), "insertSheet");
    }

    IInsertTask iInsertTask = new IInsertTask() {
        @Override
        public void onTaskInserted() {
//            tasks = TasksDatabase.getInstance(MainActivity.this).tasksDao().getActiveTasks();
            tasksAdapter = new TasksAdapter(activeTasks, iTaskUpdates);
            binding.rvTasks.setAdapter(tasksAdapter);

        }
    };

    @BindingAdapter("glide")
    public static void setImageUrl(ImageView imageView, String imageUrl) {
        Glide.with(imageView).load(imageUrl).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (item.getItemId() == R.id.item_profile) {
//            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            throw new RuntimeException("Test Crash By Amir"); // Force a crash
        }
        return super.onOptionsItemSelected(item);
    }

    ITaskUpdates iTaskUpdates = new ITaskUpdates() {
        @Override
        public void onDonePressed(Task task) {
            editTask(task, "done");
        }

        @Override
        public void onArchivePressed(Task task) {
            editTask(task, "archive");
        }
    };

    void getTasks(String status, List<Task> tasks) {
        Log.i(TAG, "getTasks: " + firebaseAuth.getCurrentUser().getUid());
        firestore.collection("julyUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("myTasks")
                .whereEqualTo("status", status)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot query) {
                        Log.i(TAG, "onSuccess: GET TASKS");

                        Log.i(TAG, "onSuccess: documents " + query.getDocuments().size());

                        tasks.clear();
                        for (DocumentSnapshot snapshot : query.getDocuments()) {
                            Task task = snapshot.toObject(Task.class);
                            tasks.add(task);
                        }

                        Log.i(TAG, "onSuccess: " + tasks.size());
                        tasksAdapter = new TasksAdapter(tasks, iTaskUpdates);
                        binding.rvTasks.setAdapter(tasksAdapter);

                    }
                });
    }


    void deleteTask(Task task) {
        firestore.collection("julyUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("myTasks")
                .document(task.getId().toString())
                .delete();
    }

    void editTask(Task task, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);

        firestore.collection("julyUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("myTasks")
                .document(task.getId().toString())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "onSuccess: UPDATED");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });
    }
}
package com.amirmohammed.ultras11july2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.amirmohammed.ultras11july2.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;
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
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    //    RecyclerView recyclerView;
    List<Task> tasks = new ArrayList<>();
    TasksAdapter tasksAdapter;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        tasksAdapter = new TasksAdapter(tasks);
        binding.rvTasks.setAdapter(tasksAdapter);


        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                if (item.getItemId() == R.id.item_tasks) {

                } else if (item.getItemId() == R.id.item_done) {

                } else if (item.getItemId() == R.id.item_archive) {

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
                Task task = tasks.get(viewHolder.getAdapterPosition());
//                TasksDatabase.getInstance(MainActivity.this).tasksDao().deleteTask(task);
                tasks.remove(viewHolder.getAdapterPosition());
                tasksAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvTasks);
    }

    public void insertTask(View view) {
        new InsertTaskSheetFragment(iInsertTask).show(getSupportFragmentManager(), "insertSheet");
    }

    IInsertTask iInsertTask = new IInsertTask() {
        @Override
        public void onTaskInserted() {
//            tasks = TasksDatabase.getInstance(MainActivity.this).tasksDao().getActiveTasks();
            tasksAdapter = new TasksAdapter(tasks);
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
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    void getTasks() {
        firestore.collection("julyUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("myTasks")
//                .whereEqualTo("status", "done")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Task> tasks = new ArrayList<>();

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Task task = snapshot.toObject(Task.class);
                            tasks.add(task);
                        }


                    }
                });
    }

    void deleteTask(Task task){
        firestore.collection("julyUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("myTasks")
                .document(task.getId())
                .delete();
    }

    void editTask(Task task , String status){
        Map<String , Object> map = new HashMap<>();
        map.put("status", status);

        firestore.collection("julyUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("myTasks")
                .document(task.getId())
                .update(map);
    }
}
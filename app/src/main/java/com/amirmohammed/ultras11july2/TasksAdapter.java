package com.amirmohammed.ultras11july2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amirmohammed.ultras11july2.databinding.ItemTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskHolder> {

    List<Task> taskArrayList = new ArrayList<>();

    public TasksAdapter(List<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_task, parent, false);
        return new TaskHolder(binding);
//        return new TaskHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = taskArrayList.get(position);
        holder.binding.setTask(task);

//        holder.binding.taskTitle.setText(task.getTitle());
//        holder.binding.taskDate.setText("Date : " + task.getDate());
//        holder.binding.taskTitle.setText("Time : " + task.getTime());

        holder.binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setStatusDone();
//                TasksDatabase.getInstance(v.getContext()).tasksDao().updateTask(task);
                taskArrayList.remove(position);
                notifyItemRemoved(position);
            }
        });

        holder.binding.archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setStatusArchive();
//                TasksDatabase.getInstance(v.getContext()).tasksDao().updateTask(task);
                taskArrayList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }


    class TaskHolder extends RecyclerView.ViewHolder {
        ItemTaskBinding binding;
//        ImageView imageViewDone, imageViewArchive;
//        TextView textViewTitle, textViewDate, textViewTime;

        public TaskHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//            imageViewDone = itemView.findViewById(R.id.done);
//            imageViewArchive = itemView.findViewById(R.id.archive);
//            textViewTitle = itemView.findViewById(R.id.task_title);
//            textViewDate = itemView.findViewById(R.id.task_date);
//            textViewTime = itemView.findViewById(R.id.task_time);
        }

    }
}

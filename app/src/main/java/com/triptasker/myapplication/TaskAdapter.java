package com.triptasker.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void updateTasks(List<Task> tasks) {
        taskList.clear();
        for (Task task : tasks) {
            if (task != null && task.getTitle() != null) {
                taskList.add(task);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.tvTaskName.setText(task.getTitle() != null ? task.getTitle() : "Nome não disponível");
        holder.tvTaskDescription.setText(task.getDescription() != null ? task.getDescription() : "Sem descrição");
        holder.tvTaskDate.setText(task.getDueDate() != null ? task.getDueDate() : "Sem data");

        // Mapeando status
        String statusText;
        switch (task.getStatus()) {
            case 0:
                statusText = "A Fazer";
                holder.tvTaskStatus.setTextColor(Color.RED);
                break;
            case 1:
                statusText = "Fazendo";
                holder.tvTaskStatus.setTextColor(Color.YELLOW);
                break;
            case 2:
                statusText = "Feito";
                holder.tvTaskStatus.setTextColor(Color.GREEN);
                break;
            default:
                statusText = "Sem status";
                holder.tvTaskStatus.setTextColor(Color.BLACK);
                break;
        }
        holder.tvTaskStatus.setText(statusText);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskDate, tvTaskName, tvTaskDescription, tvTaskStatus;
        ImageView imgEdit, imgDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskDate = itemView.findViewById(R.id.tvTaskDate);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
            tvTaskStatus = itemView.findViewById(R.id.tvTaskStatus);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}

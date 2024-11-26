package com.triptasker.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void updateTasks(List<Task> tasks) {
        synchronized (taskList) {
            taskList.clear();
            if (tasks != null) {
                for (Task task : tasks) {
                    if (task != null && task.getTitle() != null && !task.getTitle().isEmpty()) {
                        taskList.add(task);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        String formattedDate = formatDate(task.getDueDate());
        holder.tvTaskDate.setText(formattedDate);

        holder.tvTaskName.setText(task.getTitle());
        holder.tvTaskDescription.setText(task.getDescription());

        String statusText;
        switch (task.getStatus()) {
            case 0: // A Fazer
                statusText = "Status: A Fazer";
                holder.tvTaskStatus.setTextColor(Color.RED);
                break;
            case 1: // Em Andamento
                statusText = "Status: Em Andamento";
                holder.tvTaskStatus.setTextColor(Color.BLUE);
                break;
            case 2: // Concluído
                statusText = "Status: Concluído";
                holder.tvTaskStatus.setTextColor(Color.GREEN);
                break;
            default:
                statusText = "Status: Desconhecido";
                holder.tvTaskStatus.setTextColor(Color.GRAY);
        }
        holder.tvTaskStatus.setText(statusText);
    }

    private String formatDate(String dateString) {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date date = originalFormat.parse(dateString);
            return desiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskDate, tvTaskName, tvTaskDescription, tvTaskStatus;
        ImageView imgEdit, imgDelete;

        public TaskViewHolder(View itemView) {
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

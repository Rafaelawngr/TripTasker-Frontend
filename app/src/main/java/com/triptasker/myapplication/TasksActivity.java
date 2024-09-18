package com.triptasker.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private Button btnAddTask, btnSaveTask;
    private LinearLayout taskForm;

    private EditText etTaskName, etTaskDescription, etTaskDate;
    private Spinner spinnerTaskStatus, spinnerFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        taskForm = findViewById(R.id.taskForm);

        etTaskName = findViewById(R.id.etTaskName);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        etTaskDate = findViewById(R.id.etTaskDate);
        spinnerTaskStatus = findViewById(R.id.spinnerStatus);
        spinnerFilter = findViewById(R.id.spinnerTaskFilter);
        etTaskDate = findViewById(R.id.etTaskDate);

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        recyclerViewTasks.setAdapter(taskAdapter);

        Intent intent = getIntent();
        String tripName = intent.getStringExtra("tripName");
        int tripId = intent.getIntExtra("tripId", -1);
        if (tripId == -1) {
            Toast.makeText(this, "Viagem não encontrada", Toast.LENGTH_SHORT).show();
            finish();
        }

        TextView headerTitle = findViewById(R.id.header_title);
        headerTitle.setText(tripName);

//        loadTasksForTrip(tripId);


        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTaskForm();
            }
        });

        etTaskDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TasksActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        etTaskDate.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        setupTaskFilter();
    }

    private void loadTasksForTrip(int tripId) {
        // tarefas associadas ao tripId
    }

    private void showTaskForm() {
        taskForm.setVisibility(View.VISIBLE);
        btnAddTask.setVisibility(View.GONE);
    }

    private void saveTask() {
        String taskName = etTaskName.getText().toString();
        String taskDescription = etTaskDescription.getText().toString();
        String taskDate = etTaskDate.getText().toString();
        String taskStatus = spinnerTaskStatus.getSelectedItem().toString();

        if (taskName.isEmpty() || taskDate.isEmpty()) {
            Toast.makeText(this, "Nome e Data são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        Task newTask = new Task(taskName, taskDescription, taskDate, taskStatus);

        taskList.add(newTask);

        taskAdapter.notifyDataSetChanged();

        taskForm.setVisibility(View.GONE);
        btnAddTask.setVisibility(View.VISIBLE);

        etTaskName.setText("");
        etTaskDescription.setText("");
        etTaskDate.setText("");
        spinnerTaskStatus.setSelection(0);
    }

    private void setupTaskFilter() {
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filterOption = parent.getItemAtPosition(position).toString();
                applyFilter(filterOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    private void applyFilter(String filterOption) {
        List<Task> filteredList = new ArrayList<>();

        for (Task task : taskList) {
            switch (filterOption) {
                case "Todas":
                    filteredList.add(task);
                    break;

                case "A fazer":
                    if (task.getStatus().equals("A fazer")) {
                        filteredList.add(task);
                    }
                    break;

                case "Em andamento":
                    if (task.getStatus().equals("Fazendo")) {
                        filteredList.add(task);
                    }
                    break;

                case "Concluídas":
                    if (task.getStatus().equals("Feito")) {
                        filteredList.add(task);
                    }
                    break;
            }
        }

        taskAdapter.updateTasks(filteredList);
        Toast.makeText(this, "Filtrando por: " + filterOption, Toast.LENGTH_SHORT).show();
    }

}

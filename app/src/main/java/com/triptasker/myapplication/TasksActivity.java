package com.triptasker.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class TasksActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private Button btnAddTask, btnSaveTask;
    private LinearLayout taskForm;
    private SharedPreferences preferences;
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

        HeaderUtils.setupBackButton(this);

        Intent intent = getIntent();
        String tripName = intent.getStringExtra("tripName");
        int tripId = intent.getIntExtra("tripId", -1);
        if (tripId == -1) {
            Toast.makeText(this, "Viagem não encontrada", Toast.LENGTH_SHORT).show();
            finish();
        }

        TextView headerTitle = findViewById(R.id.header_title);
        headerTitle.setText(tripName);

        loadTasksForTrip(tripId);


        btnAddTask.setOnClickListener(v -> showTaskForm());

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

        btnSaveTask.setOnClickListener(v -> saveTask(tripId));

        setupTaskFilter();

        preferences = getSharedPreferences("Shared", Context.MODE_PRIVATE);

        ImageView iconMenu = findViewById(R.id.icon_menu);

        iconMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(TasksActivity.this, iconMenu);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.logout_menu) {
                    logoutClick();
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });

    }

    private void loadTasksForTrip(int tripId) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://10.0.2.2:45455/ApiTask.aspx?TripId=" + tripId;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                Log.d("API Response", response);

                try {
                    Gson gson = new Gson();
                    Type taskListType = new TypeToken<List<Task>>() {}.getType();
                    List<Task> tasks = gson.fromJson(response, taskListType);

                    if (tasks != null) {
                        Log.d("Parsed Tasks", tasks.toString());
                        taskList.clear();
                        taskList.addAll(tasks);
                        taskAdapter.notifyDataSetChanged();

                    } else {
                        Log.e("TasksActivity", "Lista de tarefas está nula ou vazia.");
                    }
                } catch (JsonSyntaxException e) {
                    Log.e("TasksActivity", "Erro ao processar JSON do servidor.", e);
                    Toast.makeText(TasksActivity.this, "Erro ao processar dados do servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("TasksActivity", "Failed to load tasks", error);
                Toast.makeText(TasksActivity.this, "Erro ao carregar tarefas", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showTaskForm() {
        taskForm.setVisibility(View.VISIBLE);
        btnAddTask.setVisibility(View.GONE);
    }

    private void saveTask(int tripId) {
        String taskTitle = etTaskName.getText().toString();
        String taskDescription = etTaskDescription.getText().toString();
        String taskDueDate = etTaskDate.getText().toString();
        String taskStatus = spinnerTaskStatus.getSelectedItem().toString();

        if (taskTitle.isEmpty() || taskDueDate.isEmpty()) {
            Toast.makeText(this, "Título e Data são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params = new RequestParams();
        params.put("Title", taskTitle);
        params.put("Description", taskDescription);
        params.put("DueDate", taskDueDate);
        params.put("Status", convertStatusToInt(taskStatus));
        params.put("TripId", tripId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://10.0.2.2:45455/ApiTask.aspx?TripId=" + tripId, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(TasksActivity.this, "Tarefa criada com sucesso", Toast.LENGTH_SHORT).show();
                loadTasksForTrip(tripId);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(TasksActivity.this, "Erro ao criar tarefa", Toast.LENGTH_SHORT).show();
            }
        });

        // Limpar campos
        taskForm.setVisibility(View.GONE);
        btnAddTask.setVisibility(View.VISIBLE);
        etTaskName.setText("");
        etTaskDescription.setText("");
        etTaskDate.setText("");
        spinnerTaskStatus.setSelection(0);
    }


    private int convertStatusToInt(String status) {
        switch (status) {
            case "Fazendo":
                return 1;
            case "Feito":
                return 2;
            default:
                return 0;
        }
    }

    private void setupTaskFilter() {
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filterOption = parent.getItemAtPosition(position).toString();
                if (!filterOption.isEmpty()) {
                    applyFilter(filterOption);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                applyFilter("Todas");
            }
        });
    }

    private void applyFilter(String filterOption) {
        List<Task> filteredList = new ArrayList<>();

        if ("Todas".equals(filterOption)) {
            filteredList.addAll(taskList);
        } else {
            for (Task task : taskList) {
                switch (filterOption) {
                    case "A fazer":
                        if (task.getStatus() == 0) {
                            filteredList.add(task);
                        }
                        break;

                    case "Em andamento":
                        if (task.getStatus() == 1) {
                            filteredList.add(task);
                        }
                        break;

                    case "Concluídas":
                        if (task.getStatus() == 2) {
                            filteredList.add(task);
                        }
                        break;
                }
            }
        }

        taskAdapter.updateTasks(filteredList);
        Toast.makeText(this, "Filtrando por: " + filterOption, Toast.LENGTH_SHORT).show();
    }

    public void logoutClick() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", "");
        editor.putBoolean("session", false);
        editor.apply();

        finishAffinity();
    }

}
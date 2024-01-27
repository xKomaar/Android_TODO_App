package pl.sm_projekt_aplikacjatodo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import pl.sm_projekt_aplikacjatodo.R;
import pl.sm_projekt_aplikacjatodo.database.TaskRepository;
import pl.sm_projekt_aplikacjatodo.model.ProfileWithTasks;
import pl.sm_projekt_aplikacjatodo.model.Task;

public class TaskListActivity extends AppCompatActivity {
    private TaskRepository taskRepository;
    private Menu menu;
    private List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final TaskAdapter taskAdapter = new TaskAdapter();
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        this.setTitle(getIntent().getStringExtra("profileName"));

        FloatingActionButton addTaskButton = findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(view -> {
            Intent intent = new Intent(TaskListActivity.this, NewTaskActivity.class);
            intent.putExtra("ownerId", getIntent().getIntExtra("profileId", -1));
            startActivity(intent);
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = newText.toLowerCase();

                List<Task> filteredTasks = tasks.stream()
                        .filter(task -> task.getTitle().toLowerCase().contains(query))
                        .collect(Collectors.toList());

                taskAdapter.setTasks(filteredTasks);

                return true;
            }
        });

        ImageButton filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(TaskListActivity.this, view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.task_list_filter_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.sort_date_asc) {
                    tasks.sort(Comparator.comparing(Task::getDateTime));
                    taskAdapter.tasks.sort(Comparator.comparing(Task::getDateTime));
                    taskAdapter.notifyDataSetChanged();
                    return true;
                } else if (item.getItemId() == R.id.sort_date_desc) {
                    tasks.sort(Comparator.comparing(Task::getDateTime).reversed());
                    taskAdapter.tasks.sort(Comparator.comparing(Task::getDateTime).reversed());
                    taskAdapter.notifyDataSetChanged();
                    return true;
                } else if (item.getItemId() == R.id.sort_name_asc) {
                    tasks.sort(Comparator.comparing(Task::getTitle));
                    taskAdapter.tasks.sort(Comparator.comparing(Task::getTitle));
                    taskAdapter.notifyDataSetChanged();
                    return true;
                } else if (item.getItemId() == R.id.sort_name_desc) {
                    tasks.sort(Comparator.comparing(Task::getTitle).reversed());
                    taskAdapter.tasks.sort(Comparator.comparing(Task::getTitle).reversed());
                    taskAdapter.notifyDataSetChanged();
                    return true;
                } else {
                    return false;
                }
            });

            popupMenu.show();
        });

        taskRepository = new TaskRepository(this.getApplication());

        if (savedInstanceState != null && savedInstanceState.containsKey("tasks")) {
            tasks = savedInstanceState.getParcelableArrayList("tasks");
            taskAdapter.setTasks(tasks);
        } else {
            taskRepository.findAllByTaskOwnerId(getIntent().getIntExtra("profileId", -1)).observe(this, taskList -> {
                this.tasks = taskList;
                taskAdapter.setTasks(taskList);
            });
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder{
        private TextView taskTitleTextView;
        private TextView taskDateTextView;
        private CheckBox isDoneCheckBox;
        private ImageButton deleteTaskButton;
        private Task task;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.task_card, parent, false));

            taskTitleTextView = itemView.findViewById(R.id.taskTitle);
            taskDateTextView = itemView.findViewById(R.id.taskDate);
            isDoneCheckBox = itemView.findViewById(R.id.isDoneCheckBox);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(TaskListActivity.this, TaskViewActivity.class);
                intent.putExtra("taskId", task.getTaskId());
                intent.putExtra("profileName", getIntent().getStringExtra("profileName"));
                startActivity(intent);
            });

            deleteTaskButton = itemView.findViewById(R.id.delete_button);
            deleteTaskButton.setOnClickListener(view -> showDeleteConfirmationDialog());
        }

        private void showDeleteConfirmationDialog() {
            Dialog deleteConfirmationDialog = new Dialog(TaskListActivity.this);
            deleteConfirmationDialog.setContentView(R.layout.delete_confirmation_dialog);

            TextView confirmationMessage = deleteConfirmationDialog.findViewById(R.id.confirmation_message);
            confirmationMessage.setText(getString(R.string.confirmation_message, task.getTitle()));

            Button buttonYes = deleteConfirmationDialog.findViewById(R.id.button_yes);
            buttonYes.setOnClickListener(v -> {
                taskRepository.delete(task);
                deleteConfirmationDialog.dismiss();
            });

            Button buttonNo = deleteConfirmationDialog.findViewById(R.id.button_no);
            buttonNo.setOnClickListener(v -> deleteConfirmationDialog.dismiss());

            deleteConfirmationDialog.show();
        }

        public void bind(Task task) {
            this.task = task;
            this.taskTitleTextView.setText(task.getTitle());
            this.taskDateTextView.setText(task.getDateTime());
            this.isDoneCheckBox.setChecked(task.isDone());
            isDoneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                this.task.setDone(isChecked);
                taskRepository.update(this.task);
            });
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> tasks;

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskHolder(getLayoutInflater(), parent);
        }
        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            if(tasks != null) {
                Task task = tasks.get(position);
                holder.bind(task);
            } else {
                Log.d("TaskListActivity", "No tasks");
            }
        }

        @Override // zliczanie taskow
        public int getItemCount() {
            if(tasks != null) {
                return tasks.size();
            } else {
                return 0;
            }
        }

        void setTasks(List<Task> tasks) {
            this.tasks = tasks;
            notifyDataSetChanged();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.task_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.return_to_profiles_button) {
            finish();
        } else if (item.getItemId() == R.id.edit_profile) {
            Intent intent = new Intent(TaskListActivity.this, EditProfileActivity.class);
            intent.putExtra("profileId", getIntent().getIntExtra("profileId", -1));
            startActivityForResult(intent, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 0 && data != null) {
            String updatedProfileName = data.getStringExtra("profileName");
            if (updatedProfileName != null) {
                this.setTitle(updatedProfileName);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tasks != null) {
            outState.putParcelableArrayList("tasks", new ArrayList<>(tasks));
        }
    }
}
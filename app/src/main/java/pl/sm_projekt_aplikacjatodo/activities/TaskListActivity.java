package pl.sm_projekt_aplikacjatodo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

import pl.sm_projekt_aplikacjatodo.R;
import pl.sm_projekt_aplikacjatodo.database.TaskRepository;
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
                // Handle filter options
                switch (item.getItemId()) {
                    case 1: //Sort by Date (ASC)
                        // Implement sorting by date ascending
                        return true;
                    case 2: //Sort by Date (DESC)
                        // Implement sorting by date descending
                        return true;
                    case 3: //Sort by Name (ASC)
                        // Implement sorting by name ascending
                        return true;
                    case 4: //Sort by Name (DESC)
                        // Implement sorting by name descending
                        return true;
                    default:
                        return false;
                }
            });

            popupMenu.show();
        });

        taskRepository = new TaskRepository(this.getApplication());
        taskRepository.findAllByTaskOwnerId(getIntent().getIntExtra("profileId", -1)).observe(this, taskList -> {
            this.tasks = taskList;
            taskAdapter.setTasks(taskList);
        });
    }

    private class TaskHolder extends RecyclerView.ViewHolder{
        private TextView taskTitleTextView;
        private TextView taskDateTextView;
        private CheckBox isDoneCheckBox;
        private Task task;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.task_card, parent, false));

            taskTitleTextView = itemView.findViewById(R.id.taskTitle);
            taskDateTextView = itemView.findViewById(R.id.taskDate);
            isDoneCheckBox = itemView.findViewById(R.id.isDoneCheckBox);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(TaskListActivity.this, TaskViewActivity.class);
                intent.putExtra("taskId", task.getTaskId());
                startActivity(intent);
            });
        }

        public void bind(Task task) { //Bindowanie danych
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
}
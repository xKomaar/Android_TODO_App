package pl.sm_projekt_aplikacjatodo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import pl.sm_projekt_aplikacjatodo.database.TaskRepository;
import pl.sm_projekt_aplikacjatodo.model.Task;

public class TaskViewActivity extends AppCompatActivity {

    private final Calendar calendar = Calendar.getInstance();
    private TaskRepository taskRepository;
    private TextView dateTextView;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private CheckBox isDoneCheckBox;
    private CheckBox notifyCheckBox;
    private Button saveButton;
    private Task task;
    private Menu menu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_task_view);

        titleEditText = findViewById(R.id.titleEditText);
        dateTextView = findViewById(R.id.dateTextView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        isDoneCheckBox = findViewById(R.id.isDoneCheckBox);
        notifyCheckBox = findViewById(R.id.notifyCheckBox);
        saveButton = findViewById(R.id.button_save);

        Intent intent = getIntent();
        taskRepository = new TaskRepository(this.getApplication());
        taskRepository.findTaskByTaskId(intent.getIntExtra("taskId", -1)).observe(this, task -> {
            if(task != null) {
                this.task = task;
            }

            titleEditText.setText(this.task.getTitle());

            DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                dateTextView.setText(calendar.getTime().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDateTime().format(formatter));
                this.task.setDateTime(calendar.getTime().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDateTime().format(formatter));
            };
            dateTextView.setOnClickListener(view -> {
                if (!isFinishing()) {
                    new DatePickerDialog(TaskViewActivity.this, date, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
            });
            dateTextView.setText(this.task.getDateTime());

            descriptionEditText.setText(this.task.getDescription());

            isDoneCheckBox.setChecked(this.task.isDone());
            isDoneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> this.task.setDone(isChecked));

            notifyCheckBox.setChecked(this.task.isNotify());
            notifyCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> this.task.setNotify(isChecked));

            saveButton.setOnClickListener(view -> {
                if(!titleEditText.getText().toString().isEmpty()) {
                    this.task.setTitle(titleEditText.getText().toString());
                    this.task.setDescription(descriptionEditText.getText().toString());
                    taskRepository.update(this.task);
                    finish();
                } else {
                    Toast.makeText(TaskViewActivity.this, getString(R.string.emptyTitle),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

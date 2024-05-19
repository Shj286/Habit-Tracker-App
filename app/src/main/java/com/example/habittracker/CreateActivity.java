package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        EditText name = findViewById(R.id.name);
        EditText comment = findViewById(R.id.comment);
        Button submit = findViewById(R.id.button);
        final Button completeButton = findViewById(R.id.completeButton);

        Spinner typeSpinner = findViewById(R.id.typeSpinner);
        String[] types = {"Habit", "Reminder", "Note"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        typeSpinner.setAdapter(adapter);

        TimePicker timePicker = findViewById(R.id.taskTimerPicker);
// Assuming API 23 or higher for simplicity. Handle accordingly for your min API level.
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        long timerDuration = (hour * 3600 + minute * 60) * 1000; // Convert to milliseconds


        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TimePicker timePicker = findViewById(R.id.taskTimerPicker);
                int hour = 0;
                int minute = 0;

                // Check for build version for TimePicker methods
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                // Calculate the timer duration in milliseconds
                long timerDuration = (hour * 3600 + minute * 60) * 1000;

                switch (typeSpinner.getSelectedItemPosition()) {
                    case 0:
                        HabitModel habit = new HabitModel(name.getText().toString(), comment.getText().toString(), timerDuration);
                        MainActivity.habitList.add(habit);
                        if (HabitsFragment.habitAdapter != null) {
                            HabitsFragment.habitAdapter.notifyItemInserted(MainActivity.habitList.size() - 1);
                        }
                        // Start the timer for the habit
                        startTaskTimer(habit.getId(), timerDuration);
                        break;
                    case 1:
                        ReminderModel reminder = new ReminderModel(name.getText().toString(), comment.getText().toString());
                        MainActivity.reminderList.add(reminder);
                        HabitsFragment.reminderAdapter.notifyItemInserted(MainActivity.reminderList.size());
                        break;
                    case 2:
                        NoteModel note = new NoteModel(name.getText().toString(), comment.getText().toString());
                        MainActivity.noteList.add(note);
                        HabitsFragment.noteAdapter.notifyItemInserted(MainActivity.noteList.size());
                        break;
                }
                finish();
            }

        });

    }





    private void startTaskTimer(String taskId, long duration) {
        new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update UI or log remaining time (optional)
            }

            public void onFinish() {
                // This is where you'd notify the user that the timer has finished.
                // For simplicity, let's use a Toast here. For actual notification, see Step 3 from the previous guidance.
                runOnUiThread(() -> Toast.makeText(CreateActivity.this, "Timer for task " + taskId + " has finished.", Toast.LENGTH_LONG).show());
            }
        }.start();
    }




    private void notifyUser(String taskId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "task_timer_channel";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Task Timer Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher) // Set your icon
                .setContentTitle("Task Timer Finished")
                .setContentText("Your task timer for " + taskId + " has finished.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(/* notification id */ 0, builder.build());
    }

}
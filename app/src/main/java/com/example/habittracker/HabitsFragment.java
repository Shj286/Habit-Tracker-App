package com.example.habittracker;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitsFragment#} factory method to
 * create an instance of this fragment.
 */
public class HabitsFragment extends Fragment {

    private String day, title;
    private RecyclerView recyclerView;
    private String today = new DateMaker().toString();
    public static HabitAdapter habitAdapter;
    public static ReminderAdapter reminderAdapter;
    public static NoteAdapter noteAdapter;

    public HabitsFragment(String title) {
        this.title = title;
        switch(title) {
            default:
                this.day = today;
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_habits, container, false);

        TextView day = root.findViewById(R.id.day);

        day.setText(this.day);
        recyclerView = root.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        habitAdapter = new HabitAdapter(recyclerView.getContext(), MainActivity.getHabitList());
        reminderAdapter = new ReminderAdapter(recyclerView.getContext(), MainActivity.getReminderList());
        noteAdapter = new NoteAdapter(recyclerView.getContext(), MainActivity.getNoteList());

        switch(title) {
            case "Habits":
                recyclerView.setAdapter(habitAdapter);
                break;
            case "Reminders":
                recyclerView.setAdapter(reminderAdapter);
                break;
            case "Notes":
                recyclerView.setAdapter(noteAdapter);
                break;
        }

        return root;
    }

    public static class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder>{
        private ArrayList<HabitModel> list;
        private Context context;



        public HabitAdapter(Context context, ArrayList<HabitModel> list) {
            this.list = list;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView name, comment;

            TextView timerTextView;
            CountDownTimer countDownTimer; // Keep reference to timer to cancel if needed

            Button completeButton,deleteButton;

            public ViewHolder(@NonNull View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                comment = (TextView) view.findViewById(R.id.comment);
                timerTextView = itemView.findViewById(R.id.timerTextView);
                completeButton = itemView.findViewById(R.id.completeButton);
                deleteButton = itemView.findViewById(R.id.deleteButton);

            }

            public TextView getNameView() {
                return name;
            }
            public TextView getCommentView() {
                return comment;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HabitAdapter.ViewHolder holder, int position) {
            String name = list.get(position).name;
            holder.getNameView().setText(name);
            HabitModel habit = list.get(position);

            String comment = list.get(position).comment;
            holder.getCommentView().setText(comment);

            holder.deleteButton.setOnClickListener(v -> {
                // Remove the item from the list
                list.remove(position);
                // Notify the adapter of item removal
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            });


            //   holder.completeButton.setVisibility(habit.isComplete() ? View.GONE : View.VISIBLE);
            if (habit.isComplete()) {
                holder.completeButton.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.green));
                holder.timerTextView.setVisibility(View.GONE); // Hide the timer for completed habits
            } else {
                holder.completeButton.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red));
                holder.timerTextView.setVisibility(View.VISIBLE); // Show the timer for incomplete habits
            }

            holder.completeButton.setOnClickListener(v -> {
                // Mark the habit as complete and change the button color to green
                habit.setComplete(true);
                holder.completeButton.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.green));
                holder.timerTextView.setVisibility(View.GONE); // Hide the timer

                // If there's an ongoing countdown timer, cancel it
                if (holder.countDownTimer != null) {
                    holder.countDownTimer.cancel();
                    holder.countDownTimer = null; // Clear the reference to the timer
                }

                notifyItemChanged(position);
            });

            // Assuming you have a method to start or manage your countdown timer
            // Make sure to check if the habit is already completed before starting a timer
            if (!habit.isComplete() && holder.countDownTimer == null) {
                // Initialize and start your countdown timer here
                // Make sure to update or hide the timerTextView in your timer callbacks
            }


            // Start a new timer
            holder.countDownTimer = new CountDownTimer(habit.getTimerDuration(), 1000) {
                public void onTick(long millisUntilFinished) {
                    // Update the timer TextView
                    long seconds = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                    holder.timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
                }

                public void onFinish() {
                    holder.timerTextView.setText("00:00");

                    // notifyUser(context, "Timer finished for: " + habit.getName());

                }


                private void notifyUser(Context context, String message) {
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID", "Channel human-readable title", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "YOUR_CHANNEL_ID")
                            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app icon
                            .setContentTitle("Habit Timer")
                            .setContentText(message)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    notificationManager.notify(new Random().nextInt(), builder.build()); // Use a random ID for demonstration
                }

            }.start();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

    public static class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{
        private ArrayList<ReminderModel> list;

        public ReminderAdapter(Context context, ArrayList<ReminderModel> list) {
            this.list = list;

        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView name, comment;

            public ViewHolder(@NonNull View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                comment = (TextView) view.findViewById(R.id.comment);
            }

            public TextView getNameView() {
                return name;
            }
            public TextView getCommentView() {
                return comment;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReminderAdapter.ViewHolder holder, int position) {
            String name = list.get(position).name;
            holder.getNameView().setText(name);

            String comment = list.get(position).comment;
            holder.getCommentView().setText(comment);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public static class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
        private ArrayList<NoteModel> list;

        public NoteAdapter(Context context, ArrayList<NoteModel> list) {
            this.list = list;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView name, comment;

            public ViewHolder(@NonNull View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                comment = (TextView) view.findViewById(R.id.comment);
            }

            public TextView getNameView() {
                return name;
            }
            public TextView getCommentView() {
                return comment;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
            String name = list.get(position).name;
            holder.getNameView().setText(name);

            String comment = list.get(position).comment;
            holder.getCommentView().setText(comment);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

}
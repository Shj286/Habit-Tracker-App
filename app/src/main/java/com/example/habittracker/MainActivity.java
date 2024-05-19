package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String[] pages = {"Habits", "Reminders", "Notes"};
    public static ArrayList<HabitModel> habitList;
    public static ArrayList<ReminderModel> reminderList;
    public static ArrayList<NoteModel> noteList;

    //private static ArrayList<ChartsFragment> statsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 mainViewPager = findViewById(R.id.mainViewPager);
        mainViewPager.setAdapter(
                new mainAdapter(this)
        );



        int notificationId = (int) System.currentTimeMillis();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel Name"; // The user-visible name of the channel.
            String description = "My Channel Description"; // The user-visible description of the channel.
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String channelId = "MY_CHANNEL_ID"; // You should define this channel ID and use it for all notifications in this channel.
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MY_CHANNEL_ID") // Use the same channel ID
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification title")
                .setContentText("Welcome to Habit Tracker App!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());



        //Instantiate ArrayLists
        habitList = new ArrayList<>();
        reminderList = new ArrayList<>();
        noteList = new ArrayList<>();


        HabitModel habit = new HabitModel("Gym", "Go to the gym at ...");
        habitList.add(habit);

        ReminderModel reminder = new ReminderModel("Reminder", "Stuff.");
        reminderList.add(reminder);

        NoteModel note = new NoteModel("Note", "Details.");
        noteList.add(note);


        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Button goToAnotherActivityButton = findViewById(R.id.goToAnotherActivityButton);
        goToAnotherActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the desired activity
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                // Start the activity
                startActivity(intent);
            }
        });


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(
                tabLayout,
                mainViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(pages[position]);
                        tab.setIcon(R.drawable.ic_launcher_foreground);
                    }
                }
        ).attach();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    class mainAdapter extends FragmentStateAdapter {
        public mainAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        public mainAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public mainAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                default:
                    return new HabitsFragment(pages[position]);

                /*case 1:
                    return new ChartsFragment();*/
            }
        }

        @Override
        public int getItemCount() {
            return pages.length;
        }
    }

    public static ArrayList<HabitModel> getHabitList() {
        return habitList;
    }
    public static ArrayList<ReminderModel> getReminderList() {
        return reminderList;
    }
    public static ArrayList<NoteModel> getNoteList() {
        return noteList;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);


            ListPreference languagePreference = findPreference("language_preference");
            if (languagePreference != null)
            {
                languagePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
                {
                    @Override
                    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue)
                    {
                        String newLanguage = (String) newValue;
                        setLocale(newLanguage);
                        return true;
                    }
                });
            }
        }

        private void setLocale(String languageCode) {
            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            Locale locale = new Locale(languageCode);
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());

            // Restart the activity to apply the language change
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }
    }
}
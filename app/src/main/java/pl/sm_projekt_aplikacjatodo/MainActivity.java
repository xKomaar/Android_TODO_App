package pl.sm_projekt_aplikacjatodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import pl.sm_projekt_aplikacjatodo.api.WeatherApi;
import pl.sm_projekt_aplikacjatodo.database.ProfileRepository;
import pl.sm_projekt_aplikacjatodo.model.Profile;


public class MainActivity extends AppCompatActivity {

    private Menu menu;
    private ProfileRepository profileRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(getString(R.string.profiles));
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.profileRecyclerView);
        final ProfileAdapter profileAdapter = new ProfileAdapter();
        recyclerView.setAdapter(profileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton addProfileButton = findViewById(R.id.add_profile_button);
        addProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewProfileActivity.class);
            startActivity(intent);
        });

        profileRepository = new ProfileRepository(this.getApplication());
        profileRepository.findAllProfiles().observe(this, profileAdapter::setProfiles);

//        Test pogody
        WeatherApi weatherTask = new WeatherApi();
        weatherTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        //TODO: TU DODANIE PRZYCISKOW DO MENU NP WYSZUKIWANIE
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO: TU AKCJE DO MENU JAK BEDZIE
        return super.onOptionsItemSelected(item);
    }

    private class ProfileHolder extends RecyclerView.ViewHolder {

        private TextView profileNameTextView;
        private ImageView profilePictureImageView;
        private Profile profile;
        public ProfileHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.profile_list_item, parent, false));

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
                intent.putExtra("profileId", profile.getProfileId());
                intent.putExtra("profileName", profile.getName());
                startActivity(intent);
            });

            profileNameTextView = itemView.findViewById(R.id.profile_name);
            profilePictureImageView = itemView.findViewById(R.id.profile_picture);
        }

        public void bind(Profile profile) {
            this.profile = profile;
            profileNameTextView.setText(profile.getName());
            if(profile.getProfilePicture() == null) {
                profilePictureImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_blank_profile_foreground));
            } else {
                profilePictureImageView.setImageBitmap(profile.getBitmapProfilePicture());
            }
        }
    }

    private class ProfileAdapter extends RecyclerView.Adapter<ProfileHolder> {
        private List<Profile> profilesWithTasks;

        @NonNull
        @Override
        public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProfileHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
            if (profilesWithTasks != null) {
                Profile profile = profilesWithTasks.get(position);
                holder.bind(profile);
            } else {
                Log.d("MainActivity", "No profiles");
            }
        }

        @Override
        public int getItemCount() {
            if(profilesWithTasks != null) {
                return profilesWithTasks.size();
            } else {
                return 0;
            }
        }
        void setProfiles(List<Profile> profilesWithTasks) {
            this.profilesWithTasks = profilesWithTasks;
            notifyDataSetChanged();
        }
    }
}
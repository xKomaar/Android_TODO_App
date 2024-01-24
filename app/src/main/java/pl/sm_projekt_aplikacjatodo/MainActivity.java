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

import java.util.List;

import pl.sm_projekt_aplikacjatodo.database.ProfileRepository;

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

        profileRepository = new ProfileRepository(this.getApplication());
        profileRepository.findAllProfilesWithTasks().observe(this, profileAdapter::setProfiles);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_pick_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_new_profile_button) {
            Intent intent = new Intent(MainActivity.this, NewProfileActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    private class ProfileHolder extends RecyclerView.ViewHolder {

        private TextView profileNameTextView;
        private ImageView profilePictureImageView;
        private ProfileWithTasks profileWithTasks;
        public ProfileHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.profile_list_item, parent, false));

            itemView.setOnClickListener(view -> {
                //PRZEJSCIE DO TASKOW W TYM PROFILU
            });

            profileNameTextView = itemView.findViewById(R.id.profile_name);
            profilePictureImageView = itemView.findViewById(R.id.profile_picture);
        }

        public void bind(ProfileWithTasks profileWithTasks) {
            this.profileWithTasks = profileWithTasks;
            profileNameTextView.setText(profileWithTasks.getProfile().getName());
            if(profileWithTasks.getProfile().getProfilePicture() == null) {
                profilePictureImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_blank_profile_foreground));
            } else {
                profilePictureImageView.setImageBitmap(profileWithTasks.getProfile().getBitmapProfilePicture());
            }
        }
    }

    private class ProfileAdapter extends RecyclerView.Adapter<ProfileHolder> {
        private List<ProfileWithTasks> profilesWithTasks;

        @NonNull
        @Override
        public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProfileHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
            if (profilesWithTasks != null) {
                ProfileWithTasks profileWithTasks = profilesWithTasks.get(position);
                holder.bind(profileWithTasks);
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
        void setProfiles(List<ProfileWithTasks> profilesWithTasks) {
            this.profilesWithTasks = profilesWithTasks;
            notifyDataSetChanged();
        }
    }
}
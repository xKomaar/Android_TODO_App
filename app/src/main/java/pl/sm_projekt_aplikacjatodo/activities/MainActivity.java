package pl.sm_projekt_aplikacjatodo.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import com.google.android.gms.location.LocationServices;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pl.sm_projekt_aplikacjatodo.R;
import pl.sm_projekt_aplikacjatodo.database.TaskRepository;
import pl.sm_projekt_aplikacjatodo.model.ProfileWithTasks;
import pl.sm_projekt_aplikacjatodo.model.Task;
import pl.sm_projekt_aplikacjatodo.weatherApi.Weather;
import pl.sm_projekt_aplikacjatodo.database.ProfileRepository;
import pl.sm_projekt_aplikacjatodo.model.Profile;
import pl.sm_projekt_aplikacjatodo.weatherApi.WeatherApiRetrofitInstance;
import pl.sm_projekt_aplikacjatodo.weatherApi.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ProfileRepository profileRepository;

    private ProgressBar loadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

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
            showLoading(false);
            startActivity(intent);
        });

        MaterialButton checkWeatherButton = findViewById(R.id.check_weather_button);
        checkWeatherButton.setOnClickListener(view -> {
            showLoading(true);
            checkWeatherBasedOnCurrentLocation();
        });

        profileRepository = new ProfileRepository(this.getApplication());
        profileRepository.findAllProfiles().observe(this, profileAdapter::setProfiles);

        loadingProgressBar = findViewById(R.id.loadingProgressBar);
    }

    private class ProfileHolder extends RecyclerView.ViewHolder {

        private TextView profileNameTextView;
        private ImageView profilePictureImageView;
        private ImageButton arrowButton;
        private ImageButton deleteButton;
        private Profile profile;
        public ProfileHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.profile_list_item, parent, false));

            arrowButton = itemView.findViewById(R.id.arrow_button);
            arrowButton.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
                intent.putExtra("profileId", profile.getProfileId());
                intent.putExtra("profileName", profile.getName());
                showLoading(false);
                startActivity(intent);
            });

            profileNameTextView = itemView.findViewById(R.id.profile_name);
            profilePictureImageView = itemView.findViewById(R.id.profile_picture);

            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(view -> showDeleteConfirmationDialog());
        }
        private void showDeleteConfirmationDialog() {
            Dialog deleteConfirmationDialog = new Dialog(MainActivity.this);
            deleteConfirmationDialog.setContentView(R.layout.delete_confirmation_dialog);

            TextView confirmationMessage = deleteConfirmationDialog.findViewById(R.id.confirmation_message);
            confirmationMessage.setText(getString(R.string.confirmation_message, profile.getName()));

            Button buttonYes = deleteConfirmationDialog.findViewById(R.id.button_yes);
            buttonYes.setOnClickListener(v -> {
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    ProfileWithTasks profileWithTasks = profileRepository.findProfileWithTasksByProfileId(profile.getProfileId());
                    TaskRepository taskRepository = new TaskRepository(getApplication());

                    for (Task task : profileWithTasks.getTaskList()) {
                        taskRepository.delete(task);
                    }

                    profileRepository.delete(profile);
                });
                deleteConfirmationDialog.dismiss();
            });

            Button buttonNo = deleteConfirmationDialog.findViewById(R.id.button_no);
            buttonNo.setOnClickListener(v -> deleteConfirmationDialog.dismiss());

            deleteConfirmationDialog.show();
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
        @SuppressLint("NotifyDataSetChanged")
        void setProfiles(List<Profile> profilesWithTasks) {
            this.profilesWithTasks = profilesWithTasks;
            notifyDataSetChanged();
        }
    }

    public void checkWeatherBasedOnCurrentLocation() {

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    permissionGranted -> {
                        if(permissionGranted) {
                            checkWeatherBasedOnCurrentLocation();
                        } else {
                            Toast.makeText(this, R.string.location_permission_denied,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(location -> {
                if(location != null) {
                    String city = executeCityGeocoding(location);
                    WeatherService weatherService = WeatherApiRetrofitInstance.getRetrofitInstance().create(WeatherService.class);
                    Call<Weather> weatherApiCall = weatherService.getWeatherData(city.toLowerCase());
                    weatherApiCall.enqueue(new Callback<Weather>() {
                        @Override
                        public void onResponse(@NonNull Call<Weather> call, @NonNull Response<Weather> response) {
                            if (response.body() != null) {
                                Weather weather = response.body();

                                View popupView = getLayoutInflater().inflate(R.layout.weather_popup, null);

                                TextView textViewCity = popupView.findViewById(R.id.textViewCity);
                                textViewCity.setText(getString(R.string.weather_popup_title, city));

                                TextView textViewTemperature = popupView.findViewById(R.id.textViewTemperature);
                                textViewTemperature.setText(getString(R.string.temperature_label, weather.getTemperature()));

                                TextView textViewFeelsLikeTemperature = popupView.findViewById(R.id.textViewFeelsLikeTemperature);
                                textViewFeelsLikeTemperature.setText(getString(R.string.feels_like_temperature_label, weather.getFeelsLikeTemperature()));

                                TextView textViewHumidity = popupView.findViewById(R.id.textViewHumidity);
                                textViewHumidity.setText(getString(R.string.humidity_label, weather.getHumidity()));

                                TextView textViewWindSpeed = popupView.findViewById(R.id.textViewWindSpeed);
                                textViewWindSpeed.setText(getString(R.string.wind_speed_label, weather.getWindSpeed()));

                                Dialog popupDialog = new Dialog(MainActivity.this);
                                popupDialog.setContentView(popupView);

                                Button buttonClose = popupView.findViewById(R.id.buttonClose);
                                buttonClose.setOnClickListener(v -> popupDialog.dismiss());

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(popupDialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                lp.gravity = Gravity.CENTER;
                                popupDialog.show();

                                showLoading(false);
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<Weather> call, @NonNull Throwable t) {
                            Snackbar.make(findViewById(R.id.main_view),
                                    getString(R.string.weather_download_fail),
                                    BaseTransientBottomBar.LENGTH_LONG).show();
                            showLoading(false);
                        }
                    });
                }
            });
        }
    }

    private String executeCityGeocoding(Location location) {
        String city = null;
        if(location != null) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> returnedCity = executor.submit(() -> {
                Geocoder geocoder;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addressList = null;
                String resultMessage = "";

                try {
                    addressList = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1);
                } catch (IOException ioException) {
                    resultMessage = getApplicationContext().getString(R.string.service_not_available);
                    Log.e(getString(R.string.geocoder_error), resultMessage, ioException);
                }

                if(addressList == null || addressList.isEmpty()) {
                    if(resultMessage.isEmpty()) {
                        resultMessage = getApplicationContext().getString(R.string.no_addresses_found);
                        Log.e(getString(R.string.geocoder_error), resultMessage);
                    }
                } else {
                    //wpisanie miasta jakos message
                    resultMessage = addressList.get(0).getLocality();
                }
                return resultMessage;
            });
            try {
                city = returnedCity.get();
            } catch (ExecutionException | InterruptedException e) {
                Log.e(getString(R.string.geocoder_error), e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
        return city;
    }

    private void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
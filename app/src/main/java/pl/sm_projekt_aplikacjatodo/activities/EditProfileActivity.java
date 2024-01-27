package pl.sm_projekt_aplikacjatodo.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pl.sm_projekt_aplikacjatodo.R;
import pl.sm_projekt_aplikacjatodo.database.ProfileRepository;
import pl.sm_projekt_aplikacjatodo.model.Profile;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button changePhotoButton;
    private Button saveProfileButton;
    private EditText profileNameTextField;
    private ActivityResultLauncher<Intent> photoIntentLauncher;
    private ProfileRepository profileRepository;
    private Profile profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.edit_profile));
        setContentView(R.layout.activity_edit_profile);

        profileNameTextField = findViewById(R.id.profile_name);
        imageView = findViewById(R.id.profile_image);
        changePhotoButton = findViewById(R.id.change_profile_image);
        saveProfileButton = findViewById(R.id.create_new_profile);
        profileRepository = new ProfileRepository(getApplication());
        profileRepository.findProfileByProfileId(getIntent()
                        .getIntExtra("profileId", -1))
                .observe(this, dbProfile -> {
                    if (dbProfile != null) {
                        profile = dbProfile;
                    }

                    profileNameTextField.setText(profile.getName());
                    imageView.setImageBitmap(profile.getBitmapProfilePicture());
                    changePhotoButton.setOnClickListener(e -> dispatchTakePictureIntent());

                    saveProfileButton.setOnClickListener(e -> {
                        if (profileNameTextField.getText().length() > 0) {
                            profile.setProfilePicture(((BitmapDrawable) imageView.getDrawable()).getBitmap());
                            profile.setName(profileNameTextField.getText().toString());
                            profileRepository.update(profile);
                            Intent resultIntent = new Intent(EditProfileActivity.this, TaskListActivity.class);
                            resultIntent.putExtra("profileName", profile.getName());
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Toast.makeText(EditProfileActivity.this, getString(R.string.emptyProfileName),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                });

        photoIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK
                    && result.getData() != null
                    && result.getData().getExtras() != null) {
                Bundle extras = result.getData().getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    imageView.setImageBitmap(cropToSquare(imageBitmap));
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_create_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_return_button) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {

            photoIntentLauncher.launch(takePictureIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(EditProfileActivity.this,
                            getString(R.string.no_camera_found),
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = Math.min(width, height);

        int left = (width - size) / 2;
        int top = (height - size) / 2;

        return Bitmap.createBitmap(bitmap, left, top, size, size);
    }
}

package pl.sm_projekt_aplikacjatodo;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import pl.sm_projekt_aplikacjatodo.database.ProfileRepository;
import pl.sm_projekt_aplikacjatodo.model.Profile;

public class EditProfileActivity extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Button changePhotoButton;
    private Button saveProfileButton;
    private EditText profileNameTextField;
    Profile profile;
    private ProfileRepository profileRepository;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(getString(R.string.edit_profile));
        setContentView(R.layout.activity_edit_profile);

        profileNameTextField = findViewById(R.id.profile_name);
        imageView = findViewById(R.id.profile_image);
        changePhotoButton = findViewById(R.id.change_profile_image);
        saveProfileButton = findViewById(R.id.create_new_profile);

        profileRepository = new ProfileRepository(this.getApplication());
        profileRepository.findProfileByProfileId(getIntent().getIntExtra("profileId", -1)).observe(this, profile -> {
            if(profile != null) {
                this.profile = profile;
            }
            profileNameTextField.setText(this.profile.getName());

            imageView.setImageBitmap(this.profile.getBitmapProfilePicture());

            changePhotoButton.setOnClickListener(e -> dispatchTakePictureIntent());

            saveProfileButton.setOnClickListener(e -> {
                if(profileNameTextField.getText().length() > 0) {
                    this.profile.setProfilePicture(((BitmapDrawable)imageView.getDrawable()).getBitmap());
                    this.profile.setName(profileNameTextField.getText().toString());
                    profileRepository.update(this.profile);
                    Intent resultIntent = new Intent(EditProfileActivity.this, TaskListActivity.class);
                    resultIntent.putExtra("profileName", this.profile.getName());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, getString(R.string.emptyProfileName),
                            Toast.LENGTH_SHORT).show();
                }
            });
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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                imageView.setImageBitmap(cropToSquare(imageBitmap));
            }
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

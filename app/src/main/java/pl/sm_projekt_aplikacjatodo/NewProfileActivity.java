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

public class NewProfileActivity extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Button changePhotoButton;
    private Button createProfileButton;
    private EditText profileNameTextField;

    private ProfileRepository profileRepository;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(getString(R.string.stworz_profil));
        setContentView(R.layout.activity_new_profile);

        profileRepository = new ProfileRepository(this.getApplication());

        profileNameTextField = findViewById(R.id.profile_name);

        imageView = findViewById(R.id.profile_image);
        imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_blank_profile_foreground));

        changePhotoButton = findViewById(R.id.change_profile_image);
        changePhotoButton.setOnClickListener(e -> dispatchTakePictureIntent());

        createProfileButton = findViewById(R.id.create_new_profile);
        createProfileButton.setOnClickListener(e -> {
            if(profileNameTextField.getText().length() > 0) {
                Profile profile = new Profile();
                profile.setProfilePicture(((BitmapDrawable)imageView.getDrawable()).getBitmap());
                profile.setName(profileNameTextField.getText().toString());
                profileRepository.insert(profile);
                finish();
            } else {
                Toast.makeText(NewProfileActivity.this, "Nazwa profilu nie może być pusta",
                        Toast.LENGTH_SHORT).show();
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
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }
}

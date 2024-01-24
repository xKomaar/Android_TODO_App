package pl.sm_projekt_aplikacjatodo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;

@Entity
public class Profile {
    @PrimaryKey(autoGenerate = true)
    private long profileId;
    private String name;

    @Nullable
    private byte[] profilePicture;

    public Profile() {

    }

    public Profile(String name) {
        this.name = name;
    }

    public Profile(String name, Bitmap image) {
        this.name = name;
        setProfilePicture(image);
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] bytes) {
        profilePicture = bytes;
    }
    public void setProfilePicture(Bitmap profilePicture) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        profilePicture.compress(Bitmap.CompressFormat.PNG, 100, bos);
        this.profilePicture = bos.toByteArray();
    }

    public Bitmap getBitmapProfilePicture() {
        return BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length);
    }
}

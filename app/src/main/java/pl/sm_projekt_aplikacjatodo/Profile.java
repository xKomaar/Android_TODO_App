package pl.sm_projekt_aplikacjatodo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;

@Entity
public class Profile {
    @PrimaryKey(autoGenerate = true)
    private long profileId;
    private String username;
    private byte[] profilePicture;


    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

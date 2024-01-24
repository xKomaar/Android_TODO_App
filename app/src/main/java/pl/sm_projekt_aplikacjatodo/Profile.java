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
    private String name;
    private byte[] profilePicture;


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

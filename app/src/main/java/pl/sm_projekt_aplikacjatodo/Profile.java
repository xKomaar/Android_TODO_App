package pl.sm_projekt_aplikacjatodo;


import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profile {
    @PrimaryKey(autoGenerate = true)
    private long profileId;
    private String username;
    //TU MUSI BC TYPE CONVERTER BO BITMAPY NIE ZAPISZESZ DO BAZY
    //private Bitmap profilePicture;


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

//    public Bitmap getProfilePicture() {
//        return profilePicture;
//    }
//
//    public void setProfilePicture(Bitmap profilePicture) {
//        this.profilePicture = profilePicture;
//    }
}

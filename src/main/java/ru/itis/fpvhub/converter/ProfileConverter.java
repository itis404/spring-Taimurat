package ru.itis.fpvhub.converter;

import org.springframework.stereotype.Component;
import ru.itis.fpvhub.entity.ProfileEntity;
import ru.itis.fpvhub.form.ProfileUpdateForm;

@Component
public class ProfileConverter {

    public ProfileUpdateForm toForm(ProfileEntity profile) {
        ProfileUpdateForm form = new ProfileUpdateForm();
        form.setDisplayName(profile.getDisplayName());
        form.setBio(profile.getBio());
        form.setFpvExperienceLevel(profile.getFpvExperienceLevel());
        form.setFavoriteSetup(profile.getFavoriteSetup());
        return form;
    }

    public void updateEntity(ProfileEntity profile, ProfileUpdateForm form) {
        profile.setDisplayName(form.getDisplayName().trim());
        profile.setBio(normalize(form.getBio()));
        profile.setFpvExperienceLevel(normalize(form.getFpvExperienceLevel()));
        profile.setFavoriteSetup(normalize(form.getFavoriteSetup()));
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

package ru.itis.fpvhub.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileUpdateForm {

    @NotBlank(message = "Отображаемое имя обязательно")
    @Size(min = 2, max = 80, message = "Отображаемое имя должно быть от 2 до 80 символов")
    private String displayName;

    @Size(max = 1000, message = "Описание профиля не должно превышать 1000 символов")
    private String bio;

    @Size(max = 80, message = "Уровень опыта не должен превышать 80 символов")
    private String fpvExperienceLevel;

    @Size(max = 500, message = "Описание сетапа не должно превышать 500 символов")
    private String favoriteSetup;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFpvExperienceLevel() {
        return fpvExperienceLevel;
    }

    public void setFpvExperienceLevel(String fpvExperienceLevel) {
        this.fpvExperienceLevel = fpvExperienceLevel;
    }

    public String getFavoriteSetup() {
        return favoriteSetup;
    }

    public void setFavoriteSetup(String favoriteSetup) {
        this.favoriteSetup = favoriteSetup;
    }
}

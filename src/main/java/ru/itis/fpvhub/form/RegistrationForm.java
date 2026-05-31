package ru.itis.fpvhub.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "Укажите username")
    @Size(min = 3, max = 40, message = "Username должен быть от 3 до 40 символов")
    private String username;

    @NotBlank(message = "Укажите email")
    @Email(message = "Некорректный email")
    @Size(max = 254, message = "Email слишком длинный")
    private String email;

    @NotBlank(message = "Укажите пароль")
    @Size(min = 8, max = 80, message = "Пароль должен быть от 8 до 80 символов")
    private String password;

    @NotBlank(message = "Повторите пароль")
    private String passwordRepeat;

    @NotBlank(message = "Укажите отображаемое имя")
    @Size(min = 2, max = 80, message = "Отображаемое имя должно быть от 2 до 80 символов")
    private String displayName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

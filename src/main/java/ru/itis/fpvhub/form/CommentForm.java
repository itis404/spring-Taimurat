package ru.itis.fpvhub.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentForm {

    @NotBlank(message = "Комментарий не должен быть пустым")
    @Size(max = 2000, message = "Комментарий должен быть не длиннее 2000 символов")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

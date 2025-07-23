package com.example.student_management_app.dto;

import com.example.student_management_app.customannotation.PasswordMatches;
import jakarta.validation.constraints.*;

@PasswordMatches(message = "Password and repeat password should be same")
public class StudentRequestDTO {

    @NotBlank(message = "name is mandatory")
    private String name;

    @Min(value = 0, message = "marks cannot be less than 0")
    @Max(value = 100, message = "marks cannot be greater than 100")
    @PositiveOrZero(message = "marks cannot be negative")
    private int marks;

    @Email
    private String email;

    @Size(min = 8, max = 16, message = "Password should contain at least 8 character and maximum of 16 character")
    private String password;

    @Size(min = 8, max = 16, message = "Password should contain at least 8 character and maximum of 16 character")
    private String repeatPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
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

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}

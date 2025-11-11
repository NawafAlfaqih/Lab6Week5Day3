package org.example.employeemanagementsystemlab6.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Employee {

    @NotBlank(message = "ID Cannot be Empty")
    @Size(min = 3, message = "ID length must be more than 2 characters")
    private String ID;

    @NotBlank(message = "name Cannot be Empty")
    @Size(min = 5, message = "name length must be more than 4 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "name must only contain characters")
    private String name;

    @NotBlank(message = "email cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "phoneNumber cannot be empty")
    @Pattern(regexp = "^05[0-9]+$", message = "phoneNumber must be only digits and starts with '05' ")
    @Size(min = 10, max = 10, message = "phoneNumber must be exactly 10 digits long")
    private String phoneNumber;

    @NotNull(message = "age cannot be empty")
    @Min(value = 26, message = "age must be more than 25")
    private Integer age;

    @NotBlank(message = "position cannot be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$", message = "position must be (supervisor or coordinator)")
    private String position;

    @NotNull(message = "onLeave cannot be empty")
    @AssertFalse(message = "onLeave must be false")
    private boolean onLeave;

    @NotNull(message = "hireDate cannot be empty")
    @PastOrPresent(message = "hireDate should be in the present or past")
    private LocalDate hireDate;

    @NotNull(message = "annualLeave cannot be empty")
    @PositiveOrZero(message = "annualLeave must be a positive number")
    private int annualLeave;
}

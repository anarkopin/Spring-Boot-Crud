package com.proyecto.aplicacioncrudspring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordForm {

    @NotNull
    private Long id;

    @NotBlank(message="Current Password must not be blank")
    private String currentPassword;

    @NotBlank(message="New Password must not be blank")
    private String newPassword;

    @NotBlank(message="Confirm Password must not be blank")
    private String confirmPassword;

    public ChangePasswordForm(Long id) {this.id = id;}

}

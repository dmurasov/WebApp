package net.homework.webapp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DepartmentDto {

    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String departmentName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String location;
}

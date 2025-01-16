package com.microservice.task.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
private Long id;
private String name;
private String lastName;
private String email;
private Long courseId;
}

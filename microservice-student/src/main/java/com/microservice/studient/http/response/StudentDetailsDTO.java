package com.microservice.studient.http.response;

import com.microservice.studient.dto.TeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDetailsDTO {
    private Long id;
    private String name;
    private String email;
    private Long courseId;
    private String courseName;
    private TeacherDTO teacher;
}

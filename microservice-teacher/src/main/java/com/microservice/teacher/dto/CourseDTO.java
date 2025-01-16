package com.microservice.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO {
    private long id;
    private String name;
    private String teacher;
    private Long teacherId;

}

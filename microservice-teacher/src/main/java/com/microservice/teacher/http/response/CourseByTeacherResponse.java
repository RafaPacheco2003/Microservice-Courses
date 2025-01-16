package com.microservice.teacher.http.response;

import com.microservice.teacher.dto.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseByTeacherResponse {
    private String nameTeacher;
    private List<CourseDTO> courseDTOList;
}

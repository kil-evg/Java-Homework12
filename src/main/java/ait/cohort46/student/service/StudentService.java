package ait.cohort46.student.service;

import ait.cohort46.student.dto.ScoreDto;
import ait.cohort46.student.dto.StudentAddDto;
import ait.cohort46.student.dto.StudentDto;
import ait.cohort46.student.dto.StudentUpdateDto;

import java.util.List;
import java.util.Set;

public interface StudentService {
    Boolean addStudent(StudentAddDto studentAddDto);

    StudentDto findStudent(Long id);

    StudentDto removeStudent(Long id);

    StudentAddDto updateStudent(Long id, StudentUpdateDto studentUpdateDto);

    Boolean addScore(Long id, ScoreDto scoreDto);

    List<StudentDto> findStudentsByName(String name);

    Integer getStudentsQuantityByNames(Set<String> names);

    List<StudentDto> findStudentsByExamMinScore(String exam, Integer minScore);
}

package ait.cohort46.student.service;

import ait.cohort46.student.dao.StudentRepository;
import ait.cohort46.student.dto.ScoreDto;
import ait.cohort46.student.dto.StudentAddDto;
import ait.cohort46.student.dto.StudentDto;
import ait.cohort46.student.dto.StudentUpdateDto;
import ait.cohort46.student.dto.exceptions.StudentNotFoundException;
import ait.cohort46.student.model.Student;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//@Component
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    //@Autowired
    private final StudentRepository studentRepository;

    @Override
    public Boolean addStudent(StudentAddDto studentAddDto) {
        if (studentRepository.findById(studentAddDto.getId()).isPresent()) {
            return false;
        }
        Student student = new Student(studentAddDto.getId(), studentAddDto.getName(), studentAddDto.getPassword());
        studentRepository.save(student);
        return true;
    }

    @Override
    public StudentDto findStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        StudentDto studentDto = new StudentDto(student.getId(), student.getName(), student.getScores());
        return studentDto;
    }

    @Override
    public StudentDto removeStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        studentRepository.deleteById(id);
        return new StudentDto(student.getId(), student.getName(), student.getScores());
    }

    @Override
    public StudentAddDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        student.setName(studentUpdateDto.getName());
        student.setPassword(studentUpdateDto.getPassword());
        studentRepository.save(student);
        return new StudentAddDto(student.getId(), student.getName(), student.getPassword());
    }

    @Override
    public Boolean addScore(Long id, ScoreDto scoreDto) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        boolean resAddScore = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
        studentRepository.save(student);
        return resAddScore;
    }

    @Override
    public List<StudentDto> findStudentsByName(String name) {
        return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
                .filter(student -> student.getName().equals(name))
                .map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getStudentsQuantityByNames(Set<String> names) {
//        return (int) StreamSupport.stream(studentRepository.findAll().spliterator(),false)
//                .filter(student -> names.contains(student.getName()))
//                .count();
        return names.stream().mapToInt(name -> findStudentsByName(name).size()).sum();
    }

    @Override
    public List<StudentDto> findStudentsByExamMinScore(String exam, Integer minScore) {
        return StreamSupport.stream(studentRepository.findAll().spliterator(),false)
                .filter(student -> {
                    Integer score = student.getScores().get(exam);
                    return score != null && score >= minScore;
                })
                .map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
                .collect(Collectors.toList());
    }
}

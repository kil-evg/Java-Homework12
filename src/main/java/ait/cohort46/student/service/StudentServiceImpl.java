package ait.cohort46.student.service;

import ait.cohort46.student.dao.StudentRepository;
import ait.cohort46.student.dto.ScoreDto;
import ait.cohort46.student.dto.StudentAddDto;
import ait.cohort46.student.dto.StudentDto;
import ait.cohort46.student.dto.StudentUpdateDto;
import ait.cohort46.student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

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
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return null;
        }
        StudentDto studentDto = new StudentDto(student.getId(), student.getName(), student.getScores());
        return studentDto;
    }

    @Override
    public StudentDto removeStudent(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return null;
        }
        studentRepository.deleteById(id);
        return new StudentDto(student.getId(), student.getName(), student.getScores());
    }

    @Override
    public StudentAddDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return null;
        }
        student.setName(studentUpdateDto.getName());
        student.setPassword(studentUpdateDto.getPassword());
        studentRepository.save(student);
        return new StudentAddDto(student.getId(), student.getName(), student.getPassword());
    }

    @Override
    public Boolean addScore(Long id, ScoreDto scoreDto) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return null;
        }
        student.addScore(scoreDto.getExamName(), scoreDto.getScore());
        studentRepository.save(student);
        return true;
    }

    @Override
    public List<StudentDto> findStudentsByName(String name) {
        List<StudentDto> students = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            if (student.getName().equals(name)) {
                students.add(new StudentDto(student.getId(), student.getName(), student.getScores()));
            }
        }
        return students;
    }

    @Override
    public Integer getStudentsQuantityByNames(Set<String> names) {
        int quantity = 0;
        for (String name : names) {
            quantity += findStudentsByName(name).size();
        }
        return quantity;
    }

    @Override
    public List<StudentDto> findStudentsByExamMinScore(String exam, Integer minScore) {
        List<StudentDto> students = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            Integer score = student.getScores().get(exam);
            if (score != null && score >= minScore) {
                students.add(new StudentDto(student.getId(), student.getName(), student.getScores()));
            }
        }
        return students;
    }
}

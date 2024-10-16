package ait.cohort46.student.dao;

import ait.cohort46.student.model.Student;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;
import java.util.stream.Stream;

public interface StudentRepository extends CrudRepository<Student, Long> {
    Stream<Student> findByNameIgnoreCase(String name);

    Stream<Student> findByNameIn(Set<String> names);

    Stream<Student> getAllBy();

    @Query("{'scores.?0': { $gt: ?1 }}")
    Stream<Student> findByExamMinScore(String exam, Integer minScore);

    //{"scores.Math": { $gt: 90 }}
}

package dao;

import entity.Student;
import utils.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.NoSuchElementException;

public class StudentDao {

    public Student findById(String studentId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(Constants.persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Student student = entityManager.find(Student.class, studentId);

        if (student == null) {
            throw new NoSuchElementException("No student with student id: " + studentId + " found");
        }
        return student;
    }
}

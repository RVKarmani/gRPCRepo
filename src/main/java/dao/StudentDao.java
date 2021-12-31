package dao;

import com.grpc_system.stubs.student.StudentResponse;
import entity.Student;
import utils.Constants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.CodeUtils.extractStudentFromStudentResponse;

public class StudentDao {

    private static final EntityManagerFactory studentEntityManagerFactory = Persistence.createEntityManagerFactory(Constants.persistenceUnitName);
    private static final EntityManager studentEntityManager = studentEntityManagerFactory.createEntityManager();

    private static final Logger logger = Logger.getLogger(StudentDao.class.getName());

    public Student findById(String studentId) {
        Student student = studentEntityManager.find(Student.class, studentId);

        if (student == null) {
            throw new NoSuchElementException("No student with student id: " + studentId + " found");
        }
        return student;
    }

    public String addNewStudents(List<StudentResponse> studentList) {
        EntityTransaction entityTransaction = studentEntityManager.getTransaction();
        try {
            entityTransaction.begin();

            for (StudentResponse studentResponse : studentList) {
                studentEntityManager.persist(extractStudentFromStudentResponse(studentResponse));
                // yet to figure out result extraction
            }
            entityTransaction.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving student details in table");
            entityTransaction.rollback();
            throw new RuntimeException("Error saving student details: " + studentList.size());
        }
        return "Saved student details: " + studentList.size();
    }
}

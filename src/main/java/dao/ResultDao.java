package dao;

import entity.Result;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.NoSuchElementException;

public class ResultDao {
    public Result findById(String studentId) {
        // Using entity managers to manage entity
        // Using factory design pattern to get entity manager
        // Providing name of persistence unit provided in persistence.xml file
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("grpc-system");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Result result = entityManager.find(Result.class, studentId);

        if (result == null) {
            throw new NoSuchElementException("No result with student id: " + studentId + " found");
        }
        return result;
    }
}

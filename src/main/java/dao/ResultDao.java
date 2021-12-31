package dao;

import com.grpc_system.stubs.result.ResultResponse;
import entity.Result;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.CodeUtils.extractResultFromStudentResponse;

public class ResultDao {

    // Using entity managers to manage entity
    // Using factory design pattern to get entity manager
    // Providing name of persistence unit provided in persistence.xml file
    private static final EntityManagerFactory resultEntityManagerFactory = Persistence.createEntityManagerFactory("grpc-system");
    private static final EntityManager resultEntityManager = resultEntityManagerFactory.createEntityManager();

    private static final Logger logger = Logger.getLogger(ResultDao.class.getName());

    public Result findById(String studentId) {
        Result result = resultEntityManager.find(Result.class, studentId);

        if (result == null) {
            throw new NoSuchElementException("No result with student id: " + studentId + " found");
        }
        return result;
    }

    public String addNewResults(List<ResultResponse> resultResponseList) {
        EntityTransaction resultEntityTransaction = resultEntityManager.getTransaction();
        try {
            resultEntityTransaction.begin();

            for (ResultResponse resultResponse : resultResponseList) {
                resultEntityManager.persist(extractResultFromStudentResponse(resultResponse));
            }
            resultEntityTransaction.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving result details in table");
            resultEntityTransaction.rollback();
            throw new RuntimeException("Error saving result details: " + resultResponseList.size());
        }
        return "Saved result details: " + resultResponseList.size();
    }
}

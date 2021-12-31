package service;

import com.grpc_system.stubs.common.Grade;
import com.grpc_system.stubs.result.*;
import dao.ResultDao;
import entity.Result;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultServiceImpl extends ResultServiceGrpc.ResultServiceImplBase {
    // instance of dao class to interact with db
    private final ResultDao resultDao = new ResultDao();

    private static final Logger logger = Logger.getLogger(ResultServiceImpl.class.getName());

    // override getResultForStudent defined in resultService class
    @Override
    public void getResultForStudent(ResultRequest resultRequest, StreamObserver<ResultResponse> resultResponseStreamObserver) {
        String studentId = resultRequest.getStudentId();

        try {
            Result result = resultDao.findById(studentId);
            ResultResponse resultResponse = ResultResponse.newBuilder()
                    .setStudentId(studentId)
                    .setMaths(Grade.valueOf(result.getMaths()))
                    .setArt(Grade.valueOf(result.getArt()))
                    .setChemistry(Grade.valueOf(result.getChemistry()))
                    .build();

            // with the onNext method we send the response, once the response is sent we use onCompleted()
            resultResponseStreamObserver.onNext(resultResponse);
            resultResponseStreamObserver.onCompleted();
        } catch (NoSuchElementException noSuchElementException) {
            logger.log(Level.SEVERE, "NO SUCH STUDENT FOUND WITH STUDENT ID: " + studentId);
            resultResponseStreamObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    // add new results
    public void addNewResultsForStudent(ResultDetails resultDetails, StreamObserver<ResultDetailsResponseMessage> resultDetailsResponseMessageStreamObserver) {
        try {
            List<ResultResponse> resultList = resultDetails.getResultListList();
            String resultDetailsMessage = resultDao.addNewResults(resultList);

            ResultDetailsResponseMessage resultDetailsResponseMessage = ResultDetailsResponseMessage.newBuilder()
                    .setResponseMessage(resultDetailsMessage).build();

            resultDetailsResponseMessageStreamObserver.onNext(resultDetailsResponseMessage);
            resultDetailsResponseMessageStreamObserver.onCompleted();
        } catch (RuntimeException runtimeException) {
            logger.log(Level.SEVERE, "Issue saving results to table");
            resultDetailsResponseMessageStreamObserver.onError(Status.DATA_LOSS.asRuntimeException());
        }
    }
}

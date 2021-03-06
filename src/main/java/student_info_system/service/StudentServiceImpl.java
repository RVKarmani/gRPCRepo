package student_info_system.service;

import com.grpc_system.stubs.common.Gender;
import com.grpc_system.stubs.common.Grade;
import com.grpc_system.stubs.student.*;
import common.Constants;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import student_info_system.client.ResultClient;
import student_info_system.dao.StudentDao;
import student_info_system.entity.Student;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    private static final Logger logger = Logger.getLogger(StudentServiceImpl.class.getName());
    private static final String RESULT_SERVER = Constants.serverHost.concat(Constants.hostPortSeparator).concat(Constants.resultServerPort);

    private final StudentDao studentDao = new StudentDao();

    @Override
    public void getStudentInfo(StudentRequest studentRequest, StreamObserver<StudentResponse> studentResponseStreamObserver) {
        String studentId = studentRequest.getStudentId();

        try {
            Student student = studentDao.findById(studentId);
            List<String> results = getResults(studentId);

            // once results is retrieved, generate studentResponse
            StudentResponse studentResponse = StudentResponse.newBuilder()
                    .setStudentId(studentId).setName(student.getName()).setAge(student.getAge())
                    .setGender(Gender.valueOf(student.getGender())).setMaths(Grade.valueOf(results.get(0))).setArt(Grade.valueOf(results.get(1))).setChemistry(Grade.valueOf(results.get(2))).build();

            studentResponseStreamObserver.onNext(studentResponse);
            studentResponseStreamObserver.onCompleted();
        } catch (NoSuchElementException noSuchElementException) {
            logger.log(Level.SEVERE, "No student found with id: " + studentId);
            // if some error happens - send with runtime exception
            studentResponseStreamObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    private List<String> getResults(String studentId) {
        return new ResultClient(ManagedChannelBuilder.forTarget(RESULT_SERVER).usePlaintext().build())
                .getResults(studentId);
    }

    // persist new student details to table
    @Override
    public void addNewStudentInfo(StudentDetails studentDetails, StreamObserver<StudentDetailsResponseMessage> studentDetailsResponseMessageStreamObserver) {
        try {
            List<StudentResponse> studentList = studentDetails.getStudentListList();
            String studentDetailsMessage = studentDao.addNewStudents(studentList);
            StudentDetailsResponseMessage studentDetailsResponseMessage = StudentDetailsResponseMessage.newBuilder()
                    .setResponseMessage(studentDetailsMessage).build();

            studentDetailsResponseMessageStreamObserver.onNext(studentDetailsResponseMessage);
            studentDetailsResponseMessageStreamObserver.onCompleted();
        } catch (RuntimeException runtimeException) {
            logger.log(Level.SEVERE, "Issue saving students to table");
            studentDetailsResponseMessageStreamObserver.onError(Status.DATA_LOSS.asRuntimeException());
        }
    }
}

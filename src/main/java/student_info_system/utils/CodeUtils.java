package student_info_system.utils;

import com.grpc_system.stubs.result.ResultResponse;
import com.grpc_system.stubs.student.StudentResponse;
import student_info_system.entity.Result;
import student_info_system.entity.Student;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CodeUtils {

    private static final Logger logger = Logger.getLogger(CodeUtils.class.getName());

    public static Student extractStudentFromStudentResponse(StudentResponse studentResponse) {
        if (studentResponse == null) {
            return new Student();
        }
        return Student.builder()
                .studentId(studentResponse.getStudentId())
                .name(studentResponse.getName())
                .age(studentResponse.getAge())
                .gender(studentResponse.getGender().name())
                .build();
    }

    public static Result extractResultFromStudentResponse(ResultResponse resultResponse) {
        if (resultResponse == null) {
            return new Result();
        }
        String studentId = resultResponse.getStudentId();
        try {
            return Result.builder()
                    .studentId(resultResponse.getStudentId())
                    .maths(resultResponse.getMaths().toString())
                    .art(resultResponse.getArt().toString())
                    .chemistry(resultResponse.getChemistry().toString()).build();
        } catch (Exception e) {
            logger.log(Level.WARNING, "No result found for student : " + studentId);
        }
        return Result.builder().studentId("error-id").maths("0").art("0").chemistry("0").build();
    }
}

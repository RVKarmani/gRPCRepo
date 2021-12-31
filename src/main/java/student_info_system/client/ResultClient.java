package student_info_system.client;

import com.grpc_system.stubs.result.ResultRequest;
import com.grpc_system.stubs.result.ResultResponse;
import com.grpc_system.stubs.result.ResultServiceGrpc;
import io.grpc.Channel;

import java.util.List;

public class ResultClient {

    // Blocking stub means that the requests are blocked until the responses are come for previous requests
    private final ResultServiceGrpc.ResultServiceBlockingStub resultServiceBlockingStub;

    // To create the blocking stub, we need something called a channel
    // This channel is passed by the calling service.
    // So, when we call this method from the student service class, we have to provide the channel
    // Now, just add this code - we will see what is a channel in student service
    public ResultClient(Channel channel) {
        resultServiceBlockingStub = ResultServiceGrpc.newBlockingStub(channel);
    }

    public List<String> getResults(String studentId) {
        ResultRequest resultRequest = ResultRequest.newBuilder().setStudentId(studentId).build();
        ResultResponse resultResponse = resultServiceBlockingStub.getResultForStudent(resultRequest);
        return List.of(
                resultResponse.getMaths().toString(),
                resultResponse.getArt().toString(),
                resultResponse.getChemistry().toString());
    }
}

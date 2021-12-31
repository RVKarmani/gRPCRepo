package student_info_system.server;

import common.Constants;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import student_info_system.service.ResultServiceImpl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultServer {
    private static final Logger logger = Logger.getLogger(ResultServer.class.getName());

    // defining main method
    public static void main(String[] args) {
        //Create instance of server class -  using ServerBuilder
        // Adding port at which resultServiceImpl will listen to
        Server server = ServerBuilder.forPort(Integer.parseInt(Constants.resultServerPort))
                .addService(new ResultServiceImpl())
                .build();
        try {
            server.start();
            logger.log(Level.INFO, "Result Server started on port " + Constants.resultServerPort);
            // This awaitTermination method will help to remain the server, otherwise the server will shutdown quickly
            server.awaitTermination();
        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "IOException in Result Server");
        } catch (InterruptedException interruptedException) {
            logger.log(Level.SEVERE, "InterruptedException in Result Server");
        }
    }
}

package com.shopping.server;

import com.shopping.service.OrderServiceImpl;
import com.shopping.service.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderServer {
    private static final Logger logger = Logger.getLogger(OrderServer.class.getName());
    private Server server;

    public void startServer(){
        int port = 50052;
        try {
            server = ServerBuilder.forPort(port)
                    .addService(new OrderServiceImpl())
                    .build()
                    .start();

            logger.info(" Order Server started on port 50052");
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run(){
                    logger.info("Clean Order server shutdown in case JVM was shutdown");

                    try {
                        OrderServer.this.stopServer();
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, " Order Server Shutdown interrupted", e);
                    }
                }
            });
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Order Server did not start", e);
        }
    }

    public void stopServer() throws InterruptedException {
        if(server != null){
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if(server != null){
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        OrderServer orderServer = new OrderServer();
        orderServer.startServer();
        orderServer.blockUntilShutdown();
    }
}

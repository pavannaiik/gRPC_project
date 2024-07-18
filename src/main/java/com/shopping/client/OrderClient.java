package com.shopping.client;

import com.shopping.service.OrderServiceImpl;
import com.shopping.stubs.orders.Order;
import com.shopping.stubs.orders.OrderRequest;
import com.shopping.stubs.orders.OrderResponse;
import com.shopping.stubs.orders.OrderServiceGrpc;
import io.grpc.Channel;

import java.util.List;
import java.util.logging.Logger;


public class OrderClient {

    private Logger logger = Logger.getLogger(OrderClient.class.getName());
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    public OrderClient(Channel channel){
        orderServiceBlockingStub = OrderServiceGrpc.newBlockingStub(channel);
    }
    public List<Order> getOrders(int userId){
        logger.info("Order client calling the OrderService method");

        OrderRequest orderRequest = OrderRequest.newBuilder().setUserId(userId).build();
        OrderResponse orderResponse = orderServiceBlockingStub.getOrderDetails(orderRequest);
        return orderResponse.getOrderList();
    }
}


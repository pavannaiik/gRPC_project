package com.shopping.service;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import com.shopping.db.Order;
import com.shopping.db.OrderDao;
import com.shopping.stubs.orders.OrderRequest;
import com.shopping.stubs.orders.OrderRequestOrBuilder;
import com.shopping.stubs.orders.OrderResponse;
import com.shopping.stubs.orders.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());
    private OrderDao orderDao = new OrderDao();
    @Override
    public void getOrderDetails(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        logger.info("Got Orders from OrderDao and converting to OrderResponse proto object ");
        List<Order> orders = orderDao.getOrders(request.getUserId());

        List<com.shopping.stubs.orders.Order> ordersForUsers = orders.stream().map(order-> com.shopping.stubs.orders.Order.newBuilder()
                .setUserId(order.getUserId())
                .setOrderId(order.getOrderId())
                .setNoOfItems(order.getNoOfItems())
                .setTotalAmount(order.getTotalAmount())
                .setOrderDate(Timestamps.fromMillis(order.getOrderDate().getTime())).build())
                .collect((Collectors.toList())
        );

        OrderResponse orderResponse = OrderResponse.newBuilder().addAllOrder(ordersForUsers).build();
        responseObserver.onNext(orderResponse);
        responseObserver.onCompleted();

    }
}

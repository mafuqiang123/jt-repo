package com.jt.dubbo.service;

import com.jt.pojo.Order;

public interface DubboOrderService {

	String insertOrder(Order order);

	Order findOrder(String orderId);

}

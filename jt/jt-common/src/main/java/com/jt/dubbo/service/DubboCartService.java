package com.jt.dubbo.service;

import java.util.List;

import com.jt.pojo.Cart;
import com.jt.pojo.Order;

public interface DubboCartService {

	List<Cart> findCartListByuserId(Long userId);

	void updateCartNum(Cart cart);

	void deletebyItemId(Long userId, Long itemId);

	void addCart(Cart cart);

	void deleteCart(Cart cart);



}

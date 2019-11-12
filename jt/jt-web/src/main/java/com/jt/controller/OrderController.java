package com.jt.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.dubbo.service.DubboCartService;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;
@Controller
@RequestMapping("/order")
public class OrderController {

	@Reference(check=false)
	private DubboCartService cartService;
	
	/**
	 * 实现订单确认页面的跳转
	 * http://www.jt.com/order/create.html
	 * 回显数据说明:${carts}
	 */
	@Reference(check=false)
	private DubboOrderService orderService;
	
	
	
	@RequestMapping("/create")
	public String create(Model model) {
		Long userId=ThreadLocalUtil.get().getId();
		List<Cart> carts=cartService.findCartListByuserId(userId);
		model.addAttribute("carts", carts);
		return "order-cart";
	}
	
	
	/**
	 * 业务说明:
	 * 完成订单的入库操作,并且返回orderId
	 * 自己动态生成一个orderId uuid
	 * 同时实现3张表入库操作
	 * 注意事务控制
	 * @param order
	 * @return
	 * paymentType: 1
orderItems[0].itemId: 562379
orderItems[0].num: 12
orderItems[0].price: 4299000
orderItems[0].totalFee: 51588000
orderItems[0].title: 三星 W999 黑色 电信3G手机 双卡双待双通
orderItems[0].picPath: http://image.taotao.com/jd/d2ac340e728d4c6181e763e772a9944a.jpg
payment: 515880.00
orderShipping.receiverName: 陈晨
orderShipping.receiverMobile: 13800807944
orderShipping.receiverState: 北京
orderShipping.receiverCity: 北京
orderShipping.receiverDistrict: 海淀区
orderShipping.receiverAddress: 清华大学
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order) {
//		String orderId=UUID.randomUUID().toString();
//		 orderId = orderId.replaceAll("-", "");
//		 order.setOrderId(orderId);
		 
		 Long userId=ThreadLocalUtil.get().getId();
		 order.setUserId(userId);
		 String orderId=orderService.insertOrder(order);
		return SysResult.success(orderId);
	}
	
	
	
	//根据orderId查询数据库 3张表查询之后页面展现数据
	//"/order/success.html?id="+result.data
	
	@RequestMapping("/success")
	public String findOrder(@RequestParam(value="id") String id,Model model) {
		Order order=orderService.findOrder(id);
		
		model.addAttribute("order", order);
		return "success";
	}
	
	
	
	
	
	
	
	
	
	
	
}

package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.ast.And;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.dubbo.service.DubboCartService;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Reference(check = false)
	private DubboCartService cartService;

	/**
	 * items="${cartList}" 查询用户的全部购物记录信息
	 * 
	 * @return
	 */
	// http://www.jt.com/cart/show.html
	@RequestMapping("/show")
	public String Show(Model model) {
		//User user = (User) request.getAttribute("JT-USER");

//		Long userId = user.getId();// 暂时写死
		Long userId = ThreadLocalUtil.get().getId();
		List<Cart> cartList = cartService.findCartListByuserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}

	/// cart/update/num/562379/8
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart, HttpServletRequest request) {
//		User user = (User) request.getAttribute("JT-USER");
//		Long userId = user.getId();
		Long userId = ThreadLocalUtil.get().getId();
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
		return SysResult.success();
	}

	// http://www.jt.com/cart/delete/562379.html
//	@RequestMapping("/delete/{itemId}")
//	public String deletebyItemId(@PathVariable Long itemId) {
//		Long userId = 7L;
//
//		cartService.deletebyItemId(userId, itemId);
//		return "redirect:/cart/show";
//	}

	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart, HttpServletRequest request) {
//		User user = (User) request.getAttribute("JT-USER");
//		Long userId = user.getId();
		Long userId = ThreadLocalUtil.get().getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}

	// http://www.jt.com/cart/add/562379.html
	/**
	 * num: 1 itemTitle: 三星 W999 黑色 电信3G手机 双卡双待双通 itemImage:
	 * http://image.taotao.com/jd/d2ac340e728d4c6181e763e772a9944a.jpg itemPrice:
	 * 4299000
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/add/{itemId}")
	public String addCart(Cart cart, HttpServletRequest request) {
//		User user = (User) request.getAttribute("JT-USER");
//		Long userId = user.getId();
		Long userId = ThreadLocalUtil.get().getId();
		cart.setUserId(userId);
		cartService.addCart(cart);
		return "redirect:/cart/show.html";
	}

}

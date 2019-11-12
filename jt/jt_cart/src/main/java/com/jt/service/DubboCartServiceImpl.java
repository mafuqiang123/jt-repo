package com.jt.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.dubbo.service.DubboCartService;
import com.jt.dubbo.service.DubboUserService;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

//编辑接口的实现类
@Service
public class DubboCartServiceImpl  implements DubboCartService{

	@Autowired
	private CartMapper cartMapper;

	
	
	
	@Override
	public List<Cart> findCartListByuserId(Long userId) {
		// TODO Auto-generated method stub
		QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
 		return cartMapper.selectList(queryWrapper);
	}

	
	//update tb_cart set num=#{num},updated=#{updated}
	//where user_id=#{userId} and item_id=#{itemId}
	//entity :要修改的数据
	//{updateWrapper} 修改的条件构造器
	@Override
	public void updateCartNum(Cart cart) {
		Cart cartTemp=new Cart();
		cartTemp.setNum(cart.getNum())
		.setUpdated(new Date());
		UpdateWrapper<Cart>	updateWrapper=new UpdateWrapper<>();
		updateWrapper.eq("user_id", cart.getUserId());
		updateWrapper.eq("item_id", cart.getItemId());
		
		cartMapper.update(cartTemp, updateWrapper);	
		
	}


	@Override
	public void deletebyItemId(Long userId, Long itemId) {

		cartMapper.deletebyItemId(userId,itemId);
		
	}

	/**
	 * 1.判断购物车中是否有记录
	 * null:新增
	 * !null: num .updated更新操作
	 * 
	 * 
	 */

	@Override
	public void addCart(Cart cart) {
		
		QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("user_id", cart.getUserId());
		queryWrapper.eq("item_id", cart.getItemId());
		Cart one = cartMapper.selectOne(queryWrapper);
		if (one!=null) {
			Integer num = one.getNum()+cart.getNum();
//			cart.setNum(num);
//			updateCartNum(cart);
//			one.setNum(num).setUpdated(new Date());
			/**
			 * sql:这样可以不用更新所有的字段,只需要更新num和date
			 */
			Cart cartTemp=new Cart();
			cartTemp.setId(one.getId())
			.setNum(num)
			.setUpdated(new Date());
			cartMapper.updateById(cartTemp);
//			updateCartNum(one);
		}else {
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}
	}

//根据对象中不为null的属性进行sql操作
	@Override
	public void deleteCart(Cart cart) {
		// TODO Auto-generated method stub
		QueryWrapper<Cart> queryWrapper=new QueryWrapper<>(cart);
		
		cartMapper.delete(queryWrapper);
	}


	/**
	 * itemId={}
	 * userId = 7L
	num: 2
	itemTitle: 三星 W999 黑色 电信3G手机 双卡双待双通
	itemImage: http://image.taotao.com/jd/d2ac340e728d4c6181e763e772a9944a.jpg
	itemPrice: 4299000
	*/
	

	

}

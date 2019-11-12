package com.jt.mapper;

import org.apache.ibatis.annotations.Delete;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Cart;

public interface CartMapper extends BaseMapper<Cart>{

	@Delete("delete from tb_cart where item_id=#{itemId} and user_id=#{userId}")
	void deletebyItemId(Long userId, Long itemId);

}

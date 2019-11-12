package com.jt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;
import com.jt.vo.EasyUITable;

public interface ItemMapper extends BaseMapper<Item> {
	/**
	 * $:一般以字段为参数时候使用
	 * 
	 * @param start
	 * @param rows
	 * @return
	 */
	@Select("select * from tb_item order by updated desc limit #{start},#{rows}")
	List<Item> findItemByPage(@Param("start") Integer start, @Param("rows") Integer rows);

	int updateItemState(@Param("status")Integer status, @Param("ids")Long[] ids);

}

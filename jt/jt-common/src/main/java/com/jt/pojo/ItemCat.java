package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain=true)
@TableName("tb_item_cat")
public class ItemCat extends BasePojo{
	private static final long serialVersionUID = 5006550716849464495L;
	@TableId(type=IdType.AUTO)//主键自增
	private Long id;// 商品分类id
	private Long parentId;//父分类ID
	private String name;//分类名称
	private Integer status;//商品分类状态
	private Integer sortOrder;// 排序号
	private Boolean isParent;// 是否为父级
  
	/*
	 * id bigint not null auto_increment, 
	 * parent_id bigint comment '父ID=0时，代表一级类目',
	 * name varchar(150), 
	 * status int(1) default 1 comment '默认值为1，可选值：1正常，2删除',
	 * sort_order int(4) not null, 
	 * is_parent tinyint(1), 
	 * created datetime, 
	 * updated datetime,
	 */
}

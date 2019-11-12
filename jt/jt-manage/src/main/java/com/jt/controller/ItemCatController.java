package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController // 返回数据都是json
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	/**
	 * 根据商品分类id查询名称 /item/cat/queryItemName
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatById(Long itemCatId) {

		ItemCat itemCat = itemCatService.findItemCatById(itemCatId);
		return itemCat.getName();
	}

	/**
	 * 1.url:/item/cat/list
	 *  2.返回值结果List<EasyUITree> 
	 *  3.业务思想: 只查询一级商品分类信息 parent_id=0
	 *  
	 *  springMVC动态接收数据
	 *  参数名称:id
	 * 目的:id当作parentId使用
	 * 要求:初始化时,id=0
	 * 
	 * 三:
	 * 作用:当页面传递的参数与接收参数名称不一致时使用
	 * @requestParam说明:
	 * name/value:接收用户提交参数
	 * defaultValue:设定默认值
	 * required:该参数是否必传 true
	 */
	@RequestMapping("/list")
	public List<EasyUITree> findItemCatByParentId(@RequestParam(value="id",defaultValue = "0")Long parentId) {

//		if (id==null) {
//			id=0L;
//		}
//		Long parentId = id;
//		List<EasyUITree> list=
//		itemCatService.findItemCatCache(parentId);
		
		//调用数据库操作
		List<EasyUITree> list = itemCatService.findEasyUITree(parentId);
		return list;
	}
	
	
	
	//java对象            json串         String redis
	
	
	
	
	
	
	

}

package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController//返回数据都是json
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	//http://localhost:8091/item/query?page=1&rows=20
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		
		return itemService.findItemByPage(page,rows);
	}
	
	/**
	 * 业务需求
	 * url:http://localhost:8091/item/save
	 * 参数:
	 * 返回值:200 201  SysResult
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
//		try {
			 itemService.saveItem(item,itemDesc);
			 return  SysResult.success();
//			 int a=1/0;
//		} catch (Exception e) {
//			e.printStackTrace();
//			 return  SysResult.fail();
//		}
	}
	
	/**
	 * 实现商品修改
	 * 规则:一般通过主键修改
	 * @param item
	 * @return
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	/**
	 * 商品删除
	 * ids:id,id,id
	 */
	@RequestMapping("/delete")//接收的是个字符串,但是springmvc转化成数组
	public SysResult deleteItems(Long[] ids) {
		
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	
	/**
	 * 下架操作
	 * @param ids
	 * @return
	 */
	//ttp://localhost:8091/item/instock 
	@RequestMapping("/instock")
	public SysResult instock(Long[] ids) {
		
		int status=2;
		itemService.updateItemState(status,ids);
		return SysResult.success();
	}
	
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long[] ids) {
		
		int status=1;//上架
		itemService.updateItemState(status,ids);
		return SysResult.success();
	}
	
	
	/**
	 * 实现商品详情查询item/query/item/desc/
	 * {status=200,msg=,data:{itemDesc的json串}}
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		
		ItemDesc itemDesc=itemService.findItemDescById(itemId);
		//将数据回传给页面
		return SysResult.success(itemDesc);
	}
	
	//http://localhost:8091/pic/upload?dir=image
	//name="uploadFile"
	
	
	
	
	
	
	
	
	
	
}

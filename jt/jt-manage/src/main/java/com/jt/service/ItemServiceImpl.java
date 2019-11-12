package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		// 1.获取商品的记录总数
		int total = itemMapper.selectCount(null);
		// 2.获取获取分页后的数据
		// sql:select * from tb_item limit 起始页下标,arg2查询条数
		int start = (page - 1) * rows;
		List<Item> itemList = itemMapper.findItemByPage(start, rows);
		/**
		 * EasyUITable table = new EasyUITable(); table.setTotal(total);
		 * table.setRows(itemList);
		 */
		return new EasyUITable(total, itemList);
	}

	//事务得控制
	@Override
	@Transactional//控制事务
	public void saveItem(Item item,ItemDesc itemDesc) {
		
		item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
		//当数据入库之后才有主键,
		//Mybatis特性:数据库操作中主键自增之后都会自动回填主键信息
		itemMapper.insert(item);
//		int a=1/0;
		
		//新增商品详情
		itemDesc.setItemId(item.getId())
		              .setCreated(item.getCreated())
		              .setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	//8888888888888888
	@Override
	@Transactional
	public void updateItem(Item item,ItemDesc itemDesc) {

		item.setUpdated(new Date());
		itemMapper.updateById(item);

		
		//更新商品详情
		itemDesc.setItemId(item.getId())
		        .setUpdated(item.getUpdated());
         itemDescMapper.updateById(itemDesc);
		
	}

	@Override
	@Transactional
	public void deleteItems(Long[] ids) {

		// 将数组转化为List集合
		List<Long> idList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		
		itemDescMapper.deleteBatchIds(idList);
		

	}

	// 修改操作一般单独修改
	@Override
	public void updateItemState(Integer status, Long[] ids) {

//		for (Long id : ids) {
//
//			// 修改其中不为空的数据id当作条件
//			Item item = new Item();
//			item.setId(id)
//			.setStatus(status)
//			.setUpdated(new Date());
//			itemMapper.updateById(item);
//		}
		 itemMapper.updateItemState(status, ids);//itemMapper.updateItemState(status, ids,new Date());
	}
    //要求:sql实现批量操作
	//update tb_item set status=#{status},updated=#{updated} where id in(...

	@Override
	public ItemDesc findItemDescById(Long itemId) {

		ItemDesc itemDesc = itemDescMapper.selectById(itemId);
		return itemDesc;
	}

	@Override
	public Item findItemById(Long id) {
		
		return itemMapper.selectById(id);
	}
}

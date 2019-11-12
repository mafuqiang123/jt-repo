package com.jt.service;

import java.util.List;

import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;

public interface ItemCatService {

	public ItemCat findItemCatById(Long itemCatId);

	public List<EasyUITree> findEasyUITree(Long parentId);

	public  List<EasyUITree> findItemCatCache(Long parentId);



}

package com.welkin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.welkin.dao.RedisDao;
import com.welkin.mapper.TbContentCategoryMapper;
import com.welkin.mapper.TbContentMapper;
import com.welkin.pojo.TbContentCategory;
import com.welkin.pojo.TbContentCategoryExample;
import com.welkin.pojo.TbContentExample;
import com.welkin.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private RedisDao redis;
	@Value("${TB_CONTENT_KEY}")
	private String TB_CONTENT_KEY;

	public int delete(Long id) {
		//在删除之前拿到pid
		Long pid = tbContentCategoryMapper.selectByPrimaryKey(id).getParentId();
		
		// 删内容类别
		int x1 = tbContentCategoryMapper.deleteByPrimaryKey(id);
		// 删内容类别下面的所有内容
		TbContentExample ex = new TbContentExample();
		com.welkin.pojo.TbContentExample.Criteria c = ex.createCriteria();
		c.andCategoryIdEqualTo(id);
		int x2 = tbContentMapper.deleteByExample(ex);
		redis.del(TB_CONTENT_KEY);

		List<TbContentCategory> subli = findByParentId(pid);
		if(subli==null || subli.isEmpty()) {
			TbContentCategory record = new TbContentCategory();
			record.setId(pid);
			record.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKeySelective(record);
		}

		return x1 & x2;
	}

	public int update(Long id, String name) {
		TbContentCategory record = new TbContentCategory();
		record.setId(id);
		record.setName(name);
		record.setUpdated(new Date());
		return tbContentCategoryMapper.updateByPrimaryKeySelective(record);
	}

	public Long save(TbContentCategory cc) {
		cc.setIsParent(false);
		cc.setStatus(1);
		cc.setSortOrder(1);
		cc.setCreated(new Date());
		cc.setUpdated(new Date());
		/* 注意：当Mapper中的insert中加入selectKey后，会自动为实体对象的主键进行赋值 */
		int x1 = tbContentCategoryMapper.insert(cc);
		int x2 = 1;
		// 将当前新结点对应的父节点的 IsParent 属性进行更改
		TbContentCategory pcc = tbContentCategoryMapper.selectByPrimaryKey(cc.getParentId());
		if (!pcc.getIsParent()) {
			pcc.setIsParent(true);
			pcc.setUpdated(new Date());
			x2 = tbContentCategoryMapper.updateByPrimaryKeySelective(pcc);
		}

		if ((x1 & x2) > 0)
			return cc.getId();
		return null;
	}

	public List<TbContentCategory> findByParentId(Long id) {
		TbContentCategoryExample ex = new TbContentCategoryExample();
		Criteria c = ex.createCriteria();
		c.andParentIdEqualTo(id);
		return tbContentCategoryMapper.selectByExample(ex);
	}

}

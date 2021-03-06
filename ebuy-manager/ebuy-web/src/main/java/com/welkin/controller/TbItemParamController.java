package com.welkin.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.welkin.commons.Message;
import com.welkin.commons.MessageUtil;
import com.welkin.commons.Pager;
import com.welkin.pojo.TbItemParam;
import com.welkin.service.TbItemParamService;

@Controller
@RequestMapping("/item/param")
public class TbItemParamController {
	@Autowired
	private TbItemParamService tbItemParamService;

	@RequestMapping("/update/{cid}")
	@ResponseBody
	public Message update(@PathVariable Long cid, @RequestParam("paramData") String pstr) {
		// 封装产品的规格信息
		TbItemParam tbItemParam = new TbItemParam();
		// 对应产品信息
		tbItemParam.setItemCatId(cid);
		tbItemParam.setParamData(pstr);

		// 调用业务更新的方法
		boolean flag = tbItemParamService.update(tbItemParam);
		return MessageUtil.generateStatus(flag);
	}

	// 删除“商品类别”对应的“规格参数”
	@RequestMapping("/delete")
	@ResponseBody
	public Message delete(@RequestParam(value = "ids", required = true) ArrayList<Long> ids) {
		int x = tbItemParamService.delete(ids);
		return MessageUtil.generateStatus(x);
	}

	/**
	 * 功能：保存商品规格信息
	 * @param cid 对应商品类型的编号
	 * @param pstr 商品类型对应的商品规格基本信息
	 * @return
	 */
	@RequestMapping("/save/{cid}")
	@ResponseBody
	public Message save(@PathVariable Long cid, @RequestParam("paramData") String pstr) {
		// 封装产品规格数据
		TbItemParam po = new TbItemParam();
		// 设置对应产品类型的编号
		po.setItemCatId(cid);
		po.setParamData(pstr);
		// 调用业务中保存方法
		int x = tbItemParamService.save(po);
		return MessageUtil.generateStatus(x);
	}

	@RequestMapping("/query/itemcatid/{id}")
	@ResponseBody
	public Message find(@PathVariable Long id) {
		TbItemParam cat = tbItemParamService.findById(id);
		Message msg = new Message();
		msg.setStatus(200);
		if (cat != null) {
			msg.setData(cat);
		}
		return msg;
	}

	@RequestMapping("/list")
	@ResponseBody
	public Pager paramlist(int page, int rows) {
		Pager p = tbItemParamService.selectPager(page, rows);
		return p;
	}

}

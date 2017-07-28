package com.welkin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.welkin.pojo.TbContent;
import com.welkin.service.ContentService;
import com.welkin.util.Message;
import com.welkin.util.MessageUtil;
import com.welkin.util.Pager;

@Controller
@RequestMapping("/content")
public class ContentController {
	@Autowired
	private ContentService contentService;

	@RequestMapping("/delete")
	@ResponseBody
	public Message delete(List<Long> ids) {

		// System.out.println("delete:" + "ids=" + ids);
		int x = contentService.delete(ids);
		return MessageUtil.compare(x);
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Message update(TbContent con) {
		// System.out.println("editting!");
		// System.out.println("title:" + con.getTitle() + ",content:" +
		// con.getContent());

		int x = contentService.update(con);

		return MessageUtil.compare(x);
	}

	@RequestMapping("/save")
	@ResponseBody
	public Message save(TbContent con) {
		int x = contentService.save(con);
		return MessageUtil.compare(x);
	}

	@RequestMapping("/query/list")
	@ResponseBody
	public Pager findByCid(Long categoryId, int page, int rows) {
		Pager p = contentService.selectPager(categoryId, page, rows);
		return p;
	}

}

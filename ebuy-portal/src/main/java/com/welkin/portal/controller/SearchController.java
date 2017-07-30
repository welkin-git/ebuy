package com.welkin.portal.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.welkin.portal.pojo.SearchResult;
import com.welkin.portal.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	
	/**
	 * 
	 * @param query 
	 * @param page
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString, Integer page, Model model) throws UnsupportedEncodingException {
		if (queryString != null) {
			
				queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
		}
		SearchResult searchResult = searchService.search(queryString, page);
		//向页面传递参数
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", searchResult.getPageCount());
		model.addAttribute("itemList", searchResult.getItemList());
		model.addAttribute("page", page);
		
		return "search";
	}
}

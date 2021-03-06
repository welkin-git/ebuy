package com.welkin.portal.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.welkin.commons.Message;
import com.welkin.commons.MessageUtil;
import com.welkin.portal.pojo.SearchResult;
import com.welkin.portal.utils.HttpClientUtils;

@Service
public class SearchService {
	@Value("${SEARCH_URL}")
	private String SEARCH_URL;

	public SearchResult search(String queryString, int page) {
		System.out.println("search q = " + queryString);
		// 查询参数
		Map<String, String> param = new HashMap<>();
		param.put("q", queryString);
		param.put("page", page + "");
		try {
			// 调用search的服务
			String json = HttpClientUtils.doGet(SEARCH_URL, param);
			// 把字符串转换成java对象
			Message m = MessageUtil.jsonToMessage(json, SearchResult.class);
			if (m.getStatus() == 200)
				return (SearchResult) m.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

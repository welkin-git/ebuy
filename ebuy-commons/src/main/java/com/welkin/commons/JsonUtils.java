package com.welkin.commons;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	// 定义jackson对象
	private static final ObjectMapper om = new ObjectMapper();

	/**
	 * 将tb_item_param的param_date转换成tb_item_param_item的param-data 例如：将
	 * [{"group":"作者","params":["作者姓名","作者简介"]},
	 * {"group":"出版社","params":["出版社名称","出版时间"]}] 转换成
	 * [{"group":"作者","params":[{"k":"作者姓名","v":"111"},{"k":"作者简介","v":"111"}]},
	 * {"group":"出版社","params":[{"k":"出版社名称","v":"111"},{"k":"出版时间","v":"111"}]}]
	 * [{"group":"作者","params":[{"k":"作者姓名","v":"111"},{"k":"作者简介","v":"111"}]},{"group":"出版社","params":[{"k":"出版社名称","v":"111"},{"k":"出版时间","v":"111"}]}]
	 * 
	 * @param paramStr
	 * @return
	 */
	public static String JsonParamToString(String paramStr) {
		// 去除 [ 和 ]
		paramStr = paramStr.replaceAll("\\[", "");
		paramStr = paramStr.replaceAll("\\]", "");
		// 去除 { 和 }
		paramStr = paramStr.replaceAll("\\{", "");
		paramStr = paramStr.replaceAll("\\}", "");
		// 去除 "
		paramStr = paramStr.replaceAll("\"", "");
		// 去除 ：
		paramStr = paramStr.replaceAll(":", "");
		// group作者,params作者姓名,作者简介,
		// group出版社,params出版社名称,出版时间

		String str[] = paramStr.split("group");
		List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
		for (int i = 1; i < str.length; i++) {
			JsonObject jsonObject = new JsonObject();
			List<String> strList = new ArrayList<String>();
			jsonObject.setGroup(str[i].split(",params")[0]);

			String paramsStr[] = str[i].split(",params")[1].split(",");
			int paramsLen = paramsStr.length;
			for (int j = 0; j < paramsLen; j++) {
				strList.add(paramsStr[j]);
			}
			jsonObject.setParams(strList);
			jsonObjects.add(jsonObject);
		}

		String result = "";
		result += "[";
		for (int i = 0; i < jsonObjects.size(); i++) {
			result += "{\"group\":\"" + jsonObjects.get(i).getGroup() + "\",\"params\":[";
			String temp = "";
			for (int j = 0; j < jsonObjects.get(i).getParams().size(); j++) {
				temp += "{\"k\":\"" + jsonObjects.get(i).getParams().get(j) + "\",\"v\":\"\"}";
			}
			temp = temp.replaceAll("\\}\\{", "\\},\\{");
			result += temp + "]}";
		}
		result += "]";
		result = result.replaceAll("\\}\\{", "\\},\\{");

		return result;
	}

	/**
	 * 将对象转换成json字符串。
	 * 
	 * @param data
	 * @return
	 */
	public static String objectToJson(Object data) {
		try {
			String string = om.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将json结果集转化为指定类的对象
	 * 
	 * @param jsonData
	 *            json数据
	 * @param clazz
	 *            指定要转化为的对象类型
	 * @return
	 */
	public static <T> T jsonToObject(String jsonData, Class<T> beanType) {
		try {
			T t = om.readValue(jsonData, beanType);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将json数据转换成pojo对象list
	 * 
	 * @param jsonData
	 * @param beanType
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
		JavaType javaType = om.getTypeFactory().constructParametricType(List.class, beanType);
		try {
			List<T> list = om.readValue(jsonData, javaType);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}

class JsonObject {
	private String group;
	private List<String> params;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}
}

package com.akart.ecommerce.core.servlets;

import java.util.Base64;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ABC {

	public static void main(String[] args) throws Exception {
//		System.out.println(CryptoUtil.decodeKey("1Lrief6mvazOYyD+YU0K0w=="));
//		System.out.println("Incogmeato&reg; Breakfast Sausage Patties".replace("&reg;", "\u00AE"));
//		System.out.println(org.apache.commons.lang.StringEscapeUtils.unescapeHtml("Carr's Table Water Crackers Baked with Roasted Garlic amp; Herbs,Carr's&reg; Table Water&reg; Crackers,Incogmeato&reg; Breakfast Sausage Patties"));
//		System.out.println(CryptoUtil.encrypt("424940"));
//		System.out.println(Base64.getDecoder().decode("917/kFoD71KS+1Skzy6B+A=="));
//		System.out.println(CryptoUtil.decrypt(CryptoUtil.encrypt("424940")));
		
		String jsonStr = "[{\"productId\":\"310938390\",\"quantity\":2},{\"productId\":\"1549532123\",\"quantity\":3},{\"productId\":\"80307863\",\"quantity\":1}]";
		JSONArray jsonObj = new JSONArray(jsonStr);
		for(int i=0; i<jsonObj.length(); i++) {
			JSONObject obj = jsonObj.getJSONObject(i);
			System.out.println("JSON format: "+ obj);
		}

	}

}

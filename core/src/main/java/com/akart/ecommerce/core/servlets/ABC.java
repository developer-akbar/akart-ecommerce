package com.akart.ecommerce.core.servlets;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
		
//		String jsonStr = "[{\"productId\":\"310938390\",\"quantity\":2},{\"productId\":\"1549532123\",\"quantity\":3},{\"productId\":\"80307863\",\"quantity\":1}]";
//		JSONArray jsonObj = new JSONArray(jsonStr);
//		for(int i=0; i<jsonObj.length(); i++) {
//			JSONObject obj = jsonObj.getJSONObject(i);
//			System.out.println("JSON format: "+ obj);
//		}
		
//		String mrp = "16999";
//		String price = "10599";
//		int finalPrice = Math.round(((Float.parseFloat(mrp) - Float.parseFloat(price)) / Float.parseFloat(mrp)) * 100);
//		System.out.println(finalPrice);
		
//		String jsonString = "{\"80307863\":{\"prodManufacturer\":\"Puma\",\"prodVariants\":\"8,9,10\",\"prodMrpPrice\":\"2499\",\"prodId\":\"80307863\",\"prodWeight\":\"320g\",\"prodExpiry\":\"\",\"prodBarcode\":\"\",\"prodSeoName\":\"puma-white-shoes\",\"prodSpecifications\":\"\",\"prodUsedBy\":\"\",\"prodName\":\"Puma Shoes\",\"prodMfg\":\"06-08-2022\",\"productPagePath\":\"/content/akart-ecommerce/en-us/products/puma-shoes\",\"prodColor\":\"White\",\"prodLaunchDate\":\"\",\"prodSize\":\"10\",\"prodImagePath\":\"/content/dam/akart-ecommerce/product-images/puma.jpg\",\"itemCount\":\"4\",\"prodDescription\":\"\",\"_charset_\":\"utf-8\",\"prodCategory\":\"Footwear\",\"prodKeyFeatures\":\"\",\"prodBrand\":\"Puma\",\"prodPrice\":\"1299\",\"prodMetrics\":\"\",\"prodsInStock\":\"10\",\"prodSubCategory\":\"Shoe\",\"prodOffer\":48},\"1549532123\":{\"prodManufacturer\":\"Nike\",\"prodVariants\":\"6,7,8,9,10,11\",\"prodMrpPrice\":\"4199\",\"prodId\":\"1549532123\",\"prodWeight\":\"300g\",\"prodExpiry\":\"\",\"prodBarcode\":\"\",\"prodSeoName\":\"nike-shoe\",\"prodSpecifications\":\"\",\"prodUsedBy\":\"\",\"prodName\":\"Nike 2023 Shoes\",\"prodMfg\":\"12-05-2022\",\"productPagePath\":\"/content/akart-ecommerce/en-us/products/nike-2023-shoes\",\"prodColor\":\"White\",\"prodLaunchDate\":\"2022-01-01\",\"prodSize\":\"10\",\"prodImagePath\":\"/content/dam/akart-ecommerce/product-images/nike-1.jpg\",\"itemCount\":\"1\",\"prodDescription\":\"\",\"_charset_\":\"utf-8\",\"prodCategory\":\"Footwear\",\"prodKeyFeatures\":\"Best for running, protects your knee\",\"prodBrand\":\"Nike\",\"prodPrice\":\"1999\",\"prodMetrics\":\"\",\"prodsInStock\":\"10\",\"prodSubCategory\":\"Shoe\",\"prodOffer\":52},\"310938390\":{\"prodManufacturer\":\"\",\"prodVariants\":\"\",\"prodMrpPrice\":\"2999\",\"prodId\":\"310938390\",\"prodWeight\":\"\",\"prodExpiry\":\"\",\"prodBarcode\":\"\",\"prodSeoName\":\"\",\"prodSpecifications\":\"\",\"prodUsedBy\":\"\",\"prodName\":\"Reebok Mens Lite 3.0 Shoes\",\"prodMfg\":\"\",\"productPagePath\":\"/content/akart-ecommerce/en-us/products/reebok-mens-lite-3-0-shoes\",\"prodColor\":\"\",\"prodLaunchDate\":\"\",\"prodSize\":\"\",\"prodImagePath\":\"/content/dam/akart-ecommerce/product-images/rebok-1.jpg\",\"itemCount\":\"2\",\"prodDescription\":\"\",\"_charset_\":\"utf-8\",\"prodCategory\":\"Footwear\",\"prodKeyFeatures\":\"\",\"prodBrand\":\"\",\"prodPrice\":\"1699\",\"prodMetrics\":\"\",\"prodsInStock\":\"\",\"prodSubCategory\":\"\",\"prodOffer\":43}}";
//		JSONObject jsonObj = new JSONObject(jsonString);
//		System.out.println(jsonObj.getJSONObject("80307863").getInt("prodsInStock") > 1);
		
		List<String> relatedProductGtinsList = new ArrayList<>();
		System.out.println(relatedProductGtinsList);
		for(String relatedProductsGtin : relatedProductGtinsList) {
			System.out.println("GTIN "+ relatedProductsGtin);
		}
	}

}

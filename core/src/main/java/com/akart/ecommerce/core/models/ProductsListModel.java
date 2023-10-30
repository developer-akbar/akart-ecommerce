package com.akart.ecommerce.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akart.ecommerce.core.constants.Constants;
import com.akart.ecommerce.core.utils.AKartUtils;
import com.day.cq.wcm.api.Page;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductsListModel {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@SlingObject
	private ResourceResolver resolver;
	
	@ScriptVariable
	private Page currentPage;

	@Inject
	private String queryParam;
	
	@ValueMapValue
	private String sectionHeading;
	
	@ValueMapValue
	private String addToCartCTALabel;
	
	@ValueMapValue
	private String buyNowCTALabel;

	Session jcrSession;
	QueryManager queryManager;

	public Map<String, String> getProductDetailsMap() {
		Map<String, String> productDetailsMap = new HashMap<>();

		try {
			jcrSession = resolver.adaptTo(Session.class);
			if (null != jcrSession) {
				queryManager = jcrSession.getWorkspace().getQueryManager();
				String sqlQuery = "SELECT * FROM [nt:unstructured] AS node WHERE ISDESCENDANTNODE(node,'"
						+ Constants.PRODUCTS_ROOT_PATH + "') " + "AND node.[prodId]='" + queryParam + "'";
				LOGGER.info("SQL Query : {}", sqlQuery);

				Query query = queryManager.createQuery(sqlQuery, Query.JCR_SQL2);
				NodeIterator resultNodes = query.execute().getNodes();

				while (resultNodes.hasNext()) {
					Node productDetailsNode = resultNodes.nextNode();
					
					PropertyIterator propertyItr = productDetailsNode.getProperties();
					while(propertyItr.hasNext()) {
						Property productProperty = propertyItr.nextProperty();
						if(!productProperty.getName().startsWith("jcr")) {
							productDetailsMap.put(productProperty.getName(), productProperty.getValue().toString());
						}
					}

					productDetailsMap.put("productPath", AKartUtils
							.addHtmlExtension(AKartUtils.getPageOfNode(resolver, productDetailsNode).getPath()));
				}
				LOGGER.debug("Product details map: {}", productDetailsMap.toString());
				return productDetailsMap;
			}
		} catch (Exception e) {
			LOGGER.error("Exception while getting product details: ", e);
		}

		return productDetailsMap;
	}

	public List<Map<String, String>> getProductList() {
		List<Map<String, String>> productList = new ArrayList<>();

		try {
			jcrSession = resolver.adaptTo(Session.class);
			if (null != jcrSession) {
				queryManager = jcrSession.getWorkspace().getQueryManager();
				String sqlQuery = "SELECT * FROM [cq:Page] AS node WHERE ISDESCENDANTNODE(node,'"
						+ Constants.PRODUCTS_ROOT_PATH + "')";
				LOGGER.info("SQL Query : {}", sqlQuery);

				Query query = queryManager.createQuery(sqlQuery, Query.JCR_SQL2);
				NodeIterator resultNodes = query.execute().getNodes();

				while (resultNodes.hasNext()) {
					Node resultNode = resultNodes.nextNode();
					Page productPage = resolver.getResource(resultNode.getPath()).adaptTo(Page.class);
					Node productDetailsNode = AKartUtils.getProductDetailsNode(resultNode);

					Map<String, String> productDetailsMap = new HashMap<>();
					PropertyIterator propertyItr = productDetailsNode.getProperties();
					while(propertyItr.hasNext()) {
						Property productProperty = propertyItr.nextProperty();
						if(!productProperty.getName().startsWith("jcr")) {
							productDetailsMap.put(productProperty.getName(), productProperty.getValue().toString());
						}
					}
					
					int prodsInStock = Integer.parseInt(
							!productDetailsMap.get("prodsInStock").isEmpty() ? productDetailsMap.get("prodsInStock")
									: "0");
					productDetailsMap.put("isInStock", prodsInStock >= 1 ? "true" : "false");
					
					int prodOffer = Math.round(((Float.parseFloat(productDetailsMap.get("prodMrpPrice"))
							- Float.parseFloat(productDetailsMap.get("prodPrice")))
							/ Float.parseFloat(productDetailsMap.get("prodMrpPrice"))) * 100);
					productDetailsMap.put("prodOffer", Integer.toString(prodOffer));
					productDetailsMap.put("productPath", AKartUtils.addHtmlExtension(productPage.getPath()));
					
					productList.add(productDetailsMap);
				}
				LOGGER.debug("Product list: {}", Arrays.asList(productList));
				return productList;
			}
		} catch (Exception e) {
			LOGGER.error("Exception while getting product details: ", e);
		}

		return productList;
	}
	
	public boolean isMiscAdminPage() {
		return currentPage.getPath().contains("/misc/");
	}

	public String getSectionHeading() {
		return sectionHeading;
	}

	public String getAddToCartCTALabel() {
		return addToCartCTALabel;
	}

	public String getBuyNowCTALabel() {
		return buyNowCTALabel;
	}
}

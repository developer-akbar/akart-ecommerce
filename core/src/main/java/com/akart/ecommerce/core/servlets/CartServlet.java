package com.akart.ecommerce.core.servlets;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.servlet.Servlet;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akart.ecommerce.core.constants.Constants;
import com.akart.ecommerce.core.utils.AKartUtils;

@Component(service = Servlet.class,
property = {
		ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/akart/cart",
		ServletResolverConstants.DEFAULT_ERROR_HANDLER_METHOD + "=" + HttpConstants.METHOD_GET,
		ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=json"
})
public class CartServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = 1L;
	
	private final transient Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		LOGGER.info("Cart Servlet starts here");
		JSONObject allProductProperties = new JSONObject();
		try {
			String cartItems = request.getParameter("cartItems");
			LOGGER.debug("Cart Items: {}", cartItems);
			JSONArray cartItemsJson = new JSONArray(cartItems);
			LOGGER.debug("Cart items: {}", cartItemsJson);
			
			for(int i=0; i<cartItemsJson.length(); i++) {
				JSONObject cartItemJson = cartItemsJson.getJSONObject(i);
				if(cartItemJson.has("productId")) {
					String productId = cartItemJson.get("productId").toString();
					String productQty = cartItemJson.get("quantity").toString();
					
					if(StringUtils.isNotBlank(productId)) {
						NodeIterator resultNodes = AKartUtils.getResultsFromSQLQuery(request.getResourceResolver(), Constants.PRODUCTS_ROOT_PATH, "prodId", productId);

						while(resultNodes.hasNext()) {
							JSONObject productProperties = new JSONObject();
							Node productDetailsNode = resultNodes.nextNode();
							LOGGER.debug("Product Node: {}", productDetailsNode.getPath());
							PropertyIterator propertyItr = productDetailsNode.getProperties();
							while(propertyItr.hasNext()) {
								Property productProperty = propertyItr.nextProperty();
								if(!productProperty.getName().startsWith("jcr")) {
									productProperties.put(productProperty.getName(), productProperty.getValue().toString());
								}
							}
							productProperties.put("itemCount", productQty);
							
							int prodsInStock = Integer.parseInt(productProperties.has("prodsInStock")
									&& !productProperties.getString("prodsInStock").isEmpty()
											? productProperties.getString("prodsInStock")
											: "0");
							productProperties.put("isInStock", prodsInStock >= 1);
							
							int prodOffer = Math.round(((Float.parseFloat(productProperties.getString("prodMrpPrice"))
									- Float.parseFloat(productProperties.getString("prodPrice")))
									/ Float.parseFloat(productProperties.getString("prodMrpPrice"))) * 100);
							productProperties.put("prodOffer", prodOffer);
							allProductProperties.put(productId, productProperties);
						}
					}
				}
			}
			LOGGER.debug("All products properties: {}", allProductProperties);
			
			response.setStatus(HttpStatus.SC_OK);
			response.getWriter().write(allProductProperties.toString());
			
		} catch(Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			LOGGER.error("Exception while getting cart items: ", e);
		}
		LOGGER.info("Cart Servlet ends here");
	}

}

package com.akart.ecommerce.core.servlets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akart.ecommerce.core.constants.Constants;
import com.akart.ecommerce.core.utils.AKartUtils;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Servlet.class, 
property = {
		ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/akart/products",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
		ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=json" 
})
public class ProductsServlet extends SlingAllMethodsServlet {

	private static final String PROD_NAME = "prodName";

	private static final String PRODUCT_PATH = "productPath";

	private static final String PRODUCT_ID = "productId";

	private static final String MESSAGE = "message";

	private static final String STATUS = "status";

	private static final String AKART_SUB_SERVICE = "akartSubService";

	private static final long serialVersionUID = 1L;

	private final transient Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Reference
	private transient ResourceResolverFactory resolverFactory;

	@Reference
	private transient Replicator replicator;

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		LOGGER.info("Add ProductServlet starts...");
		response.setContentType(Constants.APLLICATION_JSON);
		
		String selector = request.getRequestPathInfo().getSelectors()[0];
		LOGGER.debug("Request received for {}", selector);
		
		JSONObject payload = AKartUtils.requestObjectAsJson(request);
		LOGGER.debug("Request payload: {}", payload.toString());
		
		if(StringUtils.equals(selector, "addproduct")) {
			createProductPage(payload, response);
		} else if(StringUtils.equals(selector, "updateproduct")) {
			updateProductPage(request, response, payload);
		}
		
		LOGGER.info("Add ProductServlet ends...");
	}
	
	private void updateProductPage(SlingHttpServletRequest request, SlingHttpServletResponse response, JSONObject payload) {
		LOGGER.debug("Inside update product method");
		ResourceResolver resolver = request.getResourceResolver();
		try {
			Resource pageRes = resolver.getResource(payload.getString("productPagePath"));
			
			if (null != pageRes) {
				Session session = resolver.adaptTo(Session.class);
				Node productDetailsNode = AKartUtils.getProductDetailsNode(pageRes.adaptTo(Node.class));
				LOGGER.debug("Deleting {} node", productDetailsNode.getPath());
				replicator.replicate(session, ReplicationActionType.DEACTIVATE, productDetailsNode.getPath());
				productDetailsNode.remove();
				session.save();
				
				pageRes.adaptTo(Page.class).getContentResource().adaptTo(Node.class).setProperty("jcr:title", payload.getString("prodName"));
				
				Node newProductDetailsNode = pageRes.adaptTo(Node.class).addNode(Constants.PROD_DETAILS_NODE, "nt:unstructured");
				LOGGER.debug("Product Details Node added {}", newProductDetailsNode.getPath());

				@SuppressWarnings("unchecked")
				Iterator<String> keys = payload.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					if (JcrUtil.isValidName(key)) {
						productDetailsNode.setProperty(key, payload.getString(key));
					}
				}
				productDetailsNode.setProperty("prodId", Math.abs(pageRes.getPath().hashCode() - 1));
				LOGGER.debug("Product updated properties added at {}", productDetailsNode.getPath());

				// publish created product
				replicateContent(session, pageRes.getPath());
				LOGGER.debug("Product published");
				
				response.setStatus(HttpServletResponse.SC_OK);
				JSONObject respObj = new JSONObject();
				respObj.put(STATUS, HttpServletResponse.SC_OK);
				respObj.put(MESSAGE, "Product page updated successfully");
				respObj.put(PRODUCT_ID, productDetailsNode.getProperty("prodId").getString());
				respObj.put(PRODUCT_PATH, pageRes.getPath());
				response.getWriter().println(respObj.toString());
			}

		} catch (Exception e) {
			LOGGER.error("Exception while updating product page: ", e);
			setErrorResponse(response, e, "Failed to update product page");
		}
		
	}

	@Override
	protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		LOGGER.info("Delete Product Servlet starts...");
		try {
			response.setContentType(Constants.APLLICATION_JSON);

			String selector = request.getRequestPathInfo().getSelectors()[0];
			LOGGER.debug("Request received for {}", selector);

			String productPagePath = request.getParameter("productPagePath");

			if (StringUtils.equals(selector, "deleteproduct")) {
				boolean isPageDelted = deleteProductPage(request, productPagePath);
				if (isPageDelted) {
					response.setStatus(HttpServletResponse.SC_OK);
					JSONObject respObj = new JSONObject();
					respObj.put(STATUS, HttpServletResponse.SC_OK);
					respObj.put(MESSAGE, "Product page deleted successfully");
					response.getWriter().println(respObj.toString());
				} else {
					setErrorResponse(response, null, "Failed to delete product page");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while deleting product page: ", e);
			setErrorResponse(response, e, "Failed to delete product page");
		}

		LOGGER.info("Delete Product Servlet ends...");
	}

	private void setErrorResponse(SlingHttpServletResponse response, Exception e, String message) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try {
			JSONObject respObj = new JSONObject();
			respObj.put(STATUS, 500);
			respObj.put(MESSAGE, message);
			if(null != e) {
				respObj.put("errormessage", e.getLocalizedMessage());
			}
			response.getWriter().println(respObj.toString());
		} catch (Exception ex) {
			LOGGER.error("Exception while setting error response object: ", ex);
		}
	}

	private boolean deleteProductPage(SlingHttpServletRequest request, String productPagePath) {
		LOGGER.debug("Inside delete product page method");
		try {
			ResourceResolver resolver = request.getResourceResolver();

			Session session = resolver.adaptTo(Session.class);
			Resource pageResource = resolver.getResource(productPagePath);
			if (null != pageResource) {
				Node pageNode = pageResource.adaptTo(Node.class);
				LOGGER.debug("Deleting {} page", pageNode.getPath());
				replicator.replicate(session, ReplicationActionType.DEACTIVATE, pageNode.getPath());
				pageNode.remove();
				session.save();
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception while deleting the product page: ", e);
			return false;
		}
	}

	private void createProductPage(JSONObject payload, SlingHttpServletResponse response) {
		LOGGER.debug("Inside create product page method");
		ResourceResolver resolver = getResourceResolverFromFactory();
		Session session = resolver.adaptTo(Session.class);
		PageManager pageManager = resolver.adaptTo(PageManager.class);

		try {
			Resource res = resolver.getResource(Constants.PRODUCTS_ROOT_PATH);

			if (null != res) {
				String pageName = payload.getString(PROD_NAME).replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();

				if (!res.adaptTo(Node.class).hasNode(pageName)) {
					Page productPage = pageManager.create(Constants.PRODUCTS_ROOT_PATH, pageName,
							Constants.PDP_TEMPLATE, payload.getString(PROD_NAME));

					Node productDetailsNode = productPage.adaptTo(Node.class).addNode(Constants.PROD_DETAILS_NODE, "nt:unstructured");
					LOGGER.debug("Product page created with node {}", productPage.getPath());

					@SuppressWarnings("unchecked")
					Iterator<String> keys = payload.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						if (JcrUtil.isValidName(key)) {
							productDetailsNode.setProperty(key, payload.getString(key));
						}
					}
					productDetailsNode.setProperty("prodId", Math.abs(productPage.getPath().hashCode() - 1));
					LOGGER.debug("Product properties added at {}", productDetailsNode.getPath());

					// publish created product
					replicateContent(session, productPage.getPath());
					LOGGER.debug("Product published");

					response.setStatus(HttpServletResponse.SC_CREATED);
					JSONObject respObj = new JSONObject();
					respObj.put(STATUS, HttpServletResponse.SC_CREATED);
					respObj.put(MESSAGE, "Product page created successfully");
					respObj.put(PRODUCT_ID, productDetailsNode.getProperty("prodId").getString());
					respObj.put(PRODUCT_PATH, productPage.getPath());
					response.getWriter().println(respObj.toString());
				} else {
					LOGGER.debug("The product page is already available at {}", res.getChild(pageName).getPath());

					response.setStatus(HttpServletResponse.SC_OK);
					JSONObject respObj = new JSONObject();
					respObj.put(STATUS, HttpServletResponse.SC_OK);
					respObj.put("isProductAvailable", true);
					respObj.put(MESSAGE, "Product page is alreay available");
					respObj.put(PRODUCT_PATH, res.getChild(pageName).getPath());
					response.getWriter().println(respObj.toString());
				}

			}
		} catch (Exception e) {
			LOGGER.error("Exception while creating product page: ", e);
			setErrorResponse(response, e, "Failed to create product page");
		}

	}

	/**
	 * This method will publishes the content
	 * 
	 * @param session
	 * @param path:   GTIN path
	 */
	private void replicateContent(Session session, String path) {
		try {
			session.refresh(true);
			session.save();
			replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
			LOGGER.debug("Path replicated: {}", path);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while replicating path: {}", e);
		}
	}

	private ResourceResolver getResourceResolverFromFactory() {
		try {
			Map<String, Object> xmlReaderMap = new HashMap<>();
			xmlReaderMap.put(ResourceResolverFactory.SUBSERVICE, AKART_SUB_SERVICE);
			return resolverFactory.getServiceResourceResolver(xmlReaderMap);
		} catch (Exception e) {
			LOGGER.error("Exception while getting resource resolver: ", e);
		}
		return null;
	}

}

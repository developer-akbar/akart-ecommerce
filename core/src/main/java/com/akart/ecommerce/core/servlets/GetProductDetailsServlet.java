package com.akart.ecommerce.core.servlets;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akart.ecommerce.core.constants.Constants;
import com.akart.ecommerce.core.utils.AKartUtils;

@Component(service = Servlet.class, 
property = {
		ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/akart/getproductdetails",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
		ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=json" 
})
public class GetProductDetailsServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private final transient Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		LOGGER.info("Get Product Details servlet starts");

		response.setContentType(Constants.APLLICATION_JSON);

		try {
			String productPagePath = request.getParameter("productPagePath");

			Resource productRes = request.getResourceResolver().getResource(productPagePath);

			if (null != productRes) {
				JSONObject productDetailsJson = new JSONObject();
				
				Node productPageNode = productRes.adaptTo(Node.class);
				Node productDetailsNode = AKartUtils.getProductDetailsNode(productPageNode);
				PropertyIterator propItr = productDetailsNode.getProperties();
				
				while (propItr.hasNext()) {
					Property prop = propItr.nextProperty();
					String propName = prop.getName();
					String propValue = productDetailsNode.getProperty(propName).getString();
					productDetailsJson.put(propName, propValue);
				}

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(productDetailsJson.toString());
				LOGGER.debug("Product Details: {}", productDetailsJson.toString());
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write(new JSONObject().put("message", "Product path not found").toString());
			}
		} catch (Exception e) {
			LOGGER.error("Exception while getting product details: ", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			try {
				response.getWriter().write(new JSONObject().put("message", "Something went wrong").toString());
			} catch (Exception e1) {
				LOGGER.error("Exception while getting product details: ", e1);
			}
		}

		LOGGER.info("Get Product Details servlet ends");
	}

}

package com.akart.ecommerce.core.utils;

import java.io.BufferedReader;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akart.ecommerce.core.constants.Constants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

public class AKartUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AKartUtils.class);

	/**
	 * Util method to generate Json from Request Parameter map
	 * 
	 * @param slingRequest
	 * @return jsonObj
	 */
	public static JSONObject requestObjectAsJson(SlingHttpServletRequest request) {
		try {
			// retrieving request payload
			StringBuilder builder = new StringBuilder();
			String line = null;
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return new JSONObject(builder.toString());
		} catch (Exception e) {
			LOGGER.debug("Exception while getting payload data: ", e);
		}
		return null;
	}
	
	/**
	 * This util method fetches property value from a node
	 * 
	 * @param property Property name
	 * @param node Node name
	 * @return Property value if available, else empty string 
	 */
	public static String readNodeProperty(final String property, final Node node) {
		try {
			if(null != node) {
				return node.hasProperty(property) ? node.getProperty(property).getString() : "";
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while reading node property:: ", e);
		}
		
		return "";
	}
	
	/**
	 * This method will return Page object from any depth of nodes. Say the path is
	 * /content/project/page1/childnode1/childnode2/childnode3
	 * 
	 * @param resolver {@link ResourceResolver}
	 * @param nodeName Node object: childnode3
	 * @return Page object of a node
	 */
	public static Page getPageOfNode(ResourceResolver resolver, Node nodeName) throws RepositoryException {
		Page page = null;
		Resource res = resolver.getResource(nodeName.getPath());
		PageManager pageManager = resolver.adaptTo(PageManager.class);

		while (null == page && null != res) {
			res = res.getParent();
			if (res != null) {
				page = pageManager.getContainingPage(res);
			}
		}
		if (null != page) {
			return page;
		}
		return page;
	}
	
	/**
	 * Util method to add html extension for internal content paths
	 * 
	 * @param url Content path of a page
	 * @return Path with html extension
	 */
	public static String addHtmlExtension(String url) {
		if(StringUtils.isNotBlank(url) && url.startsWith("/content/") && !url.endsWith(Constants.DOT_HTML)) {
			url = url+Constants.DOT_HTML;
		}
		return url;
		
	}
	
	/**
	 * Util method to get product_details node of a page
	 * 
	 * @param request {@link SlingHttpServletRequest}
	 * @param currentPage Current page object
	 * @return Node product_details if available, else null
	 */
	public static Node getProductDetailsNode(Node currentPageNode) {
		Node prodDetailsNode= null;
		try {
			LOGGER.debug("Current Page path: {}", currentPageNode.getPath());
			if(currentPageNode.hasNode(Constants.PROD_DETAILS_NODE)) {
				prodDetailsNode = currentPageNode.getNode(Constants.PROD_DETAILS_NODE);
			}
		} catch(Exception e) {
			LOGGER.error("Exception while getting data_pim node:: ", e);
		}
		return prodDetailsNode;
	}
	
	/**
	 * Util method to return search results using SQL2 query
	 * 
	 * @param resolver ResourceResolver object
	 * @param rootPath Root path to search within
	 * @param propName JCR property name
	 * @param propValue Property value
	 * @return NodeIterator object
	 */
	public static NodeIterator getResultsFromSQLQuery(ResourceResolver resolver, String rootPath, String propName, String propValue) {
		NodeIterator resultNodes = null;
		try {
			Session jcrSession = resolver.adaptTo(Session.class);
			if (null != jcrSession && StringUtils.isNotBlank(propValue)) {
				QueryManager queryManager = jcrSession.getWorkspace().getQueryManager();

				String sqlStatement = "SELECT * FROM [nt:unstructured] AS pim_node WHERE ISDESCENDANTNODE(pim_node,'"
						+ rootPath + "') AND pim_node.[" + propName + "]='" + propValue + "'";
				LOGGER.debug("SQL Query : {}", sqlStatement);

				Query query = queryManager.createQuery(sqlStatement, Query.JCR_SQL2);
				resultNodes = query.execute().getNodes();
			}
		} catch (Exception e) {
			LOGGER.error("Exception while executing SQL query: ", e);
		}
		return resultNodes;
	}

}

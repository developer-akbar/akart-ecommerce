package com.akart.ecommerce.core.servlets;

/* * This Java Quick Start uses the jackrabbit-standalone-2.4.0.jar * file. See the previous section for the location of this JAR file */
import java.util.Iterator;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Node;
import org.apache.jackrabbit.commons.JcrUtils;

public class Demo {
	public static void main(String[] args) throws Exception {
		try {
			// Create a connection to the CQ repository running on local host
			Repository repository = JcrUtils.getRepository("http://localhost:4502/crx/server");
			// Create a Session javax.jcr.Session
			Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
			String firstName = "Tom";
			String lastName = "Blue";
			String phone = "555-555-5555";
			String desc = "active customer";

			// Create a node that represents the root node
			Node root = session.getRootNode();
			// Get the content node in the JCR
			Node content = root.getNode("content");

			// Determine if the content/customer node exists
			Node customerRoot = null;
			int custRec = doesCustExist(content);

			// -1 means that content/customer does not exist
			if (custRec == -1)
				// content/customer does not exist -- create it
				customerRoot = content.addNode("customer", "sling:OrderedFolder");
			else
				// content/customer does exist -- retrieve it
				customerRoot = content.getNode("customer");
			int custId = custRec + 1;

			// assign a new id to the customer node
			// Store content from the client JSP in the JCR
			Node custNode = customerRoot.addNode("customer" + firstName + lastName + custId, "nt:unstructured");
			// make sure name of node is unique
			custNode.setProperty("id", custId);
			custNode.setProperty("firstName", firstName);
			custNode.setProperty("lastName", lastName);
			custNode.setProperty("phone", phone);
			custNode.setProperty("desc", desc);
			// Save the session changes and log out
			session.save();
			session.logout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Determines if the content/customer node exists This method returns these
	 * values: -1 - if customer does not exist 0 - if content/customer node exists;
	 * however, contains no children number - the number of children that the
	 * content/customer node contains
	 */
	private static int doesCustExist(Node content) {
		try {
			int index = 0;
			int childRecs = 0;
			java.lang.Iterable<Node> custNode = JcrUtils.getChildNodes(content, "customer");
			Iterator<Node> it = custNode.iterator();

			// only going to be 1 content/customer node if it exists
			if (it.hasNext()) {
				// Count the number of child nodes to customer
				Node customerRoot = content.getNode("customer");
				Iterable<Node> itCust = JcrUtils.getChildNodes(customerRoot);
				Iterator<Node> childNodeIt = itCust.iterator();

				// Count the number of customer child nodes
				while (childNodeIt.hasNext()) {
					childRecs++;
					childNodeIt.next();
				}
				return childRecs;
			} else
				return -1;

			// content/customer does not exist
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
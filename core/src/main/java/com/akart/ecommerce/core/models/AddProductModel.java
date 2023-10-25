package com.akart.ecommerce.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AddProductModel {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@ChildResource
	private Resource productFields;
	
	@ValueMapValue
	private String heading;

	public Resource getProductFields() {
		LOGGER.debug("Prod fields: {}", productFields);
		return productFields;
	}

	public String getHeading() {
		return heading;
	}

}

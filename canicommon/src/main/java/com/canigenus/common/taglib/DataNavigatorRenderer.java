/*
 * Copyright 2011 http://bluefoot.info
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.canigenus.common.taglib;
    
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlOutcomeTargetLink;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import javax.servlet.RequestDispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.canigenus.common.model.LazyDataModel;
import com.canigenus.common.util.JavaUtil;

/**
 * Renderer for {@link DataNavigator}. Will render something like:
 * 
 * <pre>
 * &lt;h:link value="1" outcome="/your/current/view"&gt;
 *     &lt;f:param name="page" value="1"&gt;&lt;/f:param&gt;
 * &lt;/h:link&gt;
 * &lt;h:link value="2" outcome="/your/current/view"&gt;
 *     &lt;f:param name="page" value="2"&gt;&lt;/f:param&gt;
 * &lt;/h:link&gt;  
 * &lt;h:link value="3" outcome="/your/current/view"&gt;
 *     &lt;f:param name="page" value="3"&gt;&lt;/f:param&gt;
 * &lt;/h:link&gt;
 * </pre>
 * 
 * @author bluefoot
 * 
 */
@FacesRenderer(rendererType = "canigenus.DataNavigator", componentFamily = "canigenus.datanavigator.component")
public class DataNavigatorRenderer extends Renderer {

	public DataNavigatorRenderer() {
		System.out.println("Created the Renderer");
	}

	private static final Logger log = LoggerFactory
			.getLogger(DataNavigatorRenderer.class);
	private static final String DEFAULT_CURRENT_PAGE_CLASS = "current-page";

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("encoding DataNavigatorRender");
		}

		// ~ Getting the tools ==========================================

		DataNavigator dataNavigator = (DataNavigator) component;
		ResponseWriter writer = context.getResponseWriter();
		LazyDataModel<?> model = dataNavigator.getModel();

		// ~ Root element ===============================================

		writer.startElement("div", dataNavigator);
		writer.writeAttribute("id", dataNavigator.getClientId(context), "id");
		if (dataNavigator.getStyleClass() != null) {
			writer.writeAttribute("class", dataNavigator.getStyleClass(),
					"styleClass");
		}

		// ~ Links ======================================================

		String innerStyleClass = "";
		if (!JavaUtil.isBlank(dataNavigator.getInnerStyleClass())) {
			innerStyleClass = dataNavigator.getInnerStyleClass();
		}
		for (int i = 1; i <= model.getNumberOfPages(); i++) {
			if (log.isDebugEnabled()) {
				log.info("passing trough page " + i);
			}
			writer.startElement("span", dataNavigator);

			if (model.getCurrentPage() == i) {
				writer.writeAttribute("class", String.format("%s %s",
						innerStyleClass,
						DataNavigatorRenderer.DEFAULT_CURRENT_PAGE_CLASS), null);
			} else if (!JavaUtil.isBlank(innerStyleClass)) {
				writer.writeAttribute("class", innerStyleClass, null);
			}

			if (model.getCurrentPage() == i) {
				writer.writeText(i, null);
			} else {

				ExternalContext ctx = FacesContext.getCurrentInstance()
						.getExternalContext();
				String originalURI = (String) ctx.getRequestMap().get(
						RequestDispatcher.FORWARD_REQUEST_URI);

				if (originalURI != null) {
					HtmlOutputLink link = new HtmlOutputLink();
					UIParameter par = new UIParameter();
					par.setName("page");
					par.setValue(i);
					link.setValue(originalURI);
					UIOutput output= new UIOutput();
					output.setValue(i);
					link.getChildren().add(output);
					link.getChildren().add(par);
					link.encodeBegin(context);
					writer.writeText(i, null);
					link.encodeEnd(context);
					
				} else {
					HtmlOutcomeTargetLink link = new HtmlOutcomeTargetLink();
					UIParameter par = new UIParameter();
					par.setName("page");
					par.setValue(i);
					link.setOutcome(context.getViewRoot().getViewId());
					link.getChildren().add(par);
					link.setIncludeViewParams(true);
					link.setValue(i);
					link.encodeBegin(context);
					link.encodeEnd(context);
				}
			}
			writer.endElement("span");
		}
		writer.endElement("div");

	}
}

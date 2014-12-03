
/**
 * 
 */
package com.canigenus.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.canigenus.common.bean.GenericJsfController;
import com.canigenus.common.model.ListPictorial;
import com.canigenus.common.model.Pictorial;
import com.canigenus.common.service.GenericService;

/**
 * @author ranjarah
 * 
 */

public abstract class AbstractPhotoStreamer {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractPhotoStreamer.class);
	private static StreamedContent defaultFileContent;
	private StreamedContent fileContent;

	public abstract GenericService getCommonService();

	@SuppressWarnings({"unchecked"})
	public StreamedContent getFileContent() {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		String fromFile = externalContext.getRequestParameterMap().get(
				"from_file");
		String fromMemory = externalContext.getRequestParameterMap().get(
				"from_memory");
		String single = externalContext.getRequestParameterMap().get(
				"single");
		String path = externalContext.getRequestParameterMap().get("path");
		String photoId = externalContext.getRequestParameterMap().get(
				"photo_id");
		String className = externalContext.getRequestParameterMap().get(
				"class_name");
		if ("true".equals(fromMemory)) {

			try {
				FacesContext context = FacesContext.getCurrentInstance();
				GenericJsfController<? extends Pictorial> controller =  (GenericJsfController<? extends Pictorial>) context.getApplication().evaluateExpressionGet(context, "#{" + className + "}", Object.class);
				if("Y".equalsIgnoreCase(single))
				{
					return new DefaultStreamedContent(new ByteArrayInputStream(((JPADataModelForPrimeFaces<? extends Pictorial>)controller.getItems()).getResult().get(photoId).getPicture()));
				}
				else{
				return new DefaultStreamedContent(new  ByteArrayInputStream( ((ListPictorial) controller.getCurrent()).getPictures()
						.get(Integer.parseInt(photoId)).getPicture()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (fromFile != null && fromFile.equals("true")) {
			if (path != null && !path.isEmpty()) {
				photoId = path + photoId;
			}
			File file = new File(photoId.toString());
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return new DefaultStreamedContent(inputStream);
		} else {
			logger.trace("Entered method getFileContent.");

			if (photoId == null || photoId.equals("")) {
				fileContent = defaultFileContent;
				logger.info("Id was null or empty. Retrieved default file content.");
			} else {
				long parsedId = Long.parseLong(photoId);
				if (parsedId < 0 || parsedId > Integer.MAX_VALUE) {
					fileContent = defaultFileContent;
					logger.info("Invalid Id. Retrieved default file content.");
				}

				Class<? extends Pictorial> c = null;
				try {
					c = (Class<? extends Pictorial>) Class.forName(className);
				} catch (ClassNotFoundException e) {

					e.printStackTrace();
					throw new RuntimeException(e);
				}

				fileContent = new DefaultStreamedContent(
						new ByteArrayInputStream(getByte(c, parsedId)),
						"image/png");

				logger.info("Retrieved file content for image {}.", parsedId);
			}
			logger.trace("Exited method getFileContent.");
		}
		return fileContent;
	}

	public void setFileContent(StreamedContent fileContent) {
		this.fileContent = fileContent;
	}

	static {
		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream inputStream = contextClassLoader
				.getResourceAsStream("resources/images/Photo - 0.png");
		defaultFileContent = new DefaultStreamedContent(inputStream,
				"image/png");
	}

	public byte[] getByte(Class<? extends Pictorial> z, Long id) {
		return getCommonService().getPartialModel(z, id, "picture")
				.getPicture();
	}

}
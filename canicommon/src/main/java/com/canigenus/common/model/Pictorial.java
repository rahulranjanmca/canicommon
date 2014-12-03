package com.canigenus.common.model;

import java.io.IOException;
import java.io.InputStream;

import org.primefaces.model.StreamedContent;

public interface Pictorial {
	
	public byte[]  getPicture();
	public StreamedContent getImage() throws IOException;
	public InputStream getInputStream();

}

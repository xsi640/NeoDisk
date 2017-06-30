package com.neodisk.commons;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
	public static byte[] toByteArray(InputStream in) throws IOException{
		return org.apache.commons.io.IOUtils.toByteArray(in);
	}
}

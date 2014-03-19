package com.teamclanmatch.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class Zipper {
	
	public String compressString(String srcTxt)throws IOException {
		ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
		GZIPOutputStream zos = new GZIPOutputStream(rstBao);
		zos.write(srcTxt.getBytes());
		zos.close();		     
		byte[] bytes = rstBao.toByteArray();
		return Base64.encodeBase64String(bytes);
	}

	public static String uncompressString(String zippedBase64Str) throws IOException {
		String result = null;
		byte[] bytes = Base64.decodeBase64(zippedBase64Str);
		GZIPInputStream zi = null;
		try {
			zi = new GZIPInputStream(new ByteArrayInputStream(bytes));
			 result = IOUtils.toString(zi);
		} finally {
			zi.close();
		}
		return result;
	 }
	
}

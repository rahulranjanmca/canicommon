package com.canigenus.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("commonJavaUtil")
@ApplicationScoped
public class JavaUtil {

	public static <T> List<T> getListAsSet(Set<T> set) {
		return new ArrayList<T>(set);
	}

	public static boolean isBlank(String o) {
		if (o == null || o.isEmpty()) {
			return true;
		}
		return false;
	}

	public static Date removeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date toTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow = cal.getTime();
		return removeTime(tomorrow);
	}

	public static void copy(InputStream in, File file) throws Exception {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}

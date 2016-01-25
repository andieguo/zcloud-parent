package org.zonesion.hadoop.base.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarUtil {

	private String javaClassPath;
	private String targetPath;

	public JarUtil(String javaClassPath, String targetPath) {
		super();
		this.javaClassPath = javaClassPath;
		this.targetPath = targetPath;
	}

	public void generateJar() throws FileNotFoundException, IOException {

		System.out.println("*** --> 开始生成jar包...");
		String targetDirPath = targetPath.substring(0, targetPath.lastIndexOf(File.separator));
		File targetDir = new File(targetDirPath);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}

		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

		JarOutputStream target = new JarOutputStream(new FileOutputStream(targetPath), manifest);
		writeClassFile(new File(javaClassPath), target);
		target.close();
		System.out.println("*** --> jar包生成完毕。");
	}

	private void writeClassFile(File source, JarOutputStream target) throws IOException {
		BufferedInputStream in = null;
		try {
			if (source.isDirectory()) {
				String name = source.getPath().replace("\\", "/");
				if (!name.isEmpty()) {
					if (!name.endsWith("/")) {
						name += "/";
					}
					name = name.substring(javaClassPath.length());
					if (!name.equals("")) {// fix bug
						JarEntry entry = new JarEntry(name);
						entry.setTime(source.lastModified());
						target.putNextEntry(entry);
						target.closeEntry();
					}
				}
				for (File nestedFile : source.listFiles())
					writeClassFile(nestedFile, target);
				return;
			}

			String middleName = source.getPath().replace("\\", "/").substring(javaClassPath.length());
			if (middleName.endsWith(".class")) {
				JarEntry entry = new JarEntry(middleName);
				entry.setTime(source.lastModified());
				target.putNextEntry(entry);
				in = new BufferedInputStream(new FileInputStream(source));

				byte[] buffer = new byte[1024];
				while (true) {
					int count = in.read(buffer);
					if (count == -1)
						break;
					target.write(buffer, 0, count);
				}
			}
			target.closeEntry();
		} finally {
			if (in != null)
				in.close();
		}
	}

	public static void unJar(File jarFile, File toDir) throws IOException {
		JarFile jar = new JarFile(jarFile);
		try {
			@SuppressWarnings("rawtypes")
			Enumeration entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				if (!entry.isDirectory()) {
					InputStream in = jar.getInputStream(entry);
					try {
						File file = new File(toDir, entry.getName());
						if (!file.getParentFile().mkdirs()) {
							if (!file.getParentFile().isDirectory()) {
								throw new IOException("Mkdirs failed to create " + file.getParentFile().toString());
							}
						}
						OutputStream out = new FileOutputStream(file);
						try {
							byte[] buffer = new byte[8192];
							int i;
							while ((i = in.read(buffer)) != -1) {
								out.write(buffer, 0, i);
							}
						} finally {
							out.close();
						}
					} finally {
						in.close();
					}
				}
			}
		} finally {
			jar.close();
		}
	}

	public static void main(String[] args) {
		testGenJar();
		// testUnJar();
	}

	public static void testUnJar() {
		String targetPath = System.getProperty("user.home") + File.separator + "temp" + File.separator
				+ "education-1.jar";
		File jarFile = new File(targetPath);
		File toDir = new File(System.getProperty("user.home") + File.separator + "temp");
		try {
			JarUtil.unJar(jarFile, toDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testGenJar() {
		try {
			String javaClassPath = "C:/classes/";// E:/workspace/zcloud-education/target/classes/
			String targetPath = System.getProperty("user.home") + File.separator + "temp" + File.separator
					+ "education.jar";
			System.out.println("targetPath:" + targetPath);
			JarUtil util = new JarUtil(javaClassPath, targetPath);
			util.generateJar();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

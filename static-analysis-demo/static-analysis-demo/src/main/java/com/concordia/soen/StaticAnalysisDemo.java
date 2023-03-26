package com.concordia.soen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class StaticAnalysisDemo {
	public static void main(String[] args) {
		for (String fileName : args)
			try {
				System.out.println(read(fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	static String read(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		String src = Files.lines(path).collect(Collectors.joining("\n"));
		return src;
	}
}

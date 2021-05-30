package me.Cutiemango.Nonogram;

import java.io.InputStream;
import java.net.URL;

public class Main
{
	public static void main(String[] args) {
		GameLauncher.main(args);
	}

	public static URL getResource(String name) {
		return Main.class.getResource(name);
	}

	public static InputStream getResourceAsStream(String name) {
		return Main.class.getResourceAsStream(name);
	}
}

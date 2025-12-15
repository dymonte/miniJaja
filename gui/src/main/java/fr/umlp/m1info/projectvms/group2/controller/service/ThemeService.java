package fr.umlp.m1info.projectvms.group2.controller.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import fr.umlp.m1info.projectvms.group2.PopUp;

/**
 * Service responsible for managing application themes.
 */
public class ThemeService {
	private final Scene primaryScene;
	private final String styleFolder;

	/**
	 * Creates a new ThemeService.
	 *
	 * @param primaryScene the main scene to apply stylesheets
	 * @param styleFolder  the folder containing CSS files
	 */
	public ThemeService(Scene primaryScene, String styleFolder) {
		if (primaryScene == null || styleFolder == null) {
			throw new NullPointerException("Arguments cannot be null");
		}
		this.primaryScene = primaryScene;
		this.styleFolder = styleFolder;
	}

	/**
	 * Apply a theme by applying the CSS file to the scene.
	 *
	 * @param cssFileName the CSS file name relative to the style folder
	 */
	public void applyTheme(String cssFileName) {
		primaryScene.getStylesheets().removeIf(url -> url.contains("themes/"));

		URL cssUrl = getClass().getResource(cssFileName);
		if (cssUrl == null) {
			PopUp.info("Style Error", "Unable to find theme: " + cssFileName, "");
			return;
		}
		primaryScene.getStylesheets().add(cssUrl.toExternalForm());
	}

	/**
	 * Loads all available CSS themes from the style folder and populates a menu.
	 *
	 * @param menuThemes the Menu to populate with available themes
	 */
	public void loadThemesMenu(Menu menuThemes) {
		menuThemes.getItems().clear();

		URL themesFolderUrl = getClass().getResource(styleFolder);
		if (themesFolderUrl == null) {
			PopUp.error("Themes folder not found", "Not found", "Error while loading styles");
			return;
		}

		File themesFolder = new File(themesFolderUrl.getPath());
		File[] cssFiles = themesFolder.listFiles((dir, name) -> name.endsWith(".css"));
		if (cssFiles == null) {
			return;
		}

		for (File file : cssFiles) {
			String themeName = extractThemeName(file);
			MenuItem item = new MenuItem(themeName);

			item.setOnAction(e -> applyTheme(styleFolder + file.getName()));
			menuThemes.getItems().add(item);
		}
	}

	/**
	 * Extracts a user-friendly theme name from a CSS file.
	 * Looks for a comment in the first line: /* ThemeName: MyTheme *\/
	 *
	 * @param file the CSS file
	 * @return the theme name or file name as fallback
	 */
	private String extractThemeName(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String firstLine = br.readLine();
			if (firstLine != null && firstLine.contains("ThemeName:")) {
				return firstLine.replace("/*", "")
						.replace("*/", "")
						.replace("ThemeName:", "")
						.trim();
			}
		} catch (Exception ignored) {
		}

		String name = file.getName().replace(".css", "");
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
}

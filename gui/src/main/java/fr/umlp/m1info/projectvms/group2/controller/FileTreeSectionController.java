package fr.umlp.m1info.projectvms.group2.controller;

import fr.umlp.m1info.projectvms.group2.PopUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;

public class FileTreeSectionController {

	@FXML
	private VBox fileTreeContainer;

	@FXML
	private ScrollPane fileTreeScroll;

	@FXML
	private Label headerLabel;

	private TreeView<File> treeView;

	private final Image folderIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("folder.png")));
	private final Image fileIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("file.png")));

	private Consumer<File> onFileOpen;

	public void setOnFileOpen(Consumer<File> callback) {
		this.onFileOpen = callback;
	}

	@FXML
	public void initialize() {
		fileTreeScroll.setStyle("-fx-border-width: 0; -fx-background-insets: 0;");
		// Initialize with user home by default
		openDir(new File(System.getProperty("user.home")));
	}

	public void openDir(File directory) {
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			PopUp.error("Open directory", "Invalid directory", "");
			return;
		}

		TreeItem<File> rootNode = createNode(directory);
		rootNode.setExpanded(true);

		if (treeView == null) {
			treeView = new TreeView<>(rootNode);
			treeView.setShowRoot(true);
			treeView.setCellFactory(param -> new FileTreeCellImpl());
			treeView.setMaxWidth(Double.MAX_VALUE);
			VBox.setVgrow(treeView, javafx.scene.layout.Priority.ALWAYS);
			treeView.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1) {
					TreeItem<File> selected = treeView.getSelectionModel().getSelectedItem();
					if (selected != null && selected.getValue() != null) {
						File file = selected.getValue();
						if (file.isFile() && onFileOpen != null) {
							onFileOpen.accept(file);
						}
					}
				}
			});
			treeView.setStyle("-fx-border-width: 0; -fx-background-insets: 0;");
			fileTreeContainer.getChildren().add(treeView);
		} else {
			treeView.setRoot(rootNode);
		}
	}

	public void openFile(File file) {
		if (file == null || !file.exists()) {
			PopUp.error("Open file", "Invalid file", "");
			return;
		}
		File parentDir = file.getParentFile();
		openDir(parentDir);
		selectFileInTree(file);
	}

	private TreeItem<File> createNode(final File f) {
		return new TreeItem<>(f) {
			private boolean isLeaf;
			private boolean isFirstTimeChildren = true;
			private boolean isFirstTimeLeaf = true;

			@Override
			public ObservableList<TreeItem<File>> getChildren() {
				if (isFirstTimeChildren) {
					isFirstTimeChildren = false;
					super.getChildren().setAll(buildChildren(this));
				}
				return super.getChildren();
			}

			@Override
			public boolean isLeaf() {
				if (isFirstTimeLeaf) {
					isFirstTimeLeaf = false;
					isLeaf = f.isFile();
				}
				return isLeaf;
			}

			private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
				File f = TreeItem.getValue();
				if (f != null & f.isDirectory()) {
					File[] files = f.listFiles(file -> !file.isHidden() && !file.getName().startsWith("."));
					if (files != null && files.length > 0) {
						Arrays.sort(files, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
						ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
						for (File childFile : files) {
							children.add(createNode(childFile));
						}
						return children;
					}
				}
				return FXCollections.emptyObservableList();
			}
		};
	}

	private void selectFileInTree(File targetFile) {
		if (treeView == null || treeView.getRoot() == null) {
			return;
		}
		TreeItem<File> found = findTreeItem(treeView.getRoot(), targetFile);
		if (found != null) {
			treeView.getSelectionModel().select(found);
			treeView.scrollTo(treeView.getRow(found));
		}
	}

	private TreeItem<File> findTreeItem(TreeItem<File> current, File target) {
		if (current.getValue().equals(target)) {
			return current;
		}
		for (TreeItem<File> child : current.getChildren()) {
			TreeItem<File> found = findTreeItem(child, target);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	private final class FileTreeCellImpl extends TreeCell<File> {

		private TextField textField;

		public FileTreeCellImpl() {
		}

		@Override
		protected void updateItem(File item, boolean empty) {
			super.updateItem(item, empty);

			if (empty || item == null) {
				setText(null);
				setGraphic(null);
				setContextMenu(null);
			} else {
				if (isEditing()) {
					if (textField != null)
						textField.setText(getItemDisplayName());
					setGraphic(textField);
					setText(null);
				} else {
					setText(getItemDisplayName());
					ImageView icon = new ImageView(item.isDirectory() ? folderIcon : fileIcon);
					icon.setFitHeight(16);
					icon.setFitWidth(16);
					setGraphic(icon);
				}
			}
		}

		private String getItemDisplayName() {
			File f = getItem();
			return f == null ? "" : f.getName().isEmpty() ? f.getAbsolutePath() : f.getName();
		}
	}
}

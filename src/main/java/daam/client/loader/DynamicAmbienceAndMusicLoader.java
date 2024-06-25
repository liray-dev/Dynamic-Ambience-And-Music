package daam.client.loader;

import daam.DAAM;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class DynamicAmbienceAndMusicLoader extends JFrame {

    private final JFileChooser fileChooser;
    private final ArrayList<File> selectedFiles = new ArrayList<>();
    private final JPanel filePanel;

    public DynamicAmbienceAndMusicLoader() {
        super("Dynamic Ambience And Music Loader v1.0.0");
        this.setSize(500, 500);
        this.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JButton selectFileButton = new JButton("Select a files .ogg");
        selectFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(selectFileButton);
        this.add(topPanel, BorderLayout.NORTH);

        filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(filePanel);
        scrollPane.setPreferredSize(new Dimension(380, 300));
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton loadButton = new JButton("Load");
        bottomPanel.add(loadButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".ogg");
            }

            @Override
            public String getDescription() {
                return "OGG Files (*.ogg)";
            }
        });

        selectFileButton.addActionListener(e -> {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles();
                for (File file : files) {
                    selectedFiles.add(file);
                    JLabel fileLabel = new JLabel(file.getAbsolutePath());
                    filePanel.add(fileLabel);
                }
                filePanel.revalidate();
                filePanel.repaint();
            }
        });

        loadButton.addActionListener(e -> load(selectedFiles));
    }

    private void load(ArrayList<File> files) {
        for (File file : files) {
            String name = file.getName().replace(".ogg", "").toLowerCase();
            Path destPath = DAAM.SOUNDS_DIR.toPath().resolve(name + ".ogg");
            try {
                Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                SoundLoader.add(name);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error with file: " + file.getName(), "Dynamic Ambience And Music Loader v1.0.0", JOptionPane.INFORMATION_MESSAGE);
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Successfully loaded!", "Dynamic Ambience And Music Loader v1.0.0", JOptionPane.INFORMATION_MESSAGE);
    }

}

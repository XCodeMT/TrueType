package truetype;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Silas
 */
public class FileView extends JPanel {
    private JTextArea edit = new JTextArea();
    private Font editFont = new Font("Monaco", Font.PLAIN, 12);
    private String path = null;
    private File file = null;
    private JScrollPane scrollPane;
    private FileDialog fileLoader;
    private FileDialog fileSaver;
    
    /**
     * create a basic FileView
     */
    public FileView()
    {
        fileLoader = new FileDialog(new Frame(), "Open...", FileDialog.LOAD);
        fileLoader.setDirectory("~");
        fileSaver = new FileDialog(new Frame(), "Save...", FileDialog.SAVE);
        fileSaver.setDirectory("~");
        setLayout(new GridLayout(1, 1));
        edit.setFont(editFont);
        scrollPane = new JScrollPane(edit);
        add(scrollPane);
    }
    
    /**
     * returns the name of the file being edited
     * @return  the name of the file
     */
    public String getFileName()
    {
        return file.getName();
    }
    
    /**
     * sets the text to the of the editor from the text of the file
     */
    public void setText()
    {
        try {
            setText(FileUtils.readFileToString(file));
        } catch (IOException ex) {
        }
    }
    
    /**
     * returns the text area
     * @return the text area
     */
    public JTextArea getTextArea()
    {
        return edit;
    }
    
    /**
     * returns the text currently in the text area
     * @return
     */
    public String getText()
    {
        return edit.getText();
    }
    
    /**
     * returns the path to the file
     * @return the path
     */
    public String getPath()
    {
        return path;
    }
    
    /**
     * returns the file
     * @return the file
     */
    public File getFile()
    {
        return file;
    }
    
    /**
     * returns the scroll pane
     * @return the scroll pane
     */
    public JScrollPane getScrollPane()
    {
        return scrollPane;
    }
    
    /**
     * sets the text of the text area
     * @param text - the text to set
     */
    public void setText(String text)
    {
        edit.setText(text);
    }
    
    /**
     * sets the font of the text area
     * @param font - the font to set
     */
    public void setEditorFont(Font font)
    {
        edit.setFont(font);
    }
    
    /**
     * sets the path to the file
     * @param path - the new path
     */
    public void setPath(String path)
    {
        this.path = path;
        setFile();
    }
    
    /**
     * sets the file to the current path
     */
    public void setFile()
    {
        setFile(new File(path));
    }
    
    /**
     * sets the file to the given file
     * @param file - the new file
     */
    public void setFile(File file)
    {
        this.file = file;
        if (!path.equals(file.getAbsolutePath()))
        {
            setPath(file.getAbsolutePath());
        }
    }
    
    public void saveChoose()
    {
        
    }
    
    /**
     * saves the current text to the current file
     */
    public void save()
    {
        if (path == null || file == null)
        {
            saveChoose();
        }
        try {
            FileUtils.writeStringToFile(file, getText());
        } catch (IOException ex) {
        }
    }
}
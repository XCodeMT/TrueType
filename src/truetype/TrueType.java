package truetype;

import com.XCodeMT.chromeTabs.DefaultTab;
import com.XCodeMT.chromeTabs.DefaultTabFactory;
import com.XCodeMT.chromeTabs.ITabFactory;
import com.XCodeMT.chromeTabs.TabbedPane;
import com.XCodeMT.chromeTabs.jhrome.JhromeTabUI;
import com.apple.eawt.*;
import com.apple.eawt.AppEvent.OpenFilesEvent;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Silas
 */
public class TrueType extends JFrame implements ActionListener {
    Application application = new Application();
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem open, save, newFile, saveAs, newWindow;
    JhromeTabUI ui = new JhromeTabUI();
    TabbedPane myTabs;
    DefaultTab myTab;
    ITabFactory tabFactory = new DefaultTabFactory();
    FileDialog fileLoader;
    FileDialog fileSaver;
    
    public TrueType()
    {
        fileLoader = new FileDialog(new Frame(), "Open...", FileDialog.LOAD);
        fileLoader.setDirectory("~");
        
        fileSaver = new FileDialog(new Frame(), "Save...", FileDialog.SAVE);
        fileSaver.setDirectory("~");
        
        //set up the menu bar
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        newFile = new JMenuItem("New");
        saveAs = new JMenuItem("Save As");
        newWindow = new JMenuItem("New Window");
        
        open.setActionCommand("open");
        save.setActionCommand("save");
        newFile.setActionCommand("new");
        saveAs.setActionCommand("saveas");
        newWindow.setActionCommand("window");
        
        open.addActionListener(this);
        save.addActionListener(this);
        newFile.addActionListener(this);
        saveAs.addActionListener(this);
        newWindow.addActionListener(this);
        
        menu.add(newFile);
        menu.add(open);
        menu.add(save);
        menu.add(saveAs);
        //menu.add(newWindow);
        
        menuBar.add(menu);
        setJMenuBar(menuBar);
        enableOSXFullscreen((Window)this);
        application.setOpenFileHandler(new OpenFilesHandler() {
            @Override
            public void openFiles(OpenFilesEvent ofe) {
                
            }
        });
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
public static void enableOSXFullscreen(Window window) {
    try {
        Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
        Class params[] = new Class[]{Window.class, Boolean.TYPE};
        Method method = util.getMethod("setWindowCanFullScreen", params);
        method.invoke(util, window, true);
    } catch (ClassNotFoundException e1) {
    } catch (Exception e) {
    }
}
    
    public void open()
    {
        FileView content = new FileView();
        fileLoader.setLocation(50, 50);
        fileLoader.setVisible(true);
        content.setPath(fileLoader.getDirectory() + fileLoader.getFile());
        if (content.getPath() != null)
        {
            content.setFile();
            content.setText();
            DefaultTab newTab = new DefaultTab(
                    content.getFileName(), content);
            myTabs.addTab(myTabs.getTabCount(), newTab);
            myTabs.setSelectedTab(newTab);
        }
    }
    
    public FileView getCurrentContent()
    {
        return (FileView)(((DefaultTab)myTabs.getSelectedTab()).getContent());
    }
    
    public void saveAs(FileView content)
    {
        saveChoose(content);
        save(content);
    }
    
    public void save(FileView content)
    {
        if (content.getPath() == null || content.getFile() == null)
        {
            saveChoose(content);
        }
        if (content.getPath() != null && content.getFile() != null)
            {
            try {
                FileUtils.writeStringToFile(content.getFile(), content.getText());
            } catch (IOException ex) {
            }
            ((DefaultTab)myTabs.getSelectedTab())
                    .setTitle(content.getFileName());
        }
    }
    
    public void saveChoose(FileView content)
    {
        fileSaver.setLocation(50, 50);
        fileSaver.setVisible(true);
        if(fileSaver.getFile() != null)
        {
            content.setPath(fileSaver.getDirectory() + fileSaver.getFile());
            content.setFile();
        }
    }
    
    public void setActionListener()
    {
        myTabs.getNewTabButton().addActionListener(this);
    }
    
    public void newFile()
    {
        DefaultTab newTab = new DefaultTab(
                "New...", new FileView());
        myTabs.addTab(myTabs.getTabCount(), newTab);
        myTabs.setSelectedTab(newTab);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("new")) {
            newFile();
        } else if (e.getActionCommand().equals("open")) {
            open();
        } else if (e.getActionCommand().equals("save")) {
            save(getCurrentContent());
        } else if (e.getActionCommand().equals("saveas")) {
            saveAs(getCurrentContent());
        }
    }
    
    public void newFromString(String path)
    {
        FileView newView = new FileView();
        newView.setPath(path);
        newView.setFile();
        newView.setText();
        ((DefaultTab)myTabs.getSelectedTab()).setTitle(newView.getFileName());
        ((DefaultTab)myTabs.getSelectedTab()).setContent(newView);
    }
    
    public void runner()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                //set up the frame
                myTabs = new TabbedPane();
                myTab = new DefaultTab("New...", new FileView());
                
                //remove the new button actionlistener
                ActionListener[] listeners = myTabs.getNewTabButton()
                        .getActionListeners();
                for (int i = 0; i < listeners.length; i++)
                {
                    myTabs.getNewTabButton()
                            .removeActionListener(listeners[i]);
                }
                
                myTabs.getNewTabButton().setActionCommand("new");
                setActionListener();
                
                //set up the tab pane
                myTabs.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
                
                //set up the first tab
                myTabs.addTab(myTab);
                myTabs.setSelectedTab(myTab);
                
                //set up the frame
                add(myTabs);
                setSize(500, 500);
                setVisible(true);
            }
        });
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        new TrueType().runner();
    }
}

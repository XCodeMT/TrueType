/*
 * Copyright (C) 2013 Silas Schwarz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package truetype;

import com.XCodeMT.chromeTabs.ITab;
import com.XCodeMT.chromeTabs.ITabbedPaneWindow;
import com.XCodeMT.chromeTabs.ITabbedPaneWindowFactory;
import com.apple.eawt.AppEvent.OpenFilesEvent;
import com.apple.eawt.Application;
import com.apple.eawt.OpenFilesHandler;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author XCodeMT
 */
public class TrueType implements Runnable {
    OpenFilesHandler openHandler;
    Application app = new Application();
    ITabbedPaneWindowFactory windowFactory;
    static JFrame preferencesFrame;
    
    public TrueType() {
        loadPreferences();
        openHandler = new OpenFilesHandler() {

            @Override
            public void openFiles(final OpenFilesEvent ofe) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        windowFactory = new TTabbedPaneWindowFactory();
                        ITabbedPaneWindow window = windowFactory.createWindow();
                        for (File file : ofe.getFiles()) {
                            ITab newTab = window.getTabbedPane().getTabFactory().createTab(file.getName());
                            FileView newContent = new FileView(newTab, (TTabbedPaneWindow)window);
                            newContent.setFile(file);
                            newContent.update();
                            window.getTabbedPane().addTab(newTab);
                            window.getTabbedPane().setSelectedTab(newTab);
                        }
                        window.getWindow().setSize(500, 500);
                        window.getWindow().setVisible(true);
                        System.out.println("openFiles called");
                    }
                    
                });
            }
        };
        app.setOpenFileHandler(openHandler);
    }
    
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        SwingUtilities.invokeLater(new TrueType());
    }

    @Override
    public void run() {
        windowFactory = new TTabbedPaneWindowFactory();
        ITabbedPaneWindow window = windowFactory.createWindow();
        ITab tab = window.getTabbedPane().getTabFactory().createTab("New...");
        new FileView(tab, (TTabbedPaneWindow)window);
        window.getTabbedPane().addTab(tab);
        window.getTabbedPane().setSelectedTab(tab);
        window.getWindow().setSize(500, 500);
        window.getWindow().setVisible(true);
    }
    
    public void loadPreferences() {
        File trueTypeSupport = new File(System.getProperty("user.home") + "/Library/Application Support/TrueType");
        File trueTypeBundle = new File(System.getProperty("user.home") + "/Library/Application Support/TrueType/Preferences.ttbundle");
        try{
            if(trueTypeSupport.mkdir()) { 
                System.out.println("TrueType Support Directory Created");
            } else {
                System.out.println("Directory is not created");
            }
            if(trueTypeBundle.mkdir()) { 
                System.out.println("TrueType Preferences Bundle Created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch(Exception e){
            e.printStackTrace();
        } 
    }
}


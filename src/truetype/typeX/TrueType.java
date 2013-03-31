/*
 * This file is part of TrueType.
 * 
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

package truetype.typeX;

import com.apple.eawt.AppEvent.OpenFilesEvent;
import com.apple.eawt.Application;
import com.apple.eawt.OpenFilesHandler;
import com.xcodemt.tabs.ITab;
import com.xcodemt.tabs.XTab;
import com.xcodemt.tabs.XTabbedPaneWindow;
import java.awt.Window;
import java.io.File;
import javaX.OSUtils;
import javax.swing.SwingUtilities;

/**
 *
 * @author XCodeMT
 */
public class TrueType implements Runnable {
    Application app = Application.getApplication();
    XTabbedPaneWindow activeWindow;
    TTabbedPaneWindowFactory windowFactory = new TTabbedPaneWindowFactory(this);
    
    public static void main(String[] args) {
        TrueType thisApp = new TrueType();
        if (OSUtils.isOSX()) {
            thisApp.app.setOpenFileHandler(thisApp.openHandler);
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        SwingUtilities.invokeLater(new TrueType());
    }

    @Override
    public void run() {
        TTabbedPaneWindow window = (TTabbedPaneWindow)windowFactory.createWindow();
        XTab tab = (XTab) window.getTabbedPane().getTabFactory().createTab();
        window.getTabbedPane().addTab(tab);
        window.getTabbedPane().setSelectedTab(tab);
        window.setSize(500, 500);
        window.setVisible(true);
    }
    
    public void setActiveWindow(Window window) {
        activeWindow = (XTabbedPaneWindow)window;
    }
    
    private void open(File file) {
        System.out.println("Opening!");
        if (activeWindow != null) {
            if (((TFileView)activeWindow.getTabbedPane().getSelectedTab().getContent()).isNew()) {
                ((TFileView)activeWindow.getTabbedPane().getSelectedTab().getContent()).setFile(file);
            } else {
                ITab tab = activeWindow.getTabbedPane().getTabFactory().createTab();
                ((TFileView)tab.getContent()).setFile(file);
                activeWindow.getTabbedPane().addTab(tab);
                activeWindow.getTabbedPane().setSelectedTab(tab);
            }
        } else {
            TTabbedPaneWindow window = (TTabbedPaneWindow) windowFactory.createWindow();
            ITab tab = window.getTabbedPane().getTabFactory().createTab();
            window.getTabbedPane().addTab(tab);
            window.getTabbedPane().setSelectedTab(tab);
            ((TFileView)tab.getContent()).setFile(file);
            window.setSize(500, 500);
            window.setVisible(true);
        }
    }
    
    public OpenFilesHandler openHandler = new OpenFilesHandler() {

        @Override
        public void openFiles(OpenFilesEvent ofe) {
            for (File file : ofe.getFiles()) {
                open(file);
            }
        }
        
    };
}

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

import awtX.WindowUtils;
import com.XCodeMT.chromeTabs.ITab;
import com.XCodeMT.chromeTabs.ITabbedPaneWindow;
import com.XCodeMT.chromeTabs.ITabbedPaneWindowFactory;
import com.XCodeMT.chromeTabs.TabbedPane;
import com.XCodeMT.chromeTabs.tabsX.XTabFactory;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author XCodeMT
 */
public class TTabbedPaneWindowFactory implements ITabbedPaneWindowFactory {
    private MyWindowCloseListener closeListener = new MyWindowCloseListener();

    @Override
    public ITabbedPaneWindow createWindow() {
        TTabbedPaneWindow tabbedPaneWindow = new TTabbedPaneWindow(this);
        TTabbedPaneListener listener = new TTabbedPaneListener(tabbedPaneWindow);
        tabbedPaneWindow.getTabbedPane().setTabFactory(new XTabFactory());
        tabbedPaneWindow.getTabbedPane().setNewTabActionListener(new NewTabListener(tabbedPaneWindow.getTabbedPane(), tabbedPaneWindow));
        WindowUtils.enableOSXFullscreen(tabbedPaneWindow.getWindow());
        tabbedPaneWindow.getWindow().addWindowListener(closeListener);
        closeListener.add();
        return tabbedPaneWindow;
    }
    
    class MyWindowCloseListener implements WindowListener {
        private int windowCount;

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            windowCount--;
            if (windowCount == 0) {
                System.exit(0);
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }

        private void add() {
            windowCount++;
        }
        
    }

    class NewTabListener implements ActionListener {

        TabbedPane tabbedPane;
        TTabbedPaneWindow window;

        NewTabListener(TabbedPane tabbedPane, ITabbedPaneWindow window) {
            this.tabbedPane = tabbedPane;
            this.window = (TTabbedPaneWindow) window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (tabbedPane.getTabFactory() == null) {
                return;
            }
            ITab newTab = tabbedPane.getTabFactory().createTab("New...");
            newTab.setContent(new FileView(newTab, window));
            tabbedPane.addTab(tabbedPane.getTabCount(), newTab);
            tabbedPane.setSelectedTab(newTab);
        }
    }

}

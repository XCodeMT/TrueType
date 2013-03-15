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

package truetype.typeX;

import awtX.WindowUtils;
import com.XCodeMT.chromeTabs.ITab;
import com.XCodeMT.chromeTabs.ITabbedPaneWindow;
import com.XCodeMT.chromeTabs.ITabbedPaneWindowFactory;
import com.XCodeMT.chromeTabs.TabbedPane;
import com.XCodeMT.chromeTabs.tabsX.XTabFactory;
import com.XCodeMT.chromeTabs.tabsX.XTabbedPaneWindow;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Method;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author XCodeMT
 */
public class TTabbedPaneWindowFactory implements ITabbedPaneWindowFactory {
    private MyWindowCloseListener closeListener = new MyWindowCloseListener();

    @Override
    public ITabbedPaneWindow createWindow() {
        ITabbedPaneWindow tabbedPaneWindow = new XTabbedPaneWindow();
        tabbedPaneWindow.getTabbedPane().setTabFactory(new XTabFactory());
        tabbedPaneWindow.getTabbedPane().setWindowFactory(this);
        tabbedPaneWindow.getTabbedPane().setNewTabActionListener(new NewTabListener(tabbedPaneWindow.getTabbedPane()));
        ((JFrame)tabbedPaneWindow).setJMenuBar(createMenuBar(tabbedPaneWindow.getTabbedPane(), this));
        WindowUtils.enableOSXFullscreen(tabbedPaneWindow.getWindow());
        tabbedPaneWindow.getWindow().addWindowListener(closeListener);
        closeListener.add();
        return tabbedPaneWindow;
    }
    
    private JMenuBar createMenuBar(final TabbedPane pane, final TTabbedPaneWindowFactory windowFactory) {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem newTab = new JMenuItem("New Tab");
        JMenuItem newWindow = new JMenuItem("New Window");
        
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");

        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((FileView) pane.getSelectedTab().getContent()).open();
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((FileView) pane.getSelectedTab().getContent()).save();
            }
        });
        newTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pane.getTabFactory() == null) {
                    return;
                }
                ITab newTab = pane.getTabFactory().createTab("New...");
                newTab.setContent(new FileView(newTab));
                pane.addTab(pane.getTabCount(), newTab);
                pane.setSelectedTab(newTab);
            }
        });
        newWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            ITabbedPaneWindow window = windowFactory.createWindow();
            ITab tab = window.getTabbedPane().getTabFactory().createTab("New...");
            new FileView(tab);
            window.getTabbedPane().addTab(tab);
            window.getTabbedPane().setSelectedTab(tab);
            window.getWindow().setSize(500, 500);
            window.getWindow().setVisible(true);
            }
        });
        
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((FileView) pane.getSelectedTab().getContent()).undo();
            }
        });
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((FileView) pane.getSelectedTab().getContent()).redo();
            }
        });

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(newTab);
        fileMenu.add(newWindow);
        
        editMenu.add(undo);
        editMenu.add(redo);

        bar.add(fileMenu);
        bar.add(editMenu);

        return bar;

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

        NewTabListener(TabbedPane tabbedPane) {
            this.tabbedPane = tabbedPane;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (tabbedPane.getTabFactory() == null) {
                return;
            }
            ITab newTab = tabbedPane.getTabFactory().createTab("New...");
            newTab.setContent(new FileView(newTab));
            tabbedPane.addTab(tabbedPane.getTabCount(), newTab);
            tabbedPane.setSelectedTab(newTab);
        }
    }

}

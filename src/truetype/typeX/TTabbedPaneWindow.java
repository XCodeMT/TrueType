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

import com.xcodemt.tabs.ITab;
import com.xcodemt.tabs.TabbedPane;
import com.xcodemt.tabs.XTab;
import com.xcodemt.tabs.XTabbedPaneWindow;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 *
 * @author XCodeMT
 */
public class TTabbedPaneWindow extends XTabbedPaneWindow {

    JMenuBar bar;
    JMenu fileMenu;
    JMenuItem open;
    JMenuItem save;
    JMenuItem newTab;
    JMenuItem newWindow;
    JMenu editMenu;
    UndoAction undoAction = new UndoAction();
    RedoAction redoAction = new RedoAction();

    public TTabbedPaneWindow(TTabbedPaneWindowFactory factory) {
        super();
        getTabbedPane().setWindowFactory(factory);
        setJMenuBar(createMenuBar(getTabbedPane(), factory));
    }
    
    public UndoAction getUndoAction() {
        return undoAction;
    }
    
    public RedoAction getRedoAction() {
        return redoAction;
    }

    private JMenuBar createMenuBar(final TabbedPane pane, final TTabbedPaneWindowFactory windowFactory) {
        bar = new JMenuBar();
        fileMenu = new JMenu("File");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        newTab = new JMenuItem("New Tab");
        newWindow = new JMenuItem("New Window");

        editMenu = new JMenu("Edit");

        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        redoAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        undoAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        editMenu.add(undoAction);
        editMenu.add(redoAction);

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (((TFileView)getTabbedPane().getSelectedTab().getContent()).isNew()) {
                    ((TFileView)getTabbedPane().getSelectedTab().getContent()).open();
                } else {
                    ITab tab = getTabbedPane().getTabFactory().createTab();
                    getTabbedPane().addTab(tab);
                    getTabbedPane().setSelectedTab(tab);
                }
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((TFileView) pane.getSelectedTab().getContent()).save();
            }
        });
        newTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pane.getTabFactory() == null) {
                    return;
                }
                ITab newTab = pane.getTabFactory().createTab();
                pane.addTab(pane.getTabCount(), newTab);
                pane.setSelectedTab(newTab);
            }
        });
        newWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TTabbedPaneWindow window = (TTabbedPaneWindow) windowFactory.createWindow();
                ITab tab = window.getTabbedPane().getTabFactory().createTab();
                window.getTabbedPane().addTab(tab);
                window.getTabbedPane().setSelectedTab(tab);
                window.getWindow().setSize(500, 500);
                window.getWindow().setVisible(true);
            }
        });

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(newTab);
        fileMenu.add(newWindow);

        bar.add(fileMenu);
        bar.add(editMenu);

        return bar;

    }

    class UndoAction extends JMenuItem implements ActionListener {

        public UndoAction() {
            super("Undo");
            setEnabled(false);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ((TFileView)getTabbedPane().getSelectedTab().getContent()).getUndo().undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (((TFileView)getTabbedPane().getSelectedTab().getContent()).getUndo().canUndo()) {
                setEnabled(true);
                //putValue(Action.NAME, ((FileView)getTabbedPane().getSelectedTab().getContent()).getUndo().getUndoPresentationName());
            } else {
                setEnabled(false);
                //putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends JMenuItem implements ActionListener {

        public RedoAction() {
            super("Redo");
            setEnabled(false);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ((TFileView)getTabbedPane().getSelectedTab().getContent()).getUndo().redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if (((TFileView)getTabbedPane().getSelectedTab().getContent()).getUndo().canRedo()) {
                setEnabled(true);
                //putValue(Action.NAME, ((FileView)getTabbedPane().getSelectedTab().getContent()).getUndo().getRedoPresentationName());
            } else {
                setEnabled(false);
                //putValue(Action.NAME, "Redo");
            }
        }
    }
    
    public void refreshUndo() {
        undoAction.update();
        redoAction.update();
    }
}

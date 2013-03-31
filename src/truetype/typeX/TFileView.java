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

import awtX.XFileChooser;
import com.xcodemt.tabs.XTab;
import com.xcodemt.tabs.XTabbedPaneWindow;
import com.xcodemt.tabs.jhrome.JhromeTabBorderAttributes;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileView;
import javax.swing.undo.UndoManager;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author XCodeMT
 */
public class TFileView extends JPanel {
    
    private JTextArea textView;
    private JScrollPane scroller;
    private Font monoco = new Font("Monaco", 0, 13);
    private File file;
    
    private XFileChooser fileChooser = new XFileChooser();
    
    private JFileChooser iconGetter = new JFileChooser();
    
    private XTab tab;
    private TTabbedPaneWindow window;
    
    private boolean isNew;
    
    private UndoManager undo = new UndoManager();
    
    UndoManager getUndo() {
        return undo;
    }
    
    public TFileView(XTab tab, TTabbedPaneWindow window) {
        super(new GridLayout(1, 1));
        this.tab = tab;
        this.window = window;
        isNew = true;
        textView = new JTextArea();
        textView.setFont(monoco);
        textView.setBackground(JhromeTabBorderAttributes.SELECTED_BORDER.bottomColor);
        textView.getDocument().addUndoableEditListener(new TextEditListener());
        scroller = new JScrollPane(textView);
        scroller.setBorder(BorderFactory.createEmptyBorder());
        add(scroller);
    }
    
    public TFileView(XTab tab, TTabbedPaneWindow window, File file) {
        super(new GridLayout(1, 1));
        this.tab = tab;
        this.window = window;
        this.file = file;
        isNew = false;
    }
    
    public void setFontSize(int size) {
        monoco = new Font("Monoco", Font.PLAIN, 12);
    }
    
    public JTextArea getTextArea() {
        return textView;
    }
    
    public void setTextArea(JTextArea textView) {
        this.textView = textView;
    }
    
    public void open() {
        if (loadOpen()) {
            try {
                textView.setText(FileUtils.readFileToString(file));
            } catch (IOException ex) {
                Logger.getLogger(TFileView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void save() {
        if (file == null) {
            loadSave();
        }
        if (file != null) {
            try {
                FileUtils.writeStringToFile(file, textView.getText());
            } catch (IOException ex) {
                Logger.getLogger(TFileView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void saveAs() {
        loadSave();
        if (file != null) {
            try {
                FileUtils.writeStringToFile(file, textView.getText());
            } catch (IOException ex) {
                Logger.getLogger(TFileView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean loadOpen() {
        if (fileChooser.showOpenDialog()) {
            file = fileChooser.getFile();
            update();
            return true;
        }
        return false;
    }

    private void loadSave() {
        if(fileChooser.showSaveDialog()) {
            file = fileChooser.getFile();
            update();
        }
    }
    
    public boolean isNew() {
        return isNew;
    }
    
    public void update () {
        Icon icon = (Icon)iconGetter.getIcon(file);
        tab.getLabel().setIcon(icon);
        tab.getLabel().setText(file.getName());
    }
    
    protected class TextEditListener implements UndoableEditListener {
        
        @Override
        public void undoableEditHappened(UndoableEditEvent e) {
            undo.addEdit(e.getEdit());
            window.getUndoAction().update();
            window.getRedoAction().update();
        }
        
    }
    
    public void setFile(File file) {
        this.file = file;
        try {
            textView.setText(FileUtils.readFileToString(file));
        } catch (IOException ex) {
            Logger.getLogger(TFileView.class.getName()).log(Level.SEVERE, null, ex);
        }
        update();
    }

}

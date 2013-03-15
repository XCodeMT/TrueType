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

import com.XCodeMT.chromeTabs.ITab;
import com.XCodeMT.chromeTabs.ITabbedPaneWindow;
import com.XCodeMT.chromeTabs.ITabbedPaneWindowFactory;
import javax.swing.SwingUtilities;

/**
 *
 * @author XCodeMT
 */
public class TrueType implements Runnable {
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        SwingUtilities.invokeLater(new TrueType());
    }

    @Override
    public void run() {
        ITabbedPaneWindowFactory windowFactory = new TTabbedPaneWindowFactory();
        ITabbedPaneWindow window = windowFactory.createWindow();
        ITab tab = window.getTabbedPane().getTabFactory().createTab("New...");
        new FileView(tab);
        window.getTabbedPane().addTab(tab);
        window.getTabbedPane().setSelectedTab(tab);
        window.getWindow().setSize(500, 500);
        window.getWindow().setVisible(true);
    }

}


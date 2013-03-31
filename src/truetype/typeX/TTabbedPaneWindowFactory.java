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

import com.xcodemt.tabs.ITabbedPaneWindow;
import com.xcodemt.tabs.ITabbedPaneWindowFactory;
import com.xcodemt.tabs.XTabbedPaneWindow;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import swingX.AquaFrame;

/**
 *
 * @author XCodeMT
 */
public class TTabbedPaneWindowFactory implements ITabbedPaneWindowFactory {
    WindowCloseListener listener = new WindowCloseListener();
    ActiveWindowListener activeListener = new ActiveWindowListener();
    TrueType app;
    
    public TTabbedPaneWindowFactory(TrueType app) {
        this.app = app;
    }

    @Override
    public ITabbedPaneWindow createWindow() {
        TTabbedPaneWindow frame = new TTabbedPaneWindow(this);
        AquaFrame.toAqua(frame);
        frame.getTabbedPane().setWindowFactory(this);
        frame.setDefaultCloseOperation(XTabbedPaneWindow.EXIT_ON_CLOSE);
        frame.getTabbedPane().setTabFactory(new TTabFactory(frame));
        frame.addWindowListener(listener);
        frame.addWindowListener(activeListener);
        listener.add();
        return frame;
    }

    class WindowCloseListener extends WindowAdapter {
        private int windowCount;

        @Override
        public void windowClosing(WindowEvent e) {
            windowCount--;
            if (windowCount == 0) {
                System.exit(0);
            }
        }

        private void add() {
            windowCount++;
        }
        
    }
    
    class ActiveWindowListener extends WindowAdapter {
        @Override
        public void windowActivated(WindowEvent e) {
            app.setActiveWindow(e.getWindow());
        }
    }
    
}

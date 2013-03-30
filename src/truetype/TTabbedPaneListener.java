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

import com.XCodeMT.chromeTabs.event.ITabbedPaneListener;
import com.XCodeMT.chromeTabs.event.TabAddedEvent;
import com.XCodeMT.chromeTabs.event.TabSelectedEvent;
import com.XCodeMT.chromeTabs.event.TabbedPaneEvent;

/**
 *
 * @author XCodeMT
 */
public class TTabbedPaneListener implements ITabbedPaneListener {
    TTabbedPaneWindow window;
    
    public TTabbedPaneListener(TTabbedPaneWindow window) {
        this.window = window;
        window.getTabbedPane().addTabbedPaneListener(this);
    }

    @Override
    public void onEvent(TabbedPaneEvent event) {
        if (event instanceof TabSelectedEvent) {
            window.refreshUndo();
            System.out.println("Undo Updated.");
        }
    }

}

/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.selection;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.common.BasicBrowserToolStrip;
import fr.insalyon.creatis.vip.datamanager.client.view.util.BrowserUtil;

/**
 *
 * @author Rafael Silva
 */
public class PathSelectionWindow extends Window {

    private BasicBrowserToolStrip toolStrip;
    private ListGrid grid;
    private ModalWindow modal;
    private TextItem textItem;
    private Menu contextMenu;
    private String name;

    public PathSelectionWindow(TextItem textItem) {

        this.textItem = textItem;

        this.setTitle("Path Selection");
        this.setWidth(550);
        this.setHeight(350);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        grid = BrowserUtil.getListGrid();
        toolStrip = new BasicBrowserToolStrip(modal);
        modal = new ModalWindow(grid);
        
        configureGrid();
        configureToolStrip();
        configureContextMenu();

        this.addItem(toolStrip);
        this.addItem(grid);

        BrowserUtil.loadData(modal, grid, toolStrip, DataManagerConstants.ROOT, false);
    }

    private void configureGrid() {
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            public void onCellDoubleClick(CellDoubleClickEvent event) {
                String type = event.getRecord().getAttributeAsString("icon");
                String name = event.getRecord().getAttributeAsString("name");

                if (type.contains("folder")) {
                    BrowserUtil.loadData(modal, grid, toolStrip,
                            toolStrip.getPath() + "/" + name, false);
                } else {
                    textItem.setValue(toolStrip.getPath() + "/" + name);
                    destroy();
                }
            }
        });
        grid.addCellContextClickHandler(new CellContextClickHandler() {

            public void onCellContextClick(CellContextClickEvent event) {
                event.cancel();
                name = event.getRecord().getAttributeAsString("name");
                contextMenu.showContextMenu();
            }
        });
    }
    
    private void configureToolStrip() {

        ToolStripButton folderUpButton = new ToolStripButton();
        folderUpButton.setIcon("icon-folderup.png");
        folderUpButton.setPrompt("Folder up");
        folderUpButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (!toolStrip.getPath().equals(DataManagerConstants.ROOT)) {
                    String newPath = toolStrip.getPath();
                    BrowserUtil.loadData(modal, grid, toolStrip, 
                            newPath.substring(0, newPath.lastIndexOf("/")), false);
                }
            }
        });
        toolStrip.addButton(folderUpButton);
        
        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon("icon-refresh.png");
        refreshButton.setPrompt("Refresh");
        refreshButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                BrowserUtil.loadData(modal, grid, toolStrip, toolStrip.getPath(), true);
            }
        });
        toolStrip.addButton(refreshButton);

        ToolStripButton homeButton = new ToolStripButton();
        homeButton.setIcon("icon-home.png");
        homeButton.setPrompt("Home");
        homeButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                BrowserUtil.loadData(modal, grid, toolStrip, DataManagerConstants.ROOT, false);
            }
        });
        toolStrip.addButton(homeButton);

    }

    private void configureContextMenu() {
        
        contextMenu = new Menu();
        contextMenu.setShowShadow(true);
        contextMenu.setShadowDepth(10);
        contextMenu.setWidth(90);

        MenuItem selectItem = new MenuItem("Select this path");
        selectItem.setIcon("icon-select.png");
        selectItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                textItem.setValue(toolStrip.getPath() + "/" + name);
                destroy();
            }
        });
        contextMenu.setItems(selectItem);
    }
}
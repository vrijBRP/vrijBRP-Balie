/*
 * Copyright 2021 - 2022 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.tree;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.tree.ZaakTableContainer.AANTAL;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionType.DATABASE;
import static nl.procura.vaadin.component.container.ProcuraContainer.OMSCHRIJVING;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TreeTable;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.SubModuleZaken;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.commons.core.exceptions.ProException;

public class ZaakTable extends TreeTable {

  private boolean reset = false;

  public ZaakTable() {

    setImmediate(true);
    setSelectable(true);
    setSizeFull();

    setItemCaptionPropertyId(ZaakTableContainer.OMSCHRIJVING);
    setStyleName(GbaWebTheme.TABLE_BORDERLESS);
    addStyleName("zakentable");

    setHtml();

    addListener((ValueChangeListener) event -> {

      if (hasLeafChildren()) {
        selectFirstItem();
      } else {

        // Als parent is ingeklapt dan alles resetten

        if (isCollapsed(getParent(getValue()))) {
          initOpenItems(astr(event.getProperty().getValue()).equalsIgnoreCase("zoeken"));
          openItem();
        }

        Property classProperty = getContainerProperty(getValue(), ZaakTableContainer.LEAF_CLASS);

        if (classProperty != null) {
          Class<? extends Component> leafClass = (Class<? extends Component>) classProperty.getValue();

          try {
            selectComponent(leafClass.newInstance());
          } catch (Exception e) {
            throw new ProException(DATABASE, ERROR, "Kan component niet laden.", e);
          }
        }
      }
    });
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  public void recount() {
    if (getContainerDataSource() instanceof ZaakTableContainer) {
      ZaakTableContainer ztc = (ZaakTableContainer) getContainerDataSource();
      ztc.recount();
    }
  }

  public void reloadTree() {
    setContainerDataSource(new ZaakTableContainer(getApplication().getServices()));
  }

  public void reset() {
    this.reset = true;
    setValue(null);
    setContainerDataSource(new ZaakTableContainer(getApplication().getServices()));
  }

  @Override
  public void setContainerDataSource(Container newDataSource) {

    Object currentVal = getValue();
    super.setContainerDataSource(newDataSource);
    checkContainer(currentVal, newDataSource);
  }

  @SuppressWarnings("unused")
  protected void setNewComponent(Component component) {
  } // Override

  private void checkContainer(Object currentVal, Container newDataSource) {

    if (newDataSource instanceof ZaakTableContainer) {

      setVisibleColumns(new String[]{ OMSCHRIJVING, AANTAL });
      setColumnExpandRatio(OMSCHRIJVING, 1f);
      setColumnAlignment(AANTAL, TreeTable.ALIGN_RIGHT);
      setColumnHeaderMode(TreeTable.COLUMN_HEADER_MODE_HIDDEN);
      setColumnWidth(AANTAL, 35);

      initOpenItems(true);

      String dossier = astr(getApplication().getParameterMap().get("p"));

      for (Object obj : getContainerDataSource().getItemIds()) {
        boolean selected = false;
        if ((currentVal != null) && (obj instanceof ZaakAantalItem)) {
          ZaakAantalItem zObj = (ZaakAantalItem) obj;
          if (currentVal instanceof ZaakAantalItem) {
            ZaakAantalItem cObj = (ZaakAantalItem) currentVal;
            if (zObj.getId() == cObj.getId()) {
              selected = true;
            }
          }
        }

        if (!selected && obj.toString().equalsIgnoreCase(dossier)) {
          selected = true;
        }

        if (selected) {
          select(obj);
          return;
        }
      }

      if (reset) {
        select(ZaakTableContainer.ZOEKEN);
      }

      setPageLength(0);
      reset = false;
    }
  }

  private void collapseItem(Object itemId) {

    setCollapsed(itemId, true);

    if (hasChildren(itemId)) {
      for (Object child : getChildren(itemId)) {
        collapseItem(child);
      }
    }
  }

  private boolean hasLeafChildren() {
    return areChildrenAllowed(getValue());
  }

  private void initOpenItems(boolean isZoeken) {
    setCollapsed(ZaakTableContainer.ZOEKEN, false);
    setCollapsed(ZaakTableContainer.STATUS, !isZoeken);
    setCollapsed(ZaakTableContainer.BULKACTIES, true);
    setCollapsed(ZaakTableContainer.ZAAK, true);
  }

  private void openItem() {
    Object parentId = getParent(getValue());
    setCollapsed(parentId, false);
    selectItem(parentId);
  }

  private void selectComponent(Component component) {
    if (component instanceof SubModuleZaken) {
      SubModuleZaken layout = (SubModuleZaken) component;
      layout.init(getValue());
    }

    setNewComponent(component);
  }

  private void selectFirstItem() {
    collapseItem(ZaakTableContainer.ZAAK);
    collapseItem(ZaakTableContainer.BULKACTIES);

    if (hasChildren(getValue())) {
      for (Object child : getChildren(getValue())) {
        select(child);
        return;
      }
    }
  }

  private void selectItem(Object id) {
    Object parentId = getParent(id);
    if (parentId != null) {
      setCollapsed(parentId, false);
      selectItem(parentId);
    }
  }

  private void setHtml() {

    addGeneratedColumn("Omschrijving", (ColumnGenerator) (source, itemId, columnId) -> {

      Object item = getItem(itemId).getItemProperty(columnId);
      final Label label = new Label(item.toString(), Label.CONTENT_XHTML);
      label.setSizeUndefined();
      Property columnProp = source.getContainerProperty(itemId, columnId);
      if (columnProp instanceof ValueChangeNotifier) {
        ((ValueChangeNotifier) columnProp)
            .addListener((ValueChangeListener) event -> label.setValue(astr(event.getProperty())));
      }

      return label;
    });
  }
}

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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab6.page2;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab6.page2.Tab6DocumentenPage2Bean.*;
import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempel;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempelType;
import nl.procura.gba.web.services.zaken.documenten.stempel.PositieType;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public abstract class Tab6DocumentenPage2Form extends GbaForm<Tab6DocumentenPage2Bean> {

  public Tab6DocumentenPage2Form(DocumentStempel documentStempel) {
    setOrder(STEMPEL, TYPE, POSITIE, WOORD, PAGINAS, FONT,
        FONT_SIZE, VOLGORDE, XCOORDINAAT, YCOORDINAAT, BREEDTE, HOOGTE);
    setColumnWidths(WIDTH_130, "");
    initFields(documentStempel);
  }

  @Override
  public void afterSetBean() {

    getField(Tab6DocumentenPage2Bean.TYPE).addListener((ValueChangeListener) event -> {

      DocumentStempelType type = (DocumentStempelType) event.getProperty().getValue();

      getField(FONT).setVisible(type == DocumentStempelType.PROCURA_TEKST_ZAAK_ID);
      getField(FONT_SIZE).setVisible(type == DocumentStempelType.PROCURA_TEKST_ZAAK_ID);

      setOnChangeType(type);
    });

    getField(Tab6DocumentenPage2Bean.POSITIE).addListener((ValueChangeListener) event -> {

      PositieType type = (PositieType) event.getProperty().getValue();

      getField(WOORD).setVisible(type == PositieType.WOORD);

      setOnChangePositie(type);

      repaint();
    });

    getField(WOORD).setVisible(getBean().getPositie() == PositieType.WOORD);
    getField(FONT).setVisible(getBean().getType() == DocumentStempelType.PROCURA_TEKST_ZAAK_ID);
    getField(FONT_SIZE).setVisible(getBean().getType() == DocumentStempelType.PROCURA_TEKST_ZAAK_ID);

    super.afterSetBean();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(XCOORDINAAT)) {
      column.addComponent(new Label(
          "(kleiner dan 0 is links van de uitganspositie. Groter dan 0 is rechts van de uitgangspositie)"));
    }

    if (property.is(YCOORDINAAT)) {
      column.addComponent(new Label(
          "(kleiner dan 0 is hoger van de uitganspositie. Groter dan 0 is lager van de uitgangspositie)"));
    }

    if (property.is(VOLGORDE)) {
      column.addComponent(new Label(
          "(Stempels wordt in oplopende volgorde toegevoegd aan het document)"));
    }

    if (property.is(PAGINAS)) {
      column.addComponent(
          new Label("(Leeg is stempel op elke pagina. Nummer is specifiek voor betreffende paginanummer)"));
    }

    super.afterSetColumn(column, field, property);
  }

  protected abstract void setOnChangePositie(PositieType type);

  protected abstract void setOnChangeType(DocumentStempelType type);

  private void initFields(DocumentStempel documentStempel) {

    Tab6DocumentenPage2Bean bean = new Tab6DocumentenPage2Bean();

    if (documentStempel.isStored()) {
      bean.setStempel(documentStempel.getDocumentStempel());
      bean.setType(documentStempel.getStempelType());
      bean.setWoord(documentStempel.getWoord());
      bean.setX(astr(documentStempel.getXcoordinaat()));
      bean.setY(astr(documentStempel.getYcoordinaat()));
      bean.setPositie(documentStempel.getPositie());
      bean.setBreedte(astr(documentStempel.getBreedte()));
      bean.setHoogte(astr(documentStempel.getHoogte()));
      bean.setVolgorde(astr(documentStempel.getVolgorde()));
      bean.setPaginas(documentStempel.getPaginas());
      bean.setFont(documentStempel.getFontType());
      bean.setFontsize(astr(documentStempel.getFontSize()));
    }

    setBean(bean);

    repaint();
  }
}

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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab3;

import static nl.procura.gba.web.modules.hoofdmenu.gv.page1.Page1GvBean2.TAV_NAAM;
import static nl.procura.gba.web.modules.hoofdmenu.gv.page1.Page1GvBean2.TAV_VOORL;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.isTru;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public abstract class Tab3DocumentenPage2Form extends GbaForm<Tab3DocumentenPage2Bean> {

  public Tab3DocumentenPage2Form(DocumentAfnemer documentAfnemer) {

    init();
    setColumnWidths("200px", "");
    initFields(documentAfnemer);
  }

  @Override
  public void afterSetBean() {

    final Field vb = getField(Tab3DocumentenPage2Bean.VERSTREKBEP);
    final Field tk = getField(Tab3DocumentenPage2Bean.TOEKENNEN);

    if (vb != null) {
      vb.addListener((ValueChangeListener) event -> {
        setToekennen(tk, isTru(astr(event.getProperty().getValue())));
        repaint();
      });

      setToekennen(tk, getBean().getVerstrekbep());
    }

    super.afterSetBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(TAV_VOORL, TAV_NAAM)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  protected abstract void init();

  private void initFields(DocumentAfnemer documentAfnemer) {

    Tab3DocumentenPage2Bean bean = new Tab3DocumentenPage2Bean();

    if (documentAfnemer.isStored()) {
      bean.setAfnemer(documentAfnemer.getDocumentAfn());
      bean.setTavAanhef(documentAfnemer.getTerAttentieVanAanhef());
      bean.setTavVoorl(documentAfnemer.getTavVoorl());
      bean.setTavNaam(documentAfnemer.getTavNaam());
      bean.setEmail(documentAfnemer.getEmail());
      bean.setAdres(documentAfnemer.getAdres());
      bean.setPc(documentAfnemer.getPostcode());
      bean.setPlaats(documentAfnemer.getPlaats());
      bean.setGrondslag(documentAfnemer.getGrondslagType());
      bean.setVerstrekbep(documentAfnemer.isVerstrekBep());
      bean.setToekennen(documentAfnemer.getToekenning());
    }

    setBean(bean);
  }

  private void setToekennen(final Field tk, Boolean verstrek) {
    tk.setVisible(verstrek != null && verstrek);
  }
}

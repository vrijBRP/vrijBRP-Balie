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

package nl.procura.gba.web.modules.zaken.tmv.page6;

import static nl.procura.gba.web.modules.zaken.tmv.page6.Page6TmvBean2.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingRegistratie;
import nl.procura.gba.web.services.zaken.tmv.TmvActie;

public class Page6TmvTable extends GbaTable {

  private TerugmeldingAanvraag tmv             = null;
  private Page6TmvForm2        registratieForm = null;

  public Page6TmvTable(TerugmeldingAanvraag tmv, Page6TmvForm2 registratieForm) {

    setTmv(tmv);
    setRegistratieForm(registratieForm);
    ValueChangeListener listener = (ValueChangeListener) event -> {
      int index = aval(event.getProperty().getValue()) - 1;
      if (index >= 0) {
        select(getRecords().get(index));
      }
    };

    addListener(listener);
  }

  public Page6TmvForm2 getRegistratieForm() {
    return registratieForm;
  }

  public void setRegistratieForm(Page6TmvForm2 registratieForm) {
    this.registratieForm = registratieForm;
  }

  public TerugmeldingAanvraag getTmv() {
    return tmv;
  }

  public void setTmv(TerugmeldingAanvraag tmv) {
    this.tmv = tmv;
  }

  @Override
  public void reloadRecords() {

    super.reloadRecords();

    if (!getRecords().isEmpty()) {
      select(getRecords().get(0));
    }
  }

  @Override
  public void select(Record record) {

    TerugmeldingRegistratie reg = (TerugmeldingRegistratie) record.getObject();
    Page6TmvBean2 b = getRegistratieForm().getBean();

    b.setActie(reg.getActie().getOms());
    b.setTijdstip(reg.getIn().toString());
    b.setVerwerking(reg.getVerwerkingcode() + ": " + reg.getVerwerkingomschrijving());
    b.setDossiernr(astr(reg.getDossiernummer()));
    b.setToelichting(astr(reg.getToelichtingOmschrijving()));

    b.setVerstuurddoor(reg.getUsr().toString());
    b.setDatumaanleg(reg.getAanleg().toString());
    b.setDatumwijziging(reg.getWijz().toString());
    b.setGemeente(reg.getGemBeh().toString());
    b.setDatumVerwAfh(reg.getVerwAfh().toString());
    b.setStatusdossier(reg.getStatus().toString());
    b.setResultaatcode(reg.getOnderzoekResultaat().toString());
    b.setToelichtingResultaat(reg.getToelichtingResultaat());

    getRegistratieForm().setVisible(true);
    getRegistratieForm().setBean(b);

    getRegistratieForm().getField(TOELICHTING).setVisible(reg.getActie() == TmvActie.REGISTRATIE);
    getRegistratieForm().getField(VERSTUURDDOOR).setVisible(reg.getActie() == TmvActie.INZAG);
    getRegistratieForm().getField(DATUMAANLEG).setVisible(reg.getActie() == TmvActie.INZAG);
    getRegistratieForm().getField(DATUMWIJZIGING).setVisible(reg.getActie() == TmvActie.INZAG);
    getRegistratieForm().getField(GEMEENTE).setVisible(reg.getActie() == TmvActie.INZAG);
    getRegistratieForm().getField(DATUMVERWAFH).setVisible(reg.getActie() == TmvActie.INZAG);
    getRegistratieForm().getField(STATUSDOSSIER).setVisible(reg.getActie() == TmvActie.INZAG);
    getRegistratieForm().getField(RESULTAATCODE).setVisible(reg.getActie() == TmvActie.INZAG);
    getRegistratieForm().getField(TOELICHTINGRESULTAAT).setVisible(reg.getActie() == TmvActie.INZAG);

    getRegistratieForm().repaint();

    super.select(record);
  }

  @Override
  public void setColumns() {

    addColumn("Vnr", 20);
    addColumn("Actie", 120);
    addColumn("Tijdstip", 150);
    addColumn("Status");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {
      setSelectable(true);

      List<TerugmeldingRegistratie> l = getTmv().getRegistraties();
      int i = l.size();

      for (TerugmeldingRegistratie reg : l) {

        Record r = addRecord(reg);
        r.addValue(i);
        r.addValue(reg.getActie().getOms());
        r.addValue(reg.getIn());
        r.addValue(reg.getStatus());

        i--;
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}

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

package nl.procura.gba.web.modules.zaken.verhuizing.page14;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.vaadin.theme.twee.Icons;

/**
 * Selecteer een adres
 */
public class Page14Verhuizing extends ZakenPage {

  private final Table1       table1          = new Table1();
  private List<VerhuisAdres> adressen        = new ArrayList<>();
  private boolean            gebruikPPDCodes = false;

  public Page14Verhuizing(List<VerhuisAdres> adressen, boolean gebruikPPDCodes) {

    super("Controle op ingevoerd adres");

    addButton(buttonPrev);

    setAdressen(adressen);
    setGebruikPPDCodes(gebruikPPDCodes);
    addExpandComponent(table1);
  }

  public void addListener(AdresSelectieListener listener) {
    addListener("bla_id", AdresSelectieEvent.class, listener, AdresSelectieListener.clickMethod);
  }

  public List<VerhuisAdres> getAdressen() {
    return adressen;
  }

  public void setAdressen(List<VerhuisAdres> adressen) {
    this.adressen = adressen;
  }

  public boolean isGebruikPPDCodes() {
    return gebruikPPDCodes;
  }

  public void setGebruikPPDCodes(boolean gebruikPPDCodes) {
    this.gebruikPPDCodes = gebruikPPDCodes;
  }

  @Override
  public void onEnter() {

    selectVerhuisAdres((VerhuisAdres) table1.getSelectedRecord().getObject());

    super.onEnter();
  }

  private void selectVerhuisAdres(VerhuisAdres adres) {

    try {
      this.fireEvent(new AdresSelectieEvent(this, adres));
    } catch (Exception e) {
      getApplication().handleException(e.getCause());
    }
  }

  class Table1 extends Page14VerhuizingTable1 {

    @Override
    public void onClick(Record record) {

      selectVerhuisAdres((VerhuisAdres) record.getObject());

      super.onClick(record);
    }

    @Override
    public void setRecords() {

      int gebrPPD = aval(getApplication().getParmValue(ParameterConstant.GEBR_PPD));

      for (VerhuisAdres a : getAdressen()) {
        Record r = addRecord(a);

        if (a.isSuitableForLiving()) {
          r.addValue(new TableImage(Icons.getIcon(Icons.ICON_OK)));
        } else {
          r.addValue(new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
        }

        if (isGebruikPPDCodes() && (gebrPPD >= 0)) {
          if (gebrPPD == aval(a.getAddress().getPPD())) {
            r.addValue(new TableImage(Icons.getIcon(Icons.ICON_OK)));
          } else {
            r.addValue(new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
          }
        } else {
          r.addValue("N.v.t.");
        }

        String adres = a.getAddressLabel();
        if (pos(a.getAddress().getEndDate())) {
          adres += " - beëindigd op " + date2str(a.getAddress().getEndDate());
        }

        r.addValue(adres);

      }
    }
  }

}

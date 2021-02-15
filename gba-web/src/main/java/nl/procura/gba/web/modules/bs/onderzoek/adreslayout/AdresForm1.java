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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout;

import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.wk.baseWK.BaseWKValue;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.common.misc.SelectListener;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.adres.BewonerWindow;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoWindow;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.selectie.AdresSelectieWindow;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.SelectieAdres;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.window.Message;

public class AdresForm1 extends AdresForm<AdresBean1> {

  private final SelectListener<DossierPersoon> listener;

  public AdresForm1(Adres adres, SelectListener<DossierPersoon> listener) {
    super();
    this.listener = listener;
    setOrder(STRAAT, HNR, HNR_L, HNR_T, HNR_A, PC, WPL, AANT_PERS);

    AdresBean1 bean = new AdresBean1();
    bean.setStraat(adres.getAdres());
    bean.setHnr(adres.getHnr());
    bean.setHnrL(adres.getHnrL());
    bean.setHnrT(adres.getHnrT());
    bean.setHnrA(adres.getHnrA());
    bean.setPc(adres.getPc());
    bean.setWoonplaats(adres.getPlaats());
    bean.setAantalPersonen(adres.getAantalPersonen());

    setBean(bean);
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {

    if (property.is(HNR, HNR_L, HNR_T, HNR_A)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  public void selectAdres(final boolean bewoners, final boolean objectInfo) {

    String hnrT = astr(getField(HNR_T).getValue());
    String hnrA = astr(getField(HNR_A).getValue());
    String pc = astr(getField(PC).getValue());

    ZoekArgumenten adresZ = new ZoekArgumenten();
    adresZ.setStraatnaam(astr(getField(STRAAT).getValue()));
    adresZ.setHuisnummer(astr(getField(HNR).getValue()));
    adresZ.setHuisletter(astr(getField(HNR_L).getValue()));

    if (fil(hnrT)) {
      adresZ.setHuisnummertoevoeging(hnrT);
    }

    if (fil(hnrA)) {
      adresZ.setHuisnummeraanduiding(hnrA);
    }

    if (fil(pc)) {
      adresZ.setPostcode(pc);
    }

    adresZ.setDatum_einde("-1");

    if (!adresZ.isGevuld()) {
      throw new ProException(ENTRY, INFO, "Geen adres ingegeven.");
    }

    PersonenWsService wsService = Services.getInstance().getPersonenWsService();
    List<BaseWKExt> wkWrappers = wsService.getAdres(adresZ, false).getBasisWkWrappers();
    List<SelectieAdres> adr = getWkAdressen(wkWrappers);

    switch (adr.size()) {
      case 0:
        throw new ProException(ENTRY, INFO, "Geen adres gevonden.");

      case 1:
        SelectieAdres selectieAdres = updateAdresFields(adr.get(0));

        if (bewoners) { // Toone bewoners
          getApplication().getParentWindow().addWindow(new BewonerWindow(selectieAdres, listener));

        } else if (objectInfo) {
          getApplication().getParentWindow().addWindow(new ObjectInfoWindow(wkWrappers.get(0)));

        } else {
          new Message(getWindow(), "Dit adres is gevonden in de gemeente", Message.TYPE_SUCCESS);
        }
        break;

      default:
        getApplication().getParentWindow().addWindow(new AdresSelectieWindow(adr, (SelectieAdres adres) -> {

          ZoekArgumenten args = new ZoekArgumenten();
          args.setCode_object(adres.getCode_object().getValue());
          args.setVolgcode_einde(adres.getVolgcode_einde().getValue());
          args.setDatum_einde(astr(adres.getDatum_einde().getValue()));

          // Zoek specifiek adres zodat de personen
          // worden teruggegeven

          List<BaseWKExt> basisWkWrappers = wsService.getAdres(args, false).getBasisWkWrappers();
          getWkAdressen(basisWkWrappers).forEach(this::updateAdresFields);
        }));
    }
  }

  private SelectieAdres updateAdresFields(SelectieAdres adres) {
    AdresBean1 bean = new AdresBean1();
    if (adres != null) {
      bean.setStraat(new FieldValue(adres.getStraat().getValue()));
      bean.setHnr(adres.getHuisnummer().getValue());
      bean.setHnrL(adres.getHuisletter().getValue());
      bean.setHnrT(adres.getToevoeging().getValue());
      bean.setHnrA(new FieldValue(adres.getAanduiding().getValue()));
      bean.setPc(new FieldValue(adres.getPostcode().getValue()));

      BaseWKValue wpl = adres.getWoonplaats();
      bean.setWoonplaats(new FieldValue(wpl.getCode(), wpl.getDescr()));
    }
    setBean(bean);
    return adres;
  }

  private List<SelectieAdres> getWkAdressen(List<BaseWKExt> wks) {
    return wks.stream().map(SelectieAdres::new).collect(Collectors.toList());
  }
}

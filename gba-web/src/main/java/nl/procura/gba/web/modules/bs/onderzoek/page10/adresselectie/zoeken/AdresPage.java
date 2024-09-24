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

package nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.AdresBean.BSN;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;
import java.util.stream.Collectors;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.common.misc.SelectListener;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.selectie.AdresSelectieWindow;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class AdresPage extends NormalPageTemplate {

  private AdresForm1                                      form1;
  private AdresForm2                                      form2;
  private final ProcuraInhabitantsAddress                 oldAdres;
  private final SelectListener<ProcuraInhabitantsAddress> listener;

  public AdresPage(ProcuraInhabitantsAddress oldAdres, SelectListener<ProcuraInhabitantsAddress> listener) {
    this.oldAdres = oldAdres;
    this.listener = listener;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonSearch);
      addButton(buttonReset, 1f);
      addButton(buttonClose);

      form1 = new AdresForm1();
      form2 = new AdresForm2(oldAdres);
      addComponent(form1);
      addComponent(form2);
    }

    super.event(event);
    form1.getField(BSN).focus();
  }

  @Override
  public void onNew() {
    form1.reset();
    form2.reset();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onSearch() {
    form1.commit();
    form2.repaint();
    form2.commit();

    AdresBean bean1 = form1.getBean();

    if (bean1.getBsn().isCorrect()) {

      BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(bean1.getBsn().getStringValue());

      ZoekArgumenten adresZ = new ZoekArgumenten();
      Adres adres = pl.getVerblijfplaats().getAdres();
      adresZ.setPostcode(adres.getPostcode().getValue().getVal());
      adresZ.setHuisnummer(adres.getHuisnummer().getValue().getVal());
      adresZ.setHuisletter(adres.getHuisletter().getValue().getVal());
      adresZ.setHuisnummertoevoeging(adres.getHuisnummertoev().getValue().getVal());
      adresZ.setDatum_einde("-1");

      setAdres(adresZ);

    } else {

      AdresBean bean2 = form2.getBean();

      ZoekArgumenten adresZ = new ZoekArgumenten();
      adresZ.setStraatnaam(astr(bean2.getStraat()));
      adresZ.setPostcode(astr(bean2.getPc().getValue()));
      adresZ.setHuisnummer(bean2.getHnr());
      adresZ.setHuisletter(bean2.getHnrL());
      adresZ.setHuisnummertoevoeging(bean2.getHnrT());
      adresZ.setDatum_einde("-1");

      if (!adresZ.isGevuld()) {
        throw new ProException(ENTRY, INFO, "Geen adres ingegeven.");
      }

      setAdres(adresZ);
    }
  }

  private void setAdres(ZoekArgumenten adresZ) {
    List<BaseWKExt> wkAdressen = getServices().getPersonenWsService().getAdres(adresZ, false).getBasisWkWrappers();
    List<ProcuraInhabitantsAddress> adressen = getWkAdressen(wkAdressen);

    switch (adressen.size()) {
      case 0:
        throw new ProException(ENTRY, INFO, "Geen adres gevonden.");

      case 1:
        selectAdres(adressen.get(0));
        break;

      default:
        getParentWindow().addWindow(new AdresSelectieWindow(adressen, (ProcuraInhabitantsAddress adres) -> {

          // Zoek specifiek adres zodat de personen
          // worden teruggegeven

          ZoekArgumenten args = new ZoekArgumenten();
          args.setCode_object(astr(adres.getInternalId()));
          args.setVolgcode_einde(astr(adres.getEndDateNumber()));
          args.setDatum_einde(astr(adres.getEndDate()));

          List<BaseWKExt> basisWkWrappers = getServices().getPersonenWsService()
              .getAdres(args, false)
              .getBasisWkWrappers();

          getWkAdressen(basisWkWrappers).forEach(this::selectAdres);
        }));
    }
  }

  private void selectAdres(ProcuraInhabitantsAddress adres) {
    form2.update(adres);
    listener.select(adres);
    AdresPage.this.getWindow().closeWindow();
  }

  private List<ProcuraInhabitantsAddress> getWkAdressen(List<BaseWKExt> wks) {
    return wks.stream().map(ProcuraInhabitantsAddress::new).collect(Collectors.toList());
  }
}

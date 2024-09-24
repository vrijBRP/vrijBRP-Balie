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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.isTru;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.templates.custom.CustomTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.errorpage.RijbewijsErrorWindow;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.rijbewijs.NrdServices;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsService;
import nl.procura.rdw.functions.RdwMessage;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public abstract class Page5ZakenTab extends ZakenregisterPage<Zaak> {

  protected Page5ZakenTab() {
    super(null, "Zakenregister: rijbewijzen raadplegen");
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  protected RijbewijsAanvraag getAanvraag(BigInteger aanvraagnummer) {

    ZaakArgumenten zaakArgumenten = new ZaakArgumenten(astr(aanvraagnummer));
    RijbewijsService rijbewijzen = getServices().getRijbewijsService();
    return rijbewijzen.getMinimalZaken(zaakArgumenten)
        .stream()
        .findFirst()
        .map(rijbewijzen::getStandardZaak)
        .orElse(null);
  }

  /**
   * Stuur bericht naar RDW en toon foutmelding indien van toepassing
   */
  protected boolean sendMessage(RdwMessage message) {

    if (NrdServices.sendMessage(getServices().getRijbewijsService(), message)) {
      return true;
    }

    getParentWindow().addWindow(new RijbewijsErrorWindow(message.getResponse().getMelding()));
    return false;
  }

  public class Table extends Page5ZakenTabTable {

    @Override
    public void onDoubleClick(Record record) {
      if (record.getObject() instanceof RijbewijsAanvraag) {
        Page5ZakenTabPage page = VaadinUtils.getParent(this, Page5ZakenTabPage.class);
        if (page != null) {
          RijbewijsAanvraag aanvraag = (RijbewijsAanvraag) record.getObject();
          ZaakregisterNavigator.navigatoTo(aanvraag, page, false);
        }
      }
    }
  }

  /**
   * Geef naam terug van de basispl
   */
  public String getIdNummer(long anr, long bsn) {
    Anr anrValue = new Anr(anr);
    Bsn bsnValue = new Bsn(bsn);
    if (bsnValue.isCorrect()) {
      return bsnValue.getFormatBsn();
    }
    if (anrValue.isCorrect()) {
      return anrValue.getFormatAnummer();
    }
    return "";
  }

  /**
   * Geef naam terug van de basispl
   */
  public String getNaam(PLResultComposite result, long anr, long bsn) {
    Anr anrValue = new Anr(anr);
    if (anrValue.isCorrect()) {
      if (result != null) {
        Optional<BasePLExt> bpl = result.getBasisPLWrappers()
            .stream()
            .filter(pl -> pl.getPersoon().getAnr().getVal().equals(anrValue.getAnummer()))
            .findFirst();

        if (bpl.isPresent()) {
          if (bpl.get().getCat(GBACat.PERSOON).hasSets()) {
            return bpl.get().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
          }
        }
      }
    }
    return "";
  }

  /**
   * Zoek namen op basis van anummers
   */
  public PLResultComposite getNaamByAnr(List<Long> anrs, int max) {
    PLEArgs args = new PLEArgs();
    for (Long anr : anrs) {
      Anr anrValue = new Anr(anr);
      if (anrValue.isCorrect()) {
        args.addNummer(anrValue.getAnummer());
      }
    }

    if (args.getNumbers().isEmpty()) {
      return null;
    }

    args.setShowHistory(false);
    args.setShowArchives(false);
    args.addCat(PERSOON);
    args.setCat(HUW_GPS, isTru(getApplication().getServices()
        .getGebruiker()
        .getParameters()
        .get(ParameterConstant.ZOEK_PLE_NAAMGEBRUIK)
        .getValue()));

    args.setCustomTemplate(CustomTemplate.PERSON);
    args.setDatasource(PLEDatasource.PROCURA);
    args.setMaxFindCount(max);

    return getApplication().getServices().getPersonenWsService().getPersoonslijsten(args, true);
  }
}

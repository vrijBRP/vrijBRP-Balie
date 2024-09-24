/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page40;

import nl.procura.gba.web.modules.bs.naturalisatie.BsPageNaturalisatie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page40Naturalisatie extends BsPageNaturalisatie {

  private Page40NaturalisatieForm form;

  public Page40Naturalisatie() {
    super("Nationaliteit - toetsing");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);
      addButton(buttonNext);

      form = new Page40NaturalisatieForm(getZaakDossier());

      setInfo("Toets aan de voorwaarden en druk de benodigde documenten af. "
          + "Als de betaling heeft plaatsgevonden, leg dan de aanvraagdatum vast. "
          + "Die is van belang voor de beslistermijn. Druk op Volgende (F2) om verder te gaan.");

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {

      form.commit();

      Page40NaturalisatieBean bean = form.getBean();
      getZaakDossier().setToetsverklOndertekend(bean.getVerklaringVerblijf());
      getZaakDossier().setToetsBereidVerkl(bean.getBereidTotAfleggenVerklaring());
      getZaakDossier().setToetsBetrokkBekend(bean.getBetrokkeneBekendMetBetaling());
      getZaakDossier().setBereidAfstandType(bean.getBereidTotDoenVanAfstand());
      getZaakDossier().setToetsBewijsIdAanw(bean.getBewijsVanIdentiteit());
      getZaakDossier().setBewijsBewijsNationaliteitType(bean.getBewijsVanNationaliteit());
      getZaakDossier().setToetsBewijsnood(bean.getBewijsnoodAangetoond());
      getZaakDossier().setToetsBewijsnoodToel(bean.getToelichting1());
      getZaakDossier().setGeldigeVerblijfsvergunningType(bean.getGeldigeVerblijfsvergunning());

      // Naamsvaststelling/wijziging
      getZaakDossier().setNaamVaststellingType(bean.getNaamsvaststellingOfWijziging());
      getZaakDossier().setNaamstGeslGewToel(bean.getToelichting2());

      getServices().getNaturalisatieService().save(getDossier());
      return true;
    }

    return false;
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }
}

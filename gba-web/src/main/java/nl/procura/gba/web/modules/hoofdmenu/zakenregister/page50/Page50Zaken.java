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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page50;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;

import java.util.List;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page51.Page51Zaken;
import nl.procura.gba.web.modules.zaken.uittreksel.overzicht.UittrekselOverzichtBuilder;
import nl.procura.gba.web.services.zaken.documenten.*;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;

/**
 * Tonen uittrekselaanvraag
 */

public class Page50Zaken extends ZakenregisterOptiePage<DocumentZaak> {

  public Page50Zaken(DocumentZaak aanvraag) {

    super(aanvraag, "Zakenregister - " + getOmschrijving(aanvraag) + " document");

    addButton(buttonPrev);
  }

  private static String getOmschrijving(DocumentZaak aanvraag) {
    return DocumentTypeUtils.getZaakType(aanvraag.getDoc().getDocumentType()).toString().toLowerCase();
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
  }

  @Override
  protected void addTabs(ZaakTabsheet<DocumentZaak> tabsheet) {
    UittrekselOverzichtBuilder.addTab(tabsheet, getZaak());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToDocument() {

    DocumentService documenten = getServices().getDocumentService();
    DocumentRecord document = documenten.getDocument(getZaak().getDoc().getCDocument());
    List<DocumentSoort> soorten = documenten.getDocumentSoorten(getApplication().getServices().getGebruiker(),
        asList(document));
    soorten.addAll(documenten.getDocumentSoorten(getApplication().getServices().getGebruiker(), DocumentType.ZAAK));

    getNavigation().goToPage(new Page51Zaken(getZaak(), soorten));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.documenten", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }
}

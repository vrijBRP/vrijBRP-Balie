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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8;

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType;
import nl.procura.gba.web.services.zaken.documenten.DocumentTypeUtils;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page8ZakenTable extends GbaTable {

  public Page8ZakenTable() {
  }

  /**
   * Geeft terug of de persoon 'mijn-overheid abonnee' is
   */
  public String getAbonnee(Zaak zaak) {

    if (isWelAbonnee(zaak)) {
      return "Ja";
    } else if (isGeenAbonnee(zaak)) {
      return "Nee";
    }

    return "Nog niet bepaald";
  }

  /**
   * Geeft de profielen terug van de gebruiker die de zaak heeft ingevoerd
   */
  public String getIngevoerdDoor(Zaak zaak) {

    if (pos(zaak.getIngevoerdDoor().getValue())) {
      return zaak.getIngevoerdDoor().getDescription();
    }

    return "Onbekend";
  }

  public boolean isGeenAbonnee(Zaak zaak) {
    return zaak.getZaakHistorie().getAttribuutHistorie().is(ZaakAttribuutType.MIJN_OVERHEID_NIET);
  }

  public boolean isWelAbonnee(Zaak zaak) {
    return zaak.getZaakHistorie().getAttribuutHistorie().is(ZaakAttribuutType.MIJN_OVERHEID_WEL);
  }

  @Override
  public void onDoubleClick(Record record) {

    if (record.getObject() instanceof PrintRecord) {

      PrintRecord printrecord = (PrintRecord) record.getObject();
      ZaakregisterNavigator.navigatoTo(printrecord.getZaak(), VaadinUtils.getParent(this, Page8ZakenTab.class),
          false);
    }
  }

  public void reset() {
    init();
  }

  protected Zaak getGerelateerdeZaak(DocumentZaak aanvraag) {
    ZaakType zaakType = DocumentTypeUtils.getZaakType(aanvraag.getDoc().getDocumentType());
    return getApplication().getServices().getZaakRelatieService().getGerelateerdeZaakByType(aanvraag, zaakType,
        true);
  }
}

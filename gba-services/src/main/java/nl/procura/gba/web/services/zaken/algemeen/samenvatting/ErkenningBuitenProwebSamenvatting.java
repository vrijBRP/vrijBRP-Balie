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

package nl.procura.gba.web.services.zaken.algemeen.samenvatting;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een erkenning buiten Proweb
 */
public class ErkenningBuitenProwebSamenvatting extends ZaakSamenvattingTemplate<ErkenningBuitenProweb> {

  public ErkenningBuitenProwebSamenvatting(ZaakSamenvatting zaakSamenvatting, ErkenningBuitenProweb zaak,
      String titel) {
    super(zaakSamenvatting, zaak, titel);
  }

  @Override
  public void addDeelzaken(ErkenningBuitenProweb zaak) {
  }

  @Override
  public void addZaakItems(ErkenningBuitenProweb zaak) {

    ZaakItemRubriek rubriek = addRubriek(getTitel());
    FieldValue landErkenning = zaak.getLandErkenning();
    String plaats = astr(zaak.getGemeente());

    if (!Landelijk.getNederland().equals(landErkenning)) {
      plaats = zaak.getBuitenlandsePlaats() + ", " + zaak.getLandErkenning();
    }

    ToestemminggeverType toestType = zaak.getToestemminggeverType();
    String toestemming = astr(toestType);

    if (toestType == ToestemminggeverType.RECHTBANK) {
      toestemming += " " + zaak.getRechtbank();
    }

    rubriek.add("Plaats", plaats);
    rubriek.add("Datum", zaak.getDatumErkenning());
    rubriek.add("Akte", zaak.getAktenummer());
    rubriek.add("Toestemminggever", toestemming);
    rubriek.add("Naamskeuze", zaak.getNaamskeuzeType());
    rubriek.add("Geslachtsnaam", zaak.getNaamskeuzePersoon());
    rubriek.add("Toegepast recht van", zaak.getLandAfstamming());
  }
}

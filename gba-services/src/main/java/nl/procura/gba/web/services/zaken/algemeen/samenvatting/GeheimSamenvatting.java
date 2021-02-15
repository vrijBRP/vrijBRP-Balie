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

import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.gba.web.services.zaken.geheim.GeheimAanvraag;
import nl.procura.gba.web.services.zaken.geheim.GeheimPersoon;

/**
 * Maakt samenvatting van gegevens over een specifieke verstrekkingsbeperking
 */
public class GeheimSamenvatting extends ZaakSamenvattingTemplate<GeheimAanvraag> {

  public GeheimSamenvatting(ZaakSamenvatting zaakSamenvatting, GeheimAanvraag zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(GeheimAanvraag zaak) {
    Deelzaken deelZaken = addDeelzaken(zaak.getPersonen().size());

    for (GeheimPersoon p : zaak.getPersonen()) {
      String naam = p.getPersoon().getPersoon().getFormats().getNaam().getPred_eerstevoorn_adel_voorv_gesl();
      String leeftijd = p.getPersoon().getPersoon().getFormats().getGeboorte().getDatum_leeftijd();

      Deelzaak deelZaak = new Deelzaak();
      deelZaak.add("Naam", naam);
      deelZaak.add("BSN", p.getBurgerServiceNummer().getDescription());
      deelZaak.add("Geboren", leeftijd);
      deelZaken.add(deelZaak);
    }
  }

  @Override
  public void addZaakItems(GeheimAanvraag zaak) {
    ZaakItemRubriek rubriek = addRubriek("Geheimhouding");
    rubriek.add("Mate van beperking", zaak.getGeheimType().getOms());
  }
}

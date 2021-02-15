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

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een erkenning
 */
public class ErkenningSamenvatting extends ZaakSamenvattingTemplate<DossierErkenning> {

  public ErkenningSamenvatting(ZaakSamenvatting zaakSamenvatting, DossierErkenning zaak, String titel) {
    super(zaakSamenvatting, zaak, titel);
  }

  @Override
  public void addDeelzaken(DossierErkenning zaak) {
  }

  @Override
  public void addZaakItems(DossierErkenning zaak) {
    addRubriek(getTitel());
    setAktes(zaak.getDossier());
    setNationaliteiten(zaak.getDossier());
    setMoeder(zaak);
    setAfstamming(zaak);
    setNamenrecht(zaak);
  }

  protected void setAfstamming(DossierErkenning zaak) {

    ZaakItemRubriek rubriek = addRubriek("Afstamming");
    rubriek.add("Toegepast recht van", zaak.getLandAfstammingsRecht());
  }

  protected void setMoeder(DossierErkenning zaak) {

    DossierPersoon moeder = zaak.getMoeder();
    DossierPersoon erkenner = zaak.getErkenner();

    ZaakItemRubriek rubriek = addRubriek("Moeder - " + moeder.getNaam().getTitel_voorv_gesl());
    rubriek.add("BSN", moeder.getBurgerServiceNummer().getDescription());
    rubriek.add("Geboren", getGeboorte(moeder));
    rubriek.add("Gemeente", moeder.getWoongemeente().getDescription());

    rubriek = addRubriek("Erkenner - " + erkenner.getNaam().getTitel_voorv_gesl());
    rubriek.add("BSN", erkenner.getBurgerServiceNummer().getDescription());
    rubriek.add("Geboren", getGeboorte(erkenner));
    rubriek.add("Gemeente", erkenner.getWoongemeente().getDescription());
  }

  protected void setNamenrecht(DossierErkenning zaak) {

    ZaakItemRubriek rubriek = addRubriek("Namenrecht");
    rubriek.add("Keuze geslachtsnaam", zaak.getKeuzeGeslachtsnaam());
    rubriek.add("Toegepast recht van", zaak.getLandNaamRecht());
    rubriek.add("Keuze voorvoegsel", zaak.getKeuzeVoorvoegsel());
    rubriek.add("Naamskeuze", zaak.getNaamskeuzeType().toString());
    rubriek.add("Keuze titel", zaak.getKeuzeTitel());
    rubriek.add("Naam gekregen van", zaak.getNaamskeuzePersoon().getType());
  }

  protected void setToestemming(DossierErkenning zaak) {

    ZaakItemRubriek rubriek = addRubriek("Toestemming");
    rubriek.add("Toestemming gegeven door", zaak.getLandNaamRecht().getDescription());

    if (pos(zaak.getLandToestemmingsRechtMoeder().getValue())) {
      rubriek.add("Toegepast recht op de moeder", zaak.getLandToestemmingsRechtMoeder().getDescription());
    }

    if (pos(zaak.getLandToestemmingsRechtKind().getValue())) {
      rubriek.add("Toegepast recht op het kind", zaak.getLandToestemmingsRechtKind().getDescription());
    }
  }

  private String getGeboorte(DossierPersoon p) {
    Geboorteformats gf = new Geboorteformats();
    gf.setValues(p.getDatumGeboorte().getFormatDate(), p.getGeboorteplaats().getDescription(),
        p.getGeboorteland().getDescription());
    return gf.getDatum_plaats_land();
  }

  private void setNationaliteiten(DossierNationaliteiten zaak) {

    addRubriek("Nationaliteit(en) als gevolg van de erkenning");

    for (DossierNationaliteit nat : zaak.getNationaliteiten()) {

      ZaakItemRubriek rubriek = addRubriek(nat.getNationaliteitOmschrijving());
      rubriek.add("Sinds", nat.getSinds());
      rubriek.add("Reden", nat.getRedenverkrijgingNederlanderschap());
    }
  }
}

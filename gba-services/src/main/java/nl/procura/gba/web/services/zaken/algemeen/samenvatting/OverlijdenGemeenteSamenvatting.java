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

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een overlijden
 */
public class OverlijdenGemeenteSamenvatting extends OverlijdenSamenvatting<DossierOverlijdenGemeente> {

  public OverlijdenGemeenteSamenvatting(ZaakSamenvatting zaakSamenvatting, DossierOverlijdenGemeente zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(DossierOverlijdenGemeente zaak) {
  }

  @Override
  public void addZaakItems(DossierOverlijdenGemeente zaak) {
    addRubriek("Gegevens overlijden in gemeente");
    setAktes(zaak.getDossier());
    setOverledene(zaak);
    setAangifte(zaak);
    setGerelateerden(zaak);
    setLijkbezorging(zaak);
  }

  @Override
  protected void setAangifte(DossierOverlijdenGemeente zaak) {
    ZaakItemRubriek rubriek;
    DossierPersoon aangever = zaak.getAangever();
    rubriek = addRubriek("Aangifte / aangever");
    rubriek.add("Naam", aangever.getNaam().getGesl_pred_init_adel_voorv());
    rubriek.add("BSN", aangever.getBurgerServiceNummer().getDescription());
    rubriek.add("Geboren", aangever.getGeboorte().getDatum_te_plaats());
    rubriek.add("Document", zaak.getOntvangenDocument().getOms());
  }

  private void setOverledene(DossierOverlijdenGemeente zaak) {
    DossierPersoon overledene = zaak.getOverledene();
    ZaakItemRubriek rubriek = addRubriek("Overledene");
    rubriek.add("Naam", overledene.getNaam().getPred_adel_voorv_gesl_voorn());
    rubriek.add("BSN", overledene.getBurgerServiceNummer().getDescription());
    rubriek.add("Woonachtig", overledene.getAdres().getAdres_pc_wpl_gem());
    rubriek.add("Overleden op", zaak.getDatumOverlijden() + " om " + zaak.getTijdOverlijdenStandaard());
    rubriek.add("Geslacht", overledene.getGeslacht());
  }
}

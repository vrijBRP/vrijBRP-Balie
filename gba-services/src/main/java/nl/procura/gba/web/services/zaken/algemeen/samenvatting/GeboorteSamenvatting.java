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

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een geboorte
 */
public class GeboorteSamenvatting<T extends DossierGeboorte> extends ZaakSamenvattingTemplate<T> {

  public GeboorteSamenvatting(ZaakSamenvatting zaakSamenvatting, T zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(T zaak) {
  }

  @Override
  public void addZaakItems(T zaak) {
    String title;
    if (GezinssituatieType.BINNEN_HETERO_HUWELIJK.equals(zaak.getGezinssituatie())) {
      title = "Geboorte binnen huwelijk";
    } else {
      if (ErkenningsType.GEEN_ERKENNING.equals(zaak.getErkenningsType())) {
        title = "Geboorte buiten huwelijk, geen erkenning";
      } else {
        title = "Geboorte buiten huwelijk";
      }
    }

    addRubriek(title);
    setAktes(zaak.getDossier());
    setKinderen(zaak);
    setAangever(zaak);
    setMoederVader(zaak);
    setAfstamming(zaak);
    setNamenrecht(zaak);

    if (zaak.getVragen().heeftErkenningBijGeboorte()) {
      DossierErkenning erk = zaak.getErkenningBijGeboorte();
      new ErkenningSamenvatting(getZaakSamenvatting(), erk, "Erkenning bij aangifte geboorte");
    } else if (zaak.getVragen().heeftErkenningVoorGeboorte()) {
      DossierErkenning erk = zaak.getErkenningVoorGeboorte();
      new ErkenningSamenvatting(getZaakSamenvatting(), erk, "Erkenning vóór geboorte (in Proweb)");
    } else if (zaak.getVragen().heeftErkenningBuitenProweb()) {
      ErkenningBuitenProweb erk = zaak.getErkenningBuitenProweb();
      new ErkenningBuitenProwebSamenvatting(getZaakSamenvatting(), erk,
          "Erkenning vóór geboorte (buiten Proweb)");
    }
  }

  protected void setAangever(T zaak) {

    String reden = "";
    if (fil(zaak.getTardieveReden())) {
      reden = ", reden: " + zaak.getTardieveReden();
    }

    DossierPersoon aangever = zaak.getAangever();
    ZaakItemRubriek rubriek = addRubriek("Aangever - " + aangever.getNaam().getGesl_pred_init_adel_voorv());
    rubriek.add("BSN", aangever.getBurgerServiceNummer().getDescription());
    rubriek.add("Geboren", aangever.getGeboorte().getDatum_te_plaats());
    rubriek.add("Reden verplicht / bevoegd", zaak.getRedenVerplichtBevoegd().getOms());
    rubriek.add("Tardieve aangifte", (zaak.isTardieveAangifte() ? "Ja" : "Nee") + reden);
  }

  protected void setAfstamming(T zaak) {

    ZaakItemRubriek rubriek = addRubriek("Afstamming");
    rubriek.add("Gezinssituatie", zaak.getGezinssituatie().getOms());
    rubriek.add("Afstammingsrecht", zaak.getLandAfstammingsRecht().getDescription());
  }

  protected void setMoederVader(T zaak) {

    DossierPersoon moeder = zaak.getMoeder();
    DossierPersoon vader = zaak.getVader();

    ZaakItemRubriek rubriek = addRubriek("Moeder - " + moeder.getNaam().getTitel_voorv_gesl());
    rubriek.add("BSN", moeder.getBurgerServiceNummer().getDescription());
    rubriek.add("Geboren", getGeboorte(moeder));
    rubriek.add("Gemeente", moeder.getWoongemeente().getDescription());

    rubriek = addRubriek("Vader - " + vader.getNaam().getTitel_voorv_gesl());
    rubriek.add("BSN", vader.getBurgerServiceNummer().getDescription());
    rubriek.add("Geboren", getGeboorte(vader));
    rubriek.add("Gemeente", vader.getWoongemeente().getDescription());
  }

  protected void setNamenrecht(T zaak) {

    ZaakItemRubriek rubriek = addRubriek("Namenrecht");
    rubriek.add("namenrecht", zaak.getLandNaamRecht().getDescription());
  }

  private String getGeboorte(DossierPersoon p) {
    Geboorteformats gf = new Geboorteformats();
    gf.setValues(p.getDatumGeboorte().getFormatDate(), p.getGeboorteplaats().getDescription(),
        p.getGeboorteland().getDescription());
    return gf.getDatum_plaats_land();
  }

  private void setKinderen(T zaak) {
    for (DossierPersoon kind : zaak.getKinderen()) {
      ZaakItemRubriek rubriek = addRubriek("Kind - " + kind.getNaam().getPred_adel_voorv_gesl_voorn());
      String geboren = kind.getDatumGeboorte().getFormatDate() + " om " + kind.getTijdGeboorte().getFormatTime(
          "HH:mm");
      rubriek.add("Geboren op", geboren);
      rubriek.add("Geslacht", kind.getGeslacht());
      rubriek.add("Naamskeuze", kind.getNaamskeuzeType().getType());
    }
  }
}

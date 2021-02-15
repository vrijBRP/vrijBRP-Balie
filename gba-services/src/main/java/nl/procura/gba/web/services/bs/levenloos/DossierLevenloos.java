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

package nl.procura.gba.web.services.bs.levenloos;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.standard.Globalfunctions.*;

import java.math.BigDecimal;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.Doodsoorzaak;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.gba.web.services.bs.algemeen.enums.TermijnLijkbezorging;
import nl.procura.gba.web.services.bs.algemeen.enums.WijzeLijkbezorging;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijdenLijkbezorging;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierLevenloos extends DossierGeboorte implements DossierOverlijdenLijkbezorging {

  private FieldValue landBestemming = new FieldValue();

  public DossierLevenloos() {
    setDossier(new Dossier(ZaakType.LEVENLOOS, this));
    getDossier().toevoegenPersoon(AANGEVER);
    getDossier().toevoegenPersoon(VADER_DUO_MOEDER);
    getDossier().toevoegenPersoon(MOEDER);
  }

  @Override
  public String getAkteAanduiding() {
    return "B"; // Levenloos
  }

  // Lijkbezorging

  @Override
  public String getBestemming() {
    return trim(getPlaatsBestemming() + ", " + getLandBestemming());
  }

  @Override
  public String getBuitenBeneluxTekst() {

    StringBuilder lijkb = new StringBuilder();

    String bestBene = isBuitenBenelux() ? "Ja" : "Nee";
    String bestLand = getLandBestemming().getDescription();
    String bestPlaats = getPlaatsBestemming();
    String bestVia = getViaBestemming();
    String bestDo = getDoodsoorzaak().getOms();
    String bestVervoer = getVervoermiddel();

    lijkb.append(bestBene);

    if (fil(bestLand)) {
      lijkb.append(", land: " + bestLand + ", ");
    }

    if (fil(bestPlaats)) {
      lijkb.append("plaats: " + bestPlaats + ", ");
    }

    if (fil(bestDo)) {
      lijkb.append("doodsoorzaak: " + bestDo + ", ");
    }

    if (fil(bestVia)) {
      lijkb.append("via: " + bestVia + ", ");
    }

    if (fil(bestVervoer)) {
      lijkb.append("met: " + bestVervoer);
    }

    return trim(lijkb.toString());
  }

  @Override
  public DateTime getDatumLijkbezorging() {
    return new DateTime(getdLijkbez());
  }

  @Override
  public void setDatumLijkbezorging(DateTime dateTime) {
    setdLijkbez(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  @Override
  public Doodsoorzaak getDoodsoorzaak() {
    return Doodsoorzaak.get(getDoodsoorz());
  }

  @Override
  public void setDoodsoorzaak(Doodsoorzaak doodsoorzaak) {
    setDoodsoorz(doodsoorzaak.getCode());
  }

  @Override
  public FieldValue getLandBestemming() {
    return landBestemming;
  }

  @Override
  public void setLandBestemming(FieldValue landBestemming) {
    this.landBestemming = FieldValue.from(landBestemming);
    setcLandBest(this.landBestemming.getBigDecimalValue());
  }

  @Override
  public OntvangenDocument getOntvangenDocumentLijkbezorging() {
    return OntvangenDocument.get(getOntvDoc1());
  }

  @Override
  public void setOntvangenDocumentLijkbezorging(OntvangenDocument document) {
    setOntvDoc1(document != null ? document.getCode() : "");
  }

  @Override
  public String getPlaatsOntleding() {
    return getpOntleding();
  }

  @Override
  public void setPlaatsOntleding(String plaats) {
    setpOntleding(plaats);
  }

  @Override
  public TermijnLijkbezorging getTermijnLijkbezorging() {
    return TermijnLijkbezorging.get(getTermijnBez());
  }

  @Override
  public void setTermijnLijkbezorging(TermijnLijkbezorging termijnLijkbezorging) {
    setTermijnBez(termijnLijkbezorging.getCode());
  }

  @Override
  public DateTime getTijdLijkbezorging() {
    return new DateTime(0, gettLijkbez().longValue());
  }

  @Override
  public void setTijdLijkbezorging(DateTime dateTime) {
    settLijkbez(BigDecimal.valueOf(dateTime.getLongTime()));
  }

  @Override
  public String getTijdLijkbezorgingStandaard() {
    return gettLijkbez().longValue() >= 0 ? (" om " + getTijdLijkbezorging().getFormatTime("HH.mm")) : "";
  }

  @Override
  public String getVervoermiddel() {
    return getVervrmid();
  }

  @Override
  public void setVervoermiddel(String vervoermiddel) {
    setVervrmid(vervoermiddel);
  }

  @Override
  public WijzeLijkbezorging getWijzeLijkBezorging() {
    return WijzeLijkbezorging.get(getWijzeBez());
  }

  @Override
  public void setWijzeLijkBezorging(WijzeLijkbezorging wijzeLijkBezorging) {
    setWijzeBez(wijzeLijkBezorging.getCode());
  }

  @Override
  public boolean isBuitenBenelux() {
    return pos(getbBuitBnlx());
  }

  @Override
  public void setBuitenBenelux(boolean buitenBenelux) {
    setbBuitBnlx(toBigDecimal(buitenBenelux ? 1 : 0));
  }
}

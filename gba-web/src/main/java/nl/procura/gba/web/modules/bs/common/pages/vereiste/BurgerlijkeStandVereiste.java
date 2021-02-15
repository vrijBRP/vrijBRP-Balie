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

package nl.procura.gba.web.modules.bs.common.pages.vereiste;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

public class BurgerlijkeStandVereiste {

  private DossierPersoon[] personen;
  private String           dossierVereiste = "";
  private String           id;
  private String           bekendeInformatie;
  private String           defaultVoldoet;
  private String           voldoet;
  private String           overruleReason  = "";

  public BurgerlijkeStandVereiste(String id, String bekendeInformatie, String defaultVoldoet,
      DossierPersoon... personen) {

    this.id = id;
    this.bekendeInformatie = bekendeInformatie;
    this.defaultVoldoet = defaultVoldoet;
    this.voldoet = defaultVoldoet;
    this.voldoet = defaultVoldoet;
    this.personen = personen;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    BurgerlijkeStandVereiste other = (BurgerlijkeStandVereiste) obj;

    if (id == null) {
      return other.id == null;
    } else {
      return id.equals(other.id);
    }
  }

  public String getBekendeInformatie() {
    return bekendeInformatie;
  }

  public void setBekendeInformatie(String bekendeInformatie) {
    this.bekendeInformatie = bekendeInformatie;
  }

  public String getDefaultVoldoet() {
    return defaultVoldoet;
  }

  public void setDefaultVoldoet(String voldoet) {
    this.defaultVoldoet = voldoet;
  }

  public String getDossierVereiste() {
    return dossierVereiste;
  }

  public void setDossierVereiste(String dossierVereiste) {
    this.dossierVereiste = dossierVereiste;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNamen() {

    StringBuilder sb = new StringBuilder();

    for (DossierPersoon persoon : personen) {
      sb.append(persoon.getNaam().getPred_adel_voorv_gesl_voorn());
      sb.append(", ");
    }

    return trim(sb.toString());
  }

  public String getOverruleReason() {
    return overruleReason;
  }

  public void setOverruleReason(String overruleReason) {
    this.overruleReason = overruleReason;
  }

  public DossierPersoon[] getPersonen() {
    return personen;
  }

  public void setPersonen(DossierPersoon... personen) {
    this.personen = personen;
  }

  public String getVoldoet() {
    return voldoet;
  }

  public void setVoldoet(String voldoet) {
    this.voldoet = voldoet;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  public boolean isOverruled() {
    return !astr(getVoldoet()).equals(astr(getDefaultVoldoet()));
  }

}

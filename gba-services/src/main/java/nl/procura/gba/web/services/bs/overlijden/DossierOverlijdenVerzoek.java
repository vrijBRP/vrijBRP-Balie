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

package nl.procura.gba.web.services.bs.overlijden;

import java.math.BigDecimal;
import java.util.List;

import nl.procura.gba.common.UniqueList;
import nl.procura.gba.jpa.personen.db.DossCorrDest;
import nl.procura.gba.jpa.personen.db.DossOverlUitt;

public class DossierOverlijdenVerzoek {

  private DossCorrDest                    correspondentie;
  private List<DossOverlUitt>             uittreksels;
  private final AbstractDossierOverlijden dossierOverlijden;

  public DossierOverlijdenVerzoek(AbstractDossierOverlijden dossierOverlijden) {
    this.dossierOverlijden = dossierOverlijden;
    dossierOverlijden.setVerzoekInd(false);
    dossierOverlijden.setVerzoekOverlVoorn("");
    dossierOverlijden.setVerzoekOverlGeslNaam("");
    dossierOverlijden.setVerzoekOverlVoorv("");
    dossierOverlijden.setVerzoekOverlTitel("");
    dossierOverlijden.setVerzoekOverlGeboortedatum(BigDecimal.valueOf(-1L));
    dossierOverlijden.setVerzoekOverlGeboorteplaats("");
    dossierOverlijden.setVerzoekOverlGeboorteland(BigDecimal.valueOf(-1L));
  }

  public boolean isVerzoekInd() {
    return dossierOverlijden.isVerzoekInd();
  }

  public void setVerzoekInd(boolean verzoekInd) {
    dossierOverlijden.setVerzoekInd(verzoekInd);
  }

  public String getVoorn() {
    return dossierOverlijden.getVerzoekOverlVoorn();
  }

  public void setVoorn(String verzoekOverlVoorn) {
    dossierOverlijden.setVerzoekOverlVoorn(verzoekOverlVoorn);
  }

  public String getGeslNaam() {
    return dossierOverlijden.getVerzoekOverlGeslNaam();
  }

  public void setGeslNaam(String verzoekOverlGeslNaam) {
    dossierOverlijden.setVerzoekOverlGeslNaam(verzoekOverlGeslNaam);
  }

  public String getVoorv() {
    return dossierOverlijden.getVerzoekOverlVoorv();
  }

  public void setVoorv(String verzoekOverlVoorv) {
    dossierOverlijden.setVerzoekOverlVoorv(verzoekOverlVoorv);
  }

  public String getTitel() {
    return dossierOverlijden.getVerzoekOverlTitel();
  }

  public void setTitel(String verzoekOverlTitel) {
    dossierOverlijden.setVerzoekOverlTitel(verzoekOverlTitel);
  }

  public Integer getGeboortedatum() {
    return dossierOverlijden.getVerzoekOverlGeboortedatum().intValue();
  }

  public void setGeboortedatum(Integer verzoekOverlGeboortedatum) {
    dossierOverlijden.setVerzoekOverlGeboortedatum(BigDecimal.valueOf(verzoekOverlGeboortedatum));
  }

  public String getGeboorteplaats() {
    return dossierOverlijden.getVerzoekOverlGeboorteplaats();
  }

  public void setGeboorteplaats(String geboorteplaats) {
    dossierOverlijden.setVerzoekOverlGeboorteplaats(geboorteplaats);
  }

  public Integer getGeboorteland() {
    return dossierOverlijden.getVerzoekOverlGeboorteland().intValue();
  }

  public void setGeboorteland(Integer geboorteland) {
    dossierOverlijden.setVerzoekOverlGeboorteland(BigDecimal.valueOf(geboorteland));
  }

  public List<DossOverlUitt> getUittreksels() {
    if (uittreksels == null) {
      uittreksels = new UniqueList<>();
      uittreksels.addAll(dossierOverlijden.getDossOverlUitts());
    }
    return uittreksels;
  }

  public DossCorrDest getCorrespondentie() {
    if (correspondentie == null) {
      correspondentie = dossierOverlijden.getDossCorrDest();
    }
    return correspondentie;
  }
}

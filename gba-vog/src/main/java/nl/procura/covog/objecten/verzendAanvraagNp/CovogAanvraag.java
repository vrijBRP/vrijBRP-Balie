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

package nl.procura.covog.objecten.verzendAanvraagNp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "aanvraag")
public class CovogAanvraag {

  protected CovogOnderzoeksPersoonNP onderzoeksPersoonNP;
  protected CovogBelanghebbende      belanghebbende;
  private String                     aanvraagNummer;
  private String                     gemeenteCode;
  private String                     locatieCode;
  private String                     aanvraagdatum;
  private String                     doelCode;
  private String                     toelichtingDoel;
  private String                     indicatiePersisteren;
  private String                     indicatieBijzonderheden;
  private String                     indicatieOmstandigheden;
  private String                     indicatieCovogAdvies;
  private String                     opmerkingGemeente;
  private String                     burgemeestersadvies;

  public CovogAanvraag() {
  }

  public CovogAanvraag(String aanvraagNummer, String gemeenteCode, String locatieCode, String aanvraagdatum,
      String doelCode, String toelichtingDoel, String indicatiePersisteren,
      String indicatieBijzonderheden, String indicatieOmstandigheden, String indicatieCovogAdvies,
      String opmerkingGemeente, String burgemeestersadvies,
      CovogOnderzoeksPersoonNP onderzoeksPersoonNP, CovogBelanghebbende belanghebbende) {

    this.aanvraagNummer = aanvraagNummer;
    this.gemeenteCode = gemeenteCode;
    this.locatieCode = locatieCode;
    this.aanvraagdatum = aanvraagdatum;
    this.doelCode = doelCode;
    this.toelichtingDoel = toelichtingDoel;
    this.indicatiePersisteren = indicatiePersisteren;
    this.indicatieBijzonderheden = indicatieBijzonderheden;
    this.indicatieOmstandigheden = indicatieOmstandigheden;
    this.indicatieCovogAdvies = indicatieCovogAdvies;
    this.opmerkingGemeente = opmerkingGemeente;
    this.burgemeestersadvies = burgemeestersadvies;
    this.onderzoeksPersoonNP = onderzoeksPersoonNP;
    this.belanghebbende = belanghebbende;
  }

  public String getAanvraagNummer() {
    return aanvraagNummer;
  }

  public void setAanvraagNummer(String aanvraagNummer) {
    this.aanvraagNummer = aanvraagNummer;
  }

  public String getGemeenteCode() {
    return gemeenteCode;
  }

  public void setGemeenteCode(String gemeenteCode) {
    this.gemeenteCode = gemeenteCode;
  }

  public String getLocatieCode() {
    return locatieCode;
  }

  public void setLocatieCode(String locatieCode) {
    this.locatieCode = locatieCode;
  }

  public String getAanvraagdatum() {
    return aanvraagdatum;
  }

  public void setAanvraagdatum(String aanvraagdatum) {
    this.aanvraagdatum = aanvraagdatum;
  }

  public String getDoelCode() {
    return doelCode;
  }

  public void setDoelCode(String doelCode) {
    this.doelCode = doelCode;
  }

  public String getToelichtingDoel() {
    return toelichtingDoel;
  }

  public void setToelichtingDoel(String toelichtingDoel) {
    this.toelichtingDoel = toelichtingDoel;
  }

  public String getIndicatiePersisteren() {
    return indicatiePersisteren;
  }

  public void setIndicatiePersisteren(String indicatiePersisteren) {
    this.indicatiePersisteren = indicatiePersisteren;
  }

  public String getIndicatieBijzonderheden() {
    return indicatieBijzonderheden;
  }

  public void setIndicatieBijzonderheden(String indicatieBijzonderheden) {
    this.indicatieBijzonderheden = indicatieBijzonderheden;
  }

  public String getIndicatieOmstandigheden() {
    return indicatieOmstandigheden;
  }

  public void setIndicatieOmstandigheden(String indicatieOmstandigheden) {
    this.indicatieOmstandigheden = indicatieOmstandigheden;
  }

  public String getIndicatieCovogAdvies() {
    return indicatieCovogAdvies;
  }

  public void setIndicatieCovogAdvies(String indicatieCovogAdvies) {
    this.indicatieCovogAdvies = indicatieCovogAdvies;
  }

  public String getOpmerkingGemeente() {
    return opmerkingGemeente;
  }

  public void setOpmerkingGemeente(String opmerkingGemeente) {
    this.opmerkingGemeente = opmerkingGemeente;
  }

  public String getBurgemeestersadvies() {
    return burgemeestersadvies;
  }

  public void setBurgemeestersadvies(String burgemeestersadvies) {
    this.burgemeestersadvies = burgemeestersadvies;
  }

  public CovogOnderzoeksPersoonNP getOnderzoeksPersoonNP() {
    return onderzoeksPersoonNP;
  }

  public void setOnderzoeksPersoonNP(CovogOnderzoeksPersoonNP onderzoeksPersoonNP) {
    this.onderzoeksPersoonNP = onderzoeksPersoonNP;
  }

  public CovogBelanghebbende getBelanghebbende() {
    return belanghebbende;
  }

  public void setBelanghebbende(CovogBelanghebbende belanghebbende) {
    this.belanghebbende = belanghebbende;
  }
}

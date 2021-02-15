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

package nl.procura.gba.web.services.bs.onderzoek;

import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.standard.Globalfunctions.trim;

import org.apache.commons.lang3.StringUtils;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.jpa.personen.db.DossOnderz;
import nl.procura.gba.jpa.personen.db.DossOnderzBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingBronType;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierOnderzoekBron extends DossOnderzBron {

  private FieldValue woonplaats = new FieldValue();
  private FieldValue gemeente   = new FieldValue();
  private FieldValue land       = new FieldValue();

  public DossierOnderzoekBron() {
    super();
    setBron("");
    setGesprek("");
    setSummonsType(AanschrijvingBronType.OVERIGE);
    setAdresType(VermoedAdresType.ONBEKEND);
  }

  public Long getCode() {
    return getcDossOnderzBron();
  }

  public void setDossier(DossierOnderzoek dossier) {
    setDossOnderz(ReflectionUtil.deepCopyBean(DossOnderz.class, dossier));
  }

  public VermoedAdresType getAdresType() {
    return VermoedAdresType.get(super.getAdrType().intValue());
  }

  public void setAdresType(VermoedAdresType adres) {
    super.setAdrType(toBigDecimal(adres != null ? adres.getCode() : -1));
  }

  public AanschrijvingBronType getSummonsType() {
    return AanschrijvingBronType.get(super.getSummType().intValue());
  }

  public void setSummonsType(AanschrijvingBronType type) {
    super.setSummType(toBigDecimal(type != null ? type.getCode() : -1));
  }

  public Adresformats getAdres() {
    switch (getSummonsType()) {
      case PERSOON:
      case GERELATEERDE:
      case BELANGHEBBENDE:
        return new Adresformats().setValues(
            getAdresStraat().getDescription(),
            "",
            "",
            "",
            "", "",
            getAdresPc().getDescription(), "",
            getPlaats(),
            "",
            "",
            "",
            "",
            "",
            "",
            "");
      default:
      case OVERIGE:
        return new Adresformats().setValues(getAdresStraat().getDescription(),
            getHnr(), getHnrL(),
            getHnrT(),
            getAdresHnrA().getDescription(), "",
            getAdresPc().getDescription(), "",
            getAdresPlaats().getDescription(),
            getGemeente().getDescription(), "",
            getLand().getDescription(), "",
            getBuitenl1(), getBuitenl2(),
            getBuitenl3());
    }
  }

  public FieldValue getAdresStraat() {
    return new FieldValue(super.getAdr());
  }

  public void setAdresStraat(FieldValue adres) {
    super.setAdr(FieldValue.from(adres).getStringValue());
  }

  public FieldValue getAdresHnrA() {
    return new FieldValue(super.getHnrA());
  }

  public void setAdresHnrA(FieldValue hnrA) {
    super.setHnrA(FieldValue.from(hnrA).getStringValue());
  }

  public FieldValue getAdresPc() {
    return new FieldValue(super.getPc());
  }

  public void setAdresPc(FieldValue pc) {
    super.setPc(FieldValue.from(pc).getStringValue());
  }

  public FieldValue getAdresPlaats() {
    return woonplaats;
  }

  public void setAdresPlaats(FieldValue plaats) {
    this.woonplaats = FieldValue.from(plaats);
    super.setPlaats(plaats.getStringValue());
  }

  public FieldValue getGemeente() {
    return gemeente;
  }

  public void setGemeente(FieldValue gemeente) {
    this.gemeente = FieldValue.from(gemeente);
    super.setcGem(FieldValue.from(gemeente).getBigDecimalValue());
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = FieldValue.from(land);
    super.setcLand(FieldValue.from(land).getBigDecimalValue());
  }

  public String getInstTav() {
    return trim(StringUtils.uncapitalize(getInstAanhef()) + " " + getInstVoorl() + " " + getInstNaam());
  }
}

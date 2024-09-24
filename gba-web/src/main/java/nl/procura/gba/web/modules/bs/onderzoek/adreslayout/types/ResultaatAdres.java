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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout.types;

import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.OnderzoekAdres;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ResultaatAdres implements OnderzoekAdres {

  private final DossierOnderzoek onderzoek;

  public ResultaatAdres(DossierOnderzoek onderzoek) {
    this.onderzoek = onderzoek;
  }

  @Override
  public FieldValue getAdres() {
    return onderzoek.getResultaatAdres();
  }

  @Override
  public void setAdres(FieldValue adres) {
    onderzoek.setResultaatAdres(adres);
  }

  @Override
  public String getHnr() {
    return onderzoek.getResultaatHnr();
  }

  @Override
  public void setHnr(String hnr) {
    onderzoek.setResultaatHnr(hnr);
  }

  @Override
  public String getHnrL() {
    return onderzoek.getResultaatHnrL();
  }

  @Override
  public void setHnrL(String hnrL) {
    onderzoek.setResultaatHnrL(hnrL);
  }

  @Override
  public FieldValue getHnrA() {
    return onderzoek.getResultaatHnrA();
  }

  @Override
  public void setHnrA(FieldValue hnrA) {
    onderzoek.setResultaatHnrA(hnrA);
  }

  @Override
  public String getHnrT() {
    return onderzoek.getResultaatHnrT();
  }

  @Override
  public void setHnrT(String hnrT) {
    onderzoek.setResultaatHnrT(hnrT);
  }

  @Override
  public FieldValue getPc() {
    return onderzoek.getResultaatPc();
  }

  @Override
  public void setPc(FieldValue pc) {
    onderzoek.setResultaatPc(pc);
  }

  @Override
  public FieldValue getPlaats() {
    return onderzoek.getResultaatPlaats();
  }

  @Override
  public void setPlaats(FieldValue plaats) {
    onderzoek.setResultaatPlaats(plaats);
  }

  @Override
  public FieldValue getGemeente() {
    return onderzoek.getResultaatGemeente();
  }

  @Override
  public void setGemeente(FieldValue gemeente) {
    onderzoek.setResultaatGemeente(gemeente);
  }

  @Override
  public String getBuitenl1() {
    return onderzoek.getResultaatBuitenl1();
  }

  @Override
  public void setBuitenl1(String buitenl1) {
    onderzoek.setResultaatBuitenl1(buitenl1);
  }

  @Override
  public String getBuitenl2() {
    return onderzoek.getResultaatBuitenl2();
  }

  @Override
  public void setBuitenl2(String buitenl2) {
    onderzoek.setResultaatBuitenl2(buitenl2);
  }

  @Override
  public String getBuitenl3() {
    return onderzoek.getResultaatBuitenl3();
  }

  @Override
  public void setBuitenl3(String buitenl3) {
    onderzoek.setResultaatBuitenl3(buitenl3);
  }

  @Override
  public FieldValue getLand() {
    return onderzoek.getResultaatLand();
  }

  @Override
  public void setLand(FieldValue land) {
    onderzoek.setResultaatLand(land);
  }

  @Override
  public void setGemeentePostbus(Gemeente gemeente) {
    onderzoek.setResultaatGemeentePostbus(gemeente);
  }

  @Override
  public FieldValue getAantalPersonen() {
    return onderzoek.getResultaatAantalPersonen();
  }

  @Override
  public void setAantalPersonen(FieldValue aantalPersonen) {
    onderzoek.setResultaatAantalPersonen(aantalPersonen);
  }

  @Override
  public AddressSourceType getSource() {
    return onderzoek.getResultaatSource();
  }

  @Override
  public void setSource(AddressSourceType sourceType) {
    onderzoek.setResultaatSource(sourceType);
  }
}

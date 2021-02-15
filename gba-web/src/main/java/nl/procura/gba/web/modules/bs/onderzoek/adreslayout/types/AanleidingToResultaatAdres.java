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

import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.Adres;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class AanleidingToResultaatAdres implements Adres {

  private final DossierOnderzoek onderzoek;

  public AanleidingToResultaatAdres(DossierOnderzoek onderzoek) {
    this.onderzoek = onderzoek;
  }

  @Override
  public FieldValue getAdres() {
    return onderzoek.getAanleidingAdres();
  }

  @Override
  public void setAdres(FieldValue adres) {
    onderzoek.setResultaatAdres(adres);
  }

  @Override
  public String getHnr() {
    return onderzoek.getAanleidingHnr();
  }

  @Override
  public void setHnr(String hnr) {
    onderzoek.setResultaatHnr(hnr);
  }

  @Override
  public String getHnrL() {
    return onderzoek.getAanleidingHnrL();
  }

  @Override
  public void setHnrL(String hnrL) {
    onderzoek.setResultaatHnrL(hnrL);
  }

  @Override
  public FieldValue getHnrA() {
    return onderzoek.getAanleidingHnrA();
  }

  @Override
  public void setHnrA(FieldValue hnrA) {
    onderzoek.setResultaatHnrA(hnrA);
  }

  @Override
  public String getHnrT() {
    return onderzoek.getAanleidingHnrT();
  }

  @Override
  public void setHnrT(String hnrT) {
    onderzoek.setResultaatHnrT(hnrT);
  }

  @Override
  public FieldValue getPc() {
    return onderzoek.getAanleidingPc();
  }

  @Override
  public void setPc(FieldValue pc) {
    onderzoek.setResultaatPc(pc);
  }

  @Override
  public FieldValue getPlaats() {
    return onderzoek.getAanleidingPlaats();
  }

  @Override
  public void setPlaats(FieldValue plaats) {
    onderzoek.setResultaatPlaats(plaats);
  }

  @Override
  public FieldValue getGemeente() {
    return onderzoek.getAanleidingGemeente();
  }

  @Override
  public void setGemeente(FieldValue gemeente) {
    onderzoek.setResultaatGemeente(gemeente);
  }

  @Override
  public String getBuitenl1() {
    return onderzoek.getAanleidingBuitenl1();
  }

  @Override
  public void setBuitenl1(String buitenl1) {
    onderzoek.setResultaatBuitenl1(buitenl1);
  }

  @Override
  public String getBuitenl2() {
    return onderzoek.getAanleidingBuitenl2();
  }

  @Override
  public void setBuitenl2(String buitenl2) {
    onderzoek.setResultaatBuitenl2(buitenl2);
  }

  @Override
  public String getBuitenl3() {
    return onderzoek.getAanleidingBuitenl3();
  }

  @Override
  public void setBuitenl3(String buitenl3) {
    onderzoek.setResultaatBuitenl3(buitenl3);
  }

  @Override
  public FieldValue getLand() {
    return onderzoek.getAanleidingLand();
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
}

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
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class BronAdres implements Adres {

  private final DossierOnderzoekBron bron;

  public BronAdres(DossierOnderzoekBron bron) {
    this.bron = bron;
  }

  @Override
  public FieldValue getAdres() {
    return bron.getAdresStraat();
  }

  @Override
  public void setAdres(FieldValue adres) {
    bron.setAdresStraat(adres);
  }

  @Override
  public String getHnr() {
    return bron.getHnr();
  }

  @Override
  public void setHnr(String hnr) {
    bron.setHnr(hnr);
  }

  @Override
  public String getHnrL() {
    return bron.getHnrL();
  }

  @Override
  public void setHnrL(String hnrL) {
    bron.setHnrL(hnrL);
  }

  @Override
  public FieldValue getHnrA() {
    return bron.getAdresHnrA();
  }

  @Override
  public void setHnrA(FieldValue hnrA) {
    bron.setAdresHnrA(hnrA);
  }

  @Override
  public String getHnrT() {
    return bron.getHnrT();
  }

  @Override
  public void setHnrT(String hnrT) {
    bron.setHnrT(hnrT);
  }

  @Override
  public FieldValue getPc() {
    return bron.getAdresPc();
  }

  @Override
  public void setPc(FieldValue pc) {
    bron.setAdresPc(pc);
  }

  @Override
  public FieldValue getPlaats() {
    return bron.getAdresPlaats();
  }

  @Override
  public void setPlaats(FieldValue plaats) {
    bron.setAdresPlaats(plaats);
  }

  @Override
  public FieldValue getGemeente() {
    return bron.getGemeente();
  }

  @Override
  public void setGemeente(FieldValue gemeente) {
    bron.setGemeente(gemeente);
  }

  @Override
  public String getBuitenl1() {
    return bron.getBuitenl1();
  }

  @Override
  public void setBuitenl1(String buitenl1) {
    bron.setBuitenl1(buitenl1);
  }

  @Override
  public String getBuitenl2() {
    return bron.getBuitenl2();
  }

  @Override
  public void setBuitenl2(String buitenl2) {
    bron.setBuitenl2(buitenl2);
  }

  @Override
  public String getBuitenl3() {
    return bron.getBuitenl3();
  }

  @Override
  public void setBuitenl3(String buitenl3) {
    bron.setBuitenl3(buitenl3);
  }

  @Override
  public FieldValue getLand() {
    return bron.getLand();
  }

  @Override
  public void setLand(FieldValue land) {
    bron.setLand(land);
  }

  @Override
  public void setGemeentePostbus(Gemeente vermoedelijkeGemeente) {
  }

  @Override
  public FieldValue getAantalPersonen() {
    return null;
  }

  @Override
  public void setAantalPersonen(FieldValue aantalPersonen) {
  }
}

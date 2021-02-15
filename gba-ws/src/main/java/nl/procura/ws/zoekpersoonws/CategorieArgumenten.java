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

package nl.procura.ws.zoekpersoonws;

public class CategorieArgumenten {

  private boolean archief         = true;
  private boolean alleen_actueel  = false;
  private boolean niet_expanderen = false;
  private boolean gerelateerden   = false;
  private int     databron        = 0;
  private boolean zoek_op_adres   = false;

  private boolean cat_persoon;                  // 01
  private boolean cat_ouder_1;                  // 02
  private boolean cat_ouder_2;                  // 03
  private boolean cat_nationaliteiten;          // 04
  private boolean cat_huwelijken;               // 05
  private boolean cat_overlijden;               // 06
  private boolean cat_inschrijving;             // 07
  private boolean cat_verblijfplaats;           // 08
  private boolean cat_kinderen;                 // 09
  private boolean cat_verblijfstitel;           // 10
  private boolean cat_gezag;                    // 11
  private boolean cat_reisdocumenten;           // 12
  private boolean cat_kiesrecht;                // 13
  private boolean cat_afnemers;                 // 14
  private boolean cat_verwijzing;               // 21
  private boolean cat_diversen;                 // 30
  private boolean cat_rijbewijs;                // 31
  private boolean cat_woningkaart;              // 32
  private boolean cat_kladblokaantekening;      // 33
  private boolean cat_lokaleafnemersindicaties; // 34

  public CategorieArgumenten() {
  }

  public boolean isAlleen_actueel() {
    return alleen_actueel;
  }

  public void setAlleen_actueel(boolean alleen_actueel) {
    this.alleen_actueel = alleen_actueel;
  }

  public boolean isNiet_expanderen() {
    return niet_expanderen;
  }

  public void setNiet_expanderen(boolean niet_expanderen) {
    this.niet_expanderen = niet_expanderen;
  }

  public boolean isGerelateerden() {
    return gerelateerden;
  }

  public void setGerelateerden(boolean gerelateerden) {
    this.gerelateerden = gerelateerden;
  }

  public boolean isCat_persoon() {
    return cat_persoon;
  }

  public void setCat_persoon(boolean cat_persoon) {
    this.cat_persoon = cat_persoon;
  }

  public boolean isCat_ouder_1() {
    return cat_ouder_1;
  }

  public void setCat_ouder_1(boolean cat_ouder_1) {
    this.cat_ouder_1 = cat_ouder_1;
  }

  public boolean isCat_ouder_2() {
    return cat_ouder_2;
  }

  public void setCat_ouder_2(boolean cat_ouder_2) {
    this.cat_ouder_2 = cat_ouder_2;
  }

  public boolean isCat_nationaliteiten() {
    return cat_nationaliteiten;
  }

  public void setCat_nationaliteiten(boolean cat_nationaliteiten) {
    this.cat_nationaliteiten = cat_nationaliteiten;
  }

  public boolean isCat_huwelijken() {
    return cat_huwelijken;
  }

  public void setCat_huwelijken(boolean cat_huwelijken) {
    this.cat_huwelijken = cat_huwelijken;
  }

  public boolean isCat_overlijden() {
    return cat_overlijden;
  }

  public void setCat_overlijden(boolean cat_overlijden) {
    this.cat_overlijden = cat_overlijden;
  }

  public boolean isCat_inschrijving() {
    return cat_inschrijving;
  }

  public void setCat_inschrijving(boolean cat_inschrijving) {
    this.cat_inschrijving = cat_inschrijving;
  }

  public boolean isCat_verblijfplaats() {
    return cat_verblijfplaats;
  }

  public void setCat_verblijfplaats(boolean cat_verblijfplaats) {
    this.cat_verblijfplaats = cat_verblijfplaats;
  }

  public boolean isCat_kinderen() {
    return cat_kinderen;
  }

  public void setCat_kinderen(boolean cat_kinderen) {
    this.cat_kinderen = cat_kinderen;
  }

  public boolean isCat_verblijfstitel() {
    return cat_verblijfstitel;
  }

  public void setCat_verblijfstitel(boolean cat_verblijfstitel) {
    this.cat_verblijfstitel = cat_verblijfstitel;
  }

  public boolean isCat_gezag() {
    return cat_gezag;
  }

  public void setCat_gezag(boolean cat_gezag) {
    this.cat_gezag = cat_gezag;
  }

  public boolean isCat_reisdocumenten() {
    return cat_reisdocumenten;
  }

  public void setCat_reisdocumenten(boolean cat_reisdocumenten) {
    this.cat_reisdocumenten = cat_reisdocumenten;
  }

  public boolean isCat_kiesrecht() {
    return cat_kiesrecht;
  }

  public void setCat_kiesrecht(boolean cat_kiesrecht) {
    this.cat_kiesrecht = cat_kiesrecht;
  }

  public boolean isCat_afnemers() {
    return cat_afnemers;
  }

  public void setCat_afnemers(boolean cat_afnemers) {
    this.cat_afnemers = cat_afnemers;
  }

  public boolean isCat_verwijzing() {
    return cat_verwijzing;
  }

  public void setCat_verwijzing(boolean cat_verwijzing) {
    this.cat_verwijzing = cat_verwijzing;
  }

  public boolean isCat_diversen() {
    return cat_diversen;
  }

  public void setCat_diversen(boolean cat_diversen) {
    this.cat_diversen = cat_diversen;
  }

  public boolean isCat_rijbewijs() {
    return cat_rijbewijs;
  }

  public void setCat_rijbewijs(boolean cat_rijbewijs) {
    this.cat_rijbewijs = cat_rijbewijs;
  }

  public boolean isCat_woningkaart() {
    return cat_woningkaart;
  }

  public void setCat_woningkaart(boolean cat_woningkaart) {
    this.cat_woningkaart = cat_woningkaart;
  }

  public boolean isCat_kladblokaantekening() {
    return cat_kladblokaantekening;
  }

  public void setCat_kladblokaantekening(boolean cat_kladblokaantekening) {
    this.cat_kladblokaantekening = cat_kladblokaantekening;
  }

  public boolean isCat_lokaleafnemersindicaties() {
    return cat_lokaleafnemersindicaties;
  }

  public void setCat_lokaleafnemersindicaties(boolean cat_lokaleafnemersindicaties) {
    this.cat_lokaleafnemersindicaties = cat_lokaleafnemersindicaties;
  }

  public boolean isArchief() {
    return archief;
  }

  public void setArchief(boolean archief) {
    this.archief = archief;
  }

  public int getDatabron() {
    return databron;
  }

  public void setDatabron(int databron) {
    this.databron = databron;
  }

  public boolean isZoek_op_adres() {
    return zoek_op_adres;
  }

  public void setZoek_op_adres(boolean zoek_op_adres) {
    this.zoek_op_adres = zoek_op_adres;
  }
}

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

package nl.procura.diensten.zoekpersoon.objecten;

import static java.util.Arrays.copyOf;

import java.util.Arrays;

public class Persoonslijst {

  private Persoon persoon;
  private Ouder   ouder_1;
  private Ouder   ouder_2;

  private Nationaliteit[]           nationaliteiten;
  private Huwelijk[]                huwelijken;
  private Kind[]                    kinderen;
  private Afnemer[]                 afnemers;
  private Reisdocument[]            reisdocumenten;
  private Lokaleafnemersindicatie[] lokaleafnemersindicaties;

  private Inschrijving        inschrijving;
  private Verblijfplaats      verblijfplaats;
  private Diversen            diversen;
  private Rijbewijs           rijbewijs;
  private Woningkaart         woningkaart;
  private Verwijzing          verwijzing;
  private Overlijden          overlijden;
  private Gezag               gezag;
  private Kiesrecht           kiesrecht;
  private Verblijfstitel      verblijfstitel;
  private Kladblokaantekening kladblokaantekening;
  private MetaInfoGegevens[]  metaInfoGegevens;

  public Persoonslijst() {
  }

  public Woningkaart getWoningkaart() {
    return woningkaart;
  }

  public void setWoningkaart(Woningkaart woningkaart) {
    this.woningkaart = woningkaart;
  }

  public Rijbewijs getRijbewijs() {
    return rijbewijs;
  }

  public void setRijbewijs(Rijbewijs rijbewijs) {
    this.rijbewijs = rijbewijs;
  }

  public Diversen getDiversen() {
    return diversen;
  }

  public void setDiversen(Diversen diversen) {
    this.diversen = diversen;
  }

  public Nationaliteit[] getNationaliteiten() {
    return nationaliteiten;
  }

  public void setNationaliteiten(Nationaliteit[] nationaliteiten) {
    this.nationaliteiten = Arrays.copyOf(nationaliteiten, nationaliteiten.length);
  }

  public Huwelijk[] getHuwelijken() {
    return huwelijken;
  }

  public void setHuwelijken(Huwelijk[] huwelijken) {
    this.huwelijken = Arrays.copyOf(huwelijken, huwelijken.length);
  }

  public Verblijfplaats getVerblijfplaats() {
    return verblijfplaats;
  }

  public void setVerblijfplaats(Verblijfplaats verblijfplaats) {
    this.verblijfplaats = verblijfplaats;
  }

  public Kind[] getKinderen() {
    return kinderen;
  }

  public void setKinderen(Kind[] kinderen) {
    this.kinderen = copyOf(kinderen, kinderen.length);
  }

  public Reisdocument[] getReisdocumenten() {
    return reisdocumenten;
  }

  public void setReisdocumenten(Reisdocument[] reisdocumenten) {
    this.reisdocumenten = copyOf(reisdocumenten, reisdocumenten.length);
  }

  public Afnemer[] getAfnemers() {
    return afnemers;
  }

  public void setAfnemers(Afnemer[] afnemers) {
    this.afnemers = copyOf(afnemers, afnemers.length);
  }

  public Persoon getPersoon() {
    return persoon;
  }

  public void setPersoon(Persoon persoon) {
    this.persoon = persoon;
  }

  public Ouder getOuder_1() {
    return ouder_1;
  }

  public void setOuder_1(Ouder ouder_1) {
    this.ouder_1 = ouder_1;
  }

  public Ouder getOuder_2() {
    return ouder_2;
  }

  public void setOuder_2(Ouder ouder_2) {
    this.ouder_2 = ouder_2;
  }

  public Inschrijving getInschrijving() {
    return inschrijving;
  }

  public void setInschrijving(Inschrijving inschrijving) {
    this.inschrijving = inschrijving;
  }

  public Verwijzing getVerwijzing() {
    return verwijzing;
  }

  public void setVerwijzing(Verwijzing verwijzing) {
    this.verwijzing = verwijzing;
  }

  public Overlijden getOverlijden() {
    return overlijden;
  }

  public void setOverlijden(Overlijden overlijden) {
    this.overlijden = overlijden;
  }

  public Gezag getGezag() {
    return gezag;
  }

  public void setGezag(Gezag gezag) {
    this.gezag = gezag;
  }

  public Kiesrecht getKiesrecht() {
    return kiesrecht;
  }

  public void setKiesrecht(Kiesrecht kiesrecht) {
    this.kiesrecht = kiesrecht;
  }

  public Verblijfstitel getVerblijfstitel() {
    return verblijfstitel;
  }

  public void setVerblijfstitel(Verblijfstitel verblijfstitel) {
    this.verblijfstitel = verblijfstitel;
  }

  public Kladblokaantekening getKladblokaantekening() {
    return kladblokaantekening;
  }

  public void setKladblokaantekening(Kladblokaantekening kladblokaantekening) {
    this.kladblokaantekening = kladblokaantekening;
  }

  public Lokaleafnemersindicatie[] getLokaleafnemersindicaties() {
    return lokaleafnemersindicaties;
  }

  public void setLokaleafnemersindicaties(Lokaleafnemersindicatie[] lokaleafnemersaantekeningen) {
    this.lokaleafnemersindicaties = copyOf(lokaleafnemersaantekeningen, lokaleafnemersaantekeningen.length);
  }

  public MetaInfoGegevens[] getMetaInfoGegevens() {
    return metaInfoGegevens;
  }

  public void setMetaInfoGegevens(MetaInfoGegevens[] metaInfoGegevens) {
    this.metaInfoGegevens = copyOf(metaInfoGegevens, metaInfoGegevens.length);
  }
}

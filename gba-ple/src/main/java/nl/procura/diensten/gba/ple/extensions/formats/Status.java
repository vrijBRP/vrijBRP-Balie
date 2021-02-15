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

package nl.procura.diensten.gba.ple.extensions.formats;

import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.HashSet;

import nl.procura.burgerzaken.gba.core.enums.GBACat;

public class Status {

  private int             mutaties            = 0;
  private boolean         overleden           = false;
  private boolean         verwijzing          = false;
  private boolean         onverwerktDocument  = false;
  private boolean         staatOndercuratele  = false;
  private boolean         ministerieelBesluit = false;
  private boolean         opgeschort          = false;
  private boolean         inOnderzoek         = false;
  private boolean         geheim              = false;
  private int             geheimCode          = 0;
  private boolean         emigratie           = false;
  private boolean         rni                 = false;
  private boolean         blokkering          = false;
  private boolean         adresInOnderzoek    = false;
  private boolean         heeftBriefAdres     = false;
  private HashSet<GBACat> catMutaties         = new HashSet<>();
  private String          message             = "";

  public String getOpsomming() {

    // de geheimcode wordt uit de PL gehaald!

    this.message = "";
    ch(isOverleden(), "overleden");
    ch(isVerwijzing(), "verwijzing");
    ch(isGeheim(), "verstrekkingsbeperking");
    ch(isInOnderzoek(), "gegevens in onderzoek");
    ch(isAdresInOnderzoek(), "adresgegevens in onderzoek");
    ch(isHeeftBriefAdres(), "briefadres");
    ch(isMinisterieelBesluit(), "ministrieel besluit");
    ch(isEmigratie(), "emigratie");
    ch(isRni(), "PL aangelegd in de RNI");
    ch(isBlokkering(), "blokkering");
    ch(getMutaties(), "mutatie", "mutaties");
    ch(isOnverwerktDocument(), "niet-verwerkt document");
    ch(isStaatOnderCuratele(), "staat onder curatele");

    return trim(this.message);
  }

  private void ch(int c, String m1, String m2) {

    StringBuilder sb = new StringBuilder();

    for (GBACat gc : getCatMutaties()) {
      sb.append("- ");
      sb.append(gc.getDescr());
      sb.append("<br/>");
    }

    if (pos(c)) {
      ch(true, (c == 1) ? (c + " " + m1) : c + " " + m2);
    }
  }

  private void ch(boolean b, String m) {
    this.message = (this.message + (b ? (", " + m) : ""));
  }

  public boolean isOverleden() {
    return overleden;
  }

  public void setOverleden(boolean overleden) {
    this.overleden = overleden;
  }

  public boolean isVerwijzing() {
    return verwijzing;
  }

  public void setVerwijzing(boolean verwijzing) {
    this.verwijzing = verwijzing;
  }

  public boolean isOnverwerktDocument() {
    return onverwerktDocument;
  }

  public void setOnverwerktDocument(boolean onverwerktDocument) {
    this.onverwerktDocument = onverwerktDocument;
  }

  public boolean isMinisterieelBesluit() {
    return ministerieelBesluit;
  }

  public void setMinisterieelBesluit(boolean ministerieelBesluit) {
    this.ministerieelBesluit = ministerieelBesluit;
  }

  public boolean isOpgeschort() {
    return opgeschort;
  }

  public void setOpgeschort(boolean opgeschort) {
    this.opgeschort = opgeschort;
  }

  public boolean isInOnderzoek() {
    return inOnderzoek;
  }

  public void setInOnderzoek(boolean inOnderzoek) {
    this.inOnderzoek = inOnderzoek;
  }

  public boolean isGeheim() {
    return geheim;
  }

  public void setGeheim(boolean geheim) {
    this.geheim = geheim;
  }

  public boolean isEmigratie() {
    return emigratie;
  }

  public void setEmigratie(boolean emigratie) {
    this.emigratie = emigratie;
  }

  public boolean isBlokkering() {
    return blokkering;
  }

  public void setBlokkering(boolean blokkering) {
    this.blokkering = blokkering;
  }

  public boolean isAdresInOnderzoek() {
    return adresInOnderzoek;
  }

  public void setAdresInOnderzoek(boolean adresInOnderzoek) {
    this.adresInOnderzoek = adresInOnderzoek;
  }

  public HashSet<GBACat> getCatMutaties() {
    return catMutaties;
  }

  public void setCatMutaties(HashSet<GBACat> catMutaties) {
    this.catMutaties = catMutaties;
  }

  public int getMutaties() {
    return mutaties;
  }

  public void setMutaties(int mutaties) {
    this.mutaties = mutaties;
  }

  public int getGeheimCode() {
    return geheimCode;
  }

  public void setGeheimCode(int geheimCode) {
    this.geheimCode = geheimCode;
  }

  public boolean isHeeftBriefAdres() {
    return heeftBriefAdres;
  }

  public void setHeeftBriefAdres(boolean heeftBriefAdres) {
    this.heeftBriefAdres = heeftBriefAdres;
  }

  public boolean isRni() {
    return rni;
  }

  public void setRni(boolean rni) {
    this.rni = rni;
  }

  public boolean isStaatOnderCuratele() {
    return staatOndercuratele;
  }

  public void setStaatOnderCuratele(boolean staatOndercuratele) {
    this.staatOndercuratele = staatOndercuratele;
  }
}

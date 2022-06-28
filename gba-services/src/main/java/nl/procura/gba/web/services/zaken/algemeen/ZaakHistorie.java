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

package nl.procura.gba.web.services.zaken.algemeen;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.zaken.algemeen.aantekening.AantekeningHistorie;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.AttribuutHistorie;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakBehandelaarHistorie;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificaties;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelaties;

import ch.lambdaj.Lambda;

public class ZaakHistorie {

  private List<String>            wijzigingen         = new ArrayList<>();
  private ZaakRelaties            relaties            = new ZaakRelaties();
  private ZaakIdentificaties      identificaties      = new ZaakIdentificaties();
  private AttribuutHistorie       attribuutHistorie   = new AttribuutHistorie();
  private ZaakBehandelaarHistorie behandelaarHistorie = new ZaakBehandelaarHistorie();
  private AantekeningHistorie     aantekeningHistorie = new AantekeningHistorie();
  private ZaakStatusHistorie      statusHistorie      = new ZaakStatusHistorie();

  public void addWijziging(String wijziging) {
    wijzigingen.add(wijziging);
  }

  public AantekeningHistorie getAantekeningHistorie() {
    return aantekeningHistorie;
  }

  public void setAantekeningHistorie(AantekeningHistorie aantekeningen) {
    this.aantekeningHistorie = aantekeningen;
  }

  public AttribuutHistorie getAttribuutHistorie() {
    return attribuutHistorie;
  }

  public void setAttribuutHistorie(AttribuutHistorie attribuutHistorie) {
    this.attribuutHistorie = attribuutHistorie;
  }

  public ZaakBehandelaarHistorie getBehandelaarHistorie() {
    return behandelaarHistorie;
  }

  public void setBehandelaarHistorie(ZaakBehandelaarHistorie behandelaarHistorie) {
    this.behandelaarHistorie = behandelaarHistorie;
  }

  public ZaakIdentificaties getIdentificaties() {
    return identificaties;
  }

  public void setIdentificaties(ZaakIdentificaties identificaties) {
    this.identificaties = identificaties;
  }

  public ZaakRelaties getRelaties() {
    return relaties;
  }

  public void setRelaties(ZaakRelaties relaties) {
    this.relaties = relaties;
  }

  public ZaakStatusHistorie getStatusHistorie() {
    return statusHistorie;
  }

  public void setStatusHistorie(ZaakStatusHistorie statussen) {
    this.statusHistorie = statussen;
  }

  public List<String> getWijzigingen() {
    return wijzigingen;
  }

  public void setWijzigingen(List<String> wijzigingen) {
    this.wijzigingen = wijzigingen;
  }

  public String getWijzigingenTekst() {
    return wijzigingen.size() > 0 ? Lambda.join(wijzigingen) : "";
  }

  public boolean isGewijzigd() {
    return wijzigingen.size() > 0;
  }
}

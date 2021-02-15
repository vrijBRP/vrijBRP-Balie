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

package nl.procura.gba.web.services.zaken.algemeen.controle;

import static ch.lambdaj.Lambda.join;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.ArrayList;
import java.util.List;

public class StandaardControle implements Controle {

  private String       onderwerp;
  private String       id          = "";
  private String       omschrijving;
  private boolean      gewijzigd   = false;
  private List<String> opmerkingen = new ArrayList<>();

  public StandaardControle(String onderwerp, String omschrijving) {
    this.onderwerp = onderwerp;
    this.omschrijving = omschrijving;
  }

  @Override
  public void addOpmerking(String opmerking) {
    opmerkingen.add(opmerking);
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  @Override
  public String getOnderwerp() {
    return onderwerp;
  }

  public void setOnderwerp(String onderwerp) {
    this.onderwerp = onderwerp;
  }

  @Override
  public List<String> getOpmerkingen() {
    return opmerkingen;
  }

  public void setOpmerkingen(List<String> opmerkingen) {
    this.opmerkingen = opmerkingen;
  }

  @Override
  public String getOpmerkingenString() {
    return trim(join(getOpmerkingen())).trim();
  }

  @Override
  public boolean isGewijzigd() {
    return gewijzigd;
  }

  @Override
  public void setGewijzigd(boolean gewijzigd) {
    this.gewijzigd = gewijzigd;
  }

}

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

package nl.procura.diensten.gbav.utils;

public class GbavAntwoord {

  private GbavResultaat resultaat = new GbavResultaat();
  private XML           xml       = new XML();

  public GbavResultaat getResultaat() {
    return resultaat;
  }

  public void setResultaat(GbavResultaat resultaat) {
    this.resultaat = resultaat;
  }

  public XML getXml() {
    return xml;
  }

  public void setXml(XML xml) {
    this.xml = xml;
  }

  public class XML {

    private String vraag    = "";
    private String antwoord = "";

    public String getVraag() {
      return vraag;
    }

    public void setVraag(String vraag) {
      this.vraag = vraag;
    }

    public String getAntwoord() {
      return antwoord;
    }

    public void setAntwoord(String antwoord) {
      this.antwoord = antwoord;
    }
  }
}

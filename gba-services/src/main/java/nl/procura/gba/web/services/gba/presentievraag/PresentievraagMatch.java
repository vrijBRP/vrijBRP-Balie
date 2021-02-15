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

package nl.procura.gba.web.services.gba.presentievraag;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import nl.bprbzk.bcgba.v14.MatchIdenGegAntwoordDE;

import lombok.Data;

public class PresentievraagMatch {

  private final MatchIdenGegAntwoordDE match;

  public PresentievraagMatch(MatchIdenGegAntwoordDE match) {
    this.match = match;
  }

  public MatchIdenGegAntwoordDE getMatch() {
    return match;
  }

  @XmlTransient
  public List<Onderzoeksgegeven> getOnderzoeksgegevens() {

    List<Onderzoeksgegeven> gegevens = new ArrayList<>();

    MatchIdenGegAntwoordDE antwoord = getMatch();

    if (fil(antwoord.getDatumIngangOnderzoekPersoon())) {
      gegevens.add(new Onderzoeksgegeven("Persoon", antwoord.getDatumIngangOnderzoekPersoon(),
          antwoord.getAanduidingGegevensInOnderzoekPersoon()));
    }

    if (fil(antwoord.getDatumIngangOnderzoekPersoon())) {
      gegevens.add(new Onderzoeksgegeven("Nationaliteit", antwoord.getDatumIngangOnderzoekNationaliteit(),
          antwoord.getAanduidingGegevensInOnderzoekNationaliteit()));
    }

    if (fil(antwoord.getDatumIngangOnderzoekPersoon())) {
      gegevens.add(new Onderzoeksgegeven("Overlijden", antwoord.getDatumIngangOnderzoekOverlijden(),
          antwoord.getAanduidingGegevensInOnderzoekOverlijden()));
    }

    if (fil(antwoord.getDatumIngangOnderzoekPersoon())) {
      gegevens.add(new Onderzoeksgegeven("Adres", antwoord.getDatumIngangOnderzoekAdres(),
          antwoord.getAanduidingGegevensInOnderzoekAdres()));
    }

    return gegevens;
  }

  @XmlTransient
  public String getScore() {
    return astr(getMatch().getScore());
  }

  @XmlTransient
  public String getVolgnr() {
    return astr(getMatch().getVolgnummerMatch());
  }

  @Data
  public class Onderzoeksgegeven {

    private String id         = "";
    private String datum      = "";
    private String aanduiding = "";

    public Onderzoeksgegeven() {
    }

    public Onderzoeksgegeven(String id, String datum, String aanduiding) {
      super();
      this.id = id;
      this.datum = datum;
      this.aanduiding = aanduiding;
    }
  }
}

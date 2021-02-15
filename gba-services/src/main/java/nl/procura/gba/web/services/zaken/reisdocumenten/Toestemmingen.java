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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.gba.web.services.zaken.reisdocumenten.ToestemmingGegevenType.ONBEKEND;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Toestemmingen implements Serializable {

  private static final long serialVersionUID = -2593395099431616540L;

  private final ToestemmingbaarReisdocument toestemmingbaarReisdocument;
  private Toestemming                       toestemmingOuder1;
  private Toestemming                       toestemmingOuder2;
  private Toestemming                       toestemmingDerde;
  private Toestemming                       toestemmingCurator;

  public Toestemmingen(ToestemmingbaarReisdocument t) {

    this.toestemmingbaarReisdocument = t;

    toestemmingOuder1 = new Toestemming(ToestemmingType.OUDER_1,
        t::getToestOuder1, t::getToestOuder1Naam, t::getToestOuder1Anr,
        t::setToestOuder1, t::setToestOuder1Naam, t::setToestOuder1Anr);

    toestemmingOuder2 = new Toestemming(ToestemmingType.OUDER_2,
        t::getToestOuder2, t::getToestOuder2Naam, t::getToestOuder2Anr,
        t::setToestOuder2, t::setToestOuder2Naam, t::setToestOuder2Anr);

    toestemmingDerde = new Toestemming(ToestemmingType.DERDE,
        t::getToestDerde, t::getToestDerdeNaam, null,
        t::setToestDerde, t::setToestDerdeNaam, null);

    toestemmingCurator = new Toestemming(ToestemmingType.CURATOR,
        t::getToestCurator, t::getToestCuratorNaam, null,
        t::setToestCurator, t::setToestCuratorNaam, null);
  }

  public int getAantal() {
    BigDecimal value = toestemmingbaarReisdocument.getAantalToestemmingen();
    return value.intValue() >= 0 ? value.intValue() : 0;
  }

  public String getOmschrijving() {

    StringBuilder sb = new StringBuilder();

    sb.append(getAantal());

    StringBuilder sb2 = new StringBuilder();

    if (isGegeven(getToestemmingOuder1())) {
      sb2.append("ouder 1, ");
    }

    if (isGegeven(getToestemmingOuder2())) {
      sb2.append("ouder 2, ");
    }

    if (isGegeven(getToestemmingDerde())) {
      sb2.append("derde, ");
    }

    if (isGegeven(getToestemmingCurator())) {
      sb2.append("curator, ");
    }

    String oms = StringUtils.capitalize(trim(sb2.toString()));

    if (fil(oms)) {
      sb.append(" (");
      sb.append(oms);
      sb.append(")");
    }

    return trim(sb.toString());
  }

  public Toestemming getToestemmingCurator() {
    return toestemmingCurator;
  }

  public void setToestemmingCurator(Toestemming toestemmingCurator) {
    this.toestemmingCurator = toestemmingCurator;
  }

  public Toestemming getToestemmingDerde() {
    return toestemmingDerde;
  }

  public void setToestemmingDerde(Toestemming toestemmingDerde) {
    this.toestemmingDerde = toestemmingDerde;
  }

  public List<Toestemming> getToestemmingen() {
    List<Toestemming> l = new ArrayList<>();
    add(l, getToestemmingOuder1());
    add(l, getToestemmingOuder2());
    add(l, getToestemmingDerde());
    add(l, getToestemmingCurator());

    return l;
  }

  public List<Toestemming> getGegevenToestemmingen() {
    List<Toestemming> l = new ArrayList<>();
    for (Toestemming toestemming : getToestemmingen()) {
      if (isGegeven(toestemming)) {
        l.add(toestemming);
      }
    }
    return l;
  }

  private boolean isGegeven(Toestemming toestemming) {
    boolean isOnbekendeNaam = ONBEKEND.equals(toestemming.getGegeven()) && fil(toestemming.getNaam());
    return (toestemming.isGegeven() || isOnbekendeNaam);
  }

  /**
   * Voor gebruik in het document
   */
  public Toestemming getToestemming1() {
    List<Toestemming> gegevenToestemmingen = getGegevenToestemmingen();
    return !gegevenToestemmingen.isEmpty() ? gegevenToestemmingen.get(0) : new Toestemming();
  }

  public Toestemming getToestemming2() {
    return (getGegevenToestemmingen().size() > 1) ? getGegevenToestemmingen().get(1)
        : new Toestemming();
  }

  public Toestemming getToestemmingOuder1() {
    return toestemmingOuder1;
  }

  public void setToestemmingOuder1(Toestemming toestemmingOuder1) {
    this.toestemmingOuder1 = toestemmingOuder1;
  }

  public Toestemming getToestemmingOuder2() {
    return toestemmingOuder2;
  }

  public void setToestemmingOuder2(Toestemming toestemmingOuder2) {
    this.toestemmingOuder2 = toestemmingOuder2;
  }

  private void add(List<Toestemming> l, Toestemming t) {
    l.add(t);
  }
}

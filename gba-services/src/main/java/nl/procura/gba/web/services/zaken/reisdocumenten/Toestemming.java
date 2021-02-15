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

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static nl.procura.gba.common.MiscUtils.trimNr;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ToestemmingGegevenType.*;
import static nl.procura.standard.Globalfunctions.trim;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Toestemming implements Serializable {

  private static final long serialVersionUID = -7220637582195837143L;

  private GezagStatus     gezagStatus = GezagStatus.NEE;
  private ToestemmingType type;

  private Supplier<String>                naamSupplier;
  private Supplier<String>                anrSupplier;
  private Supplier<BigDecimal>            gegevenSupplier;
  private Consumer<String>                naamConsumer;
  private Consumer<String>                anrConsumer;
  private Consumer<BigDecimal>            gegevenConsumer;
  private String                          toelichting    = "";
  private final ToestemmingConstateringen constateringen = new ToestemmingConstateringen();
  private ToestemmingGegevenType          gegeven        = ONBEKEND;
  private String                          anr;
  private String                          naam;

  public Toestemming() {
  }

  public Toestemming(
      ToestemmingType type,
      Supplier<BigDecimal> gegevenSupplier,
      Supplier<String> naamSupplier,
      Supplier<String> anrSupplier,
      Consumer<BigDecimal> gegevenConsumer,
      Consumer<String> naamConsumer,
      Consumer<String> anrConsumer) {

    this();
    this.type = type;
    this.constateringen.setType(type);

    this.naamSupplier = naamSupplier;
    this.anrSupplier = anrSupplier;
    this.gegevenSupplier = gegevenSupplier;

    this.naamConsumer = naamConsumer;
    this.anrConsumer = anrConsumer;
    this.gegevenConsumer = gegevenConsumer;
  }

  public String getAnummer() {
    if (anr == null) {
      anr = ofNullable(anrSupplier)
          .map(Supplier::get)
          .orElse("");
    }
    return anr;
  }

  public String getNaam() {
    if (naam == null) {
      naam = ofNullable(naamSupplier)
          .map(Supplier::get)
          .orElse("");
    }
    return naam;
  }

  public String getTekstNaam() {
    String vervangendeInd = " (vervangende)";
    return getNaam() + (VERVANGENDE.equals(getGegeven()) ? vervangendeInd : "");
  }

  public ToestemmingGegevenType getGegeven() {
    if (ONBEKEND == gegeven) {
      gegeven = ofNullable(gegevenSupplier)
          .map(val -> get(val.get().intValue()))
          .filter(val -> ONBEKEND != val)
          .orElse(NEE);
    }
    return gegeven;
  }

  public boolean isGegeven() {
    return asList(JA, VERVANGENDE).contains(getGegeven());
  }

  public void setAnummer(String anr) {
    this.anr = anr;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public void setGegeven(ToestemmingGegevenType gegeven) {
    this.gegeven = gegeven;
  }

  public void opslaan() {
    ofNullable(gegevenConsumer).ifPresent(supplier -> supplier.accept(BigDecimal.valueOf(getGegeven().getCode())));
    ofNullable(naamConsumer).ifPresent(supplier -> supplier.accept(isGegeven() ? trim(getNaam()) : ""));
    ofNullable(anrConsumer).ifPresent(supplier -> supplier.accept(isGegeven() ? trimNr(getAnummer()) : ""));
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public ToestemmingType getType() {
    return type;
  }

  public boolean isVerplicht() {
    return GezagStatus.JA.equals(gezagStatus) || GezagStatus.MOGELIJK_JA.equals(gezagStatus);
  }

  public String getConclusie() {
    switch (gezagStatus) {
      case JA:
        return type.getOms() + " moet toestemming geven";

      case MOGELIJK_JA:
        return type.getOms() + " moet mogelijk toestemming geven";

      default:
      case NEE:
        return type.getOms() + " hoeft geen toestemming te geven";
    }
  }

  public ToestemmingConstateringen getConstateringen() {
    return constateringen;
  }

  public GezagStatus getGezagStatus() {
    return gezagStatus;
  }

  public void setGezagStatus(GezagStatus gezagStatus) {
    this.gezagStatus = gezagStatus;
  }
}

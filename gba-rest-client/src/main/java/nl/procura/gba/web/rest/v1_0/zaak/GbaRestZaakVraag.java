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

package nl.procura.gba.web.rest.v1_0.zaak;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

import nl.procura.gba.web.rest.v1_0.zaak.attribuut.GbaRestZaakAttribuut;
import nl.procura.gba.web.rest.v1_0.zaak.identificatie.GbaRestZaakIdType;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "persoonsnummer", "zaakId", "zaakIdType", "statussen", "attributen", "typen", "datumVanaf",
    "datumTm", "datumIngangVanaf", "datumIngangTm", "datumMutatieVanaf", "datumMutatieTm", "maxAantal", "vraagType",
    "sortering" })
public class GbaRestZaakVraag {

  private String persoonsnummer = "";

  private String zaakId = "";

  private GbaRestZaakIdType zaakIdType = GbaRestZaakIdType.ONBEKEND;

  private GbaRestZaakVraagType vraagType = GbaRestZaakVraagType.MINIMAAL;

  @XmlElementWrapper(name = "statussen")
  @XmlElement(name = "status")
  private List<GbaRestZaakStatus> statussen = new ArrayList<>();

  @XmlElementWrapper(name = "attributen")
  @XmlElement(name = "attribuut")
  private List<GbaRestZaakAttribuut> attributen = new ArrayList<>();

  @XmlElementWrapper(name = "types")
  @XmlElement(name = "type")
  private List<GbaRestZaakType> typen = new ArrayList<>();

  @XmlElement(name = "sortering")
  private GbaRestZaakSortering sortering;

  private long datumVanaf        = 0;
  private long datumTm           = 0;
  private long datumIngangVanaf  = 0;
  private long datumIngangTm     = 0;
  private long datumMutatieVanaf = 0;
  private long datumMutatieTm    = 0;
  private int  maxAantal         = 0;

  public GbaRestZaakVraag() {
  }

  public String getZaakId() {
    return zaakId;
  }

  public GbaRestZaakVraag setZaakId(String zaakId) {
    this.zaakId = zaakId;
    return this;
  }

  public List<GbaRestZaakStatus> getStatussen() {
    return statussen;
  }

  public GbaRestZaakVraag setStatussen(GbaRestZaakStatus... statussen) {
    getStatussen().addAll(asList(statussen));
    return this;
  }

  public GbaRestZaakVraag setStatussen(List<GbaRestZaakStatus> statussen) {
    this.statussen = statussen;
    return this;
  }

  public List<GbaRestZaakAttribuut> getAttributen() {
    return attributen;
  }

  public void setAttributen(List<GbaRestZaakAttribuut> attributen) {
    this.attributen = attributen;
  }

  public GbaRestZaakVraag setAttributen(GbaRestZaakAttribuut... attributen) {
    getAttributen().addAll(asList(attributen));
    return this;
  }

  public List<GbaRestZaakType> getTypen() {
    return typen;
  }

  public GbaRestZaakVraag setTypen(GbaRestZaakType... typen) {
    getTypen().addAll(asList(typen));
    return this;
  }

  public GbaRestZaakVraag setTypen(List<GbaRestZaakType> typen) {
    this.typen = typen;
    return this;
  }

  public String getPersoonsnummer() {
    return persoonsnummer;
  }

  public GbaRestZaakVraag setPersoonsnummer(String persoonsnummer) {
    this.persoonsnummer = persoonsnummer;
    return this;
  }

  public long getDatumVanaf() {
    return datumVanaf;
  }

  public void setDatumVanaf(long datumVanaf) {
    this.datumVanaf = datumVanaf;
  }

  public long getDatumTm() {
    return datumTm;
  }

  public void setDatumTm(long datumTm) {
    this.datumTm = datumTm;
  }

  public GbaRestZaakVraagType getVraagType() {
    return vraagType;
  }

  public void setVraagType(GbaRestZaakVraagType vraagType) {
    this.vraagType = vraagType;
  }

  public int getMaxAantal() {
    return maxAantal;
  }

  public void setMaxAantal(int maxAantal) {
    this.maxAantal = maxAantal;
  }

  public long getDatumIngangVanaf() {
    return datumIngangVanaf;
  }

  public void setDatumIngangVanaf(long datumIngangVanaf) {
    this.datumIngangVanaf = datumIngangVanaf;
  }

  public long getDatumIngangTm() {
    return datumIngangTm;
  }

  public void setDatumIngangTm(long datumIngangTm) {
    this.datumIngangTm = datumIngangTm;
  }

  public GbaRestZaakIdType getZaakIdType() {
    return zaakIdType;
  }

  public void setZaakIdType(GbaRestZaakIdType zaakIdType) {
    this.zaakIdType = zaakIdType;
  }

  public long getDatumMutatieVanaf() {
    return datumMutatieVanaf;
  }

  public void setDatumMutatieVanaf(long datumMutatieVanaf) {
    this.datumMutatieVanaf = datumMutatieVanaf;
  }

  public long getDatumMutatieTm() {
    return datumMutatieTm;
  }

  public void setDatumMutatieTm(long datumMutatieTm) {
    this.datumMutatieTm = datumMutatieTm;
  }

  public GbaRestZaakSortering getSortering() {
    return sortering;
  }

  public void setSortering(GbaRestZaakSortering sortering) {
    this.sortering = sortering;
  }
}

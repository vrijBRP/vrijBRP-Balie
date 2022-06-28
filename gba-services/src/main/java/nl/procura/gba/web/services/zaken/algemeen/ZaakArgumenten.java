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

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.*;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.QueryListener;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdType;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

public class ZaakArgumenten {

  private boolean       all               = false;
  private boolean       zonderBehandelaar = false;
  private long          anr               = 0;
  private long          bsn               = 0;
  private long          dInvoerVanaf      = -1;
  private long          dInvoerTm         = -1;
  private long          dMutatieVanaf     = -1;
  private long          dMutatieTm        = -1;
  private long          dIngangVanaf      = -1;
  private long          dIngangTm         = -1;
  private long          dAfhaalVanaf      = -1;
  private long          dAfhaalTm         = -1;
  private long          dTerm             = 0;
  private int           max               = 0;
  private String        akteVolgnr        = "";
  private ZaakSortering sortering         = ZaakSortering.DATUM_INGANG_OUD_NIEUW; // De oudste als eerste verwerken

  private ZaakIdType          zaakIdType      = ZaakIdType.ONBEKEND;
  private Set<ZaakKey>        zaakKeys        = new HashSet<>();
  private Set<ZaakType>       typen           = new HashSet<>();
  private Set<ZaakStatusType> statussen       = new HashSet<>();
  private Set<ZaakStatusType> negeerStatussen = new HashSet<>();

  private List<String> bronnen               = new ArrayList<>();
  private List<String> leveranciers          = new ArrayList<>();
  private List<String> attributen            = new ArrayList<>();
  private List<String> ontbrekendeAttributen = new ArrayList<>();

  private QueryListener listener              = null;
  private long          codeGebruiker         = 0;
  private long          codeGebruikerFavoriet = 0;
  private long          codeBehandelaar       = 0;
  private long          codeProfiel           = 0;

  public ZaakArgumenten() {
  }

  public ZaakArgumenten(BasePLExt pl) {
    this(pl.getPersoon().getAnr().getVal(), pl.getPersoon().getBsn().getVal());
  }

  public ZaakArgumenten(BasePLExt pl, ZaakStatusType... statussen) {
    this(pl.getPersoon().getAnr().getVal(), pl.getPersoon().getBsn().getVal());
    addStatussen(statussen);
  }

  public ZaakArgumenten(BasePLExt pl, ZaakType... typen) {
    this(pl.getPersoon().getAnr().getVal(), pl.getPersoon().getBsn().getVal());
    addTypen(typen);
  }

  public ZaakArgumenten(Collection<ZaakKey> zaakKeys) {
    getZaakKeys().addAll(zaakKeys);
  }

  public ZaakArgumenten(String zaakId) {
    addZaakKey(new ZaakKey(zaakId));
  }

  public ZaakArgumenten(Zaak zaak) {
    addZaakKey(new ZaakKey(zaak.getZaakId(), zaak.getType()));
  }

  public ZaakArgumenten(ZaakArgumenten z) {

    all = z.isAll();
    zonderBehandelaar = z.isZonderBehandelaar();
    anr = z.getAnr();
    bsn = z.getBsn();
    zaakIdType = z.getZaakIdType();

    dInvoerVanaf = z.getdInvoerVanaf();
    dInvoerTm = z.getdInvoerTm();
    dMutatieVanaf = z.getdMutatieVanaf();
    dMutatieTm = z.getdMutatieTm();
    dIngangVanaf = z.getdIngangVanaf();
    dIngangTm = z.getdIngangTm();
    dAfhaalVanaf = z.getDAfhaalVanaf();
    dAfhaalTm = z.getDAfhaalTm();

    dTerm = z.getdEndTerm();
    max = z.getMax();
    akteVolgnr = z.getAkteVolgnr();
    listener = z.getListener();
    codeGebruiker = z.getCodeGebruiker();
    codeBehandelaar = z.getCodeBehandelaar();
    codeGebruikerFavoriet = z.getCodeGebruikerFavoriet();
    codeProfiel = z.getCodeProfiel();

    zaakKeys = new HashSet<>(z.getZaakKeys());
    typen = new HashSet<>(z.getTypen());
    statussen = new HashSet<>(z.getStatussen());
    negeerStatussen = new HashSet<>(z.getNegeerStatussen());

    attributen = new ArrayList<>(z.getAttributen());
    ontbrekendeAttributen = new ArrayList<>(z.getOntbrekendeAttributen());
    bronnen = new ArrayList<>(z.getBronnen());
    leveranciers = new ArrayList<>(z.getLeveranciers());
    sortering = z.getSortering();
  }

  public ZaakArgumenten(ZaakArgumenten z, ZaakType type) {
    this(z);
    setTypen(type);
  }

  public ZaakArgumenten(ZaakKey zaakKey) {
    addZaakKey(zaakKey);
  }

  public ZaakArgumenten(ZaakStatusType... statussen) {
    addStatussen(statussen);
  }

  private ZaakArgumenten(String... nrs) {
    setNummer(nrs);
  }

  public ZaakArgumenten addAttributen(String... attributen) {
    this.attributen.addAll(Arrays.asList(attributen));
    return this;
  }

  public ZaakArgumenten addNegeerStatussen(ZaakStatusType... statussen) {
    this.negeerStatussen.addAll(Arrays.asList(statussen));
    this.negeerStatussen.addAll(ZaakStatusType.getCombiStatussen(statussen));
    return this;
  }

  public ZaakArgumenten addOntbrekendeAttributen(String... ontbrekendeAttributen) {
    this.ontbrekendeAttributen.addAll(Arrays.asList(ontbrekendeAttributen));
    return this;
  }

  public ZaakArgumenten addStatussen(ZaakStatusType... statussen) {
    this.statussen.addAll(Arrays.asList(statussen));
    this.statussen.addAll(ZaakStatusType.getCombiStatussen(statussen));
    return this;
  }

  public ZaakArgumenten addTypen(ZaakType... typen) {
    this.typen.addAll(asList(typen));
    return this;
  }

  public ZaakArgumenten addZaakKey(ZaakKey zaakKey) {
    if (fil(zaakKey.getZaakId())) {
      zaakKeys.add(zaakKey);
    }
    return this;
  }

  public String getAkteVolgnr() {
    return akteVolgnr;
  }

  public ZaakArgumenten setAkteVolgnr(String akteVolgnr) {
    this.akteVolgnr = akteVolgnr;
    return this;
  }

  public long getAnr() {
    return anr;
  }

  public ZaakArgumenten setAnr(long anr) {
    this.anr = anr;
    return this;
  }

  public List<String> getAttributen() {
    return attributen;
  }

  public ZaakArgumenten setAttributen(List<String> attributen) {
    this.attributen = attributen;
    return this;
  }

  public List<String> getBronnen() {
    return bronnen;
  }

  public ZaakArgumenten setBronnen(List<String> bronnen) {
    this.bronnen = bronnen;
    return this;
  }

  public long getBsn() {
    return bsn;
  }

  public ZaakArgumenten setBsn(long bsn) {
    this.bsn = bsn;
    return this;
  }

  public long getCodeGebruiker() {
    return codeGebruiker;
  }

  public ZaakArgumenten setCodeGebruiker(long codeGebruiker) {
    this.codeGebruiker = codeGebruiker;
    return this;
  }

  public long getCodeGebruikerFavoriet() {
    return codeGebruikerFavoriet;
  }

  public ZaakArgumenten setCodeGebruikerFavoriet(long codeGebruikerFavoriet) {
    this.codeGebruikerFavoriet = codeGebruikerFavoriet;
    return this;
  }

  public long getCodeBehandelaar() {
    return codeBehandelaar;
  }

  public ZaakArgumenten setCodeBehandelaar(long codeBehandelaar) {
    this.codeBehandelaar = codeBehandelaar;
    return this;
  }

  public long getCodeProfiel() {
    return codeProfiel;
  }

  public ZaakArgumenten setCodeProfiel(long codeProfiel) {
    this.codeProfiel = codeProfiel;
    return this;
  }

  public long getDAfhaalTm() {
    return dAfhaalTm;
  }

  public ZaakArgumenten setDAfhaalTm(long dAfhaalTm) {
    this.dAfhaalTm = dAfhaalTm;
    return this;
  }

  public long getDAfhaalVanaf() {
    return dAfhaalVanaf;
  }

  public ZaakArgumenten setDAfhaalVanaf(long dAfhaalVanaf) {
    this.dAfhaalVanaf = dAfhaalVanaf;
    return this;
  }

  public long getdEndTerm() {
    return dTerm;
  }

  public long getdIngangTm() {
    return dIngangTm;
  }

  public ZaakArgumenten setdIngangTm(long datum) {
    this.dIngangTm = datum;
    return this;
  }

  public long getdIngangVanaf() {
    return dIngangVanaf;
  }

  public ZaakArgumenten setdIngangVanaf(long datum) {
    this.dIngangVanaf = datum;
    return this;
  }

  public long getdInvoerTm() {
    return dInvoerTm;
  }

  public ZaakArgumenten setdInvoerTm(long datum) {
    this.dInvoerTm = datum;
    return this;
  }

  public long getdInvoerVanaf() {
    return dInvoerVanaf;
  }

  public ZaakArgumenten setdInvoerVanaf(long datum) {
    this.dInvoerVanaf = datum;
    return this;
  }

  public List<String> getLeveranciers() {
    return leveranciers;
  }

  public ZaakArgumenten setLeveranciers(List<String> leveranciers) {
    this.leveranciers = leveranciers;
    return this;
  }

  public QueryListener getListener() {
    return listener;
  }

  public ZaakArgumenten setListener(QueryListener listener) {
    this.listener = listener;
    return this;
  }

  public int getMax() {
    return max;
  }

  public ZaakArgumenten setMax(int max) {
    this.max = max;
    return this;
  }

  public List<Long> getNegeerStatusCodes() {

    List<Long> l = new ArrayList<>();
    for (ZaakStatusType s : getNegeerStatussen()) {
      l.add(s.getCode());
    }

    return l;
  }

  public Set<ZaakStatusType> getNegeerStatussen() {
    return negeerStatussen;
  }

  public ZaakArgumenten setNegeerStatussen(ZaakStatusType... statussen) {
    this.negeerStatussen = new HashSet<ZaakStatusType>();
    return addNegeerStatussen(statussen);
  }

  public List<String> getOntbrekendeAttributen() {
    return ontbrekendeAttributen;
  }

  public ZaakArgumenten setOntbrekendeAttributen(List<String> ontbrekendeAttributen) {
    this.ontbrekendeAttributen = ontbrekendeAttributen;
    return this;
  }

  public List<Long> getStatusCodes() {

    List<Long> l = new ArrayList<>();
    for (ZaakStatusType s : getStatussen()) {
      l.add(s.getCode());
    }

    return l;
  }

  public Set<ZaakStatusType> getStatussen() {
    return statussen;
  }

  public ZaakArgumenten setStatussen(ZaakStatusType... statussen) {
    this.statussen = new HashSet<ZaakStatusType>();
    return addStatussen(statussen);
  }

  public List<Long> getTypeCodes() {

    List<Long> l = new ArrayList<>();
    for (ZaakType s : getTypen()) {
      l.add(s.getCode());
    }

    return l;
  }

  public Set<ZaakType> getTypen() {
    return typen;
  }

  public ZaakArgumenten setTypen(ZaakType... typen) {
    this.typen = new HashSet<>();
    return addTypen(typen);
  }

  public Set<String> getZaakIds() {
    Set<String> set = new HashSet<>();
    for (ZaakKey zaakKey : zaakKeys) {
      set.add(zaakKey.getZaakId());
    }
    return set;
  }

  public ZaakArgumenten setZaakIds(Set<String> zaakIds) {
    zaakIds.stream().map(ZaakKey::new).forEach(this::addZaakKey);
    return this;
  }

  public ZaakIdType getZaakIdType() {
    return zaakIdType;
  }

  public ZaakArgumenten setZaakIdType(ZaakIdType zaakIdType) {
    this.zaakIdType = zaakIdType;
    return this;
  }

  public ZaakKey getZaakKey() {
    for (ZaakKey zaakKey : zaakKeys) {
      return zaakKey;
    }
    return new ZaakKey();
  }

  public ZaakArgumenten setZaakKey(ZaakKey zaakKey) {
    zaakKeys.clear();
    addZaakKey(zaakKey);
    return this;
  }

  public Set<ZaakKey> getZaakKeys() {
    return zaakKeys;
  }

  public ZaakArgumenten setZaakKeys(Set<ZaakKey> zaakKeys) {
    this.zaakKeys = zaakKeys;
    return this;
  }

  public boolean isAll() {
    return all;
  }

  public ZaakArgumenten setAll(boolean all) {
    this.all = all;
    return this;
  }

  public boolean isZonderBehandelaar() {
    return zonderBehandelaar;
  }

  public ZaakArgumenten setZonderBehandelaar(boolean zonderBehandelaar) {
    this.zonderBehandelaar = zonderBehandelaar;
    return this;
  }

  public boolean isCorrect() {
    return isAll() || getZaakKeys().size() > 0 || pos(getAnr()) || pos(getBsn()) ||
        pos(getdInvoerVanaf()) || pos(getdInvoerTm()) || pos(getDAfhaalVanaf()) || pos(getDAfhaalTm()) ||
        pos(getdMutatieVanaf()) || pos(getdMutatieTm()) || pos(getdIngangVanaf()) || pos(getdIngangTm()) ||
        fil(getAkteVolgnr()) || ((getStatussen() != null) && (getStatussen().size() > 0)) ||
        ((getTypen() != null) && (getTypen().size() > 0));
  }

  public ZaakArgumenten setdTerm(long dTerm) {
    this.dTerm = dTerm;
    return this;
  }

  public ZaakArgumenten setNummer(String... nrs) {
    // Is er minimaal één anr of bsn gevuld?
    boolean filledNrs = false;
    for (String nr : nrs) {
      if (fil(nr)) {
        filledNrs = true;
      }
    }

    if (filledNrs) {
      for (String nr : nrs) {
        if (fil(nr)) {
          Anummer a = new Anummer(nr);
          if (a.isCorrect()) {
            setAnr(a.getLongAnummer());
          } else {
            Bsn b = new Bsn(nr);
            if (b.isCorrect()) {
              setBsn(b.getLongBsn());
            } else {
              // Hack om te zorgen dat er geen zoekresultaten terug worden gegeven.
              setZaakKey(new ZaakKey("Foutief A-nummer / BSN: " + nr));
            }
          }
        }
      }
    } else {
      // Hack om te zorgen dat er geen zoekresultaten terug worden gegeven.
      setZaakKey(new ZaakKey("Geen a-nummer / BSN"));
    }
    return this;
  }

  public long getdMutatieVanaf() {
    return dMutatieVanaf;
  }

  public void setdMutatieVanaf(long dMutatieVanaf) {
    this.dMutatieVanaf = dMutatieVanaf;
  }

  public long getdMutatieTm() {
    return dMutatieTm;
  }

  public void setdMutatieTm(long dMutatieTm) {
    this.dMutatieTm = dMutatieTm;
  }

  public ZaakSortering getSortering() {
    return sortering;
  }

  public void setSortering(ZaakSortering sortering) {
    if (sortering != null) {
      this.sortering = sortering;
    }
  }
}

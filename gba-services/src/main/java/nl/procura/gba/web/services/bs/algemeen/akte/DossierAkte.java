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

package nl.procura.gba.web.services.bs.algemeen.akte;

import static ch.lambdaj.Lambda.*;
import static nl.procura.gba.common.ZaakStatusType.VERWERKT;
import static nl.procura.gba.common.ZaakStatusType.VERWERKT_IN_GBA;
import static nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteInvoerType.PROWEB_PERSONEN;
import static nl.procura.standard.Globalfunctions.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.*;
import java.util.stream.Collectors;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.db.Doss;
import nl.procura.gba.jpa.personen.db.DossAkte;
import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.java.reflection.ReflectionUtil;

public class DossierAkte extends DossAkte implements Comparable<DossierAkte>, DatabaseTable {

  private static final long serialVersionUID = -1462953356950499131L;

  private Set<DossierPersoon> personen = new HashSet<>();

  public DossierAkte() {
    setJaar(toBigDecimal(0));
    setRegisterdeel("");

    setBsn(toBigDecimal(-1));
    setVoorn("");
    setVoorv("");
    setGeslachtsnaam("");
    setGeslacht("");

    setpBsn(toBigDecimal(-1));
    setpVoorn("");
    setpVoorv("");
    setpGeslachtsnaam("");
    setpGeslacht("");
  }

  public DossierAkte(DateTime datum) {
    this();
    setDatumIngang(datum);
  }

  public DossierAkte(Dossier dossier) {
    this();
    setRegistersoort(toBigDecimal(DossierAkteRegistersoort.get(dossier).getCode()));
    setDossier(dossier);
  }

  public void addPersoon(DossierPersoon persoon) {
    this.personen.add(persoon);
  }

  @Override
  public int compareTo(DossierAkte o) {
    return o.getCode() < getCode() ? 1 : -1;
  }

  public String getAkte() {
    return isCorrect() ? getInternAkte() : "";
  }

  public String getDescription() {
    String akte = getAkte();
    return akte.isEmpty() ? "Onbekend" : akte;
  }

  public DossierAktePersoon getAktePartner() {
    return new DossierAktePersoon(this, true);
  }

  public DossierAktePersoon getAktePersoon() {
    return new DossierAktePersoon(this, false);
  }

  public DossierAkteRegistersoort getAkteRegistersoort() {
    return DossierAkteRegistersoort.get(getRegistersoort());
  }

  public String getBrpAkte() {
    return isCorrect() ? getInternBrpAkte() : "";
  }

  public Long getCode() {
    return getCDossAkte();
  }

  public GbaDateFieldValue getDatumFeit() {
    return new GbaDateFieldValue(getdFeit());
  }

  public void setDatumFeit(GbaDateFieldValue datum) {
    setdFeit(toBigDecimal(datum.getLongDate()));
  }

  public DateTime getDatumIngang() {
    return new DateTime(getdIn());
  }

  public void setDatumIngang(DateTime datum) {
    setdIn(toBigDecimal(datum.getLongDate()));
  }

  public String getFormatVnr() {
    return pad_left(astr(getVnr()), "0", 4);
  }

  public DossierAkteInvoerType getInvoerType() {
    return DossierAkteInvoerType.get(getInvType());
  }

  public void setInvoerType(DossierAkteInvoerType invoerType) {
    setInvType(invoerType.getCode());
  }

  /**
   * Alleen personen met een DossierPersoonType
   */

  public List<DossierPersoon> getPersonen() {
    List<DossierPersoon> knowPersons = personen
        .stream().filter(p -> !p.getDossierPersoonType()
            .is(DossierPersoonType.ONBEKEND))
        .collect(Collectors.toList());
    return new ArrayList(new LinkedHashSet(BsPersoonUtils.sort(knowPersons)));
  }

  public void setPersonen(Set<DossierPersoon> personen) {
    this.personen = personen;
  }

  public List<DossierPersoon> getPersonen(DossierPersoonType type) {
    return getPersonen().stream()
        .filter(p -> p.getDossierPersoonType().is(type))
        .collect(Collectors.toList());
  }

  public String getZaakId() {
    return heeftDossier() ? getDoss().getZaakId() : "";
  }

  public boolean heeftDossier() {
    return getDoss() != null;
  }

  public boolean heeftPersoonType(DossierPersoonType type) {
    return exists(getPersonen(), having(on(DossierPersoon.class).getDossierPersoonType(), equalTo(type)));
  }

  public boolean isCorrect() {
    return pos(getRegistersoort()) && pos(getJaar()) && fil(getRegisterdeel()) && pos(getVnr()) && pos(getdIn());
  }

  public boolean isDossierAkte() {
    return PROWEB_PERSONEN.equals(getInvoerType());
  }

  public boolean isDossierCorrect() {
    return !isDossierAkte() || heeftDossier();
  }

  public boolean isDossierVerwerkt() {
    return !isDossierAkte() || (heeftDossier() && ZaakStatusType.get(getDoss().getIndVerwerkt().longValue()).is(
        VERWERKT, VERWERKT_IN_GBA));
  }

  public void ontkoppelPersoon(DossierPersoon persoon) {
    personen.remove(persoon);
    getDossPers().remove(ReflectionUtil.deepCopyBean(DossPer.class, persoon));
  }

  public void setDossier(Dossier dossier) {
    setDoss(ReflectionUtil.deepCopyBean(Doss.class, dossier));
  }

  public void setPersoon(DossierPersoon persoon) {
    personen.remove(persoon);
    personen.add(persoon);
  }

  private String getInternAkte() {
    return getRegistersoort() + getRegisterdeel() + getFormatVnr();
  }

  private String getInternBrpAkte() {
    return getRegistersoort() + getRegisterdeel() + getAkteAand() + getFormatVnr();
  }
}

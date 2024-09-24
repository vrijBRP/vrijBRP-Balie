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

package nl.procura.gba.web.services.bs.naturalisatie;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.AANGEVER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MEDE_VERZOEKER_KIND;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MEDE_VERZOEKER_PARTNER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.VERTEGENWOORDIGER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import nl.procura.gba.common.EnumWithCode;
import nl.procura.gba.common.UniqueList;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossNatur;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BasisVerzoekType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BereidAfstandType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BevoegdTotVerzoekType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BewijsNationaliteitType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.GeldigeVerblijfsvergunningType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.MinderjarigeKinderenAkkoordType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.NaamvaststellingType;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class DossierNaturalisatie extends DossNatur implements ZaakDossier {

  private Dossier                             dossier           = new Dossier(ZaakType.NATURALISATIE);
  private List<DossierNaturalisatieVerzoeker> verzoekerGegevens = new UniqueList<>();

  public DossierNaturalisatie() {
    super();
    setDossier(new Dossier(ZaakType.NATURALISATIE, this));
    getDossier().toevoegenPersoon(AANGEVER);
    getDossier().toevoegenPersoon(PARTNER);
    getDossier().toevoegenPersoon(VERTEGENWOORDIGER);
  }

  @Override
  public void beforeSave() {
    setCDossNatur(getDossier().getCode());
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getAangever().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    getAangever().setAnummer(anummer);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getAangever().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getAangever().setBurgerServiceNummer(burgerServiceNummer);
  }

  public Long getCode() {
    return getCDossNatur();
  }

  @Override
  public Dossier getDossier() {
    return dossier;
  }

  @Override
  public void setDossier(Dossier dossier) {
    this.dossier = dossier;
  }

  public DossierPersoon getAangever() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(AANGEVER));
  }

  public DossierPersoon getPartner() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(PARTNER));
  }

  public DossierPersoon getVertegenwoordiger() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(VERTEGENWOORDIGER));
  }

  public List<DossierPersoon> getAlleVerzoekers() {
    List<DossierPersoon> personen = getDossier().getPersonen(AANGEVER, MEDE_VERZOEKER_PARTNER, MEDE_VERZOEKER_KIND);
    personen.sort(Comparator.comparingLong(person -> person.getDossierPersoonType().getCode()));
    return personen;
  }

  public List<DossierPersoon> getToestemminggevers() {
    Map<BigDecimal, DossierPersoon> list = new LinkedHashMap<>();
    getVerzoekerGegevens().stream()
        .map(DossierNaturalisatieVerzoeker::getToestemminggever)
        .filter(Objects::nonNull)
        .forEach(persoon -> list.put(persoon.getBsn(), persoon));
    return new ArrayList<>(list.values());
  }

  public List<DossierPersoon> getMedeVerzoekers() {
    return getDossier().getPersonen(MEDE_VERZOEKER_PARTNER, MEDE_VERZOEKER_KIND);
  }

  @Override
  public boolean isVolledig() {
    return true;
  }

  public boolean isOptie() {
    return Boolean.TRUE.equals(getOptie());
  }

  public void ifOptie(Runnable runnable) {
    if (isOptie()) {
      runnable.run();
    }
  }

  public void ifNotOptie(Runnable runnable) {
    if (!isOptie()) {
      runnable.run();
    }
  }

  public boolean isMeerderjarig() {
    return BevoegdTotVerzoekType.JA == getBevoegdTotVerzoekType();
  }

  public void setBevoegdTotVerzoekType(BevoegdTotVerzoekType type) {
    setBevoegdIndienen(toBigDecimal(type));
  }

  public BevoegdTotVerzoekType getBevoegdTotVerzoekType() {
    return BevoegdTotVerzoekType.get(getBevoegdIndienen());
  }

  public BereidAfstandType getBereidAfstandType() {
    return BereidAfstandType.get(getToetsBereidAfstand());
  }

  public void setBereidAfstandType(BereidAfstandType type) {
    setToetsBereidAfstand(toBigDecimal(type));
  }

  public BewijsNationaliteitType getBewijsBewijsNationaliteitType() {
    return BewijsNationaliteitType.get(getToetsBewijsNatAanw());
  }

  public void setBewijsBewijsNationaliteitType(BewijsNationaliteitType type) {
    setToetsBewijsNatAanw(toBigDecimal(type));
  }

  public GeldigeVerblijfsvergunningType getGeldigeVerblijfsvergunningType() {
    return GeldigeVerblijfsvergunningType.get(getToetsGeldVerblijfsAanw());
  }

  public void setGeldigeVerblijfsvergunningType(GeldigeVerblijfsvergunningType type) {
    setToetsGeldVerblijfsAanw(toBigDecimal(type));
  }

  public BasisVerzoekType getBasisVerzoekType() {
    return BasisVerzoekType.get(getBasisVerzoek());
  }

  public void setBasisVerzoekType(BasisVerzoekType type) {
    setBasisVerzoek(toBigDecimal(type));
  }

  public MinderjarigeKinderenAkkoordType getKinderen12AkkoordType() {
    return MinderjarigeKinderenAkkoordType.get(getBehMinderjKind1());
  }

  public void setKinderen12AkkoordType(MinderjarigeKinderenAkkoordType type) {
    setBehMinderjKind1(toBigDecimal(type));
  }

  public NaamvaststellingType getNaamVaststellingType() {
    return NaamvaststellingType.get(getNaamstNodig());
  }

  public void setNaamVaststellingType(NaamvaststellingType type) {
    setNaamstNodig(toBigDecimal(type));
  }

  public NaturalisatieDossierNummer getDossierNummer() {
    return NaturalisatieDossierNummer.ofValue(getDossiernr());
  }

  public boolean isVerzoeker(BsnFieldValue bsn) {
    return getAlleVerzoekers().stream().anyMatch(gegevens -> bsn.equals(gegevens.getBurgerServiceNummer()));
  }

  public boolean isVerzoekerGegevens(BsnFieldValue bsn) {
    return getVerzoekerGegevens(bsn) != null;
  }

  public DossierNaturalisatieVerzoeker getVerzoekerGegevens(BsnFieldValue bsn) {
    return verzoekerGegevens.stream()
        .filter(gegevens -> bsn.equals(gegevens.getBurgerServiceNummer()))
        .findFirst()
        .orElse(null);
  }

  public List<DossierNaturalisatieVerzoeker> getVerzoekerGegevens() {
    return verzoekerGegevens;
  }

  public void setVerzoekerGegevens(List<DossierNaturalisatieVerzoeker> verzoekerGegevens) {
    this.verzoekerGegevens = verzoekerGegevens;
  }

  private BigDecimal toBigDecimal(EnumWithCode<Integer> value) {
    return BigDecimal.valueOf(ofNullable(value)
        .map(EnumWithCode::getCode)
        .orElse(-1));
  }
}

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

package nl.procura.gba.web.services.beheer.verkiezing;

import static java.lang.Long.parseLong;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.jpa.personen.dao.KiesrDao;
import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.jpa.personen.db.KiesrVerkInfo;
import nl.procura.gba.jpa.personen.db.KiesrWijz;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Anummer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KiezersregisterService extends AbstractService {

  public KiezersregisterService() {
    super("Verkiezing");
  }

  public List<KiesrVerk> getVerkiezingen() {
    return KiesrDao.getVerkiezingen();
  }

  public List<KiesrVerk> getSelecteerbareVerkiezingen() {
    return KiesrDao.getVerkiezingen().stream().filter(this::isNietVerlopen).collect(Collectors.toList());
  }

  private boolean isNietVerlopen(KiesrVerk verk) {
    long currentDate = Long.parseLong(new ProcuraDate().getSystemDate());
    long dVerk = Long.parseLong(new ProcuraDate(verk.getdVerk()).getSystemDate());
    return currentDate <= dVerk;
  }

  public KiesrWijz getNieuweWijziging(KiesrStem stem, KiezersregisterActieType type, String opmerking) {
    KiesrWijz kiesrWijz = new KiesrWijz(stem);
    ProcuraDate date = new ProcuraDate();
    kiesrWijz.setdIn(Long.valueOf(date.getSystemDate()));
    kiesrWijz.settIn(Long.valueOf(date.getSystemTime()));
    kiesrWijz.setcUsr(getServices().getGebruiker().getCUsr());
    kiesrWijz.setActietype((long) type.getCode());
    kiesrWijz.setOpm(opmerking);
    return kiesrWijz;
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(KiesrVerk verkiezing) {
    saveEntity(verkiezing);
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(KiesrVerkInfo info) {
    saveEntity(info);
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(KiesrStem kiezer) {
    saveEntity(kiezer);
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(KiesrStem kiezer, KiesrWijz wijz) {
    saveEntity(kiezer);
    wijz.setKiesrStem(kiezer);
    wijz.setcKiesrStem(kiezer.getcCKiesrStem());
    saveEntity(wijz);
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void toevoegen(KiesrStem kiezer, KiesrWijz kiesrWijz) {
    kiezer.setAnr(kiezer.getAnr());
    kiezer.setVnr(getVolgnummer(kiezer));
    kiezer.setPasNr(getPasnummer(kiezer));
    kiezer.setIndToegevoegd(true);

    BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(kiezer.getAnr().toString());
    kiezer.setdGeb(parseLong(pl.getPersoon().getGeboorte().getGeboortedatum().getVal()));
    kiezer.setGeslacht(pl.getPersoon().getGeslacht().getVal());

    // Naam
    Naam naam = pl.getPersoon().getNaam();
    kiezer.setVoorn(naam.getEersteVoornOverigInit());
    kiezer.setNaam(naam.getVoorvGesl());

    // Adres
    Adres adres = pl.getVerblijfplaats().getAdres();
    kiezer.setStraat(adres.getStraat().getValue().getVal());
    kiezer.setHnr(parseLong(adres.getHuisnummer().getValue().getVal()));
    kiezer.setHnrL(adres.getHuisletter().getValue().getVal());
    kiezer.setHnrT(adres.getHuisnummertoev().getValue().getVal());
    kiezer.setPc(adres.getPostcode().getValue().getDescr());
    kiezer.setWpl(adres.getWoonplaats().getValue().getDescr());

    save(kiezer, kiesrWijz);
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void vervangen(KiesrStem kiesrStem, KiesrWijz kiesrWijz) {
    KiesrStem newKiesrStem = new KiesrStem();
    newKiesrStem.setAnr(kiesrStem.getAnr());
    newKiesrStem.setVnr(getVolgnummer(kiesrStem));
    newKiesrStem.setcKiesrVerk(kiesrStem.getcKiesrVerk());
    newKiesrStem.setKiesrVerk(kiesrStem.getKiesrVerk());
    newKiesrStem.setcKiesrVerk(kiesrStem.getcKiesrVerk());
    newKiesrStem.setPasNr(getPasnummer(newKiesrStem));
    newKiesrStem.setVnrVervanging(kiesrStem.getVnr());

    newKiesrStem.setdGeb(kiesrStem.getdGeb());
    newKiesrStem.setGeslacht(kiesrStem.getGeslacht());
    newKiesrStem.setVoorn(kiesrStem.getVoorn());
    newKiesrStem.setNaam(kiesrStem.getNaam());
    newKiesrStem.setStraat(kiesrStem.getStraat());
    newKiesrStem.setHnr(kiesrStem.getHnr());
    newKiesrStem.setHnrL(kiesrStem.getHnrL());
    newKiesrStem.setHnrT(kiesrStem.getHnrT());
    newKiesrStem.setPc(kiesrStem.getPc());
    newKiesrStem.setWpl(kiesrStem.getWpl());
    save(newKiesrStem);

    kiesrStem.setAand(StempasAanduidingType.AAND_VERVANGEN.getCode());
    kiesrStem.setdAand(parseLong(new ProcuraDate().getSystemDate()));
    kiesrStem.settAand(parseLong(new ProcuraDate().getSystemTime()));
    kiesrStem.setVnrVervanging(newKiesrStem.getVnr());
    save(kiesrStem, kiesrWijz);
  }

  public Optional<KiesrStem> getVervangendeStempas(Stempas stempas) {
    List<KiesrStem> stempassen = new ArrayList<>();
    if (stempas.getVnrVervanging() > 0) {
      stempassen.addAll(getStempassen(StempasQuery.builder(stempas.getVerkiezing().getVerk())
          .vnr(stempas.getVnrVervanging())
          .anrKiesgerechtigde(stempas.getAnr())
          .max(1).build()).getStempassen());
    }
    return Optional.ofNullable(stempassen.isEmpty() ? null : stempassen.get(0));
  }

  @Transactional
  public void verwijderAanduiding(Stempas stempas, KiesrWijz kiesrWijz) {
    getVervangendeStempas(stempas).ifPresent(this::delete);
    stempas.setAanduiding(StempasAanduidingType.AAND_GEEN);
    stempas.getStem().setVnrVervanging(-1L);
    save(stempas.getStem(), kiesrWijz);
  }

  private String getPasnummer(KiesrStem kiezer) {
    long cGem = kiezer.getKiesrVerk().getCodeGemeente();
    String formattedNr = String.format("%07d", kiezer.getVnr());
    return String.format("%04d.%s.%s", cGem, formattedNr.substring(0, 3), formattedNr.substring(3, 7));
  }

  private long getVolgnummer(KiesrStem kiezer) {
    return KiesrDao.getVnr(kiezer);
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(List<KiesrStem> kiezers) {
    for (KiesrStem kiezer : kiezers) {
      saveEntity(kiezer);
    }
  }

  @ThrowException("Fout bij verwijderen")
  @Transactional
  public void delete(KiesrVerk verkiezing) {
    removeEntity(verkiezing);
  }

  @ThrowException("Fout bij verwijderen")
  @Transactional
  public void delete(KiesrVerkInfo info) {
    removeEntity(info);
  }

  @ThrowException("Fout bij verwijderen")
  @Transactional
  public void delete(KiesrStem verkiezing) {
    removeEntity(verkiezing);
  }

  public KiesrStem toKiezerPers(KiesrVerk verkiezing, Bestandsregel regel) {
    KiesrStem kiezer = new KiesrStem(verkiezing);
    kiezer.setVnr(regel.getVStem());
    kiezer.setAnr(new Anummer(regel.getAnr()).getLongAnummer());
    kiezer.setdGeb(parseLong(regel.getDGeb()));
    kiezer.setVoorn(regel.getVoorn());
    kiezer.setNaam(regel.getNaam());
    kiezer.setGeslacht(regel.getGeslacht());
    kiezer.setStraat(regel.getStraat());
    kiezer.setHnr(parseLong(regel.getHnr()));
    kiezer.setHnrL(regel.getHnrL());
    kiezer.setHnrT(regel.getHnrT());
    kiezer.setPc(regel.getPc());
    kiezer.setWpl(regel.getWpl());
    kiezer.setPasNr(regel.getPasNr());
    return kiezer;
  }

  public KiesrVerk getKiesrVerk(KiesrVerk verk) {
    return KiesrDao.find(KiesrVerk.class, verk.getcKiesrVerk());
  }

  public long getAantalKiezers(KiesrVerk verk) {
    KiesrDao.KiesrStemQuery query = new KiesrDao.KiesrStemQuery();
    query.setCKiesrVerk(verk.getcKiesrVerk());
    return KiesrDao.getAantalStempassen(query);
  }

  public List<Verkiezing> getVerkiezingen(Anummer anummer) {
    return getSelecteerbareVerkiezingen().stream()
        .map(kiesrVerk -> toVerkiezing(anummer, kiesrVerk))
        .collect(Collectors.toList());
  }

  public StempasResult getStempassen(StempasQuery stempasQuery) {
    KiesrDao.KiesrStemQuery query = new KiesrDao.KiesrStemQuery();
    query.setCKiesrVerk(stempasQuery.getVerkiezing().getcKiesrVerk());
    query.setMax(stempasQuery.getMax());
    if (stempasQuery.getVnr() != null) {
      query.setVnr(stempasQuery.getVnr());
    }
    if (stempasQuery.getOpgenomenInROS() != null) {
      if (stempasQuery.getOpgenomenInROS()) {
        query.setAand(StempasAanduidingType.AAND_ALLE.getCode());
      } else {
        query.setAand(StempasAanduidingType.AAND_GEEN.getCode());
      }
    }
    if (stempasQuery.getAnrKiesgerechtigde() != null) {
      query.setAnr(stempasQuery.getAnrKiesgerechtigde().getLongAnummer());
    }
    if (stempasQuery.getAnrGemachtigde() != null) {
      query.setAnrVolmacht(stempasQuery.getAnrGemachtigde().getLongAnummer());
    }
    if (stempasQuery.getAanduidingType() != null) {
      query.setAand(stempasQuery.getAanduidingType().getCode());
    }
    if (stempasQuery.getDatumAanduidingVan() != null) {
      query.setDAandVan(stempasQuery.getDatumAanduidingVan());
    }
    if (stempasQuery.getDatumAanduidingTm() != null) {
      query.setDAandTm(stempasQuery.getDatumAanduidingTm());
    }
    query.setDesc(stempasQuery.isAflopend());
    KiesrDao.KiesrStemResult kiesrStemResult = KiesrDao.getStempassen(query);
    return StempasResult.builder()
        .stempassen(kiesrStemResult.getStempassen())
        .aantal(kiesrStemResult.getAantal())
        .totaal(kiesrStemResult.getTotaal())
        .build();
  }

  public List<KiesrStem> getStempassenWithMax(KiesrVerk verk, int max) {
    return getStempassen(StempasQuery.builder(verk)
        .max(max)
        .build()).getStempassen();
  }

  public List<KiesrStem> getStempassenByAnr(KiesrVerk verk, Anummer anummer) {
    return getStempassen(StempasQuery.builder(verk)
        .anrKiesgerechtigde(anummer)
        .aflopend(true)
        .build()).getStempassen();
  }

  public List<Stempas> getStempassenByVolmachtAnr(KiesrVerk verk, Anummer anummer) {
    StempasQuery query = StempasQuery.builder(verk).anrGemachtigde(anummer).build();
    return getStempassen(query).getStempassen().stream()
        .map(this::toStempas)
        .collect(Collectors.toList());
  }

  public List<KiesrWijz> getWijzigingen(KiesrVerk verk, Anummer anummer) {
    return KiesrDao.getWijzigingen(verk, anummer);
  }

  public List<KiesrVerkInfo> getInfo(KiesrVerk verk) {
    return KiesrDao.getInfo(verk);
  }

  private Verkiezing toVerkiezing(Anummer anummer, KiesrVerk kiesrVerk) {
    Verkiezing verkiezing = new Verkiezing(kiesrVerk);
    verkiezing.setStempassen(getStempassenByAnr(kiesrVerk, anummer).stream()
        .map(this::toStempas)
        .collect(Collectors.toList()));
    return verkiezing;
  }

  private Stempas toStempas(KiesrStem kiesrStem) {
    Stempas stempas = new Stempas(kiesrStem);
    PersonenWsService wsService = getServices().getPersonenWsService();
    stempas.setStemgerechtigde(wsService.getPersoonslijst(stempas.getAnr().getAnummer()));
    if (stempas.getAnrGemachtigde().isCorrect()) {
      stempas.setGemachtigde(wsService.getPersoonslijst(stempas.getAnrGemachtigde().getAnummer()));
    }
    return stempas;
  }
}

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

package nl.procura.gba.web.services.bs.onderzoek;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.AANGEVER;
import static nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType.FASE_OVERIG;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.UniqueList;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossOnderz;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.onderzoek.enums.*;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierOnderzoek extends DossOnderz implements ZaakDossier {

  private FieldValue aanleidingPlaats   = new FieldValue();
  private FieldValue aanleidingGemeente = new FieldValue();
  private FieldValue aanleidingLand     = new FieldValue();

  private FieldValue resultaatPlaats   = new FieldValue();
  private FieldValue resultaatGemeente = new FieldValue();
  private FieldValue resultaatLand     = new FieldValue();

  private Dossier                    dossier         = new Dossier(ZaakType.ONDERZOEK);
  private List<DossierOnderzoekBron> bronnen         = new UniqueList<>();
  private long                       aantalOnderzoek = -1L;
  private Gemeente                   vermoedelijkeGemeentePostbus;
  private Gemeente                   resultaatGemeentePostbus;

  public DossierOnderzoek() {
    super();
    super.setAanlBron(toBigDecimal(-1));
    super.setAanlAard(toBigDecimal(-1));
    super.setAanlVermoedAdres(toBigDecimal(-1));
    setVermoedelijkeGemeentePostbus(new Gemeente());
    setResultaatGemeentePostbus(new Gemeente());
    setDossier(new Dossier(ZaakType.ONDERZOEK, this));
    getDossier().toevoegenPersoon(AANGEVER);
  }

  @Override
  public void beforeSave() {
    setcDossOnderz(getDossier().getCode());
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
    return getcDossOnderz();
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

  public List<DossierPersoon> getBetrokkenen() {
    return getDossier().getPersonen(DossierPersoonType.BETROKKENE);
  }

  public OnderzoekBronType getOnderzoekBron() {
    return OnderzoekBronType.get(super.getAanlBron().intValue());
  }

  public void setOnderzoekBron(OnderzoekBronType bron) {
    super.setAanlBron(toBigDecimal(bron != null ? bron.getCode() : -1));
  }

  public DateTime getDatumOntvangstMelding() {
    return new DateTime(super.getAanlDMeldOntv());
  }

  public void setDatumOntvangstMelding(DateTime datum) {
    super.setAanlDMeldOntv(datum != null ? datum.getDate() : null);
  }

  public OnderzoekAardType getOnderzoekAard() {
    return OnderzoekAardType.get(super.getAanlAard().intValue());
  }

  public void setOnderzoekAard(OnderzoekAardType aard) {
    super.setAanlAard(toBigDecimal(aard != null ? aard.getCode() : -1));
  }

  public String getOnderzoekAardAnders() {
    return super.getAanlAardAnders();
  }

  public void setOnderzoekAardAnders(String aard) {
    super.setAanlAardAnders(aard);
  }

  public VermoedAdresType getVermoedelijkAdres() {
    return VermoedAdresType.get(super.getAanlVermoedAdres().intValue());
  }

  public void setVermoedelijkAdres(VermoedAdresType adres) {
    super.setAanlVermoedAdres(toBigDecimal(adres != null ? adres.getCode() : -1));
  }

  public FieldValue getAanleidingAdres() {
    return new FieldValue(super.getAanlAdres());
  }

  public void setAanleidingAdres(FieldValue adres) {
    super.setAanlAdres(FieldValue.from(adres).getStringValue());
  }

  public String getAanleidingHnr() {
    return super.getAanlHnr();
  }

  public void setAanleidingHnr(String hnr) {
    super.setAanlHnr(hnr);
  }

  public String getAanleidingHnrL() {
    return super.getAanlHnrL();
  }

  public void setAanleidingHnrL(String hnrL) {
    super.setAanlHnrL(hnrL);
  }

  public FieldValue getAanleidingHnrA() {
    return new FieldValue(super.getAanlHnrA());
  }

  public void setAanleidingHnrA(FieldValue hnrA) {
    super.setAanlHnrA(FieldValue.from(hnrA).getStringValue());
  }

  public String getAanleidingHnrT() {
    return super.getAanlHnrT();
  }

  public void setAanleidingHnrT(String hnrT) {
    super.setAanlHnrT(hnrT);
  }

  public FieldValue getAanleidingPc() {
    return new FieldValue(super.getAanlPc());
  }

  public FieldValue getAanleidingAantalPersonen() {
    return new FieldValue(super.getAanlAantPers());
  }

  public void setAanleidingAantalPersonen(FieldValue value) {
    super.setAanlAantPers(value.getBigDecimalValue());
  }

  public void setAanleidingPc(FieldValue pc) {
    super.setAanlPc(FieldValue.from(pc).getStringValue());
  }

  public FieldValue getAanleidingPlaats() {
    return aanleidingPlaats;
  }

  public void setAanleidingPlaats(FieldValue plaats) {
    this.aanleidingPlaats = FieldValue.from(plaats);
    super.setAanlPlaats(FieldValue.from(plaats).getStringValue());
  }

  public FieldValue getAanleidingGemeente() {
    return aanleidingGemeente;
  }

  public void setAanleidingGemeente(FieldValue gemeente) {
    this.aanleidingGemeente = FieldValue.from(gemeente);
    super.setAanlCGem(FieldValue.from(gemeente).getBigDecimalValue());
  }

  public String getAanleidingBuitenl1() {
    return super.getAanlBuitenl1();
  }

  public void setAanleidingBuitenl1(String buitenl1) {
    super.setAanlBuitenl1(buitenl1);
  }

  public String getAanleidingBuitenl2() {
    return super.getAanlBuitenl2();
  }

  public void setAanleidingBuitenl2(String buitenl2) {
    super.setAanlBuitenl2(buitenl2);
  }

  public String getAanleidingBuitenl3() {
    return super.getAanlBuitenl3();
  }

  public void setAanleidingBuitenl3(String buitenl3) {
    super.setAanlBuitenl3(buitenl3);
  }

  public FieldValue getAanleidingLand() {
    return aanleidingLand;
  }

  public void setAanleidingLand(FieldValue land) {
    this.aanleidingLand = FieldValue.from(land);
    super.setAanlCLand(FieldValue.from(land).getBigDecimalValue());
  }

  public DateTime getDatumEindeTermijn() {
    return new DateTime(super.getBeoordDEndTerm());
  }

  public void setDatumEindeTermijn(DateTime datum) {
    setBeoordDEndTerm(datum != null ? datum.getDate() : null);
  }

  public Boolean getBinnenTermijn() {
    return super.getBeoordBinnenTerm();
  }

  public void setBinnenTermijn(Boolean binnenTermijn) {
    super.setBeoordBinnenTerm(binnenTermijn);
  }

  public boolean isOnderzoekGestart() {
    return getBinnenTermijn() != null && !getBinnenTermijn();
  }

  public String getRedenTermijn() {
    return super.getRedenTerm();
  }

  public void setRedenTermijn(String reden) {
    super.setRedenTerm(reden);
  }

  public DateTime getDatumAanvangOnderzoek() {
    return new DateTime(super.getOnderzDAanvang());
  }

  public void setDatumAanvangOnderzoek(DateTime datum) {
    super.setOnderzDAanvang(datum != null ? datum.getDate() : null);
  }

  public AanduidingOnderzoekType getAanduidingGegevensOnderzoek() {
    return AanduidingOnderzoekType.get(super.getOnderzAandGeg());
  }

  public void setAanduidingGegevensOnderzoek(AanduidingOnderzoekType aanduiding) {
    super.setOnderzAandGeg(aanduiding.getCode());
  }

  public Boolean getGedegenOnderzoek() {
    return super.getOnderzGedegOnderzoek();
  }

  public void setGedegenOnderzoek(Boolean gedegenOnderzoek) {
    super.setOnderzGedegOnderzoek(gedegenOnderzoek);
  }

  public Boolean getRedenOverslaan() {
    return super.getOverslReden();
  }

  public void setRedenOverslaan(Boolean reden) {
    super.setOverslReden(reden);
  }

  public String getToelichtingOverslaan() {
    return super.getOverslToel();
  }

  public void setToelichtingOverslaan(String toelichting) {
    setOverslToel(toelichting);
  }

  public DateTime getFase1DatumIngang() {
    return new DateTime(super.getFase1dIn());
  }

  public void setFase1DatumIngang(DateTime datum) {
    super.setFase1dIn(datum != null ? datum.getDate() : null);
  }

  public DateTime getFase1DatumEinde() {
    return new DateTime(super.getFase1dEnd());
  }

  public void setFase1DatumEinde(DateTime datum) {
    super.setFase1dEnd(datum != null ? datum.getDate() : null);
  }

  public String getFase1Toelichting() {
    return super.getFase1Toel();
  }

  public void setFase1Toelichting(String toelichting) {
    super.setFase1Toel(toelichting);
  }

  public DateTime getFase2DatumIngang() {
    return new DateTime(super.getFase2dIn());
  }

  public void setFase2DatumIngang(DateTime datum) {
    super.setFase2dIn(datum != null ? datum.getDate() : null);
  }

  public DateTime getFase2DatumEinde() {
    return new DateTime(super.getFase2dEnd());
  }

  public void setFase2DatumEinde(DateTime datum) {
    super.setFase2dEnd(datum != null ? datum.getDate() : null);
  }

  public Boolean getFase2OnderzoekGewenst() {
    return super.getFase2OnderzGewenst();
  }

  public void setFase2OnderzoekGewenst(Boolean onderzoekGewenst) {
    super.setFase2OnderzGewenst(onderzoekGewenst);
  }

  public DateTime getFase2DatumOnderzoek() {
    return new DateTime(super.getFase2dUitvOnderzoek());
  }

  public void setFase2DatumOnderzoek(DateTime datum) {
    super.setFase2dUitvOnderzoek(datum != null ? datum.getDate() : null);
  }

  public String getFase2Toelichting() {
    return super.getFase2toel();
  }

  public void setFase2Toelichting(String toelichting) {
    super.setFase2toel(toelichting);
  }

  public BetrokkeneType getResultaatOnderzoekBetrokkene() {
    return BetrokkeneType.get(aval(super.getResOnderzBetrok()));
  }

  public void setResultaatOnderzoekBetrokkene(BetrokkeneType toelichting) {
    super.setResOnderzBetrok(toBigDecimal(toelichting.getCode()));
  }

  public DateTime getDatumEindeOnderzoek() {
    return new DateTime(super.getResOnderzdEnd());
  }

  public void setDatumEindeOnderzoek(DateTime datum) {
    super.setResOnderzdEnd(datum != null ? datum.getDate() : null);
  }

  public Boolean getNogmaalsAanschrijven() {
    return super.getResOnderzNogmaals();
  }

  public void setNogmaalsAanschrijving(Boolean waarde) {
    super.setResOnderzNogmaals(waarde);
  }

  public String getResultaatToelichting() {
    return super.getResToel();
  }

  public void setResultaatToelichting(String toelichting) {
    super.setResToel(toelichting);
  }

  public FieldValue getResultaatAdres() {
    return new FieldValue(super.getResAdres());
  }

  public void setResultaatAdres(FieldValue adres) {
    super.setResAdres(FieldValue.from(adres).getStringValue());
  }

  public Boolean getResultaatAdresGelijk() {
    return super.getResAdresGelijk();
  }

  public void setResultaatAdresGelijk(Boolean waarde) {
    super.setResAdresGelijk(waarde);
  }

  public String getResultaatHnr() {
    return super.getResHnr();
  }

  public void setResultaatHnr(String hnr) {
    super.setResHnr(hnr);
  }

  public String getResultaatHnrL() {
    return super.getResHnrL();
  }

  public void setResultaatHnrL(String hnrL) {
    super.setResHnrL(hnrL);
  }

  public FieldValue getResultaatHnrA() {
    return new FieldValue(super.getResHnrA());
  }

  public void setResultaatHnrA(FieldValue hnrA) {
    super.setResHnrA(FieldValue.from(hnrA).getStringValue());
  }

  public String getResultaatHnrT() {
    return super.getResHnrT();
  }

  public void setResultaatHnrT(String hnrT) {
    super.setResHnrT(hnrT);
  }

  public FieldValue getResultaatPc() {
    return new FieldValue(super.getResPc());
  }

  public void setResultaatPc(FieldValue pc) {
    super.setResPc(FieldValue.from(pc).getStringValue());
  }

  public FieldValue getResultaatPlaats() {
    return resultaatPlaats;
  }

  public void setResultaatPlaats(FieldValue plaats) {
    this.resultaatPlaats = FieldValue.from(plaats);
    super.setResPlaats(FieldValue.from(plaats).getStringValue());
  }

  public FieldValue getResultaatGemeente() {
    return resultaatGemeente;
  }

  public void setResultaatGemeente(FieldValue gemeente) {
    this.resultaatGemeente = FieldValue.from(gemeente);
    super.setResCGem(FieldValue.from(gemeente).getBigDecimalValue());
  }

  public String getResultaatBuitenl1() {
    return super.getResBuitenl1();
  }

  public void setResultaatBuitenl1(String buitenl1) {
    super.setResBuitenl1(buitenl1);
  }

  public String getResultaatBuitenl2() {
    return super.getResBuitenl2();
  }

  public void setResultaatBuitenl2(String buitenl2) {
    super.setResBuitenl2(buitenl2);
  }

  public String getResultaatBuitenl3() {
    return super.getResBuitenl3();
  }

  public void setResultaatBuitenl3(String buitenl3) {
    super.setResBuitenl3(buitenl3);
  }

  public FieldValue getResultaatLand() {
    return resultaatLand;
  }

  public void setResultaatLand(FieldValue land) {
    this.resultaatLand = FieldValue.from(land);
    super.setResCLand(FieldValue.from(land).getBigDecimalValue());
  }

  public FieldValue getResultaatAantalPersonen() {
    return new FieldValue(super.getResAantPers());
  }

  public void setResultaatAantalPersonen(FieldValue value) {
    super.setResAantPers(value.getBigDecimalValue());
  }

  public AanschrijvingFaseType getAanschrijvingFase() {
    AanschrijvingFaseType aanschrijvingFaseType = AanschrijvingFaseType.get(super.getAanschrFase().intValue());
    return getFases().contains(aanschrijvingFaseType) ? aanschrijvingFaseType
        : getFases().stream()
            .findFirst()
            .orElse(FASE_OVERIG);
  }

  public void setAanschrijvingFase(AanschrijvingFaseType type) {
    super.setAanschrFase(toBigDecimal(type.getCode()));
  }

  public DateTime getAanschrDatumInFase1() {
    return new DateTime(super.getAanschrFase1dIn());
  }

  public void setAanschrDatumInFase1(DateTime datum) {
    super.setAanschrFase1dIn(datum != null ? datum.getDate() : null);
  }

  public DateTime getAanschrDatumEindFase1() {
    return new DateTime(super.getAanschrFase1dEnd());
  }

  public void setAanschrDatumEindFase1(DateTime datum) {
    super.setAanschrFase1dEnd(datum != null ? datum.getDate() : null);
  }

  public DateTime getAanschrDatumInFase2() {
    return new DateTime(super.getAanschrFase2dIn());
  }

  public void setAanschrDatumInFase2(DateTime datum) {
    super.setAanschrFase2dIn(datum != null ? datum.getDate() : null);
  }

  public DateTime getAanschrDatumEindFase2() {
    return new DateTime(super.getAanschrFase2dEnd());
  }

  public void setAanschrDatumEindFase2(DateTime datum) {
    super.setAanschrFase2dEnd(datum != null ? datum.getDate() : null);
  }

  public DateTime getAanschrDatumInExtra() {
    return new DateTime(super.getAanschrExtradIn());
  }

  public void setAanschrDatumInExtra(DateTime datum) {
    super.setAanschrExtradIn(datum != null ? datum.getDate() : null);
  }

  public DateTime getAanschrDatumEindExtra() {
    return new DateTime(super.getAanschrExtradEnd());
  }

  public void setAanschrDatumEindExtra(DateTime datum) {
    super.setAanschrExtradEnd(datum != null ? datum.getDate() : null);
  }

  public DateTime getAanschrDatumInVoornemen() {
    return new DateTime(super.getAanschrVoorndIn());
  }

  public void setAanschrDatumInVoornemen(DateTime datum) {
    super.setAanschrVoorndIn(datum != null ? datum.getDate() : null);
  }

  public DateTime getAanschrDatumEindVoornemen() {
    return new DateTime(super.getAanschrVoorndEnd());
  }

  public void setAanschrDatumEindVoornemen(DateTime datum) {
    super.setAanschrVoorndEnd(datum != null ? datum.getDate() : null);
  }

  public DateTime getAanschrDatumBesluit() {
    return new DateTime(super.getAanschrbesluitdIn());
  }

  public void setAanschrDatumBesluit(DateTime datum) {
    super.setAanschrbesluitdIn(datum != null ? datum.getDate() : null);
  }

  @Override
  public boolean isVolledig() {
    return true;
  }

  public long getAantalOnderzoek() {
    return aantalOnderzoek;
  }

  public void setAantalOnderzoek(long aantalOnderzoek) {
    this.aantalOnderzoek = aantalOnderzoek;
  }

  public Gemeente getVermoedelijkeGemeentePostbus() {
    return vermoedelijkeGemeentePostbus;
  }

  public void setVermoedelijkeGemeentePostbus(Gemeente vermoedelijkeGemeente) {
    this.vermoedelijkeGemeentePostbus = vermoedelijkeGemeente;
  }

  public Gemeente getResultaatGemeentePostbus() {
    return resultaatGemeentePostbus;
  }

  public void setResultaatGemeentePostbus(Gemeente resultaatGemeentePostbus) {
    this.resultaatGemeentePostbus = resultaatGemeentePostbus;
  }

  public List<AanschrijvingFaseType> getFases() {

    DateTime fase2 = getFase2DatumIngang();
    Boolean nogmaals = getNogmaalsAanschrijven();
    BetrokkeneType resultaat = getResultaatOnderzoekBetrokkene();

    List<AanschrijvingFaseType> fases = new ArrayList<>();

    if (isOnderzoekGestart()) {
      fases.add(AanschrijvingFaseType.FASE_1);

      if (getFase1Vervolg() != null && getFase1Vervolg()) {
        fases.add(AanschrijvingFaseType.FASE_2);
      }
    }

    if (resultaat != null && !BetrokkeneType.ONBEKEND.equals(resultaat)) {
      if (fase2 != null && fase2.getLongDate() > 0 && nogmaals != null && nogmaals) {
        fases.add(AanschrijvingFaseType.FASE_EXTRA);
      }

      if (!BetrokkeneType.ZELFDE.equals(resultaat)) {
        fases.add(AanschrijvingFaseType.FASE_VOORNEMEN);
        fases.add(AanschrijvingFaseType.FASE_BESLUIT);
      }
    }

    fases.add(FASE_OVERIG);
    return fases;
  }

  public List<DossierOnderzoekBron> getBronnen() {
    return bronnen;
  }

  public void setBronnen(List<DossierOnderzoekBron> bronnen) {
    this.bronnen = bronnen;
  }
}

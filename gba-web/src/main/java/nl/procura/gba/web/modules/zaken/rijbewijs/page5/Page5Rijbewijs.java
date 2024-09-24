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

package nl.procura.gba.web.modules.zaken.rijbewijs.page5;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.beheer.parameters.container.RdwAanpassingenContainer.DEEL1;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page5.Page5RijbewijsBean1.*;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page5.Page5RijbewijsBean2.*;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_PV_NR;
import static nl.procura.gba.web.services.zaken.rijbewijs.NaamgebruikType.EIGEN_NAAM;
import static nl.procura.gba.web.services.zaken.rijbewijs.NaamgebruikType.EIGEN_NAAM_NAAM_PARTNER;
import static nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort.OMWISSELING_BUITENLANDS_RIJBEWIJS;
import static nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort.VERVANGEN_MET_HUIDIGE_GELDIGHEIDSDATA;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Field;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaat;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page5.page5a.Page5aRijbewijs;
import nl.procura.gba.web.modules.zaken.rijbewijs.page7.Page7Rijbewijs;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldTitelType;
import nl.procura.gba.web.services.zaken.rijbewijs.NaamgebruikType;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagReden;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.gba.web.services.zaken.rijbewijs.converters.P1653ToAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.converters.P1654ToAanvraag;
import nl.procura.rdw.functions.RdwAanvraagMessage;
import nl.procura.rdw.functions.RdwMeldingNr;
import nl.procura.rdw.functions.Voorletters;
import nl.procura.rdw.messages.P1651;
import nl.procura.rdw.messages.P1653;
import nl.procura.rdw.messages.P1654;
import nl.procura.rdw.processen.p1651.f08.AANRYBKOVERZ;
import nl.procura.rdw.processen.p1651.f08.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1651.f08.CATAANRYBGEG;
import nl.procura.rdw.processen.p1653.f01.AANVRRYBKRT;
import nl.procura.rdw.processen.p1653.f01.ADRESNATPGEG;
import nl.procura.rdw.processen.p1653.f01.NATPERSOONGEG;
import nl.procura.rdw.processen.p1653.f01.RYBGEG;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Postcode;

/**
 * Nieuwe aanvraag
 */

public class Page5Rijbewijs extends RijbewijsPage {

  private final P1651          message;
  private Page5RijbewijsForm1  form1      = null;
  private Page5RijbewijsForm2  form2      = null;
  private Page5RijbewijsTable1 table1     = null;
  private GbaNativeSelect      fieldSoort = null;
  private Page5RedenField      fieldReden = null;
  private RijbewijsAanvraag    aanvraag   = null;

  public Page5Rijbewijs(P1651 message) {

    super("Nieuwe aanvraag");

    this.message = message;

    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  /**
   * Geeft null terug als string leeg is. Null wordt niet in xml opgenomen
   */
  private static String toNull(String s) {
    return fil(s) ? s : null;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      aanvraag = (RijbewijsAanvraag) getServices().getRijbewijsService().getNewZaak();

      String info = "Selecteer de aanvraagsoort en indien van toepassing de reden. " +
          "Geef aan of er sprake is van een spoedaanvraag en of de naam van de partner vermeld moet worden. " +
          "<br/>Druk op Volgende (F2) om de aanvraag te vervolgen.";

      if (message.getResponse().getMelding().getNr() == RdwMeldingNr.GEEN_PERSONEN.code) {
        info += setClass(false,
            "<hr/>De persoon is niet bekend bij het RDW. Het aantal soorten is daarom beperkt.");
      }

      Page5RijbewijsBean1 bean1 = new Page5RijbewijsBean1();

      setInfo("Aanvraaggegevens registreren", info);
      setRdwInfo(message);
      AANRYBKOVERZ antwoord = null;

      if (!message.getResponse().isFoutMelding()) { // Bestaand persoon
        antwoord = (AANRYBKOVERZ) message.getResponse().getObject();
      }

      preFillBean(bean1, antwoord);

      form1 = new Page5RijbewijsForm1(bean1, antwoord) {

        @Override
        protected void initForm() {
          setCaption("Aanvraag rijbewijs");
          setColumnWidths(WIDTH_130, "230px", "100px", "100px", "180px", "");
          setOrder(SOORT, REDEN, INFO, AFHAAL_LOCATIE, VERVANGTRBW, SPOED, PROCESVERBAAL, GBABESTENDIG,
              NAAMPARTNER, VERMELDING_TITEL, DAGEN185);
        }
      };

      form1.setAfhaalLocaties(getApplication());
      form1.setVermeldingTitel(getPl());

      table1 = new Page5RijbewijsTable1() {

        @Override
        public void onClick(Record record) {
          getNavigation().goToPage(new Page5aRijbewijs((CATAANRYBGEG) record.getObject()));
        }
      };

      Page5RijbewijsBean2 bean2 = new Page5RijbewijsBean2();
      bean2.setIdBewijs(getServices().getIdentificatieService().getIdentificatie(getPl()).getKorteOmschrijving());

      form2 = new Page5RijbewijsForm2(bean2) {

        @Override
        protected void initForm() {
          setCaption("Omwisseling voor Nederlands rijbewijs");
          setColumnWidths(WIDTH_130, "", "130px", "");
          setOrder(IDBEWIJS, VERBLIJFSTATUS, NATIONALITEITEN, DATUMINNL, UITLAND, DATUMUITNL, NAARLAND);
        }
      };

      initFields(antwoord);

      addComponent(form1);
      addComponent(new Fieldset("Rijbewijscategorieën", table1));
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    if (form2.getParent() != null) {
      form2.commit();
    }

    if (along(getPl().getNatio().getNationaliteit().getValue().getVal()) < 0) {
      throw new ProException(WARNING, "Deze persoon heeft geen nationaliteit.");
    }

    final RijbewijsAanvraagSoort soort = form1.getBean().getSoort();
    final RijbewijsAanvraagReden reden = form1.getBean().getReden();

    // PV bij verlies of diefstal
    boolean isVerliesOfDiefstal = RijbewijsAanvraagReden.WEGENS_VERLIES_OF_DIEFSTAL.equals(reden);
    if (soort == RijbewijsAanvraagSoort.VERNIEUWEN && isVerliesOfDiefstal) {
      if (!form1.isValideProcesVerbaal()) {
        throw new ProException(ProExceptionSeverity.INFO, "Het proces-verbaal-nummer is niet ingevuld.");
      }
    }

    // Afhaallocatie
    if (form1.getBean().getAfhaalLocatie() != null) {
      aanvraag.setLocatieAfhaal(form1.getBean().getAfhaalLocatie());
    }

    // Vermelding titel / predikaat
    aanvraag.setVermeldingTitel(form1.getBean().getVermeldingTitel());

    ConfirmDialog confirmDialog = new ConfirmDialog("Wilt u deze aanvraag registreren?") {

      @Override
      public void buttonYes() {

        closeWindow();

        if (soort.getCode() >= OMWISSELING_BUITENLANDS_RIJBEWIJS.getCode()) { // Als er sprake is van een 'andere' rijbewijssoort
          stuur1654f1();
        } else {
          stuur1653f1();
        }
      }
    };

    getWindow().addWindow(confirmDialog);

    super.onNextPage();
  }

  /**
   * Vraag contactgegevens op
   */
  private String getCAant(BasePLExt pl, String a) {
    return getServices().getContactgegevensService().getContactWaarde(pl, a);
  }

  /**
   * Geeft waarde terug als deze niet null is. Anders -1
   */
  private long getFieldValue(FieldValue fv) {
    return fv != null ? along(fv.getValue()) : -1;
  }

  private BasePLValue getVerblijfstitel() {

    BasePLValue vbt = getPl().getVerblijfstitel().getVerblijfstitel();

    if (pos(vbt.getVal())) {
      return vbt;
    }

    BasePLValue e = new BasePLValue();
    e.setCode("0");
    e.setVal("0");
    e.setDescr("Geen");

    return e;
  }

  private void onChangeReden(RijbewijsAanvraagReden reden) {

    // Alleen als er sprake is van verlies of diefstal het proces-verbaalveld tonen
    boolean isVanwegeVerliesOfDiefstal = RijbewijsAanvraagReden.WEGENS_VERLIES_OF_DIEFSTAL == reden;
    Field pvField = form1.getField(PROCESVERBAAL);
    pvField.setReadOnly(!isVanwegeVerliesOfDiefstal);
    pvField.setValue(isVanwegeVerliesOfDiefstal ? "" : Page5RijbewijsForm1.NIET_VAN_TOEPASSING);

    String standaardPvNr = getApplication().getServices().getParameterService().getSysteemParm(RYB_PV_NR, false);
    if (emp(astr(pvField.getValue())) && fil(standaardPvNr)) {
      pvField.setValue(standaardPvNr);
    }
  }

  private void onChangeSoort(AANRYBKOVERZ proces) {

    RijbewijsAanvraagSoort aanvraagSoort = (RijbewijsAanvraagSoort) fieldSoort.getValue();

    if (aanvraagSoort != null) {

      setOmwisselingForm(aanvraagSoort);

      switch (aanvraagSoort) {
        case VERNIEUWEN:
        case VERVANGEN_MET_HUIDIGE_GELDIGHEIDSDATA:
          List<RijbewijsAanvraagReden> redenen = new ArrayList<>();
          redenen.add(RijbewijsAanvraagReden.WEGENS_VERLIES_OF_DIEFSTAL);
          redenen.add(RijbewijsAanvraagReden.WEGENS_BESCHADIGING_OF_ONLEESBAAR);
          redenen.add(RijbewijsAanvraagReden.WEGENS_VERLOOP_GELDIGHEIDSDATUM);
          redenen.add(RijbewijsAanvraagReden.WEGENS_GEDEELTELIJKE_ONGELDIGHEIDSVERKLARING);

          // Alleen voor vervangen, niet voor vernieuwen
          if (VERVANGEN_MET_HUIDIGE_GELDIGHEIDSDATA.equals(aanvraagSoort)) {
            if (getServices().getRijbewijsService().isAanpassingVanToepassing(DEEL1)) {
              redenen.add(RijbewijsAanvraagReden.WEGENS_OVERIGE_REDENEN);
            }
          }

          fieldReden.setContainerDataSource(new RijbewijsAanvraagRedenContainer(redenen));
          break;

        default:
          fieldReden.setContainerDataSource(new RijbewijsAanvraagRedenContainer(RijbewijsAanvraagReden.NVT));
          break;
      }

      if (proces != null) {
        for (AANVRRYBKGEG a : proces.getAanvrrybktab().getAanvrrybkgeg()) {
          if (along(a.getSrtaanvrrybk()) == aanvraagSoort.getCode()) {
            table1.setRecords(a.getCataanrybtab());
          }
        }
      }
    }
  }

  private void preFillBean(Page5RijbewijsBean1 bean1, AANRYBKOVERZ antwoord) {

    BasePLExt pl = getPl();

    boolean is185 = pl.getVerblijfplaats().is185DagenInNL();
    boolean isGBABes = pl.getNatio().isBestendig();

    bean1.setGbaBestendig(isGBABes ? "Ja" : "Nee");
    bean1.setDagen185(is185 ? "Ja" : "Nee");

    if (antwoord != null) { // Bestaand persoon

      // Zet vervangend rijbewijsnummer als deze > 0
      if (pos(antwoord.getRybgeg().getRybnr())) {
        bean1.setVervangtRbw(astr(antwoord.getRybgeg().getRybnr()));
      }
    }

    // Check naam partner. Als naamgebruik niet eigen is dan naam partner op JA.
    bean1.setNaamPartner(!getPl().getPersoon().getNaam().isEigenNaam());
  }

  /**
   * Bij aanvraagsoort >= 10 dan omwisselingform tonen
   */
  private void setOmwisselingForm(RijbewijsAanvraagSoort aanvraagSoort) {

    boolean isOmwisseling = aanvraagSoort.getCode() >= OMWISSELING_BUITENLANDS_RIJBEWIJS.getCode();

    BasePLValue vbt = getVerblijfstitel();

    form2.getBean().setVerblijfstatus(vbt.getVal() + " (" + vbt.getDescr() + ")");
    form2.getBean().setNationaliteiten(getPl().getNatio().getNationaliteiten());
    form1.getField(Page5RijbewijsBean1.SPOED).setEnabled(!isOmwisseling);

    if (isOmwisseling) {
      form1.getField(Page5RijbewijsBean1.SPOED).setValue(false); // Geen spoed bij omwisseling
      addComponent(form2, getComponentIndex(form1) + 1);
    } else {
      removeComponent(form2);
    }

    BasePLRec vb = getPl().getLatestRec(GBACat.VB);

    String dVest = vb.getElemVal(GBAElem.DATUM_VESTIGING_IN_NL).getVal();
    String lVest = vb.getElemVal(GBAElem.LAND_VESTIGING).getVal();

    if (pos(dVest)) {
      form2.getBean().setDatumInNl(new DateFieldValue(dVest));
      form2.getBean().setUitLand(new FieldValue(lVest));
    }

    for (BasePLSet set : getPl().getCat(GBACat.VB).getSets()) {
      for (BasePLRec i : set.getRecs()) {
        String dVertrek = i.getElemVal(GBAElem.DATUM_VERTREK_UIT_NL).getVal();
        String lVertrek = i.getElemVal(GBAElem.LAND_VERTREK).getVal();

        if (pos(dVertrek)) {
          form2.getBean().setDatumUitNl(new DateFieldValue(dVertrek));
          form2.getBean().setNaarLand(new FieldValue(lVertrek));
          break;
        }
      }
    }
  }

  /**
   * Vul de overige gegevens, zoals contactgegevens in.
   */
  private void setOverige(RdwAanvraagMessage m) {

    m.setCodeLandVertrek(-1);
    m.setCodeLandVestiging(-1);
    m.setDatumVertrek(-1);
    m.setDatumVestiging(-1);

    if (form2.getParent() != null) {
      m.setSoortId(form2.getBean().getIdBewijs());
      m.setCodeLandVertrek(getFieldValue(form2.getBean().getNaarLand()));
      m.setCodeLandVestiging(getFieldValue(form2.getBean().getUitLand()));
      m.setDatumVertrek(getFieldValue(form2.getBean().getDatumUitNl()));
      m.setDatumVestiging(getFieldValue(form2.getBean().getDatumInNl()));
    }

    m.setTelThuis(getCAant(getPl(), ContactgegevensService.TEL_THUIS));
    m.setTelMobiel(getCAant(getPl(), ContactgegevensService.TEL_MOBIEL));
    m.setTelWerk(getCAant(getPl(), ContactgegevensService.TEL_WERK));
    m.setEmail(getCAant(getPl(), ContactgegevensService.EMAIL));

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(aanvraag);
  }

  /**
   * Een aanvraagsoort < 10
   */
  private void stuur1653f1() {

    P1653 p1653 = new P1653();

    setOverige(p1653);

    AANVRRYBKRT p1653f1 = p1653.newF1(message);

    RYBGEG g = p1653f1.getRybgeg();
    NATPERSOONGEG n = p1653f1.getNatpersoongeg();
    ADRESNATPGEG a = p1653f1.getAdresnatpgeg();
    nl.procura.rdw.processen.p1653.f01.AANVRRYBKGEG r = p1653f1.getAanvrrybkgeg();

    // Als er geen pv is dan mag rybgeg ook niet voorkomen.
    if (form1.isValideProcesVerbaal()) {
      g.setProcvverld(form1.getBean().getProcesVerbaal());
    } else {
      p1653f1.setRybgeg(null);
    }

    Cat1PersoonExt persoon = getPl().getPersoon();

    boolean vermeldTitel = VermeldTitelType.JA == form1.getBean().getVermeldingTitel();
    String titel = persoon.getTitel().getValue().getVal();
    long bsCode = new BurgerlijkeStaat(getPl()).getStatus().getType().getCode();
    boolean naamPartner = form1.getBean().getNaamPartner();
    NaamgebruikType ngType = naamPartner ? EIGEN_NAAM_NAAM_PARTNER : EIGEN_NAAM;

    // Persoon
    String natpsl = p1653f1.getNatpersoongeg().getNatperssl();
    n.setNatperssl(fil(natpsl) ? natpsl : null);
    n.setGbanrnatp(BigInteger.valueOf(along(persoon.getAnr().getVal())));
    n.setNationalpers(BigInteger.valueOf(along(getPl().getNatio().getNationaliteit().getValue().getVal())));
    n.setFiscnrnatp(BigInteger.valueOf(along(persoon.getBsn().getVal())));
    n.setGeslnaamnatp(toNull(persoon.getNaam().getGeslachtsnaam().getValue().getVal()));
    n.setVoorvoegnatp(toNull(persoon.getNaam().getVoorvoegsel().getValue().getVal()));
    String voorletters = Voorletters.getVoorletters(persoon.getNaam().getVoornamen().getValue().getVal(), false);
    n.setVoorletnatp(toNull(voorletters));
    n.setAdelprednatp(toNull(vermeldTitel ? titel : null));
    n.setVoornaamnatp(toNull(persoon.getNaam().getEersteVoornaam()));
    n.setBurgstnatp(BigInteger.valueOf(bsCode));
    n.setNaamgebrnatp(BigInteger.valueOf(ngType.getRdwCode()));

    // RDW Wijzigingen (deel 1)
    if (getServices().getRijbewijsService().isAanpassingVanToepassing(DEEL1)) {
      n.setVoornamennatp(persoon.getNaam().getVoornamen().getValue().getVal());
      n.setGeslaandnatp(BigInteger.valueOf(Geslacht.get(persoon.getGeslacht().getVal()).getRdwCode()));
    }

    // Echtgeno(o)t(e)
    if (ngType == EIGEN_NAAM_NAAM_PARTNER) {
      BasePLRec huw = getPl().getHuwelijk().getActueelOfMutatieRecord();

      if (huw.hasElems()) {
        n.setGeslnaamechtg(toNull(huw.getElemVal(GBAElem.GESLACHTSNAAM).getVal()));
        n.setVoorvoegechtg(toNull(huw.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getVal()));
        n.setAdelpredechtg(toNull(huw.getElemVal(GBAElem.TITEL_PREDIKAAT).getVal()));
      }
    }

    BasePLValue gebLand = getPl().getPersoon().getGeboorte().getGeboorteland();
    BasePLValue gebPlaats = getPl().getPersoon().getGeboorte().getGeboorteplaats();

    if (pos(gebPlaats.getVal())) {
      n.setAutorcgebpl(BigInteger.valueOf(along(gebPlaats.getVal())));
    } else {
      n.setAutorcgebpl(BigInteger.valueOf(along(gebLand.getVal())));
      n.setGebplbuitenl(toNull(StringUtils.substring(gebPlaats.getDescr(), 0, 35)));
    }

    n.setGebdatnatp(BigInteger.valueOf(along(persoon.getGeboorte().getGeboortedatum().getVal())));

    Adres adres = getPl().getVerblijfplaats().getAdres();
    String pc = adres.getPostcode().getValue().getVal();

    // Adres
    a.setHuisnrnatp(BigInteger.valueOf(along(adres.getHuisnummer().getValue().getVal())));
    a.setHuistvnatp(toNull(trim(adres.getHuisletter().getValue().getVal() + " " +
        adres.getHuisnummertoev().getValue().getVal())));
    a.setWwabverwnp(toNull(adres.getHuisnummeraand().getValue().getVal()));

    if (!Postcode.isEmpty(pc)) {
      a.setPostcnnatp(BigInteger.valueOf(along(Postcode.getNummers(pc))));
      a.setPostcanatp(Postcode.getLetters(pc));
    }

    a.setStraatnatp(toNull(adres.getStraat().getValue().getVal()));
    a.setWoonplnatp(toNull(adres.getPlaats().getValue().getDescr()));
    a.setLocregelnatp(toNull(adres.getLocatie().getValue().getVal()));
    a.setWplregelnatp(null);
    a.setLandcodenatp(null);

    // Rijbewijskaart
    r.setAutorarybk(BigInteger.valueOf(along(getApplication().getServices().getGebruiker().getGemeenteCode())));
    r.setGemlocarybk(
        BigInteger.valueOf(along(getApplication().getServices().getGebruiker().getLocatie().getCodeRas())));
    r.setSrtaanvrrybk(BigInteger.valueOf(form1.getBean().getSoort().getCode()));

    // Alleen reden als deze > 0
    if (pos(form1.getBean().getReden().getCode())) {
      r.setRedenaanrybk(BigInteger.valueOf(form1.getBean().getReden().getCode()));
    }

    r.setGbabestind(getPl().getNatio().isBestendig() ? "J" : "N");
    r.setSpoedafhind(form1.getBean().getSpoed() ? "J" : "N");
    r.setRybnrvervang(toNull(form1.getBean().getVervangtRbw()));

    // Stuur
    if (sendMessage(p1653)) {
      RijbewijsAanvraag rijbewijsAanvraag = P1653ToAanvraag.get(p1653, aanvraag, getPl(), getServices());
      getServices().getRijbewijsService().save(rijbewijsAanvraag);
      getServices().getRijbewijsService().saveVermissing(rijbewijsAanvraag);
      getNavigation().goToPage(new Page7Rijbewijs(aanvraag, p1653));
    }
  }

  /**
   * Een aanvraagsoort >= 10
   */
  private void stuur1654f1() {

    P1654 p1654 = new P1654();

    setOverige(p1654);

    nl.procura.rdw.processen.p1654.f01.AANVRRYBKRT p1654f1 = p1654.newF1(message);
    nl.procura.rdw.processen.p1654.f01.NATPERSOONGEG n = p1654f1.getNatpersoongeg();
    nl.procura.rdw.processen.p1654.f01.ADRESNATPGEG a = p1654f1.getAdresnatpgeg();
    nl.procura.rdw.processen.p1654.f01.AANVRRYBKGEG r = p1654f1.getAanvrrybkgeg();

    Cat1PersoonExt persoon = getPl().getPersoon();

    boolean vermeldTitel = VermeldTitelType.JA == form1.getBean().getVermeldingTitel();
    String titel = persoon.getTitel().getValue().getVal();
    long bsCode = new BurgerlijkeStaat(getPl()).getStatus().getType().getCode();
    boolean naamPartner = form1.getBean().getNaamPartner();
    NaamgebruikType ngType = naamPartner ? EIGEN_NAAM_NAAM_PARTNER : EIGEN_NAAM;

    // Persoon
    n.setGbanrnatp(BigInteger.valueOf(along(persoon.getAnr().getVal())));
    n.setNationalpers(BigInteger.valueOf(along(getPl().getNatio().getNationaliteit().getValue().getVal())));
    n.setFiscnrnatp(BigInteger.valueOf(along(persoon.getBsn().getVal())));
    n.setGeslnaamnatp(toNull(persoon.getNaam().getGeslachtsnaam().getValue().getVal()));
    n.setVoorvoegnatp(toNull(persoon.getNaam().getVoorvoegsel().getValue().getVal()));
    String voorletters = Voorletters.getVoorletters(persoon.getNaam().getVoornamen().getValue().getVal(), false);
    n.setVoorletnatp(toNull(voorletters));
    n.setAdelprednatp(toNull(vermeldTitel ? titel : null));
    n.setVoornaamnatp(toNull(persoon.getNaam().getEersteVoornaam()));
    n.setBurgstnatp(BigInteger.valueOf(bsCode));
    n.setNaamgebrnatp(BigInteger.valueOf(ngType.getRdwCode()));

    // RDW Wijzigingen (deel 1)
    if (getServices().getRijbewijsService().isAanpassingVanToepassing(DEEL1)) {
      n.setVoornamennatp(persoon.getNaam().getVoornamen().getValue().getVal());
      n.setGeslaandnatp(BigInteger.valueOf(Geslacht.get(persoon.getGeslacht().getVal()).getRdwCode()));
    }

    // Echtgeno(o)t(e)
    if (ngType == EIGEN_NAAM_NAAM_PARTNER) {

      BasePLRec huw = getPl().getHuwelijk().getActueelOfMutatieRecord();

      if (huw.hasElems()) {

        n.setGeslnaamechtg(toNull(huw.getElemVal(GBAElem.GESLACHTSNAAM).getVal()));
        n.setVoorvoegechtg(toNull(huw.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getVal()));
        n.setAdelpredechtg(toNull(huw.getElemVal(GBAElem.TITEL_PREDIKAAT).getVal()));
      }
    }

    BasePLValue gebLand = getPl().getPersoon().getGeboorte().getGeboorteland();
    BasePLValue gebPlaats = getPl().getPersoon().getGeboorte().getGeboorteplaats();

    if (pos(gebPlaats.getVal())) {
      n.setAutorcgebpl(BigInteger.valueOf(along(gebPlaats.getVal())));
    } else {
      n.setAutorcgebpl(BigInteger.valueOf(along(gebLand.getVal())));
      n.setGebplbuitenl(toNull(gebPlaats.getDescr()));
    }

    n.setGebdatnatp(BigInteger.valueOf(along(persoon.getGeboorte().getGeboortedatum().getVal())));

    Adres adres = getPl().getVerblijfplaats().getAdres();
    String pc = adres.getPostcode().getValue().getVal();

    // Adres
    a.setHuisnrnatp(BigInteger.valueOf(along(adres.getHuisnummer().getValue().getVal())));
    a.setHuistvnatp(toNull(trim(adres.getHuisletter().getValue().getVal() + " " +
        adres.getHuisnummertoev().getValue().getVal())));
    a.setWwabverwnp(toNull(adres.getHuisnummeraand().getValue().getVal()));

    if (!Postcode.isEmpty(pc)) {
      a.setPostcnnatp(BigInteger.valueOf(along(Postcode.getNummers(pc))));
      a.setPostcanatp(Postcode.getLetters(pc));
    }

    a.setStraatnatp(toNull(adres.getStraat().getValue().getVal()));
    a.setWoonplnatp(toNull(adres.getPlaats().getValue().getDescr()));
    a.setLocregelnatp(toNull(adres.getLocatie().getValue().getVal()));
    a.setWplregelnatp(null);
    a.setLandcodenatp(null);

    // Rijbewijskaart

    r.setAutorarybk(BigInteger.valueOf(along(getApplication().getServices().getGebruiker().getGemeenteCode())));
    r.setGemlocarybk(
        BigInteger.valueOf(along(getApplication().getServices().getGebruiker().getLocatie().getCodeRas())));
    r.setSrtaanvrrybk(BigInteger.valueOf(form1.getBean().getSoort().getCode()));
    r.setGbabestind(getPl().getNatio().isBestendig() ? "J" : "N");
    r.setRybnrvervang(toNull(form1.getBean().getVervangtRbw()));

    // Stuur
    if (sendMessage(p1654)) {
      RijbewijsAanvraag rijbewijsAanvraag = P1654ToAanvraag.get(p1654, aanvraag, getPl(), getServices());
      getServices().getRijbewijsService().save(rijbewijsAanvraag);
      getServices().getRijbewijsService().saveVermissing(rijbewijsAanvraag);
      getNavigation().goToPage(new Page7Rijbewijs(aanvraag, p1654));
    }
  }

  private void initFields(final AANRYBKOVERZ proces) {

    fieldSoort = (GbaNativeSelect) form1.getField(SOORT);
    fieldReden = (Page5RedenField) form1.getField(REDEN);

    if (proces != null) { // Persoon wel bekend
      fieldSoort.setContainerDataSource(
          new RijbewijsAanvraagSoortContainer(proces.getAanvrrybktab().getAanvrrybkgeg()));
      onChangeSoort(proces);
    } else { // Persoon niet bekend

      fieldSoort.setContainerDataSource(new RijbewijsAanvraagSoortContainerOnbekend());
      fieldReden.setContainerDataSource(new RijbewijsAanvraagRedenContainer(RijbewijsAanvraagReden.NVT));
      fieldReden.setValue(RijbewijsAanvraagReden.NVT);
      fieldReden.setNullSelectionAllowed(false);
    }

    fieldSoort.addListener((ValueChangeListener) event -> onChangeSoort(proces));

    fieldReden.addListener(new FieldChangeListener<RijbewijsAanvraagReden>() {

      @Override
      public void onChange(RijbewijsAanvraagReden value) {
        onChangeReden(value);
      }
    });
  }
}

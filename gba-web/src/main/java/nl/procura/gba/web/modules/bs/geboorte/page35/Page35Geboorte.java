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

package nl.procura.gba.web.modules.bs.geboorte.page35;

import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.ErkenningsApplicatie.BUITEN_PROWEB;
import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.ErkenningsApplicatie.IN_PROWEB;
import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteBean1.ERKENNINGS_TYPE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteBean1.GEZIN;
import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteBean1.NAAMSKEUZE_TYPE;
import static nl.procura.gba.web.services.bs.erkenning.ErkenningsType.ERKENNING_BIJ_AANGIFTE;
import static nl.procura.gba.web.services.bs.erkenning.ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT;
import static nl.procura.gba.web.services.bs.erkenning.ErkenningsType.GEEN_ERKENNING;
import static nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType.ERKENNER;
import static nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType.JA;
import static nl.procura.gba.web.services.bs.geboorte.GezinssituatieType.BINNEN_HETERO_HUWELIJK;
import static nl.procura.gba.web.services.bs.geboorte.GezinssituatieType.BINNEN_HOMO_HUWELIJK;
import static nl.procura.gba.web.services.bs.geboorte.GezinssituatieType.BUITEN_HUWELIJK;
import static nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType.RECHTBANK;
import static nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsKindTable;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.geboorte.BsPageGeboorte;
import nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.BinnenProwebLayoutErk;
import nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.BinnenProwebLayoutNk;
import nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.BuitenProwebLayoutErk;
import nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.BuitenProwebLayoutNk;
import nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanErk;
import nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb.Page35GeboorteBeanNk;
import nl.procura.gba.web.modules.bs.geboorte.page35.form1.ErkenningsApplicatie;
import nl.procura.gba.web.modules.bs.geboorte.page35.form1.NaamskeuzeApplicatie;
import nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteForm1;
import nl.procura.gba.web.modules.bs.geboorte.page40.Page40Geboorte;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.EersteKindType;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.erkenning.argumenten.ErkenningArgumenten;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.bs.geboorte.naamskeuzebuitenproweb.NaamskeuzeBuitenProweb;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;
import nl.procura.gba.web.services.bs.naamskeuze.argumenten.NaamskeuzeArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page35Geboorte<T extends DossierGeboorte> extends BsPageGeboorte<T> {

  private BinnenProwebLayoutErk binnenProwebLayoutErk = null;
  private BuitenProwebLayoutErk buitenProwebLayoutErk = null;
  private BinnenProwebLayoutNk  binnenProwebLayoutNk  = null;
  private BuitenProwebLayoutNk  buitenProwebLayoutNk  = null;
  private Page35GeboorteForm1   situatieForm          = null;

  public Page35Geboorte() {

    this("Geboorte - gezinssituatie");
  }

  public Page35Geboorte(String title) {
    super(title);
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    situatieForm.commit();

    nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteBean1 b1 = situatieForm.getBean();

    getZaakDossier().setGezinssituatie(b1.getGezin());
    getZaakDossier().setErkenningsType(b1.getErkenningsType());
    getZaakDossier().setNaamskeuzeSoort(b1.getNaamskeuzeType());

    ErkenningsApplicatie erkenningsApp = situatieForm.getBean().getErkenningsApp();

    if (erkenningsApp != null) {
      switch (erkenningsApp) {
        case IN_PROWEB:
          if (!getZaakDossier().getVragen().heeftErkenningVoorGeboorte()) {
            throw new ProException(WARNING, "Koppel eerst een erkenning");
          }

          break;

        case BUITEN_PROWEB:
          buitenProwebLayoutErk.getForm().commit();

          Page35GeboorteBeanErk bean = buitenProwebLayoutErk.getForm().getBean();
          ErkenningBuitenProweb bp = getZaakDossier().getErkenningBuitenProweb();
          bp.setAktenummer(bean.getAktenr());
          bp.setDatumErkenning(new DateTime(bean.getDatum().getBigDecimalValue()));
          bp.setGemeente(bean.getGemeente());
          bp.setLandErkenning(bean.getLand());
          bp.setBuitenlandsePlaats(bean.getPlaats());
          bp.setToestemminggeverType(bean.getToestemminggeverType());
          bp.setVerklaringGezag(bean.isVerklaringGezag());
          bp.setRechtbank(RECHTBANK.equals(bean.getToestemminggeverType()) ? bean.getRechtbank().getCode() : "");
          bp.setNaamskeuzeType(bean.getNaamskeuzeType());
          bp.setNaamskeuzePersoon(JA.equals(bean.getNaamskeuzeType()) ? ERKENNER : bean.getNaamsPersoonType());
          bp.setLandAfstamming(bean.getAfstammingsrecht());
          break;

        default:
          break;
      }
    }

    NaamskeuzeApplicatie naamskeuzeApp = situatieForm.getBean().getNaamskeuzeApp();

    if (naamskeuzeApp != null) {
      switch (naamskeuzeApp) {
        case IN_PROWEB:
          if (!getZaakDossier().getVragen().heeftNaamskeuzeVoorGeboorte()) {
            throw new ProException(WARNING, "Koppel eerst een naamskeuze");
          }

          break;

        case BUITEN_PROWEB:
          buitenProwebLayoutNk.getForm().commit();

          Page35GeboorteBeanNk bean = buitenProwebLayoutNk.getForm().getBean();
          NaamskeuzeBuitenProweb bp = getZaakDossier().getNaamskeuzeBuitenProweb();
          bp.setAktenummer(bean.getAktenr());
          bp.setDatum(new DateTime(bean.getDatum().getBigDecimalValue()));
          bp.setGemeente(bean.getGemeente());
          bp.setLand(bean.getLand());
          bp.setBuitenlandsePlaats(bean.getPlaats());
          bp.setNaamskeuzePersoon(bean.getNaamsPersoonType());
          bp.setGeslachtsnaam(bean.getGeslachtsnaam());
          bp.setVoorvoegsel(bean.getVoorv());
          bp.setTitel(bean.getTitel());
          bp.setBijzonderheden(bean.getBijzonderheden());
          getZaakDossier().setEersteKindType(EersteKindType.JA);
          getZaakDossier().setNaamskeuzeType(JA);
          break;

        default:
          break;
      }
    }

    getApplication().getServices().getGeboorteService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {
      if (event.isEvent(InitPage.class)) {
        addComponent(new BsStatusForm(getDossier()));
        initForms();
        addComponent(situatieForm);
        addComponent(new Fieldset("Kind(eren) van de moeder",
            new PageBsKindTable(getZaakDossier().getMoeder())));

        binnenProwebLayoutErk = new BinnenProwebLayoutErk(getZaakDossier());
        buitenProwebLayoutErk = new BuitenProwebLayoutErk(getZaakDossier());

        binnenProwebLayoutNk = new BinnenProwebLayoutNk(getZaakDossier());
        buitenProwebLayoutNk = new BuitenProwebLayoutNk(getZaakDossier());

        situatieForm.initFields();
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void onNextPage() {

    situatieForm.commit();

    GezinssituatieType gezinType = situatieForm.getBean().getGezin();
    ErkenningsType erkenningsType = situatieForm.getBean().getErkenningsType();

    if (isVaderMogelijk(gezinType, erkenningsType)) {
      getProcessen().getProces(Page40Geboorte.class).forceStatus(BsProcesStatus.EMPTY);

    } else {
      getProcessen().getProces(Page40Geboorte.class).forceStatus(BsProcesStatus.DISABLED);
    }

    goToNextProces();
    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
    super.onPreviousPage();
  }

  private void initForms() {

    situatieForm = new Page35GeboorteForm1(getServices(), getZaakDossier()) {

      @Override
      protected void updateGekoppeldeZaakLayouts(final ErkenningsApplicatie erkApp, final NaamskeuzeApplicatie nkApp,
          boolean init) {
        GezinssituatieType gezin = (GezinssituatieType) getField(GEZIN).getValue();
        ErkenningsType erkType = (ErkenningsType) getField(ERKENNINGS_TYPE).getValue();
        NaamskeuzeType nkType = (NaamskeuzeType) getField(NAAMSKEUZE_TYPE).getValue();

        if (!init) {
          resetErkenningEnNaamskeuze();
        }

        if (gezin.is(BINNEN_HETERO_HUWELIJK, BINNEN_HOMO_HUWELIJK) && NAAMSKEUZE_VOOR_GEBOORTE.is(nkType)) {
          if (NaamskeuzeApplicatie.IN_PROWEB.is(nkApp)) {
            Page35Geboorte.this.addComponent(binnenProwebLayoutNk);
          }
          if (NaamskeuzeApplicatie.BUITEN_PROWEB.is(nkApp)) {
            Page35Geboorte.this.addComponent(buitenProwebLayoutNk);
          }
        }

        if (gezin.is(BUITEN_HUWELIJK, BINNEN_HOMO_HUWELIJK) && ERKENNING_ONGEBOREN_VRUCHT.is(erkType)) {
          if (IN_PROWEB.is(erkApp)) {
            Page35Geboorte.this.addComponent(binnenProwebLayoutErk);
          }
          if (BUITEN_PROWEB.is(erkApp)) {
            Page35Geboorte.this.addComponent(buitenProwebLayoutErk);
          }
        }
      }

      private void resetErkenningEnNaamskeuze() {
        Page35Geboorte.this.removeComponent(buitenProwebLayoutErk);
        Page35Geboorte.this.removeComponent(binnenProwebLayoutErk);
        Page35Geboorte.this.removeComponent(buitenProwebLayoutNk);
        Page35Geboorte.this.removeComponent(binnenProwebLayoutNk);
        getZaakDossier().setErkenningVoorGeboorte(null);
        getZaakDossier().setNaamskeuzeVoorGeboorte(null);
        getZaakDossier().getErkenningBuitenProweb().reset();
        getZaakDossier().getNaamskeuzeBuitenProweb().reset();
      }

      @Override
      protected DossierErkenning getProwebErkenning() {
        ErkenningArgumenten zaakArgumenten = new ErkenningArgumenten();
        zaakArgumenten.setMoeder(getZaakDossier().getMoeder());
        zaakArgumenten.setdInvoerVanaf(along(new ProcuraDate().addYears(-1).getSystemDate()));

        List<Zaak> zaken = getServices().getErkenningService().getOngeborenErkenningen(zaakArgumenten);
        if (zaken.size() == 1) {
          return (DossierErkenning) ((Dossier) zaken.get(0)).getZaakDossier();
        }
        return null;
      }

      @Override
      protected DossierNaamskeuze getProwebNaamskeuze() {
        NaamskeuzeArgumenten zaakArgumenten = new NaamskeuzeArgumenten();
        zaakArgumenten.setMoeder(getZaakDossier().getMoeder());
        zaakArgumenten.setdInvoerVanaf(along(new ProcuraDate().addYears(-1).getSystemDate()));

        List<Zaak> zaken = getServices().getNaamskeuzeService().getNaamskeuzesVoorGeboorte(zaakArgumenten);
        if (zaken.size() == 1) {
          return (DossierNaamskeuze) ((Dossier) zaken.get(0)).getZaakDossier();
        }
        return null;
      }
    };
  }

  /**
   * Kan deze geboorte een vader hebben?
   */
  private boolean isVaderMogelijk(GezinssituatieType gezinType, ErkenningsType erkenningsType) {
    return !(gezinType.is(BUITEN_HUWELIJK, BINNEN_HOMO_HUWELIJK)
        && erkenningsType.is(GEEN_ERKENNING, ERKENNING_BIJ_AANGIFTE));
  }
}

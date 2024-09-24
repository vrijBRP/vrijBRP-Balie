/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.bs.geboorte.page35.form1;

import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteBean1.ERKENNINGS_APP;
import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteBean1.ERKENNINGS_TYPE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteBean1.GEZIN;
import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteBean1.NAAMSKEUZE_APP;
import static nl.procura.gba.web.modules.bs.geboorte.page35.form1.Page35GeboorteBean1.NAAMSKEUZE_TYPE;
import static nl.procura.gba.web.services.bs.erkenning.ErkenningsType.GEEN_ERKENNING;
import static nl.procura.gba.web.services.bs.geboorte.GezinssituatieType.BINNEN_HETERO_HUWELIJK;
import static nl.procura.gba.web.services.bs.geboorte.GezinssituatieType.BINNEN_HOMO_HUWELIJK;
import static nl.procura.gba.web.services.bs.geboorte.GezinssituatieType.BUITEN_HUWELIJK;
import static nl.procura.standard.Globalfunctions.emp;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.bs.common.utils.BsHuwelijkUtils;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public abstract class Page35GeboorteForm1 extends GbaForm<Page35GeboorteBean1> {

  private final DossierGeboorte geboorte;

  public Page35GeboorteForm1(Services services, DossierGeboorte geboorte) {
    this.geboorte = geboorte;

    setCaption("Gezinssituatie");
    setOrder(GEZIN, ERKENNINGS_TYPE, ERKENNINGS_APP, NAAMSKEUZE_TYPE, NAAMSKEUZE_APP);
    setColumnWidths("200px", "");
    setGeboorte(services, geboorte);
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);

    getField(GEZIN).addListener(new FieldChangeListener<GezinssituatieType>() {

      @Override
      public void onChange(GezinssituatieType gezin) {
        ErkenningsType erkType = (ErkenningsType) getField(ERKENNINGS_TYPE).getValue();
        bijWijzigingGezin(gezin, erkType);
        getField(ERKENNINGS_TYPE).setValue(null);
        getField(NAAMSKEUZE_TYPE).setValue(null);
        bijWijzigingErkenningsType(gezin, erkType);
        bijWijzigingNaamskeuzeType(null);
      }
    });

    getField(ERKENNINGS_TYPE).addListener(new FieldChangeListener<ErkenningsType>() {

      @Override
      public void onChange(ErkenningsType type) {
        bijWijzigingErkenningsType((GezinssituatieType) getField(GEZIN).getValue(), type);
        getField(ERKENNINGS_APP).setValue(null);
        updateGekoppeldeZaakLayouts(null, null, false);
      }
    });

    getField(ERKENNINGS_APP).addListener(new FieldChangeListener<ErkenningsApplicatie>() {

      @Override
      public void onChange(ErkenningsApplicatie app) {
        updateGekoppeldeZaakLayouts(app, null, false);
      }
    });

    getField(NAAMSKEUZE_TYPE).addListener(new FieldChangeListener<NaamskeuzeType>() {

      @Override
      public void onChange(NaamskeuzeType type) {
        bijWijzigingNaamskeuzeType(type);
        getField(NAAMSKEUZE_APP).setValue(null);
        updateGekoppeldeZaakLayouts(null, null, false);
      }
    });

    getField(NAAMSKEUZE_APP).addListener(new FieldChangeListener<NaamskeuzeApplicatie>() {

      @Override
      public void onChange(NaamskeuzeApplicatie app) {
        updateGekoppeldeZaakLayouts(null, app, false);
      }
    });
  }

  @Override
  public Page35GeboorteBean1 getNewBean() {
    return new Page35GeboorteBean1();
  }

  public void initFields() {
    GezinssituatieType gezin = getBean().getGezin();
    ErkenningsType erkType = getBean().getErkenningsType();
    ErkenningsApplicatie erkApp = getBean().getErkenningsApp();
    NaamskeuzeType nkType = getBean().getNaamskeuzeType();
    NaamskeuzeApplicatie nkApp = getBean().getNaamskeuzeApp();

    bijWijzigingGezin(gezin, erkType);
    bijWijzigingErkenningsType(gezin, erkType);
    bijWijzigingNaamskeuzeType(nkType);
    updateGekoppeldeZaakLayouts(erkApp, nkApp, true);
  }

  public void setCaptionAndOrder() {
  }

  protected abstract DossierErkenning getProwebErkenning();

  protected abstract DossierNaamskeuze getProwebNaamskeuze();

  private void bijWijzigingGezin(GezinssituatieType gezin, ErkenningsType erkType) {
    boolean binnenHeteroHuw = BINNEN_HETERO_HUWELIJK.equals(gezin);
    boolean binnenHomoHuw = BINNEN_HOMO_HUWELIJK.equals(gezin);
    boolean geenErkenning = GEEN_ERKENNING.equals(erkType);

    getField(ERKENNINGS_TYPE).setVisible(!binnenHeteroHuw);
    getField(NAAMSKEUZE_TYPE).setVisible(binnenHeteroHuw || (binnenHomoHuw && geenErkenning));

    repaint();
  }

  private void bijWijzigingErkenningsType(GezinssituatieType gezin, ErkenningsType erkType) {
    boolean binnenHeteroHuw = BINNEN_HETERO_HUWELIJK.is(gezin);
    boolean binnenHomoHuw = BINNEN_HOMO_HUWELIJK.is(gezin);
    boolean geenErkenning = GEEN_ERKENNING.equals(erkType);

    getField(ERKENNINGS_APP).setVisible(ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT.equals(erkType));
    getField(NAAMSKEUZE_TYPE).setVisible(binnenHeteroHuw || (binnenHomoHuw && geenErkenning));
    getField(NAAMSKEUZE_APP).setVisible(binnenHeteroHuw || (binnenHomoHuw && geenErkenning));

    repaint();
  } // Override

  @SuppressWarnings("unused")
  protected abstract void updateGekoppeldeZaakLayouts(ErkenningsApplicatie erkApp, NaamskeuzeApplicatie nkApp,
      boolean init);

  private void bijWijzigingNaamskeuzeType(NaamskeuzeType type) {
    getField(NAAMSKEUZE_APP).setVisible(NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE.equals(type));
    repaint();
  }

  private void detecteerProwebErkenning(Page35GeboorteBean1 bean) {
    DossierErkenning prowebErkenning = getProwebErkenning();
    if (bean.getGezin().is(BUITEN_HUWELIJK, BINNEN_HOMO_HUWELIJK) && prowebErkenning != null) {
      bean.setErkenningsType(ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT);
      bean.setErkenningsApp(ErkenningsApplicatie.IN_PROWEB);
      geboorte.setErkenningVoorGeboorte(prowebErkenning);
    }
  }

  private void detecteerProwebNaamskeuze(Page35GeboorteBean1 bean) {
    DossierNaamskeuze prowebNaamskeuze = getProwebNaamskeuze();
    if (bean.getGezin().is(BINNEN_HETERO_HUWELIJK) && prowebNaamskeuze != null) {
      bean.setNaamskeuzeType(NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE);
      bean.setNaamskeuzeApp(NaamskeuzeApplicatie.IN_PROWEB);
      geboorte.setNaamskeuzeVoorGeboorte(prowebNaamskeuze);

    } else if (bean.getGezin().is(BINNEN_HOMO_HUWELIJK) && prowebNaamskeuze != null) {
      bean.setErkenningsType(GEEN_ERKENNING);
      bean.setNaamskeuzeType(NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE);
      bean.setNaamskeuzeApp(NaamskeuzeApplicatie.IN_PROWEB);
      geboorte.setNaamskeuzeVoorGeboorte(prowebNaamskeuze);
    }
  }

  private void setGeboorte(Services services, DossierGeboorte geboorte) {

    Page35GeboorteBean1 bean = new Page35GeboorteBean1();

    if (geboorte.getGezinssituatie() != GezinssituatieType.ONBEKEND) {
      bean.setGezin(geboorte.getGezinssituatie());
    }

    if (geboorte.getErkenningsType() != ErkenningsType.ONBEKEND) {
      bean.setErkenningsType(geboorte.getErkenningsType());
    }

    if (geboorte.getErkenningsType() == ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT) {
      if (geboorte.getVragen().heeftErkenningBuitenProweb()) {
        bean.setErkenningsApp(ErkenningsApplicatie.BUITEN_PROWEB);
      }

      if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {
        bean.setErkenningsApp(ErkenningsApplicatie.IN_PROWEB);
      }
    }

    if (geboorte.getNaamskeuzeSoort() != NaamskeuzeType.ONBEKEND) {
      bean.setNaamskeuzeType(geboorte.getNaamskeuzeSoort());
    }

    if (geboorte.getNaamskeuzeSoort() == NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE) {
      if (geboorte.getVragen().heeftNaamskeuzeBuitenProweb()) {
        bean.setNaamskeuzeApp(NaamskeuzeApplicatie.BUITEN_PROWEB);
      }

      if (geboorte.getVragen().heeftNaamskeuzeVoorGeboorte()) {
        bean.setNaamskeuzeApp(NaamskeuzeApplicatie.IN_PROWEB);
      }
    }

    if (emp(geboorte.getGezinssituatie().getCode())) {
      for (DossierPersoon kind : geboorte.getKinderen()) {
        long geboorteDatum = kind.getDatumGeboorte().getLongDate();
        BsnFieldValue bsnMoeder = geboorte.getMoeder().getBurgerServiceNummer();
        bean.setGezin(BsHuwelijkUtils.getGezinssituatie(services, geboorteDatum, bsnMoeder));
        detecteerProwebErkenning(bean);
        detecteerProwebNaamskeuze(bean);
      }
    }

    setBean(bean);
  }
}

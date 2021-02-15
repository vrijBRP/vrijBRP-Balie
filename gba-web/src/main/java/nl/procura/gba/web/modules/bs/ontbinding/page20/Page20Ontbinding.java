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

package nl.procura.gba.web.modules.bs.ontbinding.page20;

import static java.util.Arrays.asList;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Button;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdeLayout;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdePersoon;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsAkteUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page20Ontbinding extends BsPage<DossierOntbinding> {

  private final Button   buttonResetData = new Button("Reset gegevens");
  private Form1          form1           = null;
  private Form2          form2           = null;
  private RelatiesLayout relatiesLayout1 = null;
  private RelatiesLayout relatiesLayout2 = null;

  public Page20Ontbinding() {
    super("Ontbinding/einde huwelijk/GPS in gemeente - huidige situatie");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    super.checkPage();

    form1.commit();
    form2.commit();

    getZaakDossier().setSoortVerbintenis(form1.getBean().getSoort());
    getZaakDossier().setDatumVerbintenis(new DateTime(form1.getBean().getDatum()));
    getZaakDossier().setPlaatsVerbintenis(form1.getPlaats());
    getZaakDossier().setLandVerbintenis(form1.getBean().getLand());

    getZaakDossier().setBsAkteNummerVerbintenis(form2.getBean().getBsAkteNummer());
    getZaakDossier().setBrpAkteNummerVerbintenis(form2.getBean().getBrpAkteNummer());
    getZaakDossier().setAktePlaatsVerbintenis(form2.getBean().getAktePlaats());
    getZaakDossier().setAkteJaarVerbintenis(aval(form2.getBean().getAkteJaar()));

    getApplication().getServices().getOntbindingService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {
      if (event.isEvent(InitPage.class)) {

        addComponent(new BsStatusForm(getDossier()));

        setInfo("De huidige situatie van de verbintenis");

        form1 = new Form1();
        form2 = new Form2();

        resetGegevens(false);

        OptieLayout optieLayout = new OptieLayout();
        optieLayout.getLeft().addComponent(form1);
        optieLayout.getLeft().addComponent(form2);

        optieLayout.getRight().setWidth("170px");
        optieLayout.getRight().setCaption("Opties");
        optieLayout.getRight().addButton(buttonResetData, this);

        addComponent(optieLayout);

        relatiesLayout1 = new RelatiesLayout(getZaakDossier().getPartner1());
        relatiesLayout1.setMargin(true, false, false, false);
        relatiesLayout2 = new RelatiesLayout(getZaakDossier().getPartner2());
        relatiesLayout2.setMargin(true, false, false, false);

        GbaTabsheet personenTabsheet = new GbaTabsheet();
        personenTabsheet.addStyleName("zoektab");
        personenTabsheet.addTab(relatiesLayout1, "Partner 1");
        personenTabsheet.addTab(relatiesLayout2, "Partner 2");

        addComponent(new Fieldset("Gerelateerden"));
        addExpandComponent(personenTabsheet);
      } else if (event.isEvent(AfterReturn.class)) {
        relatiesLayout1.init();
        relatiesLayout2.init();
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonResetData) {
      resetGegevens(true);
      infoMessage("De akte- en sluitinggegevens zijn opnieuw geladen");
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  public void resetGegevens(boolean force) {

    DossierPersoon p1 = getZaakDossier().getPartner1();
    DossierPersoon p2 = getZaakDossier().getPartner2();

    Page20OntbindingBean1 bean1 = new Page20OntbindingBean1();
    Page20OntbindingBean2 bean2 = new Page20OntbindingBean2();

    updateBean(force, bean1, bean2, getZaakDossier(), p1, p2);

    form1.setBean(bean1);
    form2.setBean(bean2);
  }

  private void updateBean(boolean force, Page20OntbindingBean1 bean1, Page20OntbindingBean2 bean2,
      DossierOntbinding zaakDossier, DossierPersoon... personen) {

    if (!force && zaakDossier.getDatumVerbintenis().getLongDate() > 0) {

      bean1.setSoort(zaakDossier.getSoortVerbintenis());

      // GPS sluitingsgegevens
      bean1.setDatum(zaakDossier.getDatumVerbintenis().getDate());

      if (pos(zaakDossier.getPlaatsVerbintenis().getStringValue())) {
        bean1.setPlaatsNL(zaakDossier.getPlaatsVerbintenis());
      } else {
        bean1.setPlaatsBuitenland(zaakDossier.getDatumVerbintenis().toString());
      }

      bean1.setLand(zaakDossier.getLandVerbintenis());

      // GPS Aktegegevens
      bean2.setBsAkteNummer(zaakDossier.getBsAkteNummerVerbintenis());
      bean2.setBrpAkteNummer(zaakDossier.getBrpAkteNummerVerbintenis());
      bean2.setAktePlaats(zaakDossier.getAktePlaatsVerbintenis());

      if (pos(zaakDossier.getAkteJaarVerbintenis())) {
        bean2.setAkteJaar(astr(zaakDossier.getAkteJaarVerbintenis()));
      }
    } else {

      // GPS gegevens niet gezet

      for (DossierPersoon p : personen) {

        if (p.isVolledig() && p.isIngeschreven()) {

          BasePLExt persoon = getPersoonslijst(p.getBurgerServiceNummer());
          BasePLRec partnerRecord = persoon.getLatestRec(GBACat.HUW_GPS);

          if (partnerRecord.getStatus() == GBARecStatus.CURRENT) {

            String soort = partnerRecord.getElemVal(SOORT_VERBINTENIS).getVal();
            String dHuw = partnerRecord.getElemVal(DATUM_VERBINTENIS).getVal();
            String dOntb = partnerRecord.getElemVal(DATUM_ONTBINDING).getVal();
            String plaats = partnerRecord.getElemVal(PLAATS_VERBINTENIS).getVal();
            long land = partnerRecord.getElemVal(LAND_VERBINTENIS).toLong();
            String aktenummer = partnerRecord.getElemVal(AKTENR).getVal();
            String gemeente = partnerRecord.getElemVal(REGIST_GEM_AKTE).getVal();

            if (pos(dHuw) && aval(dOntb) < 0) {

              // Sluitingsgegevens
              bean1.setSoort(SoortVerbintenis.get(soort));
              bean1.setDatum(new DateTime(dHuw).getDate());
              bean1.setPlaatsNL(new FieldValue(plaats));
              bean1.setLand(new FieldValue(land));

              // Aktegegevens
              bean2.setAkteJaar(new ProcuraDate(astr(dHuw)).getYear());
              bean2.setBsAkteNummer(BsAkteUtils.getBsAktenummer(aktenummer));
              bean2.setBrpAkteNummer(BsAkteUtils.getBrpAktenummer(aktenummer, "B"));
              bean2.setAktePlaats(new FieldValue(gemeente));
            }
          }
        }
      }
    }
  }

  public class Form1 extends Page20OntbindingForm1 {

    private Form1() {
      setBean(new Page20OntbindingBean1());
    }

    public FieldValue getPlaats() {

      GbaComboBox plaatsNl = getField(Page20OntbindingBean1.PLAATS_NL, GbaComboBox.class);
      GbaTextField plaatsBl = getField(Page20OntbindingBean1.PLAATS_BL, GbaTextField.class);

      if (plaatsNl.isVisible()) {
        return (FieldValue) plaatsNl.getValue();
      }

      return new FieldValue(astr(plaatsBl.getValue()));
    }
  }

  public class Form2 extends Page20OntbindingForm2 {

    private Form2() {
      setBean(new Page20OntbindingBean2());
    }
  }

  public class RelatiesLayout extends PageBsGerelateerdeLayout {

    private final DossierPersoon dossierPersoon;

    public RelatiesLayout(DossierPersoon dossierPersoon) {

      super(Page20Ontbinding.this.getApplication(), getDossier(), dossierPersoon, KIND);

      this.dossierPersoon = dossierPersoon;
    }

    @Override
    public void onDossierPersoon(DossierPersoon dossierPersoon) {
      getNavigation().goToPage(
          new PageBsGerelateerdePersoon(getZaakDossier().getDossier().getType(), dossierPersoon));
    }

    @Override
    public void onToevoegen(DossierPersoonType type) {

      DossierPersoon nieuwePersoon = dossierPersoon.toevoegenPersoon(type);
      getNavigation().goToPage(
          new PageBsGerelateerdePersoon(getZaakDossier().getDossier().getType(), nieuwePersoon));
    }

    @Override
    public void onVerwijderen(DossierPersoon persoon) {
      getServices().getDossierService().deletePersonen(getDossier(), asList(persoon));
    }
  }
}

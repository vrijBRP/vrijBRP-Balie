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

package nl.procura.gba.web.modules.bs.lv.page30;

import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;
import static nl.procura.gba.web.modules.bs.lv.page30.Page30LvBean1.SOORT;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.Date;
import java.util.Optional;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;

import nl.procura.burgerzaken.gba.StringUtils;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.functies.BsAkteUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.lv.LvType;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Data;

public class Page30Lv extends BsPage<DossierLv> {

  private final Button buttonResetData = new Button("Reset gegevens");
  private       Form1  form1           = null;
  private       Form2  form2           = null;

  public Page30Lv() {
    super("Latere vermelding - huidige situatie");
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    super.checkPage();

    form1.commit();
    form2.commit();

    getZaakDossier().setSoortLv(toBigDecimal(Optional.ofNullable(form1.getBean().getSoort())
        .map(LvType::getCode)
        .orElse(-1)));
    getZaakDossier().setDatumLv(form1.getBean().getDatum());

    getZaakDossier().setAkte(form2.getBean().getAkteNummer());
    getZaakDossier().setHuidigBrpAkte(form2.getBean().getHuidigBrpAkteNummer());
    getZaakDossier().setNieuweBrpAkte(form2.getBean().getNieuwBrpAkteNummer());
    getZaakDossier().setAkteGem(form2.getBean().getAktePlaats().getBigDecimalValue());
    getZaakDossier().setAkteJaar(toBigDecimal(form2.getBean().getAkteJaar()));

    getApplication().getServices().getLvService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {
      if (event.isEvent(InitPage.class)) {
        addComponent(new BsStatusForm(getDossier()));
        setInfo("Kies de soort latere vermelding en vul de gegevens in of aan. "
            + "Druk op Volgende (F2) om verder te gaan");

        form1 = new Form1();
        form2 = new Form2(getGeboortegegevens(getZaakDossier()));

        resetGegevens(false);

        form1.getField(SOORT).addListener((ValueChangeListener) lvEvent -> {
          LvType lvType = (LvType) lvEvent.getProperty().getValue();
          updateBean(true, form1.getBean(), form2.getBean(), getZaakDossier(), lvType);
          form2.setBean(form2.getBean());
          form2.repaint();
        });

        OptieLayout optieLayout = new OptieLayout();
        optieLayout.getLeft().addComponent(form1);
        optieLayout.getLeft().addComponent(form2);

        optieLayout.getRight().setWidth("170px");
        optieLayout.getRight().setCaption("Opties");
        optieLayout.getRight().addButton(buttonResetData, this);

        addComponent(optieLayout);
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonResetData) {
      resetGegevens(true);
      infoMessage("De soort- en geboorteaktegegevens zijn opnieuw geladen");
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

    Page30LvBean1 bean1 = new Page30LvBean1();
    Page30LvBean2 bean2 = new Page30LvBean2();

    DossierLv zaakDossier = getZaakDossier();
    bean1.setDatum(zaakDossier.getDatumLv());
    bean1.setSoort(zaakDossier.getSoort());

    if (bean1.getDatum() == null) {
      bean1.setDatum(new Date());
    }

    updateBean(force, bean1, bean2, zaakDossier, zaakDossier.getSoort());

    form1.setBean(bean1);
    form2.setBean(bean2);
  }

  private void updateBean(boolean force, Page30LvBean1 bean1, Page30LvBean2 bean2, DossierLv zaakDossier,
      LvType lvType) {

    bean2.setAkteNummer(zaakDossier.getAkte());
    bean2.setHuidigBrpAkteNummer(zaakDossier.getHuidigBrpAkte());

    if (lvType != LvType.ONBEKEND) {
      bean2.setNieuwBrpAkteNummer(zaakDossier.getNieuweBrpAkte());
    }

    bean2.setAktePlaats(PLAATS.get(zaakDossier.getAkteGem()));

    if (pos(zaakDossier.getAkteJaar())) {
      bean2.setAkteJaar(astr(zaakDossier.getAkteJaar().intValue()));
    }

    if (force || emp(bean2.getAkteNummer())) {
      GeboorteGegevens geboorteGegevens = getGeboortegegevens(zaakDossier);
      if (geboorteGegevens != null) {
        String brpAkteNr = geboorteGegevens.getBrpAkteNr();
        bean2.setAkteNummer(geboorteGegevens.getAktenr());
        bean2.setHuidigBrpAkteNummer(brpAkteNr);
        bean2.setAktePlaats(geboorteGegevens.getPlaats());
        bean2.setAkteJaar(geboorteGegevens.getJaar());

        if (lvType != LvType.ONBEKEND) {
          if (lvType == null || StringUtils.isBlank(lvType.getAkteCode())) {
            bean2.setNieuwBrpAkteNummer(brpAkteNr);
          } else {
            String brpAktenummer = BsAkteUtils.getBrpAktenummer(brpAkteNr, lvType.getAkteCode());
            bean2.setNieuwBrpAkteNummer(brpAktenummer);
          }
        }
      }
    }
  }

  public static class Form1 extends Page30LvForm1 {

    private Form1() {
      setBean(new Page30LvBean1());
    }
  }

  public static class Form2 extends Page30LvForm2 {

    private Form2(GeboorteGegevens geboorteGegevens) {
      super(geboorteGegevens);
      setBean(new Page30LvBean2());
    }

  }

  @Data
  public static class GeboorteGegevens {

    private FieldValue plaats;
    private String     jaar;
    private String     aktenr;
    private String     brpAkteNr;
  }

  private GeboorteGegevens getGeboortegegevens(DossierLv zaakDossier) {
    GeboorteGegevens geboorteGegevens = new GeboorteGegevens();
    DossierPersoon kind = zaakDossier.getKind();
    if (kind.isVolledig() && kind.isIngeschreven()) {
      BasePLExt persoon = getPersoonslijst(kind.getBurgerServiceNummer());
      BasePLRec partnerRecord = persoon.getCurrentRec(GBACat.PERSOON);
      String aktenummer = partnerRecord.getElemVal(GBAElem.AKTENR).getVal();
      geboorteGegevens.setAktenr(BsAkteUtils.getBsAktenummer(aktenummer));
      geboorteGegevens.setBrpAkteNr(aktenummer);
      BasePLValue geboorteplaats = partnerRecord.getElemVal(GBAElem.GEBOORTEPLAATS);
      geboorteGegevens.setPlaats(new FieldValue(geboorteplaats.getVal(), geboorteplaats.getDescr()));
      geboorteGegevens.setJaar(new ProcuraDate(partnerRecord.getElemVal(GBAElem.GEBOORTEDATUM).getVal()).getYear());
      return geboorteGegevens;
    }
    return null;
  }
}

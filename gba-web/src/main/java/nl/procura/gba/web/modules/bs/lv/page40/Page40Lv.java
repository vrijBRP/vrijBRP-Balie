/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.bs.lv.page40;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.BETREFT_OUDER;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.BETREFT_OUDER_PERSOON;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.DAG_VAN_WIJZIGING;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.DATUM_GEWIJSDE;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.DATUM_UITSPRAAK;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.DOCUMENT;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.ERKENNING_DOOR;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.FAMRECHT;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GEKOZEN_RECHT;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESLNM_GW;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESLNM_KIND_GW;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESLN_IS;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESLN_OUDER_GW;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESLN_OUDER_VG;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESLN_VA;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESL_AAND;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GEZAG;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.KEUZE_GESLACHTSNAAM;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.OUDERSCHAP_ONTKEND;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.OUDERSCHAP_VASTGESTELD;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.SOORT_VERBINTENIS;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.TOEGEPAST_RECHT;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.TOESTEMMING_DOOR;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.TWEEDE_DOC;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.UITSPRAAK;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.VERBETERD;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.VOORNAMEN_GW_IN;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.VOORNAMEN_VA;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.VOORN_OUDER;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.Date;

import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.enums.LvDocumentType;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.bs.lv.afstamming.KeuzeVaststellingGeslachtsnaam;
import nl.procura.gba.web.services.bs.lv.afstamming.LvDocumentDoorType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvField;
import nl.procura.gba.web.services.bs.lv.afstamming.LvGezagType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvOuderType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvSoortVerbintenisType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvToegepastRechtType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvToestemmingType;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page40Lv extends BsPage<DossierLv> {

  private Form1                form1 = null;
  private Form2                form2 = null;
  private Page40VerbeterLayout verbeterLayout;

  public Page40Lv() {
    super("Latere vermelding - brondocument");
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {
    super.checkPage();

    form1.commit();
    form2.commit();

    DossierLv zd = getZaakDossier();

    zd.setSoortVerbintenis("");
    zd.setDoc("");
    zd.setDocNr("");
    zd.setDocDatum(null);
    zd.setDocPlaats("");
    zd.setDocDoor(toBigDecimal(-1L));

    zd.setBetreftOuder("");
    zd.setBetreftOuderP(toBigDecimal(-1));
    zd.setGeslOuder("");
    zd.setVoornOuder("");
    zd.setKeuzeGesl("");

    zd.setGesl("");
    zd.setVoorn("");
    zd.setGeslAand("");
    zd.setGekozenRecht("");
    zd.setDatumWijziging(null);
    zd.setVerbeterd("");

    zd.setTweedeDoc(false);
    zd.setTweedeDocOms("");
    zd.setTweedeDocDatum(null);
    zd.setTweedeDocPlaats("");

    zd.setUitspraak("");
    zd.setDatumUitspraak(null);
    zd.setDatumGewijsde(null);
    zd.setToestemming("");
    zd.setToegepastRecht(toBigDecimal(-1L));

    // Form 1

    form1.ifLvFieldValue(UITSPRAAK,
        value -> zd.setUitspraak(value.is(RechtbankLocatie.ANDERS)
            ? form1.getBean().getUitspraakAnders()
            : value.getOms()),
        RechtbankLocatie.class);

    form1.ifLvFieldValue(DATUM_UITSPRAAK, zd::setDatumUitspraak, Date.class);
    form1.ifLvFieldValue(DATUM_GEWIJSDE, zd::setDatumGewijsde, Date.class);

    form1.ifLvFieldValue(SOORT_VERBINTENIS,
        value -> zd.setSoortVerbintenis(value.getCode()),
        LvSoortVerbintenisType.class);

    if (form1.isVisibleLvField(DOCUMENT)) {
      LvDocumentType document = form1.getBean().getDocument();
      if (document != null) {
        if (document == LvDocumentType.ANDERS) {
          zd.setDoc(form1.getBean().getDocumentAnders());
        } else {
          zd.setDoc(document.getOms());
        }
      }

      zd.setDocNr(form1.getBean().getNummer());
      zd.setDocDatum(form1.getBean().getDatum());
      zd.setDocPlaats(form1.getBean().getPlaats());
      zd.setDocDoor(toBigDecimal(ofNullable(form1.getBean().getDoor())
          .map(LvDocumentDoorType::getCode)
          .orElse(-1)));
    }

    if (form1.isVisibleLvField(TWEEDE_DOC)) {
      zd.setTweedeDoc(form1.getBean().getTweedeDocument());
      if (Boolean.TRUE.equals(zd.getTweedeDoc())) {
        zd.setTweedeDoc(form1.getBean().getTweedeDocument());
        LvDocumentType tweedeDocumentOms = form1.getBean().getTweedeDocumentOms();
        if (tweedeDocumentOms == LvDocumentType.ANDERS) {
          zd.setTweedeDocOms(form1.getBean().getTweedeDocumentOmsAnders());
        } else if (tweedeDocumentOms != null) {
          zd.setTweedeDocOms(tweedeDocumentOms.getOms());
        }

        zd.setTweedeDocDatum(form1.getBean().getTweedeDocumentDatum());
        zd.setTweedeDocPlaats(form1.getBean().getTweedeDocumentPlaats());
      }
    }

    // Form2

    form2.ifLvFieldValue(FAMRECHT, value -> zd.setBetreftOuder(value.getCode()), LvOuderType.class);
    form2.ifLvFieldValue(OUDERSCHAP_ONTKEND, value -> zd.setBetreftOuder(value.getCode()), LvOuderType.class);
    form2.ifLvFieldValue(OUDERSCHAP_VASTGESTELD, value -> zd.setBetreftOuder(value.getCode()), LvOuderType.class);
    form2.ifLvFieldValue(BETREFT_OUDER, value -> zd.setBetreftOuder(value.getCode()), LvOuderType.class);
    form2.ifLvFieldValue(ERKENNING_DOOR, value -> zd.setBetreftOuder(value.getCode()), LvOuderType.class);
    form2.ifLvFieldValue(BETREFT_OUDER_PERSOON, value -> zd.setBetreftOuderP(toBigDecimal(value.getCode())),
        KeuzeOuder.class);

    if (form2.isVisibleLvField(TOESTEMMING_DOOR)) {
      LvToestemmingType toestemmingDoor = form2.getBean().getToestemmingDoor();
      if (toestemmingDoor != null) {
        if (toestemmingDoor == LvToestemmingType.ANDERS) {
          zd.setToestemming(form2.getBean().getToestemmingAnders());
        } else {
          zd.setToestemming(toestemmingDoor.getOms());
        }
      }
    }

    form2.ifLvFieldValue(GESLN_OUDER_VG, zd::setGeslOuder);
    form2.ifLvFieldValue(GESLN_OUDER_GW, zd::setGeslOuder);
    form2.ifLvFieldValue(VOORN_OUDER, zd::setVoornOuder);

    form2.ifLvFieldValue(KEUZE_GESLACHTSNAAM,
        value -> zd.setKeuzeGesl(value.getCode()),
        KeuzeVaststellingGeslachtsnaam.class);

    form2.ifLvFieldValue(GESLN_IS, zd::setGesl);
    form2.ifLvFieldValue(GESLN_VA, zd::setGesl);
    form2.ifLvFieldValue(GESLNM_GW, zd::setGesl);
    form2.ifLvFieldValue(GESLNM_KIND_GW, zd::setGesl);

    form2.ifLvFieldValue(VOORNAMEN_VA, zd::setVoorn);
    form2.ifLvFieldValue(VOORNAMEN_GW_IN, zd::setVoorn);

    if (form2.isVisibleLvField(TOEGEPAST_RECHT)) {
      LvToegepastRechtType toegepastRecht = form2.getBean().getToegepastRecht();
      if (toegepastRecht != null) {
        if (toegepastRecht == LvToegepastRechtType.ANDERS) {
          zd.setToegepastRecht(toBigDecimal(form2.getBean().getToegepastRechtAnders().getValue()));
        } else {
          zd.setToegepastRecht(toBigDecimal(toegepastRecht.getCode()));
        }
      }
    }

    form2.ifLvFieldValue(GEZAG, value -> zd.setGezag(value.getCode()), LvGezagType.class);
    form2.ifLvFieldValue(GESL_AAND, value -> zd.setGeslAand(value.getAfkorting()), Geslacht.class);
    form2.ifLvFieldValue(GEKOZEN_RECHT, zd::setGekozenRecht);
    form2.ifLvFieldValue(DAG_VAN_WIJZIGING, zd::setDatumWijziging, Date.class);
    form2.ifLvFieldValue(VERBETERD, zd::setVerbeterd);

    if (verbeterLayout != null) {
      if (!verbeterLayout.validate()) {
        infoMessage("Vul alle velden in");
        return false;
      }
      zd.setVerbeteringen(verbeterLayout.getVerbeteringen());
    }

    getApplication().getServices().getLvService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {
      if (event.isEvent(InitPage.class)) {
        addComponent(new BsStatusForm(getDossier()));
        setInfo("Vul de gegevens in of aan. Druk op Volgende (F2) om verder te gaan");

        form1 = new Form1();
        form2 = new Form2();

        if (form1.getOrder().length > 0) {
          addComponent(form1);
        }

        if (isVerbeteringen()) {
          verbeterLayout = new Page40VerbeterLayout(getZaakDossier().getVerbeteringen());
          addComponent(new Fieldset("Verbeteringen", verbeterLayout));

        } else if (form2.getOrder().length > 0) {
          addComponent(form2);
        }
      }
    } finally {
      super.event(event);
    }
  }

  private boolean isVerbeteringen() {
    return LvField.toMapping(getZaakDossier().getSoort()).getFields().contains(VERBETERD);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  public class Form1 extends Page40LvForm1 {

    private Form1() {
      setBean(getZaakDossier());
    }
  }

  public class Form2 extends Page40LvForm2 {

    private Form2() {
      setBean(getZaakDossier());
    }
  }
}

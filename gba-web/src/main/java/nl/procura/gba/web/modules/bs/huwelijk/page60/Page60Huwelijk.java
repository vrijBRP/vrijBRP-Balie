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

package nl.procura.gba.web.modules.bs.huwelijk.page60;

import static nl.procura.gba.web.modules.bs.huwelijk.page60.Page60HuwelijkBean1.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.containers.NaamgebruikContainer;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page60Huwelijk extends BsPage<DossierHuwelijk> {

  private Page60HuwelijkForm1 form1  = null;
  private Page60HuwelijkTable table1 = null;

  public Page60Huwelijk() {

    super("Huwelijk/GPS - naam(gebruik)");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    super.checkPage();

    form1.commit();

    DossierHuwelijk zd = getZaakDossier();
    DossierPersoon p1 = zd.getPartner1();
    DossierPersoon p2 = zd.getPartner2();

    getZaakDossier().setTitelPartner1(get(form1.getBean().getTitelP1(), p1.getTitel()));
    getZaakDossier().setTitelPartner2(get(form1.getBean().getTitelP2(), p2.getTitel()));

    getZaakDossier().setVoorvPartner1(astr(form1.getBean().getVoorvP1()));
    getZaakDossier().setVoorvPartner2(astr(form1.getBean().getVoorvP2()));

    getZaakDossier().setNaamPartner1(form1.getBean().getNaamP1());
    getZaakDossier().setNaamPartner2(form1.getBean().getNaamP2());

    int i = 0;
    for (HuwelijkNaamgebruik ng : table1.getAllValues(HuwelijkNaamgebruik.class)) {

      i++;

      if (i == 1) {
        getZaakDossier().setNaamGebruikPartner1(ng.getNieuweWaarde());
      } else {
        getZaakDossier().setNaamGebruikPartner2(ng.getNieuweWaarde());
      }
    }

    getApplication().getServices().getHuwelijkService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        addComponent(new BsStatusForm(getDossier()));

        setInfo(
            "Bepaal of de namen van de partner wijzigen als gevolg van de verbintenis en/of leg vast wat het gewenste naamgebruik moet gaan worden na de sluiting. "
                + "<br/>Druk indien nodig de verzoeken af voor ondertekening. Druk op Volgende (F2) om verder te gaan.");

        DossierHuwelijk zd = getZaakDossier();
        DossierPersoon p1 = zd.getPartner1();
        DossierPersoon p2 = zd.getPartner2();

        Page60HuwelijkBean1 bean = new Page60HuwelijkBean1(p1, p2);

        bean.setTitelP1(get(zd.getTitelPartner1(), p1.getTitel()));
        bean.setTitelP2(get(zd.getTitelPartner2(), p2.getTitel()));

        bean.setVoorvP1(new FieldValue(get(zd.getVoorvPartner1(), p1.getNaam().getVoorvoegsel())));
        bean.setVoorvP2(new FieldValue(get(zd.getVoorvPartner2(), p2.getNaam().getVoorvoegsel())));

        bean.setNaamP1(get(zd.getNaamPartner1(), p1.getNaam().getGeslachtsnaam()));
        bean.setNaamP2(get(zd.getNaamPartner2(), p2.getNaam().getGeslachtsnaam()));

        form1 = new Form1(bean);

        table1 = new Table1();

        addComponent(form1);
        addComponent(new Fieldset("Naamgebruik na sluiting verbintenis", table1));
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  private FieldValue get(FieldValue value1, FieldValue value2) {
    return FieldValue.from(value1 != null && fil(value1.getStringValue()) ? value1 : value2);
  }

  private String get(String value1, String value2) {
    return fil(value1) ? value1 : value2;
  }

  public class Form1 extends Page60HuwelijkForm1 {

    private Form1(Page60HuwelijkBean1 bean) {

      super(bean);
    }

    @Override
    public void init() {

      setCaption("Naam na sluiting verbintenis");

      setColumnWidths("180px", "");

      setOrder(NATIOP1, NATIOP2, TITELP1, VOORVP1, NAAMP1, TITELP2, VOORVP2, NAAMP2);
    }

    @Override
    public void setColumn(Column column, Field field, Property property) {

      if (property.is(NAAMP1, NAAMP2, VOORVP1, VOORVP2)) {
        column.setAppend(true);
      }

      super.setColumn(column, field, property);
    }
  }

  public class Table1 extends Page60HuwelijkTable {

    @Override
    public void set() {

      NaamgebruikContainer c = new NaamgebruikContainer();

      String huidig1 = getZaakDossier().getPartner1().getNaam().getNaamgebruik();
      String huidig2 = getZaakDossier().getPartner2().getNaam().getNaamgebruik();

      String nieuw1 = getZaakDossier().getNaamGebruikPartner1();
      String nieuw2 = getZaakDossier().getNaamGebruikPartner2();

      HuwelijkNaamgebruik n1 = new HuwelijkNaamgebruik();
      n1.setNaam(getZaakDossier().getPartner1().getNaam().getPred_adel_voorv_gesl_voorn());
      n1.setNaamgebruikHuidig(c.get(huidig1));
      n1.setNaamgebruikNieuw(c.get(fil(nieuw1) ? nieuw1 : huidig1));

      HuwelijkNaamgebruik n2 = new HuwelijkNaamgebruik();
      n2.setNaam(getZaakDossier().getPartner2().getNaam().getPred_adel_voorv_gesl_voorn());
      n2.setNaamgebruikHuidig(c.get(huidig2));
      n2.setNaamgebruikNieuw(c.get(fil(nieuw2) ? nieuw2 : huidig2));

      add(n1);
      add(n2);
    }
  }
}

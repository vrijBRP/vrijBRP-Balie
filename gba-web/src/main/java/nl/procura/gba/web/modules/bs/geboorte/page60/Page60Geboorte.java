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

package nl.procura.gba.web.modules.bs.geboorte.page60;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.*;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.erkenning.page20.Page20ErkenningForm1;
import nl.procura.gba.web.modules.bs.erkenning.page20.Page20ErkenningForm2;
import nl.procura.gba.web.modules.bs.geboorte.BsPageGeboorte;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */

public class Page60Geboorte<T extends DossierGeboorte> extends BsPageGeboorte<T> {

  private static final int STAP_0 = 0;
  private static final int STAP_1 = 1;
  private static final int STAP_2 = 2;
  private static final int STAP_3 = 3;

  private Page60GeboorteGezin      formGezin  = null;
  private Page60GeboorteFormStap1a form1a     = null;
  private Page60GeboorteFormStap1b form1b     = null;
  private Page60GeboorteFormStap2  form2a     = null;
  private Page60GeboorteFormStap2  form2b     = null;
  private Page60GeboorteStap3      form3      = null;
  private Page60GeboorteForm5      form5      = null;
  private Page60GeboorteForm4      form4      = null;
  private InfoLayout               rechtTekst = new InfoLayout();

  public Page60Geboorte() {
    this("Geboorte - afstamming");
  }

  public Page60Geboorte(String caption) {

    super(caption);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    DossierGeboorte dossier = getZaakDossier();

    dossier.setVerblijfsLandAfstamming(new FieldValue());

    if (isBinnenHuwelijk()) {

      form1a.commit();
      form2a.commit();
      form3.commit();

      if (getComponentIndex(form3) > STAP_0) {
        dossier.setVerblijfsLandAfstamming(form3.getBean().getVerblijfsLand());
      }

      dossier.setAfstammingStap(toBigDecimal(0));

      if (form2a.hasParent()) {
        dossier.setAfstammingStap(toBigDecimal(form2a.getBean().getStap2() ? STAP_2 : STAP_3));
      } else if (form1a.hasParent() && form1a.getBean().getStap1()) {
        dossier.setAfstammingStap(toBigDecimal(STAP_1));
      }
    }

    form5.commit();

    dossier.setLandAfstammingsRecht(form5.getBean().getRecht());

    getApplication().getServices().getGeboorteService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(new BsStatusForm(getDossier()));

      String asielMsg = "<br/><span style='color:red'>Let op!</span> Op staatlozen, vluchtelingen en vreemdelingen met een vergunning asiel (on)bepaalde tijd (code 26 of 27) is Nederlands recht van toepassing.";

      if (isBinnenHuwelijk()) {
        setInfo(
            "Doorloop de stappen en vul in van welk land het recht moet worden toegepast. Druk op Volgende (F2) om verder te gaan. "
                + asielMsg);
      } else {
        setInfo("Bepaal van welk land het recht moet worden toegepast. Druk op Volgende (F2) om verder te gaan. "
            + asielMsg);
      }

      initFormulieren();
      controleFormulieren();
    }

    super.event(event);
  }

  /**
   * De container plus verblijfplaats land
   */
  public Page60RechtContainer getRechtContainer(FieldValue land) {
    return new Page60RechtContainer(getServices(), getZaakDossier(), land);
  }

  @Override
  public void onNextPage() {

    goToNextProces();

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {

    goToPreviousProces();

    super.onPreviousPage();
  }

  private void addListener(Field field) {

    field.addListener(new FieldChangeListener() {

      @Override
      public void onChange(Object value) {
        controleFormulieren();
      }
    });
  }

  private void controleFormulieren() {

    if (isBinnenHuwelijk()) {

      removeComponent(form2a); // Stap 2
      removeComponent(form2b); // Stap 2 b
      removeComponent(form3); // Stap 3

      Object stap1 = form1a.getValue(Page60GeboorteFormStap1a.STAP1);
      Object stap2 = form2a.getValue(Page60GeboorteFormStap2.STAP2);

      boolean isStap1 = stap1 == null || ((Boolean) stap1);
      boolean isStap2 = stap2 == null || ((Boolean) stap2);

      if (!isStap1) {

        addComponent(form2a, getComponentIndex(form1b) + STAP_1);
        addComponent(form2b, getComponentIndex(form2a) + STAP_1);
        addListener(form2a.getField(Page60GeboorteFormStap2.STAP2));
      }

      if (!isStap1 && !isStap2) {
        addComponent(form3, getComponentIndex(form2b) + STAP_1);
      }

      controleRechtBinnen();
    } else {
      controleRechtBuiten();
    }
  }

  private void controleRechtBinnen() {

    Object stap1 = form1a.getField(Page20ErkenningForm1.STAP1).getValue();
    Object stap2 = form2a.getField(Page20ErkenningForm2.STAP2).getValue();

    boolean isStap1Ja = form1a.hasParent() && stap1 != null && stap1 == Boolean.TRUE;
    boolean isStap2Ja = form2a.hasParent() && stap2 != null && stap2 == Boolean.TRUE;

    boolean isStap1Nee = form1a.hasParent() && stap1 != null && stap1 == Boolean.FALSE;
    boolean isStap2Nee = form2a.hasParent() && stap2 != null && stap2 == Boolean.FALSE;

    DossierGeboorte impl = to(getZaakDossier(), DossierGeboorte.class);
    setRecht(null, "");

    if (isStap2Nee) {

      if (isWoonachtigInNederland(impl.getMoeder())) {

        FieldValue land = form3.getBean().getVerblijfsLand();

        if (land == null || !pos(land.getValue())) {

          form3.getBean().setVerblijfsLand(Landelijk.getNederland());
        } else {

          form3.setVerblijfsLand(land);
        }

        setRecht(Landelijk.getNederland(), "De moeder is woonachtig in Nederland");
      }

      form3.onWijzigingVerblijfsland(form3.getVerblijfsLand());
    } else if (isStap2Ja) {

      form3.setVerblijfsLand(new FieldValue());

      if (heeftGedeeldLand(impl)) {

        setRecht(getGedeeldLand(impl), "De ouders hebben een gedeelde verblijfplaats");
      }

      form3.onWijzigingVerblijfsland(form3.getVerblijfsLand());
    } else if (isStap1Nee) {

      if (heeftGedeeldLand(impl)) {
        form2a.getField(Page20ErkenningForm2.STAP2).setValue(true);
      }
    } else if (isStap1Ja) {

      if (heeftGedeeldeNationaliteit(impl)) {
        FieldValue land = getServices().getKennisbankService().getLand(getGedeeldeNationaliteit(impl));
        setRecht(land, "De ouders hebben een gedeelde nationaliteit");
      }
    } else {
      if (!impl.isAfstammingsRechtBepaald()) {

        if (heeftGedeeldeNationaliteit(impl)) {

          FieldValue land = getServices().getKennisbankService().getLand(getGedeeldeNationaliteit(impl));
          setRecht(land, "De ouders hebben een gedeelde nationaliteit");
          form1a.getField(Page60GeboorteFormStap1a.STAP1).setValue(true);
        } else {

          form1a.getField(Page60GeboorteFormStap1a.STAP1).setValue(false);

          if (heeftGedeeldLand(impl)) {

            setRecht(getGedeeldLand(impl), "De ouders hebben een gedeelde verblijfplaats");
            form2a.getField(Page60GeboorteFormStap2.STAP2).setValue(true);
          } else {
            form2a.getField(Page60GeboorteFormStap2.STAP2).setValue(true);
          }
        }
      }
    }
  }

  private void controleRechtBuiten() {

    DossierGeboorte impl = to(getZaakDossier(), DossierGeboorte.class);

    if (!impl.isAfstammingsRechtBepaald()) {

      if (heeftNederlandseNationaliteit(getZaakDossier().getMoeder())) {

        setRecht(Landelijk.getNederland(), "De moeder heeft de Nederlandse nationaliteit");
      }
    }
  }

  private void initFormulieren() {

    // Toe te passen recht
    form5 = new Page60GeboorteForm5() {

      @Override
      public void setCaptionAndOrder() {

        Page60GeboorteBean5 bean = new Page60GeboorteBean5();

        DossierGeboorte impl = getZaakDossier();

        if (impl.isAfstammingsRechtBepaald()) {
          bean.setRecht(getZaakDossier().getLandAfstammingsRecht());
        }

        setBean(bean);

        updateContainer(new FieldValue());
      }

      @Override
      protected com.vaadin.data.Container getContainer(FieldValue land) {
        return getRechtContainer(land);
      }
    };

    if (isBinnenHuwelijk()) {

      formGezin = new Page60GeboorteGezin() {

        @Override
        public void setCaptionAndOrder() {
          setBean(new Page60GeboorteBean1(getZaakDossier().getGezinssituatie()));
        }
      };

      // Stap 1
      form1a = new Page60GeboorteFormStap1a() {

        @Override
        public void setCaptionAndOrder() {

          setCaption("Stap 1");

          setColumnWidths("692px", "");

          setOrder(STAP1);

          Page60GeboorteBean2 b = new Page60GeboorteBean2();

          DossierGeboorte impl = getZaakDossier();

          if (impl.isAfstammingsRechtBepaald() && aval(impl.getAfstammingStap()) >= STAP_1) {
            b.setStap1(aval(impl.getAfstammingStap()) == STAP_1);
          }

          setBean(b);
        }
      };

      // Nationaliteiten en verblijfstitelcodes
      form1b = new Page60GeboorteFormStap1b(getZaakDossier());

      // Stap 2
      form2a = new Page60GeboorteFormStap2() {

        @Override
        public void setCaptionAndOrder() {

          setCaption("Stap 2");

          setColumnWidths("692px", "");

          setOrder(STAP2);

          Page60GeboorteBean3 bean = new Page60GeboorteBean3();

          DossierGeboorte impl = getZaakDossier();

          if (impl.isAfstammingsRechtBepaald() && aval(impl.getAfstammingStap()) >= STAP_2) {
            bean.setStap2(aval(impl.getAfstammingStap()) == STAP_2);
          }

          setBean(bean);
        }
      };

      // Verblijfstitelcodes
      form2b = new Page60GeboorteFormStap2(getZaakDossier()) {

        @Override
        public void setCaptionAndOrder() {

          setColumnWidths("200px", "");

          setOrder(VB1, VB2);
        }
      };

      // Verblijfplaats
      form3 = new Page60GeboorteStap3(getZaakDossier()) {

        @Override
        protected void onWijzigingVerblijfsland(FieldValue land) {

          form5.updateContainer(land);

          form5.setRecht(land);
        }
      };

      addComponent(formGezin);
      addComponent(form1a); // Stap 1
      addComponent(form1b); // Stap 1
      addComponent(new Fieldset("Conclusie Afstammingsrecht"));
      addComponent(rechtTekst);
      addComponent(form5);

      addListener(form1a.getField(Page60GeboorteFormStap1a.STAP1));
      addListener(form2a.getField(Page60GeboorteFormStap2.STAP2));
    } else {

      // Buitenhuwelijk

      form4 = new Page60GeboorteForm4(getZaakDossier());

      addComponent(form4);
      addComponent(new Fieldset("Conclusie Afstammingsrecht"));
      addComponent(rechtTekst);
      addComponent(form5);
    }
  }

  private boolean isBinnenHuwelijk() {
    return getZaakDossier().getGezinssituatie() == GezinssituatieType.BINNEN_HETERO_HUWELIJK;
  }

  private void setRecht(FieldValue land, String reden) {

    DossierGeboorte impl = getZaakDossier();

    String newReden = "Leidt zelf het recht af";

    if (land != null) {
      if (fil(reden) && !impl.isAfstammingsRechtBepaald()) {
        form5.getField(Page60GeboorteForm5.RECHT).setValue(land);
      }

      if (fil(reden)) {
        newReden = reden + ". Hierdoor is het recht van " + land + " van toepassing.";
      }
    }

    setRechtToelichting(newReden);
  }

  private void setRechtToelichting(String oms) {
    InfoLayout nieuwRechtTekst = new InfoLayout("", oms);
    replaceComponent(rechtTekst, nieuwRechtTekst);
    rechtTekst = nieuwRechtTekst;
  }
}

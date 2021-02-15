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

package nl.procura.gba.web.modules.bs.erkenning.page20;

import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.heeftAsiel;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.heeftNederlandseNationaliteit;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.BeanHandler;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.erkenning.BsPageErkenning;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Erkenning
 */

public class Page20Erkenning extends BsPageErkenning {

  private static final int STAP_1 = 1;
  private static final int STAP_2 = 2;
  private static final int STAP_3 = 3;
  private static final int STAP_4 = 4;
  private static final int STAP_5 = 5;

  private Page20ErkenningForm1  form1a     = null;
  private Page20ErkenningForm1  form1b     = null;
  private Page20ErkenningForm2  form2a     = null;
  private Page20ErkenningForm2  form2b     = null;
  private Page20ErkenningForm3  form3a     = null;
  private Page20ErkenningForm3  form3b     = null;
  private Page20ErkenningForm4  form4a     = null;
  private Page20ErkenningForm4  form4b     = null;
  private Page20ErkenningForm10 form10     = null;
  private InfoLayout            rechtTekst = new InfoLayout();

  public Page20Erkenning() {

    super("Erkenning - afstammingsrecht");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    form1a.commit();
    form1b.commit();
    form2a.commit();
    form2b.commit();
    form3a.commit();
    form3b.commit();
    form4a.commit();
    form4b.commit();
    form10.commit();

    DossierErkenning dossier = getZaakDossier();

    dossier.setLandAfstammingsRecht(form10.getBean().getRecht());

    // Sla stappen op

    dossier.setAfstammingStap(toBigDecimal(0));

    if (form4a.hasParent()) {
      dossier.setAfstammingStap(toBigDecimal(form4a.getBean().getStap4() ? STAP_4 : STAP_5));
    } else if (form3a.hasParent() && form3a.getBean().getStap3()) {
      dossier.setAfstammingStap(toBigDecimal(STAP_3));
    } else if (form2a.hasParent() && form2a.getBean().getStap2()) {
      dossier.setAfstammingStap(toBigDecimal(STAP_2));
    } else if (form1a.hasParent() && form1a.getBean().getStap1()) {
      dossier.setAfstammingStap(toBigDecimal(STAP_1));
    }

    getServices().getErkenningService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(new BsStatusForm(getZaakDossier().getDossier()));

      setInfo("Doorloop de stappen en bepaal het toe te passen recht op de erkenning en de toestemming. "
          + "Druk op Volgende (F2) om verder te gaan. ");

      initFormulieren();

      addListener(form1a, form1a.getField(Page20ErkenningForm1.STAP1));
      addListener(form2a, form2a.getField(Page20ErkenningForm2.STAP2));
      addListener(form3a, form3a.getField(Page20ErkenningForm3.STAP3));
      addListener(form4a, form4a.getField(Page20ErkenningForm4.STAP4));

      controleFormulieren();
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  private void addListener(final Form form, final Field field) {

    field.addListener(new FieldChangeListener() {

      @Override
      public void onChange(Object event) {
        controleFormulieren();
      }
    });
  }

  private void controleFormulieren() {

    removeComponent(form2a); // Stap 2
    removeComponent(form2b); // Stap 2 b
    removeComponent(form3a); // Stap 3
    removeComponent(form3b); // Stap 3 b
    removeComponent(form4a); // Stap 4
    removeComponent(form4b); // Stap 4 b

    Object stap1 = form1a.getValue(Page20ErkenningForm1.STAP1);
    Object stap2 = form2a.getValue(Page20ErkenningForm2.STAP2);
    Object stap3 = form3a.getValue(Page20ErkenningForm3.STAP3);

    boolean isStap1 = stap1 == null || ((Boolean) stap1);
    boolean isStap2 = stap2 == null || ((Boolean) stap2);
    boolean isStap3 = stap3 == null || ((Boolean) stap3);

    if (!isStap1) {

      addComponent(form2a, getComponentIndex(form1b) + STAP_1);
      addComponent(form2b, getComponentIndex(form2a) + STAP_1);

      if (!isStap2) {

        addComponent(form3a, getComponentIndex(form2b) + STAP_1);
        addComponent(form3b, getComponentIndex(form3a) + STAP_1);

        if (!isStap3) {

          addComponent(form4a, getComponentIndex(form3b) + STAP_1);
          addComponent(form4b, getComponentIndex(form4a) + STAP_1);
        }
      }
    }

    controleRecht();
  }

  private void controleRecht() {

    Object stap1 = form1a.getField(Page20ErkenningForm1.STAP1).getValue();
    Object stap2 = form2a.getField(Page20ErkenningForm2.STAP2).getValue();
    Object stap3 = form3a.getField(Page20ErkenningForm3.STAP3).getValue();
    Object stap4 = form4a.getField(Page20ErkenningForm4.STAP4).getValue();

    boolean isStap1Ja = form1a.hasParent() && stap1 != null && stap1 == Boolean.TRUE;
    boolean isStap2Ja = form2a.hasParent() && stap2 != null && stap2 == Boolean.TRUE;
    boolean isStap3Ja = form3a.hasParent() && stap3 != null && stap3 == Boolean.TRUE;
    boolean isStap4Ja = form4a.hasParent() && stap4 != null && stap4 == Boolean.TRUE;

    boolean isStap1Nee = form1a.hasParent() && stap1 != null && stap1 == Boolean.FALSE;
    boolean isStap2Nee = form2a.hasParent() && stap2 != null && stap2 == Boolean.FALSE;
    boolean isStap3Nee = form3a.hasParent() && stap3 != null && stap3 == Boolean.FALSE;
    boolean isStap4Nee = form4a.hasParent() && stap4 != null && stap4 == Boolean.FALSE;

    DossierErkenning impl = getZaakDossier();
    setNederlandsRecht("");

    if (isStap4Nee) {
      setNederlandsRecht(""); // Recht niet af te leiden
    } else if (isStap4Ja) {
      setNederlandsRecht("De erkenner is woonachtig in Nederland");
    } else if (isStap3Nee) {
      if (Landelijk.isNederland(impl.getErkenner().getLand())) {
        form4a.getField(Page20ErkenningForm4.STAP4).setValue(true);
      }
    } else if (isStap3Ja) {
      setNederlandsRecht("De moeder heeft de Nederlandse nationaliteit");
    } else if (isStap2Nee) {
      if (heeftNederlandseNationaliteit(impl.getMoeder())) {
        form3a.getField(Page20ErkenningForm3.STAP3).setValue(true);
      }
    } else if (isStap2Ja) {
      if (impl.isOngeborenVrucht()) {
        setNederlandsRecht("De moeder is woonachtig in Nederland");
      } else {
        setNederlandsRecht("Het kind is woonachtig in Nederland");
      }
    } else if (isStap1Nee) {
      if (Landelijk.isNederland(impl.getMoeder().getLand())) {
        form2a.getField(Page20ErkenningForm2.STAP2).setValue(true);
      }
    } else if (isStap1Ja) {

      if (heeftNederlandseNationaliteit(impl.getErkenner())) {
        setNederlandsRecht("De erkenner heeft de Nederlandse nationaliteit");
      } else if (heeftAsiel(impl.getErkenner())) {
        setNederlandsRecht("De erkenner heeft verblijfsvergunning asiel");
      }
    } else {
      if (!impl.isAfstammingsRechtBepaald()) {

        if (heeftNederlandseNationaliteit(impl.getErkenner())) {

          form1a.getField(Page20ErkenningForm1.STAP1).setValue(true);
          setNederlandsRecht("De erkenner heeft de Nederlandse nationaliteit");
        } else if (heeftAsiel(impl.getErkenner())) {

          form1a.getField(Page20ErkenningForm1.STAP1).setValue(true);
          setNederlandsRecht("De erkenner heeft verblijfsvergunning asiel");
        }
      }
    }
  }

  private void initFormulieren() {

    // Afstammingsrecht

    form1a = new Page20ErkenningForm1() {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Afstammingsrecht: erkenning stap 1");

        setColumnWidths("", "300px");

        setOrder(STAP1);

        Page20ErkenningBean1 bean = new Page20ErkenningBean1();

        DossierErkenning impl = getZaakDossier();

        if (impl.isAfstammingsRechtBepaald() && aval(impl.getAfstammingStap()) >= STAP_1) {

          bean.setStap1(aval(impl.getAfstammingStap()) == STAP_1);
        }

        setBean(bean);
      }
    };

    form1b = new Page20ErkenningForm1() {

      @Override
      public void setCaptionAndOrder() {

        setColumnWidths("140px", "", "100px", "300px");

        setOrder(NAT1, VBT1);

        DossierErkenning dossier = getZaakDossier();

        Page20ErkenningBean1 bean = new Page20ErkenningBean1();

        bean.setNat1(dossier.getErkenner().getNationaliteitenOmschrijving());

        BeanHandler.trim(bean);

        setBean(bean);
      }

      @Override
      protected List<DossierNationaliteit> getNationaliteiten() {

        return getZaakDossier().getErkenner().getNationaliteiten();
      }
    };

    form2a = new Page20ErkenningForm2() {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Stap 2");

        setColumnWidths("", "300px");

        setOrder(STAP2);

        Page20ErkenningBean2 bean = new Page20ErkenningBean2();

        DossierErkenning impl = getZaakDossier();

        if (impl.isAfstammingsRechtBepaald() && aval(impl.getAfstammingStap()) >= STAP_2) {

          bean.setStap2(aval(impl.getAfstammingStap()) == STAP_2);
        }

        setBean(bean);
      }
    };

    form2b = new Page20ErkenningForm2() {

      @Override
      public void setCaptionAndOrder() {

        setColumnWidths("140px", "");

        setOrder(VBT_MOEDER, VBT_KIND);

        Page20ErkenningBean2 bean = new Page20ErkenningBean2();

        bean.setVbtMoeder(getZaakDossier().getMoeder().getLand().getDescription());

        bean.setVbtKind("Geen");

        if (getZaakDossier().isBestaandKind()) {

          bean.setVbtKind(getZaakDossier().getKinderen().get(0).getLand().getDescription());
        }

        BeanHandler.trim(bean);

        setBean(bean);
      }

      @Override
      protected FieldValue getVerblijfplaats() {
        return getZaakDossier().getMoeder().getLand();
      }
    };

    form3a = new Page20ErkenningForm3() {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Stap 3");

        setColumnWidths("", "300px");

        setOrder(STAP3);

        Page20ErkenningBean3 bean = new Page20ErkenningBean3();

        DossierErkenning impl = getZaakDossier();

        if (impl.isAfstammingsRechtBepaald() && aval(impl.getAfstammingStap()) >= STAP_3) {

          bean.setStap3(aval(impl.getAfstammingStap()) == STAP_3);
        }

        setBean(bean);
      }
    };

    form3b = new Page20ErkenningForm3() {

      @Override
      public void setCaptionAndOrder() {

        setColumnWidths("140px", "");

        setOrder(NAT_MOEDER, NAT_KIND);

        Page20ErkenningBean3 bean = new Page20ErkenningBean3();

        List<DossierPersoon> kinderen = getZaakDossier().getKinderen();

        bean.setNatMoeder(getZaakDossier().getMoeder().getNationaliteitenOmschrijving());

        bean.setNatKind(kinderen.size() > 0 ? kinderen.get(0).getNationaliteitenOmschrijving() : "Geen");

        BeanHandler.trim(bean);

        setBean(bean);
      }

      @Override
      protected FieldValue getNationaliteitKind() {
        List<DossierPersoon> kinderen = getZaakDossier().getKinderen();
        return kinderen.size() > 0 ? kinderen.get(0).getNationaliteit().getNationaliteit() : new FieldValue();
      }

      @Override
      protected FieldValue getNationaliteitMoeder() {
        return getZaakDossier().getMoeder().getNationaliteit().getNationaliteit();
      }
    };

    form4a = new Page20ErkenningForm4() {

      @Override
      public void setCaptionAndOrder() {

        setCaption("Stap 4");

        setColumnWidths("", "300px");

        setOrder(STAP4);

        Page20ErkenningBean4 bean = new Page20ErkenningBean4();

        DossierErkenning impl = getZaakDossier();

        if (impl.isAfstammingsRechtBepaald() && aval(impl.getAfstammingStap()) >= STAP_4) {

          bean.setStap4(aval(impl.getAfstammingStap()) == STAP_4);
        }

        setBean(bean);
      }
    };

    form4b = new Page20ErkenningForm4() {

      @Override
      public void setCaptionAndOrder() {

        setColumnWidths("140px", "", "100px", "300px");

        setOrder(VBT1);

        Page20ErkenningBean4 bean = new Page20ErkenningBean4();

        bean.setVbt1(getZaakDossier().getMoeder().getLand().getDescription());

        setBean(bean);
      }
    };

    form10 = new Page20ErkenningForm10() {

      @Override
      public void setCaptionAndOrder() {

        Page20ErkenningBean10 bean = new Page20ErkenningBean10();

        DossierErkenning impl = getZaakDossier();

        if (impl.isAfstammingsRechtBepaald()) {

          bean.setRecht(getZaakDossier().getLandAfstammingsRecht());
        }

        setBean(bean);
      }

      @Override
      protected Container getContainer() {
        return new Page20RechtContainer(getServices(), getZaakDossier());
      }
    };

    addListener(form2a, form2a.getField(Page20ErkenningForm2.STAP2));
    addListener(form3a, form3a.getField(Page20ErkenningForm3.STAP3));
    addListener(form4a, form4a.getField(Page20ErkenningForm4.STAP4));

    addComponent(form1a);
    addComponent(form1b);
    addComponent(new Fieldset("Conclusie Afstammingsrecht"));
    addComponent(rechtTekst);
    addComponent(form10);
  }

  private void setNederlandsRecht(String reden) {

    DossierErkenning impl = getZaakDossier();

    if (fil(reden) && !impl.isAfstammingsRechtBepaald()) {
      form10.getField(Page20ErkenningForm10.RECHT).setValue(Landelijk.getNederland());
    }

    String newReden = "Leidt zelf het recht af";

    if (fil(reden)) {
      newReden = reden + ". Hierdoor is het recht van Nederland van toepassing.";
    }

    setRechtToelichting(newReden);
  }

  private void setRechtToelichting(String oms) {
    InfoLayout nieuwRechtTekst = new InfoLayout("", oms);
    replaceComponent(rechtTekst, nieuwRechtTekst);
    rechtTekst = nieuwRechtTekst;
  }
}

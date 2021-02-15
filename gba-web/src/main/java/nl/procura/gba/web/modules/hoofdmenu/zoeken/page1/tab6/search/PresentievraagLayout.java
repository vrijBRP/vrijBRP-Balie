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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search;

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search.PresentievraagZoekBean.*;

import com.vaadin.ui.VerticalLayout;

import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;

public class PresentievraagLayout extends VerticalLayout {

  private final VraagForm        vraagForm = new VraagForm();
  private PresentievraagZoekBean zoekBean;
  private PresentievraagZoekForm zoekForm;

  public PresentievraagLayout() {
    this(null);
  }

  public PresentievraagLayout(PresentievraagZoekBean newZoekBean) {

    this.zoekBean = (newZoekBean == null) ? new PresentievraagZoekBean() : newZoekBean;
    zoekForm = new PresentievraagZoekForm(zoekBean);

    setSizeFull();
    addComponent(vraagForm);
    setVraag1();
  }

  public void commit() {
    vraagForm.commit();
    zoekForm.commit();
  }

  public VraagForm getVraagForm() {
    return vraagForm;
  }

  public PresentievraagZoekForm getZoekForm() {
    return zoekForm;
  }

  public void reset() {
    zoekBean = new PresentievraagZoekBean();
    zoekForm.setBean(zoekBean);
    vraagForm.commit();
    setVraag(vraagForm.getBean().getVraag());
  }

  private void setVraag(BcGbaVraagbericht value) {
    switch (value) {
      default:
      case VRAAG1:
        setVraag1();
        break;

      case VRAAG2:
        setVraag2();
        break;

      case VRAAG3:
        setVraag3();
        break;

      case VRAAG4:
        setVraag4();
        break;
    }
  }

  private void setVraag1() {
    removeComponent(zoekForm);
    zoekForm = new PresentievraagZoekForm(zoekBean);
    zoekForm.setRequired(GESLACHTSNAAM, GEBOORTEDATUM, GESLACHT);
    zoekForm.disable(BUITENLAND_NR);
    addComponent(zoekForm);
    zoekForm.getField(PresentievraagZoekBean.VOORNAMEN).focus();
  }

  private void setVraag2() {
    removeComponent(zoekForm);
    zoekForm = new PresentievraagZoekForm(zoekBean);
    zoekForm.setRequired(GESLACHTSNAAM, GEBOORTEDATUM, GESLACHT, NATIONALITEIT, BUITENLAND_NR);
    addComponent(zoekForm);
    zoekForm.getField(PresentievraagZoekBean.VOORNAMEN).focus();
  }

  private void setVraag3() {
    removeComponent(zoekForm);
    zoekForm = new PresentievraagZoekForm(zoekBean);
    zoekForm.setRequired(NATIONALITEIT, BUITENLAND_NR);
    zoekForm.disable(VOORNAMEN, GEBOORTEDATUM, GEBOORTEPLAATS, GESLACHTSNAAM, GEBOORTELAND, GESLACHT);
    addComponent(zoekForm);
    zoekForm.getField(PresentievraagZoekBean.BUITENLAND_NR).focus();
  }

  private void setVraag4() {
    removeComponent(zoekForm);
    zoekForm = new PresentievraagZoekForm(zoekBean);
    zoekForm.setRequired(GEBOORTEDATUM);
    zoekForm.disable(GESLACHTSNAAM);
    addComponent(zoekForm);
    zoekForm.getField(PresentievraagZoekBean.VOORNAMEN).focus();
  }

  public class VraagForm extends PresentievraagVraagForm {

    @Override
    public void onChangeVraag(BcGbaVraagbericht value) {
      setVraag(value);
    }
  }
}

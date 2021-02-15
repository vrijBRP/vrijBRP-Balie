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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results;

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.PresentievraagResultBean.*;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.PresentievraagResultBean;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.PresentievraagResultForm;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public abstract class PresentatievraagResultsLayout extends VLayout {

  private final Presentievraag     presentievraag;
  private PresentievraagResultForm formPresentievraag;

  public PresentatievraagResultsLayout(final Presentievraag presentievraag) {
    this.presentievraag = presentievraag;
    spacing(true);
    addComponent(getLayout1());

    if (presentievraag.getPresentievraagAntwoord() != null) {
      PresentievraagResultsTable table = new PresentievraagResultsTable(presentievraag) {

        @Override
        public void onClick(Record record) {
          navigateToResult(presentievraag, record.getObject(PresentievraagMatch.class));
        }
      };

      if (!presentievraag.getPresentievraagAntwoord().getMatches().isEmpty()) {
        addComponent(new Fieldset("Overzicht resultaten presentievraag"));
        addComponent(new InfoLayout("", "Druk op een regel in de tabel voor meer informatie"));
        addExpandComponent(table);
      }
    }
  }

  public void update() {
    PresentievraagResultBean bean = formPresentievraag.getBean();
    bean.setZaakToelichting(presentievraag.getToelichting());
    formPresentievraag.setBean(bean);
  }

  protected abstract void navigateToResult(Presentievraag presentievraag, PresentievraagMatch match);

  private VerticalLayout getLayout1() {

    formPresentievraag = getPresentievraagForm();

    VLayout layout = new VLayout();
    layout.spacing(true).add(formPresentievraag);

    if (presentievraag.getPresentievraagAntwoord() != null) {
      PresentievraagResultForm resultaatForm = getResultaatForm();
      PresentievraagResultForm vraagForm = getVraagform();
      layout.spacing(true).add(resultaatForm, vraagForm);
    }
    return layout;
  }

  private PresentievraagResultForm getPresentievraagForm() {
    List<String> order = new ArrayList(
        Arrays.asList(ZAAK_OMSCHRIJVING, ZAAK_GEBRUIKER, ZAAK_TIJDSTIP, ZAAK_LOCATIE, ZAAK_TOELICHTING));
    PresentievraagResultForm form = new PresentievraagResultForm(presentievraag, null,
        order.toArray(new String[0]));
    form.setColumnWidths("200px", "300px", "180px", "");
    form.setCaption("Presentievraag");
    return form;
  }

  private PresentievraagResultForm getResultaatForm() {
    List<String> order = new ArrayList(Arrays.asList(VRAAG, VERWERKING, RESULTAAT));
    if (fil(presentievraag.getPresentievraagAntwoord().toResultaatPn(true))) {
      order.add(RESULTAAT_PN);
    }

    PresentievraagResultForm form = new PresentievraagResultForm(presentievraag, null,
        order.toArray(new String[0]));
    form.setColumnWidths("200px", "");
    form.setCaption("Resultaat");
    return form;
  }

  private PresentievraagResultForm getVraagform() {
    String[] order = new String[]{ VR_VOORNAMEN, VR_GEBOORTEDATUM, VR_VOORVOEGSEL, VR_GEBOORTEPLAATS, VR_GESLACHTSNAAM,
        VR_GEBOORTELAND, VR_GESLACHT, VR_DATUM_AANVANG_BL, VR_GEMEENTE, VR_BUITENLAND_PERSNR, VR_NATIONALITEIT };
    PresentievraagResultForm form = new PresentievraagResultForm(presentievraag, null, order);
    form.setColumnWidths("200px", "300px", "180px", "");
    form.setCaption("Vraag");
    return form;
  }
}

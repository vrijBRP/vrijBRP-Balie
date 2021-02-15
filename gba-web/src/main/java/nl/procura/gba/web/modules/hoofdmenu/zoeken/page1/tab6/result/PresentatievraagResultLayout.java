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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result;

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.PresentievraagResultBean.*;

import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.functies.VaadinUtils;

public class PresentatievraagResultLayout extends VLayout {

  private final Presentievraag         presentievraag;
  private final PresentievraagAntwoord antwoord;
  private final PresentievraagMatch    match;

  public PresentatievraagResultLayout(Presentievraag presentievraag, PresentievraagMatch match) {
    this.presentievraag = presentievraag;
    this.match = match;
    this.antwoord = presentievraag.getPresentievraagAntwoord();

    spacing(true);

    GbaTabsheet tabsheet = new GbaTabsheet();
    tabsheet.addStyleName("dynamic-width-tab");
    tabsheet.setExtraTopMargin();
    tabsheet.addTab(getLayout1(), "Resultaat / Score (" + match.getScore() + ")");
    tabsheet.addTab(getLayout2(), "Persoonsgevens");
    tabsheet.addTab(getLayout3(), "Onderzoekgegevens (" + match.getOnderzoeksgegevens().size() + ")");
    tabsheet.addListener((SelectedTabChangeListener) event -> VaadinUtils.resetHeight(getWindow()));
    addComponent(tabsheet);
  }

  private PresentievraagResultForm getAdresForm() {
    String[] order = new String[]{ GEMEENTEVANINSCHRIJVING, FUNCTIEADRES, GEMEENTEDEEL, LOCATIE, ADRES,
        LAND_VAN_VERTREK, ADRES_BUITENLAND };
    PresentievraagResultForm form = new PresentievraagResultForm(presentievraag, match, order);
    form.setCaption("Adres");
    return form;
  }

  private VerticalLayout getLayout1() {
    PresentievraagCompareTable table = new PresentievraagCompareTable(antwoord, match);
    VLayout layout = new VLayout();
    layout.spacing(true).add(getScoreForm()).add(new Fieldset("Vergelijking tussen vraag en antwoord")).add(table);
    return layout;
  }

  private VerticalLayout getLayout2() {

    VLayout layout = new VLayout();
    layout.spacing(true).add(getPersoonForm()).add(getAdresForm());
    return layout;
  }

  private VerticalLayout getLayout3() {
    VLayout layout = new VLayout();
    PresentievraagResultTable table = new PresentievraagResultTable(antwoord, match);
    layout.sizeFull().spacing(true).add(new Fieldset("Gegevens in onderzoek")).addExpandComponent(table);
    return layout;
  }

  private PresentievraagResultForm getPersoonForm() {
    String[] order = new String[]{ BSN, VOORNAMEN, VOORVOEGSEL, GESLACHTSNAAM, ADELIJKETITEL, GEBOREN, GESLACHT,
        DATUMOVERLIJDEN, INDICATIEGEHEIM, OPSCHORTING, NATIONALITEIT, BUITENLANDS_PERSOONSNUMMER };
    PresentievraagResultForm form = new PresentievraagResultForm(presentievraag, match, order);
    form.setCaption("Persoon");
    return form;
  }

  private PresentievraagResultForm getScoreForm() {
    String[] order = new String[]{ SCORE, REGISTRATIE, VOLGNR };
    PresentievraagResultForm form = new PresentievraagResultForm(presentievraag, match, order);
    form.setColumnWidths("100px", "");
    form.setCaption("Score");
    return form;
  }
}

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

package nl.procura.gba.web.modules.beheer.gebruikers.page11;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.CheckBox;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.gebruikers.page1.Page1Gebruikers;
import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.modules.beheer.parameters.container.ParmBooleanContainer;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfo;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.locatie.LocatieType;
import nl.procura.gba.web.services.beheer.parameter.Parameter;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.services.beheer.parameter.annotations.ParameterAnnotation;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page11Gebruikers extends NormalPageTemplate {

  private final static Logger LOGGER    = LoggerFactory.getLogger(Page11Gebruikers.class.getName());
  private final CheckBox      cbProf    = new CheckBox("Profielen", true);
  private final CheckBox      cbParm    = new CheckBox("Parameters", true);
  private final CheckBox      cbLoc     = new CheckBox("Locaties", true);
  private final CheckBox      cbDoc     = new CheckBox("Documenten", true);
  private final CheckBox      cbInfo    = new CheckBox("Extra gebruikersgegevens", false);
  private final List<String>  meldingen = new ArrayList<>();
  private Page11GebruikerForm form      = null;
  private Table               table     = null;

  public Page11Gebruikers() {

    super("Kopiëren van instellingen van gebruikers");

    buttonSave.setCaption("Uitvoeren (F9)");

    addButton(buttonPrev);
    addButton(buttonSave);

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInfo("Kopiëer de instellingen van <b>gebruiker A</b> naar <b>gebruiker B</b>.");

      form = new Page11GebruikerForm();
      table = new Table();

      addComponent(form);
      addComponent(cbProf);
      addComponent(cbParm);
      addComponent(cbLoc);
      addComponent(cbDoc);
      addComponent(cbInfo);
      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(Page1Gebruikers.class);
  }

  @Override
  public void onSave() {

    form.commit();

    if (!is(cbProf, cbParm, cbLoc, cbDoc, cbInfo)) {
      throw new ProException(WARNING, "Selecteer minimaal 1 gegevenstype.");
    }

    long codeVan = along(form.getBean().getGebruikerVan().getValue());
    long codeNaar = along(form.getBean().getGebruikerNaar().getValue());

    final Gebruiker gebruikerVan = getServices().getGebruikerService().getGebruikerByCode(codeVan, true);
    final Gebruiker gebruikerNaar = getServices().getGebruikerService().getGebruikerByCode(codeNaar, true);

    String vraag = String.format("Wilt u de gegevens kopiëren van %s naar %s?", gebruikerVan.getNaam(),
        gebruikerNaar.getNaam());

    getWindow().addWindow(new ConfirmDialog(vraag) {

      @Override
      public void buttonYes() {

        meldingen.clear();

        opschonen(gebruikerNaar);
        kopieeren(gebruikerVan, gebruikerNaar);

        table.init();

        successMessage("De instellingen zijn gekopieerd.");

        closeWindow();
      }
    });
  }

  private Field getField(String parm) {
    for (Field field : ParameterBean.class.getDeclaredFields()) {
      ParameterAnnotation parmAnn = field.getAnnotation(ParameterAnnotation.class);
      if (parmAnn != null) {
        if (parmAnn.value().getKey().equalsIgnoreCase(parm)) {
          return field;
        }
      }
    }

    return null;
  }

  private List<Object> getGebruikerObjecten(Gebruiker gebruiker) {

    List<Object> list = new ArrayList<>();

    if (is(cbProf)) {
      list.addAll(gebruiker.getProfielen().getAlle());
    }
    if (is(cbParm)) {
      list.addAll(select(gebruiker.getParameters().getAlle(),
          having(on(Parameter.class).isNiveauGebruiker(), equalTo(true))));
    }
    if (is(cbLoc)) {
      list.addAll(
          getServices().getLocatieService().getGekoppeldeLocaties(gebruiker, LocatieType.NORMALE_LOCATIE));
    }
    if (is(cbDoc)) {
      list.addAll(getServices().getDocumentService().getGekoppeldeDocumenten(gebruiker));
    }
    if (is(cbInfo)) {
      list.addAll(select(gebruiker.getInformatie().getAlles(),
          having(on(GebruikerInfo.class).isIedereen(), equalTo(false))));
    }

    return list;
  }

  private String getParameterOmschrijving(String parm) {

    Field field = getField(parm);

    if (field != null) {

      nl.procura.vaadin.annotation.field.Field fieldAnn = field.getAnnotation(
          nl.procura.vaadin.annotation.field.Field.class);

      if (fieldAnn != null) {

        return fieldAnn.caption();
      }
    }

    return "";
  }

  private boolean is(CheckBox... cbs) {
    for (CheckBox cb : cbs) {
      if (isTru(astr(cb.getValue()))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Zoek field in ParameterBean op basis van parameter
   */
  private boolean isBooleanParm(String parm) {

    Field field = getField(parm);

    if (field != null) {

      Select selectAnn = field.getAnnotation(Select.class);

      if (selectAnn != null) {

        return (selectAnn.containerDataSource() == ParmBooleanContainer.class);
      }
    }

    return false;
  }

  private void kopieeren(Gebruiker gebruikerVan, Gebruiker gebruikerNaar) {

    List<Object> gebruikerObjecten = getGebruikerObjecten(gebruikerVan);

    for (Object object : gebruikerObjecten) {

      if (object instanceof Profiel) {
        Profiel profiel = (Profiel) object;

        getServices().getProfielService().koppelActie(asList(gebruikerNaar), asList(profiel),
            KoppelActie.KOPPEL);

        meldingen.add("Profiel|" + profiel.getProfiel() + "|gekoppeld");
      } else if (object instanceof Parameter) {

        Parameter parm = (Parameter) object;
        String oms = getParameterOmschrijving(parm.getParm());

        if (fil(oms)) {
          getServices().getParameterService().setParm(ParameterType.getByKey(parm.getParm()), parm.getValue(),
              gebruikerNaar, null,
              false);

          String waarde = parm.getValue();
          if (isBooleanParm(parm.getParm())) {
            if (waarde.equals("1")) {
              waarde = "Ja";
            }

            if (waarde.equals("0")) {
              waarde = "Nee";
            }
          }

          meldingen.add("Parameter|" + oms + "|" + waarde);
        }
      } else if (object instanceof Locatie) {

        Locatie locatie = (Locatie) object;

        getServices().getLocatieService().koppelActie(asList(gebruikerNaar), asList(locatie),
            KoppelActie.KOPPEL);

        meldingen.add("Locatie|" + locatie.getLocatie() + "|gekoppeld");
      } else if (object instanceof DocumentRecord) {

        DocumentRecord document = (DocumentRecord) object;

        getServices().getDocumentService().koppelActie(asList(gebruikerNaar), asList(document),
            KoppelActie.KOPPEL);

        meldingen.add("Document|" + document.getDocument() + "|gekoppeld");
      } else if (object instanceof GebruikerInfo) {

        GebruikerInfo gebruikerInfo = (GebruikerInfo) object;

        gebruikerInfo.setGebruiker(gebruikerNaar);

        getServices().getGebruikerInfoService().save(gebruikerInfo);

        meldingen.add("Info|" + gebruikerInfo.getOmschrijving() + "|" + gebruikerInfo.getWaarde());
      } else {

        LOGGER.error("Onbekend|" + object + "|" + object.getClass());
      }
    }
  }

  /**
   * Alle gegevens bij gebruiker verwijderen
   */
  private void opschonen(Gebruiker gebruikerNaar) {

    List<Object> gebruikerObjecten = getGebruikerObjecten(gebruikerNaar);

    for (Object object : gebruikerObjecten) {

      if (object instanceof Profiel) {
        Profiel profiel = (Profiel) object;
        getServices().getProfielService().koppelActie(asList(gebruikerNaar),
            asList(profiel), KoppelActie.ONTKOPPEL);

      } else if (object instanceof Parameter) {
        Parameter parm = (Parameter) object;
        getServices().getParameterService().setParm(ParameterType.getByKey(parm.getParm()),
            "", gebruikerNaar, null, true);

      } else if (object instanceof Locatie) {
        Locatie locatie = (Locatie) object;
        getServices().getLocatieService().koppelActie(asList(gebruikerNaar),
            asList(locatie), KoppelActie.ONTKOPPEL);

      } else if (object instanceof DocumentRecord) {
        DocumentRecord document = (DocumentRecord) object;
        getServices().getDocumentService().koppelActie(asList(gebruikerNaar),
            asList(document), KoppelActie.ONTKOPPEL);

      } else if (object instanceof GebruikerInfo) {
        GebruikerInfo gebruikerInfo = (GebruikerInfo) object;
        gebruikerInfo.setWaarde("");
        gebruikerInfo.setGebruiker(gebruikerNaar);
        getServices().getGebruikerInfoService().save(gebruikerInfo);
      }
    }
  }

  public class Table extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Nr", 30);
      addColumn("Category", 100);
      addColumn("Object");
      addColumn("Waarde");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      int nr = 0;

      for (String melding : meldingen) {

        nr++;

        String[] splits = melding.split("\\|");

        if (splits.length == 3) {

          Record r = addRecord(melding);

          r.addValue(nr);
          r.addValue(splits[0]);
          r.addValue(splits[1]);
          r.addValue(splits[2]);
        }
      }

      super.setRecords();
    }
  }
}

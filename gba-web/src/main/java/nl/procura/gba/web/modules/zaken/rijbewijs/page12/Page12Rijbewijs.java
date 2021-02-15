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

package nl.procura.gba.web.modules.zaken.rijbewijs.page12;

import static com.vaadin.event.ShortcutAction.KeyCode.F1;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page12.Page12RijbewijsBean1.*;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.FS_RIJBEWIJS;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.application.ProcessChangeInterceptor;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.modules.zaken.rijbewijs.RbwUpdateToDateCheck;
import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsTabel1;
import nl.procura.gba.web.modules.zaken.rijbewijs.page9.Page9RijbewijsBean1;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagStatus;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType;
import nl.procura.gba.web.services.zaken.rijbewijs.converters.P1658ToAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.converters.P1658ToDocumentAanvraag;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.rdw.messages.P1656;
import nl.procura.rdw.messages.P1658;
import nl.procura.rdw.messages.P1908;
import nl.procura.rdw.processen.p1656.f02.STATRYBKGEG;
import nl.procura.rdw.processen.p1658.f02.AANVRRYBKRT;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.dialog.OkDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

/**
 * Uitreikproces
 */

public class Page12Rijbewijs extends RijbewijsPage {

  private final Button buttonUitreiken     = new Button("Uitreiken");
  private final Button buttonNietUitreiken = new Button("Niet uitreiken");

  private final P1658          p1658;
  private String               belemmering = "";
  private Page12RijbewijsForm1 form1       = null;
  private Page12RijbewijsForm1 form2       = null;
  private Page2RijbewijsTabel1 table1      = null;
  private RijbewijsAanvraag    crbAanvraag;

  public Page12Rijbewijs(P1658 p1658) {

    super("Rijbewijsaanvraag: uitreiken");

    this.p1658 = p1658;

    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonUitreiken);
    addButton(buttonNietUitreiken);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table1 = new Page2RijbewijsTabel1();

      AANVRRYBKRT p1658_f2 = (AANVRRYBKRT) p1658.getResponse().getObject();

      P1908 p1908 = new P1908();
      p1908.newF1(astr(p1658_f2.getNatpersoongeg().getFiscnrnatp()));

      crbAanvraag = P1658ToAanvraag.get(p1658, getPl(), getServices());

      belemmering = "";
      if (sendMessage(p1908)) {
        setInfo("Uitreiken / niet uitreiken van het rijbewijs",
            "De RDW meldt dat er geen belemmeringen zijn voor de uitreiking.");

        new RbwUpdateToDateCheck(crbAanvraag, (GbaWindow) getWindow()) {

          @Override
          public void onProceed(RijbewijsAanvraag checkedAanvraag) {
            belemmering = "";

            if (form2 != null) {
              form2.getBean().setBelemmering(belemmering);
              form2.repaint();
            }

            crbAanvraag = checkedAanvraag;
            table1.setAanvraag(checkedAanvraag);
            checkUitreikBelemmeringen(false);
          }

          @Override
          public void onNotUpdated() {
            belemmering = "database dient eerst te worden bijgewerkt.";
          }
        };

      } else {
        setInfo("Uitreiken / niet uitreiken van het rijbewijs", "").setIcon(ProcuraTheme.ICOON_24.WARNING);
        checkUitreikBelemmeringen(true);

        if (p1908.getResponse().isMelding()) {
          belemmering = p1908.getResponse().getMelding().getMeldingKort().toLowerCase();
        }
      }

      AANVRRYBKRT response = (AANVRRYBKRT) p1658.getResponse().getObject();
      RijbewijsAanvraagAntwoord antwoord = P1658ToDocumentAanvraag.get(response, getServices());
      form1 = getForm1(antwoord, belemmering);
      form2 = getForm2(antwoord, belemmering);

      addComponent(form1);
      addComponent(form2);

      addComponent(new Fieldset("Statussen toegevoegd vanuit Proweb personen", table1));
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonUitreiken) {
      updateStatus(RijbewijsStatusType.RIJBEWIJS_UITGEREIKT);

    } else if (button == buttonNietUitreiken) {
      getParentWindow().addWindow(new ConfirmDialog(
          "U wilt het document definitief niet uitreiken?", 340) {

        @Override
        public void buttonYes() {
          updateStatus(RijbewijsStatusType.RIJBEWIJS_NIET_UITGEREIKT);
          super.buttonYes();
        }
      });

    } else if (isKeyCode(button, keyCode, F1, buttonPrev)) {
      getApplication().getProcess().intercept(new ProcessChangeInterceptor(getWindow(), null) {

        @Override
        protected void proceed(AccordionLink link) {
          onPreviousPage();
        }
      });
    } else {

      super.handleEvent(button, keyCode);
    }
  }

  private void checkUitreikBelemmeringen(boolean isRDWBelemmering) {
    boolean functiescheidingCorrect = isFunctiescheidingCorrect();

    buttonUitreiken.setEnabled(functiescheidingCorrect && !isRDWBelemmering);
    buttonNietUitreiken.setEnabled(functiescheidingCorrect);

    StringBuilder info = new StringBuilder();
    List<String> fouten = new ArrayList<>();

    if (isRDWBelemmering) { // Mag niet doorgaan van het RDW
      fouten.add(setClass(false, "De RDW meldt een belemmering voor de uitreiking."));
    }

    if (!functiescheidingCorrect) { // Functiescheiding is van toepassing
      fouten.add(setClass(false, "Uitreiking niet mogelijk wegens functiescheiding."));
    }

    if (fouten.size() > 0) {
      info.append("<ul>");
      for (String fout : fouten) {
        info.append("<li>");
        info.append(fout);
        info.append("</li>");
      }

      info.append("</ul>");
      addInfo(info.toString());

    } else {
      getApplication().getProcess().startProcess();
    }
  }

  private Page12RijbewijsForm1 getForm1(RijbewijsAanvraagAntwoord antwoord, String belemmering) {

    return new Page12RijbewijsForm1(antwoord, belemmering) {

      @Override
      public void initForm() {

        setCaption("Aanvraaggegevens");
        setOrder(STATUS, SOORT, REDEN, AANVRAAGNR, HUIDIGRBWNR, NIEUWRBWNR);

        super.initForm();
      }
    };
  }

  private Page12RijbewijsForm1 getForm2(RijbewijsAanvraagAntwoord antwoord, String belemmering) {

    return new Page12RijbewijsForm1(antwoord, belemmering) {

      @Override
      public void initForm() {

        setCaption("Belemmering");
        setOrder(BELEMMERING);

        super.initForm();
      }
    };
  }

  /**
   * is er sprake van functiescheiding en wordt uitreiking door dezelfde persoon gedaan?
   */
  private boolean isFunctiescheidingCorrect() {

    boolean fs = isTru(getApplication().getParmValue(FS_RIJBEWIJS));

    boolean eqGebruiker = along(
        crbAanvraag.getIngevoerdDoor().getValue()) == getApplication().getServices().getGebruiker().getCUsr();

    return (!fs || !eqGebruiker);
  }

  private void updateStatus(RijbewijsStatusType statusType) {

    P1656 p1656 = new P1656();

    long userId = getApplication().getServices().getGebruiker().getCUsr();

    p1656.newF1(statusType.getCode(), null, userId, p1658);

    if (sendMessage(p1656)) {

      nl.procura.rdw.processen.p1656.f02.AANVRRYBKRT p1656f2 = (nl.procura.rdw.processen.p1656.f02.AANVRRYBKRT) p1656
          .getResponse().getObject();

      STATRYBKGEG stat_geg = p1656f2.getStatrybkgeg();

      long dIn = along(stat_geg.getStatdatrybk());
      long tIn = along(stat_geg.getStattydrybk());
      RijbewijsStatusType stat = RijbewijsStatusType.get(along(stat_geg.getStatcoderybk()));

      Page9RijbewijsBean1 b = new Page9RijbewijsBean1();
      b.setStatus(stat.toString());

      crbAanvraag.getStatussen().addStatus(
          new RijbewijsAanvraagStatus(dIn, tIn, stat, getApplication().getServices().getGebruiker()));

      crbAanvraag.setGebruikerUitgifte(new UsrFieldValue(getApplication().getServices().getGebruiker()));
      getServices().getRijbewijsService().save(crbAanvraag);

      table1.init();

      String msg = "De status bij het RDW is nu: <hr/><b>" + stat + "</b>.<hr/>Dit proces is voltooid.";
      getWindow().addWindow(new OkDialog("Voltooid.", msg) {

        @Override
        public void closeWindow() {
          Page12Rijbewijs.this.getApplication().getProcess().endProcess();
          super.closeWindow();
        }
      });
    }
  }
}

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

package nl.procura.gba.web.modules.zaken.verkiezing.page1;

import static java.util.Collections.singletonList;
import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.STANDAARD;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.KiesrWijz;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.verkiezing.page2.Page2VerkiezingWindow;
import nl.procura.gba.web.modules.zaken.verkiezing.page3.Page3Verkiezing;
import nl.procura.gba.web.modules.zaken.verkiezing.page4.Page4VerkiezingWindow;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType;
import nl.procura.gba.web.services.beheer.verkiezing.Verkiezing;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.ProcuraTheme;
import nl.procura.validation.Anummer;

public class Page1Verkiezing extends ZakenPage {

  private final Button zoekBtn   = new Button("Gemachtigde zoeken");
  private final Button wijzigBtn = new Button("Stempas aanpassen");
  private final Button printBtn  = new Button("Afdrukken");
  private final Button protBtn   = new Button("Uitgevoerde handelingen");

  private Page1VerkiezingForm1 form1;
  private Page1VerkiezingForm2 form2;
  private Page1VerkiezingTable table;

  public Page1Verkiezing() {
    super("Kiezersregister");
    setMargin(true);
    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {
    if (event instanceof InitPage) {
      if (getVerkiezingen().isEmpty()) {
        setInfo("", "Er zijn geen verkiezingen ingesteld door de applicatiebeheerder of de verkiezing is geweest");
      } else {
        table = new Page1VerkiezingTable();
        form2 = new Page1VerkiezingForm2();
        form2.setStempasListener(stempas -> {
          zoekBtn.setEnabled(stempas != null && stempas.getAnrGemachtigde().isCorrect());
          printBtn.setEnabled(stempas != null && stempas.isOpgeslagen());
        });

        form1 = new Page1VerkiezingForm1();
        form1.setVerkiezingListener(verkiezing -> {
          form2.setStempassen(getStempassen(verkiezing));
          table.setStempassen(getVolmachten(verkiezing));
        });
        refreshVerkiezing();
        OptieLayout ol1 = new OptieLayout();
        ol1.getLeft().addComponent(form1);
        ol1.getRight().setCaption("Opties");
        ol1.getRight().setWidth("200px");
        ol1.getRight().addButton(protBtn, e -> onProt(form1.getVerkiezing()));

        addComponent(ol1);
        addComponent(new Fieldset("Ontvangen volmachten"));
        addComponent(table);

        OptieLayout ol2 = new OptieLayout();
        ol2.getLeft().addComponent(form2);
        ol2.getRight().setCaption("Opties");
        ol2.getRight().setWidth("200px");
        ol2.getRight().addButton(wijzigBtn, e -> onChange(form2.getStempas()));
        ol2.getRight().addButton(printBtn, e -> onPrint(form2.getStempas()));
        ol2.getRight().addButton(zoekBtn, e -> onZoekPersoon(form2.getStempas().getAnrGemachtigde()));
        addComponent(ol2);
      }
    }

    super.event(event);
  }

  private void onProt(Verkiezing verkiezing) {
    getParentWindow().addWindow(new Page4VerkiezingWindow(getWijzigingen(verkiezing)));
  }

  private void onZoekPersoon(Anummer anr) {
    getApplication().goToPl(getWindow(), "zaken.kiezersregister", STANDAARD, anr.getAnummer());
  }

  private void onChange(Stempas stempas) {
    getParentWindow().addWindow(new Page2VerkiezingWindow(stempas) {

      @Override
      public void closeWindow() {
        refreshVerkiezing();
        super.closeWindow();
      }
    });
  }

  private void refreshVerkiezing() {
    form1.setVerkiezingen(getVerkiezingen());
  }

  private void onPrint(Stempas stempas) {
    if (StempasAanduidingType.AAND_VERVANGEN == stempas.getAanduidingType()) {
      String msg = "Deze stempas is vervangen, doorgaan naar het afdrukscherm?";
      getParentWindow().addWindow(new ConfirmDialog("Afdrukken?", msg,
          "450px", ProcuraTheme.ICOON_24.WARNING) {

        @Override
        public void buttonYes() {
          getNavigation().goToPage(new Page3Verkiezing(stempas));
          super.buttonYes();
        }
      });
    } else {
      getNavigation().goToPage(new Page3Verkiezing(stempas));
    }
  }

  private List<Verkiezing> getVerkiezingen() {
    return getServices().getKiezersregisterService().getVerkiezingen(getAnummer());
  }

  private List<KiesrWijz> getWijzigingen(Verkiezing verkiezing) {
    if (verkiezing != null) {
      Anummer anr = getAnummer();
      return getServices().getKiezersregisterService().getWijzigingen(verkiezing.getVerk(), anr);
    }
    return new ArrayList<>();
  }

  private List<Stempas> getStempassen(Verkiezing verkiezing) {
    List<Stempas> stempassen = verkiezing.getStempassen();
    return stempassen.isEmpty() ? singletonList(new Stempas(verkiezing)) : stempassen;
  }

  private List<Stempas> getVolmachten(Verkiezing verkiezing) {
    if (verkiezing != null) {
      Anummer anr = getAnummer();
      return getServices().getKiezersregisterService().getStempassenByVolmachtAnr(verkiezing.getVerk(), anr);
    }
    return new ArrayList<>();
  }

  private Anummer getAnummer() {
    return new Anummer(getServices().getPersonenWsService().getHuidige().getPersoon().getAnr().getVal());
  }
}

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

package nl.procura.gba.web.modules.bs.geboorte.page20;

import static nl.procura.gba.web.components.containers.Container.PLAATS;
import static nl.procura.gba.web.modules.bs.geboorte.page20.Page20GeboorteBean2.TARDIEVE_AANGIFTE;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.web.common.misc.GbaDatumUtils;
import nl.procura.gba.web.common.misc.GbaDatumUtils.Dag;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */

public class Page20Geboorte<T extends DossierGeboorte> extends BsPage<T> {

  private static final int MAX_DAGEN     = 3;
  private static final int MAX_WERKDAGEN = 2;

  private final Button buttonAdd    = new Button("+");
  private final Button buttonRemove = new Button("-");

  private final InlineDateField datetime = new InlineDateField();
  private GeboorteTable         table    = null;
  private Page20GeboorteForm2   form2    = null;

  private Page20GeboorteForm4 form4 = null;
  private Page20GeboorteForm3 form3 = null;

  private InfoLayout tardieveInfo = new InfoLayout("", "");
  private boolean    tardieve     = false;

  public Page20Geboorte() {
    this("Geboorte - aangifte");
  }

  public Page20Geboorte(String title) {
    super(title);
  }

  @Override
  public boolean checkPage() {

    form2.commit();
    form4.commit();

    if (table.getRecords().isEmpty()) {
      throw new ProException(INFO, "Voer de geboortedatums en tijden in.");
    }

    DossierGeboorte dossier = getZaakDossier();

    boolean isTardieve = form4.getBean().isTardieveAangifte();
    String redenTardieve = form4.getBean().getTardieveReden();

    // Reden verplicht + tardieve
    dossier.setRedenVerplichtBevoegd(form2.getBean().getRedenVerplicht());
    dossier.setTardieveAangifte(form4.getBean().isTardieveAangifte());
    dossier.setTardieveReden(isTardieve ? redenTardieve : "");

    // Maak de aangiftes definitief
    for (DossierPersoon kind : getDossier().getPersonen(KIND)) {
      kind.setDefinitief(true);
    }

    // Save
    getApplication().getServices().getGeboorteService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form2 = new Page20GeboorteForm2(getZaakDossier());
      form3 = new Page20GeboorteForm3();
      form4 = new Page20GeboorteForm4(getZaakDossier());

      addButton(buttonPrev);
      addButton(buttonNext);

      addComponent(new BsStatusForm(getDossier()));

      setInfo("Vul de aangiftegegevens in en druk op Volgende (F2) om verder te gaan.");

      addComponent(form2);

      HorizontalLayout h1 = new HorizontalLayout();
      h1.setWidth("100%");
      h1.setSpacing(true);

      VerticalLayout v1 = new VerticalLayout();
      v1.setSpacing(true);
      v1.setWidth("250px");

      datetime.setValue(new java.util.Date());
      datetime.setResolution(InlineDateField.RESOLUTION_DAY);
      datetime.setImmediate(true);

      VerticalLayout buttonLayout = new VerticalLayout();
      buttonLayout.setWidth("60px");
      buttonLayout.setSpacing(true);

      buttonLayout.addComponent(buttonAdd);
      buttonLayout.addComponent(buttonRemove);

      buttonAdd.addListener(this);
      buttonRemove.addListener(this);

      buttonAdd.setWidth("50px");
      buttonRemove.setWidth("50px");

      v1.addComponent(datetime);
      v1.addComponent(form3);

      h1.addComponent(v1);
      h1.addComponent(buttonLayout);

      table = new GeboorteTable();
      table.setHeight("200px");

      h1.addComponent(table);
      h1.setExpandRatio(table, 1f);

      addComponent(new Fieldset("Geboorten", h1));

      if (isToonTermijnLayout()) {

        addComponent(new Fieldset("Termijn"));
        addComponent(tardieveInfo);
        addComponent(form4);
      }

      checkTardieve(false);
    }

    super.event(event);

    form3.getField(Page20GeboorteForm3.TIJD).focus();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonAdd) {

      addKind();
    } else if (button == buttonRemove) {

      List<DossierPersoon> kinderen = table.getSelectedValues(DossierPersoon.class);

      for (DossierPersoon kind : kinderen) {
        if (kind.isDefinitief()) {
          throw new ProException(WARNING, "Deze gegevens kunnen niet meer worden verwijderd.");
        }
      }

      getServices().getDossierService().deletePersonen(getDossier(), kinderen);

      table.init();

      checkTardieve(true);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onEnter() {

    addKind();
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

  /**
   * Bij levenloos is er geen sprake van termijn
   */
  protected boolean isToonTermijnLayout() {
    return true;
  }

  private void addKind() {

    form3.commit();

    Date geboorte = (Date) datetime.getValue();

    if (pos(new ProcuraDate().diffInDays(new ProcuraDate(geboorte)))) {
      throw new ProException(WARNING, "De geboortedatum kan niet in de toekomst liggen.");
    }

    FieldValue gemeente = PLAATS.get(getApplication().getServices().getGebruiker().getGemeenteCode());

    if (!pos(gemeente.getValue())) {
      throw new ProException(WARNING, "Kan de huidige gemeente niet bepalen.");
    }

    DossierPersoon kind = new DossierPersoon();
    kind.setDatumGeboorte(new GbaDateFieldValue(new DateTime(geboorte)));

    kind.setTijdGeboorte(new DateTime(0, along(form3.getBean().getTijd().getValue()), TimeType.TIME_4_DIGITS));
    kind.setGeboorteplaats(gemeente);
    kind.setGeboorteland(Landelijk.getNederland());

    getServices().getDossierService().addKind(getZaakDossier(), kind);

    table.init();

    checkTardieve(true);
  }

  /**
   * Controleer of er sprake is van tardieve
   *
   * @param addRemoveKind = Is er sprake van toevoegen of verwijderen van kinderen. Zoja, dan tardieve aangifte opnieuw beoordelen
   */
  private void checkTardieve(boolean addRemoveKind) {

    if (!isToonTermijnLayout()) {
      return;
    }

    tardieve = false;

    StringBuilder sb = new StringBuilder();
    int aantalDagen = 0;
    int aantalWerkDagen = 0;

    SimpleDateFormat dg = new SimpleDateFormat("dd-MM-yyyy");

    ArrayList<DossierPersoon> kinderen = table.getAllValues(DossierPersoon.class);

    if (kinderen.size() > 0) {

      for (DossierPersoon kind : kinderen) {

        for (Dag dag : GbaDatumUtils.getDagenTussen(new DateTime(kind.getDatumGeboorte().getLongDate()),
            new DateTime(new ProcuraDate().getSystemDate()))) {

          aantalDagen++;

          // Als derde dag valt in weekend of op feestdag dan verlengen met 1 dag

          if (aantalDagen == MAX_DAGEN && !dag.isTelt()) {
            aantalDagen--;
          }

          if (dag.isTelt()) {

            aantalWerkDagen++;

            sb.append(MessageFormat.format("{0}e dag: ({1}) {2}", aantalDagen, dg.format(dag.getDag()),
                dag.getOms().toLowerCase()));

            // Als er meer dan 3 dagen zijn verstreken en aantal werkdagen > 2 dan is er sprake van tardieve.

            if (aantalDagen > MAX_DAGEN) {

              if (aantalWerkDagen > MAX_WERKDAGEN) {
                tardieve = true;
              } else {
                sb.append(", maar 1 werkdag in termijn dus extra dag toevoegen");
              }
            }

            if (tardieve) {
              sb.append("<b> (te laat)</b>");
            }
          } else {

            sb.append(MessageFormat.format("{0}e dag: ({1}) geen werkdag ({2})", aantalDagen,
                dg.format(dag.getDag()), dag.getOms().toLowerCase()));
          }

          sb.append("<br/>");

          if (tardieve) {
            break;
          }
        }

        break;
      }
    } else {

      sb.append("Geeft eerst de geboortedatum in.");
    }

    InfoLayout il = new InfoLayout("Afleiding termijn", sb.toString());

    replaceComponent(tardieveInfo, il);

    tardieveInfo = il;

    if (addRemoveKind) {

      form4.getField(TARDIEVE_AANGIFTE).setValue(tardieve);
    }
  }

  public class GeboorteTable extends Page20GeboorteTable {

    @Override
    public void setRecords() {

      int nr = 0;

      for (DossierPersoon kind : getZaakDossier().getKinderen()) {

        Record r = addRecord(kind);

        nr++;

        r.addValue(nr);
        r.addValue(kind.getDatumGeboorte().getFormatDate() + " om " + kind.getTijdGeboorte().getFormatTime(
            "HH:mm"));
      }

      super.setRecords();
    }
  }
}

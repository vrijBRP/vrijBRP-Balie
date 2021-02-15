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

package nl.procura.gba.web.modules.bs.common.pages.residentpage;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.common.ZaakStatusType.*;
import static nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider.consentProvider;
import static nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider.otherConsentProvider;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.common.misc.SelectListener;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.bs.common.layouts.relocation.AddressInfoLayout;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.services.gba.functies.PersoonslijstStatus;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

import lombok.Setter;

public class ResidentPage extends NormalPageTemplate {

  private static final String RIGHT_WIDTH = "200px";

  private final Button buttonToonBinnen = new Button("Zoeken (Enter)");
  private final Button buttonToonBuiten = new Button("Zoeken buiten adres (F3)");
  private final Button buttonNp         = new Button("Natuurlijk persoon (F4)");
  private final Button buttonNnp        = new Button("Niet-natuurlijk persoon (F5)");
  private final Button buttonShowInfo   = new Button("Toon relevante informatie");

  private final ResidentTable                   residentTable = new ResidentTable();
  private final VerhuisAdres                    destinationAddress;
  private final SelectListener<ConsentProvider> relationshipListener;
  private final FeedbackForm                    feedbackForm;
  private final FindResidentForm                findResidentForm;
  private final ConsentProviderForm             consentProviderForm;

  private ConsentProvider                        relationship;
  private final Function<PLEArgs, List<Relatie>> searchResidents;

  /**
   * @param destinationAddress address where the people are moving to or want register
   * @param relationship existing relationship which already selected
   * @param listener relationship update listener
   * @param searchResidents function for search residents
   */
  public ResidentPage(VerhuisAdres destinationAddress, ConsentProvider relationship,
      SelectListener<ConsentProvider> listener,
      Function<PLEArgs, List<Relatie>> searchResidents) {

    super("Bewoners");

    this.destinationAddress = requireNonNull(destinationAddress);
    this.relationship = requireNonNull(relationship);
    this.searchResidents = requireNonNull(searchResidents);
    consentProviderForm = new ConsentProviderForm();
    feedbackForm = new FeedbackForm();
    findResidentForm = new FindResidentForm(this.destinationAddress);
    relationshipListener = listener;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
    super.onClose();
  }

  @Override
  public void initPage() {

    buttonClose.addListener(this);
    getMainbuttons().add(buttonClose);

    consentProviderForm.setHeight("90px");

    // Layout 1
    final OptieLayout ol1 = new OptieLayout();
    ol1.getLeft().addComponent(findResidentForm);
    ol1.getRight().setWidth(RIGHT_WIDTH);
    ol1.getRight().setCaption("Zoeken");

    final VerticalLayout v1 = new VerticalLayout();
    v1.setSpacing(true);
    add(v1, buttonToonBinnen);
    add(v1, buttonToonBuiten);
    v1.setExpandRatio(buttonToonBuiten, 1F);
    ol1.getRight().addComponent(v1);

    // Layout 2
    final OptieLayout ol2 = new OptieLayout();
    ol2.getLeft().addComponent(consentProviderForm);
    ol2.getRight().setWidth(RIGHT_WIDTH);
    ol2.getRight().setCaption("Toestemming");

    final VerticalLayout v2 = new VerticalLayout();
    v2.setSpacing(true);
    add(v2, buttonNp);
    add(v2, buttonNnp);
    v2.setExpandRatio(buttonNnp, 1F);
    ol2.getRight().addComponent(v2);

    // Layout 3
    final OptieLayout ol3 = new OptieLayout();
    ol3.getRight().setWidth(RIGHT_WIDTH);
    ol3.getRight().setCaption("Terugmelding");
    ol3.getLeft().addComponent(feedbackForm);
    ol3.getRight().addButton(buttonSave, this);
    ol3.getRight().addButton(buttonDel, this);

    // Layout 4
    final OptieLayout ol4 = new OptieLayout();
    ol4.getRight().setWidth(RIGHT_WIDTH);
    ol4.getRight().setCaption("Opties");
    ol4.getRight().addButton(buttonShowInfo, this);
    ol4.getLeft().addComponent(new Fieldset("Bewoners"));
    ol4.getLeft().addComponent(residentTable);

    final String msg = "<hr/>Selecteer de toestemminggever door deze aan te klikken. Met behulp van de optie Niet-natuurlijk persoon kan "
        + "toestemming door bijvoorbeeld een stichting worden vastgelegd. <br/>"
        + "Als er personen staan ingeschreven op het adres die er niet (meer) wonen, vul dan in waar deze verblijven, "
        + "selecteer deze personen en druk op Opslaan (F9) om hier een terugmelding van te maken.";

    addComponent(new AddressInfoLayout().set(destinationAddress, msg));
    addComponent(ol1);
    addComponent(ol2);
    addComponent(ol3);
    addExpandComponent(ol4);

    updateRelationship();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (isKeyCode(button, keyCode, KeyCode.ENTER, buttonToonBinnen)) {

      if (destinationAddress.getCurrentPersons().size() > 50) {

        final String msg = "Op dit adres wonen " + destinationAddress.getCurrentPersons().size() + " personen. "
            + "<br/>Weet u zeker dat u deze wilt tonen?";

        getParentWindow().addWindow(new ConfirmDialog(msg) {

          @Override
          public void buttonYes() {

            close();

            searchOnAddress();
          }
        });
      } else {
        searchOnAddress();
      }
    } else if (isKeyCode(button, keyCode, KeyCode.F3, buttonToonBuiten)) {

      searchOnPersonOnly();
    } else if (isKeyCode(button, keyCode, KeyCode.F4, buttonNp)) {

      if (residentTable.isSelectedRecords()) {
        selectToest(residentTable.getSelectedRecord());
      } else {
        if (residentTable.getRecords().isEmpty()) {
          throw new ProException(WARNING, "Zoek en selecteer eerst een bewoner.");
        }
        throw new ProException(WARNING, "Selecteer eerst een bewoner.");
      }
    } else if (isKeyCode(button, keyCode, KeyCode.F5, buttonNnp)) {
      getParentWindow()
          .addWindow(new NonNaturalPersonWindow(relationship.getOtherConsentProvider(), this::setOtherConsentProvider));
    } else if (buttonShowInfo == button) {
      searchStatus();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    if (residentTable.isSelectedRecords()) {

      getParentWindow().addWindow(
          new ConfirmDialog("Wilt u de laatste terugmelding van de geselecteerde bewoners verwijderen?", 350) {

            @Override
            public void buttonYes() {

              for (final Relatie relatie : residentTable.getAllValues(Relatie.class)) {

                final ZaakArgumenten z = new ZaakArgumenten(relatie.getPl(), INCOMPLEET, OPGENOMEN, INBEHANDELING);
                final TerugmeldingService db = getServices().getTerugmeldingService();
                final List<TerugmeldingAanvraag> zaken = db.getMinimalZaken(z);
                if (!zaken.isEmpty()) {
                  db.delete(zaken.get(0));
                }
              }

              residentTable.showResidents();

              super.buttonYes();
            }
          });
    } else {
      throw new ProException(WARNING, "Geen personen geselecteerd");
    }
  }

  @Override
  public void onNextPage() {

    onPreviousPage();

    super.onNextPage();
  }

  @Override
  public void onSave() {

    findResidentForm.commit();
    feedbackForm.commit();

    if (!residentTable.getRecords().isEmpty()) {

      if (!residentTable.getSelectedRecords().isEmpty()) {

        for (final Record r : residentTable.getSelectedRecords()) {

          final Relatie relatie = (Relatie) r.getObject();
          final Cat1PersoonExt persoon = relatie.getPl().getPersoon();

          // Terugmelding
          final TerugmeldingAanvraag tmv = (TerugmeldingAanvraag) getServices().getTerugmeldingService().getNewZaak();
          tmv.setAnummer(new AnrFieldValue(persoon.getAnr().getVal()));
          tmv.setBurgerServiceNummer(new BsnFieldValue(persoon.getBsn().getVal()));
          tmv.setIngevoerdDoor(new UsrFieldValue(getApplication().getServices().getGebruiker()));
          tmv.setTerugmelding(feedbackForm.getBean().getTerugmelding());

          getServices().getTerugmeldingService().save(tmv);
        }

        residentTable.showResidents();
      } else {
        throw new ProException(WARNING, "Geen bewoners geselecteerd");
      }
    } else {

      throw new ProException(WARNING, "Zoek eerst de bewoners");
    }

    super.onSave();
  }

  private void add(VerticalLayout v, Button button) {

    button.setWidth("100%");
    button.addListener(this);
    v.addComponent(button);
    v.setComponentAlignment(button, Alignment.TOP_CENTER);
  }

  private void setToestemminggever(Relatie relatie) {
    relationship = consentProvider(relatie);
    relationshipListener.select(relationship);
    residentTable.showResidents();

    updateConsentProvider(relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
  }

  private void searchStatus() {
    residentTable.setShowStatus(true);
    residentTable.showResidents();
  }

  private void selectToest(Record record) {
    setToestemminggever((Relatie) record.getObject());
  }

  private void updateConsentProvider(String consentProverder) {
    consentProviderForm.getBean().setToestemmingGever(consentProverder);
    consentProviderForm.repaint();
  }

  private void updateRelationship() {
    if (relationship.getBrpConsentProvider() != null) {
      updateConsentProvider(
          relationship.getBrpConsentProvider().getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
    } else {
      updateConsentProvider(relationship.getOtherConsentProvider());
    }
  }

  private void searchOnAddress() {
    findResidentForm.commit();
    final ResidentPageBean bean = findResidentForm.getBean();

    final PLEArgs args = new PLEArgs();
    args.addNummer(astr(bean.getBsn().getValue()));
    args.setGeboortedatum(astr(bean.getGeboortedatum().getValue()));
    args.setGeslachtsnaam(bean.getGeslachtsnaam());

    args.setHuisnummer(destinationAddress.getAddress().getHnr());
    args.setHuisletter(destinationAddress.getAddress().getHnrL());
    args.setHuisnummertoevoeging(destinationAddress.getAddress().getHnrT());
    args.setAanduiding(destinationAddress.getAddress().getHnrA());
    args.setPostcode(destinationAddress.getAddress().getPostalCode());
    args.setSearchOnAddress(true);

    residentTable.setRelations(searchResidents.apply(args));
  }

  private void searchOnPersonOnly() {
    findResidentForm.commit();

    final ResidentPageBean bean = findResidentForm.getBean();

    final PLEArgs args = new PLEArgs();
    args.addNummer(astr(bean.getBsn().getValue()));
    args.setGeboortedatum(astr(bean.getGeboortedatum().getValue()));
    args.setGeslachtsnaam(bean.getGeslachtsnaam());

    residentTable.setRelations(searchResidents.apply(args));
  }

  private void setOtherConsentProvider(String otherConsentProvider) {
    relationship = otherConsentProvider(otherConsentProvider);
    relationshipListener.select(relationship);
    updateConsentProvider(otherConsentProvider);
  }

  public class ResidentTable extends ResidentPageTable {

    @Setter
    private boolean showStatus;

    private List<Relatie> relations = new ArrayList<>();

    @Override
    public void onDoubleClick(Record record) {
      getParentWindow().addWindow(new FeedbackDialog(this, (Relatie) record.getObject()));
      super.onClick(record);
    }

    void setRelations(List<Relatie> relations) {
      this.relations = unmodifiableList(relations);
      showResidents();
    }

    void showResidents() {
      getRecords().clear();

      try {
        if (relations.isEmpty()) {
          throw new ProException(SELECT, "Er zijn geen bewoners gevonden.");
        }
        addResidentRecords();
      } finally {
        reloadRecords();
        setShowStatus(false);
      }
    }

    private void addResidentRecords() {
      int nr = 0;
      for (final Relatie relatie : relations) {
        nr++;
        addResidentRecord(nr, relatie);
      }
    }

    private void addResidentRecord(int nr, Relatie relatie) {
      final BasePLExt bpl = relatie.getPl();
      final Record r = addRecord(relatie);
      String naam = relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();

      if (showStatus) {
        final String status = PersoonslijstStatus.getStatus(getServices(), relatie.getPl());
        if (fil(status)) {
          naam += setClass(GbaWebTheme.TEXT.RED, " (" + trim(status) + ")");
        } else {
          naam += " <b>(Geen blokkeringen en lopenden zaken)</b>";
        }
      }

      r.addValue(nr);
      r.addValue(naam);

      final Relatie toestemmingGever = relationship.getBrpConsentProvider();

      if (toestemmingGever != null && getBsn(toestemmingGever).equalsIgnoreCase(getBsn(relatie))) {
        r.addValue("Toestemminggever");
      } else {
        r.addValue("");
      }

      final ZaakArgumenten z = new ZaakArgumenten(bpl, INCOMPLEET, OPGENOMEN, INBEHANDELING);
      final int aantal = getServices().getTerugmeldingService().getZakenCount(z);

      r.addValue(pos(aantal) ? "<b>" + aantal + "</b>" : astr(aantal));
      r.addValue(relatie.getRelatieType().getOms());
      r.addValue(relatie.getPl().getPersoon().getGeslacht().getDescr());
      r.addValue(relatie.getPl().getPersoon().getGeboorte().getDatumLeeftijd());
    }

    private String getBsn(Relatie relatie) {
      return relatie.getPl().getPersoon().getBsn().getVal();
    }

  }

}

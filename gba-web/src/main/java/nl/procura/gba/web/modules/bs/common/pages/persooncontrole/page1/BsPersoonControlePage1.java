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

package nl.procura.gba.web.modules.bs.common.pages.persooncontrole.page1;

import static java.util.Collections.singletonList;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdenUtils.getOntbrekendePersonen;
import static nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdenUtils.getTypePersonen;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER1;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER2;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import java.util.*;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.pages.persooncontrole.BsPersoonControleCompareWindow;
import nl.procura.gba.web.modules.bs.common.pages.persooncontrole.DossierPersoonSynchronizer;
import nl.procura.gba.web.modules.bs.common.pages.persooncontrole.DossierPersoonSynchronizer.Element;
import nl.procura.gba.web.modules.bs.huwelijk.processen.HuwelijkProcessen;
import nl.procura.gba.web.modules.bs.omzetting.processen.OmzettingProcessen;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

/**
 * Controle op persoonsgegevens
 */
public abstract class BsPersoonControlePage1 extends ButtonPageTemplate {

  private final Dossier            dossier;
  private Consumer<DossierPersoon> changeListener;
  private final Label              updateLabel = new Label("", Label.CONTENT_XML);
  private PersoonTable             table       = null;
  private List<SyncRecord>         records     = null;

  //  private final String            fakeVoornaam   = asList ("Klaas", "Piet", "Jan").get (new Random ().nextInt (3));
  //  private final GbaDateFieldValue fakeOverlDatum = new Random ().nextInt (2) == 1 ? new GbaDateFieldValue (20170101) : null;

  public BsPersoonControlePage1(Dossier dossier, Consumer<DossierPersoon> changeListener) {
    this.dossier = dossier;
    this.changeListener = changeListener;

    setSpacing(true);
    buttonClose.addListener(this);
    buttonNext.addListener(this);

    H2 h2 = new H2("Controle op persoonsgegevens");
    HLayout hLayout = new HLayout().widthFull().add(h2).add(buttonNext, buttonClose);
    hLayout.setExpandRatio(h2, 1f);
    hLayout.setComponentAlignment(buttonClose, Alignment.MIDDLE_RIGHT);
    hLayout.setComponentAlignment(buttonNext, Alignment.MIDDLE_RIGHT);

    addComponent(hLayout);

    checkUpdateStatus();

    buttonNext.setCaption("Naar de zaak (F2)");
    buttonNext.setWidth("130px");

    buttonClose.setCaption("Sluiten (Esc)");
    buttonClose.setWidth("130px");

    buttonSearch.setCaption("Controleren (F3)");
    buttonSearch.setWidth("160px");

    buttonSave.setCaption("Bijwerken (F9)");
    buttonSave.setWidth("160px");
    buttonSave.setEnabled(false);

    addButton(buttonSearch);
    addButton(buttonSave);
    updateLabel.setSizeUndefined();
    getButtonLayout().addComponent(updateLabel);
    getButtonLayout().setComponentAlignment(updateLabel, Alignment.MIDDLE_RIGHT);
    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(buttonSave, 1f);
  }

  public abstract void afterBijwerken();

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      setInfo("De persoonsgegevens kunnen gewijzigd zijn sinds de laatste keer dat deze zaak is aangepast. "
          + "Met deze controle kunt u de persoonsgegevens controleren. "
          + "</br>Alleen personen met een valide burgerservicenummer worden gecontroleerd.");

      setTable(new PersoonTable());
      addComponent(getTable());
    }

    super.event(event);
  }

  public PersoonTable getTable() {
    return table;
  }

  public void setTable(PersoonTable table) {
    this.table = table;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  public abstract void onGoToZaak();

  @Override
  public void onNextPage() {

    String msg = "";
    if (!isGecontroleerd()) {
      msg = "Niet alle gegevens zijn gecontroleerd.";
    } else if (isBijwerkenNodig()) {
      msg = "Niet alle gegevens zijn bijgewerkt.";
    }

    if (fil(msg)) {
      // Waarschuwing dat niet alle gegevens zijn bijgewerkt.
      ConfirmDialog confirmDialog = new ConfirmDialog(msg + "</br> Wilt u toch doorgaan naar de zaak?") {

        @Override
        public void buttonYes() {
          onGoToZaak();
          super.buttonYes();
        }
      };
      getParentWindow().addWindow(confirmDialog);
    } else {
      onGoToZaak();
    }
  }

  @Override
  public void onSave() {
    for (SyncRecord record : getSelectedValues()) {
      if (Status.ONTBREEKT == record.getStatus()) {
        record.getParentPersoon().toevoegenPersonen(singletonList(record.getHuidigePersoon()));
        changeListener.accept(record.getHuidigePersoon());
        record.setStatus(Status.TOEGEVOEGD);

      } else if (Status.VERSCHIL == record.getStatus()) {
        DossierPersoonSynchronizer sync = record.getSynchronizer();
        if (sync != null) {
          BsPersoonUtils.kopieDossierPersoon(sync.getNieuwePersoon(), sync.getHuidigePersoon());
        }
        changeListener.accept(sync.getNieuwePersoon());
        record.setStatus(Status.BIJGEWERKT);
      }
    }

    // Commentaar toevoegen met mogelijke waarschuwingen
    ZaakCommentaren commentaar = dossier.getCommentaren();
    List<String> messages = new ArrayList<>();
    if (dossier.getZaakDossier() instanceof DossierHuwelijk) {
      messages.addAll(HuwelijkProcessen.getProcesStatussen(dossier).getMessages());
    } else if (dossier.getZaakDossier() instanceof DossierOmzetting) {
      messages.addAll(OmzettingProcessen.getProcesStatussen(dossier).getMessages());
    }

    commentaar.verwijderen().toevoegenWarn(messages);

    // Opslaan zaak
    getServices().getZakenService().getService(dossier).save(dossier);
    table.init();
    afterBijwerken();
    super.onSave();
  }

  @Override
  public void onSearch() {
    for (SyncRecord record : getSelectedValues()) {
      DossierPersoon persoon = record.getHuidigePersoon();
      if (record.getStatus() != Status.ONTBREEKT) {
        if (persoon.getBurgerServiceNummer().isCorrect()) {
          DossierPersoon nieuwePersoon = getNieuwPersoon(persoon);
          DossierPersoonSynchronizer synchronizer = new DossierPersoonSynchronizer(persoon, nieuwePersoon);
          record.setSynchronizer(synchronizer);
          if (synchronizer.isPersoonGevonden()) {
            if (synchronizer.isMatch()) {
              record.setStatus(Status.UP_TO_DATE);
            } else {
              record.setStatus(Status.VERSCHIL);
            }
          } else {
            record.setStatus(Status.GEEN_PERSOON_GEVONDEN);
          }
        } else {
          record.setStatus(Status.GEEN_BSN);
        }
      }
    }

    table.init();
  }

  private void checkUpdateStatus() {
    buttonSave.setEnabled(isBijwerkenNodig());
    String msg;
    if (isGecontroleerd()) {
      if (isBijwerkenNodig()) {
        msg = setClass(false, "De gegevens moeten worden bijgewerkt.");
      } else {
        msg = setClass(true, "De gegevens zijn up-to-date.");
      }
    } else {
      msg = "De gegevens zijn nog niet gecontroleerd.";
    }

    updateLabel.setValue(new ObjectProperty<>(msg, String.class));
  }

  private List<SyncRecord> getNewSyncRecords() {
    List<SyncRecord> newRecords = new ArrayList<>();
    // Eerst de personen in zaak
    for (DossierPersoon persoon : dossier.getPersonen()) {
      newRecords.add(new SyncRecord(persoon));
    }

    // Dan de personen in die relaties zijn van de personen in de zaak
    for (DossierPersoon persoon : dossier.getPersonen()) {
      for (DossierPersoon relatie : persoon.getAllePersonen()) {
        newRecords.add(new SyncRecord(relatie, persoon, Status.NIET_GECONTROLEERD));
      }
    }

    // Ontbrekende personen toevoegen
    for (DossierPersoon persoon : dossier.getPersonen(PARTNER1, PARTNER2)) {
      for (DossierPersoon ontbPersoon : getOntbrekendePersonen(getApplication().getServices(), persoon)) {
        newRecords.add(new SyncRecord(ontbPersoon, persoon, Status.ONTBREEKT));
      }
    }

    return newRecords;
  }

  /**
   * Zoek de persoon op basis van de huidige persoon
   */
  private DossierPersoon getNieuwPersoon(DossierPersoon persoon) {
    BsnFieldValue bsn = persoon.getBurgerServiceNummer();
    if (bsn != null && pos(bsn.getValue())) {
      BasePLExt basisPl = getServices().getPersonenWsService().getPersoonslijst(bsn.getStringValue());
      if (basisPl != null && !basisPl.getCats().isEmpty()) {
        DossierPersoon newPersoon = new DossierPersoon();
        newPersoon.setDossierPersoonType(persoon.getDossierPersoonType());
        return BsPersoonUtils.kopieDossierPersoon(basisPl, newPersoon);
      }
    }

    return new DossierPersoon();
  }

  /**
   * Geeft de geselecteerde regels terug en als er niets is geselecteerd dan worden alle regels teruggegeven.
   */
  private List<SyncRecord> getSelectedValues() {
    List<SyncRecord> selectedRecords = new ArrayList<>();
    if (table.isSelectedRecords()) {
      selectedRecords.addAll(table.getSelectedValues(SyncRecord.class));
    } else {
      selectedRecords.addAll(table.getAllValues(SyncRecord.class));
    }
    return selectedRecords;
  }

  private Object getStatusIcon(Status status) {
    int icon = Icons.ICON_EMPTY;
    if (status != null) {
      switch (status) {
        case UP_TO_DATE:
        case BIJGEWERKT:
        case TOEGEVOEGD:
          icon = Icons.ICON_OK;
          break;

        case VERSCHIL:
        case GEEN_PERSOON_GEVONDEN:
        case ONTBREEKT:
          icon = Icons.ICON_WARN;
          break;

        case GEEN_BSN:
        case NIET_GECONTROLEERD:
        default:
          break;
      }
    }
    return new Embedded("", new ThemeResource(Icons.getIcon(icon)));
  }

  private String getStatusOmschrijving(Status status) {

    if (status == null) {
      return "";
    }

    switch (status) {
      case GEEN_BSN:
        return "Geen BSN. Controle is niet mogelijk.";

      case UP_TO_DATE:
        return setClass(true, "Gegevens zijn up-to-date.");

      case BIJGEWERKT:
        return setClass(true, "Gegevens zijn bijgewerkt.");

      case TOEGEVOEGD:
        return setClass(true, "Persoon toegevoegd.");

      case VERSCHIL:
        buttonSave.setEnabled(true);
        return setClass(false, "Gegevens zijn niet up-to-date.");

      case GEEN_PERSOON_GEVONDEN:
        return setClass(false, "Geen persoon gevonden.");

      case ONTBREEKT:
        return setClass(false, "Persoon ontbreekt.");

      case NIET_GECONTROLEERD:
      default:
        return "Nog geen controle uitgevoerd.";
    }
  }

  /**
   * Is er een update nodig?
   */
  private boolean isBijwerkenNodig() {
    if (records != null) {
      return records.stream()
          .anyMatch(record -> Status.VERSCHIL.equals(record.getStatus())
              || record.getStatus() == Status.ONTBREEKT);
    }
    return false;
  }

  /**
   * Is er een controle uitgevoerd?
   */
  private boolean isGecontroleerd() {
    if (records != null) {
      return records.stream()
          .noneMatch(record -> Status.NIET_GECONTROLEERD.equals(record.getStatus()));
    }
    return true;
  }

  public enum Status {
    NIET_GECONTROLEERD,
    ONTBREEKT,
    GEEN_BSN,
    UP_TO_DATE,
    GEEN_PERSOON_GEVONDEN,
    VERSCHIL,
    BIJGEWERKT,
    TOEGEVOEGD
  }

  public class PersoonTable extends GbaTable {

    public PersoonTable() {
      setSelectable(true);
      setMultiSelect(true);
    }

    @Override
    public void onDoubleClick(Record record) {

      SyncRecord syncRecord = record.getObject(SyncRecord.class);
      if (syncRecord.isChecked()) {
        getParentWindow().addWindow(new BsPersoonControleCompareWindow(syncRecord));
      } else {
        throw new ProException(INFO, "Geen controle uitgevoerd.");
      }

      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      addColumn("&nbsp;", 15);
      addColumn("Status", 250).setUseHTML(true);
      addColumn("Type", 140);
      addColumn("Naam", 300);
      addColumn("Wijzigingen");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      if (records == null) {
        records = new ArrayList<>();
        records.addAll(getNewSyncRecords());
      }

      // Sorteren

      for (SyncRecord syncRecord : records) {

        DossierPersoon persoon = syncRecord.getHuidigePersoon();
        String naam = BsPersoonUtils.getNaam(persoon);

        Record record = addRecord(syncRecord);
        record.addValue(getStatusIcon(syncRecord.getStatus()));
        record.addValue(getStatusOmschrijving(syncRecord.getStatus()));
        record.addValue(syncRecord.getTypeOmschrijving());
        record.addValue(fil(naam) ? naam : "Nog niet aangegeven");
        record.addValue(syncRecord.getWijzigingen());
      }

      checkUpdateStatus();

      super.setRecords();
    }

  }

  public class SyncRecord {

    private Status               status;
    private final DossierPersoon huidigePersoon;

    private String                     typeOmschrijving;
    private DossierPersoon             parentPersoon;
    private DossierPersoonSynchronizer synchronizer;

    public SyncRecord(DossierPersoon huidigePersoon) {
      this(huidigePersoon, null, Status.NIET_GECONTROLEERD);
    }

    public SyncRecord(DossierPersoon huidigePersoon, DossierPersoon parentPersoon, Status status) {
      this.huidigePersoon = huidigePersoon;
      this.parentPersoon = parentPersoon;
      this.status = status;

      if (status == Status.NIET_GECONTROLEERD && !huidigePersoon.getBurgerServiceNummer().isCorrect()) {
        this.status = Status.GEEN_BSN;
      }

      if (parentPersoon != null) {
        typeOmschrijving = parentPersoon.getDossierPersoonType().getDescr()
            + " - " + huidigePersoon.getDossierPersoonType().getDescr();
      } else {
        typeOmschrijving = huidigePersoon.getDossierPersoonType().getDescr();
      }
    }

    public DossierPersoon getHuidigePersoon() {
      return huidigePersoon;
    }

    public DossierPersoon getParentPersoon() {
      return parentPersoon;
    }

    public Status getStatus() {
      return status;
    }

    public void setStatus(Status status) {
      this.status = status;
    }

    public DossierPersoonSynchronizer getSynchronizer() {
      return synchronizer;
    }

    public void setSynchronizer(DossierPersoonSynchronizer synchronizer) {
      this.synchronizer = synchronizer;
    }

    public String getTypeOmschrijving() {
      return typeOmschrijving;
    }

    public String getWijzigingen() {

      Set<String> changes = new LinkedHashSet<>();
      StringBuilder out = new StringBuilder();

      if (synchronizer != null && synchronizer.isPersoonGevonden()) {
        for (Element element : synchronizer.getElementen()) {
          if (!element.isMatch()) {
            changes.add(element.getNaam());
          }
        }
      }

      if (changes.isEmpty()) {
        out.append("geen");
      } else {
        for (String naam : changes) {
          out.append(naam);
          out.append(", ");
        }
      }

      return StringUtils.capitalize(StringUtils.abbreviate(trim(out.toString()), 80).toLowerCase());
    }

    public boolean isChecked() {
      return synchronizer != null;
    }
  }
}

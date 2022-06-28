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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page3;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.modules.hoofdmenu.klapper.page3.Page3KlapperBean2.*;
import static nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteInvoerType.BLANCO;
import static nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteInvoerType.ONBEKEND;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.hoofdmenu.klapper.PageKlapperTemplate;
import nl.procura.gba.web.modules.hoofdmenu.klapper.page1.Page1Klapper;
import nl.procura.gba.web.modules.hoofdmenu.klapper.windows.KlapperZoekWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page100.Page100Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page110.Page110Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page120.Page120Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page130.Page130Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page140.Page140Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page150.Page150Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page200.Page200Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page210.Page210Zaken;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.*;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Bsn;

public class Page3Klapper extends PageKlapperTemplate {

  protected final Button buttonCheck  = new Button("Controle (F3)");
  protected final Button buttonChange = new Button("Wijzigen");

  private final DossierAkte           dossierAkte;
  private final List<CommittableForm> forms = new ArrayList<>();
  private Page3KlapperForm1           form;
  private final boolean               muteerbaar;

  public Page3Klapper(DossierAkte dossierAkte, boolean muteerbaar) {
    super("Klapper - inzage");
    this.dossierAkte = dossierAkte;
    this.muteerbaar = muteerbaar;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (getWindow().isModal()) {
        addButton(buttonClose);

      } else {
        addButton(buttonPrev);

        if (muteerbaar) {
          addButton(buttonCheck);
          addButton(buttonSave);

        } else {
          addButton(buttonNew);
          addButton(buttonChange);

          if (dossierAkte.isStored() && dossierAkte.heeftDossier()) {
            buttonNext.setCaption("Toon zaak (F2)");
            addButton(buttonNext);
          }

          addButton(buttonChange);

          if (!dossierAkte.isDossierCorrect()) {
            setInfo("Deze klapper zou gekoppeld moeten zijn aan een dossier in Proweb Personen.");

          } else if (!dossierAkte.isDossierVerwerkt()) {
            setInfo("De zaak die gekoppeld is aan deze klapper is nog niet verwerkt.");
          }
        }
      }

      resetDefaultForm();
      resetExtraForms();

      if (!muteerbaar) {
        List<DossierAkte> aktes = new ArrayList<>();
        if (fil(dossierAkte.getAkteGroepId())) {
          KlapperZoekargumenten zoekargumenten = new KlapperZoekargumenten();
          zoekargumenten.setAkteGroepId(dossierAkte.getAkteGroepId());
          aktes.addAll(getServices().getAkteService()
              .getAktes(zoekargumenten).stream()
              .filter(d -> !d.getCode().equals(dossierAkte.getCode()))
              .collect(Collectors.toList()));
        }
        addComponent(new KlapperKoppelLayout(aktes, da -> getNavigation()
            .goToPage(new Page3Klapper(da, false))));
      }
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (isKeyCode(button, keyCode, KeyCode.F3, buttonCheck)) {
      onCheck();
    } else if (isKeyCode(button, keyCode, -1, buttonChange)) {
      onChange();
    }

    super.handleEvent(button, keyCode);
  }

  public void onChange() {
    getNavigation().removePage(Page3Klapper.class);
    getNavigation().goToPage(new Page3Klapper(dossierAkte, true));
  }

  public void onCheck() {
    check(getAkteNummer());
    successMessage("Dit volgnummer is te gebruiken");
  }

  @Override
  public void onClose() {
    if (getWindow().isModal()) {
      getWindow().closeWindow();
    }
    super.onClose();
  }

  @Override
  public void onNew() {
    getNavigation().removePage(Page3Klapper.class);
    getNavigation().goToPage(new Page3Klapper(new DossierAkte(new DateTime()), true));
    super.onNew();
  }

  @Override
  public void onNextPage() {
    if (dossierAkte.isDossierCorrect()) {
      ZakenService db = getServices().getZakenService();
      List<Zaak> zaken = db.getVolledigeZaken(db.getMinimaleZaken(new ZaakArgumenten(dossierAkte.getZaakId())));

      for (Zaak zaak : zaken) {
        if (zaak instanceof Dossier) {
          Dossier dossier = to(zaak, Dossier.class);
          ZaakType zaakType = zaak.getType();

          switch (zaakType) {
            case ERKENNING:
              getNavigation().goToPage(new Page150Zaken(dossier));
              break;

            case GEBOORTE:
              getNavigation().goToPage(new Page100Zaken(dossier));
              break;

            case HUWELIJK_GPS_GEMEENTE:
              getNavigation().goToPage(new Page110Zaken(dossier));
              break;

            case OMZETTING_GPS:
              getNavigation().goToPage(new Page210Zaken(dossier));
              break;

            case LEVENLOOS:
              getNavigation().goToPage(new Page200Zaken(dossier));
              break;

            case LIJKVINDING:
              getNavigation().goToPage(new Page130Zaken(dossier));
              break;

            case OVERLIJDEN_IN_BUITENLAND:
              getNavigation().goToPage(new Page140Zaken(dossier));
              break;

            case OVERLIJDEN_IN_GEMEENTE:
              getNavigation().goToPage(new Page120Zaken(dossier));
              break;

            default:
              throw new ProException(WARNING, "Deze dossier is onvolledig en kan niet worden geladen.");
          }
        }

        break;
      }
    }

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(Page1Klapper.class);
  }

  @Override
  public void onSave() {

    final DossierAkteNummer akteNummer = getAkte();

    try {
      check(akteNummer);
      doSave(akteNummer);
    } catch (ProException e) {
      StringBuilder message = new StringBuilder(e.getMessage());
      message.append("<hr/>Wilt u toch dit record opslaan?");
      ConfirmDialog confirmDialog = new ConfirmDialog(message.toString(), 500) {

        @Override
        public void buttonYes() {
          doSave(akteNummer);
          super.buttonYes();
        }
      };

      getParentWindow().addWindow(confirmDialog);
    }

    super.onSave();
  }

  public void resetExtraForms() {

    for (CommittableForm form : forms) {
      removeComponent(form);
    }

    forms.clear();

    if (form.getInvoerType() == null || BLANCO.is(form.getInvoerType()) || ONBEKEND.is(form.getInvoerType())) {
      // Geen verdere formulieren tonen
      return;
    }

    DossierAkteRegistersoort soort = form.getSoort();

    if (soort != null) {
      switch (soort) {
        case AKTE_ERKENNING_NAAMSKEUZE:
          addForm(new Form(soort, muteerbaar, "Erkenning", DATUM_ERKENNING));
          addForm(new PersoonForm(soort, muteerbaar, "Kind", false));
          break;

        case AKTE_GEBOORTE:
          addForm(new Form(soort, muteerbaar, "Geboorte", DATUM_GEBOORTE));
          addForm(new PersoonForm(soort, muteerbaar, "Kind", false));
          break;

        case AKTE_GPS:
          addForm(new Form(soort, muteerbaar, "Geregistreerd partnerschap", DATUM_HUWELIJK));
          addForm(new PersoonForm(soort, muteerbaar, "Partner 1", false));
          addForm(new PersoonForm(soort, muteerbaar, "Partner 2", true));
          break;

        case AKTE_HUWELIJK:
          addForm(new Form(soort, muteerbaar, "Huwelijk", DATUM_HUWELIJK));
          addForm(new PersoonForm(soort, muteerbaar, "Partner 1", false));
          addForm(new PersoonForm(soort, muteerbaar, "Partner 2", true));
          break;

        case AKTE_OVERLIJDEN:
          addForm(new Form(soort, muteerbaar, "Overlijden / lijkvinding", DATUM_OVERLIJDEN));
          addForm(new PersoonForm(soort, muteerbaar, "Persoon", false));
          break;

        case AKTE_ONBEKEND:
        default:
          break;
      }
    }
  }

  private <T extends CommittableForm> T addForm(T form) {

    CommittableForm component = form;

    if (form instanceof PersoonForm) {
      component = new PersoonLayout((PersoonForm) form);
    }

    addComponent(component);
    forms.add(component);

    return form;
  }

  private boolean check(DossierAkteNummer akteNummer) {
    getServices().getAkteService().isCheck(dossierAkte, akteNummer, null);
    return true;
  }

  private void doSave(DossierAkteNummer akteNummer) {
    getServices().getAkteService().save(null, dossierAkte, akteNummer, true);
    successMessage("De gegevens zijn opgeslagen");

    getNavigation().removePage(Page3Klapper.class);
    getNavigation().goToPage(new Page3Klapper(dossierAkte, false));
  }

  private DossierAkteNummer getAkte() {

    DossierAkteNummer akteNummer = getAkteNummer();

    for (CommittableForm form : forms) {
      form.commit();
    }

    dossierAkte.setInvoerType(form.getInvoerType());
    dossierAkte.setDatumIngang(new DateTime(form.getDatum()));
    dossierAkte.setOpm(form.getBean().getOpm());

    for (CommittableForm form : forms) {
      updateDatumFeit(form, DATUM_GEBOORTE);
      updateDatumFeit(form, DATUM_HUWELIJK);
      updateDatumFeit(form, DATUM_ERKENNING);
      updateDatumFeit(form, DATUM_OVERLIJDEN);

      if (form instanceof PersoonLayout) {
        PersoonLayout persoonLayout = (PersoonLayout) form;
        PersoonForm persoonForm = persoonLayout.getForm();

        if (persoonForm.isPartner()) {
          dossierAkte.getAktePartner().setBurgerServiceNummer(persoonForm.getBean().getBsn2());
          dossierAkte.getAktePartner().setVoornaam(persoonForm.getBean().getVoorn2());
          dossierAkte.getAktePartner().setVoorvoegsel(
              Optional.ofNullable(persoonForm.getBean().getVoorv2()).map(
                  FieldValue::getStringValue).orElse(""));
          dossierAkte.getAktePartner().setGeslachtsnaam(persoonForm.getBean().getNaam2());
          dossierAkte.getAktePartner().setGeslacht(persoonForm.getBean().getGeslacht2());
          dossierAkte.getAktePartner().setGeboortedatum(
              new GbaDateFieldValue(persoonForm.getBean().getGeboortedatum2().getLongValue()));
        } else {
          dossierAkte.getAktePersoon().setBurgerServiceNummer(persoonForm.getBean().getBsn1());
          dossierAkte.getAktePersoon().setVoornaam(persoonForm.getBean().getVoorn1());
          dossierAkte.getAktePersoon().setVoorvoegsel(
              Optional.ofNullable(persoonForm.getBean().getVoorv1()).map(
                  FieldValue::getStringValue).orElse(""));
          dossierAkte.getAktePersoon().setGeslachtsnaam(persoonForm.getBean().getNaam1());
          dossierAkte.getAktePersoon().setGeslacht(persoonForm.getBean().getGeslacht1());
          dossierAkte.getAktePersoon().setGeboortedatum(
              new GbaDateFieldValue(persoonForm.getBean().getGeboortedatum1().getLongValue()));
        }
      }
    }

    return akteNummer;
  }

  private DossierAkteNummer getAkteNummer() {

    form.commit();

    long datum = form.getDatum();
    DossierAkteDeel deel = form.getDeel();
    long nummer = form.getNummer();

    return new DossierAkteNummer(datum, deel, nummer);
  }

  private void resetDefaultForm() {

    if (form != null) {
      removeComponent(form);
    }

    form = new Page3KlapperForm1(dossierAkte, muteerbaar, getApplication()) {

      @Override
      public void checkVolgNummer() {
        check(getAkteNummer());
        successMessage("Dit aktenummer is te gebruiken");
      }

      @Override
      public void resetForms() {
        resetExtraForms();
      }
    };

    addComponent(form);

    buttonCheck.setVisible(form.isMuteerbaar());
    buttonSave.setVisible(form.isMuteerbaar());
  }

  /**
   * Wijzig datum feit
   */
  private void updateDatumFeit(CommittableForm form, String datum) {
    Field datumVeld = form.getForm().getField(datum);

    if (datumVeld != null) {
      dossierAkte.setDatumFeit(
          new GbaDateFieldValue(new DateFieldValue(astr(datumVeld.getValue())).getLongValue()));
    }
  }

  public interface CommittableForm extends Component {

    void commit();

    Page3KlapperForm getForm();
  }

  public class Form extends Page3KlapperForm2 implements CommittableForm {

    public Form(DossierAkteRegistersoort soort, boolean muteerbaar, String caption, String... order) {
      super(soort, dossierAkte, muteerbaar, caption, order);
    }

    @Override
    public Page3KlapperForm getForm() {
      return this;
    }
  }

  public class PersoonForm extends Page3KlapperForm2 implements CommittableForm {

    private final boolean isPartner;

    public PersoonForm(DossierAkteRegistersoort soort, boolean muteerbaar, String caption, boolean isPartner) {

      super(soort, dossierAkte, muteerbaar, caption);

      this.isPartner = isPartner;

      setColumnWidths(WIDTH_130, "300px", "130px", "");

      String[] persoon1 = new String[]{ VOORN1, BSN1, VOORV1, GESLACHT1, NAAM1, GEBOORTEDATUM1 };
      String[] persoon2 = new String[]{ VOORN2, BSN2, VOORV2, GESLACHT2, NAAM2, GEBOORTEDATUM2 };

      setOrder(isPartner ? persoon2 : persoon1);

      setBean(getBean());
    }

    @Override
    public Page3KlapperForm getForm() {
      return this;
    }

    public boolean isPartner() {
      return isPartner;
    }

    @Override
    public void onButtonPersoon(Bsn bsn) {
      getApplication().goToPl(getWindow(), "", PLEDatasource.STANDAARD, bsn.getDefaultBsn());
    }
  }

  public class PersoonLayout extends OptieLayout implements ClickListener, CommittableForm {

    private final PersoonForm form;
    private final Button      buttonId;
    private final Button      buttonZoek;

    public PersoonLayout(PersoonForm form) {

      this.form = form;

      getLeft().addComponent(form);

      getRight().setWidth("200px");
      getRight().setCaption("Opties");

      buttonId = new Button("Identificeren");
      buttonZoek = new Button("Zoek persoon");

      getRight().addButton(buttonId, this);
      getRight().addButton(buttonZoek, this);
    }

    @Override
    public void buttonClick(ClickEvent event) {

      if (event.getButton() == buttonId) {

        form.commit();

        PLEArgs args = new PLEArgs();

        if (form.isPartner) {
          args.setGeslachtsnaam(form.getBean().getNaam2());
          args.setVoornaam(form.getBean().getVoorn2());
          args.setVoorvoegsel(astr(form.getBean().getVoorv2()));
          args.setGeslacht(form.getBean().getGeslacht2().getAfkorting());
          args.setGeboortedatum(form.getBean().getGeboortedatum2().getStringValue());
        } else {
          args.setGeslachtsnaam(form.getBean().getNaam1());
          args.setVoornaam(form.getBean().getVoorn1());
          args.setVoorvoegsel(astr(form.getBean().getVoorv1()));
          args.setGeslacht(form.getBean().getGeslacht1().getAfkorting());
          args.setGeboortedatum(form.getBean().getGeboortedatum1().getStringValue());
        }

        if (!args.isNawGevuld()) {
          throw new ProException(WARNING, "Geen zoekargumenten ingegeven");
        }

        List<BasePLExt> persoonslijsten = getServices().getPersonenWsService().getPersoonslijsten(args,
            false).getBasisPLWrappers();

        if (persoonslijsten.size() > 0) {

          getParentWindow().addWindow(new KlapperZoekWindow(persoonslijsten) {

            @Override
            public void update(BasePLExt pl) {

              BsnFieldValue bsn = new BsnFieldValue(pl.getPersoon().getBsn().getVal());

              if (form.isPartner) {

                form.getBean().setBsn2(bsn);
                dossierAkte.getAktePartner().setBurgerServiceNummer(bsn);
              } else {
                form.getBean().setBsn1(bsn);
                dossierAkte.getAktePersoon().setBurgerServiceNummer(bsn);
              }

              form.repaint();

              getServices().getAkteService().save(dossierAkte);

              successMessage("De akte is opgeslagen");
            }
          });
        } else {

          throw new ProException(WARNING, "Geen personen gevonden");
        }
      } else if (event.getButton() == buttonZoek) {

        BsnFieldValue bsn = form.getBean().getBsn1();

        if (form.isPartner) {
          bsn = form.getBean().getBsn2();
        }

        if (!bsn.isCorrect()) {
          throw new ProException(WARNING, "Geen burgerservicenummer ingegeven");
        }

        getApplication().goToPl(getParentWindow(), "pl.persoon", PLEDatasource.STANDAARD, astr(bsn.getValue()));
      }
    }

    @Override
    public void commit() {
      form.commit();
    }

    @Override
    public PersoonForm getForm() {
      return form;
    }
  }
}

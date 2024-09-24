/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static java.util.Optional.ofNullable;
import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.REISDOCUMENTAANVRAAG;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.AANVRAAGDATUM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.AANVRAAGNUMMER;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.ARTIKEL23B;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.AUTORITEIT_VAN_AFGIFTE;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.AUT_VAN_VERSTREK;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.BSN;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.DATUM_VERSTREKKING;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.DOC_NR;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.DOC_SOORT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.GEBOORTEDATUM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.GEBOORTEPLAATS;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.GELDIG_VOOR_LANDEN;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.GESLACHT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.GOEDKEURING_GELAAT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.INLEVERDATUM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.INLEVERINSTANTIE;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.INSTANTIE_LOCATIE;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.LENGTE;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.NAAM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.NATIONALITEIT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.NIET_INSTAAT_TOT_ONDERTEKENING;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.NIET_PERSOONLIJK_AANWEZIG;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.PARTNER;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.PSEUDONIEM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.REDEN_VINGER_AFWEZIG;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.SPOED;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.STAATLOOS;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.TVV;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.UITGEZONDERDE_LANDEN;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.VERBLIJFSDOCUMENT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.VERMELDING_PARTNER;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.VERMELDING_TAAL;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.VERZOCHT_EINDE_GELDIGHEID;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.vaadin.ui.Component;

import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponse;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseAanvraagactiviteit;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseAanvraagmelding;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseAanvraagstatus;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseAdresBinnenland;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseAdresBuitenland;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseAdresOngespecificeerd;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvragenResponse;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvragenResponseControleAanvraagBasic;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet;
import nl.procura.vaadin.theme.twee.ProcuraTheme;
import nl.procura.validation.Bsn;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class VrsAanvragenWindow extends GbaModalWindow {

  private final Aanvraagnummer            aanvraagnummer;
  private final ControleAanvragenResponse response;

  public VrsAanvragenWindow(Aanvraagnummer aanvraagnummer,
      ControleAanvragenResponse response) {
    super("Aanvragen in het aanvraagarchief van het Basisregister Reisdocumenten (Esc om te sluiten)", "1000px");
    this.aanvraagnummer = aanvraagnummer;
    this.response = response;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page1()));
  }

  public class Page1 extends NormalPageTemplate {

    public Page1() {
      addComponent(new InfoLayout("De aanvraaggegevens (lopend en afgerond) in het Basisregister reisdocumenten",
          "Klik op een regel voor meer detailgegevens van de aanvraag"));
      addComponent(new Table());
    }

    public class Table extends GbaTable {

      @Override
      public void setColumns() {
        setSelectable(true);
        addColumn("Nummer", 100);
        addColumn("Soort", 300);
        addColumn("Status");
        addColumn("Datum status", 100).setUseHTML(true);

        super.setColumns();
      }

      @Override
      public void setRecords() {
        List<ControleAanvragenResponseControleAanvraagBasic> reisdocumentenLijst = response
            .getAanvragen();
        for (ControleAanvragenResponseControleAanvraagBasic document : reisdocumentenLijst) {
          Record r = addRecord(document);
          r.addValue(new Aanvraagnummer(astr(document.getAanvraagnummer())).getFormatNummer());
          r.addValue(document.getAangevraagdReisdocumentsoort());
          r.addValue(document.getAanvraagstatus().getAanvraagstatusOmschrijving());
          r.addValue(DateTime.of(document.getAanvraagstatus().getBegindatum()));
        }
      }

      @Override
      public void onClick(Record record) {
        ControleAanvragenResponseControleAanvraagBasic rd = record
            .getObject(ControleAanvragenResponseControleAanvraagBasic.class);
        if (rd != null) {
          Services services = getApplication().getServices();
          services.getReisdocumentService()
              .getVrsService()
              .getAanvraag(new VrsRequest()
                  .aanleiding(REISDOCUMENTAANVRAAG)
                  .bsn(new Bsn(rd.getPersonalisatiegegevens().getBsn()))
                  .aanvraagnummer(rd.getAanvraagnummer().toString()))
              .ifPresent(response -> getNavigation().goToPage(new Page2(response)));
        }
      }
    }
  }

  public static class Page2 extends NormalPageTemplate {

    private final ControleAanvraagVolledigResponse                         response;
    private final List<ControleAanvraagVolledigResponseAanvraagstatus>     statussen;
    private final List<ControleAanvraagVolledigResponseAanvraagactiviteit> activiteiten;
    private final List<Adres>                                              adressen = new ArrayList<>();

    public Page2(ControleAanvraagVolledigResponse response) {
      this.response = response;
      this.statussen = response.getAanvraagstatussen();
      this.activiteiten = response.getAanvraagactiviteiten();
      addButton(buttonPrev);
    }

    @Override
    protected void initPage() {
      super.initPage();
      setStyleName(ProcuraTheme.AUTOSCROLL);
      setSpacing(true);

      LazyTabsheet tabs = new LazyTabsheet();
      tabs.addStyleName(GbaWebTheme.TABSHEET_BORDERLESS);
      tabs.addStyleName(GbaWebTheme.TABSHEET_LIGHT);
      tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);
      tabs.addStyleName("reisdoc-aanvragen");

      VrsAanvraagForm1 form1 = new VrsAanvraagForm1(response,
          DOC_NR, AANVRAAGNUMMER, AANVRAAGDATUM, DOC_SOORT, INSTANTIE_LOCATIE, ARTIKEL23B, NIET_PERSOONLIJK_AANWEZIG,
          AUT_VAN_VERSTREK, INLEVERINSTANTIE, INLEVERDATUM, VERZOCHT_EINDE_GELDIGHEID, REDEN_VINGER_AFWEZIG,
          GOEDKEURING_GELAAT, NIET_INSTAAT_TOT_ONDERTEKENING, AUTORITEIT_VAN_AFGIFTE);
      VrsAanvraagForm1 form2 = new VrsAanvraagForm1(response,
          NAAM, BSN, GEBOORTEDATUM, GEBOORTEPLAATS, NATIONALITEIT, GESLACHT, SPOED, DATUM_VERSTREKKING,
          LENGTE, PARTNER, PSEUDONIEM, VERMELDING_PARTNER, VERBLIJFSDOCUMENT, VERMELDING_TAAL, TVV,
          UITGEZONDERDE_LANDEN, GELDIG_VOOR_LANDEN, STAATLOOS);

      int statussen = ofNullable(response.getAanvraagstatussen()).map(List::size).orElse(0);
      int activiteiten = ofNullable(response.getAanvraagactiviteiten()).map(List::size).orElse(0);
      int adressen1 = ofNullable(response.getAdressenbinnenland()).map(List::size).orElse(0);
      int adressen2 = ofNullable(response.getAdressenbuitenland()).map(List::size).orElse(0);
      int adressen3 = ofNullable(response.getAdressenongespecificeerd()).map(List::size).orElse(0);
      int adressen = adressen1 + adressen2 + adressen3;
      setAdressen();

      tabs.addTab(newTab(form1), "Reisdocument / aanvraag");
      tabs.addTab(newTab(form2), "Personalisatiegegevens");
      tabs.addTab(newTab(new Table1()), "Statussen (" + statussen + ")");
      tabs.addTab(newTab(new Table2()), "Activiteiten (" + activiteiten + ")");
      tabs.addTab(newTab(new Table3()), "Addressen (" + adressen + ")");

      addExpandComponent(tabs);
    }

    void setAdressen() {
      ofNullable(response.getAdressenbinnenland())
          .ifPresent(list -> list
              .stream()
              .map(this::toAdres)
              .forEach(adressen::add));

      ofNullable(response.getAdressenbuitenland())
          .ifPresent(list -> list
              .stream()
              .map(this::toAdres)
              .forEach(adressen::add));

      ofNullable(response.getAdressenongespecificeerd())
          .ifPresent(list -> list
              .stream()
              .map(this::toAdres)
              .forEach(adressen::add));
    }

    private Adres toAdres(ControleAanvraagVolledigResponseAdresBinnenland adres) {
      return new Adres("Binnenland",
          adres.getRegistratiedatum(),
          trim(String.join(" ",
              astr(adres.getStraatnaam()),
              astr(adres.getHuisnummer()),
              astr(adres.getHuisletter()),
              astr(adres.getHuisnummertoevoeging()),
              astr(adres.getPostcode()),
              astr(adres.getPlaatsnaam()))));
    }

    private Adres toAdres(ControleAanvraagVolledigResponseAdresBuitenland adres) {
      return new Adres("Buitenland",
          adres.getRegistratiedatum(),
          trim(String.join(" ",
              astr(adres.getRegelAdresBuitenland1()),
              astr(adres.getRegelAdresBuitenland2()),
              astr(adres.getRegelAdresBuitenland3()))));
    }

    private Adres toAdres(ControleAanvraagVolledigResponseAdresOngespecificeerd adres) {
      return new Adres("Ongespecificeerd", null,
          astr(adres.getOngespecificeerdAdres()));
    }

    private static VLayout newTab(Component form1) {
      VLayout vLayout = new VLayout();
      vLayout.setSizeFull();
      vLayout.addExpandComponent(form1);
      vLayout.setMargin(true, false, false, false);
      return vLayout;
    }

    @Override
    public void onPreviousPage() {
      getNavigation().goBackToPreviousPage();
    }

    public class Table1 extends GbaTable {

      @Override
      public void setColumns() {
        setSelectable(true);
        addColumn("Begindatum", 150);
        addColumn("Einddatum", 150);
        addColumn("Status", 300);
        addColumn("Melding");
        super.setColumns();
      }

      @Override
      public void setRecords() {
        for (ControleAanvraagVolledigResponseAanvraagstatus status : statussen) {
          Record r = addRecord(status);
          r.addValue(DateTime.of(status.getBegindatum().toLocalDateTime()));
          r.addValue(ofNullable(status.getEinddatum())
              .map(datum -> DateTime.of(datum.toLocalDateTime()).getFormatDate())
              .orElse(""));
          r.addValue(status.getOmschrijving());
          r.addValue(getMelding(status));
        }
        super.setRecords();
      }

      private String getMelding(ControleAanvraagVolledigResponseAanvraagstatus status) {
        return ofNullable(status.getAanvraagmeldingen())
            .flatMap(meldingen -> meldingen.stream()
                .map(ControleAanvraagVolledigResponseAanvraagmelding::getOmschrijving)
                .filter(Objects::nonNull)
                .findFirst())
            .orElse("");
      }
    }

    public class Table2 extends GbaTable {

      @Override
      public void setColumns() {
        setSelectable(true);
        addColumn("Datum", 150);
        addColumn("Status");
        addColumn("Omschrijving");
        addColumn("Conclusie");
        addColumn("Toelichting");
        super.setColumns();
      }

      @Override
      public void setRecords() {
        if (activiteiten != null) {
          for (ControleAanvraagVolledigResponseAanvraagactiviteit activiteit : activiteiten) {
            Record r = addRecord(activiteit);
            r.addValue(DateTime.of(activiteit.getDatumAanvraagactiviteit().toLocalDateTime()));
            r.addValue(activiteit.getConclusie());
            r.addValue(activiteit.getOmschrijving());
            r.addValue(activiteit.getToelichting());
          }
        }
        super.setRecords();
      }
    }

    public class Table3 extends GbaTable {

      @Override
      public void setColumns() {
        setSelectable(true);
        addColumn("Soort", 150);
        addColumn("Datum / tijd");
        addColumn("Adres");
        super.setColumns();
      }

      @Override
      public void setRecords() {
        for (Adres adres : adressen) {
          Record r = addRecord(adres);
          r.addValue(ofNullable(adres.getDatumTijd())
              .map(OffsetDateTime::toLocalDateTime)
              .map(DateTime::of)
              .map(DateTime::getFormatDate)
              .orElse(""));
          r.addValue(adres.getAdres());
        }
        super.setRecords();
      }
    }
  }

  @Getter
  @AllArgsConstructor
  public static class Adres {

    private final String         soort;
    private final OffsetDateTime datumTijd;
    private final String         adres;
  }
}

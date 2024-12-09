/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page50;

import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_ADVIES;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_ANDERE_OUDER_AKKOORD;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_BERICHT_OMTRENT_TOELATING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_BESLISSING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_DATUM_AANVRAAG;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_DATUM_BEVESTIGING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_DATUM_KONING_BESLUIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_EINDE_TERMIJN;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_INFORMATIE_JUSTIS;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_MINDERJARIGE_KINDEREN_12;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_MINDERJARIGE_KINDEREN_16;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_NAAM_ANDERE_OUDER_WETTELIJK;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_NR_KONING_BESLUIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_TOELICHTING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50NaturalisatieBean.F_TOELICHTING2;

import com.vaadin.ui.Field;
import java.time.ZoneId;
import java.util.Date;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.bs.naturalisatie.page50.ToestemmComponent.ToestemmComponentValue;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceConfig;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceField;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;
import nl.procura.gba.web.services.bs.naturalisatie.enums.AdviesBurgemeesterType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.AndereOuderAkkoordType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BeslissingType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page50NaturalisatieForm extends GbaForm<Page50NaturalisatieBean> {

  private final DossierNaturalisatie dossier;

  public Page50NaturalisatieForm(DossierNaturalisatie dossier) {
    this.dossier = dossier;
    setCaption("Aanvullingen n.a.v. aanvraag");
    setReadThrough(true);
    setColumnWidths("300px", "");

    if (dossier.isOptie()) {
      setOrder(F_BERICHT_OMTRENT_TOELATING, F_MINDERJARIGE_KINDEREN_12, F_MINDERJARIGE_KINDEREN_16,
          F_ANDERE_OUDER_AKKOORD, F_NAAM_ANDERE_OUDER_WETTELIJK, F_TOELICHTING, F_INFORMATIE_JUSTIS,
          F_DATUM_AANVRAAG, F_EINDE_TERMIJN, F_BESLISSING, F_TOELICHTING2, F_DATUM_BEVESTIGING);
    } else {
      setOrder(F_BERICHT_OMTRENT_TOELATING, F_MINDERJARIGE_KINDEREN_12, F_MINDERJARIGE_KINDEREN_16,
          F_ANDERE_OUDER_AKKOORD, F_NAAM_ANDERE_OUDER_WETTELIJK, F_TOELICHTING,
          F_INFORMATIE_JUSTIS, F_DATUM_AANVRAAG, F_EINDE_TERMIJN, F_ADVIES, F_TOELICHTING2,
          F_DATUM_KONING_BESLUIT, F_NR_KONING_BESLUIT);
    }

    Page50NaturalisatieBean bean = new Page50NaturalisatieBean();
    bean.setBerichtOmtrentToelating(dossier.getBehBotOpgevraagd());
    bean.setMinderjarigeKinderen12(dossier.getKinderen12AkkoordType());
    bean.setMinderjarigeKinderen16(dossier.getBehMinderjKind2());
    bean.setToelichting(dossier.getBehAndereVertToel());
    bean.setInformatieJustis(dossier.getBehOpgevrJustis());
    bean.setDatumAanvraag(dossier.getBehDAanvr());
    bean.setEindeTermijn(dossier.getBehTermDEnd());
    bean.setToelichting2(dossier.getBehTermToel());
    setBean(bean);
  }

  @Override
  public Page50NaturalisatieBean getNewBean() {
    return new Page50NaturalisatieBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(F_DATUM_AANVRAAG)) {
      getLayout().addFieldset("Datum aanvraag");
    }
    if (property.is(F_EINDE_TERMIJN)) {
      getLayout().addFieldset("Termijn / beslissing / advies");
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_NAAM_ANDERE_OUDER_WETTELIJK)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<ToestemmComponentValue> create()
          .title("Andere ouder/wettelijk vertegenwoordiger")
          .component(verzoeker -> new ToestemmComponent(dossier, verzoeker))
          .getter(this::getToestemmComponentValue)
          .setter((verzoeker, value) -> verzoeker.setBsnToestemminggever(value.getBsn()))
          .build()));
    }

    if (property.is(F_ADVIES)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<AdviesBurgemeesterType>create()
          .title("Advies burgemeester")
          .component(v -> {
            GbaNativeSelect select = new GbaNativeSelect();
            select.setContainerDataSource(new AdviesBurgemeesterContainer());
            select.setImmediate(true);
            return select;
          })
          .getter(DossierNaturalisatieVerzoeker::getAdviesBurgemeesterType)
          .setter(DossierNaturalisatieVerzoeker::setAdviesBurgemeesterType)
          .build()));
    }

    if (property.is(F_ANDERE_OUDER_AKKOORD)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<AndereOuderAkkoordType> create()
          .title("Andere ouder/wettelijk vertegenwoordiger akkoord")
          .component(v -> {
            GbaNativeSelect select = new GbaNativeSelect();
            select.setContainerDataSource(new AndereOuderAkkoordContainer());
            select.setImmediate(true);
            return select;
          })
          .getter(DossierNaturalisatieVerzoeker::getAndereOuderAkkoordType)
          .setter(DossierNaturalisatieVerzoeker::setAndereOuderAkkoordType)
          .build()));
    }

    if (property.is(F_DATUM_KONING_BESLUIT)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<Date> create()
          .title("Datum Koninklijk Besluit")
          .component(v -> {
            ProDateField dateField = new ProDateField();
            dateField.setImmediate(true);
            return dateField;
          })
          .getter(DossierNaturalisatieVerzoeker::getBehDKoningBesluit)
          .setter((verzoeker, behDKoningBesluit) -> {
            verzoeker.setBehDKoningBesluit(behDKoningBesluit);
            verzoeker.setCeremonieDVerval(addYear(behDKoningBesluit));
          })
          .build()));
    }

    if (property.is(F_NR_KONING_BESLUIT)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<String> create()
          .title("Nummer Koninklijk Besluit")
          .component(v -> {
            GbaTextField nummerField = new GbaTextField();
            nummerField.setMaxLength(15);
            nummerField.setImmediate(true);
            nummerField.setNullRepresentation("");
            return nummerField;
          })
          .getter(DossierNaturalisatieVerzoeker::getBehNrKoningBesluit)
          .setter(DossierNaturalisatieVerzoeker::setBehNrKoningBesluit)
          .build()));
    }

    if (property.is(F_BESLISSING)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<BeslissingType> create()
          .title("Beslissing")
          .component(v -> {
            GbaNativeSelect select = new GbaNativeSelect();
            select.setContainerDataSource(new BeslissingTypeContainer());
            return select;
          })
          .getter(DossierNaturalisatieVerzoeker::getBeslissingType)
          .setter(DossierNaturalisatieVerzoeker::setBeslissingType)
          .build()));
    }

    if (property.is(F_DATUM_BEVESTIGING)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<Date> create()
          .title("Datum bevestiging")
          .component(v -> {
            ProDateField dateField = new ProDateField();
            dateField.setImmediate(true);
            return dateField;
          })
          .getter(DossierNaturalisatieVerzoeker::getBehDBevest)
          .setter((verzoeker, behDBevest) -> {
            verzoeker.setBehDBevest(behDBevest);
            verzoeker.setCeremonieDVerval(addYear(behDBevest));
          })
          .build()));
    }

    super.afterSetColumn(column, field, property);
  }

  private static Date addYear(Date behDBevest) {
    return behDBevest != null ? new ProcuraDate(behDBevest).addYears(1).getDateFormat() : null;
  }

  private ToestemmComponentValue getToestemmComponentValue(DossierNaturalisatieVerzoeker verzoeker) {
    BsnFieldValue bsn = verzoeker.getBsnToestemminggever();
    if (bsn.isCorrect()) {
      return new ToestemmComponentValue(bsn, () -> getApplication()
          .getServices()
          .getPersonenWsService()
          .getPersoonslijst(bsn.getStringValue()));
    }
    return new ToestemmComponentValue(bsn);
  }

  @Override
  public void afterSetBean() {
    getField(F_INFORMATIE_JUSTIS).setVisible(dossier.isOptie());
    dossier.ifOptie(() -> getField(F_EINDE_TERMIJN).setCaption("Einde termijn (datum aanvraag + 13 weken)"));
    dossier.ifNotOptie(() -> getField(F_EINDE_TERMIJN).setCaption("Einde termijn (datum aanvraag + 1 jaar)"));
    getField(F_DATUM_AANVRAAG).addListener(new FieldChangeListener<Date>() {

      @Override
      public void onChange(Date value) {
        if (value != null) {
          dossier.ifOptie(() -> getField(F_EINDE_TERMIJN).setValue(Date.from(value.toInstant()
              .atZone(ZoneId.systemDefault()).plusWeeks(13).toInstant())));

          dossier.ifNotOptie(() -> getField(F_EINDE_TERMIJN).setValue(Date.from(value.toInstant()
              .atZone(ZoneId.systemDefault()).plusYears(1).toInstant())));
        }
      }
    });
    super.afterSetBean();
  }
}

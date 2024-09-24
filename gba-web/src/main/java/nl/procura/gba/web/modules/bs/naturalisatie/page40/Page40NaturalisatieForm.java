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

package nl.procura.gba.web.modules.bs.naturalisatie.page40;

import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_BEREID_TOT_AFLEGGEN_VERKLARING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_BEREID_TOT_DOEN_VAN_AFSTAND;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_BETROKKENE_BEKEND_MET_BETALING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_BEWIJSNOOD_AANGETOOND;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_BEWIJS_VAN_IDENTITEIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_BEWIJS_VAN_NATIONALITEIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_GELDIGE_VERBLIJFSVERGUNNING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_INBURGERING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_NAAMSVASTSTELLINGOFWIJZIGING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_NAAM_GEWIJZIGD;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_NAAM_VASTGESTELD;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_TOELICHTING1;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_TOELICHTING2;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_VERKLARING_VERBLIJF;
import static nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40NaturalisatieBean.F_VNR;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.naturalisatie.page40.GeslVoornComponent.GeslVoornComponentValue;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceConfig;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceField;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;
import nl.procura.gba.web.services.bs.naturalisatie.enums.InburgeringType;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page40NaturalisatieForm extends GbaForm<Page40NaturalisatieBean> {

  private final DossierNaturalisatie dossier;
  private ValueChoiceField           inburgeringField;

  public Page40NaturalisatieForm(DossierNaturalisatie dossier) {
    this.dossier = dossier;
    setCaption("Toetsing aan voorwaarden");
    setReadThrough(true);
    setColumnWidths("300px", "");
    setOrder(F_VNR, F_VERKLARING_VERBLIJF, F_BEREID_TOT_AFLEGGEN_VERKLARING,
        F_BETROKKENE_BEKEND_MET_BETALING, F_BEREID_TOT_DOEN_VAN_AFSTAND,
        F_BEWIJS_VAN_IDENTITEIT, F_BEWIJS_VAN_NATIONALITEIT, F_BEWIJSNOOD_AANGETOOND, F_TOELICHTING1,
        F_GELDIGE_VERBLIJFSVERGUNNING, F_INBURGERING,
        F_NAAMSVASTSTELLINGOFWIJZIGING, F_NAAM_VASTGESTELD,
        F_NAAM_GEWIJZIGD, F_TOELICHTING2);

    Page40NaturalisatieBean bean = new Page40NaturalisatieBean();

    bean.setVerklaringVerblijf(dossier.getToetsverklOndertekend());
    bean.setBereidTotAfleggenVerklaring(dossier.getToetsBereidVerkl());
    bean.setBetrokkeneBekendMetBetaling(dossier.getToetsBetrokkBekend());
    bean.setBereidTotDoenVanAfstand(dossier.getBereidAfstandType());
    bean.setBewijsVanIdentiteit(dossier.getToetsBewijsIdAanw());
    bean.setBewijsVanNationaliteit(dossier.getBewijsBewijsNationaliteitType());
    bean.setBewijsnoodAangetoond(dossier.getToetsBewijsnood());
    bean.setToelichting1(dossier.getToetsBewijsnoodToel());
    bean.setGeldigeVerblijfsvergunning(dossier.getGeldigeVerblijfsvergunningType());

    // Naamsvaststelling/wijziging
    bean.setNaamsvaststellingOfWijziging(dossier.getNaamVaststellingType());
    bean.setToelichting2(dossier.getNaamstGeslGewToel());

    setBean(bean);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(F_NAAMSVASTSTELLINGOFWIJZIGING)) {
      getLayout().addFieldset("Naamsvaststelling/wijziging");
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_VNR)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<String> create()
          .title("V-nummer")
          .component(v -> {
            GbaTextField textField = new GbaTextField();
            textField.setMaxLength(10);
            return textField;
          })
          .getter(DossierNaturalisatieVerzoeker::getVnr)
          .setter(DossierNaturalisatieVerzoeker::setVnr)
          .build()));
    }

    if (property.is(F_NAAM_VASTGESTELD)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<GeslVoornComponentValue> create()
          .title("Naam vastgesteld als")
          .component(v -> new GeslVoornComponent("Geslachtsnaam vastgesteld als", "Voornamen vastgesteld als"))
          .getter(v -> GeslVoornComponent.value(v.getNaamstGesl(), v.getNaamstVoorn()))
          .setter((v, value) -> {
            v.setNaamstGesl(value.getGesl());
            v.setNaamstVoorn(value.getVoorn());
          })
          .build()));
    }

    if (property.is(F_NAAM_GEWIJZIGD)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<GeslVoornComponentValue> create()
          .title("Naam gewijzigd in")
          .component(v -> new GeslVoornComponent("Geslachtsnaam gewijzigd in", "Voornamen gewijzigd in"))
          .getter(v -> GeslVoornComponent.value(v.getNaamstGeslGew(), v.getNaamstVoornGew()))
          .setter((v, value) -> {
            v.setNaamstGeslGew(value.getGesl());
            v.setNaamstVoornGew(value.getVoorn());
          })
          .build()));
    }

    if (property.is(F_INBURGERING)) {
      inburgeringField = new ValueChoiceField(dossier, ValueChoiceConfig.<InburgeringType> create()
          .title("Inburgering")
          .component(verzoeker -> {
            GbaNativeSelect select = new GbaNativeSelect();
            select.setContainerDataSource(new InburgeringTypeContainer());
            return select;
          })
          .required(true)
          .getter(DossierNaturalisatieVerzoeker::getInburgeringType)
          .setter(DossierNaturalisatieVerzoeker::setInburgeringType)
          .build());

      column.addComponent(inburgeringField);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page40NaturalisatieBean getNewBean() {
    return new Page40NaturalisatieBean();
  }

  @Override
  public void commit() throws SourceException, InvalidValueException {
    super.commit();
    if (inburgeringField != null) {
      inburgeringField.validate();
    }
  }

  @Override
  public void afterSetBean() {
    getField(F_INBURGERING).setVisible(!dossier.isOptie());
    getField(F_NAAM_GEWIJZIGD).setVisible(!dossier.isOptie());
    super.afterSetBean();
  }
}

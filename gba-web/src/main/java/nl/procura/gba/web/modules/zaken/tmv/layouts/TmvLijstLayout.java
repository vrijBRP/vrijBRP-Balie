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

package nl.procura.gba.web.modules.zaken.tmv.layouts;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.burgerzaken.gba.core.validators.AlfVal;
import nl.procura.burgerzaken.gba.core.validators.DatVal;
import nl.procura.burgerzaken.gba.core.validators.NumVal;
import nl.procura.burgerzaken.gba.core.validators.Val;
import nl.procura.gba.web.common.validators.GbaDatumValidator;
import nl.procura.gba.web.components.containers.TabelContainer;
import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.persoonslijst.overig.grid.RecordElementCombo;
import nl.procura.gba.web.modules.zaken.tmv.objects.TmvRecord;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.AnrField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class TmvLijstLayout extends GbaVerticalLayout {

  private static final long NEDERLAND = 6030;

  private List<RecordElementCombo> list  = new ArrayList<>();
  private GbaTable                 table = null;

  public TmvLijstLayout(List<RecordElementCombo> list) {

    setList(list);

    addStyleName("v-form-error");

    setTable(new GbaTable() {

      @Override
      public void setColumns() {

        addStyleName("tmv_table");

        addColumn("Categorie", 90);
        addColumn("Elem.", 40);
        addColumn("Omschrijving", 250);
        addColumn("Set", 20);
        addColumn("Huidige waarde");
        addColumn("Nieuwe waarde", 250).setClassType(Component.class);

        super.setColumns();
      }

      @Override
      public void setPageLength(int pageLength) {
        super.setPageLength(getRecords().size() + 2);
      }

      @Override
      public void setRecords() {

        try {
          if (getRecords().isEmpty()) {

            for (RecordElementCombo combo : getList()) {

              TmvRecord tmv = new TmvRecord(combo, getInitComponent(combo));

              Record r = addRecord(tmv);
              r.addValue(GBACat.getByCode(aval(tmv.getCat())).getDescr());
              r.addValue(tmv.getElem());
              r.addValue(tmv.getDescr());
              r.addValue(tmv.getSet() + 1);
              r.addValue(tmv.getHuidigeWaarde().getDescr());
              r.addValue(tmv.getField());
            }

            checkFields();
          }
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }
    });

    addExpandComponent(getTable());
  }

  public List<RecordElementCombo> getList() {
    return list;
  }

  public void setList(List<RecordElementCombo> list) {
    this.list = list;
  }

  public GbaTable getTable() {
    return table;
  }

  public void setTable(GbaTable table) {
    this.table = table;
  }

  public List<TmvRecord> getTmvRecords() {

    List<TmvRecord> list = new ArrayList<>();

    boolean verschillend = false;

    for (Record r : getTable().getRecords()) {

      TmvRecord tmvRecord = (TmvRecord) r.getObject();

      if (tmvRecord.isVerschillend()) {
        verschillend = true;
      }

      tmvRecord.getField().validate();

      list.add(tmvRecord);
    }

    if (!verschillend) {
      throw new ProException(ENTRY, WARNING, "Er dient minimaal 1 veld te worden gewijzigd.");
    }

    return list;
  }

  private void checkFields() {

    for (Record r : getTable().getRecords()) {

      setStandaardWaarden((TmvRecord) r.getObject());
    }
  }

  /**
   * Als de gemeente van de inschrijving de huidige gemeente is dan de straten tonen
   */
  private void checkGemeenteInschrijving(RecordElementCombo combo) {

    Record gemeente = getRecord(combo, GBAElem.GEM_INSCHR);

    if (gemeente != null) {

      TmvRecord gemeenteR = (TmvRecord) gemeente.getObject();
      Record straat = getRecord(combo, GBAElem.STRAATNAAM);

      if (straat != null) {

        TmvRecord straatR = (TmvRecord) straat.getObject();
        FieldValue gemeenteValue = (FieldValue) gemeenteR.getField().getValue();

        int plaatsCode = aval(
            (gemeenteValue != null) ? gemeenteValue.getValue() : combo.getGbaElement().getValue().getVal());
        boolean isGemeente = getApplication().getServices().getGebruiker().isGemeente(plaatsCode);

        AbstractField f = isGemeente ? getDefaultComponent(straatR.getGbaCombo()) : new GbaTextField();
        straat.getValues().get(5).setValue(setFieldStyle(f));
        straatR.setField(f);

        setStandaardWaarden(straatR);
        getTable().reloadRecords();
      }
    }
  }

  /**
   * Als de gemeente van inschrijving Nederland is dan de plaatsen tonen
   */
  private void checkLand(RecordElementCombo combo) {

    Record land = getRecord(combo, GBAElem.GEBOORTELAND);

    if (land != null) {

      TmvRecord landR = (TmvRecord) land.getObject();
      Record plaats = getRecord(combo, GBAElem.GEBOORTEPLAATS);
      TmvRecord plaatsR = (TmvRecord) plaats.getObject();
      FieldValue landValue = (FieldValue) landR.getField().getValue();

      int landCode = aval(
          (landValue != null) ? landValue.getValue() : combo.getGbaElement().getValue().getVal());
      AbstractField f = (landCode == NEDERLAND) ? getDefaultComponent(plaatsR.getGbaCombo()) : new GbaTextField();
      plaats.getValues().get(5).setValue(setFieldStyle(f));
      plaatsR.setField(f);
      setStandaardWaarden(plaatsR);
      getTable().reloadRecords();
    }
  }

  private AbstractField getDefaultComponent(RecordElementCombo combo) {
    return setFieldStyle(getField(combo.getGbaElement().getCatCode(), combo.getGbaElement().getElemCode()));
  }

  private AbstractField getField(int catCode, int elementCode) {

    GBAGroupElements.GBAGroupElem pleE = GBAGroupElements.getByCat(catCode, elementCode);

    Val val = pleE.getElem().getVal();
    GBATable tabel = pleE.getElem().getTable();
    GbaTextField field = new GbaTextField();

    if (pleE.getElem() == GBAElem.BSN) {
      return new GbaBsnField();
    } else if (pleE.getElem() == GBAElem.ANR) {
      return new AnrField();
    } else if (tabel != GBATable.ONBEKEND) {
      TabelContainer container = new TabelContainer(tabel, true);
      AbstractSelect select = (container.getItemIds().size() < 10) ? new NativeSelect() : new Select();
      select.setContainerDataSource(container);
      return select;
    } else if (val instanceof NumVal) {
      return new NumberField();
    } else if (val instanceof AlfVal) {
      field.setMaxLength(((AlfVal) val).getMax());
    } else if (val instanceof DatVal) {
      DatumVeld datumVeld = new DatumVeld(null,
          new AbstractStringValidator("Incorrecte datum") {

            @Override
            public boolean isValidString(String value) {
              return new GbaDatumValidator().isValid(value);
            }
          });

      datumVeld.setUitzonderingenToestaan(true);
      return datumVeld;
    }

    return field;
  }

  private AbstractField getInitComponent(RecordElementCombo combo) {

    AbstractField f = getDefaultComponent(combo);

    // Bij geboorteland of gemeente van inschrijving
    // listeners toevoegen.
    if (combo.getPleElement().getElem() == GBAElem.GEBOORTELAND) {

      f.setImmediate(true);
      f.addListener(new ComboChangeListener(combo));
    } else if (combo.getPleElement().getElem() == GBAElem.GEM_INSCHR) {

      f.setImmediate(true);
      f.addListener(new ComboChangeListener(combo));
    }

    return setFieldStyle(f);
  }

  /**
   * Geef record met bepaald element met dezelfde categorie als combo
   */
  private Record getRecord(RecordElementCombo combo, GBAElem elem) {

    for (Record r : getTable().getRecords()) {

      TmvRecord tR = (TmvRecord) r.getObject();

      if ((tR.getCat() == combo.getGbaRecord().getCatType().getCode()) && (tR.getElem() == elem.getCode())) {

        return r;
      }
    }

    return null;
  }

  /**
   * Stijl van velden aanpassen
   */
  private AbstractField setFieldStyle(AbstractField f) {

    if (f instanceof TextField) {
      f.setHeight("23px");
    }

    f.setWidth("100%");

    return f;
  }

  private void setStandaardWaarden(TmvRecord tmvRecord) {

    RecordElementCombo gbaCombo = tmvRecord.getGbaCombo();

    FieldValue fv = new FieldValue(gbaCombo.getGbaElement().getValue().getVal(),
        gbaCombo.getGbaElement().getValue().getDescr());

    tmvRecord.getField().setValue(fv);
  }

  public class ComboChangeListener implements ValueChangeListener {

    private RecordElementCombo combo = null;

    private ComboChangeListener(RecordElementCombo combo) {

      setCombo(combo);
    }

    public RecordElementCombo getCombo() {
      return combo;
    }

    public void setCombo(RecordElementCombo combo) {
      this.combo = combo;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {

      checkLand(getCombo());
      checkGemeenteInschrijving(getCombo());
    }
  }
}

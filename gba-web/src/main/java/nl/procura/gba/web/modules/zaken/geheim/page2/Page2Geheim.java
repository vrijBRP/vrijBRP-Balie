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

package nl.procura.gba.web.modules.zaken.geheim.page2;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import java.util.HashMap;
import java.util.Map.Entry;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.GeheimType;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.geheim.PageGeheim;
import nl.procura.gba.web.modules.zaken.geheim.page4.Page4Geheim;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijst;
import nl.procura.gba.web.services.zaken.geheim.GeheimAanvraag;
import nl.procura.gba.web.services.zaken.geheim.GeheimPersoon;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.Fieldset;

/**
 * Nieuwe aanvraag
 */

public class Page2Geheim extends PageGeheim {

  private final HashMap<BasePLExt, GeheimType> geheimList = new HashMap<>();
  private final GbaTable                       table;
  private final Page2GeheimForm                form;

  public Page2Geheim(GeheimAanvraag aanvraag) {
    super("Verstrekkingsbeperking: nieuw");
    setAanvraag(aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    setMargin(true);

    table = new GbaTable() {

      @Override
      public void setColumns() {

        addColumn("Persoon");
        addColumn("Relatie", 100);
        addColumn("Geslacht", 100);
        addColumn("Geboren", 100);
        addColumn("Huidige beperking", 150).setUseHTML(true);
        addColumn("Nieuwe beperking", 150).setClassType(GbaNativeSelect.class);

        super.setColumns();
      }

      @Override
      public void setRecords() {

        try {
          RelatieLijst rl = getApplication().getServices().getPersonenWsService().getRelatieLijst(getPl(),
              false);

          for (Relatie relatie : rl.getSortedRelaties()) {

            // Alleen gezinsleden
            if (!relatie.isHuisgenoot() || relatie.isRelatieType(NIET_GERELATEERD, NIET_GERELATEERD, OUDER,
                MEERDERJARIG_KIND)) {
              continue;
            }

            Record r = addRecord(relatie);
            r.addValue(relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
            r.addValue(relatie.getRelatieType().getOms());
            r.addValue(relatie.getPl().getPersoon().getGeslacht().getDescr());
            r.addValue(relatie.getPl().getPersoon().getGeboorte().getDatumLeeftijd());
            r.addValue(getGeheimLabel(getGeheimCode(relatie.getPl())));
            r.addValue(getSelect(relatie.getPl()));

          }
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }

    };

    form = new Page2GeheimForm();
    addComponent(form);

    addComponent(new Fieldset("Gerelateerde personen op hetzelfde adres"));
    addExpandComponent(table);
  }

  @Override
  public void onNextPage() {

    form.commit();

    if (getGeheimhouding() == GeheimType.ONBEKEND) {
      throw new ProException(ENTRY, WARNING, "Geen soort verstrekkingsbeperking opgegeven.");
    }

    getAanvraag().setDatumTijdInvoer(new DateTime());
    getAanvraag().setDatumIngang(new DateTime(form.getBean().getDatumWijz()));
    getAanvraag().getPersonen().clear();

    for (Entry<BasePLExt, GeheimType> e : geheimList.entrySet()) {

      BasePLExt pl = e.getKey();
      AnrFieldValue anr = new AnrFieldValue(pl.getPersoon().getAnr().getVal());
      BsnFieldValue bsn = new BsnFieldValue(pl.getPersoon().getBsn().getVal());

      getAanvraag().getPersonen().add(new GeheimPersoon(anr, bsn));
    }

    getAanvraag().setAnummerAangever(new AnrFieldValue(getPl().getPersoon().getAnr().getVal()));
    getAanvraag().setGeheimType(getGeheimhouding());
    getAanvraag().setBurgerServiceNummerAangever(new BsnFieldValue(getPl().getPersoon().getBsn().getVal()));

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(getAanvraag());

    super.onSave();

    getNavigation().goToPage(new Page4Geheim(getAanvraag()));
  }

  private int getGeheimCode(BasePLExt pl) {
    return pl.getPersoon().getGeheim();
  }

  private GeheimType getGeheimhouding() {

    GeheimType gt = GeheimType.ONBEKEND;

    for (Entry<BasePLExt, GeheimType> e : geheimList.entrySet()) {

      if (gt != GeheimType.ONBEKEND && e.getValue() == GeheimType.ONBEKEND) {
        continue;
      }

      if ((gt == GeheimType.ONBEKEND) || (gt == e.getValue())) {
        gt = e.getValue();
      } else {
        throw new ProException(ENTRY, INFO, "Er kan maar voor 1 soort geheimhouding worden gekozen.");
      }
    }

    return gt;
  }

  private String getGeheimLabel(int geheimCode) {

    GeheimType type = GeheimType.get(geheimCode);

    switch (type) {
      case GEDEELTELIJK:
        return setClass("orange", geheimCode + ": " + type.getOms());

      case MAXIMALE:
        return setClass("red", geheimCode + ": " + type.getOms());

      default:
        return setClass("green", geheimCode + ": " + type.getOms());
    }
  }

  private GbaNativeSelect getSelect(final BasePLExt pl) {

    GbaNativeSelect s = new GbaNativeSelect();
    s.setWidth("100%");
    s.setImmediate(true);
    s.setDataSource(new GeheimContainer(pl));
    s.addListener((ValueChangeListener) event -> {

      GeheimType type = (GeheimType) event.getProperty().getValue();

      if (type == null) {
        type = GeheimType.ONBEKEND;
      }

      geheimList.remove(pl); // verwijder de oude waarde

      if (type != GeheimType.ONBEKEND) {
        geheimList.put(pl, type); // voeg de nieuwe waarde toe
      }

      GeheimType geheimtype = null;

      try {
        geheimtype = getGeheimhouding();
      } finally {
        boolean isBekend = geheimtype != null && (geheimtype != GeheimType.ONBEKEND);

        if (isBekend && geheimtype != null) {
          form.getBean().setWijzigenIn(geheimtype.toString());
        } else {
          form.getBean().setWijzigenIn("&lt;Selecteer de nieuwe beperking in de tabel hieronder&gt;");
        }

        form.repaint();
      }
    });

    return s;
  }

  private class GeheimContainer extends IndexedContainer {

    public GeheimContainer(BasePLExt pl) {

      int geheimCode = pl.getPersoon().getGeheim();
      GeheimType type = GeheimType.get(geheimCode);

      if ((type == GeheimType.GEDEELTELIJK) || (type == GeheimType.MAXIMALE)) {
        addItem(GeheimType.GEEN);
      }

      if ((type == GeheimType.GEDEELTELIJK) || (type == GeheimType.GEEN)) {
        addItem(GeheimType.MAXIMALE);
      }
    }
  }
}

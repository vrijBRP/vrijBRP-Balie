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

package nl.procura.gba.web.modules.zaken.verhuizing.page11;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import java.util.List;

import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisRelatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.java.collection.Collections;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.theme.twee.Icons;

/**
 * Toevoegen persoon
 */

public class Page11Verhuizing extends ZakenPage {

  private final Table1          table1       = new Table1();
  private Page11VerhuizingForm1 form1        = null;
  private VerhuisType           verhuisType  = null;
  private VerhuisAdres          verhuisAdres = null;

  public Page11Verhuizing(VerhuisType verhuisType, VerhuisAdres verhuisAdres) {

    super("Verhuizing");

    this.verhuisType = verhuisType;
    this.verhuisAdres = verhuisAdres;

    addButton(buttonPrev);
    addButton(buttonNext);

    form1 = new Page11VerhuizingForm1(verhuisType, verhuisAdres);

    OptieLayout ol = new OptieLayout();

    ol.getLeft().addComponent(form1);
    ol.getLeft().addComponent(new InfoLayout());

    ol.getRight().setWidth("200px");
    ol.getRight().setCaption("Opties");

    ol.getRight().addButton(buttonSearch, this);

    addComponent(ol);
    addComponent(table1);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onNextPage() {

    for (Record r : table1.getSelectedRecords()) {
      Relatie relatie = (Relatie) r.getObject();

      if (fil(getStatus(verhuisType, relatie))) {
        String naam = relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
        throw new ProException(SELECT, WARNING, naam + " komt niet voor dit type verhuizing in aanmerking.");
      }
    }

    selectToest(table1.getSelectedRecords());
  }

  @Override
  public void onSearch() {

    form1.commit();

    Page11VerhuizingBean1 bean = form1.getBean();

    PLEArgs zArgs = new PLEArgs();
    zArgs.addNummer(astr(bean.getBsn().getValue()));
    zArgs.setGeboortedatum(astr(bean.getGeboortedatum().getValue()));
    zArgs.setGeslachtsnaam(bean.getGeslachtsnaam());
    zArgs.setSearchOnAddress(bean.isAdresind());

    if (!verhuisType.isHervestiging()) {
      zArgs.setHuisnummer(verhuisAdres.getAddress().getHnr());
      zArgs.setHuisletter(verhuisAdres.getAddress().getHnrL());
      zArgs.setHuisnummertoevoeging(verhuisAdres.getAddress().getHnrT());
      zArgs.setPostcode(verhuisAdres.getAddress().getPostalCode());
    }

    table1.getRecords().clear();

    table1.reloadRecords();

    List<Relatie> relaties = getServices().getVerhuizingService().findVerhuisRelaties(getPl(), zArgs,
        verhuisType.isHervestiging());

    if (relaties.isEmpty()) {
      throw new ProException(SELECT, "Er zijn geen bewoners gevonden.");
    }

    for (Relatie relatie : relaties) {

      String naam = relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
      String status = getStatus(verhuisType, relatie);

      Record r = table1.addRecord(relatie);

      TableImage warning = new TableImage(Icons.getIcon(Icons.ICON_WARN));
      r.addValue(fil(status) ? warning : null);
      r.addValue(naam + " " + setClass("red", status));
      r.addValue(relatie.getRelatieType().getOms());
      r.addValue(relatie.getRelatieType().getAangifte());
      r.addValue(relatie.getPl().getPersoon().getGeslacht().getDescr());
      r.addValue(relatie.getPl().getPersoon().getGeboorte().getDatumLeeftijd());
    }

    table1.reloadRecords();

    super.onSearch();
  }

  private String getStatus(VerhuisType type, Relatie relatie) {

    boolean isGeemigreerd = relatie.getPl().getPersoon().getStatus().isEmigratie();
    boolean isRni = relatie.getPl().getPersoon().getStatus().isRni();
    boolean isMinister = relatie.getPl().getPersoon().getStatus().isMinisterieelBesluit();

    StringBuilder sb = new StringBuilder();

    switch (type) {

      case HERVESTIGING:
        if (!isGeemigreerd && !isRni && !isMinister) {
          sb.append("Geen sprake van emigratie, RNI of ministrieel besluit, ");
        }
        break;

      default:
        if (isGeemigreerd) {
          sb.append("Geëmigreerd, ");
        }
        if (isRni) {
          sb.append("PL aangelegd in de RNI, ");
        }
    }

    return trim(" - " + trim(sb.toString()));
  }

  private void selectToest(List<Record> records) {

    try {

      if (records.isEmpty()) {
        throw new ProException(SELECT, "Selecteer minimaal één bewoner.");
      }

      for (Record r : records) {

        VerhuisRelatie verhuisRelatie = new VerhuisRelatie((Relatie) r.getObject());

        // zoek opnieuw persoon voor volledige PL
        verhuisRelatie.getRelatie().setPl(
            getServices().getPersonenWsService().getPersoonslijst(verhuisRelatie.getRelatie().getPl()));

        verhuisAdres.addVerhuisRelatie(verhuisRelatie);
      }

      onPreviousPage();
    } catch (Exception e) {
      getApplication().handleException(e);
    }
  }

  public class Table1 extends Page11VerhuizingTable1 {

    @Override
    public void onDoubleClick(Record record) {

      selectToest(Collections.list(record));
    }
  }
}

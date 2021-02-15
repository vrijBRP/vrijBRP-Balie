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

package nl.procura.gba.web.modules.bs.huwelijk.page40;

import static nl.procura.gba.web.modules.bs.huwelijk.page40.Page40HuwelijkBean.GEMEENTEGETUIGEN;
import static nl.procura.gba.web.modules.bs.huwelijk.page40.Page40HuwelijkBean.GERELATEERDEN;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.huwelijk.page40.Page40Huwelijk.Table;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieSoort;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page40HuwelijkForm extends GbaForm<Page40HuwelijkBean> {

  private final Table           table;
  private final DossierHuwelijk dossierHuwelijk;

  public Page40HuwelijkForm(Table table, DossierHuwelijk dossierHuwelijk) {

    this.table = table;
    this.dossierHuwelijk = dossierHuwelijk;

    setCaption("Vereisten volgens Nederlands recht");
    setColumnWidths("150px", "");

    init();
  }

  public DossierPersoon getAvailablePersoon() {
    for (DossierPersoon dp : table.getAllValues(DossierPersoon.class)) {
      if (!dp.isVolledig()) {
        return dp;
      }
    }

    return null;
  }

  @Override
  public Page40HuwelijkBean getNewBean() {
    return new Page40HuwelijkBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(GEMEENTEGETUIGEN)) {

      GbaNativeSelect getuigen = (GbaNativeSelect) field;

      getuigen.setContainerDataSource(new GetuigeContainer());
    } else if (property.is(GERELATEERDEN)) {

      GbaNativeSelect ouders = (GbaNativeSelect) field;

      ouders.setContainerDataSource(new GerelateerdenContainer());

      ouders.addListener((ValueChangeListener) event -> {

        GbaNativeSelect field1 = (GbaNativeSelect) event.getProperty();

        FieldValue fieldValue = (FieldValue) field1.getValue();

        if (fieldValue != null) {

          final DossierPersoon newPersoon = getAvailablePersoon();

          if (newPersoon == null) {
            throw new ProException(ProExceptionSeverity.INFO, "Maximaal aantal getuigen is bereikt.");
          }

          DossierPersoon persoon = (DossierPersoon) fieldValue.getValue();
          BsPersoonUtils.kopieDossierPersoon(persoon, newPersoon);
          newPersoon.setCode(null);
          newPersoon.setDossierPersoonType(GETUIGE);

          String toelichting = "";
          if (persoon.getDossierPersoonType().is(OUDER)) {
            toelichting = "ouder";
          } else if (persoon.getDossierPersoonType().is(KIND)) {
            toelichting = "kind";
          }

          newPersoon.setToelichting(toelichting);
          table.init();
        }
      });
    }

    field.setValue(0);

    super.setColumn(column, field, property);
  }

  protected void init() {
  }

  public class GerelateerdenContainer extends ArrayListContainer {

    public GerelateerdenContainer() {

      removeAllItems();

      List<DossierPersoon> personen = new ArrayList<>();

      personen.addAll(dossierHuwelijk.getPartner1().getPersonen(OUDER));
      personen.addAll(dossierHuwelijk.getPartner1().getPersonen(KIND));

      personen.addAll(dossierHuwelijk.getPartner2().getPersonen(OUDER));
      personen.addAll(dossierHuwelijk.getPartner2().getPersonen(KIND));

      Map<String, FieldValue> map = new LinkedHashMap<>();

      for (DossierPersoon dp : personen) {

        int lt = dp.getLeeftijd();
        String type = dp.getDossierPersoonType().getDescr();
        String naam = dp.getNaam().getPred_adel_voorv_gesl_voorn();

        if (dp.getDossierPersoonType() == KIND) {
          naam += " (" + lt + ")";
        }

        String description = type + ": " + naam;
        map.put(description, new FieldValue(dp, description));
      }

      addItems(map.values());
    }
  }

  public class GetuigeContainer extends ArrayListContainer {

    public GetuigeContainer() {

      removeAllItems();

      HuwelijksLocatieSoort huwelijksLocatieSoort = dossierHuwelijk.getHuwelijksLocatie().getLocatieSoort();

      for (int i = 0; i <= huwelijksLocatieSoort.getAantalGetuigenMax(); i++) {

        if (i == 0) {
          addItem(new FieldValue("0", "Geen"));
        } else {
          addItem(new FieldValue(astr(i), astr(i)));
        }

      }
    }
  }
}

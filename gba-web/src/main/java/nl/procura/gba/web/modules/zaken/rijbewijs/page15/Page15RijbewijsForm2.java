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

package nl.procura.gba.web.modules.zaken.rijbewijs.page15;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page15.Page15RijbewijsBean1.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.date2str;

import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.collections4.map.LinkedMap;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f07.ONGELDCATGEG;
import nl.procura.rdw.processen.p0252.f07.UITGMAATRGEG;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.rdw.processen.p0252.f08.UITGRYBGEG;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page15RijbewijsForm2 extends GbaForm<Page15RijbewijsBean1> {

  private final P0252                   p0252f2;
  private final Map<String, BigInteger> categorieen = new LinkedMap<>();
  private final String[]                fields      = new String[]{ CAT_AM, CAT_B, CAT_BE, CAT_A1, CAT_C, CAT_CE,
      CAT_A2, CAT_C1, CAT_C1E, CAT_A, CAT_D, CAT_DE, CAT_AL, CAT_D1, CAT_D1E, CAT_AZ, CAT_T };

  Page15RijbewijsForm2(P0252 p0252f1, P0252 p0252f2) {
    this.p0252f2 = p0252f2;
    setCaption("Rijbewijscategorieën");

    // Add categories
    NATPRYBMAATR a = (NATPRYBMAATR) p0252f1.getResponse().getObject();
    if (a != null) {
      Optional<UITGRYBGEG> rijbewijs = a.getUitgrybtab().getUitgrybgeg().stream().findFirst();
      rijbewijs.ifPresent(u -> u.getCatrybtab()
          .getCatrybgeg()
          .forEach(c -> categorieen.put(c.getRybcatr(), c.getEindgelddatc())));
    }

    setReadThrough(true);
    setOrder(fields);
    setColumnWidths("50px", "", "50px", "", "50px", "");
    initForm();
    setBean(new Page15RijbewijsBean1());
  }

  private Entry<String, BigInteger> getCategorie(String formCat) {
    for (Entry<String, BigInteger> rdwCat : categorieen.entrySet()) {
      if (stripCategorie(formCat).equalsIgnoreCase(rdwCat.getKey())) {
        return rdwCat;
      }
    }
    return null;
  }

  private String stripCategorie(String cat) {
    return cat.replaceAll("cat", "").toUpperCase();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(CAT_AZ)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {
    Entry<String, BigInteger> cat = getCategorie(astr(property.getId()));

    String labelValue;
    if (cat != null) {
      labelValue = " (geldig tot " + getGeldigheidsdatum(cat) + ")";
    } else {
      labelValue = MiscUtils.setClass("grey", " (categorie niet op rijbewijs)");
      field.setEnabled(false);
    }

    Label label = new Label(labelValue, Label.CONTENT_XHTML);
    label.setSizeUndefined();
    column.addComponent(label);

    super.afterSetColumn(column, field, property);
  }

  private String getGeldigheidsdatum(Entry<String, BigInteger> cat) {

    if (p0252f2 != null) {
      nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR maatr = (nl.procura.rdw.processen.p0252.f07.NATPRYBMAATR) p0252f2
          .getResponse()
          .getObject();
      for (UITGMAATRGEG m : maatr.getUitgmaatrtab().getUitgmaatrgeg()) {
        for (ONGELDCATGEG o : m.getOngeldcattab().getOngeldcatgeg()) {
          if (cat.getKey().equalsIgnoreCase(o.getRybcatov())) {
            return ("<strike>" + date2str(cat.getValue().toString()) + "</strike> ") +
                date2str(o.getOngelddatcat().toString());
          }
        }
      }
    }
    return date2str(cat.getValue().toString());

  }

  public void initForm() {
  }

  void fill(OngeldigVerklaring ongeldigVerklaring) {
    for (String propertyId : fields) {
      Field field = getField(propertyId);
      if (field.isEnabled()) {
        if (field.getValue() != null) {
          DateFieldValue dfv = new DateFieldValue(field.getValue().toString());
          ongeldigVerklaring.addCategorie(stripCategorie(propertyId), dfv);
        }
      }
    }
  }
}

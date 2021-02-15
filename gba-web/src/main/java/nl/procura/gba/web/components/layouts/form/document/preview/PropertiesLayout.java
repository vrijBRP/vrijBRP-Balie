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

package nl.procura.gba.web.components.layouts.form.document.preview;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.table.TableLayout;

/**
 * Properties tablelayout
 */
public class PropertiesLayout extends TableLayout {

  public PropertiesLayout(byte[] content) {

    try {
      Properties p = new SortedProperties();
      p.load(new ByteArrayInputStream(content));
      create(p);
    } catch (IOException e) {
      throw new ProException(ERROR, "Fout bij het laden");
    }
  }

  public PropertiesLayout(Map<String, String> properties) {
    create(properties);
  }

  public PropertiesLayout(Properties properties) {
    create(properties);
  }

  private void create(Map<String, String> properties) {

    Set<Map.Entry<String, String>> sets = properties.entrySet();

    for (Map.Entry<String, String> set : sets) {

      Object key = set.getKey();
      Object val = set.getValue();

      Column label = addColumn(ColumnType.LABEL);
      label.addText(astr(key));

      Column data = addColumn(ColumnType.DATA);
      data.addText(astr(val));
    }
  }

  private void create(Properties properties) {

    Enumeration<Object> penum = properties.keys();

    while (penum.hasMoreElements()) {

      Object key = penum.nextElement();
      Object val = properties.get(key);

      Column label = addColumn(ColumnType.LABEL);
      label.addText(astr(key));

      Column data = addColumn(ColumnType.DATA);
      data.addText(astr(val));
    }
  }

  /**
   * Sorteer de properties
   */
  private class SortedProperties extends Properties {

    @Override
    @SuppressWarnings("unchecked")
    public synchronized Enumeration keys() {

      Enumeration keysEnum = super.keys();
      Vector keyList = new Vector();

      while (keysEnum.hasMoreElements()) {
        keyList.add(keysEnum.nextElement());
      }

      Collections.sort(keyList);
      return keyList.elements();
    }
  }
}

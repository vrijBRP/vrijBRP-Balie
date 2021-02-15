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

package nl.procura.gbaws.web.vaadin.module.requests.page2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestContent implements Serializable {

  public static final String  DATABRON               = "databron";
  public static final String  AANTAL_WONINGKAARTEN   = "Aantal woningkaarten";
  public static final String  AANTAL_PERSOONSLIJSTEN = "Aantal persoonslijsten";
  public static final String  DUUR_GBAV              = "Duur GBA-V DB";
  public static final String  DUUR_PROCURA           = "Duur Procura DB";
  public static final String  BRON                   = "Bron";
  private static final Logger LOGGER                 = LoggerFactory.getLogger(RequestContent.class);
  private final List<Chapter> chapters               = new ArrayList<>();
  private final String        content;

  public RequestContent(String content) {
    this.content = content;
  }

  public List<Chapter> getChapters() {

    chapters.clear();

    for (final String line : content.split("\n")) {

      if (line.startsWith(">>")) {
        addItem(line.replaceAll(">", ""));
      } else if (line.startsWith(">")) {
        addChapter(line.replaceAll(">", ""));
      } else {
        getLastItem(getLastChapter()).getValue().append(line);
      }
    }

    return chapters;
  }

  public void addChapter(String label) {
    chapters.add(new Chapter(label));
  }

  public void addItem(String label) {
    chapters.get(chapters.size() - 1).addItem(label);
  }

  public Chapter getLastChapter() {
    try {
      return chapters.get(chapters.size() - 1);
    } catch (final RuntimeException e) {

      LOGGER.debug(e.toString());
      return new Chapter();
    }
  }

  public Item getLastItem(Chapter chapter) {
    try {
      return chapter.getItems().get(chapter.getItems().size() - 1);
    } catch (final RuntimeException e) {
      LOGGER.debug(e.toString());
      return new Item();
    }
  }

  public String getItemValue(String label) {

    for (Chapter chapter : getChapters()) {
      for (Item item : chapter.getItems()) {
        if (item.getLabel().equals(label)) {
          return item.getValue().toString();
        }
      }
    }

    return "";
  }

  public class Chapter implements Serializable {

    private String     label = "";
    private List<Item> items = new ArrayList<>();

    public Chapter() {
    }

    public Chapter(String label) {
      setLabel(label);
    }

    public void addItem(String label) {
      getItems().add(new Item(label));
    }

    public List<Item> getItems() {
      return items;
    }

    public void setItems(ArrayList<Item> items) {
      this.items = items;
    }

    public String getLabel() {
      return label;
    }

    public void setLabel(String label) {
      this.label = label;
    }
  }

  public class Item implements Serializable {

    private String        label = "";
    private StringBuilder value = new StringBuilder();

    public Item() {
    }

    public Item(String label) {
      setLabel(label);
    }

    public String getLabel() {
      return label;
    }

    public void setLabel(String label) {
      this.label = label;
    }

    public StringBuilder getValue() {
      return value;
    }

    public void setValue(StringBuilder value) {
      this.value = value;
    }
  }
}

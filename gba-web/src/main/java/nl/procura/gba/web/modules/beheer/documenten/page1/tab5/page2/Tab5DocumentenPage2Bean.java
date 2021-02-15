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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.PrintOptieValue;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Tab5DocumentenPage2Bean implements Serializable {

  public static final String CODE           = "code";
  public static final String NAME           = "name";
  public static final String LOCATION       = "location";
  public static final String COMMAND        = "command";
  public static final String COLOR          = "color";
  public static final String MEDIA          = "media";
  public static final String PRINTER        = "printer";
  public static final String BSM_HASHCODE   = "bsmHashcode";
  public static final String MO_BERICHTTYPE = "moBerichtType";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Naam",
      width = "400px",
      required = true)
  private String name     = "";
  @Field(customTypeClass = GbaTextField.class,
      caption = "Uitvoer naar",
      width = "400px")
  private String location = "";

  @Field(customTypeClass = ProTextArea.class,
      caption = "Commando </br> <b>${file} = bestand</b></br><b>${user.name} = gebruikersnaam</b></br><b>${user.id} = gebruikerscode</b>",
      width = "750px",
      height = "80px")
  private String command = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Bsm referentie",
      width = "400px",
      required = true)
  private String bsmHashcode = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Berichttype",
      width = "400px",
      required = true)
  private String moBerichtType = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "kleur",
      width = "400px")
  @Immediate
  private FieldValue color = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "media",
      width = "400px")
  @Immediate
  private FieldValue media = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Printer",
      width = "400px",
      required = true)
  @Immediate
  private PrintOptieValue printer = new PrintOptieValue(PrintOptieType.COMMAND,
      PrintOptieType.COMMAND.getCode(),
      PrintOptieType.COMMAND.getCode());

  public PrintOptieType getPrinterType() {
    if (printer == null) {
      return PrintOptieType.COMMAND;
    } else {
      return printer.getType();
    }
  }
}

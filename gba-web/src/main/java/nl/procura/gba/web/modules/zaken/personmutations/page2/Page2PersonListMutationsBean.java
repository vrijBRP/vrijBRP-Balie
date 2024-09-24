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

package nl.procura.gba.web.modules.zaken.personmutations.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.personmutations.page2.containers.ContainerItem;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2PersonListMutationsBean implements Serializable {

  public static final String CAT    = "category";
  public static final String SET    = "set";
  public static final String RECORD = "record";
  public static final String ACTION = "action";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Categorie",
      width = "250px")
  @Immediate
  @Select(nullSelectionAllowed = false)
  private ContainerItem<GBACat> category;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gegevensset",
      width = "250px")
  @Immediate
  @Select(nullSelectionAllowed = false)
  private ContainerItem<BasePLSet> set;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Record",
      width = "430px")
  @Immediate
  @Select(nullSelectionAllowed = false)
  private ContainerItem<BasePLRec> record;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Actie",
      width = "430px")
  @Immediate
  @Select(nullSelectionAllowed = false)
  private ContainerItem<PersonListActionType> action;
}

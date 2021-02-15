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

package nl.procura.gbaws.web.rest.v2.personlists;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GbaWsPersonListRequest {

  private List<Long>    ids          = new ArrayList<>();
  private List<Integer> categories   = new ArrayList<>();
  private Integer       maxFindCount = 10;
  private Integer       datasource   = 0;

  private Boolean showArchives      = false;
  private Boolean showRemoved       = false;
  private Boolean showHistory       = false;
  private Boolean showSuspended     = false;
  private Boolean showMutations     = false;
  private Boolean searchOnAddress   = false;
  private Boolean searchRelations   = false;
  private Boolean searchIndications = false;

  private String lastName     = "";
  private String firstName    = "";
  private String prefix       = "";
  private String gender       = "";
  private String dateOfBirth  = "";
  private String title        = "";
  private String street       = "";
  private String hnr          = "";
  private String hnrL         = "";
  private String hnrT         = "";
  private String hnrA         = "";
  private String postalCode   = "";
  private String residence    = "";
  private String municipality = "";

}

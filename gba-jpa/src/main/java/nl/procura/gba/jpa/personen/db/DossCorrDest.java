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

package nl.procura.gba.jpa.personen.db;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "doss_corr_dest")
public class DossCorrDest extends BaseEntity<Long> {

  @Id
  @Column(name = "c_doss_corr_dest")
  private Long cDossCorrDest;

  @Column(name = "comm_type")
  private String communicatieType;

  @Column(name = "organisatie")
  private String organisatie;

  @Column(name = "afdeling")
  private String afdeling;

  @Column(name = "naam")
  private String naam;

  @Column(name = "email")
  private String email;

  @Column(name = "tel")
  private String telefoon;

  @Column(name = "straat")
  private String straat;

  @Column(name = "hnr")
  private Integer hnr;

  @Column(name = "hnr_l")
  private String hnrL;

  @Column(name = "hnr_t")
  private String hnrT;

  @Column(name = "pc")
  private String postcode;

  @Column(name = "plaats")
  private String plaats;

  public static DossCorrDest newDefault(String type) {
    DossCorrDest dossCorrDest = new DossCorrDest();
    dossCorrDest.setCommunicatieType(type);
    dossCorrDest.setOrganisatie("");
    dossCorrDest.setAfdeling("");
    dossCorrDest.setNaam("");
    dossCorrDest.setEmail("");
    dossCorrDest.setTelefoon("");
    dossCorrDest.setStraat("");
    dossCorrDest.setHnr(-1);
    dossCorrDest.setHnrL("");
    dossCorrDest.setHnrT("");
    dossCorrDest.setPostcode("");
    dossCorrDest.setPlaats("");

    return dossCorrDest;
  }
}

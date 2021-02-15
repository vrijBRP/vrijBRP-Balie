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

import java.math.BigDecimal;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "doss_registration")
public class DossRegistration extends BaseEntity {

  private static final long serialVersionUID = 3074236329753381384L;

  @Id // ID is set with value of doss.c_doss
  @Column(name = "c_doss_registration", unique = true, nullable = false, precision = 131_089)
  private Long cDossRegistration;

  // bi-directional one-to-one association to Doss
  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_registration", nullable = false, insertable = false, updatable = false)
  private Doss doss;

  @Column(name = "address_source")
  private String addressSource = "";

  @Column(name = "aon")
  private String aon = "";

  @Column(name = "straat")
  private String street = "";

  @Column(name = "hnr", precision = 131_089)
  private Long houseNumber;

  @Column(name = "hnr_l", length = 1)
  private String houseNumberL = "";

  @Column(name = "hnr_t")
  private String houseNumberT = "";

  @Column(name = "hnr_a")
  private String houseNumberA = "";

  @Column(name = "pc")
  private String postalCode = "";

  @Column(name = "plaats")
  private String residence = "";

  @Column(name = "func_adr", length = 1)
  private String addressFunction = "";

  @Column(name = "toest_bsn")
  private String bsnOfConsentProvider = "";

  @Column(name = "toest_geg", precision = 131_089)
  private Long consentProvider = -1L;

  @Column(name = "toest_anders")
  private String otherConsentProvider = "";

  @Column(name = "aant_pers", precision = 131_089)
  private Long noOfPeople = -1L;

  @Column(name = "duur")
  private String duration = "";

  @Column(name = "origin_situation", precision = 131089)
  private String originSituation;

  @Column(name = "country_of_departure", precision = 131089)
  private BigDecimal countryOfDeparture;

  @Column(name = "aangever_type", length = 1)
  private String declarantType;

  @OneToOne
  @JoinColumn(name = "aangever_c_pers")
  private DossPer declarantPerson;

  @Column(name = "aangever_toelichting")
  private String declarantComment = "";

  @Column(name = "interpreter", length = 1)
  private String interpreter;

  @Column(name = "interpreter_name")
  private String interpreterName;

  @Column(name = "interpreter_language")
  private String interpreterLanguage;
}

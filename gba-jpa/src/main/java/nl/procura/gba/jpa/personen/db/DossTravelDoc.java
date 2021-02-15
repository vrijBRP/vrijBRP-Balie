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

import nl.procura.java.reflection.ReflectionUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "doss_travel_doc")
public class DossTravelDoc extends BaseEntity {

  private static final long serialVersionUID = 6539516171892153452L;

  @Id
  @TableGenerator(name = "table_gen_doss_travel_doc",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_travel_doc",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen_doss_travel_doc")
  @Column(name = "c_doss_travel_doc", unique = true, nullable = false, precision = 131_089)
  private Long cDossTravelDoc;

  @ManyToOne(optional = false)
  @JoinColumn(name = "c_doss_pers")
  private DossPer dossPers;

  @Column(name = "ned_reisdoc", length = 10)
  private String nedReisdoc;

  @Column(name = "doc_nr", length = 40)
  private String docNr;

  @Column(name = "d_verkrijging", precision = 131_089)
  private BigDecimal dVerkrijging;

  @Column(name = "d_end", precision = 131_089)
  private BigDecimal dEnd;

  @Column(name = "aut_verstrek", length = 10)
  private String autVerstrek;

  @Column(name = "aut_verstrek_gem", length = 10)
  private String autVerstrekGem;

  @Column(name = "aut_verstrek_land", length = 10)
  private String autVerstrekLand;

  @Column(name = "doss_gem", length = 10)
  private String dossGem;

  @Column(name = "doss_d_in", precision = 131_089)
  private BigDecimal dossDIn;

  @Column(name = "doss_oms", length = 255)
  private String dossOms;

  // empty constructor required for persistence
  protected DossTravelDoc() {
  }

  public DossTravelDoc(DossPer person) {
    dossPers = ReflectionUtil.deepCopyBean(DossPer.class, person);
    dossOms = "";
  }

}

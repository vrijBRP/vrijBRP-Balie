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

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "zaak_usr")
@NamedQuery(name = "ZaakUsr.findAll",
    query = "SELECT z FROM ZaakUsr z")
public class ZaakUsr extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_zaak_usr",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "zaak_usr",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_zaak_usr")
  @Column(name = "c_zaak_usr",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long id;

  @Column(name = "zaak_id",
      nullable = false)
  private String zaakId;

  @Column(name = "d_in",
      nullable = false)
  private Long dIn;

  @Column(name = "t_in",
      nullable = false)
  private Long tIn;

  @Column(name = "opm",
      nullable = false)
  private String opm;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr_toek")
  private Usr usrToek;
}

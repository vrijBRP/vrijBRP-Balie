
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

package nl.procura.gba.jpa.personenws.db;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gbav_profile")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GbavProfile implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "gbav_profile", nullable = false)
  private String gbavProfile;

  @Column(name = "descr")
  private String descr;

  @Id
  @Column(name = "c_gbav_profile", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Integer cGbavProfile;

  @Column(name = "block")
  private BigInteger block;

  @Column(name = "type")
  private int type = -1;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "gbavProfile")
  @BatchFetch(BatchFetchType.IN)
  private Collection<GbavProfileParm> gbavProfileParmCollection;
}

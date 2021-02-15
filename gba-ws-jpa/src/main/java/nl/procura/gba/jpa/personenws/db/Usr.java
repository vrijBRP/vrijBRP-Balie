
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
@Table(name = "usr")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usr implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "usr", nullable = false)
  private String usr;

  @Column(name = "pw")
  private String pw;

  @Column(name = "admin")
  private BigInteger admin;

  @Column(name = "display")
  private String display;

  @Id
  @Column(name = "c_usr", nullable = false)
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer cUsr;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "usr")
  private Collection<UsrParm> usrParmCollection;

  @OneToMany(mappedBy = "cUsr")
  private Collection<Request> requestCollection;

  @JoinColumn(name = "c_profile", referencedColumnName = "c_profile")
  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  private Profile cProfile;
}

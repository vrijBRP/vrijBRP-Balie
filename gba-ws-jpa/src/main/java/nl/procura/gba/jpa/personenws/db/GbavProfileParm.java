
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

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import lombok.*;

@Entity
@Table(name = "gbav_profile_parm")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GbavProfileParm implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @NonNull
  @EqualsAndHashCode.Include
  protected GbavProfileParmPK gbavProfileParmPK;

  @Column(name = "value")
  private String value;

  @JoinColumn(name = "c_gbav_profile",
      referencedColumnName = "c_gbav_profile",
      insertable = false,
      updatable = false)
  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  private GbavProfile gbavProfile;
}

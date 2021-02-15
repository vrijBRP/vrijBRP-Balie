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

package nl.procura.diensten.gba.wk.procura.templates;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.jpa.probev.db.Vbo;
import nl.procura.standard.ProcuraDate;

public class WKSearchObjectTemplate extends WKTemplateProcura {

  private final int      startDate       = aval(new ProcuraDate().getSystemDate());
  private ZoekArgumenten searchArguments = new ZoekArgumenten();
  private List<Vbo>      vbos            = new ArrayList<>();

  @Override
  public void parse() {
    setVbos(getVboObjects());
  }

  public ZoekArgumenten getSearchArguments() {
    return searchArguments;
  }

  public void setSearchArguments(ZoekArgumenten searchArguments) {
    this.searchArguments = searchArguments;
  }

  public List<Vbo> getVbos() {
    return vbos;
  }

  public void setVbos(List<Vbo> vbos) {
    this.vbos = vbos;
  }

  @SuppressWarnings("unchecked")
  private List<Vbo> getVboObjects() {

    setSql("select o from Vbo o where o.id.dEnd < 0 and o.dIn <= " + startDate);

    if (pos(getSearchArguments().getCode_straat())) {
      addSQL("o.straat.cStraat = :c_straat");
      addProp("c_straat", aval(getSearchArguments().getCode_straat()));
    }

    if (fil(getSearchArguments().getStraatnaam())) {
      addSQL("o.straat.straat = :straat");
      addProp("straat", getDiacsTrimmed(getSearchArguments().getStraatnaam()));
    }

    if (pos(getSearchArguments().getCode_gemeentedeel())) {
      addSQL("o.gemDeel.cGemDeel = :c_gem_deel");
      addProp("c_gem_deel", aval(getSearchArguments().getCode_gemeentedeel()));
    }

    if (fil(getSearchArguments().getGemeentedeel())) {
      addSQL("o.gemDeel.gemDeel = :gemdeel");
      addProp("gemdeel", getDiacsTrimmed(getSearchArguments().getGemeentedeel()));
    }

    if (pos(getSearchArguments().getCode_lokatie())) {
      addSQL("o.locatie.cLocatie = :c_locatie");
      addProp("c_locatie", aval(getSearchArguments().getCode_lokatie()));
    }

    if (fil(getSearchArguments().getLokatie())) {
      addSQL("o.locatie.locatie = :locatie");
      addProp("locatie", getDiacsTrimmed(getSearchArguments().getLokatie()));
    }

    String hnr = getSearchArguments().getHuisnummer();
    String hnrL = getSearchArguments().getHuisletter();
    String hnrT = getSearchArguments().getHuisnummertoevoeging();
    String hnrA = getSearchArguments().getHuisnummeraanduiding();
    String pc = getSearchArguments().getPostcode();

    if (pos(hnr)) {
      addSQL("o.hnr = :hnr");
      addProp("hnr", aval(hnr));
    }

    if (fil(hnrL)) {
      addSQL("(o.hnrL = :hnr_l or o.hnrL = :hnr_l_u)");
      addProp("hnr_l", hnrL.toLowerCase());
      addProp("hnr_l_u", hnrL.toUpperCase());
    }

    if (fil(hnrT)) {
      addSQL("(o.hnrT = :hnr_t or o.hnrT = :hnr_t_u)");
      addProp("hnr_t", hnrT.toLowerCase());
      addProp("hnr_t_u", hnrT.toUpperCase());
    }

    if (fil(hnrA)) {
      addSQL("(o.hnrA = :hnr_a or o.hnrA = :hnr_a_u)");
      addProp("hnr_a", hnrA.toLowerCase());
      addProp("hnr_a_u", hnrA.toUpperCase());
    }

    if (fil(pc)) {
      addSQL("(o.pc = :pc or o.pc = :pc_u)");
      addProp("pc", pc.toLowerCase().replaceAll("\\s+", ""));
      addProp("pc_u", pc.toUpperCase().replaceAll("\\s+", ""));
    }

    if (fil(getSearchArguments().getCode_object())) {
      addSQL("o.id.cVbo = :c_vbo");
      addProp("c_vbo", aval(getSearchArguments().getCode_object()));
    }

    if (fil(getSearchArguments().getVolgcode_einde())) {
      addSQL("o.id.vEnd = :v_end");
      addProp("v_end", aval(getSearchArguments().getVolgcode_einde()));
    }

    if (fil(getSearchArguments().getDatum_einde())) {
      addSQL("o.id.dEnd = :d_end");
      addProp("d_end", aval(getSearchArguments().getDatum_einde()));
    }

    addSQL("o.id.cVbo > 0 order by o.dIn desc");
    if (getProps().isEmpty()) {
      throw new IllegalArgumentException("Geen zoekargumenten ingegeven.");
    }

    Query q = getEntityManager().createQuery(getSql());
    Iterator<?> it = getProps().keySet().iterator();

    while (it.hasNext()) {
      String key = (String) it.next();
      q.setParameter(key, getProps().get(key));
    }

    List<Vbo> list = q.getResultList();
    List<Vbo> postList = new ArrayList<>();

    for (Vbo obj : list) {

      if (emp(hnrL) && (hnrL != null && !"".equals(hnrL)) && fil(obj.getHnrL())) {
        continue;
      }

      if (emp(hnrA) && (hnrA != null && !"".equals(hnrA)) && fil(obj.getHnrA())) {
        continue;
      }

      if (emp(hnrT) && (hnrT != null && !"".equals(hnrT)) && fil(obj.getHnrT())) {
        continue;
      }

      postList.add(obj);
    }

    return postList;
  }
}

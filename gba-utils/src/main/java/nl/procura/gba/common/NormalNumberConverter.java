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

package nl.procura.gba.common;

import static nl.procura.gba.common.NormalNumberConverter.VertaalType.MAAND;
import static nl.procura.gba.common.NormalNumberConverter.VertaalType.NUMMER;
import static nl.procura.standard.Globalfunctions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NormalNumberConverter {

  public NormalNumberConverter() {

    //        toon (4);
    //        toon (19);
    //        toon (22);
    //        toon (33);
    //        toon (34);
    //        toon (40);
    //        toon (140);
    //        toon (340);
    //        toon (1900);
    //        toon (1922);
    //        toon (1934);
    //        toon (1000);
    //        toon (1034);
    //        toon (2000);
    //        toon (2011);
    //        toon (1901);
    //        toon (2001);
    //        toon (2034);
    //        toon (3434);
    //        toon (19434);
    //        System.out.println ("Vandaag: " + toString (Taal.NL, new ProcuraDate ().getDateFormat ()));
  }

  public static void main(String[] args) {
    new NormalNumberConverter();
  }

  public static String toString(Taal taal, Date date) {

    String s = new SimpleDateFormat("ddMMyyyy").format(date);

    int d = aval(s.substring(0, 2));
    int n = aval(s.substring(2, 4));
    int y = aval(s.substring(4, 8));

    return c(taal, d) + " " + m(taal, n) + " " + c(taal, y);
  }

  public static String toString(Taal taal, int nr) {
    return c(taal, nr);
  }

  private static String c(Taal taal, int nr) {

    StringBuilder t = new StringBuilder();

    String s_nr = astr(nr);

    int length = s_nr.length();

    switch (length) {

      case 1:
        t.append(n(taal, nr));

        break;

      case 2: // >= 10

        if (nr < 20) {
          t.append(n(taal, nr)); // Cijfer opzoeken
        } else {

          // 1e getal + 2e getal
          String w1 = n(taal, s(nr, 1));
          String w2 = n(taal, s(nr, 0));
          if (w1.endsWith("e")) {
            t.append(w1 + "ën" + w2);
          } else {
            t.append(w1 + "en" + w2);
          }
        }

        break;

      case 3: // >= 100

        int eersteNr = eersteNr(nr);

        if (eersteNr > 1) {

          t.append(n(taal, eersteNr));
        }

        t.append(n(taal, 100) + " " + c(taal, x(nr, 1, 2)));

        break;

      case 4: // >= 1000

        int d = x(nr, 0, 1);

        if (d % 10 == 0) { // Duizend getal

          if (d > 10) { // > 1000

            t.append(n(taal, eersteNr(nr)));
          }

          t.append(n(taal, 1000) + " ");
        } else {

          t.append(c(taal, eersteTwee(nr)) + n(taal, 100) + " ");
        }

        t.append(c(taal, x(nr, 2, 3)));

        break;

      case 5: // >= 10000

        t.append(c(taal, x(nr, 0, 1)) + n(taal, 1000) + " " + c(taal, x(nr, 2, 4)));
        break;

      default:
        break;
    }

    return t.toString().replaceAll(" en", " ").replaceAll("^en", "").trim();
  }

  private static int eersteNr(int nr) {
    return x(nr, 0, 0);
  }

  private static int eersteTwee(int nr) {
    return x(nr, 0, 1);
  }

  /**
   * substr
   */
  private static int x(int nr, int s_index, int e_index) {

    String s_nr = astr(nr);

    String n_nr = s_nr.substring(s_index, e_index + 1);

    return aval(n_nr);
  }

  /**
   * Met nulwaarden
   */
  private static int s(int nr, int s_index) {

    String s_nr = astr(nr);

    String n_nr = s_nr.substring(s_index, s_index + 1);

    int length = s_nr.length() - s_index;

    return aval(pad_right(n_nr, "0", length));
  }

  private static String get(VertaalType type, Taal taal, int nr) {

    for (VertaalElement e : VertaalElement.values()) {
      if (e.getType() == type && e.getNr() == nr) {
        if (taal == Taal.NL) {
          return e.getNederlands();
        }
        return e.getFries();
      }
    }

    return "";
  }

  public static String n(Taal taal, int nr) {
    return get(NUMMER, taal, nr);
  }

  public static String m(Taal taal, int nr) {
    return get(MAAND, taal, nr);
  }

  protected void toon(int nr) {
    Taal nl = Taal.NL;
    Taal fy = Taal.FRIES;
    System.out.println(pad_left(astr(nr), " ", 5) + String.format(": %s - %s", c(nl, nr), c(fy, nr)));
  }

  public enum Taal {
    NL,
    FRIES
  }

  public enum VertaalType {
    NUMMER,
    MAAND
  }

  public enum VertaalElement {
    /*
     * Nummers
     */

    EEN(NUMMER, 1, "een", "ien"),
    TWEE(NUMMER, 2, "twee", "twa"),
    DRIE(NUMMER, 3, "drie", "trije"),
    VIER(NUMMER, 4, "vier", "fjouwer"),
    VIJF(NUMMER, 5, "vijf", "fiif"),
    ZES(NUMMER, 6, "zes", "seis"),
    ZEVEN(NUMMER, 7, "zeven", "sân"),
    ACHT(NUMMER, 8, "acht", "acht"),
    NEGEN(NUMMER, 9, "negen", "njoggen"),
    TIEN(NUMMER, 10, "tien", "tsien"),
    ELF(NUMMER, 11, "elf", "alve"),
    TWAALF(NUMMER, 12, "twaalf", "tolve"),
    DERTIEN(NUMMER, 13, "dertien", "trettjin"),
    VEERTIEN(NUMMER, 14, "veertien", "fjirtjin"),
    VIJFTIEN(NUMMER, 15, "vijftien", "fyftjin"),
    ZESTIEN(NUMMER, 16, "zestien", "sechstjin"),
    ZEVENTIEN(NUMMER, 17, "zeventien", "santjin"),
    ACHTTIEN(NUMMER, 18, "achttien", "achttjin"),
    NEGENTIEN(NUMMER, 19, "negentien", "njoggentjin"),
    TWINTIG(NUMMER, 20, "twintig", "tweintich"),
    DERTIG(NUMMER, 30, "dertig", "tritich"),
    VEERTIG(NUMMER, 40, "veertig", "fjirtich"),
    VIJFTIG(NUMMER, 50, "vijftig", "fyftich"),
    ZESTIG(NUMMER, 60, "zestig", "sechstich"),
    ZEVENTIG(NUMMER, 70, "zeventig", "santich"),
    TACHTIG(NUMMER, 80, "tachtig", "tachtich"),
    NEGENTIG(NUMMER, 90, "negentig", "njoggentich"),
    HONDERD(NUMMER, 100, "honderd", "hûndert"),
    DUIZEND(NUMMER, 1000, "duizend", "tûzen"),
    /*
     * Maanden
     */

    JANUARI(MAAND, 1, "januari", "jannewaris"),
    FEBRUARI(MAAND, 2, "februari", "febrewaris"),
    MAART(MAAND, 3, "maart", "maart"),
    APRIL(MAAND, 4, "april", "april"),
    MEI(MAAND, 5, "mei", "maaie"),
    JUNI(MAAND, 6, "juni", "juny"),
    JULI(MAAND, 7, "juli", "july"),
    AUGUSTUS(MAAND, 8, "augustus", "augustus"),
    SEPTEMBER(MAAND, 9, "september", "septimber"),
    OKTOBER(MAAND, 10, "oktober", "oktober"),
    NOVEMBER(MAAND, 11, "november", "novimber"),
    DECEMBER(MAAND, 12, "december", "desimber");

    private VertaalType type;
    private int         nr;
    private String      nederlands;
    private String      fries;

    VertaalElement(VertaalType type, int nr, String nederlands, String fries) {
      this.type = type;
      this.nr = nr;
      this.nederlands = nederlands;
      this.fries = fries;
    }

    public VertaalType getType() {
      return type;
    }

    public void setType(VertaalType type) {
      this.type = type;
    }

    public int getNr() {
      return nr;
    }

    public void setNr(int nr) {
      this.nr = nr;
    }

    public String getNederlands() {
      return nederlands;
    }

    public void setNederlands(String nederlands) {
      this.nederlands = nederlands;
    }

    public String getFries() {
      return fries;
    }

    public void setFries(String fries) {
      this.fries = fries;
    }
  }
}

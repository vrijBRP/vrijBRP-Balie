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

package nl.procura.gba.web.modules.beheer.overig;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.NavFile;
import nl.procura.commons.core.exceptions.ProException;

public class NavigationSorterTest {

  private static final File d  = new File("Test");
  private static final File d1 = new File(d, "Testje");
  private static final File d2 = new File(d, "Testje2");
  private static final File d3 = new File(d1, "Testje3");
  private static final File f  = new File(d, "Test.txt");
  private static final File f1 = new File(d, "Test1.txt");
  private static final File f2 = new File(d, "Test2.txt");
  private static final File f3 = new File(d1, "Test3.txt");
  private static final File f4 = new File(d2, "Test4.txt");
  private static final File f5 = new File(d3, "Test5.txt");

  @BeforeClass
  public static void create() {
    try {
      d.mkdir();
      d1.mkdir();
      d2.mkdir();
      d3.mkdir();

      f.createNewFile();
      f1.createNewFile();
      f2.createNewFile();
      f3.createNewFile();
      f4.createNewFile();
      f5.createNewFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterClass
  public static void clean() {
    try {
      f5.delete();
      f4.delete();
      f3.delete();
      f2.delete();
      f1.delete();
      f.delete();

      d3.delete();
      d2.delete();
      d1.delete();
      d.delete();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAdd() {
    String a = "hoi";
    String b = "test";

    NavigationSorter nav = new NavigationSorter();
    nav.add(a, b);
    assertEquals(1, nav.getNavFiles().size());
  }

  @Test
  public void testInvalidFileAdd() {
    File f = null;
    NavigationSorter nav = new NavigationSorter();
    nav.add("", f);
    assertEquals(0, nav.getNavFiles().size());
  }

  @Test
  public void testValidPathFileAdd() {
    NavigationSorter nav = new NavigationSorter();
    nav.add("path", d);
    assertEquals(1, nav.getNavFiles().size());
  }

  @Test
  public void testGetPath1() {

    NavigationSorter nav = new NavigationSorter();
    String filePad = nav.getPath(f1, d);
    assertEquals("", filePad);
  }

  @Test
  public void testGetPath2() {
    NavigationSorter nav = new NavigationSorter();
    String filePad = nav.getPath(f3, d);
    assertEquals("Testje", filePad);
  }

  @Test(expected = ProException.class)
  public void testInvalidFileGetPath() {
    NavigationSorter nav = new NavigationSorter();
    nav.getPath(f, d1);
  }

  @Test
  public void testNullFileGetPath() {
    NavigationSorter nav = new NavigationSorter();
    assertEquals(null, nav.getPath(null, d));
  }

  @Test
  public void testTwoDirGetPath() {
    NavigationSorter nav = new NavigationSorter();
    String filePad = nav.getPath(d1, d);
    assertEquals("", filePad);
  }

  @Test
  public void testGetAllFilesValid() {
    List<File> test = new ArrayList<File>();
    test.add(f);
    test.add(f1);
    test.add(f2);
    test.add(f3);
    test.add(f4);
    test.add(f5);
    Collections.sort(test);

    Collection<File> allFiles = FileUtils.listFiles(d, null, true);

    List<File> files = new ArrayList<File>();
    for (File f : allFiles) {
      files.add(f);
    }
    Collections.sort(files);

    assertArrayEquals(test.toArray(), files.toArray());
  }

  @Test
  public void testGetAllFilesInvalidDirectory() {
    FileUtils.listFiles(new File(""), null, true);
  }

  @Test
  public void testIfListFilesAlsoReturnsDirectories() { // listFiles() geeft geen directories terug!!

    List<File> expectedResult = new ArrayList<File>();

    Collection<File> allFiles = FileUtils.listFiles(d, null, true);
    List<File> dirs = new ArrayList<File>();

    for (File file : allFiles) {
      if (file.isDirectory()) {
        dirs.add(file);
      }
    }

    assertArrayEquals(expectedResult.toArray(), dirs.toArray());
  }

  @Test
  public void testGetDirsFromNavigationSorter() {

    NavigationSorter nav = new NavigationSorter();
    Collection<File> allFiles = FileUtils.listFiles(d, null, true);

    for (File f : allFiles) {
      String filePath = nav.getPath(f, d);
      nav.add(filePath, f);
    }

    Set<Dir> navDirs = nav.getDirs("", d.getPath());

    Set<Dir> expectedDirs = new TreeSet<NavigationSorter.Dir>();
    expectedDirs.add(nav.new Dir("Test/Testje", "Testje"));
    expectedDirs.add(nav.new Dir("Test/Testje2", "Testje2"));

    assertEquals(navDirs, expectedDirs);
  }

  @Test
  public void navigationSorterReturnsExistingDirectories() {

    NavigationSorter nav = new NavigationSorter();
    Collection<File> allFiles = FileUtils.listFiles(d, null, true);

    for (File f : allFiles) {
      String filePath = nav.getPath(f, d);
      nav.add(filePath, f);
    }

    Set<Dir> navDirs = nav.getDirs("", d.getPath());

    for (Dir dir : navDirs) {
      assertEquals(true, dir.getFile().exists());
    }
  }

  @Test
  public void testGetAllFiles() {

    NavigationSorter nav = new NavigationSorter();
    Collection<File> allFiles = FileUtils.listFiles(d, null, true);

    for (File f : allFiles) {
      String filePath = nav.getPath(f, d);
      nav.add(filePath, f);
    }

    List<NavFile> navFiles = nav.getAllFiles(nav.getPath(d2, d) + "/" + d2.getName());
    List<File> resultFiles = new ArrayList<File>();

    for (NavFile navFile : navFiles) {
      if (navFile.getObj() instanceof File) {
        File f = (File) (navFile.getObj());
        resultFiles.add(f);
      }
    }

    List<File> expectedFiles = new ArrayList<File>();
    expectedFiles.add(f4);

    assertArrayEquals(expectedFiles.toArray(), resultFiles.toArray());
  }

  @Test
  public void testGetAllFiles2() {

    NavigationSorter nav = new NavigationSorter();
    Collection<File> allFiles = FileUtils.listFiles(d, null, true);

    for (File f : allFiles) {
      String filePath = nav.getPath(f, d);
      nav.add(filePath, f);
    }

    List<NavFile> navFiles = nav.getAllFiles(nav.getPath(d, d));
    List<File> resultFiles = new ArrayList<File>();

    for (NavFile navFile : navFiles) {
      if (navFile.getObj() instanceof File) {
        File f = (File) (navFile.getObj());
        resultFiles.add(f);
      }
    }
    Collections.sort(resultFiles);

    List<File> expectedFiles = new ArrayList<File>();
    expectedFiles.add(f);
    expectedFiles.add(f1);
    expectedFiles.add(f2);
    Collections.sort(expectedFiles);

    assertArrayEquals(expectedFiles.toArray(), resultFiles.toArray());
  }

  @Test
  public void testGetAllFiles3() {

    NavigationSorter nav = new NavigationSorter();
    Collection<File> allFiles = FileUtils.listFiles(d, null, true);

    for (File f : allFiles) {
      String filePath = nav.getPath(f, d);
      nav.add(filePath, f);
    }

    List<NavFile> navFiles = nav.getAllFiles(nav.getPath(d1, d) + "/" + d1.getName());
    List<File> resultFiles = new ArrayList<File>();

    for (NavFile navFile : navFiles) {
      if (navFile.getObj() instanceof File) {
        File f = (File) (navFile.getObj());
        resultFiles.add(f);
      }
    }
    Collections.sort(resultFiles);

    List<File> expectedFiles = new ArrayList<File>();
    expectedFiles.add(f3);
    expectedFiles.add(f5);

    assertArrayEquals(expectedFiles.toArray(), resultFiles.toArray());
  }

  @Test
  public void testGetAllFiles4() {

    NavigationSorter nav = new NavigationSorter();
    Collection<File> allFiles = FileUtils.listFiles(d, null, true);

    for (File f : allFiles) {
      String filePath = nav.getPath(f, d);
      nav.add(filePath, f);
    }

    List<NavFile> navFiles = nav.getAllFiles(nav.getPath(d3, d) + "/" + d3.getName());
    List<File> resultFiles = new ArrayList<File>();

    for (NavFile navFile : navFiles) {
      if (navFile.getObj() instanceof File) {
        File f = (File) (navFile.getObj());
        resultFiles.add(f);
      }
    }
    Collections.sort(resultFiles);

    List<File> expectedFiles = new ArrayList<File>();
    expectedFiles.add(f5);

    assertArrayEquals(expectedFiles.toArray(), resultFiles.toArray());
  }
}

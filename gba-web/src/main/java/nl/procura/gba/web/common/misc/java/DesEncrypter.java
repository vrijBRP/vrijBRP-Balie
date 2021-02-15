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

package nl.procura.gba.web.common.misc.java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import nl.procura.standard.security.Base64;

public class DesEncrypter {

  private final byte[] salt           = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35,
      (byte) 0xE3, (byte) 0x03 };
  private final int    iterationCount = 19;
  private Cipher       ecipher;
  private Cipher       dcipher;

  public DesEncrypter(String passPhrase) {

    try {

      // Create the key
      KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
      SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

      ecipher = Cipher.getInstance("PBEWithMD5AndDES");
      dcipher = Cipher.getInstance("PBEWithMD5AndDES");

      // Prepare the parameter to the ciphers
      AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

      // Create the ciphers
      ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
      dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String decrypt(String str) {

    try {
      // Decode base64 to get bytes
      byte[] dec = Base64.decode(str);

      // Decrypt
      byte[] utf8 = dcipher.doFinal(dec);

      // Decode using utf-8
      return new String(utf8, StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public Object deserializeFromFile(String filename) {
    try {
      try (FileInputStream f_in = new FileInputStream(filename)) {
        try (ObjectInputStream obj_in = new ObjectInputStream(f_in)) {
          return obj_in.readObject();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void serializeToFile(Object object, String filename) {
    try {
      try (FileOutputStream f_out = new FileOutputStream(filename)) {
        try (ObjectOutputStream obj_out = new ObjectOutputStream(f_out)) {
          obj_out.writeObject(object);
          obj_out.flush();
          f_out.flush();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

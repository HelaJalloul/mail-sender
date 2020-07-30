/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendmails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 *
 * @author jalloul
 */
public class SendMails {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new SendMails().walkFile(new File (Constants.MAILS_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void walkFile(File file) {
        BufferedReader br = null;
        InputStreamReader fr = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            fr = new InputStreamReader(fis);
            br = new BufferedReader(fr);
            FileOutputStream fos;
            String sCurrentLine;
            File out;
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                try {
                    new SendInlineImagesInEmail().sendMail(sCurrentLine);
                    i++;
                    System.out.println(i + " : " + sCurrentLine);
                } catch (AddressException ex) {
                    ex.printStackTrace();
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            fr.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

/*
 * Centro de Estudios sobre Desarrollo Econ�mico (CEDE) - Universidad de los Andes
 * Octubre 18 de 2006
 *
 *********************************************************
 * SPADIES                                               *
 * Sistema para la Prevenci�n y An�lisis de la Deserci�n *
 * en las Instituciones de Educaci�n Superior            *
 *********************************************************
 * Autores del c�digo fuente (�ltima versi�n):           *
 *  Alejandro Sotelo Ar�valo   alejandrosotelo@gmail.com *
 *  Andr�s C�rdoba Melani      acordoba@gmail.com        *
 *********************************************************
 *
 * Para informaci�n de los participantes del proyecto v�ase el "Acerca De" de la aplicaci�n.
 * 
 * La modificaci�n del c�digo fuente est� prohibida sin permiso expl�cito por parte de
 * los autores o del Ministerio de Educaci�n Nacional de la Rep�blica de Colombia.
 *
 */
package spadies.gui;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import spadies.gui.frames.VentanaCarga;
import spadies.gui.frames.VentanaPrincipalSimple;
import spadies.util.Constantes;

public class PrincipalSimple {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        String[] w={"Label","Button","ComboBox","CheckBox","Table","List","TextField","TextArea","EditorPane","TextPane","FileChooser","Menu","MenuItem","Popup"};
        for (String s:w) UIManager.put(s+".font",new Font("Dialog",Font.PLAIN,12));
        UIManager.put("SplitPane.dividerSize",10);
        UIManager.put("ComboBox.disabledForeground",new javax.swing.plaf.ColorUIResource(new Color(160,160,160)));
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        VentanaPrincipalSimple.getInstance().setVisible(true);
        Constantes.cargarArchivoConfiguracion();
        Constantes.cargarArchivoFiltroIES();
        try {
          Constantes.logSPADIES.init();
        } catch (FileNotFoundException e) {
          System.err.println("Error iniciando el log de SPADIES"+"");
        }
        try {Thread.sleep(100);} catch (Throwable th) {}
        new VentanaCarga(VentanaPrincipalSimple.getInstance()).ejecutar();
      }
    });
  }
}

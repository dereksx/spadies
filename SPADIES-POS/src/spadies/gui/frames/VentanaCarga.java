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
package spadies.gui.frames;

import java.text.*;

import javax.swing.JFrame;

import spadies.gui.util.*;
import spadies.util.*;
import spadies.kernel.*;

@SuppressWarnings("serial")
public class VentanaCarga extends MyDialogProgreso {
  private static final DecimalFormat df=new DecimalFormat("0.000");
  public VentanaCarga(JFrame frm) {
    super(frm,"Cargando la informaci�n de la carpeta "+Constantes.carpetaDatos.getPath(),true);
  }
  public void ejecutar() {
    setVisible(true);
    new Thread() {
      public void run() {
        try {
          long tiempo=KernelSPADIES.getInstance().cargar(Constantes.carpetaDatos,Constantes.cargaDatosPersonales, VentanaCarga.this);
          RutinasGUI.desplegarInformacion(VentanaCarga.this,"<html>Se cargaron exitosamente los datos de la carpeta "+CajaDeHerramientas.stringToHTML(Constantes.carpetaDatos.getPath())+" en "+df.format(0.001*tiempo).replace(',','.')+" segundos. Memoria: "+CajaDeHerramientas.usoMemoria()+"</html>");
        }
        catch (MyException ex) {
          RutinasGUI.desplegarError(VentanaCarga.this,"<html>"+CajaDeHerramientas.stringToHTML(ex.getMessage())+"</html>");
        }
        VentanaCarga.this.dispose();
      }
    }.start();
  }
}

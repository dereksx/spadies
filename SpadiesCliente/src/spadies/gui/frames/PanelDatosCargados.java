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

import java.awt.*;
import java.util.*;
import javax.swing.*;
import spadies.gui.util.*;
import spadies.kernel.*;
import spadies.util.variables.*;

@SuppressWarnings("serial")
public class PanelDatosCargados extends JPanel {
  private final MyPanelTabla panelTabla=new MyPanelTabla(new InfoTabla(null,null,null),500,300,130);
  private final KernelSPADIES kernel=KernelSPADIES.getInstance();
  private final MyLabel labelTitulo=new MyLabel("<html>N�mero de estudiantes que aparecen como prim�paros en las IES seleccionadas:<p><p><b>Conteo de estudiantes en los archivos csv de entrada:</b></html>");
  public PanelDatosCargados() {
    setLayout(new BorderLayout());
    add(new MyFlowPane(0,0,labelTitulo),BorderLayout.NORTH);
    add(new MyScrollPane(panelTabla,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,-1,-1),BorderLayout.CENTER);
  }
  public void actualizar() {
    Object[] res={null,null,null};
    int numEst=0;
    if (VentanaPrincipal.getInstance().estaSeleccionandoIES()) {
      Filtro[] filtrosIES=VentanaPrincipal.getInstance().getSeleccionPura(new EnumMap<Variable,Filtro>(Variable.class)).values().toArray(new Filtro[0]);
      res=kernel.getTablaCantidadArchivos(filtrosIES);
      numEst=kernel.getCantidadEstudiantes(filtrosIES);
    }
    panelTabla.setDatos(new InfoTabla((String[][])(res[0]),(String[][])(res[1]),(String[])(res[2])));
    labelTitulo.setText("<html>N�mero de estudiantes que aparecen como prim�paros en las IES seleccionadas: "+(numEst==0?"-":(""+numEst))+"<p><p><b>Conteo de estudiantes en los archivos csv de entrada:</b></html>");
  }
}

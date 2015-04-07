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

@SuppressWarnings("serial")
public class VentanaGrafica extends JFrame implements Observer {
  private final KernelSPADIES kernel=KernelSPADIES.getInstance();
  public VentanaGrafica(JFrame padre, String titulo, JPanel imagen1, JPanel imagen2, InfoTabla tabla1, InfoTabla tabla2, JComponent labelDescripcion) {
    super(titulo);
    setIconImage(padre.getIconImage());
    kernel.addObserver(this);
    JTabbedPane panelCentro=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
    {
      if (imagen1!=null) panelCentro.addTab("Cantidades",imagen1);
      if (imagen2!=null) panelCentro.addTab("Porcentajes",imagen2);
      if (tabla1!=null) panelCentro.addTab("Tabla de cantidades",crearPanelTabla(tabla1));
      if (tabla2!=null) panelCentro.addTab("Tabla de porcentajes",crearPanelTabla(tabla2));
    }
    if (imagen1!=null && imagen2!=null) panelCentro.setSelectedIndex(1);
    Component compDerecha=(labelDescripcion==null)?null:new MyScrollPane(labelDescripcion,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS,-1,-1);
    Component compPrincipal=(compDerecha==null)?panelCentro:new MySplitPane(JSplitPane.HORIZONTAL_SPLIT,true,panelCentro,compDerecha,true);
    {
      setContentPane(new MyBorderPane(false,0,0,0,0,null,null,compPrincipal,null,null));
      Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
      setResizable(true);
      setSize(new Dimension(d.width-200,d.height-200));
      if (labelDescripcion!=null) {
        MySplitPane msp=(MySplitPane)compPrincipal;
        msp.setResizeWeight(1.0);
        msp.setDividerLocation(getWidth()-300);
      }
      setLocation((int)((d.width-getWidth())/2),(int)((d.height-getHeight())/2));
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
  }
  public void update(Observable obs, Object arg) {
    if (arg.equals("CARGA")) dispose();
  }
  public void setVisible(boolean b) {
    if (b) super.setVisible(true);
    else   dispose();
  }
  public void dispose() {
    kernel.deleteObserver(this);
    super.setVisible(false);
    super.dispose();
  }
  private JPanel crearPanelTabla(InfoTabla tabla) {
    return new MyBorderPane(false,0,0,0,0,null,null,new MyPanelTabla(tabla,100,100,60),null,null);
  }
}

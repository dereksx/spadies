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
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeSelectionModel;

import org.jfree.chart.*;
import spadies.gui.util.*;
import spadies.gui.util.RutinasGUI.CategorizacionVariables;
import spadies.gui.graficas.*;
import spadies.kernel.*;
import spadies.util.*;
import spadies.util.variables.*;

@SuppressWarnings("serial")
public class PanelConsultasDesercionIntersemestral extends JPanel implements ActionListener {
  private static final CategorizacionVariables cvar = RutinasGUI.categorizacionVariables(false);
  private static final Variable[] criterios = cvar.criterios;
  private static final int lims[][] = cvar.lims;
  private static final String titulos[] = cvar.titulos;
  private final int tam=criterios.length,tamNormal=tam-cvar.textras;
  private final MyPanelSeleccion[] panelsSeleccion=new MyPanelSeleccion[tam];
  private final JCheckBox[] checksDiferenciacion=new JCheckBox[tam];
  private final MyEditorPane labelSeleccion=new MyEditorPane(false);
  //private final JTabbedPane panelVariables=new JTabbedPane(JTabbedPane.LEFT,JTabbedPane.SCROLL_TAB_LAYOUT);
  private final JPanel panelVariables = new JPanel(new GridLayout());
  private DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(new OpcionVariable("Variables"));
  private DefaultTreeModel dtm = new DefaultTreeModel(dmt);
  private final JTree menuVariables = new JTree(/*dmt*/dtm);
  //private final JComponent[] tabsExtras=new JComponent[5];
  private final DefaultMutableTreeNode[] nodosExtras=new DefaultMutableTreeNode[cvar.textras];
  //private final JComponent[] tabsExtras=new JComponent[5];
  private final JButton[] botones=new JButton[2];
  private boolean cuadrando=false;
  private final int posExtra;
  public PanelConsultasDesercionIntersemestral() {
    for (int i=0; i<tam; i++) {
      panelsSeleccion[i]=new MyPanelSeleccion(criterios[i].items,true,280,200);
      {
        JCheckBox cb=new JCheckBox("Diferenciado?",false);
        checksDiferenciacion[i]=cb;
        cb.setFont(cb.getFont().deriveFont(Font.BOLD));
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width,20));
      }
    }
    final CardLayout cl = new CardLayout();
    final JPanel panelesSelecciones = new JPanel(cl);
    menuVariables.addTreeSelectionListener(new TreeSelectionListener(){
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode dms = (DefaultMutableTreeNode)menuVariables.getLastSelectedPathComponent();
        if (dms==null) return;
        OpcionVariable opcion = (OpcionVariable) dms.getUserObject();
        int num = opcion.getNumero(); cl.show(panelesSelecciones, num==-1?"":String.valueOf(num));
      }
    });
    menuVariables.addTreeWillExpandListener(new TreeWillExpandListener(){
      public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        if(dmt.getRoot()==event.getPath().getLastPathComponent()) throw new ExpandVetoException(event);
      }
      public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {}
    });
    menuVariables.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    menuVariables.putClientProperty("JTree.lineStyle", false);

    panelesSelecciones.add(new JPanel(),"");
    panelVariables.add(menuVariables);
    panelVariables.add(panelesSelecciones);
    int pos = 0;
    for (int j=0,jT=lims.length; j<jT; j++) {
      if (titulos[j]==null) {
        for (int i=lims[j][0]; i<=lims[j][1]; i++) {
          //JComponent comp=new MyBoxPane(BoxLayout.Y_AXIS,panelsSeleccion[i]);
          JPanel comp = new MyBoxPane(BoxLayout.Y_AXIS);
          comp.add(new MyFlowPane(0,0,checksDiferenciacion[i]));
          comp.add(panelsSeleccion[i]);
          panelesSelecciones.add(comp, String.valueOf(i));
          DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(new OpcionVariable(i, criterios[i].nombre));
          if (i<tamNormal) {
            dtm.insertNodeInto(nodo, dmt, pos++);
            //dmt.add(nodo);
          }
          else nodosExtras[i-tamNormal] = nodo;
        }
        continue;
      }
      DefaultMutableTreeNode nOpcion = new DefaultMutableTreeNode(new OpcionVariable(titulos[j]));
      //dmt.add(nOpcion);
      dtm.insertNodeInto(nOpcion, dmt, pos++);
      int pos2 = 0;
      for (int i=lims[j][0]; i<=lims[j][1]; i++) {
        dtm.insertNodeInto(new DefaultMutableTreeNode(new OpcionVariable(i, criterios[i].nombre)), nOpcion, pos2++);
        //nOpcion.add(new DefaultMutableTreeNode(new OpcionVariable(i, criterios[i].nombre)));
        JPanel panelSub = new MyBoxPane(BoxLayout.Y_AXIS);
        //panelSub.add(new MyFlowPane(0,0,new MyLabel(criterios[i].nombre)));
        panelSub.add(new MyFlowPane(0,0,checksDiferenciacion[i]));
        panelSub.add(panelsSeleccion[i]);
        panelesSelecciones.add(panelSub, String.valueOf(i));
      }
    }
    posExtra  = pos;
    menuVariables.expandRow(0);
    /*for (int j=0,jT=lims.length; j<jT; j++) {
      if (titulos[j]==null) {
        for (int i=lims[j][0]; i<=lims[j][1]; i++) {
          JComponent comp=new MyBoxPane(BoxLayout.Y_AXIS,new MyFlowPane(0,0,checksDiferenciacion[i]),panelsSeleccion[i]);
          if (i<tamNormal) panelVariables.addTab(criterios[i].nombre,comp);
          else             tabsExtras[i-tamNormal]=comp;
        }
        continue;
      }
      JPanel panel=new MyBoxPane(BoxLayout.Y_AXIS);
      for (int i=lims[j][0]; i<=lims[j][1]; i++) {
        panel.add(new MyFlowPane(0,0,new MyLabel(criterios[i].nombre)));
        panel.add(new MyFlowPane(0,0,checksDiferenciacion[i]));
        panel.add(panelsSeleccion[i]);
        if (i<lims[j][1]) panel.add(Box.createVerticalStrut(15));
      }
      panelVariables.addTab(titulos[j],panel);
    }*/
    botones[0]=new JButton("Restablecer selecci�n");
    botones[1]=new JButton("Generar gr�fica");
    for (JButton b:botones) {
      RutinasGUI.configurarBoton(b,new Color(245,245,255),16,-1,23);
      b.addActionListener(this);
    }
    TableModelListener tml=new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        actualizarLabelSeleccion();
      }
    };
    ActionListener al=new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actualizarLabelSeleccion();
      }
    };
    for (int i=0; i<tam; i++) panelsSeleccion[i].getTabla().getModel().addTableModelListener(tml);
    for (int i=0; i<tam; i++) checksDiferenciacion[i].addActionListener(al);
    JPanel panelBotones=new JPanel(new GridLayout(1,2));
    for (JButton b:botones) panelBotones.add(b);
    setLayout(new BorderLayout());
    add(new MyBorderPane(false,0,0,0,0,null,panelVariables,new MyScrollPane(labelSeleccion,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS,-1,-1),null,null),BorderLayout.CENTER);
    add(panelBotones,BorderLayout.SOUTH);
  }
  public void actualizar() {
    cuadrando=true;
    for (DefaultMutableTreeNode nodo:nodosExtras) try {
      dtm.removeNodeFromParent(nodo);
    } catch (IllegalArgumentException e) {}
    //for (DefaultMutableTreeNode nodo:nodosExtras) if (dmt.isNodeChild(nodo)) dmt.remove(nodo);
    int pos = posExtra;
    for (int i=0; i<tam; i++) {
      Variable v=criterios[i];
      if (Constantes.variablesActualizables.contains(v)) {
        panelsSeleccion[i].setValoresManteniendoSeleccion(v.items,true);
      }
      else if (i>=tamNormal) {
        panelsSeleccion[i].setValores(v.items,true);
        ((OpcionVariable)nodosExtras[i-tamNormal].getUserObject()).setNombre(v.nombre);
        if (v.items.length>0) dtm.insertNodeInto(nodosExtras[i-tamNormal], dmt, pos++);
        //if (v.items.length>0) dmt.add(nodosExtras[i-tamNormal]);
      }
    }
    /*for (JComponent comp:tabsExtras) panelVariables.remove(comp);
    for (int i=0; i<tam; i++) {
      Variable v=criterios[i];
      if (v==Variable.PROGRAMA_EST || v==Variable.PERIODO_INGRESO_EST || v==Variable.PERIODO_MATRICULADO_PER || v==Variable.NUMERO_SEMESTRE_PER) {
        panelsSeleccion[i].setValoresManteniendoSeleccion(v.items,true);
      }
      else if (i>=tamNormal) {
        panelsSeleccion[i].setValores(v.items,true);
        if (v.items.length>0) panelVariables.addTab("<html><font color=\"#008000\">"+CajaDeHerramientas.stringToHTML(criterios[i].nombre)+"</font></html>",tabsExtras[i-tamNormal]);
      }
    }*/
    cuadrando=false;
    actualizarLabelSeleccion();
  }
  private void actualizarLabelSeleccion() {
    if (!cuadrando) labelSeleccion.setText(getStringSeleccion());
  }
  private Object[] getComponentesParametros() {
    VentanaPrincipal vp=VentanaPrincipal.getInstance();
    MyPanelSeleccion[] arrPS=CajaDeHerramientas.concatenarArreglos(MyPanelSeleccion.class,vp.getPanelsSeleccion(),panelsSeleccion);
    JCheckBox[] arrCB=CajaDeHerramientas.concatenarArreglos(JCheckBox.class,vp.getChecksDiferenciacion(),checksDiferenciacion);
    Variable[] arrVE=CajaDeHerramientas.concatenarArreglos(Variable.class,vp.getCriterios(),criterios);
    return new Object[]{arrPS,arrCB,arrVE};
  }
  private String getStringSeleccion() {
    Object[] params=getComponentesParametros();
    return RutinasGUI.getStringSeleccion((MyPanelSeleccion[])(params[0]),(JCheckBox[])(params[1]),(Variable[])(params[2]),null,VentanaPrincipal.getInstance().estaSeleccionandoAgregado());
  }
  public void actionPerformed(ActionEvent e) {
    int ind=CajaDeHerramientas.searchSame(botones,e.getSource());
    if (ind==0) {
      cuadrando=true;
      for (int i=0; i<tam; i++) {
        panelsSeleccion[i].seleccionar(true);
        checksDiferenciacion[i].setSelected(false);
      }
      cuadrando=false;
      actualizarLabelSeleccion();
    }
    else if (ind==1) {
      Component cmDespl=this;
      try {
        boolean ag=VentanaPrincipal.getInstance().estaSeleccionandoAgregado();
        Object[] params=getComponentesParametros();
        MyPanelSeleccion[] arrPS=(MyPanelSeleccion[])(params[0]);
        JCheckBox[] arrCB=(JCheckBox[])(params[1]);
        Variable[] arrVE=(Variable[])(params[2]);
        EnumSet<Variable> mDiferenciados=EnumSet.noneOf(Variable.class);
        EnumMap<Variable,Filtro> mFiltros=new EnumMap<Variable,Filtro>(Variable.class);
        RutinasGUI.getSeleccion(arrVE,arrCB,arrPS,mDiferenciados,mFiltros);
        if (ag) {
          mDiferenciados.remove(Variable.PROGRAMA_EST);
          mFiltros.remove(Variable.PROGRAMA_EST);
        }
        Variable[] diferenciados=mDiferenciados.toArray(new Variable[0]);
        Filtro[] filtros=mFiltros.values().toArray(new Filtro[0]);
        new VentanaRealizarConsultaGrafica(ag,diferenciados,filtros).ejecutar();
      }
      catch (MyException ex) {
        RutinasGUI.desplegarError(cmDespl,"<html>"+CajaDeHerramientas.stringToHTML(ex.getMessage())+"</html>");
      }
      catch (OutOfMemoryError err) {
        RutinasGUI.desplegarError(cmDespl,"<html>Memoria RAM insuficiente para ejecutar el proceso.</html>");
      }
      catch (Throwable th) {
        th.printStackTrace();
        RutinasGUI.desplegarError(cmDespl,"<html>Hubo un error realizando la consulta.</html>");
      }
    }
  }
  private class VentanaRealizarConsultaGrafica extends MyDialogProgreso {
    private final boolean alMinisterio;
    private final Variable[] diferenciados;
    private final Filtro[] filtros;
    public VentanaRealizarConsultaGrafica(boolean pAlMinisterio, Variable[] pDiferenciados, Filtro[] pFiltros) {
      super(VentanaPrincipal.getInstance(),"Realizando la consulta"+(pAlMinisterio?" en el Ministerio de Educaci�n Nacional":""),false);
      alMinisterio=pAlMinisterio;
      diferenciados=pDiferenciados;
      filtros=pFiltros;
    }
    public void ejecutar() {
      setVisible(true);
      new Thread() {
        public void run() {
          Window window=VentanaRealizarConsultaGrafica.this;
          try {
            generarGrafica();
          }
          catch (MyException ex) {
            RutinasGUI.desplegarError(window,"<html>"+CajaDeHerramientas.stringToHTML(ex.getMessage())+"</html>");
            window.dispose();
          }
          catch (OutOfMemoryError err) {
            RutinasGUI.desplegarError(window,"<html>Memoria RAM insuficiente para ejecutar el proceso.</html>");
            window.dispose();
          }
          catch (Throwable th) {
            th.printStackTrace();
            RutinasGUI.desplegarError(window,"<html>Hubo un error realizando la consulta.</html>");
            window.dispose();
          }
        }
      }.start();
    }
    @SuppressWarnings("unchecked")
    private void generarGrafica() throws Exception {
      Object[] resultado=obtenerResultado(alMinisterio,diferenciados,filtros);
      byte limInf = (Byte) resultado[5], limSup = (Byte) resultado[6]; 
      Map<byte[],int[]> resC=(Map<byte[],int[]>)(resultado[0]);
      Map<byte[],double[]> resP=(Map<byte[],double[]>)(resultado[1]);
      Integer[] codigosIESDif=(Integer[])(resultado[2]);
      String[] codigosProgramasDif=(String[])(resultado[3]);
      int tam=(Integer)(resultado[4]);
      ChartPanel graf=FabricaGraficas.crearGraficaConteosPoblacion("","Per�odo","N�mero de ausentes",diferenciados,new Object[]{resC,codigosIESDif,codigosProgramasDif},false,true, 0xFF&limInf);
      ChartPanel grafPorc=FabricaGraficas.crearGraficaConteosPoblacion("","Per�odo","% ausencia",diferenciados,new Object[]{resP,codigosIESDif,codigosProgramasDif},true,true, 0xFF&limInf);
      int t=diferenciados.length,m=resC.size();
      String encFilas[][]=new String[t][m+1],encColumnas[]=new String[tam];
      for (int i=0; i<t; i++) encFilas[i][0]=diferenciados[i].nombre;
      {
        int ind=1;
        for (byte[] llave:resC.keySet()) {
          for (int i=0; i<t; i++) encFilas[i][ind]=Variable.toString(diferenciados[i],llave[i],codigosIESDif,codigosProgramasDif);
          ind++;
        }
      }
      {
        String[] encsColsB = CajaDeHerramientas.getTextosSemestresEntre(limInf, limSup);
        for (int j=0; j<tam; j++) encColumnas[j]= encsColsB[j];//""+(j+1);
      }
      String[][] valores=new String[m][tam],valoresPorc=new String[m][tam];
      {
        int ind=0;
        for (Map.Entry<byte[],int[]> eC:resC.entrySet()) {
          int[] contC=eC.getValue();
          double[] contP=resP.get(eC.getKey());
          int tam2=tam;
          while (tam2>0 && contC[tam2-1]==0) tam2--;
          for (int i=0; i<tam2; i++) {
            valores[ind][i]=""+contC[i];
            valoresPorc[ind][i]=(contP==null)?"":(RutinasGUI.df_porcentaje.format(contP[i]));
          }
          for (int i=tam2; i<tam; i++) valores[ind][i]=valoresPorc[ind][i]="";
          ind++;
        }
      }
      InfoTabla tabla=new InfoTabla(valores,encFilas,encColumnas),tablaPorc=new InfoTabla(valoresPorc,encFilas,encColumnas);
      VentanaRealizarConsultaGrafica.this.dispose();
      new VentanaGrafica(VentanaPrincipal.getInstance(),"Resultado de la consulta",graf,grafPorc,tabla,tablaPorc,new MyEditorPane(false,getStringSeleccion())).setVisible(true);
    }
  }
  public static Object[] obtenerResultado(boolean alMinisterio, Variable[] diferenciados, Filtro[] filtros) throws MyException {
    Object[] resultado=null;
    if (!alMinisterio) {
      //resultado=KernelSPADIES.getInstance().getConteoPoblacion(filtros,diferenciados);
      resultado=KernelSPADIES.getInstance().getDesercionIntersemestral(filtros,diferenciados);
    }
    else {
      // Variable.PROGRAMA_EST no debe estar ni en 'diferenciados' ni en 'filtros'
      //resultado=PuertaAlServidorDeConsultas.obtenerResultadoConsulta(1,Object[].class,filtros,diferenciados);
      resultado=PuertaAlServidorDeConsultas.obtenerResultadoConsulta(9,Object[].class,filtros,diferenciados);
    }
    if (resultado==null) throw new MyException("La consulta no arroj� datos.");
    return resultado;
  }
}
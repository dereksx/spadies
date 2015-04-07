/*
 * Centro de Estudios sobre Desarrollo Econ�mico (CEDE) - Universidad de los Andes
 * Martes 14 de Octubre 2008
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
package spadies.tools;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import spadies.gui.format.ProcesadorConsultaDesercion;
import spadies.gui.format.ProcesadorDesercionCohorte;
import spadies.gui.format.ProcesadorDesercionPorPeriodo;
import spadies.gui.format.ResultadoConsulta;
import spadies.gui.util.InfoTabla;
import spadies.kernel.Estudiante;
import spadies.kernel.IES;
import spadies.kernel.KernelSPADIES;
import spadies.kernel.Programa;
import spadies.util.Constantes;
import spadies.util.MyException;
import spadies.util.variables.AmbienteVariables;
import spadies.util.variables.Filtro;
import spadies.util.variables.Item;
import spadies.util.variables.Variable;

public class DiagnosticoIES {
  private enum PruebaIES {
    SINCRONIZACION_VACIA("Sincronizaci�n vacia",new String[]{"<!-- La anterior tabla le muestra el n�mero de estudiantes prim�paros reportado en cada cohorte. Las cohortes donde el n�mero de estudiantes es cero, se deben revisar y completar la informaci�n correspondiente dentro del archivo de prim�paros del per�odo respectivo. -->"}) {
      private static final int umbral = 2;
      public boolean diagnosticar(IES ies, String[] sal) {
        boolean res = false;
        int[] conteoCohorte = new int[ies.n];
        for (Estudiante est: ies.estudiantes) conteoCohorte[est.getSemestrePrimiparo()]++;
        for (int conteo:conteoCohorte) if (conteo>=umbral) res = true;
        /*
        String dat[][] = new String[ies.n][1];
        String encF[] = new String[ies.n+1];
        encF[0] = "Cohorte";
        for (int i = 0;i<ies.n;i++) {
          dat[i][0] = String.valueOf(conteoCohorte[i]);
          encF[i+1] = ies.semestres[i];
        }
        InfoTabla it = new InfoTabla(dat, new String[][]{encF}, new String[]{"Individuos"});
        sal[0] = generarTablaHTML(it, "COHORTES SIN PRIM�PAROS");
        */
        sal[0] = res?
            textoBien("No es una sincronizaci�n de prueba porque hay periodos con estudiantes.")
          : textoError("Es una sincronizaci�n de prueba, ninguno de los periodos tiene estudiantes.");
        sal[0]+= "["+ies.semestres[0]+"-"+ies.semestres[ies.semestres.length-1]+"]";
        sal[0] = "<div><b>Sincronizaci�n de prueba: </b>"+sal[0]+"</div>";
        return res;
      }
    },
    PERIODOS_VACIOS("Periodos vacios",new String[]{"Los per�odos que se resaltan en rojo, corresponden a per�odos donde la informaci�n es nula o no se carg� en el sistema. Para corregir esta informaci�n es necesario que ingrese al archivo de prim�paros del per�odo en cuesti�n y arregle la informaci�n all� reportada. Por favor revise la informaci�n correspondiente a este per�odo o en caso de que no exista informaci�n para dicho per�odo, env�e un comunicado de rector�a al sistema SPADIES."}) {
      private static final int umbral = 2;
      public boolean diagnosticar(IES ies, String[] sal) {
        boolean res = true;
        int[] conteoCohorte = new int[ies.n];
        for (Estudiante est: ies.estudiantes) conteoCohorte[est.getSemestrePrimiparo()]++;
        for (int conteo:conteoCohorte) if (conteo<umbral) res = false;
        String dat[][] = new String[ies.n][1];
        String encF[] = new String[ies.n+1];
        encF[0] = "Cohorte";
        for (int i = 0;i<ies.n;i++) {
          dat[i][0] = conteoCohorte[i]<umbral?
              textoError(String.valueOf(conteoCohorte[i])):
              textoBien(String.valueOf(conteoCohorte[i]));
          encF[i+1] = ies.semestres[i];
        }
        InfoTabla it = new InfoTabla(dat, new String[][]{encF}, new String[]{"Individuos"});
        sal[0] = generarTablaHTML(it, "COHORTES SIN PRIM�PAROS");
        return res;
      }
    },
    EXCESO_RETIRODIS("Retiro disciplinario excesivo",new String[]{"De acuerdo con el seguimiento de informaci�n hecho por el SPADIES, los n�meros en rojo indican que la informaci�n reportada por su instituci�n en relaci�n con los retiros disciplinarios es muy alta para esa cohorte. En los casos m�s extremos que se han detectado dentro del sistema, los retiros disciplinarios no corresponden a un n�mero superior al 2% de la poblaci�n matriculada, raz�n por la cual todo dato superior al 2% se considera como una falla en el reporte de la informaci�n. Sabemos que se han presentado confusiones respecto a la definici�n de los retiros disciplinarios (antes llamados retiros forzosos), por lo que le recordamos la definici�n de retiros disciplinarios:",
        "<b>Retiros disciplinarios:</b> son todos aquellos estudiantes que por razones exclusivamente disciplinarias (casos de plagio, falta al reglamento, etc) han sido sancionados disciplinariamente por la instituci�n y por esa raz�n no se encuentran matriculados en la instituci�n dentro del per�odo en cuesti�n.  Todos los retiros disciplinarios deben estar respaldados por un acto administrativo.",
        "Por favor revise la informaci�n reportada dentro de los archivos antes denominados �Retiros Forzosos�, y retire los estudiantes all� reportados que no entren dentro de la clasificaci�n antes descrita."
        }) {
      public static final double umbral = 0.02d;
      public boolean diagnosticar(IES ies, String[] sal) {
        boolean res = true;
        String[] encCol = new String[]{"Graduado","Retirado","Activo","Desertor"};
        int conteo[][] = new int[ies.n][4];
        for (Estudiante est: ies.estudiantes) {
          int ind = -1;
          switch(est.getEstado()) {
          case  1:ind=0;break;//Graduado
          case  2:ind=1;break;//Retirado
          case  0:ind=2;break;//Activo
          case -1:ind=3;break;//Desertor
          }
          conteo[est.getSemestrePrimiparo()][ind]++;
        }
        double porcentaje[][] = new double[ies.n][4];
        for (int i=0;i<ies.n;i++) {
          int tot = 0; for (int val:conteo[i]) tot+=val;
          for (int j=0;j<4;j++) porcentaje[i][j] = (1d*conteo[i][j])/tot;
        }
        for (int i=0;i<ies.n;i++) if (porcentaje[i][1]>umbral) res = false;
        String [][] dat = new String[ies.n][4];
        for (int i=0;i<ies.n;i++) for (int j=0;j<4;j++) {
          dat[i][j] = df.format(porcentaje[i][j]);
          if (j==1 && porcentaje[i][j]>umbral) dat[i][j] = textoError(dat[i][j]);
        }
        String [][] encF = new String[1][ies.n+1];
        encF[0][0] = "Cohorte";
        System.arraycopy(ies.semestres, 0, encF[0], 1, ies.n);
        sal[0] = generarTablaHTML(new InfoTabla(dat, encF, encCol), "REPORTE DE INFORMACI�N DE RETIROS DISCIPLINARIOS");
        return res;
      }
    },
    PERIODOS_SUBREPORTADOS("Periodos subreportados",new String[]{"En la anterior tabla, usted podr� encontrar en la primera columna, el per�odo de informaci�n que se est� analizando; en la segunda columna, una relaci�n del n�mero de matriculados reportados en el SPADIES para cada per�odo; y  en la tercera columna, un c�lculo del cambio porcentual en el n�mero de matriculados por per�odo frente al per�odo inmediatamente anterior. Todo cambio porcentual inferior al 80% y superior al 300% se considera an�malo, por lo que se resalta en rojo para indicar que la informaci�n de matriculados correspondiente a ese per�odo debe ser revisada.",
        "Por favor revise la anterior tabla e identifique los per�odos en los que los cambios en su poblaci�n matriculada est�n por fuera de lo normal y proceda a revisar la informaci�n de sus estudiantes en el archivo de matriculados que corresponde al per�odo resaltado en rojo. Verifique que los nombres est�n bien digitados y que cada estudiante cuente con informaci�n completa (apellidos, nombres, tipo de documento, n�mero de documento, programa, etc)."}) {
      public static final double limSup = 3d;
      public static final double limInf = 0.8d;
      public boolean diagnosticar(IES ies, String[] sal) {
        boolean res = true;
        int [] conteo = new int[ies.n];
        double [] razon = new double[ies.n];
        for (Estudiante e: ies.estudiantes) {
          int jI=e.getSemestrePrimiparo();
          long matri=e.getSemestresMatriculadoAlDerecho()>>>jI;
          for (int j=jI; j<ies.n; j++,matri>>>=1)
            if ((matri&1L)==1L) conteo[j]++;
        }
        for (int i = 1;i<ies.n;i++) razon[i] = (conteo[i]*1d)/(conteo[i-1]);
        String [] encC = {"Matriculados","Cambio"};
        String [][] encF = new String[1][ies.n+1];
        encF[0][0] = "Periodo";
        System.arraycopy(ies.semestres, 0, encF[0], 1, ies.n);
        String [][] dat = new String[ies.n][2];
        for (int i = 0;i<ies.n;i++) {
          dat[i][0] = String.valueOf(conteo[i]);
          if (i!=0 && razon[i]!=Double.NaN) {
            dat[i][1] = df.format(razon[i]);
            if (razon[i]<limInf || razon[i]>limSup) {
              dat[i][1] = textoError(dat[i][1]);
              res = false;
            }
          } else dat[i][1] = "";
        }
        InfoTabla it = new InfoTabla(dat,encF, encC);
        sal[0] = generarTablaHTML(it, "PERIODOS CON MATRICULA AN�MALA");
        return res;
      }
    },
    PROGRAMAS_MISMO_CODIGO("Diferentes programas reportados con el mismo codigo",new String[]{"La anterior tabla le indica los c�digos de programa que se identificaron entre los archivos planos, con m�s de un nombre de programa dentro de su instituci�n. Por favor revise estos c�digos y relaci�nelos con el �nico programa al que pertenecen.  Si la tabla est� vac�a haga caso omiso a este �tem."}) {
      public boolean diagnosticar(IES ies, String[] sal) {
        Map<String,Set<String>> nombresProgramaPorCodigo = new TreeMap<String,Set<String>>(),
          newNombresProgramaPorCodigo = new TreeMap<String,Set<String>>();
        for (Programa p: ies.programas) {
          String cod = new String(p.codigoSNIES);
          if (cod.equals("")) continue; //Ignorar codigos vacios
          Set<String> set = nombresProgramaPorCodigo.get(cod);
          if (set==null) nombresProgramaPorCodigo.put(cod, set =  new TreeSet<String>());
          set.add(new String(p.nombre));
        }
        for (Entry<String,Set<String>> ent: nombresProgramaPorCodigo.entrySet())
          if (ent.getValue().size()>1)
            newNombresProgramaPorCodigo.put(ent.getKey(),ent.getValue());
        String [] encC = new String[]{"Codigo", "Nombres"};
        String [][] dat = new String[newNombresProgramaPorCodigo.size()][encC.length];
        {
          int i = 0;
          for (Entry<String,Set<String>> ent:newNombresProgramaPorCodigo.entrySet())
            dat[i++] = new String[]{ent.getKey(),listaNoOrdenadaHTML(ent.getValue())};
        }
        sal[0] = generarTablaHTML(new InfoTabla(dat, new String[0][0], encC), "PROGRAMAS CUYO C�DIGO SNIES APARECE CON VARIOS NOMBRES");
        return newNombresProgramaPorCodigo.size()==0;
      }
    },
    PROGRAMAS_SIN_CRUCE("Programas con codigos invalidos",new String[]{"El anterior cuadro le indica los programas cuyo nombre y c�digo SNIES reportado no concuerdan seg�n la tabla de programas reportada por el SNIES. En la primera columna encontrar� bajo el t�tulo �C�digo� el n�mero correspondiente al c�digo SNIES que se report� dentro de los datos suministrados por su instituci�n al SPADIES. En la segunda columna encontrar� bajo el t�tulo �Nombre� el nombre del programa que se identific� con el c�digo de la primera columna. Si estas dos columnas no coinciden de acuerdo con la tabla del SNIES, el c�digo y nombre de programa por usted reportado se encontrar� en el cuadro anterior.", 
        "Para corregir esta informaci�n, usted debe ir a los archivos de prim�paros e identificar los errores. En el caso de la tabla que resulta del an�lisis de la informaci�n correspondiente a su instituci�n, encontramos que los programas ac� relacionados carecen de informaci�n sobre su c�digo de programa, por lo que solicitamos que revise esta informaci�n en el archivo de prim�paros y complete los datos correspondientes al c�digo de programa para cada uno de los programas aqu� enumerados.  Si la tabla est� vac�a haga caso omiso a este �tem."}) {
      public boolean diagnosticar(IES ies, String[] sal) {
        Collection<Programa> programasSinCruce = new LinkedList<Programa>();
        for (Programa p: ies.programas)
          if (p.nivel==-1) programasSinCruce.add(p);
        String [] encC = new String[]{"Codigo", "Nombre"};
        String [][] dat = new String[programasSinCruce.size()][encC.length];
        {
          int i = 0;
          for (Programa p:programasSinCruce)
            dat[i++] = new String[]{new String(p.codigoSNIES),new String(p.nombre)};
        }
        sal[0] = generarTablaHTML(new InfoTabla(dat, new String[0][0], encC), "PROGRAMAS CUYO C�DIGO SNIES NO CRUZA CON LA INFORMACI�N REPORTADA POR EL SNIES");
        return programasSinCruce.isEmpty();
      }
    },
    INDIVIDUOS_SIN_PROGRAMA("Individuos sin programa",new String[]{"La anterior tabla le muestra una relaci�n del n�mero de individuos reportados en el archivo de prim�paros de cada cohorte, que no cuentan con informaci�n sobre el programa acad�mico al que ingresaron. Por favor revise esta informaci�n y compl�tela ya que es de suma importancia para poder llevar a cabo un buen an�lisis del comportamiento de los programas en su instituci�n. Si la tabla est� vac�a haga caso omiso a este �tem."}) {
      public boolean diagnosticar(IES ies, String[] sal) {
        boolean res = true;
        int[] conteoSinProgramaCohorte = new int[ies.n];
        for (Estudiante est: ies.estudiantes) {
          if (est.getIndicePrograma() == -1) {
            res = false;
            conteoSinProgramaCohorte[est.getSemestrePrimiparo()]++;
          }
        }
        String dat[][] = new String[ies.n][1];
        String encF[] = new String[ies.n+1];
        encF[0] = "Cohorte";
        for (int i = 0;i<ies.n;i++) {
          dat[i][0] = String.valueOf(conteoSinProgramaCohorte[i]);
          encF[i+1] = ies.semestres[i];
        }
        InfoTabla it = new InfoTabla(dat, new String[][]{encF}, new String[]{"Individuos"});
        sal[0] = generarTablaHTML(it, "INDIVIDUOS SIN PROGRAMA SEG�N COHORTE");
        return res;
      }
    },
    INFORMACION_ACTUALIZADA("Informaci�n actualizada",new String[]{"Nota: Si el �ltimo per�odo reportado por su instituci�n es inferior a 2008-1, es necesario que generen toda la informaci�n hasta completar dicho per�odo, lo antes posible."}) {
      final int ultimoPeriodoRequerido = 20101;  
      public boolean diagnosticar(IES ies, String[] sal) {
        int ultimoPer = Integer.parseInt(ies.semestres[ies.n-1]);
        boolean res = ultimoPer>=ultimoPeriodoRequerido;
        String sPer = String.valueOf(ultimoPer);
        sal[0] = "<b>Informacion actualizada</b> El ultimo periodo reportado por la IES es " + (res?textoBien(sPer):textoError(sPer));
        return res;
      }
    },
    CRUCE_ICFES("Cruce con el ICFES",new String[]{"Nota: Si el porcentaje de estudiantes que cruzan es inferior al 80%, por favor revise la calidad de la informaci�n contenida en sus archivos de prim�paros, en especial la ortograf�a de apellidos y nombres, tipo de documento, n�mero de documento y  fecha de nacimiento."}) {
      final double umbral = 0.70d;  
      public boolean diagnosticar(IES ies, String[] sal) {
        int tot = 0, val = 0;
        for (Estudiante est:ies.estudiantes) {
          if (est.getPuntajeICFES()!=-1) val++;
          tot++;
        }
        double por = (1d*val)/tot;
        String sPor = df.format(por);
        boolean res = por>=umbral;
        sal[0] = "<div><b>Cruce con el ICFES</b> El porcentaje de estudiantes que cruzaron fue " + (res?textoBien(sPor):textoError(sPor))+"</div>";
        return res;
      }
    },
    MATRICULA_PROGRAMAS("Matricula programas",new String[]{"En el anterior cuadro se hace un an�lisis de la informaci�n del n�mero de matriculados reportado para cada programa en cada per�odo, seg�n la �ltima sincronizaci�n de su instituci�n. Los programas donde no se encuentra continuidad en el n�mero de matriculados entre periodos consecutivos, se pueden observar en la tabla, seg�n programa. Por favor revise la informaci�n en sus archivos de matriculados y verifique la informaci�n correspondiente a los programas con problemas de informaci�n, en los per�odos referidos en la tabla."}) {
      public boolean diagnosticar(IES ies, String[] sal) {
        boolean res = true;
        int [][] conteo = new int[ies.programas.length+1][ies.n];
        for (Estudiante e: ies.estudiantes) {
          int jI=e.getSemestrePrimiparo();
          long matri=e.getSemestresMatriculadoAlDerecho()>>>jI;
          for (int j=jI; j<ies.n; j++,matri>>>=1)
            if ((matri&1L)==1L) conteo[e.getIndicePrograma()+1][j]++;
        }
        //for (int i = 1;i<ies.n;i++) razon[i] = (conteo[i]*1d)/(conteo[i-1]);
        String [] encC = new String[ies.n];
        System.arraycopy(ies.semestres, 0, encC, 0, ies.n);
        String [][] encF = new String[1][conteo.length+1];
        encF[0][0] = "Periodo";
        encF[0][1] = "DESCONOCIDO";
        for (int i=0;i<ies.programas.length;i++) encF[0][i+2] = new String(ies.programas[i].nombre);
        String [][] dat = new String[conteo.length][ies.n];
        for (int ip = 0;ip<conteo.length;ip++) {
          for (int i = 0;i<ies.n;i++)
            dat[ip][i] = conteo[ip][i]==0?"":String.valueOf(conteo[ip][i]);
        }
        InfoTabla it = new InfoTabla(dat,encF, encC);
        sal[0] = generarTablaHTML(it, "Matricula por programa/periodo");
        return res;
      }
    }, 
    ;
    private static boolean conDescripcion = true;
    public final String nombre;
    private String [] descripcion = new String[]{};
    /*private PruebaIES(String nombre) {
      this.nombre = nombre;
    }*/
    private PruebaIES(String nombre, String[] descripcion) {
      this.nombre = nombre;
      this.descripcion = descripcion;
    }
    private String htmlDescripcion() {
      if (!conDescripcion || descripcion.length==0) return "";
      StringBuilder sb = new StringBuilder("<div>");
      for (String s: descripcion) {
        sb.append("<div style=\"text-align: justify;\">");
        sb.append(s);
        sb.append("</div>");
      }
      sb.append("</div>");
      return sb.toString();
    }
    public abstract boolean diagnosticar(IES ies, String[] sal);
    public boolean diagnosticarDetallado(IES ies, String[] sal) {
      boolean res = diagnosticar(ies, sal);
      sal[0] += htmlDescripcion(); 
      return res;
    }
  }
  private static KernelSPADIES kernel = null;
  private static PrintStream ps = null;
  private static File carImg;
  private static void pl(String s) {ps.println(s);};
  private static void p(String s) {ps.print(s);};
  private static final transient DecimalFormat df=new DecimalFormat("0.00%");
  public static void main(String[] args) throws MyException, IOException {
    Constantes.cargarArchivoFiltroIES();
    kernel = KernelSPADIES.getInstance();
    kernel.cargarParaServidor(Constantes.carpetaDatos, Long.MAX_VALUE,false);
    File carSal = new File("reporteIES");
    carImg = new File(carSal,"img");
    if (!carSal.exists()) carSal.mkdir();
    if (!carImg.exists()) carImg.mkdir();
    File fIndice = new File(carSal,"indice.html");
    PrintStream psI = new PrintStream(fIndice);
    psI.print(inicioPagina("Indice diagnostico IES"));
    int numPruebas = PruebaIES.values().length;
    int numCamposIES = 2;

    String[] encC = new String[numCamposIES+numPruebas];
    encC[0] = "CodigoIES";
    encC[1] = "NombreIES";
    for (int i=0;i<numPruebas;i++) encC[i+numCamposIES] = PruebaIES.values()[i].nombre;

    String[][] dat = new String[kernel.listaIES.length][numCamposIES+numPruebas];
    {
      int i = 0;
      for (IES ies: kernel.listaIES) {
        String[] da = dat[i++];
        da[0] = "<a href = \""+ies.codigo+".html\">"+ies.codigo+"</a>";
        da[1] = new String(ies.nombre);
        PrintStream ps = new PrintStream(new File(carSal,ies.codigo+".html"));
        ps.print(inicioPagina("Diagnostico "+ies.codigo));
        ps.print(descripcionIES(ies));
        ps.print(preliminares(ies));
        for (int j=0;j<numPruebas;j++) {
          String sa[] = new String[1];
          da[j+numCamposIES] = PruebaIES.values()[j].diagnosticarDetallado(ies, sa)?textoBien("OK"):textoError("X");
          ps.print(sa[0]);
        }
        ps.print(finalPagina());
        ps.close();
      }
    }
    InfoTabla it = new InfoTabla(dat, new String[0][0], encC);
    psI.print(generarTablaHTML(it, "Indice IES"));
    psI.print(finalPagina());
    psI.close();
  }

  private static String descripcionIES(IES ies) {
    StringBuilder sb = new StringBuilder("<div>");
    sb.append("<div>");
    sb.append("<div>");
    sb.append(ies.codigo);
    sb.append('-');
    sb.append(new String(ies.nombre));
    sb.append("</div>");
    sb.append("<div>");
    sb.append("Se han encontrado las siguientes caracter�sticas en la informaci�n reportada por la IES.");
    sb.append("</div>");
    sb.append("</div>");
    return sb.toString();
  }
  private static String preliminares(IES ies) throws MyException {
    Filtro fil = new Filtro(Variable.CODIGO_IES, new Item[]{new Item(ies.codigo,"","")});
    StringBuilder sb = new StringBuilder("<div>");
    {
      Object[][] resp = kernel.getVariablesRelevantes(new Filtro[]{fil});
      String[][] valores = new String[resp.length][3];
      for (int i=0;i<resp.length;i++)
        for (int j=0;j<3;j++)
          valores[i][j] = j!=1?resp[i][j].toString():((Variable)resp[i][0]).rango.toString(((byte[])resp[i][1])[0]);
      InfoTabla it = new InfoTabla(valores,null,new String[]{"Variable","Mejor","Puntaje"});
      sb.append(generarTablaHTML(it,"Variables segun importancia"));
    }
    {
      Object[] res = kernel.getTablaCantidadArchivos(new Filtro[]{fil});
      InfoTabla it = new InfoTabla((String[][])(res[0]),(String[][])(res[1]),(String[])(res[2]));
      sb.append(generarTablaHTML(it,"Inventario"));
    }
    {
      ProcesadorConsultaDesercion.instance.setParametros(false, new Variable[0], new Filtro[]{fil});
      sb.append("<h1>Deserci�n</h1>");
      try {
        Collection<ResultadoConsulta> graf = ProcesadorConsultaDesercion.instance.generarGrafica();
        int i=0;
        for (ResultadoConsulta res:graf) {
          if (res.resultado instanceof ChartPanel) {
            String nomAr = ies.codigo+"_1_"+i+".jpg";
            guardarImagen(((ChartPanel)res.resultado),new File(carImg,nomAr));
            sb.append("<img src=\"img/"+nomAr+"\">");
            i++;
          }
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    {
      ProcesadorDesercionPorPeriodo.instance.setParametros(false, new Variable[0], new Filtro[]{fil});
      try {
        Collection<ResultadoConsulta> graf = ProcesadorDesercionPorPeriodo.instance.generarGrafica();
        int i=0;
        for (ResultadoConsulta res:graf) {
          if (res.resultado instanceof ChartPanel) {
            String nomAr = ies.codigo+"_2_"+i+".jpg";
            guardarImagen(((ChartPanel)res.resultado),new File(carImg,nomAr));
            sb.append("<img src=\"img/"+nomAr+"\">");
            i++;
          }
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    sb.append("</div>");

    return sb.toString();
  }
  public static String inicioPagina(String titulo) {
    StringBuilder s = new StringBuilder();
    s.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\r\n");
    s.append("<html>");
    s.append("<head>");
    s.append("<style>");
    s.append("<!--");
    
    s.append("table.tablaDiagnostico th {text-align: center;");
    s.append("border: 1px solid black;");
    s.append("font-size: 0.8em;");
    s.append("background-color: #aaa;}");
    
    s.append("table.tablaDiagnostico tr td {text-align: right;");
    s.append("border: 1px solid black;");
    s.append("font-size: 0.8em;");
    s.append("}");
    
    s.append("div.titulo0 {font-size: 1.3em;;}");

    s.append(".txtError {color: #F00;}");
    s.append(".txtOK    {color: #00F;}");

    s.append("-->");
    s.append("</style>");
    s.append("<title>" + titulo + "</title></head>");
    s.append("<body>");
    return s.toString();
  }
  public static String finalPagina() {
    return "</body></html>";
  }
  public static void salida() throws MyException, IOException {
    kernel = KernelSPADIES.getInstance();
    File carSal = new File("reporteHTML");
    if (!carSal.exists()) carSal.mkdir();
    for (IES ies: kernel.listaIES) {
      escribirReporteIES(ies, new File(carSal, ies.codigo + ".html"));
    }
  }

  private static void escribirReporteIES(IES ies, File fSal) throws FileNotFoundException, MyException {
    Filtro filtro = new Filtro(Variable.CODIGO_IES, new Item[]{new Item(ies.codigo,"","")});
    AmbienteVariables.getInstance().notificarCambioSeleccion(new Filtro[]{filtro});
    ps = new PrintStream(fSal);
    pl("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
    pl("<html>");
    pl("<head>");
    pl("<style>");
    pl("<!--");
    
    pl("th {text-align: center;");
    pl("border: 1px solid black;");
    pl("background-color: #aaa;}");
    
    pl("td {text-align: right;");
    pl("border: 1px solid black;");
    pl("}");

    pl("-->");
    pl("</style>");
    p("<title>Reporte IES" + ies.codigo + "</title></head>");
    tablaIdentificacionIES(ies);
    tablaInventarioDatos(ies);
    tablaInventarioProgramas(ies);
    tablaPeriodosProgramas(ies);
    //tablaProgramasICFES(ies);
    //tablaProgramasPrimiparos(ies);
    tablaProgramasMatriculados(ies);
    tablaAux(ies);
    pl("</html>");
    ps.close();
  }
  private static void tablaInventarioDatos(IES ies) {
    Filtro filtro = new Filtro(Variable.CODIGO_IES, new Item[]{new Item(ies.codigo,"","")});
    Object[] res = kernel.getTablaCantidadArchivos(new Filtro[]{filtro});
    InfoTabla it = new InfoTabla((String[][])(res[0]),(String[][])(res[1]),(String[])(res[2]));
    imprimirTabla(it);
  }

  private static void tablaIdentificacionIES(IES ies) {
    Filtro filtro = new Filtro(Variable.CODIGO_IES, new Item[]{new Item(ies.codigo,"","")});
    int numEst = kernel.getCantidadEstudiantes(new Filtro[]{filtro});
    p("<table class=\"tablaDiagnostico\"><caption>Datos de la IES</caption>");
    p("<tr><th>Codigo</th><td>"+ies.codigo+"</td></tr>");
    p("<tr><th>Nombre</th><td>"+new String(ies.nombre)+"</td></tr>");
    p("<tr><th>Periodos</th><td>"+ies.semestres[0]+"-"+ies.semestres[ies.semestres.length-1]+"</td></tr>");
    p("<tr><th>Sujetos</th><td>"+numEst+"</td></tr>");
    p("</table>");
  }

  private static void tablaInventarioProgramas(IES ies) throws MyException {
    String[][] dat = new String[ies.programas.length][1];
    //String[][] encF = new String[ies.programas.length+1][1];
    int i = 0;
    for(Programa p:ies.programas) {
      //encF[i+1][0] = String.valueOf(i + 1);
      dat[i] = new String[]{new String(p.codigoSNIES),
          new String(p.nombre),String.valueOf(p.area),
          String.valueOf(p.nucleo),String.valueOf(p.nivel),
          String.valueOf(p.metodologia),
      };
      i++;
    }
    InfoTabla res = new InfoTabla(dat,new String[0][0]/*encF*/,new String[]{"Codigo","Nombre","Area","Nucleo","Nivel","Metodologia"});
    imprimirTabla(res,"Programas");
  }
  private static void tablaPeriodosProgramas(IES ies) throws MyException {
    String[][] dat = new String[ies.programas.length+1][ies.n];
    String[][] encF = new String[1][ies.programas.length+2];
    for(int i = 0,t = ies.programas.length;i<t;i++) encF[0][i+2] = new String(ies.programas[i].nombre);

    SortedMap<Integer,Integer> resp = new TreeMap<Integer, Integer>();
    for (Estudiante e:ies.estudiantes) {
      int val = (e.getIndicePrograma()==-1?99999:e.getIndicePrograma())*100+e.getSemestrePrimiparo();
      Integer valp = resp.get(val);
      resp.put(val,(valp==null?0:valp)+1);
    }
    for(int i = -1,t = ies.programas.length;i<t;i++) {
      for(int j = 0;j<ies.n;j++) {
        Integer val = resp.get(100*j+(i==-1?99999:i));
        dat[i+1][j] = val==null?"":val.toString();
      }
    }
    InfoTabla res = new InfoTabla(dat,encF,ies.semestres);
    imprimirTabla(res,"Cohorte por programa");
  }
  private static void tablaProgramasMatriculados(IES ies) throws MyException {
    Filtro filtro = new Filtro(Variable.CODIGO_IES, new Item[]{new Item(ies.codigo,"","")});
    InfoTabla[] res = cruceVariables(new Filtro[]{filtro}, Variable.PROGRAMA_EST, Variable.PERIODO_MATRICULADO_PER);
    imprimirTabla(res[0],"Matriculas por programa");
  }
  private static void tablaProgramasPrimiparos(IES ies) throws MyException {
    Filtro filtro1 = new Filtro(Variable.CODIGO_IES, new Item[]{new Item(ies.codigo,"","")}),
      filtro2 = new Filtro(Variable.NUMERO_SEMESTRE_PER, new Item[]{new Item((byte)1,"","")});
    InfoTabla[] res = cruceVariables(new Filtro[]{filtro1,filtro2}, Variable.PROGRAMA_EST, Variable.PERIODO_INGRESO_EST);
    imprimirTabla(res[0]);
  }
  private static void tablaProgramasICFES(IES ies) throws MyException {
    Filtro filtro1 = new Filtro(Variable.CODIGO_IES, new Item[]{new Item(ies.codigo,"","")}),
      filtro2 = new Filtro(Variable.NUMERO_SEMESTRE_PER, new Item[]{new Item((byte)1,"","")});
    InfoTabla[] res = cruceVariables(new Filtro[]{filtro1,filtro2}, Variable.PROGRAMA_EST, Variable.CLASIFICACION_PUNTAJE_ICFES_EST);
    imprimirTabla(res[1]);
    imprimirTabla(res[0]);
  }

  private static void tablaAux(IES ies) throws MyException {
    Filtro filtro1 = new Filtro(Variable.CODIGO_IES, new Item[]{new Item(ies.codigo,"","")}),
      filtro2 = new Filtro(Variable.NUMERO_SEMESTRE_PER, new Item[]{new Item((byte)1,"","")}),
      filtro3 = new Filtro(Variable.CLASIFICACION_ESTADO_EST, new Item[]{new Item((byte)-1,"",""),new Item((byte)2,"","")});
    InfoTabla[] res = cruceVariables(new Filtro[]{filtro1,filtro2,filtro3}, Variable.PERIODO_INGRESO_EST, Variable.ULTIMO_PERIODO_MATRICULADO_EST);
    imprimirTabla(res[0]);
  }
  
  private static InfoTabla[] cruceVariables(Filtro[] filtros, Variable difX, Variable difY) throws MyException {
    Variable[] diferenciados = new Variable[]{difX,difY};
    Object[] resultado = KernelSPADIES.getInstance().getCruceVariables(filtros,diferenciados);
    Map<byte[],int[]> resC=(Map<byte[],int[]>)(resultado[0]);
    Integer[] codigosIESDif=(Integer[])(resultado[1]);
    String[] codigosProgramasDif=(String[])(resultado[2]);
    Map<Byte,Integer> relX = new TreeMap<Byte,Integer>(),
    relY = new TreeMap<Byte,Integer>();
    {
      SortedSet<Byte> valsX = new TreeSet<Byte>(),
        valsY = new TreeSet<Byte>();
      for (byte[] llave:resC.keySet()) {
        valsX.add(llave[0]);
        valsY.add(llave[1]);
      }
      int ix = 0;
      for (byte codVar:valsX) {
        relX.put(codVar, ix++);
      }
      ix = 0;
      for (byte codVar:valsY) {
        relY.put(codVar, ix++);
      }
    }
    String encFilas[][]=new String[2][relX.size()+1],encColumnas[]=new String[relY.size()];
    Iterator<Byte> it = relX.keySet().iterator();
    encFilas[0][0]=encFilas[1][0]="";
    for (int i=0,ta=relX.size(); i<ta; i++) {
      Byte val = it.next();
      encFilas[0][i+1]=diferenciados[0].nombre;
      encFilas[1][i+1]=val==-1?Constantes.S_DESCONOCIDO:Variable.toString(diferenciados[0],val,codigosIESDif,codigosProgramasDif);
    }
    it = relY.keySet().iterator();
    for (int i=0,ta=relY.size(); i<ta; i++) encColumnas[i]=Variable.toString(diferenciados[1],it.next(),codigosIESDif,codigosProgramasDif);
    double totX[] = new double[relX.size()];
    double totY[] = new double[relY.size()];
    double tot = 0; 
    {
      for (Map.Entry<byte[],int[]> eC:resC.entrySet()) {
        int cont = eC.getValue()[0];
        byte[] llave = eC.getKey();
        int fil =relX.get(llave[0]), col = relY.get(llave[1]); 
        tot+=cont;
        totX[fil]+= cont;
        totY[col]+= cont;
      }
    }
    String[][] valores=new String[relX.size()][relY.size()],
      valoresPX=new String[relX.size()][relY.size()],
      valoresPY=new String[relX.size()][relY.size()],
      valoresPT=new String[relX.size()][relY.size()];
    {
      for (Map.Entry<byte[],int[]> eC:resC.entrySet()) {
        int cont = eC.getValue()[0];
        byte[] llave = eC.getKey();
        int fil =relX.get(llave[0]), col = relY.get(llave[1]); 
        valores[fil][col]=String.valueOf(cont);
        valoresPX[fil][col]=df.format(cont/totX[fil]);
        valoresPY[fil][col]=df.format(cont/totY[col]);
        valoresPT[fil][col]=df.format(cont/tot);
      }
    }
    InfoTabla tabla=new InfoTabla(valores,encFilas,encColumnas),
      tablaPX=new InfoTabla(valoresPX,encFilas,encColumnas),
      tablaPY=new InfoTabla(valoresPY,encFilas,encColumnas),
      tablaPT=new InfoTabla(valoresPT,encFilas,encColumnas);
    return new InfoTabla[]{tabla, tablaPX, tablaPY, tablaPT};
  }
  private static void imprimirTabla(InfoTabla it) {
    imprimirTabla(it,"");
  }
  private static void imprimirTabla(InfoTabla it, String titulo) {
    p(generarTablaHTML(it, titulo));
  }
  private static String generarTablaHTML(InfoTabla it, String titulo) {
    StringBuilder sb = new StringBuilder();
    sb.append("<table class=\"tablaDiagnostico\">");
    sb.append("<caption>"+titulo+"</caption>");
    int nc = it.getNumColumnas(),
      nef = it.getNumEncabezadosFilas(),
      nf = it.getNumFilas();
    sb.append("<thead><tr>");
    for (int i = 0; i < nef;i++) sb.append("<th>"+it.getEncabezadoFila(0, i)+"</th>"); 
    for (int i = 0; i < nc;i++) sb.append("<th>" + it.getEncabezadoColumna(i) + "</th>");
    sb.append("</tr></thead><tbody>");
    for (int i = 0; i < nf;i++) {
      sb.append("<tr>");
      for (int j = 0; j < nef;j++) sb.append("<th>" + it.getEncabezadoFila(i+1, j) + "</th>");
      for (int j = 0; j < nc;j++) sb.append("<td>" + it.getValor(i, j) + "</td>");
      sb.append("</tr>");
    }
    sb.append("</tbody></table>");
    return sb.toString();
  }

  private static String textoError(String txt) {
    return textoTipo(txt,"txtError");
  }
  private static String textoBien(String txt) {
    return textoTipo(txt,"txtOK");
  }
  private static String textoTipo(String txt, String tipo) {
    return "<span class=\""+tipo+"\">"+txt+"</span>";
  }
  private static String listaNoOrdenadaHTML(Collection<String> col) {
    StringBuilder s = new StringBuilder();
    s.append("<ul>");
    for (String str:col) {
      s.append("<li>");
      s.append(str);
      s.append("</li>");
    }
    s.append("</ul>");
    return s.toString();
  }
  private static void guardarImagen(ChartPanel cp, File f) {
    JFreeChart chart = cp.getChart();
    BufferedImage img =
      new BufferedImage(700, 400,
      BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = img.createGraphics();
    chart.draw(g2, new Rectangle2D.Double(0, 0, 700, 400));
    g2.dispose();
    try {
      ImageIO.write(img, "JPG", f);
    } catch (IOException e) {
      System.err.println("Error almacenando imagen");
      e.printStackTrace();
    }
  }
}
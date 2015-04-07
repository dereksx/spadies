/*
 * Centro de Estudios sobre Desarrollo Econ�mico (CEDE) - Universidad de los Andes
 * Junio 30 de 2009
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
package spadies.auxiliar;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import spadies.io.MyDataInputStream;
import spadies.io.MyDataOutputStream;
import spadies.kernel.*;
import spadies.server.kernel.*;
import spadies.server.kernel.PreparadorDatos.ArchivoCSV;
import spadies.util.*;
import spadies.util.variables.*;
import static spadies.util.CajaDeHerramientas.*;
/**
 * Cruza la tabla del ICFES con la del SISBEN
 * @author an-cordo
 *
 */
public class PrincipalMatch166_Saber_SISBEN_FULL {
  private static long tInic;
  final static File carBases = new File("./datosNoSPA");
  final static File carSisben = new File("datosNoSPA"),
    fSisbenTo = new File(carSisben, "sisben.csv"),
    fSisbenToPro = new File(carSisben, "sisben.procesado"),
    fSisbenToInd = new File(carSisben, "sisben.ind"),
    fSaber = new File("./SABER/sabernombres.csv"),
    fSaberPro = new File(carSisben, "saber.procesado"),
    fSaberInd = new File(carSisben, "saber.ind");
  final static File f166_2005 = new File("./C166/todos/muestra2005.csv"),
    f166_2006 = new File("./C166/todos/muestra2006.csv"),
    f166_2007 = new File("./C166/todos/muestra2007.csv"),
    f166_2008 = new File("./C166/todos/muestra2008.csv");
  final static File f166_2005ind = new File("./C166/todos/proc/muestra2005.ind"),
    f166_2006ind = new File("./C166/todos/proc/muestra2006.ind"),
    f166_2007ind = new File("./C166/todos/proc/muestra2007.ind"),
    f166_2008ind = new File("./C166/todos/proc/muestra2008.ind");
  final static File f166_2005pro = new File("./C166/todos/proc/muestra2005.procesado"),
    f166_2006pro = new File("./C166/todos/proc/muestra2006.procesado"),
    f166_2007pro = new File("./C166/todos/proc/muestra2007.procesado"),
    f166_2008pro = new File("./C166/todos/proc/muestra2008.procesado");
  final static File car166 = new File("./C166"),
    car166_2005 = new File("./C166/2005"),
    car166_2006 = new File("./C166/2006"),
    car166_2007 = new File("./C166/2007"),
    car166_2008 = new File("./C166/2008");
  final static File fSalida = new File("c166_saber_FULL");
  static enum BASE{C166_2005,C166_2006,C166_2007,C166_2008,SABER};

  final static File carpetaBase(BASE base) {
    switch (base) {
    case C166_2005:return car166_2005;
    case C166_2006:return car166_2006;
    case C166_2007:return car166_2007;
    case C166_2008:return car166_2008;
    //case SABER:    return carSaber;
    }
    return null;
  }
  final static File archivoBase(BASE base, String cod, String ext) {
    String nom = cod+"."+ext;
    return new File(carpetaBase(base),nom);
  }
  final static File archivoBase(BASE base, String cod) {
    return archivoBase(base, cod,"csv");
  }
  static abstract class ArchivoCSV_Lineas_General implements ArchivoCSV {
    private File ind,proc,orig;
    private String titulo;
    public ArchivoCSV_Lineas_General(String titulo, File orig, File proc, File ind) {
      this.titulo = titulo;
      this.orig = orig;
      this.proc = proc;
      this.ind  = ind;
    }
    int conteo = 0;
    public String getTitulo() {return titulo;}
    public File getIn() {return orig;}
    public File getOut() {return proc;}
    public File getInd() {return ind;}
    //public String[] getIdLinea(String[] w) {return new String[]{w[244],w[10]};}
    public byte[] getDatoLinea(String[] w) {
      MyByteSequence mbs = new MyByteSequence(4);
      mbs.setInt(0, conteo++);
      return mbs.getBytes();
    }
  }
  static abstract class ArchivoCSV_Lineas implements ArchivoCSV {
    private BASE base;
    private String cod;
    private File cbase;
    public ArchivoCSV_Lineas(BASE base, String cod) {
      this.base = base;
      this.cod = base!=BASE.SABER?"TODOS":cod;
      this.cbase = carpetaBase(base);
    }
    int conteo = 0;
    public String getTitulo() {return base + " "+cod;}
    public File getIn() {return new File(cbase,cod+".csv");}
    public File getOut() {return new File(new File(cbase,"proc"),cod+".procesado");}
    public File getInd() {return new File(new File(cbase,"proc"),cod+".ind");}
    //public String[] getIdLinea(String[] w) {return new String[]{w[244],w[10]};}
    public byte[] getDatoLinea(String[] w) {
      MyByteSequence mbs = new MyByteSequence(4);
      mbs.setInt(0, conteo++);
      return mbs.getBytes();
    }
  }
  static class Cronometro {
    public long val0 = 0, val1=0;
    public Cronometro(){val0=val1=System.currentTimeMillis();};
    public long tic() {
      val0 = val1;
      val1=System.currentTimeMillis();
      return val1-val0;
    }
    public double tics() {
      return tic()/1000d;
    }
  }
  public static String enc6A = "anno_car;cod_sed;dpto_car;muni_car;cod_mun;anno_inf;dane_ie;dane_sed;cons_sed;ti_codi_;nro_docu;exp_dept;exp_mun;apellido;apelli2;nombre1;nombre2;direccio;tel;res_dpto;res_mun;estrato;fecha_na;grado;nac_dpto;nac_mun;genero;pob_vict;dpto_exp;mun_exp;sec_priv;ot_mun;td_codi_;tt_codi_;etnia;res;ins_fami;jornada;caracter;especial;grupo;metodol;subsidia;repitent;nuevo;sit_acad;con_alum;bandera;t_mat;desplaz",
    encSaber="grado;institucion1;departamento;municipio;dane_ie;dane_sed_jornada;nominst;nohoja;_1r_apellido;_2o_apellido;_1r_nombre;_2o_nombre;forma;respuestas;cons_sed",
    encSisben="depmuni;ficha;region;depto;munic;zona;tamano;sector;seccion;manzana;comuna;barrio;direcc;teles;vivienda;riesgo;pared;piso;energia;alcanta;gas;telefono;basura;acueduc;estrato;agua;thogar;recolec;aplica;entidad;encuesta;supervi;digita;fecha;hogar;teneviv;tcuartos;tdormir;sanitar;usanitar;usosani;tsanitar;ducha;cocinan;alumbra;usotele;nevera;lavadora;tvcolor;tvcable;calenta;horno;aire;tpersona;orden;ape1;ape2;nom1;nom2;parentes;estcivil;conyuviv;hijosde;tipodoc;documen;fechanto;sexo;embaraza;discapa;carnet;asiste;tipoesta;grado;nivel;activi;buscando;ingresos;edad;puntavi;fecham;nucleo;nparente;nivsisvi;fape1;fape2;fnom1;fnom2;puntaje;nivsis;id1;id2;id3;cantidad_miembros_hogar;grado_jefe;nivel_jefe;ingresos_jefe;grado_conyugue_jefe;nivel_conyugue_jefe;ingresos_conyugue_jefe;";
  public static String vacSaber = ";;;;;;;;;;;;;;",
    vacSisben=";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;";
  static Cronometro c = new Cronometro();
  public static void main(String[] args) throws Exception {
    final int chuecos[] = new int[]{0};
    if(!f166_2005ind.exists()) indexarCSV(f166_2005, f166_2005ind);
    tInic = System.currentTimeMillis();
    FileOutputStream fos = new FileOutputStream(new File("bla"));
    PrintStream ps = new PrintStream(fSalida);
    ps.println("x;"+enc6A+";"+encSaber+";"+encSisben);
    //final File fSABER = new File(carBases,"saber_ambos_nombres.csv");
    c.tic();
    if (!fSaberInd.exists()) {
      System.out.println("Indexando SABER");c.tic();
      indexarCSV(fSaber, fSaberInd);
      System.out.println(c.tics());
    }
    System.out.println("Cargando indice SISBEN");c.tic();
    long[] indSis = cargarIndiceCSV(fSisbenToInd);
    RandomAccessFile rafSis = new RandomAccessFile(fSisbenTo, "r");
    System.out.println(c.tics());
    System.out.println("Cargando indice SABER");c.tic();
    int[] indSaber = cargarIndiceCSV_int(fSaberInd);
    RandomAccessFile rafSaber = new RandomAccessFile(fSaber,"r");
    System.out.println(c.tics());
    {
      ArchivoCSV_Lineas_General ar2006 = new ArchivoCSV_Lineas_General("C166_6A 2006",f166_2006,f166_2006pro,f166_2006ind) {
        public String[] getIdLinea(String[] w) {
          if (w.length<=17) chuecos[0]++;
          return new String[]{w.length>17?w[13]+" "+w[14]+" "+w[15]+" "+w[16]:"",w.length>17?w[10]:""};
        }
      };
      ArchivoCSV_Lineas_General ar2007 = new ArchivoCSV_Lineas_General("C166_6A 2007",f166_2007,f166_2007pro,f166_2007ind) {
        public String[] getIdLinea(String[] w) {
          return new String[]{w.length>17?w[13]+" "+w[14]+" "+w[15]+" "+w[16]:"",w.length>17?w[10]:""};
        }
      };
      ArchivoCSV_Lineas_General ar2008 = new ArchivoCSV_Lineas_General("C166_6A 2008",f166_2008,f166_2008pro,f166_2008ind) {
        public String[] getIdLinea(String[] w) {
          return new String[]{w.length>17?w[13]+" "+w[14]+" "+w[15]+" "+w[16]:"",w.length>17?w[10]:""};
        }
      };
      ArchivoCSV_Lineas_General arSABER = new ArchivoCSV_Lineas_General("SABER 2007",fSaber,fSaberPro,fSaberInd) {
        public String[] getIdLinea(String[] w) {
          return w.length<12?new String[]{"",""}:new String[]{w[8]+" "+w[9]+" "+w[10]+" "+w[11],""};
        }
      };
      PreparadorDatos pd = PreparadorDatos.getInstance();
      try {
        for (ArchivoCSV_Lineas_General ar:new ArchivoCSV_Lineas_General[]{ar2006,ar2007,ar2008,arSABER}) if (ar.getIn().exists()){
          pd.prepararArchivoBase(ar);
          if (!ar.getInd().exists()) indexarCSV(ar.getIn(), ar.getInd());
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error preparando archivo");
        System.exit(1);
      }
      System.out.println(chuecos[0]+"XXX");
      File fRef = f166_2005;
      int regs = registrosEnArchivo(fRef);
      System.out.println("Cargando registros");c.tic();
      byte[][][] INFO_IES = new byte[3][regs][];
      {
        BufferedReader br = new BufferedReader(new FileReader(fRef));
        br.readLine();//Encabezado
        int i = 0;
        for (String s = br.readLine();s!=null;s = br.readLine()) {
          String sL[] = csvToString(s,0,';');
          INFO_IES[0][i]=codifLetrasServer.getCodigos(sL[13]+" "+sL[14]+" "+sL[15]+" "+sL[16]);
          INFO_IES[1][i]=codifNumeros.getCodigos(sL[10]);
          INFO_IES[2][i]= new byte[0];
          ++i;
        }
        br.close();
      }
      System.out.println(c.tics());
      System.out.println("Realizando match SISBEN");c.tic();
      MatcherSPADIES m = new MatcherSPADIES(INFO_IES,97.99,true,fSisbenToPro,new MyDataOutputStream(fos));
      byte[][] resSISBEN = m.procesar(m.total, null);
      System.out.println(c.tics());
      System.out.println("Match 2006");c.tic();
      MatcherSPADIES m2006 = ar2006.getOut().exists()?new MatcherSPADIES(INFO_IES,97.99,true,ar2006.getOut(),new MyDataOutputStream(fos)):null;
      byte[][] res2006 = m2006==null?null:m2006.procesar(m2006.total, null);
      System.out.println(c.tics());
      System.out.println("Match 2007");c.tic();
      MatcherSPADIES m2007 = ar2007.getOut().exists()?new MatcherSPADIES(INFO_IES,97.99,true,ar2007.getOut(),new MyDataOutputStream(fos)):null;
      byte[][] res2007 = m2007==null?null:m2007.procesar(m2007.total, null);
      System.out.println(c.tics());
      System.out.println("Match 2008");c.tic();
      MatcherSPADIES m2008 = ar2008.getOut().exists()?new MatcherSPADIES(INFO_IES,97.99,true,ar2008.getOut(),new MyDataOutputStream(fos)):null;
      byte[][] res2008 = m2008==null?null:m2008.procesar(m2008.total, null);
      System.out.println(c.tics());
      System.out.println("Match SABER");c.tic();
      MatcherSPADIES mSABER = arSABER.getOut().exists()?new MatcherSPADIES(INFO_IES,97.99,true,arSABER.getOut(),new MyDataOutputStream(fos)):null;
      byte[][] resSABER = new byte[res2008.length][0];Arrays.fill(resSABER, null);//mSABER==null?null:mSABER.procesar(mSABER.total, null);
      System.out.println(c.tics());
      int[] ind2006 = ar2006.getInd().exists()?cargarIndiceCSV_int(ar2006.getInd()):null;
      int[] ind2007 = ar2007.getInd().exists()?cargarIndiceCSV_int(ar2007.getInd()):null;
      int[] ind2008 = ar2008.getInd().exists()?cargarIndiceCSV_int(ar2008.getInd()):null;
      int[] indB = cargarIndiceCSV_int(f166_2005ind);
      RandomAccessFile rafB = new RandomAccessFile(f166_2005,"r"), 
        raf2006 = ar2006.getIn().exists()?new RandomAccessFile(ar2006.getIn(),"r"):null,
        raf2007 = ar2007.getIn().exists()?new RandomAccessFile(ar2007.getIn(),"r"):null,
        raf2008 = ar2008.getIn().exists()?new RandomAccessFile(ar2008.getIn(),"r"):null;
      {
        for (int pos = 0,t=INFO_IES[0].length;pos<t;pos++) {
          rafB.seek(indB[pos]);
          String dat1 = rafB.readLine(),
          dat2 = vacSaber, dat3=vacSisben;
          if (resSABER[pos]!=null) {
            int ind = new MyByteSequence(resSABER[pos]).getInt(0);
            rafSaber.seek(indSaber[ind]);
            dat2 = rafSaber.readLine();
          }
          if (resSISBEN[pos]!=null) {
            int ind = new MyByteSequence(resSISBEN[pos]).getInt(0);
            rafSis.seek(indSis[ind]);
            dat3 = rafSis.readLine();
          }
          ps.println(pos+";"+dat1 + ";" + dat2 + ";" + dat3);
          if (res2006!=null && res2006[pos]!=null) {
            int ind = new MyByteSequence(res2006[pos]).getInt(0);
            raf2006.seek(ind2006[ind]);
            dat1 = raf2006.readLine();
            ps.println(pos+";"+dat1 + ";" + dat2 + ";" + dat3);
          }
          if (res2007!=null && res2007[pos]!=null) {
            int ind = new MyByteSequence(res2007[pos]).getInt(0);
            raf2007.seek(ind2007[ind]);
            dat1 = raf2007.readLine();
            ps.println(pos+";"+dat1 + ";" + dat2 + ";" + dat3);
          }
          if (res2008!=null && res2008[pos]!=null) {
            int ind = new MyByteSequence(res2007[pos]).getInt(0);
            raf2008.seek(ind2008[ind]);
            dat1 = raf2008.readLine();
            ps.println(pos+";"+dat1 + ";" + dat2 + ";" + dat3);
          }
        }
      }      
      System.out.println(c.tics());
    }
    ps.close();
    System.out.println(System.currentTimeMillis()-tInic);
  }

  public static final String formateoCampoNum(int num) {
    return num==-1?"":String.valueOf(num);
  }
  
  public static final String formateoCampoNum(double num) {
    return num==-1.0?"":String.valueOf(num);
  }
  
  
  /**
   * Asume que hay encabezado, el primer registro es el 0
   * @param archivoCSV
   * @param fOut
   * @throws IOException
   */
  public static void indexarCSV(File archivoCSV, File fOut) throws IOException {
    FileReader fr = new FileReader(archivoCSV);
    List<Integer> posic = new LinkedList<Integer>();
    boolean proxf = false;
    int numc = 0;
    for (int car = fr.read();car!=-1;car = fr.read()) {
      if (car == 10 || car == 13) proxf = true;
      else if (proxf) {
        posic.add(numc);
        proxf = false;
      }
      ++numc;
    }
    fr.close();
    {
      MyDataOutputStream mdo = new MyDataOutputStream(new FileOutputStream(fOut));
      mdo.writeInt(posic.size());
      for (Integer val: posic) mdo.writeInt(val);
      mdo.close();
    }
  }

  public static int[] cargarIndiceCSV_int(File fIn) throws IOException {
    MyDataInputStream mdis = new MyDataInputStream(new FileInputStream(fIn));
    int tam = mdis.readInt();
    int[] res = new int[tam];
    for (int i = 0;i < tam;i++) res[i] = mdis.readInt();
    return res;
  }

  public static long[] cargarIndiceCSV(File fIn) throws IOException {
    MyDataInputStream mdis = new MyDataInputStream(new FileInputStream(fIn));
    int tam = mdis.readInt();
    long[] res = new long[tam];
    for (int i = 0;i < tam;i++) res[i] = mdis.readLong();
    return res;
  }

  public static String[] splitCSV(String s) {
    List<String> p=new LinkedList<String>();
    for (int i=0,t=s.length(); i<t; ) {
      if (s.charAt(i)==';') {
        p.add("");
        i++;
      }
      else if (s.charAt(i)!='"') {
        int j=s.indexOf(';',i+1);
        if (j==-1) j=t;
        p.add(s.substring(i,j));
        i=j+1;
      }
      else {
        int j=s.indexOf('"',i+1);
        while (j!=-1 && j<t-1 && s.charAt(j+1)=='"') j=s.indexOf('"',j+2);
        p.add(s.substring(i+1,j).replaceAll("\"\"","\""));
        j=s.indexOf(';',j+1);
        if (j==-1) j=t;
        i=j+1;
      }
      if (i==t) p.add("");
    }
    return p.toArray(new String[0]);
  }
  
  public static void impresionT(String msg) {
    System.out.println(formatoT(System.currentTimeMillis()-tInic) + " " + msg);
  }
  public static String formatoT(long tO) {
    int t = (int) (tO/1000);
    int s = t%60,
      m = (t/60)%60,
      h = t/(60*60);
    String stS = String.valueOf(s),
      stM = String.valueOf(m),
      stH = String.valueOf(h);
    if (stS.length()==1) stS = "0"+stS;
    if (stM.length()==1) stM = "0"+stM;
    return stH + "h " + stM + "m " + stS + "s ";
  }  
  public static int registrosEnArchivo(File f) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(f));
    br.readLine();//Lectura encabezado
    int conteo = 0;
    while(br.readLine()!=null) conteo++;
    br.close();
    return conteo;
  }
  
  public static String stringMasLargo(String ... strings) {
    int max = Integer.MIN_VALUE;
    for (String s:strings) if (s.length()>max) max = s.length();
    for (String s:strings) if (s.length()==max) return s;
    return null;
  }
}
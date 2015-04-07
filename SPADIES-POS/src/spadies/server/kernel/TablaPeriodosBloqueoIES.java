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
package spadies.server.kernel;

import java.io.*;
import java.util.*;

import spadies.server.util.*;
import spadies.util.CajaDeHerramientas;
import static spadies.util.CajaDeHerramientas.*;

public final class TablaPeriodosBloqueoIES {
  private static final TablaPeriodosBloqueoIES instance=new TablaPeriodosBloqueoIES();
  private Map<Integer,Byte> pers=new TreeMap<Integer,Byte>();
  public static TablaPeriodosBloqueoIES getInstance() {return instance;}
  private TablaPeriodosBloqueoIES() {}
  public void preparar() throws Exception {
    long tmi=System.currentTimeMillis();
    System.out.println("LEYENDO LOS PERIODOS DE BLOQUEO DE LAS IES ("+ConstantesServer.fPERBLOQUEO_IES.getName()+")");
    pers = lecturaPeriodos();
    System.out.println("  "+ConstantesServer.fPASSWORDS_IES.getName()+" LEIDO EN "+(System.currentTimeMillis()-tmi)+"ms");
  }
  public Integer getPerBloqueoIES(int codigo) {
    Byte b = pers.get(codigo);
    return b==null?null:(0xFF&b);
  }
  private static Map<Integer,Byte> lecturaPeriodos() throws NumberFormatException, IOException {
    Map<Integer,Byte> res = new TreeMap<Integer, Byte>();
    BufferedReader br=new BufferedReader(new FileReader(ConstantesServer.fPERBLOQUEO_IES));
    String enc = br.readLine();//encabezado
    for (String sL=br.readLine(); sL!=null; sL=br.readLine()) {
      String wL[]=csvToString(sL,0,';');
      res.put(Integer.parseInt(wL[0]),CajaDeHerramientas.getCodigoSemestre(wL[1]));
    }
    br.close();
    return Collections.unmodifiableMap(res);
  }
}

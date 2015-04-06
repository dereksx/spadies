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
import static spadies.util.CajaDeHerramientas.*;

public final class TablaDepartamentos {
  private static final TablaDepartamentos instance=new TablaDepartamentos();
  private final Map<Integer,Map<Integer,Double>> tasasDesempleo=new TreeMap<Integer,Map<Integer,Double>>();
  public static TablaDepartamentos getInstance() {return instance;}
  private TablaDepartamentos() {}
  public void preparar() throws Exception {
    long tmi=System.currentTimeMillis();
    System.out.println("LEYENDO LA LISTA DE TASAS DE DESEMPLEO DEPARTAMENTAL ("+ConstantesServer.fDEPARTAMENTOS_DESEMPLEO.getName()+")");
    BufferedReader br=new BufferedReader(new FileReader(ConstantesServer.fDEPARTAMENTOS_DESEMPLEO));
    for (String sL=br.readLine(); (sL=br.readLine())!=null; ) {
      String wL[]=csvToString(sL,0,';'),sCodigo=wL[0].trim(),sAnho=wL[1].trim(),sTasaDesempleo=wL[2].trim().replace(',','.');
      int codigo=Integer.parseInt(sCodigo);
      Map<Integer,Double> map=tasasDesempleo.get(codigo);
      if (map==null) tasasDesempleo.put(codigo,map=new TreeMap<Integer,Double>());
      map.put(Integer.parseInt(sAnho),Double.parseDouble(sTasaDesempleo));
    }
    System.out.println("  "+ConstantesServer.fDEPARTAMENTOS_DESEMPLEO.getName()+" LEIDO EN "+(System.currentTimeMillis()-tmi)+"ms");
  }
  public Map<Integer,Double> getTasasDesempleoPorAnho(int codigo) {
    return tasasDesempleo.get(codigo);
  }
}

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
package spadies.io;

import java.io.*;
import spadies.util.*;
/**
 * Permite leer tipos de datos especificos de un InputStream subyacente.
 */
public final class MyDataInputStream implements Closeable{
  private MyListener listener=null;
  private final InputStream is;
  private byte buffer[]=new byte[51200];
  private int pos=0,tot=0,size=0;
  public MyDataInputStream(InputStream pIs) {
    is=pIs;
  }
  public int getNumeroBytesLeidos() {
    return tot;
  }
  public void setListener(MyListener pListener) {
    listener=pListener;
  }
  /**
   * Lee un long de 8 bytes
   * @return el long leido
   * @throws IOException
   */
  public long readLong() throws IOException {
    return (readInt()&0xFFFFFFFFL)|((readInt()&0xFFFFFFFFL)<<32);
  }
  public int readInt() throws IOException {
    return (readByte()&0xFF)|((readByte()&0xFF)<<8)|((readByte()&0xFF)<<16)|((readByte()&0xFF)<<24);
  }
  /**
   * Lee un entero de tres bytes
   * @return el valor del entero leido
   * @throws IOException
   */
  public int readLittleInt() throws IOException {
    return (readByte()&0xFF)|((readByte()&0xFF)<<8)|((readByte()&0xFF)<<16);
  }
  /**
   * Lee un short de dos bytes
   * @return
   * @throws IOException
   */
  public short readShort() throws IOException {
    return (short)((readByte()&0xFF)|((readByte()&0xFF)<<8));
  }
  /**
   * Lee un float de cuatro bytes
   * @return
   * @throws IOException
   */
  public float readFloat() throws IOException {
    return Float.intBitsToFloat(readInt());
  }
  public byte[] readByteArray(boolean readSize, int t) throws IOException {
    if (readSize) t=readShort();
    byte v[]=new byte[t];
    for (int i=0; i<t; i++) v[i]=readByte();
    return v;
  }
  public byte[] readLargeByteArray(boolean readSize, int t) throws IOException {
    if (readSize) t=readInt();
    byte v[]=new byte[t];
    for (int i=0; i<t; i++) v[i]=readByte();
    return v;
  }
  public byte readByte() throws IOException {
    if (pos==0) {
      size=is.read(buffer);
      if (size<=0) buffer=null;
    }
    byte b=buffer[pos++]; tot++;
    if (pos==size) {
      pos=0;
      if (listener!=null) listener.notify(tot);
    }
    return b;
  }
  /**
   * Cierra el stream subyacente y libera los recursos asociados a el.
   * @throws IOException
   */
  public void close() throws IOException {
    is.close();
  }
}

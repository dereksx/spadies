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
package spadies.util;

public final class MyByteSequence {
  private final byte[] bytes;
  public MyByteSequence(int numBytes) {
    bytes=new byte[numBytes];
  }
  public MyByteSequence(byte[] bytes) {
    this.bytes=bytes;
  }
  public boolean getBoolean(int i, int j) {
    return ((bytes[i]>>>j)&1)==1;
  }
  public byte getBits2(int i, int j) {
    return (byte)((bytes[i]>>>j)&3);
  }
  public byte getBits4(int i, int j) {
    return (byte)((bytes[i]>>>j)&15);
  }
  public byte getByte(int i) {
    return bytes[i];
  }
  public short getShort(int i) {
    return (short)((bytes[i]&0xFF)|((bytes[i+1]&0xFF)<<8));
  }
  public int getLittleInt(int i) {
    return (bytes[i]&0xFF)|((bytes[i+1]&0xFF)<<8)|((bytes[i+2]&0xFF)<<16);
  }
  public int getInt(int i) {
    return (bytes[i]&0xFF)|((bytes[i+1]&0xFF)<<8)|((bytes[i+2]&0xFF)<<16)|((bytes[i+3]&0xFF)<<24);
  }
  public long getLong(int i) {
    return (getInt(i)&0xFFFFFFFFL)|((getInt(i+4)&0xFFFFFFFFL)<<32);
  }
  public void setBoolean(int i, int j, boolean v) {
    bytes[i]&=~(1<<j);
    if (v) bytes[i]|=(((byte)1)<<j);
  }
  public void setBits2(int i, int j, byte v) {
    bytes[i]&=~(3<<j);
    bytes[i]|=(v<<j);
  }
  public void setBits4(int i, int j, byte v) {
    bytes[i]&=~(15<<j);
    bytes[i]|=(v<<j);
  }
  public void setByte(int i, byte v) {
    bytes[i]=v;
  }
  public void setShort(int i, short v) {
    bytes[i]=(byte)(v&0xFF);
    bytes[i+1]=(byte)((v>>>8)&0xFF);
  }
  public void setLittleInt(int i, int v) {
    bytes[i]=(byte)(v&0xFF);
    bytes[i+1]=(byte)((v>>>8)&0xFF);
    bytes[i+2]=(byte)((v>>>16)&0xFF);
  }
  public void setInt(int i, int v) {
    bytes[i]=(byte)(v&0xFF);
    bytes[i+1]=(byte)((v>>>8)&0xFF);
    bytes[i+2]=(byte)((v>>>16)&0xFF);
    bytes[i+3]=(byte)((v>>>24)&0xFF);
  }
  public void setLong(int i, long v) {
    setInt(i,(int)(v&0xFFFFFFFFL));
    setInt(i+4,(int)((v>>>32)&0xFFFFFFFFL));
  }
  public byte[] getBytes() {
    return bytes;
  }
  public int getSize() {
    return bytes.length;
  }
}

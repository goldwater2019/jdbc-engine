/**
 * Autogenerated by Thrift Compiler (0.16.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.ane56.engine.jdbc.thrit.struct;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.16.0)", date = "2022-03-31")
public class TJDBCRsultRow implements org.apache.thrift.TBase<TJDBCRsultRow, TJDBCRsultRow._Fields>, java.io.Serializable, Cloneable, Comparable<TJDBCRsultRow> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TJDBCRsultRow");

  private static final org.apache.thrift.protocol.TField COLUMN_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("columnList", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TJDBCRsultRowStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TJDBCRsultRowTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.util.List<TJDBCResultColumn> columnList; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    COLUMN_LIST((short)1, "columnList");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // COLUMN_LIST
          return COLUMN_LIST;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.COLUMN_LIST, new org.apache.thrift.meta_data.FieldMetaData("columnList", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TJDBCResultColumn.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TJDBCRsultRow.class, metaDataMap);
  }

  public TJDBCRsultRow() {
  }

  public TJDBCRsultRow(
    java.util.List<TJDBCResultColumn> columnList)
  {
    this();
    this.columnList = columnList;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TJDBCRsultRow(TJDBCRsultRow other) {
    if (other.isSetColumnList()) {
      java.util.List<TJDBCResultColumn> __this__columnList = new java.util.ArrayList<TJDBCResultColumn>(other.columnList.size());
      for (TJDBCResultColumn other_element : other.columnList) {
        __this__columnList.add(new TJDBCResultColumn(other_element));
      }
      this.columnList = __this__columnList;
    }
  }

  public TJDBCRsultRow deepCopy() {
    return new TJDBCRsultRow(this);
  }

  @Override
  public void clear() {
    this.columnList = null;
  }

  public int getColumnListSize() {
    return (this.columnList == null) ? 0 : this.columnList.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<TJDBCResultColumn> getColumnListIterator() {
    return (this.columnList == null) ? null : this.columnList.iterator();
  }

  public void addToColumnList(TJDBCResultColumn elem) {
    if (this.columnList == null) {
      this.columnList = new java.util.ArrayList<TJDBCResultColumn>();
    }
    this.columnList.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<TJDBCResultColumn> getColumnList() {
    return this.columnList;
  }

  public TJDBCRsultRow setColumnList(@org.apache.thrift.annotation.Nullable java.util.List<TJDBCResultColumn> columnList) {
    this.columnList = columnList;
    return this;
  }

  public void unsetColumnList() {
    this.columnList = null;
  }

  /** Returns true if field columnList is set (has been assigned a value) and false otherwise */
  public boolean isSetColumnList() {
    return this.columnList != null;
  }

  public void setColumnListIsSet(boolean value) {
    if (!value) {
      this.columnList = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case COLUMN_LIST:
      if (value == null) {
        unsetColumnList();
      } else {
        setColumnList((java.util.List<TJDBCResultColumn>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case COLUMN_LIST:
      return getColumnList();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case COLUMN_LIST:
      return isSetColumnList();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TJDBCRsultRow)
      return this.equals((TJDBCRsultRow)that);
    return false;
  }

  public boolean equals(TJDBCRsultRow that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_columnList = true && this.isSetColumnList();
    boolean that_present_columnList = true && that.isSetColumnList();
    if (this_present_columnList || that_present_columnList) {
      if (!(this_present_columnList && that_present_columnList))
        return false;
      if (!this.columnList.equals(that.columnList))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetColumnList()) ? 131071 : 524287);
    if (isSetColumnList())
      hashCode = hashCode * 8191 + columnList.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TJDBCRsultRow other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetColumnList(), other.isSetColumnList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetColumnList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.columnList, other.columnList);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TJDBCRsultRow(");
    boolean first = true;

    sb.append("columnList:");
    if (this.columnList == null) {
      sb.append("null");
    } else {
      sb.append(this.columnList);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TJDBCRsultRowStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TJDBCRsultRowStandardScheme getScheme() {
      return new TJDBCRsultRowStandardScheme();
    }
  }

  private static class TJDBCRsultRowStandardScheme extends org.apache.thrift.scheme.StandardScheme<TJDBCRsultRow> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TJDBCRsultRow struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // COLUMN_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.columnList = new java.util.ArrayList<TJDBCResultColumn>(_list0.size);
                @org.apache.thrift.annotation.Nullable TJDBCResultColumn _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = new TJDBCResultColumn();
                  _elem1.read(iprot);
                  struct.columnList.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setColumnListIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TJDBCRsultRow struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.columnList != null) {
        oprot.writeFieldBegin(COLUMN_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.columnList.size()));
          for (TJDBCResultColumn _iter3 : struct.columnList)
          {
            _iter3.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TJDBCRsultRowTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TJDBCRsultRowTupleScheme getScheme() {
      return new TJDBCRsultRowTupleScheme();
    }
  }

  private static class TJDBCRsultRowTupleScheme extends org.apache.thrift.scheme.TupleScheme<TJDBCRsultRow> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TJDBCRsultRow struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetColumnList()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetColumnList()) {
        {
          oprot.writeI32(struct.columnList.size());
          for (TJDBCResultColumn _iter4 : struct.columnList)
          {
            _iter4.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TJDBCRsultRow struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list5 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRUCT);
          struct.columnList = new java.util.ArrayList<TJDBCResultColumn>(_list5.size);
          @org.apache.thrift.annotation.Nullable TJDBCResultColumn _elem6;
          for (int _i7 = 0; _i7 < _list5.size; ++_i7)
          {
            _elem6 = new TJDBCResultColumn();
            _elem6.read(iprot);
            struct.columnList.add(_elem6);
          }
        }
        struct.setColumnListIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}


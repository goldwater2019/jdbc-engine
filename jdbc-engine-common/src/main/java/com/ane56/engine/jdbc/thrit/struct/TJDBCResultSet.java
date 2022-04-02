/**
 * Autogenerated by Thrift Compiler (0.16.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.ane56.engine.jdbc.thrit.struct;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.16.0)", date = "2022-04-02")
public class TJDBCResultSet implements org.apache.thrift.TBase<TJDBCResultSet, TJDBCResultSet._Fields>, java.io.Serializable, Cloneable, Comparable<TJDBCResultSet> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TJDBCResultSet");

  private static final org.apache.thrift.protocol.TField RESULT_ROW_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("resultRowList", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TJDBCResultSetStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TJDBCResultSetTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.util.List<TJDBCRsultRow> resultRowList; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    RESULT_ROW_LIST((short)1, "resultRowList");

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
        case 1: // RESULT_ROW_LIST
          return RESULT_ROW_LIST;
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
    tmpMap.put(_Fields.RESULT_ROW_LIST, new org.apache.thrift.meta_data.FieldMetaData("resultRowList", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TJDBCRsultRow.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TJDBCResultSet.class, metaDataMap);
  }

  public TJDBCResultSet() {
  }

  public TJDBCResultSet(
    java.util.List<TJDBCRsultRow> resultRowList)
  {
    this();
    this.resultRowList = resultRowList;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TJDBCResultSet(TJDBCResultSet other) {
    if (other.isSetResultRowList()) {
      java.util.List<TJDBCRsultRow> __this__resultRowList = new java.util.ArrayList<TJDBCRsultRow>(other.resultRowList.size());
      for (TJDBCRsultRow other_element : other.resultRowList) {
        __this__resultRowList.add(new TJDBCRsultRow(other_element));
      }
      this.resultRowList = __this__resultRowList;
    }
  }

  public TJDBCResultSet deepCopy() {
    return new TJDBCResultSet(this);
  }

  @Override
  public void clear() {
    this.resultRowList = null;
  }

  public int getResultRowListSize() {
    return (this.resultRowList == null) ? 0 : this.resultRowList.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<TJDBCRsultRow> getResultRowListIterator() {
    return (this.resultRowList == null) ? null : this.resultRowList.iterator();
  }

  public void addToResultRowList(TJDBCRsultRow elem) {
    if (this.resultRowList == null) {
      this.resultRowList = new java.util.ArrayList<TJDBCRsultRow>();
    }
    this.resultRowList.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<TJDBCRsultRow> getResultRowList() {
    return this.resultRowList;
  }

  public TJDBCResultSet setResultRowList(@org.apache.thrift.annotation.Nullable java.util.List<TJDBCRsultRow> resultRowList) {
    this.resultRowList = resultRowList;
    return this;
  }

  public void unsetResultRowList() {
    this.resultRowList = null;
  }

  /** Returns true if field resultRowList is set (has been assigned a value) and false otherwise */
  public boolean isSetResultRowList() {
    return this.resultRowList != null;
  }

  public void setResultRowListIsSet(boolean value) {
    if (!value) {
      this.resultRowList = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case RESULT_ROW_LIST:
      if (value == null) {
        unsetResultRowList();
      } else {
        setResultRowList((java.util.List<TJDBCRsultRow>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case RESULT_ROW_LIST:
      return getResultRowList();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case RESULT_ROW_LIST:
      return isSetResultRowList();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TJDBCResultSet)
      return this.equals((TJDBCResultSet)that);
    return false;
  }

  public boolean equals(TJDBCResultSet that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_resultRowList = true && this.isSetResultRowList();
    boolean that_present_resultRowList = true && that.isSetResultRowList();
    if (this_present_resultRowList || that_present_resultRowList) {
      if (!(this_present_resultRowList && that_present_resultRowList))
        return false;
      if (!this.resultRowList.equals(that.resultRowList))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetResultRowList()) ? 131071 : 524287);
    if (isSetResultRowList())
      hashCode = hashCode * 8191 + resultRowList.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TJDBCResultSet other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetResultRowList(), other.isSetResultRowList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResultRowList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.resultRowList, other.resultRowList);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TJDBCResultSet(");
    boolean first = true;

    sb.append("resultRowList:");
    if (this.resultRowList == null) {
      sb.append("null");
    } else {
      sb.append(this.resultRowList);
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

  private static class TJDBCResultSetStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TJDBCResultSetStandardScheme getScheme() {
      return new TJDBCResultSetStandardScheme();
    }
  }

  private static class TJDBCResultSetStandardScheme extends org.apache.thrift.scheme.StandardScheme<TJDBCResultSet> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TJDBCResultSet struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // RESULT_ROW_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list8 = iprot.readListBegin();
                struct.resultRowList = new java.util.ArrayList<TJDBCRsultRow>(_list8.size);
                @org.apache.thrift.annotation.Nullable TJDBCRsultRow _elem9;
                for (int _i10 = 0; _i10 < _list8.size; ++_i10)
                {
                  _elem9 = new TJDBCRsultRow();
                  _elem9.read(iprot);
                  struct.resultRowList.add(_elem9);
                }
                iprot.readListEnd();
              }
              struct.setResultRowListIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, TJDBCResultSet struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.resultRowList != null) {
        oprot.writeFieldBegin(RESULT_ROW_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.resultRowList.size()));
          for (TJDBCRsultRow _iter11 : struct.resultRowList)
          {
            _iter11.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TJDBCResultSetTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TJDBCResultSetTupleScheme getScheme() {
      return new TJDBCResultSetTupleScheme();
    }
  }

  private static class TJDBCResultSetTupleScheme extends org.apache.thrift.scheme.TupleScheme<TJDBCResultSet> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TJDBCResultSet struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetResultRowList()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetResultRowList()) {
        {
          oprot.writeI32(struct.resultRowList.size());
          for (TJDBCRsultRow _iter12 : struct.resultRowList)
          {
            _iter12.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TJDBCResultSet struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list13 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRUCT);
          struct.resultRowList = new java.util.ArrayList<TJDBCRsultRow>(_list13.size);
          @org.apache.thrift.annotation.Nullable TJDBCRsultRow _elem14;
          for (int _i15 = 0; _i15 < _list13.size; ++_i15)
          {
            _elem14 = new TJDBCRsultRow();
            _elem14.read(iprot);
            struct.resultRowList.add(_elem14);
          }
        }
        struct.setResultRowListIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}


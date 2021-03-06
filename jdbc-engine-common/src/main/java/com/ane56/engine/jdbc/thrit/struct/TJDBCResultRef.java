/**
 * Autogenerated by Thrift Compiler (0.16.0)
 * <p>
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *
 * @generated
 */
package com.ane56.engine.jdbc.thrit.struct;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.16.0)", date = "2022-04-02")
public class TJDBCResultRef implements org.apache.thrift.TBase<TJDBCResultRef, TJDBCResultRef._Fields>, java.io.Serializable, Cloneable, Comparable<TJDBCResultRef> {
    // isset id assignments
    public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TJDBCResultRef");
    private static final org.apache.thrift.protocol.TField RESULT_SET_FIELD_DESC = new org.apache.thrift.protocol.TField("resultSet", org.apache.thrift.protocol.TType.STRUCT, (short) 1);
    private static final org.apache.thrift.protocol.TField OPERATION_REF_FIELD_DESC = new org.apache.thrift.protocol.TField("operationRef", org.apache.thrift.protocol.TType.STRUCT, (short) 2);
    private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TJDBCResultRefStandardSchemeFactory();
    private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TJDBCResultRefTupleSchemeFactory();

    static {
        java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
        tmpMap.put(_Fields.RESULT_SET, new org.apache.thrift.meta_data.FieldMetaData("resultSet", org.apache.thrift.TFieldRequirementType.DEFAULT,
                new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TJDBCResultSet.class)));
        tmpMap.put(_Fields.OPERATION_REF, new org.apache.thrift.meta_data.FieldMetaData("operationRef", org.apache.thrift.TFieldRequirementType.DEFAULT,
                new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TJDBCOperationRef.class)));
        metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
        org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TJDBCResultRef.class, metaDataMap);
    }

    public @org.apache.thrift.annotation.Nullable
    TJDBCResultSet resultSet; // required
    public @org.apache.thrift.annotation.Nullable
    TJDBCOperationRef operationRef; // required

    public TJDBCResultRef() {
    }

    public TJDBCResultRef(
            TJDBCResultSet resultSet,
            TJDBCOperationRef operationRef) {
        this();
        this.resultSet = resultSet;
        this.operationRef = operationRef;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public TJDBCResultRef(TJDBCResultRef other) {
        if (other.isSetResultSet()) {
            this.resultSet = new TJDBCResultSet(other.resultSet);
        }
        if (other.isSetOperationRef()) {
            this.operationRef = new TJDBCOperationRef(other.operationRef);
        }
    }

    private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
        return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
    }

    public TJDBCResultRef deepCopy() {
        return new TJDBCResultRef(this);
    }

    @Override
    public void clear() {
        this.resultSet = null;
        this.operationRef = null;
    }

    @org.apache.thrift.annotation.Nullable
    public TJDBCResultSet getResultSet() {
        return this.resultSet;
    }

    public TJDBCResultRef setResultSet(@org.apache.thrift.annotation.Nullable TJDBCResultSet resultSet) {
        this.resultSet = resultSet;
        return this;
    }

    public void unsetResultSet() {
        this.resultSet = null;
    }

    /**
     * Returns true if field resultSet is set (has been assigned a value) and false otherwise
     */
    public boolean isSetResultSet() {
        return this.resultSet != null;
    }

    public void setResultSetIsSet(boolean value) {
        if (!value) {
            this.resultSet = null;
        }
    }

    @org.apache.thrift.annotation.Nullable
    public TJDBCOperationRef getOperationRef() {
        return this.operationRef;
    }

    public TJDBCResultRef setOperationRef(@org.apache.thrift.annotation.Nullable TJDBCOperationRef operationRef) {
        this.operationRef = operationRef;
        return this;
    }

    public void unsetOperationRef() {
        this.operationRef = null;
    }

    /**
     * Returns true if field operationRef is set (has been assigned a value) and false otherwise
     */
    public boolean isSetOperationRef() {
        return this.operationRef != null;
    }

    public void setOperationRefIsSet(boolean value) {
        if (!value) {
            this.operationRef = null;
        }
    }

    public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
        switch (field) {
            case RESULT_SET:
                if (value == null) {
                    unsetResultSet();
                } else {
                    setResultSet((TJDBCResultSet) value);
                }
                break;

            case OPERATION_REF:
                if (value == null) {
                    unsetOperationRef();
                } else {
                    setOperationRef((TJDBCOperationRef) value);
                }
                break;

        }
    }

    @org.apache.thrift.annotation.Nullable
    public java.lang.Object getFieldValue(_Fields field) {
        switch (field) {
            case RESULT_SET:
                return getResultSet();

            case OPERATION_REF:
                return getOperationRef();

        }
        throw new java.lang.IllegalStateException();
    }

    /**
     * Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise
     */
    public boolean isSet(_Fields field) {
        if (field == null) {
            throw new java.lang.IllegalArgumentException();
        }

        switch (field) {
            case RESULT_SET:
                return isSetResultSet();
            case OPERATION_REF:
                return isSetOperationRef();
        }
        throw new java.lang.IllegalStateException();
    }

    @Override
    public boolean equals(java.lang.Object that) {
        if (that instanceof TJDBCResultRef)
            return this.equals((TJDBCResultRef) that);
        return false;
    }

    public boolean equals(TJDBCResultRef that) {
        if (that == null)
            return false;
        if (this == that)
            return true;

        boolean this_present_resultSet = true && this.isSetResultSet();
        boolean that_present_resultSet = true && that.isSetResultSet();
        if (this_present_resultSet || that_present_resultSet) {
            if (!(this_present_resultSet && that_present_resultSet))
                return false;
            if (!this.resultSet.equals(that.resultSet))
                return false;
        }

        boolean this_present_operationRef = true && this.isSetOperationRef();
        boolean that_present_operationRef = true && that.isSetOperationRef();
        if (this_present_operationRef || that_present_operationRef) {
            if (!(this_present_operationRef && that_present_operationRef))
                return false;
            if (!this.operationRef.equals(that.operationRef))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode = hashCode * 8191 + ((isSetResultSet()) ? 131071 : 524287);
        if (isSetResultSet())
            hashCode = hashCode * 8191 + resultSet.hashCode();

        hashCode = hashCode * 8191 + ((isSetOperationRef()) ? 131071 : 524287);
        if (isSetOperationRef())
            hashCode = hashCode * 8191 + operationRef.hashCode();

        return hashCode;
    }

    @Override
    public int compareTo(TJDBCResultRef other) {
        if (!getClass().equals(other.getClass())) {
            return getClass().getName().compareTo(other.getClass().getName());
        }

        int lastComparison = 0;

        lastComparison = java.lang.Boolean.compare(isSetResultSet(), other.isSetResultSet());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetResultSet()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.resultSet, other.resultSet);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = java.lang.Boolean.compare(isSetOperationRef(), other.isSetOperationRef());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetOperationRef()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.operationRef, other.operationRef);
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
        java.lang.StringBuilder sb = new java.lang.StringBuilder("TJDBCResultRef(");
        boolean first = true;

        sb.append("resultSet:");
        if (this.resultSet == null) {
            sb.append("null");
        } else {
            sb.append(this.resultSet);
        }
        first = false;
        if (!first) sb.append(", ");
        sb.append("operationRef:");
        if (this.operationRef == null) {
            sb.append("null");
        } else {
            sb.append(this.operationRef);
        }
        first = false;
        sb.append(")");
        return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
        // check for required fields
        // check for sub-struct validity
        if (resultSet != null) {
            resultSet.validate();
        }
        if (operationRef != null) {
            operationRef.validate();
        }
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

    /**
     * The set of fields this struct contains, along with convenience methods for finding and manipulating them.
     */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
        RESULT_SET((short) 1, "resultSet"),
        OPERATION_REF((short) 2, "operationRef");

        private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

        static {
            for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
                byName.put(field.getFieldName(), field);
            }
        }

        private final short _thriftId;
        private final java.lang.String _fieldName;

        _Fields(short thriftId, java.lang.String fieldName) {
            _thriftId = thriftId;
            _fieldName = fieldName;
        }

        /**
         * Find the _Fields constant that matches fieldId, or null if its not found.
         */
        @org.apache.thrift.annotation.Nullable
        public static _Fields findByThriftId(int fieldId) {
            switch (fieldId) {
                case 1: // RESULT_SET
                    return RESULT_SET;
                case 2: // OPERATION_REF
                    return OPERATION_REF;
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

        public short getThriftFieldId() {
            return _thriftId;
        }

        public java.lang.String getFieldName() {
            return _fieldName;
        }
    }

    private static class TJDBCResultRefStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
        public TJDBCResultRefStandardScheme getScheme() {
            return new TJDBCResultRefStandardScheme();
        }
    }

    private static class TJDBCResultRefStandardScheme extends org.apache.thrift.scheme.StandardScheme<TJDBCResultRef> {

        public void read(org.apache.thrift.protocol.TProtocol iprot, TJDBCResultRef struct) throws org.apache.thrift.TException {
            org.apache.thrift.protocol.TField schemeField;
            iprot.readStructBegin();
            while (true) {
                schemeField = iprot.readFieldBegin();
                if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
                    break;
                }
                switch (schemeField.id) {
                    case 1: // RESULT_SET
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
                            struct.resultSet = new TJDBCResultSet();
                            struct.resultSet.read(iprot);
                            struct.setResultSetIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 2: // OPERATION_REF
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
                            struct.operationRef = new TJDBCOperationRef();
                            struct.operationRef.read(iprot);
                            struct.setOperationRefIsSet(true);
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

        public void write(org.apache.thrift.protocol.TProtocol oprot, TJDBCResultRef struct) throws org.apache.thrift.TException {
            struct.validate();

            oprot.writeStructBegin(STRUCT_DESC);
            if (struct.resultSet != null) {
                oprot.writeFieldBegin(RESULT_SET_FIELD_DESC);
                struct.resultSet.write(oprot);
                oprot.writeFieldEnd();
            }
            if (struct.operationRef != null) {
                oprot.writeFieldBegin(OPERATION_REF_FIELD_DESC);
                struct.operationRef.write(oprot);
                oprot.writeFieldEnd();
            }
            oprot.writeFieldStop();
            oprot.writeStructEnd();
        }

    }

    private static class TJDBCResultRefTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
        public TJDBCResultRefTupleScheme getScheme() {
            return new TJDBCResultRefTupleScheme();
        }
    }

    private static class TJDBCResultRefTupleScheme extends org.apache.thrift.scheme.TupleScheme<TJDBCResultRef> {

        @Override
        public void write(org.apache.thrift.protocol.TProtocol prot, TJDBCResultRef struct) throws org.apache.thrift.TException {
            org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
            java.util.BitSet optionals = new java.util.BitSet();
            if (struct.isSetResultSet()) {
                optionals.set(0);
            }
            if (struct.isSetOperationRef()) {
                optionals.set(1);
            }
            oprot.writeBitSet(optionals, 2);
            if (struct.isSetResultSet()) {
                struct.resultSet.write(oprot);
            }
            if (struct.isSetOperationRef()) {
                struct.operationRef.write(oprot);
            }
        }

        @Override
        public void read(org.apache.thrift.protocol.TProtocol prot, TJDBCResultRef struct) throws org.apache.thrift.TException {
            org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
            java.util.BitSet incoming = iprot.readBitSet(2);
            if (incoming.get(0)) {
                struct.resultSet = new TJDBCResultSet();
                struct.resultSet.read(iprot);
                struct.setResultSetIsSet(true);
            }
            if (incoming.get(1)) {
                struct.operationRef = new TJDBCOperationRef();
                struct.operationRef.read(iprot);
                struct.setOperationRefIsSet(true);
            }
        }
    }
}


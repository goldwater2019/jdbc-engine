/**
 * Autogenerated by Thrift Compiler (0.16.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.ane56.engine.jdbc.thrit.service;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.16.0)", date = "2022-03-31")
public class JDBCEngineExecutorService {

  public interface Iface {

    /**
     * SQL查询
     * 请求时绑定starttime
     * 结束时绑定endtime
     * 
     * 
     * @param jdbcOperationRef
     */
    public com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef query(com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef) throws org.apache.thrift.TException;

  }

  public interface AsyncIface {

    public void query(com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef, org.apache.thrift.async.AsyncMethodCallback<com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef> resultHandler) throws org.apache.thrift.TException;

  }

  public static class Client extends org.apache.thrift.TServiceClient implements Iface {
    public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
      public Factory() {}
      public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
        return new Client(prot);
      }
      public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
        return new Client(iprot, oprot);
      }
    }

    public Client(org.apache.thrift.protocol.TProtocol prot)
    {
      super(prot, prot);
    }

    public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
      super(iprot, oprot);
    }

    public com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef query(com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef) throws org.apache.thrift.TException
    {
      send_query(jdbcOperationRef);
      return recv_query();
    }

    public void send_query(com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef) throws org.apache.thrift.TException
    {
      query_args args = new query_args();
      args.setJdbcOperationRef(jdbcOperationRef);
      sendBase("query", args);
    }

    public com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef recv_query() throws org.apache.thrift.TException
    {
      query_result result = new query_result();
      receiveBase(result, "query");
      if (result.isSetSuccess()) {
        return result.success;
      }
      throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "query failed: unknown result");
    }

  }
  public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
    public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
      private org.apache.thrift.async.TAsyncClientManager clientManager;
      private org.apache.thrift.protocol.TProtocolFactory protocolFactory;
      public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
        this.clientManager = clientManager;
        this.protocolFactory = protocolFactory;
      }
      public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
        return new AsyncClient(protocolFactory, clientManager, transport);
      }
    }

    public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
      super(protocolFactory, clientManager, transport);
    }

    public void query(com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef, org.apache.thrift.async.AsyncMethodCallback<com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef> resultHandler) throws org.apache.thrift.TException {
      checkReady();
      query_call method_call = new query_call(jdbcOperationRef, resultHandler, this, ___protocolFactory, ___transport);
      this.___currentMethod = method_call;
      ___manager.call(method_call);
    }

    public static class query_call extends org.apache.thrift.async.TAsyncMethodCall<com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef> {
      private com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef;
      public query_call(com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef, org.apache.thrift.async.AsyncMethodCallback<com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef> resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
        super(client, protocolFactory, transport, resultHandler, false);
        this.jdbcOperationRef = jdbcOperationRef;
      }

      public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
        prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("query", org.apache.thrift.protocol.TMessageType.CALL, 0));
        query_args args = new query_args();
        args.setJdbcOperationRef(jdbcOperationRef);
        args.write(prot);
        prot.writeMessageEnd();
      }

      public com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef getResult() throws org.apache.thrift.TException {
        if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
          throw new java.lang.IllegalStateException("Method call not finished!");
        }
        org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
        org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
        return (new Client(prot)).recv_query();
      }
    }

  }

  public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor<I> implements org.apache.thrift.TProcessor {
    private static final org.slf4j.Logger _LOGGER = org.slf4j.LoggerFactory.getLogger(Processor.class.getName());
    public Processor(I iface) {
      super(iface, getProcessMap(new java.util.HashMap<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
    }

    protected Processor(I iface, java.util.Map<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
      super(iface, getProcessMap(processMap));
    }

    private static <I extends Iface> java.util.Map<java.lang.String,  org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> getProcessMap(java.util.Map<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {
      processMap.put("query", new query());
      return processMap;
    }

    public static class query<I extends Iface> extends org.apache.thrift.ProcessFunction<I, query_args> {
      public query() {
        super("query");
      }

      public query_args getEmptyArgsInstance() {
        return new query_args();
      }

      protected boolean isOneway() {
        return false;
      }

      @Override
      protected boolean rethrowUnhandledExceptions() {
        return false;
      }

      public query_result getResult(I iface, query_args args) throws org.apache.thrift.TException {
        query_result result = new query_result();
        result.success = iface.query(args.jdbcOperationRef);
        return result;
      }
    }

  }

  public static class AsyncProcessor<I extends AsyncIface> extends org.apache.thrift.TBaseAsyncProcessor<I> {
    private static final org.slf4j.Logger _LOGGER = org.slf4j.LoggerFactory.getLogger(AsyncProcessor.class.getName());
    public AsyncProcessor(I iface) {
      super(iface, getProcessMap(new java.util.HashMap<java.lang.String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));
    }

    protected AsyncProcessor(I iface, java.util.Map<java.lang.String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {
      super(iface, getProcessMap(processMap));
    }

    private static <I extends AsyncIface> java.util.Map<java.lang.String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase,?>> getProcessMap(java.util.Map<java.lang.String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {
      processMap.put("query", new query());
      return processMap;
    }

    public static class query<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, query_args, com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef> {
      public query() {
        super("query");
      }

      public query_args getEmptyArgsInstance() {
        return new query_args();
      }

      public org.apache.thrift.async.AsyncMethodCallback<com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef> getResultHandler(final org.apache.thrift.server.AbstractNonblockingServer.AsyncFrameBuffer fb, final int seqid) {
        final org.apache.thrift.AsyncProcessFunction fcall = this;
        return new org.apache.thrift.async.AsyncMethodCallback<com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef>() { 
          public void onComplete(com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef o) {
            query_result result = new query_result();
            result.success = o;
            try {
              fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY,seqid);
            } catch (org.apache.thrift.transport.TTransportException e) {
              _LOGGER.error("TTransportException writing to internal frame buffer", e);
              fb.close();
            } catch (java.lang.Exception e) {
              _LOGGER.error("Exception writing to internal frame buffer", e);
              onError(e);
            }
          }
          public void onError(java.lang.Exception e) {
            byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
            org.apache.thrift.TSerializable msg;
            query_result result = new query_result();
            if (e instanceof org.apache.thrift.transport.TTransportException) {
              _LOGGER.error("TTransportException inside handler", e);
              fb.close();
              return;
            } else if (e instanceof org.apache.thrift.TApplicationException) {
              _LOGGER.error("TApplicationException inside handler", e);
              msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
              msg = (org.apache.thrift.TApplicationException)e;
            } else {
              _LOGGER.error("Exception inside handler", e);
              msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
              msg = new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
            }
            try {
              fcall.sendResponse(fb,msg,msgType,seqid);
            } catch (java.lang.Exception ex) {
              _LOGGER.error("Exception writing to internal frame buffer", ex);
              fb.close();
            }
          }
        };
      }

      protected boolean isOneway() {
        return false;
      }

      public void start(I iface, query_args args, org.apache.thrift.async.AsyncMethodCallback<com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef> resultHandler) throws org.apache.thrift.TException {
        iface.query(args.jdbcOperationRef,resultHandler);
      }
    }

  }

  public static class query_args implements org.apache.thrift.TBase<query_args, query_args._Fields>, java.io.Serializable, Cloneable, Comparable<query_args>   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("query_args");

    private static final org.apache.thrift.protocol.TField JDBC_OPERATION_REF_FIELD_DESC = new org.apache.thrift.protocol.TField("jdbcOperationRef", org.apache.thrift.protocol.TType.STRUCT, (short)1);

    private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new query_argsStandardSchemeFactory();
    private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new query_argsTupleSchemeFactory();

    public @org.apache.thrift.annotation.Nullable com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      JDBC_OPERATION_REF((short)1, "jdbcOperationRef");

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
          case 1: // JDBC_OPERATION_REF
            return JDBC_OPERATION_REF;
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
      tmpMap.put(_Fields.JDBC_OPERATION_REF, new org.apache.thrift.meta_data.FieldMetaData("jdbcOperationRef", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef.class)));
      metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(query_args.class, metaDataMap);
    }

    public query_args() {
    }

    public query_args(
      com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef)
    {
      this();
      this.jdbcOperationRef = jdbcOperationRef;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public query_args(query_args other) {
      if (other.isSetJdbcOperationRef()) {
        this.jdbcOperationRef = new com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef(other.jdbcOperationRef);
      }
    }

    public query_args deepCopy() {
      return new query_args(this);
    }

    @Override
    public void clear() {
      this.jdbcOperationRef = null;
    }

    @org.apache.thrift.annotation.Nullable
    public com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef getJdbcOperationRef() {
      return this.jdbcOperationRef;
    }

    public query_args setJdbcOperationRef(@org.apache.thrift.annotation.Nullable com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef jdbcOperationRef) {
      this.jdbcOperationRef = jdbcOperationRef;
      return this;
    }

    public void unsetJdbcOperationRef() {
      this.jdbcOperationRef = null;
    }

    /** Returns true if field jdbcOperationRef is set (has been assigned a value) and false otherwise */
    public boolean isSetJdbcOperationRef() {
      return this.jdbcOperationRef != null;
    }

    public void setJdbcOperationRefIsSet(boolean value) {
      if (!value) {
        this.jdbcOperationRef = null;
      }
    }

    public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
      switch (field) {
      case JDBC_OPERATION_REF:
        if (value == null) {
          unsetJdbcOperationRef();
        } else {
          setJdbcOperationRef((com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef)value);
        }
        break;

      }
    }

    @org.apache.thrift.annotation.Nullable
    public java.lang.Object getFieldValue(_Fields field) {
      switch (field) {
      case JDBC_OPERATION_REF:
        return getJdbcOperationRef();

      }
      throw new java.lang.IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new java.lang.IllegalArgumentException();
      }

      switch (field) {
      case JDBC_OPERATION_REF:
        return isSetJdbcOperationRef();
      }
      throw new java.lang.IllegalStateException();
    }

    @Override
    public boolean equals(java.lang.Object that) {
      if (that instanceof query_args)
        return this.equals((query_args)that);
      return false;
    }

    public boolean equals(query_args that) {
      if (that == null)
        return false;
      if (this == that)
        return true;

      boolean this_present_jdbcOperationRef = true && this.isSetJdbcOperationRef();
      boolean that_present_jdbcOperationRef = true && that.isSetJdbcOperationRef();
      if (this_present_jdbcOperationRef || that_present_jdbcOperationRef) {
        if (!(this_present_jdbcOperationRef && that_present_jdbcOperationRef))
          return false;
        if (!this.jdbcOperationRef.equals(that.jdbcOperationRef))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      int hashCode = 1;

      hashCode = hashCode * 8191 + ((isSetJdbcOperationRef()) ? 131071 : 524287);
      if (isSetJdbcOperationRef())
        hashCode = hashCode * 8191 + jdbcOperationRef.hashCode();

      return hashCode;
    }

    @Override
    public int compareTo(query_args other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;

      lastComparison = java.lang.Boolean.compare(isSetJdbcOperationRef(), other.isSetJdbcOperationRef());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetJdbcOperationRef()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.jdbcOperationRef, other.jdbcOperationRef);
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
      java.lang.StringBuilder sb = new java.lang.StringBuilder("query_args(");
      boolean first = true;

      sb.append("jdbcOperationRef:");
      if (this.jdbcOperationRef == null) {
        sb.append("null");
      } else {
        sb.append(this.jdbcOperationRef);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
      // check for sub-struct validity
      if (jdbcOperationRef != null) {
        jdbcOperationRef.validate();
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

    private static class query_argsStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
      public query_argsStandardScheme getScheme() {
        return new query_argsStandardScheme();
      }
    }

    private static class query_argsStandardScheme extends org.apache.thrift.scheme.StandardScheme<query_args> {

      public void read(org.apache.thrift.protocol.TProtocol iprot, query_args struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TField schemeField;
        iprot.readStructBegin();
        while (true)
        {
          schemeField = iprot.readFieldBegin();
          if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
            break;
          }
          switch (schemeField.id) {
            case 1: // JDBC_OPERATION_REF
              if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
                struct.jdbcOperationRef = new com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef();
                struct.jdbcOperationRef.read(iprot);
                struct.setJdbcOperationRefIsSet(true);
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

      public void write(org.apache.thrift.protocol.TProtocol oprot, query_args struct) throws org.apache.thrift.TException {
        struct.validate();

        oprot.writeStructBegin(STRUCT_DESC);
        if (struct.jdbcOperationRef != null) {
          oprot.writeFieldBegin(JDBC_OPERATION_REF_FIELD_DESC);
          struct.jdbcOperationRef.write(oprot);
          oprot.writeFieldEnd();
        }
        oprot.writeFieldStop();
        oprot.writeStructEnd();
      }

    }

    private static class query_argsTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
      public query_argsTupleScheme getScheme() {
        return new query_argsTupleScheme();
      }
    }

    private static class query_argsTupleScheme extends org.apache.thrift.scheme.TupleScheme<query_args> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, query_args struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
        java.util.BitSet optionals = new java.util.BitSet();
        if (struct.isSetJdbcOperationRef()) {
          optionals.set(0);
        }
        oprot.writeBitSet(optionals, 1);
        if (struct.isSetJdbcOperationRef()) {
          struct.jdbcOperationRef.write(oprot);
        }
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, query_args struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
        java.util.BitSet incoming = iprot.readBitSet(1);
        if (incoming.get(0)) {
          struct.jdbcOperationRef = new com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef();
          struct.jdbcOperationRef.read(iprot);
          struct.setJdbcOperationRefIsSet(true);
        }
      }
    }

    private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
      return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
    }
  }

  public static class query_result implements org.apache.thrift.TBase<query_result, query_result._Fields>, java.io.Serializable, Cloneable, Comparable<query_result>   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("query_result");

    private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRUCT, (short)0);

    private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new query_resultStandardSchemeFactory();
    private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new query_resultTupleSchemeFactory();

    public @org.apache.thrift.annotation.Nullable com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef success; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      SUCCESS((short)0, "success");

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
          case 0: // SUCCESS
            return SUCCESS;
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
      tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef.class)));
      metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(query_result.class, metaDataMap);
    }

    public query_result() {
    }

    public query_result(
      com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef success)
    {
      this();
      this.success = success;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public query_result(query_result other) {
      if (other.isSetSuccess()) {
        this.success = new com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef(other.success);
      }
    }

    public query_result deepCopy() {
      return new query_result(this);
    }

    @Override
    public void clear() {
      this.success = null;
    }

    @org.apache.thrift.annotation.Nullable
    public com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef getSuccess() {
      return this.success;
    }

    public query_result setSuccess(@org.apache.thrift.annotation.Nullable com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef success) {
      this.success = success;
      return this;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    /** Returns true if field success is set (has been assigned a value) and false otherwise */
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
      switch (field) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef)value);
        }
        break;

      }
    }

    @org.apache.thrift.annotation.Nullable
    public java.lang.Object getFieldValue(_Fields field) {
      switch (field) {
      case SUCCESS:
        return getSuccess();

      }
      throw new java.lang.IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new java.lang.IllegalArgumentException();
      }

      switch (field) {
      case SUCCESS:
        return isSetSuccess();
      }
      throw new java.lang.IllegalStateException();
    }

    @Override
    public boolean equals(java.lang.Object that) {
      if (that instanceof query_result)
        return this.equals((query_result)that);
      return false;
    }

    public boolean equals(query_result that) {
      if (that == null)
        return false;
      if (this == that)
        return true;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      int hashCode = 1;

      hashCode = hashCode * 8191 + ((isSetSuccess()) ? 131071 : 524287);
      if (isSetSuccess())
        hashCode = hashCode * 8191 + success.hashCode();

      return hashCode;
    }

    @Override
    public int compareTo(query_result other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;

      lastComparison = java.lang.Boolean.compare(isSetSuccess(), other.isSetSuccess());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetSuccess()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
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
      java.lang.StringBuilder sb = new java.lang.StringBuilder("query_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
      // check for sub-struct validity
      if (success != null) {
        success.validate();
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

    private static class query_resultStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
      public query_resultStandardScheme getScheme() {
        return new query_resultStandardScheme();
      }
    }

    private static class query_resultStandardScheme extends org.apache.thrift.scheme.StandardScheme<query_result> {

      public void read(org.apache.thrift.protocol.TProtocol iprot, query_result struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TField schemeField;
        iprot.readStructBegin();
        while (true)
        {
          schemeField = iprot.readFieldBegin();
          if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
            break;
          }
          switch (schemeField.id) {
            case 0: // SUCCESS
              if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
                struct.success = new com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef();
                struct.success.read(iprot);
                struct.setSuccessIsSet(true);
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

      public void write(org.apache.thrift.protocol.TProtocol oprot, query_result struct) throws org.apache.thrift.TException {
        struct.validate();

        oprot.writeStructBegin(STRUCT_DESC);
        if (struct.success != null) {
          oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
          struct.success.write(oprot);
          oprot.writeFieldEnd();
        }
        oprot.writeFieldStop();
        oprot.writeStructEnd();
      }

    }

    private static class query_resultTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
      public query_resultTupleScheme getScheme() {
        return new query_resultTupleScheme();
      }
    }

    private static class query_resultTupleScheme extends org.apache.thrift.scheme.TupleScheme<query_result> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, query_result struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
        java.util.BitSet optionals = new java.util.BitSet();
        if (struct.isSetSuccess()) {
          optionals.set(0);
        }
        oprot.writeBitSet(optionals, 1);
        if (struct.isSetSuccess()) {
          struct.success.write(oprot);
        }
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, query_result struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
        java.util.BitSet incoming = iprot.readBitSet(1);
        if (incoming.get(0)) {
          struct.success = new com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef();
          struct.success.read(iprot);
          struct.setSuccessIsSet(true);
        }
      }
    }

    private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
      return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
    }
  }

}
